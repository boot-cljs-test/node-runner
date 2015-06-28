(ns boot-cljs-test.node-runner
  (:require [clojure.java.io :as io]
            [boot.core :as core :refer [deftask]]
            [boot.util :as util :refer [sh]]
            [boot.file :as file]
            [clostache.parser :refer [render render-resource]]))

(defn required-ns
  "Receives a list of namespaces. Generates a string that can be fed
  to the placeholder inside `(:require ...)` form"
  [namespaces]
  (->> (map #(str "[" % "]") namespaces)
       (clojure.string/join "\n   ")))

(defn tested-ns
  "Receives a list of namespaces. Generates a string that can be fed
  to the placeholder inside `(run-test ...)` form"
  [namespaces]
  (->> (map #(str "'" %) namespaces)
       (clojure.string/join " ")))

(defn mk-parents [file]
  (-> file .getParent io/file .mkdirs))

(deftask cljs-test-node-runner
  "Automatically produces:

  - a Clojurescript source that will run all tests in given `namespaces` list.
  - an EDN file to instruct `boot-cljs` to build the file targeting Nodejs.

  Should be called before `boot-cljs` task."
  [n namespaces NS #{sym} "Namespaces whose tests will be run."]
  (let [templates ["boot_cljs_test/node_runner.cljs"
                   "cljs_test_node_runner.cljs.edn"]
        test-dir (core/tmp-dir!)]
    (core/with-pre-wrap fileset
      (file/empty-dir! test-dir)
      (doseq [template templates
              :let [data {:required-ns (required-ns namespaces)
                          :tested-ns (tested-ns namespaces)}
                    output (io/file test-dir template)
                    content (render-resource template data)]]
        (mk-parents output)
        (spit output content))
      (-> fileset
          (core/add-source test-dir)
          (core/commit!)))))

(deftask run-cljs-test
  "Run the script produced by `cljs-test-node-runner` in
  Nodejs. Should be called after `boot-cljs` task."
  [c node-cmd str "Custom Node command. Default: 'node'"]
  (fn middleware [next-handler]
    (fn handler [fileset]
      (sh (or node-cmd "node") "target/cljs_test_node_runner.js")
      (-> fileset next-handler))))

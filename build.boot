(set-env!
 :resource-paths #{"resources"}
 :source-paths   #{"src"}
 :dependencies   '[[de.ubercode.clostache/clostache "1.4.0"]
                   [adzerk/bootlaces "0.1.10" :scope "test"]])

(require '[adzerk.bootlaces :refer :all])

(def +version+ "0.1.0")

(bootlaces! +version+)

(task-options!
 pom {:project     'boot-cljs-test/node-runner
      :version     +version+
      :description "Boot task producing a script to test ClojureScript
      namespaces in NodeJs."
      :url         "https://github.com/boot-cljs-test/node-runner"
      :scm         {:url "https://github.com/boot-cljs-test/node-runner"}
      :license     {"Eclipse Public License"
                    "http://www.eclipse.org/legal/epl-v10.html"}})

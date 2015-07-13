# boot-cljs-test/node-runner

```clj
[boot-cljs-test.node-runner "0.1.0"]
```

Boot task that automatically generate a ClojureScript test runner
script that works on Nodejs.

## Usage

Add `boot-cljs-test/node-runner` to your `build.boot` dependencies and
`require` the namespace:

```clj
(set-env! :dependencies '[[boot-cljs-test/node-runner "X.Y.Z" :scope "test"]])
(require '[boot-cljs-test/node-runner :refer :all])
```

You can see the options available on the command line:

```bash
$ boot cljs-test-node-runner -h
```

or in the REPL:

```bash
boot.user=> (doc cljs-test-node-runner)
```

## Setup

```clj
(deftask cljs-auto-test []
  (comp (watch)
        (speak)
        (cljs-test-node-runner :namespaces '[foo.core-test bar.util-test]) ;; put it before `cljs` task
        (cljs :source-map true
              :optimizations :none)
        (run-cljs-test) ;; put it after `cljs` task
))
```

## License

Copyright © 2015 Hoang Minh Thang

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

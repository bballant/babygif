(defproject babygif "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2322"]
                 [clj-time "0.8.0"]
                 [compojure "1.1.8"]
                 [domina "1.0.2"]
                 [jayq "2.5.2"]
                 [hiccup "1.0.5"]]
  :plugins [[lein-cljsbuild "1.0.3"]
            [lein-ring "0.8.11"]]
  :source-paths ["src/clj"]
  :ring {:handler babygif.handler/app}
  :cljsbuild {
    :builds [{:source-paths ["src/cljs"],
              :compiler {
                         :output-to "resources/public/hockey.js",
                         :optimization :whitespace,
                         :pretty-print true}}]}
  :profiles
    {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                          [ring-mock "0.1.5"]]}})

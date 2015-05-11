(defproject panpan "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.namespace "0.2.10"]
                 [org.clojure/data.zip "0.1.1"]
                 [clj-http "1.1.2"]
                 [clj-docomo-dialogue "0.0.2"]
                 [jubot "0.1.1"]]

  :uberjar-name "panpan-standalone.jar"
  :min-lein-version "2.0.0"
  :profiles {:dev {:dependencies
                   [[org.clojars.runa/conjure "2.2.0"]]
                   :source-paths ["dev"]
                   }}

  :aliases {"dev" ["run" "-m" "panpan.core/-main"]})

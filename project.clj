(defproject hnclient "0.1.0-SNAPSHOT"
  :description "Simple Hacker News command line client"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [clj-http "3.0.1"]
                 [org.clojure/data.json "0.2.6"]
                 [clojure-lanterna "0.9.4"]]
  :main ^:skip-aot hnclient.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

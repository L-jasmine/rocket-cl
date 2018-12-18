(defproject rocket-cl "0.1.0-SNAPSHOT"
  :description "a rocketmq client for clojure"
  :url "https://github.com/L-jasmine/rocket-cl"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.apache.rocketmq/rocketmq-client "4.3.2"]]
  :main ^:skip-aot rocket-cl.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

(ns rocket-cl.producer
  (:import
    [org.apache.rocketmq.client.producer DefaultMQProducer SendCallback SendResult]
    [org.apache.rocketmq.common.message Message]
    [org.apache.rocketmq.remoting.common RemotingHelper]))

(defn producer
"-------------------------
type    producer's type
:default
name    producer's name
server  producer's server (coll or string)

ex:
  (defproducer \"producername\" :default [\"127.0.0.1:8888\" \"127.0.0.2:8888\"])
  (defproducer \"producername\" :default \"127.0.0.1:8888;127.0.0.2:8888\")
 -------------------------"
  [name type server]
  (doto
  (case type
    :default (DefaultMQProducer. name)
    (throw (IllegalArgumentException. "Unsupported producer type!")))
  (.setNamesrvAddr
    (if (string? server)
    server (clojure.string/join ";" server)))
  .start))


(defn send!
  [producer {:keys [^String topic
                    ^String tags
                    ^String keys
                    ^int flag
                    wait]} body]
  (.send producer
    (Message. topic
      (or tags "")
      (or keys "")
      (or flag 0)
      body
      (if wait true false))))

(defn close! [producer]
  (.shutdown producer))

(def t (producer "cshtestp" :default "192.168.1.29:9876"))
(dotimes [n 100]
  (send! t {:topic "testtop" :tag "testtag"} (.getBytes (str "hello-" n))))
(close! t)

(ns rocket-cl.consumer
  (:import [org.apache.rocketmq.client.consumer DefaultMQPushConsumer]
           [org.apache.rocketmq.client.consumer.listener
            ConsumeConcurrentlyContext
            ConsumeConcurrentlyStatus
            MessageListenerConcurrently]
           [org.apache.rocketmq.client.exception MQClientException]
           [org.apache.rocketmq.common.consumer ConsumeFromWhere]
           [org.apache.rocketmq.common.message MessageExt]))


(defn pushconsumer
  "-------------------
  -------------------"
  [name server {tag :tag topic :topic from :consume-from} listener-fn & [consume-timestamp]]
  (let [consumer (DefaultMQPushConsumer. name)]
    (doto consumer
      (.setNamesrvAddr
        (if (string? server)
        server (clojure.string/join ";" server)))
      (.subscribe topic tag)
      (.setConsumeFromWhere
        (case from
          :last ConsumeFromWhere/CONSUME_FROM_LAST_OFFSET
          :first ConsumeFromWhere/CONSUME_FROM_FIRST_OFFSET
          :timestamp ConsumeFromWhere/CONSUME_FROM_TIMESTAMP
          (throw (IllegalArgumentException. "Unsupported consumer consume-from!"))))
    )
    (if (and (= from :timestamp) consume-timestamp)
      (.setConsumeTimestamp consumer consume-timestamp))
    (.registerMessageListener consumer
      (reify
        MessageListenerConcurrently
        (consumeMessage [this msgs context]
          (if (listener-fn this msgs context)
            ConsumeConcurrentlyStatus/CONSUME_SUCCESS
            ConsumeConcurrentlyStatus/RECONSUME_LATER
          )))
    )
    (.start consumer)
    consumer
  )
)

(defn close! [consumer]
  (.shutdown consumer))

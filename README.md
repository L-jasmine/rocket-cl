# rocket-cl

一个rocketmq-client的简单封装

a rocketmq-client for clojure

## Installation

clone from https://github.com/L-jasmine/rocket-cl.

## Usage


## Options


## Examples
```clojure
(def c (pushconsumer "testsumer" "127.0.0.1:9876"
{:tag "*" :topic "testtop" :consume-from :last}
  (fn [this msgs context]
    (prn "msg:" (vec (map #(String. (.getBody %)) msgs)))
    true
  )
))

(close! c)

(def p (producer "cshtestp" :default "127.0.0.1:9876"))
(dotimes [n 100]
  (send! p {:topic "testtop" :tag "testtag"} (.getBytes (str "hello-" n))))
(close! p)

```
### Bugs

...

### Any Other Sections
### That You Think
### Might be Useful

## License

Copyright © 2018 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

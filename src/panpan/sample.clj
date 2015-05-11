(ns panpan.sample
  (:require
    [clojure.string  :as str]
    [jubot.handler   :as handler]
    [jubot.scheduler :as scheduler]
    [jubot.brain     :as brain]))

(defn ping-handler
  "p ping - pong"
  [{:keys [text message-for-me?]}]
  (when (and message-for-me? (= text "ping")) "pong"))

(defn- brain-key-values
  [& _]
  (let [ks          (brain/keys)
        key-max-len (apply max (map count ks))
        make-spaces #(str/join "" (repeat (- key-max-len (count %)) " "))
        ]
    (->> ks
         (reduce
           (fn [res k]
             (conj res (str " * " k (make-spaces k) " : " (brain/get k))))
           [])
         (str/join "\n")
         (str "Key/Value\n"))))

(defn brain-handler
  [{:keys [message-for-me?] :as arg}]
  (when message-for-me?
    (handler/regexp arg
      #"^set (.+?) (.+?)$" (fn [{[_ k v] :match}] (brain/set k v) "OK")
      #"^get (.+?)$"       (fn [{[_ k]   :match}] (brain/get k))
      #"^keys$"            (fn [& _]
                             (->> (brain/keys)
                                  (map #(str " * " %))
                                  (concat ["Keys:"])
                                  (str/join "\n")))
      #"^brain$"           brain-key-values
      )))

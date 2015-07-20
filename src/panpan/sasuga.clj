(ns panpan.sasuga
  (:require
    [clojure.string  :as str]
    [jubot.scheduler :as js]
    [jubot.handler   :as jh]
    [jubot.brain     :as jb]))

(def ^:const SASUGA_KEY "sasugaore")

(defn- get-sasuga-count
  []
  (if-let [i (jb/get SASUGA_KEY)] i 0))

(defn sasuga-handler
  [{:keys [user] :as arg}]
  (jh/regexp arg
    #"^sasuga"
    (fn [& _]
      (let [i (get-sasuga-count)]
        (jb/set SASUGA_KEY (inc i))
        (str "さすが俺" (str/join (repeat i "！")))))))

(def sasuga-schedule
  (js/schedules
    "0 0 9 * * * *"
    (fn [& _]
      (let [i (get-sasuga-count)]
        (jb/set SASUGA_KEY 0)
        (str "昨日は " i "さすが だったよ")))))

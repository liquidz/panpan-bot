(ns panpan.reminder
  (:require
    [jubot.scheduler :as scheduler]))

(def remind-schedule
  (scheduler/schedules
    "0 28 18 6 * * *"
    #(str "青空レストラン始まるよ")

    "0 58 18 7 * * *"
    #(str "鉄腕ダッシュ始まるよ")))



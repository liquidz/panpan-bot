(ns panpan.reminder
  (:require
    [jubot.adapter   :as adapter]
    [jubot.scheduler :as scheduler]))

(def remind-schedule
  (scheduler/schedules
    "0 25 18 6 * * *"
    #(adapter/out "青空レストラン始まるよ")

    "0 55 18 7 * * *"
    #(adapter/out "鉄腕ダッシュ始まるよ")
    ))



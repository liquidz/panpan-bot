(ns panpan.reminder
  (:require
    [jubot.adapter   :as adapter]
    [jubot.scheduler :as scheduler]))

(def remind-schedule
  (scheduler/schedules
    "0 5 12 * * * *"
    #(adapter/out "uochan 住所確認した？")

    "0 28 18 6 * * *"
    #(adapter/out "青空レストラン始まるよ")

    "0 58 16 7 * * *"
    #(adapter/out "七つの大罪始まるよ")

    "0 58 18 7 * * *"
    #(adapter/out "鉄腕ダッシュ始まるよ")))



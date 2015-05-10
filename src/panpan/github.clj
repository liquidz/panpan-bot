(ns panpan.github
  (:require
    [jubot.adapter   :as ja]
    [jubot.scheduler :as js]
    [jubot.util.rss  :as rss]))

(def ^:const SCHEDULE "0 /1 * * * * *")
(def ^:const NAME "エイナ")
(def ^:const RSS_URL "https://github.com/liquidz.atom")
(def ^:const ICON "https://dl.dropboxusercontent.com/u/14918307/slack_icon/eina.png")
(def ^:private out (do (ja/out (apply str %&) :as NAME :icon-url ICON) nil))

(def github-schedule
  (scheduler/schedules
    SCHEDULE
    #(let [feeds (rss/get-unread-feeds URL)]
       (when-not (empty? feeds)
         (out "OK")))))

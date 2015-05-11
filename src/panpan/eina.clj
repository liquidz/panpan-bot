(ns panpan.eina
  (:require
    [panpan.github.rss :as rss]
    [jubot.adapter     :as ja]
    [jubot.scheduler   :as js]
    [jubot.handler     :as jh]
    [jubot.brain       :as jb]))

(def ^:const SCHEDULE "0 /1 * * * * *")
(def ^:const NAME "エイナ")
(def ^:const ICON "https://dl.dropboxusercontent.com/u/14918307/slack_icon/eina.png")
(def ^:private out #(do (ja/out (apply str %&) :as NAME :icon-url ICON) nil))

(def ^:const MESSAGES
  {:open-pull-request
   ["新しいクエストがきてるよ"
    "クエストだからって無理しちゃダメだぞ"
    "今日はこんなクエストあるけどどう？"
    "今日もクエスト？頑張ってね"
    ]
   :merge-pull-request
   ["クエストお疲れ様(^^)"
    "クエスト完了ね"
    "無事完了。流石ね！"
    "もうクエスト終わったの！？"
    "お疲れ様。少し休んだら？"
    ]
   :open-issue
   ["ミッションよ"
    "このミッションお願いできない？"
    ]
   :comment-issue
   ["頑張ってるみたいね(^^)"
    "無理していない？"
    "あら？追加情報かしら？"
    ]
   :close-issue
   ["ありがとう。助かったわ"
    "ミッション、本当にお疲れ様"
    ]})

(def github-schedule
  (js/schedules
    SCHEDULE
    #(some->> (rss/get-github-event)
              :event
              (get MESSAGES)
              rand-nth
              out)))

(defn eina-handler
  [{:keys [text]}]
  (when (re-find #"エイナ(さん)?.+github.+テスト" text)
    (jb/set rss/RSS_URL nil)
    (doseq [feed (rss/get-github-feeds)]
      (out (rss/github-respond feed)))

    (jb/set rss/RSS_URL nil)
    ((first github-schedule))))


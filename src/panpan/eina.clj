(ns panpan.eina
  (:require
    [clojure.string       :as str]
    [panpan.github.feed   :as feed]
    [panpan.github.search :as search]
    [jubot.adapter        :as ja]
    [jubot.scheduler      :as js]
    [jubot.handler        :as jh]
    [jubot.brain          :as jb]))

(def ^:const SCHEDULE "0 /1 * * * * *")
(def ^:const NAME "エイナ")
(def ^:const ICON "https://dl.dropboxusercontent.com/u/14918307/slack_icon/eina.png")
(def ^:private out #(do (ja/out (apply str %&) :as NAME :icon-url ICON) nil))
(def ^:private pre #(str "```\n" % "\n```"))

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
    ]
   :thanks
   ["いーえ(^^)"
    "どういたしまして(^^)"
    ]
   :reply
   ["はーい"
    "ちょっと待ってね"
    ]
   })

(def github-schedule
  (js/schedules
    SCHEDULE
    #(some->> (feed/get-github-event)
              :event
              (get MESSAGES)
              rand-nth
              out)))

(defn eina-handler
  "エイナ(さん)?.*クエスト.*(教えて|おしえて) - github のクエスト一覧
   エイナ(さん)?.*github.*テスト              - github rss 取得のテスト
  "
  [{:keys [user] :as arg}]
  (jh/regexp arg
    #"エイナ(さん)?.*ありがと"
    (fn [& _]
      (->> MESSAGES :thanks rand-nth (out "@" user " ")))
    #"エイナ(さん)?.*クエスト.*(教えて|おしえて)"
    (fn [& _]
      (->> MESSAGES :reply rand-nth (out "@" user " "))
      (->> (search/get-assigned-issues "liquidz" :state "open")
           (map #(str " * <" (:url %) "|" (:title %) ">"))
           (str/join "\n")
           out))
    #"エイナ(さん)?.*github.*テスト"
    (fn [& _]
      (->> MESSAGES :reply rand-nth (out "@" user " "))
      (jb/set feed/FEED_URL nil)
      (->> (feed/get-github-feeds)
           (map feed/github-respond)
           (str/join "\n")
           pre
           out)

      (jb/set feed/FEED_URL nil)
      ((first github-schedule)))))


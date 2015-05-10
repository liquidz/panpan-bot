(ns panpan.github
  (:require
    [jubot.brain :as jb] ; FIXME
    [jubot.adapter   :as ja]
    [jubot.scheduler :as js]
    [jubot.handler   :as jh]
    [panpan.util.rss :as rss]))

(def ^:const SCHEDULE "0 /1 * * * * *")
(def ^:const NAME "エイナ(ギルド)")
(def ^:const RSS_URL "https://github.com/liquidz.atom")
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
   ["クエストお疲れ様"
    "クエスト完了ね"
    "無事完了。流石ね"
    "もうクエスト終わったの！？"
    "お疲れ様。少し休んだら？"
    ]
   :open-issue
   ["ミッションよ"
    "このミッションお願いできない？"
    ]
   :comment-issue
   ["頑張ってるみたいね"
    "無理していない？"
    "あら？追加情報かしら？"
    ]
   :close-issue
   ["ありがとう。助かったわ"
    "ミッション、本当にお疲れ様"
    ]})

(defmacro matchfn
  [bind & body]
  `(fn [{[_# ~@bind] :match}] ~@body))

(defn github-respond
  [{:keys [title]}]
  (jh/regexp {:text title}
    #"opened issue (.+)#(\d+)$"
    (matchfn [repo issue] (out (-> MESSAGES :open-issue rand-nth)))

    #"closed issue (.+)#(\d+)$"
    (matchfn [repo issue] (out (-> MESSAGES :close-issue rand-nth)))

    #"commented on issue (.+)(#\d+)$"
    (matchfn [repo issue] (out (-> MESSAGES :comment-issue rand-nth)))

    #"opened pull request (.+)(#\d+)$"
    (matchfn [repo issue] (out (-> MESSAGES :open-pull-request rand-nth)))

    #"merged pull request (.+)(#\d+)$"
    (matchfn [repo issue] (out (-> MESSAGES :merge-pull-request rand-nth)))
    ;#"created branch (.+) at (.+)$"
    ;(matchfn [branch repo] (str "NEW BRANCH " repo " " branch))
    ;#"pushed to (.+?) at (.+)$"
    ;(matchfn [branch repo] (str "PUSH " repo " " branch))
    ;#"deleted branch (.+) at (.+)$"
    ;(matchfn [branch repo] (str "DEL BRANCH " repo " " branch))
    ))

;(defn github-handler
;  [{:keys [text]}]
;  (when (re-find #"github" text)
;    (jb/set RSS_URL nil)
;    (doseq [entry (rss/get-unread-feeds RSS_URL)]
;      (respond entry))))

(def github-schedule
  (js/schedules
    SCHEDULE
    #(some-> (rss/get-unread-feeds RSS_URL)
             seq first github-respond)))

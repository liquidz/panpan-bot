(ns panpan.liliruca
  (:require
    [jubot.adapter        :as ja]
    [jubot.handler        :as jh]
    [jubot.scheduler      :as js]
    [panpan.util.match    :refer :all]
    [panpan.jubot.version :refer :all]
    [panpan.trello.api    :as trello]))

(def ^:private private-todo (System/getenv "TRELLO_PRIVATE_TODO"))
(def ^:private work-todo (System/getenv "TRELLO_WORK_TODO"))

(def ^:const NAME "リリ")
(def ^:const ICON "https://dl.dropboxusercontent.com/u/14918307/slack_icon/liliruca.png")
(def ^:private out #(do (ja/out (apply str %&) :as NAME :icon-url ICON) nil))
(def ^:const MESSAGES
  {:jubot-document
   ["jubot のドキュメントが古いようですよ"
    "あっ、jubot のドキュメント古くないですか？"
    ]
   :jubot-template
   ["jubot のテンプレートが古いみたいです"
    "jubot のテンプレート古くないですか？"
    ]
   :thanks
   ["どういたしまして！"
    "滅相もないです"
    "サポーターですから！"
    ]
   :add-task
   ["予定にいれておきますね"
    "あ、それ登録しておきますね"
    "早速登録しておきましたよ"
    ]
   })

(defn liliruca-handler
  "^task\\s+(.+?)$     - 個人タスクに追加
   ^work\\s+(.+?)$     - ワークタスクに追加
   リリ.*jubot.+テスト - jubot ドキュメントバージョンチェックのテスト
  "
  [{:keys [user] :as arg}]
  (jh/regexp arg
    #"^task\s+(.+?)$"
    (matchfn [text]
      (when (trello/add-card private-todo text)
        (->> MESSAGES :add-task rand-nth (out "@" user " "))))
    #"^work\s+(.+?)$"
    (matchfn [text]
      (when (trello/add-card work-todo text)
        (->> MESSAGES :add-task rand-nth (out "@" user " "))))

    #"リリ.*jubot.+テスト"
    (fn [& _]
      (out "@" user " 了解です")
      (out "```\n"
           "core    : " (get-jubot-core-version) "\n"
           "document: " (get-jubot-document-version) "\n"
           "template: " (get-jubot-template-version) "\n"
           "```"))
    #"リリ.*ありがと"
    (fn [& _] (->> MESSAGES :thanks rand-nth (out "@" user " ")))))

(def liliruca-schedule
  (js/schedules
    ;; jubot document
    "0 0 20,21,22 * * * *"
    #(let [jc (get-jubot-core-version)
           jd (get-jubot-document-version)
           jt (get-jubot-template-version)]
       (when (not= jc jd)
         (out (-> MESSAGES :jubot-document rand-nth)))
       (when (not= jc jt)
         (out (-> MESSAGES :jubot-template rand-nth))))))

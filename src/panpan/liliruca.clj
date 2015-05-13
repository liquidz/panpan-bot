(ns panpan.liliruca
  (:require
    [jubot.adapter        :as ja]
    [jubot.handler        :as jh]
    [jubot.scheduler      :as js]
    [panpan.jubot.version :refer :all]))

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
   })

(defn liliruca-handler
  "リリ.*jubot.+テスト - jubot ドキュメントバージョンチェックのテスト
  "
  [{:keys [user] :as arg}]
  (jh/regexp arg
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

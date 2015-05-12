(ns panpan.liliruca
  (:require
    [jubot.adapter    :as ja]
    [jubot.handler    :as jh]
    [jubot.scheduler  :as js]
    [panpan.jubot.doc :refer :all]))

(def ^:const NAME "リリ")
(def ^:const ICON "https://dl.dropboxusercontent.com/u/14918307/slack_icon/liliruca.png")
(def ^:private out #(do (ja/out (apply str %&) :as NAME :icon-url ICON) nil))
(def ^:const MESSAGES
  {:jubot-document
   ["jubot のドキュメントが古いようですよ"
    "あっ、jubot のドキュメント古くないですか？"
    ]
   :response
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
           "document: " (get-jubot-document-version) "\n"
           "core    : " (get-jubot-core-version) "\n```"))
    #"リリ.*ありがと"
    (fn [& _] (->> MESSAGES :response rand-nth (out "@" user " ")))))

(def liliruca-schedule
  (js/schedules
    ;; jubot document
    "0 0 20,21,22 * * * *"
    #(when-not (is-document-latest?)
       (out (-> MESSAGES :jubot-document rand-nth)))))

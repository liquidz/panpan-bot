(ns panpan.syr
  (:require
    [jubot.adapter      :as ja]
    [jubot.scheduler    :as js]
    [jubot.handler      :as jh]
    [panpan.vim.version :as vv]))

(def ^:const NAME "シル")
(def ^:const ICON "https://dl.dropboxusercontent.com/u/14918307/slack_icon/syr.png")
(def ^:private out #(do (ja/out (apply str %&) :as NAME :icon-url ICON) nil))

(def ^:const MESSAGES
  {:new-version
   ["Vim の新しいパッチがきているようですよ"
    "知ってます？Vim のパッチがきてますよ"
    "あっ、Vimのパッチがきてるみたいです"
    ]
   :no-new-version
   ["新しいパッチは無いみたいです"
    "ごめんなさい。新しいパッチないです"
    "んー、特に新しいパッチはないですね"
    ]
   :thanks
   ["どういたしまして(^^)"
    "いえいえ(^^)"
    "役に立ったなら良かったです！"
    "お役に立ててなりよりです！"
    ]
   :reply
   ["ちょっと待ってくださいね"
    "少々お待ちください〜"
    ]
   })

(defn- generate-message
  []
  (let [res (vv/check-vim-version)]
    (when (:latest? res)
      (str "```\n"
           "Version: " (:version res) "\n"
           "Message: " (:message res) "\n"
           "```"))))

(defn syr-handler
  "シル(さん)?.*(vim|Vim).*テスト - Vim のバージョンチェックテスト
  "
  [{:keys [user] :as arg}]
  (jh/regexp arg
    #"シル(さん)?.*ありがと"
    (fn [& _] (->> MESSAGES :thanks rand-nth (out "@" user " ")))

    #"シル(さん)?.*(vim|Vim).*テスト"
    (fn [& _]
      (->> MESSAGES :reply rand-nth (out "@" user " "))
      (vv/initialize)
      (some-> (generate-message) out))))

(def syr-schedule
  (js/schedules
    ;; vim-version check
    "0 0 10,13,16,19,22 * * * *"
    #(when-let [msg (generate-message)]
       (-> MESSAGES :new-version rand-nth (out "\n" msg)))))

(ns panpan.vim
  (:require
    [clojure.string  :as str]
    [clj-http.client :as client]
    [jubot.adapter   :as ja]
    [jubot.brain     :as jb]
    [jubot.scheduler :as js]
    [jubot.handler   :as jh]))

(def ^:const NAME "シル")
(def ^:const ICON "https://dl.dropboxusercontent.com/u/14918307/slack_icon/syr.png")
(def ^:const URL  "http://ftp.vim.org/pub/vim/patches/7.4/README")
(def ^:const KEY  "vim-patch")
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
   :response
   ["どういたしまして(^^)"
    "いえいえ(^^)"
    "役に立ったなら良かったです！"
    "お役に立ててなりよりです！"
    ]
   })

(defn get-latest-patch
  []
  (-> URL client/get :body str/split-lines last str/trim
      (str/split #"\s+" 3)))

(defn check-vim-version
  [& {:keys [user else?]}]
  (let [[_ version message] (get-latest-patch)
        last-version        (jb/get KEY)]
    (if (not= version last-version)
      (do (jb/set KEY version)
          (out (if user (str "@" user " ") "")
               (-> MESSAGES :new-version rand-nth)
               "\n" version ": " message))
      (when else?
        (out (if user (str "@" user " ") "")
             (-> MESSAGES :no-new-version rand-nth))))))

(defn vim-version-handler
  [{:keys [user] :as arg}]
  (jh/regexp arg
    #"シル(さん)?.+ありがと"
    (fn [& _] (->> MESSAGES :response rand-nth (out "@" user " ")))

    #"シル(さん)?.+(vim|Vim)"
    (fn [& _] (check-vim-version :user user :else? true))))

(def vim-version-check-schedule
  (js/schedules
    "0 0 10,13,16,19,22 * * * *"
    check-vim-version))

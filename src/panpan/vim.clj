(ns panpan.vim
  (:require
    [clojure.string  :as str]
    [clj-http.client :as client]
    [jubot.adapter   :as ja]
    [jubot.brain     :as jb]
    [jubot.scheduler :as js]))

(def ^:const NAME "シル")
(def ^:const ICON "https://dl.dropboxusercontent.com/u/14918307/slack_icon/syr.png")
(def ^:const URL  "http://ftp.vim.org/pub/vim/patches/7.4/README")
(def ^:const KEY  "vim-patch")
(def ^:private out #(do (ja/out (apply str %&) :as NAME :icon-url ICON) nil))

(def ^:const MESSAGES
  ["Vim の新しいパッチがきているようですよ"
   "知ってます？Vim のパッチがきてますよ"
   "あっ、Vimのパッチがきてるみたいです"
   ])

(defn get-latest-patch
  []
  (-> URL client/get :body str/split-lines last str/trim
      (str/split #"\s+" 3)))

(defn check-vim-version
  []
  (let [[_ version message] (get-latest-patch)
        last-version        (jb/get KEY)]
    (when (not= version last-version)
      (jb/set KEY version)
      (out (rand-nth MESSAGES)
           "\n" version ": " message))))

;(defn vim-version-handler
;  [{text :text}]
;  (when (re-find #"vim" text)
;    (check-vim-version)))

(def vim-version-check-schedule
  (js/schedules
    "0 0 10,13,16,19,22 * * * *"
    check-vim-version))

(ns panpan.vim.version
  (:require
    [clojure.string  :as str]
    [clj-http.client :as client]
    [jubot.brain     :as jb]))

(def ^:const URL  "http://ftp.vim.org/pub/vim/patches/7.4/README")
(def ^:const KEY  "vim-patch")

(defn get-latest-patch
  []
  (-> URL client/get :body str/split-lines last str/trim
      (str/split #"\s+" 3)))

(defn check-vim-version
  [& {:keys [user else?]}]
  (let [[_ ver msg]  (get-latest-patch)
        last-ver     (jb/get KEY)
        latest?      (not= ver last-ver)]
    (when latest? (jb/set KEY ver))
    {:latest? latest?
     :version ver
     :message msg}))

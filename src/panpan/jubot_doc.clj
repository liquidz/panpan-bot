(ns panpan.jubot-doc
  (:require
    [jubot.adapter :as adapter]
    [jubot.scheduler :as scheduler]
    [clj-http.lite.client :as client]))

(def ^:const DOCUMENT_URL
  "http://liquidz.github.io/jubot/api/index.html")

(def ^:const CLOJARS_URL
  "https://clojars.org/jubot")

(def ^:const MESSAGES
  ["jubot のドキュメント古いよ！"])

(defn get-jubot-document-version
  []
  (->> DOCUMENT_URL
       client/get
       :body
       (re-find #"<title>Jubot (.+?) API documentation</title>")
       second))

(defn get-jubot-core-version
  []
  (->> CLOJARS_URL
       client/get
       :body
       (re-find #"<title>jubot (.+?) - Clojars</title>")
       second))

(defn latest-jubot-document?
  []
  (let [dv (get-jubot-document-version)
        jv (get-jubot-core-version)]
    (= dv jv)))

(def jubot-document-version-check-schedule
  (scheduler/schedules
    "0 0 * * * * *" 
    #(when-not (latest-jubot-document?)
      (adapter/out (str "uochan " (rand-nth MESSAGES))))))

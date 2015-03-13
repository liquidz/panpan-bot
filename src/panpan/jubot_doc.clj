(ns panpan.jubot-doc
  (:require
    [jubot.scheduler :as scheduler]
    [clj-http.client :as client]))

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

(def jubot-document-version-check-schedule
  (scheduler/schedules
    "0 0 20,21,22 * * * *"
    #(let [dv (get-jubot-document-version)
           jv (get-jubot-core-version)]
       (when (not= dv jv)
         (str "uochan " (rand-nth MESSAGES) "\n"
              "  ドキュメントのバージョン: " dv "\n"
              "  jubot本体のバージョン   : " jv)))))

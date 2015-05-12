(ns panpan.jubot.doc
  (:require
    [clj-http.client :as client]))

(def ^:const DOCUMENT_URL
  "http://liquidz.github.io/jubot/api/index.html")

(def ^:const CLOJARS_URL
  "https://clojars.org/jubot")

(defn get-jubot-document-version
  []
  (some->> DOCUMENT_URL
           client/get
           :body
           (re-find #"<title>Jubot (.+?) API documentation</title>")
           second))

(defn get-jubot-core-version
  []
  (some->> CLOJARS_URL
           client/get
           :body
           (re-find #"<title>jubot (.+?) - Clojars</title>")
           second))

(defn is-document-latest?
  []
  (= (get-jubot-document-version)
     (get-jubot-core-version)))

(ns panpan.jubot.version
  (:require
    [clj-http.client :as client]))

(def ^:const DOCUMENT_URL "http://liquidz.github.io/jubot/api/index.html")
(def ^:const CLOJARS_CORE_URL "https://clojars.org/jubot")
(def ^:const CLOJARS_TEMPLATE_URL "https://clojars.org/jubot/lein-template")
(defn- get-clojars-version
  [url]
  (some->> url client/get :body
           (re-find #"<title>[^ ]+ (.+?) - Clojars</title>")
           second))

(defn get-jubot-document-version
  []
  (some->> DOCUMENT_URL
           client/get
           :body
           (re-find #"<title>Jubot (.+?) API documentation</title>")
           second))

(def get-jubot-core-version
  (partial get-clojars-version CLOJARS_CORE_URL))

(def get-jubot-template-version
  (partial get-clojars-version CLOJARS_TEMPLATE_URL))

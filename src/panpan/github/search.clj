(ns panpan.github.search
  (:require
    [clojure.string    :as str]
    [clj-http.client   :as client]
    [clojure.data.json :as json]))

(def ^:const SEARCH_ISSUE
  "https://api.github.com/search/issues")

(def ^:const SEARCH_REPOSITORY
  "https://api.github.com/search/repositories")

(defn- parse-json
  [s]
  (json/read-str s :key-fn keyword))

(defn map->query
  [m]
  (->> (keys m)
       (map #(list (name %) (get m %)))
       (map #(str/join ":" %))
       (str/join "+")))

(defn get-assigned-issues
  [username & {:as option}]
  (some->> {:assignee username}
           (merge option)
           map->query
           (str SEARCH_ISSUE "?q=")
           client/get
           :body
           parse-json
           :items))

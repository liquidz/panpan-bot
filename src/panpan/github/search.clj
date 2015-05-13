(ns panpan.github.search
  (:require
    [clojure.string    :as str]
    [clj-http.client   :as client]
    [clojure.data.json :as json]))

(def ^:const SEARCH_API
  "https://api.github.com/search/issues")

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
           (str SEARCH_API "?q=")
           client/get
           :body
           parse-json
           :items))

(ns panpan.trello.api
  (:require
    [clj-http.client   :as client]
    [clojure.data.json :as json]))

(def ^:const API_URL "https://trello.com/1")
(def ^:private trello-key (System/getenv "TRELLO_KEY"))
(def ^:private trello-token (System/getenv "TRELLO_TOKEN"))

(defn call-get
  [url & {:as param}]
  (when (and trello-key trello-token)
    (-> url
        (client/get {:query-params (merge param {:key trello-key :token trello-token})})
        :body
        (json/read-str :key-fn keyword))))

(defn get-my-boards
  []
  (->> (str API_URL "/members/my/boards")
       call-get
       (remove #(:closed %))))

(defn get-lists
  [board-id]
  (-> (str API_URL "/boards/" board-id "/lists")
      call-get))

(defn add-card
  [list-id text]
  (when (and trello-key trello-token)
    (-> (str API_URL "/cards")
        (client/post {:form-params {:key trello-key :token trello-token
                                    :name text :idList list-id}})
        :status
        (= 200))))

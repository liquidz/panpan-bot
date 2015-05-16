(ns panpan.toggl.api
  (:require
    [clj-http.client   :as client]
    [clojure.data.json :as json]
    ;[clj-time.format :refer [formatter parse]]
    [clj-time.core :as c]
    [clj-time.format :as cf]
    [clj-time.coerce :as cc]
    ))


(def ^:const API_URL "https://www.toggl.com/api/v8/time_entries")
(def ^:const TOKEN_KEY "TOGGL_TOKEN")
(def ^:private token (System/getenv TOKEN_KEY))

(def  date-format
  (cf/formatter "yyyy-MM-dd'T'HH:mm:ssZZ"))

(defn call-api
  [url & {:keys [method body query]}]
  (let [header (-> {:basic-auth [token "api_token"] :content-type :json}
                   (merge (if body  {:body (json/write-str body)} {})
                          (if query {:query-params query} {})))
        method (case method
                 "POST"   client/post
                 "PUT"    client/put
                 "DELETE" client/delete
                 client/get)]
    (try
      (method url header)
      (catch Exception e
    ;    (.printStackTrace e)
        nil))))

(defn get-running-entry
  []
  (let [url  (str API_URL "/current")
        data (some-> (call-api url)
                     :body
                     (json/read-str :key-fn keyword)
                     :data)
        from (some-> data :start cf/parse cc/to-long)
        now  (-> (c/now) cc/to-long)]
    (when (and data from now)
      (-> data
          (assoc :sec (-> (- now from) (/ 1000) int))))))

(defn start-entry
  [desc]
  (let [url  (str API_URL "/start")
        body {:time_entry {:description desc :created_with "panpan"}}]

    (some-> (call-api url :method "POST" :body body)
            :body
            (json/read-str :key-fn keyword)
            :data
            :id)))

(defn stop-entry
  ([]
   (some-> (get-running-entry) :id stop-entry))
  ([entry-id]
   (let [url (str API_URL "/" entry-id "/stop")]
     (some-> (call-api url :method "PUT")
             :status
             (= 200)))))

(defn delete-entry
  ([]
   (some-> (get-running-entry) :id delete-entry))
  ([entry-id]
   (some-> (str API_URL "/" entry-id)
           (call-api :method "DELETE")
           :status
           (= 200))))

(defn get-last-entry
  []
  (let [to    (c/now)
        from  (c/minus to (c/hours 5))
        query {"start_date" (cf/unparse date-format from)
               "end_date"   (cf/unparse date-format to)}]
    (some-> API_URL
            (call-api :query query)
            :body
            (json/read-str :key-fn keyword)
            reverse
            first)))

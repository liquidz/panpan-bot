(ns panpan.util.atom
  (:require
    [clojure.data.zip.xml :refer :all]
    [clojure.xml          :as xml]
    [clojure.zip          :as zip]
    [jubot.brain          :as brain]
    ;[jubot.test :as jt] ; DELETE ME
    ))

(defn- get-latest-atom-id [url]    (brain/get url))
(defn- set-latest-atom-id [url id] (brain/set url id))

(defn- parse-entry
  [entry]
  {:id        (xml1-> entry :id text)
   :title     (xml1-> entry :title text)
   :published (xml1-> entry :published text)
   :link      (xml1-> entry :link (attr :href))})

(defn get-unread-feeds
  [url]
  (let [data    (-> url xml/parse zip/xml-zip)
        lid     (get-latest-atom-id url)
        unreads (->> (xml-> data :entry)
                     (take-while #(not= lid (xml1-> % :id text)))
                     (map parse-entry))]
    (when-let [id (-> unreads first :id)]
      (set-latest-atom-id url id))
    unreads))

;(def sample-url "https://github.com/liquidz.atom")
;(jt/with-test-brain
;  (set-latest-atom-id sample-url "tag:github.com,2008:PushEvent/2790626033")
;  (let [sample (get-unread-feeds sample-url)]
;    (-> sample
;        count
;        println
;        )
;    (println "test " (count (get-unread-feeds sample-url)))
;    ))

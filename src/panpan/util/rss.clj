(ns panpan.util.rss
  (:require
    [clojure.data.zip.xml :refer :all]
    [clojure.xml          :as xml]
    [clojure.zip          :as zip]
    [jubot.brain          :as brain]))

(defn- get-latest-rss-id [url]    (brain/get url))
(defn- set-latest-rss-id [url id] (brain/set url id))

(defn- parse-entry
  [entry]
  {:id        (xml1-> entry :guid text)
   :title     (xml1-> entry :title text)
   :published (xml1-> entry :pubDate text)
   :link      (xml1-> entry :link text)})

(defn get-unread-feeds
  [url]
  (let [data    (-> url xml/parse zip/xml-zip)
        lid     (get-latest-rss-id url)
        unreads (->> (xml-> data :channel :item)
                     (take-while #(not= lid (xml1-> % :guid text)))
                     (map parse-entry))]
    (when-let [id (-> unreads first :id)]
      (set-latest-rss-id url id))
    unreads))

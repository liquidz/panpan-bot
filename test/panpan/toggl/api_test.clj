(ns panpan.toggl.api-test
  (:require
    [clojure.test      :refer :all]
    [panpan.toggl.api  :refer :all]
    [clojure.data.json :as json]
    [conjure.core      :refer [stubbing]]
    [clj-time.core     :as c]
    [clj-time.format   :as cf]))

(def ^:private one-minute-ago
  (cf/unparse date-format (c/minus (c/now) (c/minutes 1))))

(defmacro stub-toggl-api
  [m & body]
  `(stubbing [call-api {:status 200 :body (json/write-str ~m)}]
     ~@body))
(defmacro stub-500-error
  [m & body]
  `(stubbing [call-api {:status 500}]
     ~@body))

(deftest get-running-entry-test
  (stub-toggl-api {:data {:start one-minute-ago}}
    (let [ret (get-running-entry)]
      (is (some #(= % (:sec ret)) [60 61]))))
  (stub-toggl-api {}
    (is (nil? (get-running-entry))))
  (stub-500-error
    (is (nil? (get-running-entry)))))

(deftest start-entry-test
  (stub-toggl-api {:data {:id "foo"}}
    (is (= "foo" (start-entry "desc"))))
  (stub-500-error
    (is (nil? (start-entry "desc")))))

(deftest stop-entry-test
  (stub-toggl-api {:data {:id "foo" :start one-minute-ago}}
    (is (stop-entry))
    (is (stop-entry "bar")))
  (stub-500-error
    (is (nil? (stop-entry "id")))))

(deftest delete-entry-test
  (stub-toggl-api {:data {:id "foo" :start one-minute-ago}}
    (is (delete-entry))
    (is (delete-entry "bar")))
  (stub-500-error
    (is (nil? (delete-entry "id")))))

(deftest get-last-entries-test
  (stub-toggl-api [{:id "1"} {:id "2"}]
    (is (= ["2"] (map :id (get-last-entries))))
    (is (= ["2" "1"] (map :id (get-last-entries 2)))))
  (stub-500-error
    (is (nil? (get-last-entries)))))

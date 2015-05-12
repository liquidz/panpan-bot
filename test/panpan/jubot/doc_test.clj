(ns panpan.jubot.doc-test
  (:require
    [panpan.jubot.doc :refer :all]
    [clojure.test     :refer :all]
    [conjure.core     :refer [stubbing]]
    [clj-http.client  :as client]))

(deftest test-get-jubot-document-version
  (stubbing [client/get {:body "<title>Jubot 1.0.0 API documentation</title>"}]
    (is (= "1.0.0" (get-jubot-document-version)))))

(deftest test-get-jubot-core-version
  (stubbing [client/get {:body "<title>jubot 1.0.0 - Clojars</title>"}]
    (is (= "1.0.0" (get-jubot-core-version)))))

(deftest test-is-document-latest?
  (stubbing [get-jubot-document-version "a"
             get-jubot-core-version     "a"]
    (is (true? (is-document-latest?))))

  (stubbing [get-jubot-document-version "a"
             get-jubot-core-version     "b"]
    (is (false? (is-document-latest?)))))

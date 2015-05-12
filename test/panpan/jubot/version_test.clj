(ns panpan.jubot.version-test
  (:require
    [panpan.jubot.version :refer :all]
    [clojure.test         :refer :all]
    [conjure.core         :refer [stubbing]]
    [clj-http.client      :as client]))

(deftest test-get-jubot-document-version
  (stubbing [client/get {:body "<title>Jubot 1.0.0 API documentation</title>"}]
    (is (= "1.0.0" (get-jubot-document-version)))))

(deftest test-get-jubot-core-version
  (stubbing [client/get {:body "<title>jubot 1.1.0 - Clojars</title>"}]
    (is (= "1.1.0" (get-jubot-core-version)))))

(deftest test-get-jubot-template-version
  (stubbing [client/get {:body "<title>lein-template 1.2.0 - Clojars</title>"}]
    (is (= "1.2.0" (get-jubot-template-version)))))

(ns panpan.jubot-doc-test
  (:require
    [panpan.jubot-doc :refer :all]
    [clojure.test :refer :all]))

(deftest test-get-jubot-document-version
  (is (some? (get-jubot-document-version))))

(deftest test-get-jubot-core-version
  (is (some? (get-jubot-core-version))))

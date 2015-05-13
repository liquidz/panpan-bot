(ns panpan.github.search-test
  (:require
    [clojure.test         :refer :all]
    [panpan.github.search :refer :all]))

(deftest map->query-test
  (are [x y] (= x (map->query y))
    "a:b"     {:a "b"}
    "b:c+a:b" {:a "b" :b "c"}))

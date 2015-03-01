(ns panpan.memo-test
  (:require
    [panpan.memo  :refer :all]
    [jubot.test   :refer :all]
    [clojure.string :as str]
    [clojure.test :refer :all]))

(defn- memo-test
  [text]
  (memo-handler {:text text}))

(deftest test-memo-handler
  (with-test-brain
    (is (some? (memo-test "メモ bar")))
    (is (= "bar" (-> "メモ 何だっけ？"
                     memo-test
                     (str/split #": ")
                     second)))
    (is (some? (memo-test "メモ 消して")))
    (is (nil? (-> "メモ 何だっけ？"
                  memo-test
                  (str/split #": ")
                  second)))))

(deftest test-memo-schedule
  (with-test-brain
    (let [f (first memo-schedule)]
      (is (nil? (f)))

      (memo-test "メモ foo")
      (is (some? (f)))

      (memo-test "メモ 消して")
      (is (nil? (f))))))

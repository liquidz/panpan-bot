(ns panpan.supplement-test
  (:require
    [panpan.supplement :refer :all]
    [jubot.test        :refer :all]
    [jubot.brain       :as jb]
    [clojure.test      :refer :all]))

(deftest test-supplement-handler
  (let [f #(supplement-handler {:text %})]
    (with-test-brain
      (testing "もう飲んだ"
        (is (nil? (jb/get SNOOZE_SKIP_KEY)))
        (is (some? (f "もう飲んだ")))
        (is (= "true" (jb/get SNOOZE_SKIP_KEY))))

      (testing "飲んだ"
        (jb/set SNOOZE_KEY "true")
        (is (some? (f "飲んだ")))
        (is (nil? (jb/get SNOOZE_KEY)))))))

(deftest test-supplement-schedule
  (let [[start remind] supplement-schedule]
    (testing "事前にもう飲んだしておけばリマインドがキャンセルされること"
      (with-test-brain
        (jb/set SNOOZE_SKIP_KEY "true")
        (is (nil? (start)))
        (is (nil? (jb/get SNOOZE_SKIP_KEY)))
        (is (nil? (jb/get SNOOZE_KEY)))))

    (testing "リマインドが開始できること"
      (with-test-brain
        (jb/set SNOOZE_SKIP_KEY nil)
        (is (nil? (start)))
        (is (= "true" (jb/get SNOOZE_KEY)))))

    (testing "リマインドできること"
      (with-test-brain
        (is (nil? (remind)))
        (is (some? (do (start) (remind))))))))

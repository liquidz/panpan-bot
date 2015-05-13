(ns panpan.util.atom-test
  (:require
    [panpan.util.atom :refer :all]
    [clojure.java.io :as io]
    [clojure.test    :refer :all]
    [clojure.xml     :as xml]
    [conjure.core    :refer [stubbing]]
    [jubot.brain     :as jb]
    [jubot.test      :as jt]))

(def test-data (xml/parse (io/file "test/files/util/liquidz.atom")))

(deftest get-unread-feeds-test
  (stubbing [xml/parse test-data]
    (jt/with-test-brain
      (let [url "dummy-url"
            feeds (get-unread-feeds url)]
        (testing "初期状態では全てのフィードが取れること"
          (is (= 30 (count feeds))))
        (testing "最新の entry の ID が保存されていること"
          (is (= (-> feeds first :id) (jb/get url))))
        (testing "既に取得済みのフィードは取れないこと"
          (is (= 0 (count (get-unread-feeds url))))

          (jb/set url (-> feeds (nth 3) :id))
          (is (= 3 (count (get-unread-feeds url)))))
        (testing "取得済みのフィードがない場合は最新IDが更新されないこと"
          (jb/set url nil)
          (get-unread-feeds url)
          (get-unread-feeds url)
          (is (= (-> feeds first :id) (jb/get url))))))))



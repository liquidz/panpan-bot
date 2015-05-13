(ns panpan.github.feed-test
  (:require
    [clojure.test       :refer :all]
    [panpan.github.feed :refer :all]))

(deftest github-respond-test
  (are [x y] (= x (:event (github-respond {:title y})))
    :open-issue         "liquidz opened issue liquidz/test#3"
    :close-issue        "liquidz closed issue liquidz/test#3"
    :comment-issue      "liquidz commented on issue liquidz/test#3"
    :open-pull-request  "liquidz opened pull request liquidz/test#2"
    :merge-pull-request "liquidz merged pull request liquidz/test#2"
    :new-branch         "liquidz created branch 0.1.1 at liquidz/test"
    :push-branch        "liquidz pushed to gh-pages at liquidz/jubot"
    :delete-branch      "liquidz deleted branch 0.1.1 at liquidz/jubot"))

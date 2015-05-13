(ns panpan.github.feed
  (:require
    [panpan.util.match :refer :all]
    [panpan.util.atom  :as atom]
    [jubot.handler     :as jh]))

(def ^:const FEED_URL "https://github.com/liquidz.atom")

(defn github-respond
  [{:keys [title]}]
  (jh/regexp {:text title}
    #"opened issue (.+)#(\d+)$"
    (matchfn [repo issue]
             {:event      :open-issue
              :repository repo
              :issue      issue})
    #"closed issue (.+)#(\d+)$"
    (matchfn [repo issue]
             {:event      :close-issue
              :repository repo
              :issue      issue})
    #"commented on issue (.+)(#\d+)$"
    (matchfn [repo issue]
             {:event      :comment-issue
              :repository repo
              :issue      issue})
    #"opened pull request (.+)(#\d+)$"
    (matchfn [repo issue]
             {:event      :open-pull-request
              :repository repo
              :issue      issue})
    #"merged pull request (.+)(#\d+)$"
    (matchfn [repo issue]
             {:event      :merge-pull-request
              :repository repo
              :issue      issue})
    #"created repository (.+)$"
    (matchfn [repo]
             {:event      :new-repository
              :repository repo})
    #"created branch (.+) at (.+)$"
    (matchfn [branch repo]
             {:event      :new-branch
              :repository repo
              :branch     branch})
    #"pushed to (.+?) at (.+)$"
    (matchfn [branch repo]
             {:event      :push-branch
              :repository repo
              :branch     branch})
    #"deleted branch (.+) at (.+)$"
    (matchfn [branch repo]
             {:event      :delete-branch
              :repository repo
              :branch     branch})))

(defn get-github-feeds
  []
  (atom/get-unread-feeds FEED_URL))

(defn get-github-event
  []
  (some-> (get-github-feeds)
          seq first github-respond))

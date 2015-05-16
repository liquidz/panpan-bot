(ns panpan.hestia
  (:require
    [clojure.string    :as str]
    [jubot.adapter     :as ja]
    [jubot.handler     :as jh]
    [jubot.scheduler   :as js]
    [panpan.util.match :refer :all]
    [panpan.toggl.api  :as toggl]))

(def ^:const NAME "ヘスティア") ; {{{
(def ^:const ICON "https://dl.dropboxusercontent.com/u/14918307/slack_icon/hestia.png")
(def ^:private out #(do (ja/out (apply str %&) :as NAME :icon-url ICON) nil))
(def ^:private pre #(str "```\n" % "\n```")) ; }}}

(def ^:const WORKSPACE_ID "141468")
(def ^:const REST_PID "3141045")
(def ^:const WARN_SEC (* 25 60)) ; 25 min
(def ^:const REST_WARN_SEC (* 5 60)) ; 5 min


(def ^:const MESSAGES ; {{{
  {:start-entry
   ["了解だよ！"
    "無理するなよ！"
    ]
   :stop-entry
   ["お疲れ！"
    "お疲れ様！"
    ]
   :delete-entry
   ["おっと、了解だよ！"
    "おっとゴメンよ！"
    ]
   :no-entry-to-delete
   ["ん？何が違うんだい？"]
   :start-rest
   ["オッケーだよ！"
    ]
   :reply
   ["ちょっとまって"
    ]
   :rest-warn
   ["そろそろ休憩終わりじゃないかい？"
    "いつまで休んでるんだい？"
    ]
   :warn
   ["そろそろ休憩したらどうだい？"
    "集中できてるかい？"
    ]
   :thanks
   ["どういたしまして！"
    "君のためだからね！"
    ]
   :sorry
   ["わかればいいんだよ"
    "次から気をつけるんだよ？"
    ]
   }) ; }}}

(defn- desc->pid ; {{{
  [desc]
  (jh/regexp {:text desc}
    #"cont"  (fn [& _] "870449")
    #"tdsei" (fn [& _] "8276549")
    #"sso"   (fn [& _] "3141182")
    #"ple"   (fn [& _] "3146901")
    #"(MTG|mtg|打ち合わせ|ミーティング)"
    (fn [& _] "3141739")
    #"(休憩|きゅうけい)"
    (fn [& _] "3141045")
    #"(ゲーム|げーむ)"
    (fn [& _] "9863311")
    #".*"
    (fn [& _] "8256158"))) ; }}}

(defn hestia-handler
  "^(.+?)\\s*(を)?(開始|始める|はじめる) - toggl開始
   ^(再開|さいかい)                     - toggl再開
   ^(終了|終わった|おわった|おわた)     - toggl停止
   ^(神様|神さま).*違(い|う)            - toggl削除
   ^(神様|神さま).*(休憩|一休み)        - toggl休憩エントリー開始
  "
  [{:keys [user] :as arg}]
  (jh/regexp arg
    #"^(.+?)\s*(を)?(開始|始め|はじめ)"
    (matchfn [desc]
      (toggl/start-entry desc :pid (desc->pid desc))
      (->> MESSAGES :start-entry rand-nth (out "@" user " ")))
    #"^(再開|さいかい)"
    (matchfn []
      (when-let [entry (toggl/get-last-entry)]
        (hestia-handler
          (assoc arg :text (str (:description entry) "開始")))
        nil))
    #"^(終了|終わった|おわった|おわた)"
    (matchfn []
      (when (toggl/stop-entry)
        (->> MESSAGES :stop-entry rand-nth (out "@" user " "))))
    #"^(神様|神さま).*違(い|う)"
    (matchfn []
      (let [key (if (toggl/delete-entry) :delete-entry :no-entry-to-delete)]
        (->> MESSAGES
             key
             rand-nth
             (out "@" user " "))))
    #"^(神様|神さま).*(休憩|一休み)"
    (matchfn []
      (->> MESSAGES :start-rest rand-nth (out "@" user " "))
      (toggl/start-entry "休憩" :pid REST_PID)
      nil)

    #"^(神様|神さま).*ありがと"
    (matchfn []
      (->> MESSAGES :thanks rand-nth (out "@" user " ")))

    #"^(神様|神さま).*(ごめん|ゴメン)"
    (matchfn []
      (->> MESSAGES :sorry rand-nth (out "@" user " ")))

    #"^(神様|神さま).*プロジェクト"
    (matchfn []
      (->> MESSAGES :reply rand-nth (out "@" user " "))
      (->> (toggl/get-projects WORKSPACE_ID)
           (map #(str (:name %) " (" (:id %) ")"))
           (str/join "\n")
           pre out))
    ))

(def hestia-schedule
  (js/schedules
    "0 /5 * * * * *"
    #(when-let [{:keys [pid sec]} (toggl/get-running-entry)]
       (cond
         (and (= (str pid) REST_PID) (> sec REST_WARN_SEC))
         (->> MESSAGES :rest-warn rand-nth (out "@uochan "))

         (> sec WARN_SEC)
         (->> MESSAGES :warn rand-nth (out "@uochan "))))
    ))

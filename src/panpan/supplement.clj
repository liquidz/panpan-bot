(ns panpan.supplement
  (:require
    [jubot.adapter   :as adapter]
    [jubot.handler   :as handler]
    [jubot.scheduler :as scheduler]
    [jubot.brain     :as brain]))

(def ^:const SNOOZE_KEY      "supplement_snooze")
(def ^:const SNOOZE_SKIP_KEY "supplement_snooze_skip")

(def ^:const SUPPLEMENT_REMINDER_TEXTS
  ["もう飲んだ？" "まだ飲んでないの？" "早く飲めよ" "飲め飲め" "さっさと飲みなさい" "何はともあれ飲め"])
(def ^:const SUPPLEMENT_OK_TEXTS
  ["よくやった" "よし" "OK" "おっけー"])
(def ^:const SUPPLEMENT_SKIP_TEXTS
  ["えっ？お、おｋ" "やるな" "そんな馬鹿な。。" "流石" "マジか、、"])

(def supplement-handler
  (handler/regexp
    #"もう.*(飲んだ|のんだ|飲んじゃった|のんじゃった)"
    (fn [{user :user}]
      (brain/set SNOOZE_SKIP_KEY "true")
      (rand-nth SUPPLEMENT_SKIP_TEXTS))

    #"(飲んだ|のんだ|nonda)"
    (fn [{user :user}]
      (brain/set SNOOZE_KEY nil)
      (rand-nth SUPPLEMENT_OK_TEXTS))))

(def supplement-schedule
  (scheduler/schedules
    "0 13 21 * * * *"
    #(if-let [x (brain/get SNOOZE_SKIP_KEY)]
       (brain/set SNOOZE_SKIP_KEY nil)
       (brain/set SNOOZE_KEY "true"))

    "0 * * * * * *"
    #(if-let [x (brain/get SNOOZE_KEY)]
       (->> SUPPLEMENT_REMINDER_TEXTS
            rand-nth
            (str "uochan ")
            adapter/out))))

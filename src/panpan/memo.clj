(ns panpan.memo
  (:require
    [jubot.handler   :as handler]
    [jubot.adapter   :as adapter]
    [jubot.scheduler :as scheduler]
    [jubot.brain     :as brain]))

(def ^:const MEMO_KEY "panpan_memo")
(def ^:const REMEMBER_TEXTS
  ["わかった" "らじゃー" "おーけー" "仕方ないなぁ"])
(def ^:const FORGET_TEXTS
  ["忘れたよ" "消したよ"])
(def ^:const REMINDER_TEXTS
  ["思い出して" "覚えてる？" "忘れてない？" "そういえば" "これどうするの？"])
(def ^:const REMIND_SCHEDULE "0 30 7,12,18,20 * * * *")

(def memo-handler
  (handler/regexp
    #"^メモ (消して|けして|削除|忘れて|忘れて)"
      (fn [_]
        (let [x (brain/get MEMO_KEY)]
          (brain/set MEMO_KEY nil)
          (str (rand-nth FORGET_TEXTS) ": " x)))
    #"^メモ (何だっけ|なんだっけ|忘れた|わすれた)"
      (fn [_]
        (if-let [x (brain/get MEMO_KEY)]
          (str (rand-nth REMINDER_TEXTS) ": " x)
          "何もないよ？"))
    #"^メモ (.+?)$"
      (fn [{[_ text] :match}]
        (brain/set MEMO_KEY text)
        (str (rand-nth REMEMBER_TEXTS) ": " text))))

(def memo-schedule
  (scheduler/schedules
    REMIND_SCHEDULE
      #(when-let [x (brain/get MEMO_KEY)]
         (adapter/out (str (rand-nth REMINDER_TEXTS) ": " x)))))

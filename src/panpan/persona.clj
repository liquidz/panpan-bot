(ns panpan.persona
  (:require
    [jubot.handler   :as handler]
    [jubot.scheduler :as scheduler]
    [jubot.brain     :as brain]))

(defn persona-handler
  "p 何食べたい？ - 食べたいものを決めてもらう"
  [{:keys [user] :as arg}]
  (handler/regexp
    arg
    #"ぬるぽ"
    (fn [_] (str "@" user " ガッ"))

    #"(パンダ|ぱんだ)"
    (fn [_]
      (str "@" user " "
           (rand-nth ["呼んだ？" "なぁに？" "ん？" "はーい、え？違う？"
                      "こっち見んな" "？" "僕のこと？" "うるさいぞ"])))

    #"(なに|何)(たべ|食べ)(よう|ようかな|る?|る？|たい?|たい？)"
    (fn [_]
      (rand-nth ["牛丼" "パスタ" "カレー" "寿司" "蕎麦" "うどん" "定食"
                 "ファミレス" "ラーメン" "チャーハン" "餃子" "ハンバーグ"
                 "竹" "笹" "タケノコ" "豆腐" "オムライス"]))))

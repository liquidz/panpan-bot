(ns panpan.persona
  (:require
    [jubot.handler   :as handler]
    [jubot.scheduler :as scheduler]
    [jubot.brain     :as brain]))

(def ^{:doc (str "p 何食べたい？ - 食べたいものを決めてもらう")}
  persona-handler
  (handler/regexp
    #"ぬるぽ"
      (constantly "ガッ")
    #"(パンダ|ぱんだ)"
      (fn [_]
        (rand-nth ["呼んだ？" "なぁに？" "ん？" "はーい、え？違う？"
                   "こっち見んな" "？" "僕のこと？" "うるさいぞ"]))
    #"(竹|笹|筍|たけのこ|タケノコ|竹の子)"
      (fn [{[_ text] :match}]
        (rand-nth ["ガタッ" "じゅる" "えっ？" (str text "？") (str text "、美味しそう")
                   "お腹空いた。。" (str text "！") (str text "どこ？")
                   (str text "くれるの？") (str text "よこせ")]))
    #"何.*食べ.*(\?|？)"
      (fn [_]
        (rand-nth ["牛丼" "パスタ" "カレー" "寿司" "蕎麦" "うどん" "定食"
                   "ファミレス" "ラーメン" "チャーハン" "餃子" "ハンバーグ"
                   "竹" "笹" "タケノコ" "豆腐" "オムライス"]))))

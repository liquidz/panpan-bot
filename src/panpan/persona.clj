(ns panpan.persona
  (:require
    [clojure.string  :as str]
    [jubot.handler   :as handler]
    [jubot.brain     :as brain]
    [clj-time.core   :as tt]
    [clj-time.format :as tf]
    [clj-docomo-dialogue.core :as dd]))

(def ^:const CONTEXT_KEY "docomo_dialogue_context")
(def ^:private api-key (System/getenv "DOCOMO_API_KEY"))

(defn- brain-key-values
  [& _]
  (->> (brain/keys)
       (reduce #(conj % (str " * " %2 " => " (brain/get %2))) [])
       (str/join "\n")
       (str "Key/Value:\n")))

(defn persona-hear-handler
  [{:keys [user] :as arg}]
  (handler/regexp
    arg
    #"ぬるぽ"
    (fn [_] "ガッ")

    #"(パンダ|ぱんだ)"
    (fn [_]
      (rand-nth ["呼んだ？" "なぁに？" "ん？" "はーい、え？違う？"
                 "こっち見んな" "？" "僕のこと？" "うるさいぞ"]))

    #"(なに|何)(たべ|食べ)(よう|ようかな|る?|る？|たい?|たい？)"
    (fn [_]
      (rand-nth ["牛丼" "パスタ" "カレー" "寿司" "蕎麦" "うどん" "定食"
                 "ファミレス" "ラーメン" "チャーハン" "餃子" "ハンバーグ"
                 "竹" "笹" "タケノコ" "豆腐" "オムライス"]))))

(defn panpan-handler
  "p ping        - pong
   p set KEY VAL - brain に情報を登録
   p get KEY     - brain から情報を取得
   p brain       - brain の KEY/VALUE 一覧
   p 文字列      - 会話
  "
  [{:keys [text message-for-me?] :as arg}]
  (when message-for-me?
    (handler/regexp arg
      #"ping"              (constantly "pong")
      #"date"              (fn [& _] (str (tt/now)))
      #"^set (.+?) (.+?)$" (fn [{[_ k v] :match}] (brain/set k v) "OK")
      #"^get (.+?)$"       (fn [{[_ k]   :match}] (brain/get k))
      #"^brain$"           brain-key-values
      #"^(?!.*help).+"
      (fn [& _]
        (let [opt {:context (brain/get CONTEXT_KEY)}
              res (some-> api-key (dd/talk text opt))]
          (when res
            (brain/set CONTEXT_KEY (:context res))
            (:utt res)))))))

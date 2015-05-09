(ns panpan.github
  (:require
    [jubot.handler :as handler]))

(defn github-handler
  ""
  [{:keys [user text] :as arg}]
  (when (= "github" user)
    (str "DEBUG:\"" text "\"")
    #_(handler/regexp
      arg
      #"(?s)^\[(.+?)\].+Issue created.+(#\d+)"
      (fn [{[_ repo issue] :match}]
        (str repo "で新しいissueだよー[" issue "]"))

      #"(?s)^\[(.+?)\].+Issue closed.+(#\d+)"
      (fn [{[_ repo issue] :match}]
        (str repo "でissueがクローズしたよ[" issue "]"))

      #"(?s)^\[(.+?)\].+New comment.+(#\d+)"
      (fn [{[_ repo issue] :match}]
        (str repo "でコメントがついたよ"))

      #"(?s)^\[(.+?)\].+New branch.+\"(.+?)\""
      (fn [{[_ repo branch] :match}]
        (str repo "で新しいぶらんち" branch "ができたよ"))

      #"(?s)^\[(.+?)\].+branch.+\"(.+?)\".+deleted"
      (fn [{[_ repo branch] :match}]
        (str repo "でぶらんち" branch "が削除されたよ"))

      #"(?s)^\[(.+?)\].+Pull request submitted.+(#\d+)"
      (fn [{[_ repo issue] :match}]
        (str repo "でpull-requestがきたよ[" issue "]"))

      #"(?s)^\[(.+?)\].+Pull request closed.+(#\d+)"
      (fn [{[_ repo issue] :match}]
        (str repo "でpull-requestがクローズされたよ[" issue "]"))
      )))


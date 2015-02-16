(ns panpan.core
  (:require
    [jubot.core :refer [jubot]]))

(def ns-prefix #"^panpan\.")

(def -main (jubot :ns-regexp ns-prefix))



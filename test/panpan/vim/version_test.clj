(ns panpan.vim.version-test
  (:require
    [clojure.test       :refer :all]
    [panpan.vim.version :refer :all]
    [conjure.core       :refer [stubbing]]
    [jubot.test         :refer :all]
    [jubot.brain        :as jb]))

(deftest check-vim-version-test
  (with-test-brain
    (stubbing [get-latest-patch ["dummy" "0.0.1" "message"]]
      (jb/set KEY nil)
      (is (= {:latest? true :version "0.0.1" :message "message"}
             (check-vim-version)))
      (is (= "0.0.1" (jb/get KEY)))

      (is (= {:latest? false :version "0.0.1" :message "message"}
             (check-vim-version))))))


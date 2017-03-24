(ns maverick.classic
  (:require [maverick.db :as db]))


(defn can-move?
  [db loc]
  (let [plocs (-> db ::db/current-position ::db/locations)]
    

    true))


(defn destinations
  [db [i j]]

  ;; placeholder
  
  [[i (inc j)]
   [i (+ j 2)]])



(defn game-result
  [db]

  ;; placeholder
  
  nil)

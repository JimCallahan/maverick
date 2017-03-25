(ns maverick.classic
  (:require [maverick.db :as db]))


(defn can-move?
  [db loc]
  (let [color (-> db ::db/current-move ::db/color)
        plocs (-> db ::db/current-position ::db/locations)
        piece (get plocs loc)]

    ;; placeholder
    
    (= color (::db/color piece))))


(defn destinations
  [db [i j]]

  ;; placeholder
  
  (when (can-move? db [i j]) 
    [[i (inc j)]
     [i (+ j 2)]]))



(defn game-result
  [db]

  ;; placeholder
  
  nil)

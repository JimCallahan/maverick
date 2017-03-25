(ns maverick.board
  (:require [re-frame.core :as rf]
            [maverick.subs :as subs :refer [listen]]
            [maverick.events :as events]
            [maverick.db :as db]))

(defn screen-j
  "The vertical coordinate of a square in the same direction as screen Y space
   taking into account the orientation white/black of the board."
  [j rows orient]
  (case orient
    ::db/white (- rows j 1)
    ::db/black j))
         
(defn squares []
  (let [{:keys [::db/rows ::db/cols ::db/orient]} (listen [::subs/board-layout])
        rem (case orient
              ::db/white 1
              ::db/black 0)]
    [:g 
     (for [i (range 0 cols)
           j (range 0 rows)]
       ^{:key [i j]}
       [:rect {:class (if (= (mod (+ i j) 2) rem)
                        "lite-squares"
                        "dark-squares")
               :width 1
               :height 1
               :x i
               :y (screen-j j rows orient)
               :on-click      #(rf/dispatch [::events/square-click [i j]])
               :on-mouse-over #(rf/dispatch [::events/square-hover [i j]])
               :on-mouse-out  #(rf/dispatch [::events/square-hover nil])}])]))

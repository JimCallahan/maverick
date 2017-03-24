(ns maverick.views
    (:require [re-frame.core :as rf]
              [re-com.core :as rc]
              [maverick.subs :as subs :refer [listen]]
              [maverick.db :as db]
              [maverick.events :as events]
              [maverick.pieces :as pieces]))

(defn header []
  [rc/h-box
   :children
   [[rc/gap :size "1"]
    [rc/title
     :label "Maverick"
     :level :level1]
    [rc/gap :size "1"]]])

(defn squares []
  (let [{:keys [::db/rows ::db/cols]} (listen [::subs/board-dimens])]
    [:g 
     (for [i (range 0 cols)
           j (range 0 rows)]
       ^{:key [i j]}
       [:rect {:class (if (= (mod (+ i j) 2) 1)
                        "lite-squares"
                        "dark-squares")
               :width 1
               :height 1
               :x i
               :y (dec (- rows j))
               :on-click      #(rf/dispatch [::events/square-click [i j]])
               :on-mouse-over #(rf/dispatch [::events/square-hover [i j]])
               :on-mouse-out  #(rf/dispatch [::events/square-hover nil])}])]))

(defn pieces []
  (let [ps (listen [::subs/piece-locations])] 
    (into [:g]
          (for [[loc {:keys [::db/color ::db/kind]}] ps]
            [pieces/piece color kind loc]))))

(defn hovers []
  [:g
   [pieces/hover]
   [pieces/move-start]])

(defn board []
  (let [{:keys [::db/rows ::db/cols]} (listen [::subs/board-dimens])
        size (listen [::subs/board-size])]
    [:center
     [:svg
      {:view-box (str "0 0 " cols " " rows)
       :width size
       :height size}
      [squares]
      [hovers]
      [pieces]]]))

(defn main-panel []
  [rc/v-box 
   :children
   [[header]
    [board]]])
 

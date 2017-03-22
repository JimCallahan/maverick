(ns maverick.views
    (:require [re-frame.core :as rf]
              [re-com.core :as rc]
              [maverick.subs :as subs :refer [listen]]
              [maverick.db :as db]
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
               :y (dec (- rows j))}])]))

(defn pieces []
  (let [ps (listen [::subs/current-position])]
    (into [:g]
          (for [[[i j] {:keys [::db/color ::db/kind]}] ps]
            [pieces/piece color kind i j]))))

(defn board []
  (let [{:keys [::db/rows ::db/cols]} (listen [::subs/board-dimens])
        size (listen [::subs/board-size])]
    [:center
     [:svg
      {:view-box (str "0 0 " cols " " rows)
       :width size
       :height size}
      [squares] 
      [pieces]]]))

(defn main-panel []
  [rc/v-box 
   :children
   [[header]
    [board]]])
 

(ns maverick.views
    (:require [re-frame.core :as rf]
              [re-com.core :as rc]
              [maverick.db :as db]
              [maverick.subs :as subs :refer [listen]]
              [maverick.board :as board]
              [maverick.pieces :as pieces]))

(defn header []
  [rc/h-box
   :children
   [[rc/gap :size "1"]
    [rc/title
     :label "Maverick"
     :level :level1]
    [rc/gap :size "1"]]])


(defn pieces []
  (let [ps (listen [::subs/piece-locations])] 
    (into [:g]
          (for [[loc {:keys [::db/color ::db/kind]}] ps]
            [pieces/piece color kind loc]))))

(defn board []
  (let [{:keys [::db/rows ::db/cols]} (listen [::subs/board-layout])
        size (listen [::subs/board-size])]
    [:center
     [:svg
      {:view-box (str "0 0 " cols " " rows)
       :width size
       :height size}
      [board/squares]
      [board/hovers]
      [pieces]]]))

(defn main-panel []
  [rc/v-box 
   :children
   [[header]
    [board]]])
 

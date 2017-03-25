(ns maverick.geom  
  (:require [reagent.format :as fmt]
            [maverick.db :as db]
            [maverick.subs :as subs :refer [listen]]))

(defn- path
  "An SVG `path`."
  ([class specs]
   (let [clz (when class {:class class})]
     [:path (merge clz {:d (apply str specs)})]))
  ([specs]
   (path nil specs)))

(defn screen-j
  "The vertical coordinate of a square in the same direction as screen Y space
   taking into account the orientation white/black of the board."
  [j rows orient]
  (case orient
    ::db/white (- rows j 1)
    ::db/black j))
         
(defn loc-group
  [class [i j]]
  (let [{:keys [::db/rows ::db/orient]} (listen [::subs/board-layout])
        nj (screen-j j rows orient)
        ss (->> (/ 1.0 360.0) (fmt/format "%.4f"))]
    [:g {:class (name class)
         :transform (str "translate(" i "," nj ") scale(" ss ")")}]))
 

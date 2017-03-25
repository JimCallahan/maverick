(ns maverick.board
  (:require [re-frame.core :as rf]
            [maverick.db :as db]
            [maverick.events :as events]
            [maverick.subs :as subs :refer [listen]]
            [maverick.geom :as geom :refer [path]]))

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
               :y (geom/screen-j j rows orient)
               :on-click      #(rf/dispatch [::events/square-click [i j]])
               :on-mouse-over #(rf/dispatch [::events/square-hover [i j]])
               :on-mouse-out  #(rf/dispatch [::events/square-hover nil])}])]))


(def bracket
  [(path ["M9,90V21A12,12,0,0,1,21,9H90"])
   (path ["M90,351H21A12,12,0,0,1,9,339V270"])
   (path ["M351,270v69a12,12,0,0,1-12,12H270"])
   (path ["M270,9h69a12,12,0,0,1,12,12V90"])])

(defn hover []
  (let [loc (listen [::subs/hover-loc])]
    (when loc
      (into (geom/loc-group :hover loc)
            bracket))))
    
(defn move-start []
  (let [loc (listen [::subs/start-location])]
    (when loc
      (into (geom/loc-group :move loc)
            bracket))))

(defn hovers []
  [:g
   [hover]
   [move-start]])

(defn targets []
  (->> (listen [::subs/target-locations])
       (map (fn [loc] 
              (conj (geom/loc-group :move loc)
                    [:circle {:cx 180.0 :cy 180.0 :r 50}])))
       (into [:g])))

       


(ns maverick.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as rf]
              [maverick.db :as db]))

(defn listen
  [query-v]
  @(rf/subscribe query-v))

;; The size of the board in pixels.
(rf/reg-sub
 ::board-size
 (fn [db _]
   (case (get-in db [::db/look ::db/board-size])
     ::db/small 500
     ::db/medium 800
     ::db/large 1000)))

;; The layout of the board.
(rf/reg-sub
 ::board-layout 
 (fn [db _]
   (::db/board db)))

;; The locations of all pieces on the board.
(rf/reg-sub
 ::piece-locations
 (fn [db _] 
   (-> db ::db/current-position ::db/locations)))

;; The currently selected start location for a move.
(rf/reg-sub
 ::start-location
 (fn [db _]
   (-> db ::db/current-move ::db/start-location)))

;; The currently selected end location of a move.
(rf/reg-sub
 ::end-location
 (fn [db _]
   (-> db ::db/current-move ::db/start-location)))

;; The location under the mouse.
(rf/reg-sub
 ::hover-location
 (fn [db _]
   (-> db ::db/feedback ::db/hover-loc)))

;; The location under the mouse, but only if it is different than the move ones.
(rf/reg-sub
 ::hover-loc
 :<- [::start-location]
 :<- [::hover-location]   
 (fn [[sloc hloc] _]
   (when (not= hloc sloc)
     hloc)))

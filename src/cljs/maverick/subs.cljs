(ns maverick.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as rf]
              [maverick.db :as db]))

(defn listen
  [query-v]
  @(rf/subscribe query-v))

(rf/reg-sub
 ::board-size
 (fn [db _]
   (case (get-in db [::db/look ::db/board-size])
     ::db/small 500
     ::db/medium 800
     ::db/large 1000)))
 
(rf/reg-sub
 ::board-dimens
 (fn [db _]
   (::db/board db)))

(rf/reg-sub
 ::current-position
 (fn [db _] 
   (-> (::db/positions db) first)))

(ns maverick.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as rf]
              [maverick.db :as db]))

(rf/reg-sub
 ::board-size
 (fn [db]
   (case (get-in db [::db/look ::db/board-size])
     ::db/small 500
     ::db/medium 800
     ::db/large 1000)))
 
(rf/reg-sub
 ::board-dimens
 (fn [db]
   (::db/board db)))

(rf/reg-sub
 ::position
 (fn [db]
   (::db/position db)))

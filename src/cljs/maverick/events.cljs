(ns maverick.events
    (:require [re-frame.core :as rf]
              [maverick.db :as db]))

(rf/reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))

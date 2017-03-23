(ns maverick.events
  (:require [re-frame.core :as rf]
            [maverick.db :as db]))

(defn- reg-event-db
  ([id handler-fn] 
   (reg-event-db id nil handler-fn))
  ([id interceptors handler-fn]
    (rf/reg-event-db 
     id 
     [(when ^boolean goog.DEBUG rf/debug)
      (when ^boolean goog.DEBUG (rf/after db/valid-database?)) 
      interceptors]
     handler-fn)))

(reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))

(reg-event-db
 ::initialize-game
 (fn [db [_ game]]
   (merge db db/game-start (game db/game-setups))))

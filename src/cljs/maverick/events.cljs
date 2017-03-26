(ns maverick.events
  (:require [re-frame.core :as rf]
            [maverick.db :as db]
            [maverick.rules.api :as rules]
            [maverick.rules.core :refer [rules-for cur-rules]]))


;;------------------------------------------------------------------------------
;; Helpers. 
;;------------------------------------------------------------------------------

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


;;------------------------------------------------------------------------------
;; Initialization. 
;;------------------------------------------------------------------------------

(reg-event-db
 ::initialize-db
 (fn  [_ _]
   (rules/init-db (rules-for ::db/classic))))


;;------------------------------------------------------------------------------
;; Board Interactions. 
;;------------------------------------------------------------------------------

(reg-event-db
 ::square-hover
 (fn [db [_ loc]]
   (assoc-in db [::db/feedback ::db/hover-loc]
             (when (rules/in-bounds? (cur-rules db) db loc)
               loc))))

(reg-event-db
 ::square-click
 (fn [db [_ loc]]
   (let [{:keys [::db/move-number ::db/color ::db/start-location ::db/start-stamp]
          :as pmove} (::db/current-move db)
         now (.now js/Date)]
     
     (if start-location
       (let [targets (rules/targets (cur-rules db) db start-location)]
         (if-not (get targets loc)
         
           ;; Cancel the move.
           (update-in db [::db/current-move] dissoc ::db/start-location)
           
           ;; Make a move.
           (let [plocs (-> db ::db/current-position ::db/locations)
                 took (when-let [p (get plocs loc)]
                        {::db/took p})
                 move (-> (assoc pmove
                                 ::db/end-location loc
                                 ::db/end-stamp now)
                          (merge took))
                 next-move (-> (case color
                                 ::db/white {::db/move-number move-number
                                             ::db/color ::db/black}
                                 ::db/black {::db/move-number (inc move-number)
                                             ::db/color ::db/white})
                               (assoc ::db/start-stamp now))
                 piece (get plocs start-location)
                 end-locations {::db/locations (-> plocs
                                                   (dissoc start-location)
                                                   (assoc loc piece))}
                 position (-> (select-keys pmove [::db/move-number ::db/color])
                              (merge end-locations))]
             (-> db
                 (update-in [::db/moves] conj move)
                 (assoc ::db/current-move next-move)
                 (update-in [::db/positions] conj position)
                 (assoc ::db/current-position end-locations)))))
         
       ;; Start a move.
       (let [targets (rules/targets (cur-rules db) db loc)]
         (if (seq targets)
           (let [kind (-> db
                          ::db/current-position
                          ::db/locations
                          (get loc)
                          ::db/kind)]
             (->> (assoc pmove
                         ::db/start-location loc
                         ::db/kind kind)
                  (assoc db ::db/current-move)))
           db))))))
       
       

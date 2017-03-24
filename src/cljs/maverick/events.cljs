(ns maverick.events
  (:require [re-frame.core :as rf]
            [maverick.db :as db]
            [maverick.classic :as classic]))

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
   db/default-db))

(reg-event-db
 ::initialize-game
 (fn [db [_ game]]
   (merge db db/game-start (game db/game-setups))))



;;------------------------------------------------------------------------------
;; Board Interactions. 
;;------------------------------------------------------------------------------

(defn- in-bounds?
  [db [i j]]
  (let [{:keys [::db/board]} db
        {:keys [::db/rows ::db/cols]} board]
    (and i j
         (and (<= 0 i) (< i cols))
         (and (<= 0 j) (< j rows)))))

(reg-event-db
 ::square-hover
 (fn [db [_ loc]]
   (assoc-in db [::db/feedback ::db/hover-loc]
             (when (in-bounds? db loc) loc))))

(reg-event-db
 ::square-click
 (fn [db [_ loc]]
   (let [{:keys [::db/move-number ::db/color ::db/start-location ::db/start-stamp]
          :as pmove}
         (::db/current-move db)
         now (.now js/Date)]
     (if start-location
       (if (= loc start-location)
         ;; Cancel the move.
         (update-in db [::db/current-move] dissoc ::db/start-location)
         ;; Make a move.
         (let [move (assoc pmove
                           ::db/end-location loc
                           ::db/end-stamp now)
               next (-> (case color
                          ::db/white {::db/move-number move-number
                                      ::db/color ::db/black}
                          ::db/black {::db/move-number (inc move-number)
                                      ::db/color ::db/white})
                        (assoc ::db/start-stamp now))]
           (-> db
               (update-in [::db/moves] conj move)
               (assoc ::db/current-move next))))
       ;; Start a move.
       (if (classic/can-move? db loc)
         (->> (assoc pmove ::db/start-location loc)
              (assoc db ::db/current-move))
         db)))))
       
       

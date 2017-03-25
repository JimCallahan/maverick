(ns maverick.rules.api
  (:require [maverick.db :as db]
            [maverick.rules.protocol :as proto]))

;;------------------------------------------------------------------------------
;; Rules API. 
;;------------------------------------------------------------------------------

(defn init-db
  "Generate the initial value of the database." 
  [rules]
  (proto/init-db rules)) 

(defn in-bounds?
  "Whether the location is within the bounds of the board."
  [rules db loc]
  (proto/in-bounds? rules db loc))

(defn can-move?
  "Whether there exist a piece at the location which can be moved this turn."
  [rules db loc]
  (proto/can-move? rules db loc))

(defn targets
  "The possible target locations when moving the piece at the location."
  [rules db loc]
  (proto/targets rules db loc))
                                       
(defn game-result
  "The result of the game, if completed."
  [rules db]
  (proto/game-result rules db))


;;------------------------------------------------------------------------------
;; Data Specs. 
;;------------------------------------------------------------------------------

#_(def rules? (partial satisfies? proto/Rules))

  
;;------------------------------------------------------------------------------
;; Function Specs. 
;;------------------------------------------------------------------------------
  
;; Whether a there is a piece at the given location which can be moved.
#_(s/fdef can-move
        :args (s/cat :rules rules?
                     :db db/database?
                     :loc db/location?)
        :ret boolean?)

;; The legal locations the piece at the given location can be moved to.
#_(s/def ::targets  (s/fspec :args (s/cat :db map? :loc location?)
                               :ret (s/every location? :kind vector?)))

;; The legal results of a completed game.
#_(def game-result? #{::white-win ::black-win ::draw})

;; The result of the game, if complete.
#_(s/def game-result (s/fspec :args (s/cat :db map?)
                            :ret (s/or :going nil?
                                       :over game-result?)))

 

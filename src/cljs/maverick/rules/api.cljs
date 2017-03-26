(ns maverick.rules.api
  (:require [maverick.db :as db]
            [maverick.rules.protocol :as proto]))

(defn filter-targets
  "Filter the legal target moves from the threats."
  [threats attack-color]
  (->> threats 
       (filter (fn [[_ {:keys [::db/color ::db/depth]}]]
                 (and (not= color attack-color)
                      (= depth 0))))
       (into {})))


;;------------------------------------------------------------------------------
;; Rules API. 
;;------------------------------------------------------------------------------

(defn init-db
  "Generate the initial value of the database.
   - The rules of the game."
  [rules]
  (proto/init-db rules)) 

(defn in-bounds?
  "Whether the location is within the bounds of the board.
   - The rules of the game.
   - The application database.
   - The location in question."
  [rules db loc]
  (proto/in-bounds? rules db loc))

(defn threats
  "The information about the squares on the board threatened by a piece this 
   move.
   - The rules of the game.
   - The application database.
   - The location of the piece being moved.

   Returns a map indexed by location of the square being threatened which 
   contains:
   - `:depth` The number of pieces blocking the attack on the square.
   - `:kind` The kind of piece occupying the square (optional).
   - `:color` The color of the piece occupying the square (optional).
   - `:took-location` The location of the piece that was taken as a result of 
   a move. This is only needed for moves like en passant which have
   a different location than the square being threatened."
  [rules db loc]
  (proto/threats rules db loc))

(defn targets
  "The set of locations for squares where the piece can be legally moved.
   - The rules of the game.
   - The application database.
   - The location of the piece being moved."
  [rules db loc]
  (let [color (-> db ::db/current-move ::db/color)]
    (-> (proto/threats rules db loc)
        (filter-targets color)
        (keys)
        (set))))
                                       
(defn game-result
  "The result of the game, if completed.
   - The rules of the game.
   - The application database.
   
   Returns `:white-wins`, `:black-wins`, `:draw` or `nil` if the game 
   continues."
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

 

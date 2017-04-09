(ns maverick.rules.api
  (:require [maverick.db :as db]
            [maverick.rules.protocol :as proto]))

(defn- select-moves
  "Select move locations from the set of threats which match a given criteria.
   - `threats` The map of all threats.
   - `attack-color` The `:color` of the player whose turn it is to move.
   - `direct?` Whether to select direct (as opposed to indirect) threats."
  [threats attack-color direct?]
  (->> threats 
       (filter (fn [[_ {:keys [::db/color ::db/direct]}]]
                 (and (not= color attack-color)
                      (= direct direct?))))
       (into {})
       (keys)
       (set)))


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
   - `rules` The rules of the game.
   - `db`    The application database.
   - `loc`   The location of the piece being moved.

   Returns a map indexed by location of the square being threatened which 
   contains:
   - `:direct` Whether the threat is direct and therefore a legal next move.
   Indirect threats are those which would be legal moves if a single enemy 
   piece was removed from the board.
   - `:kind`   The kind of piece occupying the square (optional).
   - `:color`  The color of the piece occupying the square (optional).
   - `:took-location` The location of the piece that was taken as a result of 
   a move. This is only needed for moves like en passant which have
   a different location than the square being threatened."
  [rules db loc]
  (proto/threats rules db loc))

(defn targets
  "The set of locations for squares where the piece can be legally moved.
   - `rules` The rules of the game.
   - `db`    The application database.
   - `loc`   The location of the piece being moved."
  [rules db loc]
  (let [color (-> db ::db/current-move ::db/color)]
    (-> (proto/threats rules db loc)
        (select-moves color true))))

(defn xrays
   "The set of locations for squares which would be targeted except for a 
   single blocking enemy piece. 
   - `rules` The rules of the game.
   - `db`    The application database.
   - `loc`   The location of the piece being moved."
  [rules db loc]
  (let [color (-> db ::db/current-move ::db/color)]
    (-> (proto/threats rules db loc)
        (select-moves color false))))
                                       
(defn game-result
  "The result of the game, if completed.
   - `rules` The rules of the game.
   - `db`    The application database.
   
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

 

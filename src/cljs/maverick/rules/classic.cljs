(ns maverick.rules.classic
  (:require [maverick.db :as db]
            [maverick.rules.protocol :as proto]))

;;------------------------------------------------------------------------------
;; Helpers. 
;;------------------------------------------------------------------------------

(defn- add
  "2D vector addition."
  [[ai aj] [bi bj]]
  [(+ ai bi) (+ aj bj)])

;;
;; Piece Threats.
;;

(defn- pawn-threats
  [db loc]
  (let [plocs (-> db ::db/current-position ::db/locations)
        attack-color (-> db ::db/current-move ::db/color) 
        [start-j step] (case attack-color
                         ::db/white [1 [0 1]]
                         ::db/black [6 [0 -1]])
        [single double right left eright eleft]
        (let [s (add loc step)
              d (add s step)
              r (add s [1 0])
              l (add s [-1 0])]        
          (->> [s d r l]
               (map (fn [lc] [lc (-> (get plocs lc)
                                    (assoc ::db/depth 0))]))))
        
        moves (when-not (-> single second ::db/kind)
                (if (or (-> double second ::db/kind)
                        (not= start-j (second loc)))
                  (into {} [single])
                  (into {} [single double])))
        
        takes (->> [right left]
                   (filter (fn [[_ {:keys [::db/color]}]]
                             (and color (not= color attack-color))))
                   (map (fn [[lc p]] [lc (assoc p ::db/depth 0)]))
                   (into {}))
        
        en-passant
        (let [{:keys [::db/kind ::db/start-location ::db/end-location ]
               :as last-move}
              (-> db ::db/moves last)
              ep-js (case attack-color
                      ::db/white [6 4]
                      ::db/black [1 3])
              [_ sj] start-location
              [_ ej] end-location]
          (some->> [(add loc [-1 0]) (add loc [1 0])]
                   (filter #(and (= kind ::db/pawn)
                                 (= end-location %)
                                 (= [sj ej] ep-js)))
                   (first)
                   ((fn [lc]
                      {(add lc step) 
                       (-> (select-keys last-move [::db/kind ::db/color])
                           (assoc ::db/depth 0
                                  ::db/took-location end-location))}))))]
    (merge moves takes en-passant)))

(defn- knight-threats
  [rules db loc]
  (let [plocs (-> db ::db/current-position ::db/locations)]
    (->> [[1 2] [1 -2] [-1 2] [-1 -2]
          [2 1] [-2 1] [2 -1] [-2 -1]]
         (map (partial add loc))
         (filter (partial proto/in-bounds? rules db))
         (map (fn [lc]
                [lc (assoc (get plocs lc) ::db/depth 0)]))
         (into {}))))


(defn- slider
  [rules db loc delta]
  (let [plocs (-> db ::db/current-position ::db/locations)
        attack-color (-> db ::db/current-move ::db/color)]
    (loop [ts []
           lc (add loc delta)
           dp 0]
      (if-not (proto/in-bounds? rules db lc)
        (into {} ts)
        (let [{:keys [::db/color] :as p} (assoc (get plocs lc) ::db/depth dp)
              ndp (if color
                    (when (not= color attack-color)
                      (inc dp))
                    dp)]
          (if-not ndp
            (into {} ts)
            (recur (conj ts [lc p])
                   (add lc delta)
                   ndp)))))))

(defn- bishop-threats
  [rules db loc]
  (merge (slider rules db loc [1 1])
         (slider rules db loc [-1 1])
         (slider rules db loc [-1 -1])
         (slider rules db loc [1 -1])))

(defn- rook-threats
  [rules db loc]
  (merge (slider rules db loc [1 0])
         (slider rules db loc [-1 0])
         (slider rules db loc [0 1])
         (slider rules db loc [0 -1])))

(defn- queen-threats
  [rules db loc]
  (merge (bishop-threats rules db loc)
         (rook-threats rules db loc)))


;;------------------------------------------------------------------------------
;; Rules Protocol Implementation.
;;------------------------------------------------------------------------------

(def rules
  (reify proto/Rules
    (init-db [_]
      (let [place (fn [i j color kind]
                    [[i j] {::db/color color ::db/kind kind}])

            pawns (fn [c j]
                    (into {} (for [i (range 0 8)]
                               (place i j c ::db/pawn))))
            majors (fn [c j]
                     (into {}
                           [(place 0 j c ::db/rook)
                            (place 1 j c ::db/knight)
                            (place 2 j c ::db/bishop)
                            (place 3 j c ::db/queen)
                            (place 4 j c ::db/king)
                            (place 5 j c ::db/bishop)
                            (place 6 j c ::db/knight)
                            (place 7 j c ::db/rook)]))]
        {::db/look {::db/board-size ::db/medium}
         ::db/board {::db/cols 8
                     ::db/rows 8
                     ::db/orient ::db/white}
         ::db/rules ::db/classic
         ::db/current-move {::db/move-number 1
                            ::db/color ::db/white
                            ::db/start-stamp (.now js/Date)}
         ::db/moves []
         ::db/current-position {::db/locations (merge (majors ::db/black 7)
                                                      (pawns ::db/black 6)
                                                      (pawns ::db/white 1)
                                                      (majors ::db/white 0))}
         ::db/positions []}))
  
    (in-bounds? [_ db [i j]]
      (let [{:keys [::db/board]} db
            {:keys [::db/rows ::db/cols]} board]
        (and i j
             (and (<= 0 i) (< i cols))
             (and (<= 0 j) (< j rows)))))

    (threats [this db loc]
      (let [{:keys [::db/kind ::db/color]}
            (-> db ::db/current-position ::db/locations (get loc))
            attack-color (-> db ::db/current-move ::db/color)]
        (when (= attack-color color)
          (case kind
            ::db/pawn (pawn-threats db loc)
            ::db/knight (knight-threats this db loc)
            ::db/bishop (bishop-threats this db loc)
            ::db/rook (rook-threats this db loc)
            ::db/queen (queen-threats this db loc)
            nil))))

    (game-result [this db]

      ;; placeholder
      
      nil)))

(ns maverick.rules.protocol)

(defprotocol Rules
  ""
  (init-db [this])
  (in-bounds? [this db loc])
  (can-move? [this db loc])
  (destinations [this db loc])
  (game-result [this db]))


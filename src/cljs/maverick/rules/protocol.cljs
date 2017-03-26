(ns maverick.rules.protocol)

(defprotocol Rules
  ""
  (init-db [this])
  (in-bounds? [this db loc])
  (threats [this db loc])
  (game-result [this db]))


(ns maverick.rules.core
  (:require [maverick.db :as db]
            [maverick.rules.classic :as classic]))

(defn rules-for 
  [key]
  (key {::db/classic classic/rules}))

(defn cur-rules
  [db]
  (rules-for (::db/rules db)))

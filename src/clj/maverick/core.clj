(ns maverick.core 
  (:require [ring.middleware.resource :refer [wrap-resource]]
            [ring.util.response :as rsp]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [compojure.core :refer [defroutes routes GET]]))

(defn init []
  (println "Maverick is Starting..."))

(defn destroy []
  (println "Maverick is Shutting Down..."))

(defroutes app-routes
  (GET "/" [] (rsp/resource-response "index.html" {:root "public"}))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (routes app-routes)
      (handler/site)))

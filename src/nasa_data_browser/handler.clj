(ns nasa-data-browser.handler
  (:use [compojure.core]
        [ring.middleware.resource]
        [ring.middleware.file-info])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [compojure.response :as response]
            [clojure.java.io :as io]
            [nasa-data-browser.templates :as templates]
            [nasa-data-browser.parameters :as parameters]
            [nasa-data-browser.variables :as variables]
            [nasa-data-browser.comparison :as comparison]
            [nasa-data-browser.info :as info]
            [nasa-data-browser.utils :as u]))
(comment (def endpoint "http://localhost:8080/openrdf-sesame/repositories/nasa"))
(def endpoint "http://volume456.allegrograph.net:10035/repositories/nasa")

(defroutes app-routes
  (GET "/templates/:view" [view]       
       (-> (templates/get-data view)
           u/json-response))
  (GET "/parameters" []
       (-> (parameters/get-data endpoint)
           u/json-response))
  (GET "/variables/:parameter" [parameter]
       (-> (variables/get-data parameter endpoint)
           u/json-response))
  (GET "/comparison" {{vars :vars} :params}
       (-> (comparison/get-data vars endpoint)
           u/json-response))
  (GET "/info/:item" [item]
       (-> (info/get-data item endpoint)
           u/json-response))
  (route/resources "/")
  (route/not-found "Not Found"))
  
(def app
  (-> app-routes
      handler/site))
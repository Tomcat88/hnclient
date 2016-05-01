(ns hnclient.hnclient
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]))

(def hn-endpoint "https://hacker-news.firebaseio.com/v0/")

(defn topstories
  ([limit]
   (take limit (topstories)))
  ([]
   (try
     (-> (str hn-endpoint "topstories.json") (client/get {:as :clojure :throw-exceptions true}) :body )
     (catch Exception e (do (println "Exception while retrieving stories" (.getMessage e))
                            []))
     )
  ))

(defn item [id]
  (-> (str hn-endpoint "item/" id ".json") (client/get {:as :json}) :body json/read-str))

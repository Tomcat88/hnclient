(ns hnclient.core
  (:gen-class)
  (:require [hnclient.hnclient :as hnclient]
            [clojure.data.json :as json]
            [hnclient.screen :as screen]))

(defn pretty-string [story]
  (str (get story "title") " - " (get story "by")))

(defn key-listener [char]
  (case char
    \q (do (screen/close-screen)
           (println "Exiting...")
           false)
    :else (println "Unkwnown command")
      ))

(defn -main
  "main cli loop"
  [& args]
  (println "** Hacker News Client **")
  (screen/init-screen)
  (screen/writeln "** Hacker News Client **")
  (if-let [entries (for [story-id (hnclient/topstories 10)]
                    (let [story (hnclient/item story-id)]
                      {:id story-id :title (pretty-string story)})
                    )]
    (screen/write-entries entries)
    (screen/writeln "No entries! (Is this computer connected to the Internet?)")
  )
  #_(doseq [story-id (hnclient/topstories 10)]
    (let [story (hnclient/item story-id)]
      (println "Writing entry " story-id)
      (screen/write-entry {:id story-id :title (pretty-string story)}))
    )
  (screen/input key-listener)
  )

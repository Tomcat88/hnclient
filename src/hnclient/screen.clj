(ns hnclient.screen
  (:require [lanterna.screen :as s]))

(def scr (atom (s/get-screen :swing)))
(def cursor (atom {:x 0 :y 0}))
(def screen-entries (atom {}))
(def current-entry (atom {}))

(defn redraw []
  (let [{:keys [x y]} @cursor]
    (s/move-cursor @scr x y)
    (s/redraw @scr)))

(defn move-cursor [point]
  (let [{:keys [x y]} point]
    (reset! cursor {:x x :y y})))

(defn ln []
  (let [{:keys [x y]} @cursor]
    (move-cursor {:x x :y (inc y)})))

(defn add-entry [id number point]
  (swap! screen-entries assoc number {:id id :number number :point point}))

(defn set-current-entry [entry]
  (reset! current-entry entry))

(defn move-cursor-to-entry [entry]
  (move-cursor (:point entry))
  (set-current-entry entry))

(defn move-cursor-by [op]
  (let [current (:number @current-entry)
        last (->> @screen-entries count (get @screen-entries) :number)
        res (op current)
        next (cond
               (< res 1) last
               (> res last) 1
               :else res)]
    #_(println current last next)
    (move-cursor-to-entry (get @screen-entries next))
    (redraw)
    true))

(defn init-screen []
  (s/start @scr))

(defn close-screen []
  (s/stop @scr))

(defn writeln [line & [should-redraw]]
  (let [{:keys [x y] :as old} @cursor]
    (s/put-string @scr x y line)
    (ln)
    (if (or (nil? should-redraw) should-redraw) (redraw))
    old))

(defn input
  ([listener] (input listener true))
  ([listener continue]
   (if continue
     (let [key (s/get-key-blocking @scr)]
       (->> (case key
              :up (move-cursor-by dec)
              :down (move-cursor-by inc)
              (listener key))
            (recur listener))
       ))))

(defn write-entry [entry]
  (let [{:keys [id title]} entry
        entry-n (-> @screen-entries count inc)
        entry-str (str entry-n " - " title)]
    (add-entry id entry-n (writeln entry-str false))))

(defn write-entries [entries]
  (do(doseq [entry entries]
       (write-entry entry))
     (move-cursor-to-entry (get @screen-entries 1))
     (redraw)))

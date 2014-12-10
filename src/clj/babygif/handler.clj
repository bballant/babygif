(ns babygif.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [clj-time.core :as t]
            [hiccup.core :refer :all]
            [babygif.templates :refer :all]))

(def directory
  (clojure.java.io/file "gifs"))

; gets info map from gif file
(defn gif-info [gif-file]
  (let [date-seq
          (re-seq #"([0-9]{4})-([0-9]{2})-([0-9]{2})"
                  (.getName gif-file))
        [full-date-str & year-month-day-strs]
          (first date-seq)
        date-nums
          (if (empty? year-month-day-strs) [1970 1 1]
            (map #(if (nil? %) 0 (Integer/parseInt %))
                 year-month-day-strs))]
      {:name (.getName gif-file)
       :path (str "/" (.getPath gif-file))
       :date (apply t/date-time date-nums)}))

(defn gif-infos-for-month [n gif-infos]
  (filter (fn [info] (= (.getMonthOfYear (:date info)) n)) gif-infos))
      
(def gif-info-seq
  (map gif-info
       (filter #(.endsWith (.getName %) ".gif")
               (file-seq directory))))

(defn rando-gif-html []
  (f-html (rand-nth gif-info-seq)))

(defroutes app-routes
  (GET "/" [] (files-html gif-info-seq))
  (GET "/rando" [] (layout "randobabygif" (rando-gif-html)))
  (GET "/test" [] (layout "testtesttest" (draw-menu gif-info-seq)))
  (GET "/mo/:n" [n] (files-html (gif-infos-for-month (Integer/parseInt n) gif-info-seq)))
  ;(GET "/:n" [n] (file-html files (Integer/parseInt n)))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
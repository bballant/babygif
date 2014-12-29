(ns babygif.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [clj-time.core :as t]
            [hiccup.core :refer :all]
            [babygif.templates :refer :all]
            [babygif.gifinfo :refer :all]))

; (def directory
;   (clojure.java.io/file "gifs"))
; 
; ; gets info map from gif file
; (defn gif-info [gif-file]
;   (let [date-seq
;           (re-seq #"([0-9]{4})-([0-9]{2})-([0-9]{2})"
;                   (.getName gif-file))
;         [full-date-str & year-month-day-strs]
;           (first date-seq)
;         date-nums
;           (if (empty? year-month-day-strs) [1970 1 1]
;             (map #(if (nil? %) 0 (Integer/parseInt %))
;                  year-month-day-strs))]
;       {:name (.getName gif-file)
;        :path (str "/" (.getPath gif-file))
;        :date (apply t/date-time date-nums)}))

(def gif-info-list
  (read-gif-info-list "db.json"))

(defn gif-infos-for-month [n gif-infos]
  (filter (fn [info] (= (.getMonthOfYear (:date info)) n)) gif-infos))
      
; (def gif-info-seq
;   (map gif-info
;        (filter #(.endsWith (.getName %) ".gif")
;                (file-seq directory))))
; 

; (defn gif-html []
;   (f-html (rand-nth gif-info-list)))

(defn render-year-month [ystr mstr params]
  (let [[y m] (map #(Integer/parseInt %) [ystr mstr])
        month-gif-infos
          (sort-by #(.getDayOfMonth (% :date))
            (filter
              #(= [y m] [(.getYear (% :date)) (.getMonthOfYear (% :date))])
              gif-info-list))
        infos-by-day (partition-by
                       #(.getDayOfMonth (% :date)) month-gif-infos)
        default-gif-info
          (let [infos-at-day
                (nth infos-by-day (Integer/parseInt (get params :d "0")))]
            (nth infos-at-day (Integer/parseInt (get params :i "0"))))]
    (layout "babygif"
            (thumbnails infos-by-day
                        (f-html default-gif-info)))))

(defroutes app-routes
  ;(GET "/" [] (files-html gif-info-seq))
  ;(GET "/timeline" [] (layout "timeline" (timeline (rando-gif-html))))
  ;(GET "/rando" [] (layout "randobabygif" (rando-gif-html)))
  ;(GET "/test" [] (layout "testtesttest" (draw-menu gif-info-seq)))
  ; (GET "/mo/:n" [n]
  ;      (files-html (gif-infos-for-month (Integer/parseInt n) gif-info-seq)))
  ;(GET "/:n" [n] (file-html files (Integer/parseInt n)))
  (GET "/" {params :params} [] (str "hey " params))
  (GET "/t/:y/:m" [y m :as {p :params}] (render-year-month y m p))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))

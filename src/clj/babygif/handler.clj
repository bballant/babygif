(ns babygif.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [clj-time.core :as t]
            [hiccup.core :refer :all]
            [babygif.templates :refer :all]
            [babygif.gifinfo :refer :all]))

(def gif-info-list
  (read-gif-info-list "db.json"))

(def one-info-per-month-list
  (let [infos-mo (group-by #(vec [(iyear %) (imonth %)])
                           (filter
                             #(not (and (= 2014 (iyear %)) (< 6 (imonth %))))
                             gif-info-list))]
    (for [mo (sort (keys infos-mo))]
      (first (infos-mo mo)))))

(defn gif-infos-for-month [n gif-infos]
  (filter (fn [info] (= (.getMonthOfYear (:date info)) n)) gif-infos))
      
(defn render-year-month [ystr mstr params]
  (let [intparamf (fn [sym dflt] (Integer/parseInt
                                   (get params sym (str dflt))))
        [y m]     (map #(Integer/parseInt %) [ystr mstr])
        infos4mo  (sort-by iday (filter #(= [y m] [(iyear %) (imonth %)])
                                       gif-info-list))
        infos-day (group-by iday infos4mo)
        info-dflt (let [dayinfos (get infos-day (intparamf :d 0))]
                    (nth dayinfos (intparamf :i 0)))
        info      (if (nil? info-dflt) (first infos4mo) info-dflt)]
    (layout "babygif"
            (main-content (topthumbs one-info-per-month-list)
                          (f-html info)
                          (thumbnails infos-day)))))

(defroutes app-routes
  (GET "/" {params :params} [] (str "hey " params))
  (GET "/t/:y/:m" [y m :as {p :params}] (render-year-month y m p))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))

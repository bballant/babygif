(ns babygif.templates
  (:require [hiccup.core :refer :all]
            [hiccup.page :refer :all]
            [clj-time.format :as df]
            [babygif.gifinfo :refer :all]))

(def date-fmt
  (df/formatter "E, MMM d YYYY"))

(def month-year-fmt
  (df/formatter "MMM"))

(defn fmt-date [d fmt]
  (if (= (.getYear d) 1970) ""
    (df/unparse fmt d)))

(defn layout [title & content]
  (html5 {:lang "en"}
         [:head [:title title]
          (include-css "/css/main.css")]
         [:body [:div {:class "container"}
                 [:h1 "Noa & Ava Year One in Gifs"]
                 content]
          (include-js "/js/main.js")]))

(defn main-content [topnav middle botnav]
  [:div topnav middle botnav])

(defn topthumb [info]
  [:div [:label (fmt-date (info :date) month-year-fmt)]
   [:a {:href (str "/t/" (iyear info) "/" (imonth info))}
    [:img {:src (str "/thms/" (info :name) "-thm.gif")}]]])

(defn topthumbs [infos]
  [:div {:class "topnav"} (map topthumb infos)])

(defn f-html [f]
  [:div {:class "main"}
   [:div (fmt-date (f :date) date-fmt)]
   [:div [:img {:src (str "/gifs/" (f :name) ".gif") }]]
   [:div {:style "display:none;"} (:name f)]])

(defn thumb [info i]
  [:li 
   [:a {:href
        (str "/t/" (iyear info) "/" (imonth info)
             "?d=" (iday info) "&i=" i)}
    [:img
     {:src (str "/thms/" (info :name) "-thm.gif")}]]])

(defn thumbnails [infos-day]
  [:div {:class "timeline"}
   (for [day (sort (keys infos-day))]
     (let [infos (infos-day day)]
       [:div {:class "column"}
        [:label (str day)]
        [:ul
         (map #(thumb %1 %2) infos (iterate inc 0))]]))])


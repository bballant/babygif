(ns babygif.templates
  (:require [hiccup.core :refer :all]
            [hiccup.page :refer :all]
            [clj-time.format :as df]))

(def date-fmt
  (df/formatter "E, MMM d YYYY"))

(defn fmt-date [d]
  (if (= (.getYear d) 1970) ""
    (df/unparse date-fmt d)))

(defn layout [title & content]
  (html5 {:lang "en"}
         [:head [:title title]
          (include-css "css/main.css")]
         [:body [:div {:class "container"}
                 content]
          (include-js "js/main.js")]))

(defn menu-cell [d url]
  (html [:span {:class "menucell"
                :onclick (str "location.href=" url)} "&nbsp;"]))

(defn draw-menu [files]
  (html [:div {:class "menu"}
         (for [f files]
          (menu-cell (:date f) (:path f)))]))

(defn f-html [f]
  (html [:div {:class "main"}
         [:div (fmt-date (:date f))]
         [:div [:img {:src (:path f) }]]
         [:div {:style "display:none;"} (:name f)]
         [:div [:a {:href "/rando"} "next"]]]))

(defn files-html [files]
  (html [:ul
         (for [f files]
           [:li (f-html f)])]))

(defn timeline [tops]
  [:div tops
  [:div {:class "timeline"}
    (for [i (range 31)]
      [:div {:class "column"}
       [:label (str (+ i 1))]
       [:ul
        (for [i (range (+ 1 (rand 3)))]
          [:li [:img {:src "/img/test.gif"}]])]])]])

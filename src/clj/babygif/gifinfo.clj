(ns babygif.gifinfo
  (:require [clojure.data.json :as json]
            [clj-time.core :as t]))

(defn read-gif-info-json [filename]
  (json/read (clojure.java.io/reader filename)))

(defn datestr->date [datestr]
  (let [{year 0, month 1, day 2 :or {year 1970, month 1, day 1}}
         (vec (map #(Integer/parseInt %) (re-seq #"\d+" datestr)))]
    (t/date-time year month day)))

(defn gif-info [gif-map]
  {:name (gif-map "filename")
   :date (datestr->date (gif-map "datestring"))})

(defn read-gif-info-list [filename]
  (map gif-info (read-gif-info-json filename)))

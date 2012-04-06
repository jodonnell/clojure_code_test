(ns jacob_clojure_test.core
  (:require [clojure.string :as string]))
(import [org.joda.time.format DateTimeFormat])

(defn date [string]
  (.parseLocalDate (DateTimeFormat/forPattern "MM/dd/yyyy") string))

(defn pretty-person [person]
  (string/join " " [(get person :last) (get person :first) (get person :gender) (.toString (get person :birthday) "MM/dd/yyyy") (get person :color)]))

(defn split-on-newline [string]
  (string/split string #"\n"))

(defn split-on-space [string]
  (string/split string #" "))

(defn split-on-comma [string]
  (string/split string #", "))

(defn split-on-pipe [string]
  (string/split string #" \| "))

(defn get-file-lines [filename]
  (split-on-newline (string/replace (slurp filename) "-" "/")))

(defn pretty-gender [string]
  (case string "M" "Male" "F" "Female" string))

(defn gender-normalize [person]
  (conj person [:gender (pretty-gender (:raw-gender person))]))
                 
(defn string-to-date [person]
  (conj person [:birthday (date (:raw-birthday person))]))

(defn space-create-person [person-fields]
  (zipmap [:last :first :middle :raw-gender :raw-birthday :color] person-fields))

(defn comma-create-person [person-fields]
  (zipmap [:last :first :raw-gender :color :raw-birthday] person-fields))

(defn pipe-create-person [person-fields]
  (zipmap [:last :first :middle :raw-gender :color :raw-birthday] person-fields))

(defn space-fields []
  (map split-on-space (get-file-lines "src/jacob_clojure_test/space.txt")))

(defn pipe-fields []
  (map split-on-pipe (get-file-lines "src/jacob_clojure_test/pipe.txt")))

(defn comma-fields []
  (map split-on-comma (get-file-lines "src/jacob_clojure_test/comma.txt")))

(defn space-records []
  (map space-create-person (space-fields)))

(defn comma-records []
  (map comma-create-person (comma-fields)))

(defn pipe-records []
  (map pipe-create-person (pipe-fields)))

(defn all-records []
  (map gender-normalize (map string-to-date (into (into (space-records) (comma-records)) (pipe-records)))))

(defn sort-by-gender-last-name []
  (sort-by #(vec (map % [:gender :last])) (all-records)))

(defn sort-by-last-name-desc []
  (reverse (sort-by :last (all-records))))

(defn sort-by-date-then-last-name []
  (sort-by #(vec (map % [:birthday :last])) (all-records)))

(defn print-results []
  (println (str
            "Output 1:\n"
            (string/join "\n" (map pretty-person (sort-by-gender-last-name)))
            "\n\n"
            "Output 2:\n"
            (string/join "\n" (map pretty-person (sort-by-date-then-last-name)))
            "\n\n"
            "Output 3:\n"
            (string/join "\n" (map pretty-person (sort-by-last-name-desc))))))

(defn -main [& args]
  (print-results))

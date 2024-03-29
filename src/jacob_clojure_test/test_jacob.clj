(use '[clojure.string :only (split replace)])
(import [org.joda.time.format DateTimeFormat])

(defn date [string]
  (.parseLocalDate (DateTimeFormat/forPattern "MM-dd-yyyy") string))

(defn print-person [person]
  (println (get person :last) (get person :first) (get person :gender) (get person :birthday) (get person :color)))

(defn split-on-newline [string]
  (split string #"\n"))

(defn split-on-space [string]
  (split string #" "))

(defn split-on-comma [string]
  (split string #", "))

(defn split-on-pipe [string]
  (split string #" \| "))

(defn get-file-lines [filename]
  (split-on-newline (slurp filename)))

(defn gender-normalize [person]
  (merge person {:gender 
                 (case (get person :gender)
                   "M" "Male"
                   "F" "Female"
                   (get person :gender))}))

(defn date-normalize [person]
  (merge person {:date
                 (replace (get person :date) "-" "/")}))

(defn remove-middle-name [records]
  (concat (subvec records 0 2) (subvec records 3)))

(defn space-create-person [person-fields]
   (zipmap [:last :first :gender :birthday :color] person-fields))

(defn comma-create-person [person-fields]
   (zipmap [:last :first :gender :color :birthday] person-fields))

(defn pipe-create-person [person-fields]
   (zipmap [:last :first :gender :color :birthday] person-fields))

(defn space-records []
  (map gender-normalize (map space-create-person (map remove-middle-name (map split-on-space (get-file-lines "space.txt"))))))

(defn comma-records []
  (map comma-create-person (map split-on-comma (get-file-lines "comma.txt"))))

(defn pipe-records []
  (map gender-normalize (map pipe-create-person (map remove-middle-name (map split-on-pipe (get-file-lines "pipe.txt"))))))

(defn all-records []
  (into (into (space-records) (comma-records)) (pipe-records)))

(defn sort-by-gender-last-name []
  (sort-by #(vec (map % [:gender :last])) (all-records)))

(defn sort-by-last-name-desc []
  (reverse (sort-by :last (all-records))))

(defn sort-by-date-then-last-name []
  (sort-by :last (all-records)))

(defn print-results []
  (println "Output 1:")
  (doall (map print-person (sort-by-gender-last-name)))
  (println "")
  (println "Output 2:")
  (doall (map print-person (sort-by-date-then-last-name)))
  (println "")
  (println "Output 3:")
  (doall (map print-person (sort-by-last-name-desc))))



(print-results)

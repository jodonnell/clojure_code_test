; sum of all numbers between 1...999 that are divisible by 3 or 5
; 233168

;(use '[clojure.java.io :only (slurp)])
(use '[clojure.string :only (split)])
(use '[clojure.set])

;; (let [list '()]
;;      (dotimes [number 1000] (conj list number)))
     
;(defn all-nums []
;  (dotimes [number 1000]
;	 (when (or (= (mod number 5) 0) (= (mod number 3) 0))
;	   number)))

;(println (all-nums))


;(defn baby [number]
;  (if (< number 1000)
;      (conj (baby (inc number))))

;(baby 1)




;(defn pop [p] (if (= 1 p) 1 (conj (pop (- p 1)) p)))

;; (let [num 0]
;;      (dotimes [number 1000]
;; 	      (if (or (= (mod number 5) 0) (= (mod number 3) 0))
;; 		  (+ num number)))
;;      (println num))

;; (def vowel? (set "aoeui"))

;; (defn pig-latin [word]
;;   (let [first-letter (first word)]
;;        (if (vowel? first-letter)
;; 	   (str word "ay")
;; 	 (str (subs word 1) first-letter "ay"))))
       

;; (println (pig-latin "boom"))
;; (println (pig-latin "apple"))

(defn eat [filename] (split (slurp filename) #"\n"))
(defn people [] (flatten (map eat ["pipe.txt" "comma.txt" "space.txt"])))

(defn pipey? [line] (re-matches #".*\|.*" line))
(defn commay? [line] (re-matches #".*,.*" line))
(defn spacey? [line] (and (not (commay? line)) (not (pipey? line))))

(defn pipey-lines [] (filter pipey? (people)))
(defn commay-lines [] (filter commay? (people)))
(defn spacey-lines [] (filter spacey? (people)))

(defn parse-gender [gender-string] (if (= \F (first gender-string)) "Female" "Male"))
(defn replace-gender [person] (conj (dissoc person :gender) [:gender (parse-gender (:gender person))]))

(defn parse-date [date-string] date-string)
(defn replace-date [person] person)

(defn parse [mapped-person] (replace-date (replace-gender mapped-person)))

(defn space-person [space-line] (parse (zipmap [:lastname :firstname :middle :gender :birthday :color] (split space-line #" "))))
(defn comma-person [comma-line] (parse (zipmap [:lastname :firstname :gender :color :birthday] (split comma-line #", "))))
(defn pipe-person [pipe-line] (parse (zipmap [:lastname :firstname :middle :gender :color :birthday] (split pipe-line #" \| "))))

;(defn pretty [person] (map person [:lastname :firstname :gender :birthday :color]))

(println (map pipe-person (pipey-lines)))
(println (map comma-person (commay-lines)))
(println (map space-person (spacey-lines)))

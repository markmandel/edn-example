(ns edn-example.core
    (require [clojure.edn :as edn])
    (:gen-class))

(def sample-map {:foo "bar" :bar "foo"})
(def sample-vector [1 2 3 4 "five" true])

(defn convert-sample-map-to-edn
    "Converting a Map to EDN"
    []
    ;; yep, converting a map to EDN is that simple
    (str sample-map))

(defn convert-sample-vector-to-edn
    "Converting a Vector to EDN"
    []
    ;; yep, converting a vector to EDN is that simple
    (str sample-vector))

(defn -main
    "Show off the EDN examples"
    [& args]
    (print "Let's convert a map to EDN: ")
    (println (convert-sample-map-to-edn))
    (print "Now let's covert the map back: ")
    (println (edn/read-string (convert-sample-map-to-edn)))
    (print "Let's convert a vector to EDN: ")
    (println (convert-sample-vector-to-edn))
    (print "Now let's covert the vector back: ")
    (println (edn/read-string (convert-sample-vector-to-edn))))




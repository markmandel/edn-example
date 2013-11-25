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

;;lets get more fancy, and convert a defrecord to and from EDN
(defrecord Goat [stuff things]
    Object
    ;; We overwrite toString for this defrecord to keep up with (str ...) being
    ;; able to output EDN directly.
    ;; We will make '#edn-example.Goat' out tag, and then use the stardart toString
    ;; for a Clojure map to do the rest.
    (toString [this] (str "#edn-example.Goat" (into {} this))))

(def sample-goat (->Goat "I love Goats", "Goats are awesome"))

(defn convert-sample-goat-to-edn
    "Converting a Goat to EDN"
    []
    (str sample-goat))

(defn fail-converting-edn-to-goat
    "This won't work, as the reader won't recognise the #edn.example.Goat tag"
    []
    (try
        (edn/read-string (convert-sample-goat-to-edn))
        (catch Exception e (str "Caught Exception: " (.getMessage e)))))

;; This is a map of reader functions that match up to the #tags we have.
;; we can use map->Goat, as we get back a map from the EDN block after the tag
;; deserialises as a map, and we can just pass that through.
(def edn-readers {'edn-example.Goat map->Goat})

(defn convert-edn-to-goat
    []
    (edn/read-string {:readers edn-readers} (convert-sample-goat-to-edn)))

(defn -main
    "Show off the EDN examples"
    [& args]
    (println "Let's convert a map to EDN: " (convert-sample-map-to-edn))
    (print "Now let's covert the map back: ")
    (println (edn/read-string (convert-sample-map-to-edn)))
    (println "Let's convert a vector to EDN: " (convert-sample-vector-to-edn))
    (print "Now let's covert the vector back: ")
    (println (edn/read-string (convert-sample-vector-to-edn)))
    (println "Let's convert our defrecord Goat into EDN: " (convert-sample-goat-to-edn))
    (println "Let's try converting a Goat back to EDN, but it will fail: " (fail-converting-edn-to-goat))
    (println "Let's try converting a Goat back to EDN: " (convert-edn-to-goat)))




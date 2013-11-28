(ns edn-example.core
    (require [clojure.edn :as edn])
    (:gen-class))

(def sample-map {:foo "bar" :bar "foo"})
(def sample-vector [1 2 3 4 "five" true])

(defn convert-sample-map-to-edn
    "Converting a Map to EDN"
    []
    ;; yep, converting a map to EDN is that simple"
    (prn-str sample-map))

(defn convert-sample-vector-to-edn
    "Converting a Vector to EDN"
    []
    ;; yep, converting a vector to EDN is that simple
    (prn-str sample-vector))

;;lets get more fancy, and convert a defrecord to and from EDN
(defrecord Goat [stuff things])

(def sample-goat (->Goat "I love Goats", "Goats are awesome"))

(defn convert-sample-goat-to-edn
    "Converting a Goat to EDN"
    []
    (prn-str sample-goat))

(defn fail-converting-edn-to-goat
    "This won't work, as the reader won't recognise the #edn.example.Goat tag"
    []
    (try
        (edn/read-string (convert-sample-goat-to-edn))
        (catch Exception e (str "Caught Exception: " (.getMessage e)))))

;; This is a map of reader functions that match up to the #tags we have.
;; we can use map->Goat, as we get back a map from the EDN block after the tag
;; deserialises as a map, and we can just pass that through.
(def edn-readers {'edn_example.core.Goat map->Goat})

(defn convert-edn-to-goat
    "Convert EDN back into a Goat. We will use the :readers option to pass through a map
    of tags -> readers, so EDN knows how to handle our custom EDN tag."
    []
    (edn/read-string {:readers edn-readers} (convert-sample-goat-to-edn)))

(defn alternative-edn-for-goat
    "Creates a different edn format for the goat. Flattens the goat map into a sequence of keys and values"
    [^Goat goat]
    (str "#edn-example/Alt.Goat" (prn-str (mapcat identity goat))))

(defn convert-alt-goat-edn
    "Takes the altenative EDN and converts it into a Goat"
    [elems]
    (map->Goat (apply hash-map elems)))

(defn convert-alt-edn-to-goat
    "Convert the alternative edn format for a goat back to a Goat"
    []
    (edn/read-string {:readers {'edn-example/Alt.Goat convert-alt-goat-edn}} (alternative-edn-for-goat sample-goat)))

(defn default-reader
    "A default reader, for when we don't know what's coming in."
    [t v]
    {:tag t :value v})

(defn convert-unknown-edn
    "We don't know what this EDN is, so let's give it to the default reader"
    []
    (edn/read-string {:default default-reader} (alternative-edn-for-goat sample-goat)))

(defn -main
    "Show off the EDN examples"
    [& args]
    (println "Let's convert a map to EDN: " (convert-sample-map-to-edn))
    (println "Now let's covert the map back: " (edn/read-string (convert-sample-map-to-edn)))
    (println "Let's convert a vector to EDN: " (convert-sample-vector-to-edn))
    (println "Now let's covert the vector back: " (edn/read-string (convert-sample-vector-to-edn)))
    (println "Let's convert our defrecord Goat into EDN: " (convert-sample-goat-to-edn))
    (println "Let's try converting EDN back to a Goat, but it will fail: " (fail-converting-edn-to-goat))
    (println "Let's try converting EDN back to a Goat: " (convert-edn-to-goat))
    (println "Lets convert our Goat to our custom EDN format: " (alternative-edn-for-goat sample-goat))
    (println "Lets convert our custom EDN back into a Goat: " (convert-alt-edn-to-goat))
    (println "Let's handle some unknown EDN: " (convert-unknown-edn)))





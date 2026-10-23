(ns eta-mu.receipt-river.api
  "Authoritative programmatic API for Receipt River."
  (:require [eta-mu.receipt-river.domain.event :as event]
            [eta-mu.receipt-river.extern.bus :as bus]
            [eta-mu.receipt-river.extern.crypto :as crypto]
            [eta-mu.receipt-river.law.completion :as completion]
            [eta-mu.receipt-river.law.receipt :as law]
            [eta-mu.receipt-river.shape.edn :as edn]))

(def package-name law/package-name)
(def package-version law/package-version)
(def schema-documents law/schema-documents)
(def schema-registry law/schemas)
(def current-schemas law/current-versions)

(defn build-event [metadata payload]
  (event/build-event metadata payload))

(defn validate-line [line line-number]
  (try
    (let [record (edn/parse-line line)
          errors (law/record-errors record)]
      {:ok (empty? errors)
       :line-number line-number
       :event record
       :source/schema (if (contains? record :event/schema)
                        {:status :declared
                         :id (get-in record [:event/schema :id])
                         :version (get-in record [:event/schema :version])}
                        {:status :unversioned})
       :errors errors
       :line line})
    (catch :default error
      (let [message (ex-message error)]
        (bus/emit-error! :receipt-river/invalid-edn
                         {:line-number line-number
                          :exception/name "Error"
                          :exception/message message})
        {:ok false
         :line-number line-number
         :event nil
         :source/schema {:status :unreadable}
         :errors [(str "invalid EDN: " message)]
         :line line}))))

(defn validate-ledger
  "Validate ordered raw lines, retaining raw errors beside explicit completions.
   SHA256 binds UTF-8 line content excluding its line terminator. No mutation,
   historical validity, producer authentication or deployment approval is implied."
  [lines]
  (completion/summarize
    (mapv (fn [index line]
            (assoc (validate-line line (inc index))
                   :sha256 (crypto/short-sha256 line 64)))
          (range) lines)))

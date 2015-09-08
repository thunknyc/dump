(ns dump.core
  (:require [clojure.pprint :refer [cl-format]]
            [clojure.string :refer [join]]))

(def canonical
  "Dump format to replicate output of `hexdump -C`."
  {:bytes-per-line 16
   :groups-per-line 2
   :offset-width 8})

(def wide
  "Wide format inspired by `hexdump -C`."
  {:bytes-per-line 32
   :groups-per-line 4
   :offset-width 8})

(def ^:dynamic *dump-format* "Current dump format." canonical)

(defn ^:private byte->unsigned
  [x]
  (let [b (if (char? x) (byte x) x)]
    (if (< b 0)
      (+ 0x100 b)
      b)))

(defn ^:private byte->char
  [i]
  (cond (< i 0x20) \.
        (< i 0x7e) (char i)
        :else \.))

(defn ^:private dump-line
  [line offset {:keys [bytes-per-group bytestring-width offset-width]}]
  (let []
    (cl-format nil "~V,'0X  ~V<~{~{~2,'0X ~} ~}~;~>~@[|~{~C~}|~]"
               offset-width offset bytestring-width
               (partition-all bytes-per-group line)
               (seq (map byte->char line)))))

(defn ^:private dump-lines-seq
  [lines offset fmt]
  (lazy-seq
   (if-let [line (first lines)]
     (cons (dump-line line offset fmt)
           (dump-lines-seq (rest lines) (+ offset (count line)) fmt))
     (cons (dump-line nil offset fmt)
           nil))))

(defn ^:private dump-bytes
  [bs]
  (let [{:keys [bytes-per-line groups-per-line] :as fmt} *dump-format*
        xs (map byte->unsigned (seq bs))
        lines (partition-all bytes-per-line xs)]
    (dump-lines-seq
     lines 0
     (assoc fmt
            :bytes-per-group (/ bytes-per-line groups-per-line)
            :bytestring-width (+ (* bytes-per-line 3) groups-per-line)))))

(defn dump-seq
  "Return a lazy sequence of strings (without newlines) representing
  the dump of `thing`, which can be a string, a sequence of bytes, a
  byte array, or anything that `slurp` would otherwise accept. The
  sequence is formatted according to the dump format at the time of
  invocation. (Note that strings are treated as strings, not URIs.)"
  [thing]
  (cond (string? thing)
        (dump-bytes (.getBytes thing))
        (sequential? thing)
        (dump-bytes thing)
        (instance? (Class/forName "[B") thing)
        (dump-bytes thing)
        :else
        (dump-bytes (.getBytes (slurp thing)))))

(defn dump-str
  "Return a string a with a representation of the dump of `thing`."
  [thing]
  (join \newline (dump-seq thing)))

(defn print-dump
  "Print string dump of `thing` to `*out*`. Return `nil`."
  [thing]
  (doseq [line (dump-seq thing)]
    (println line)))

(defmacro with-dump-format
  "Alter the current dump format for any dump sequences created in the
  dynamic scope of `body`."
  [opts & body]
  `(binding [*dump-format* (merge *dump-format* ~opts)]
     ~@body))

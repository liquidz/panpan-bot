(ns panpan.util.match)

(defmacro matchfn
  [bind & body]
  `(fn [{[_# ~@bind] :match}] ~@body))

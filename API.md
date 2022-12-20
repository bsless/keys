# bsless.keys 





## `rename`
``` clojure

(rename kmap)
(rename m kmap)
```


Like [[clojure.set/rename-keys]] but faster.
  If called without a map, returns a function that will rename keys according to kmap.
<br><sub>[source](null/blob/null/src/bsless/keys.clj#L197-L210)</sub>
## `rename*`
``` clojure

(rename* m)
(rename* m k0 k0')
(rename* m k0 k0' k1 k1')
(rename* m k0 k0' k1 k1' k2 k2')
(rename* m k0 k0' k1 k1' k2 k2' k3 k3')
```

<sub>[source](null/blob/null/src/bsless/keys.clj#L215-L220)</sub>
## `rename-key!`
``` clojure

(rename-key! m from to)
```

<sub>[source](null/blob/null/src/bsless/keys.clj#L182-L195)</sub>
## `select`
``` clojure

(select ks)
(select m ks)
```


Like [[clojure.core/select-keys]] but faster.
  If called without a map, returns a function with two arities which
  will select the provided keys.
  The returned function is operationally equivalent to:
  ((select ks) m) == (select-keys m ks)
  ((select ks) init m) == (merge init (select-keys m ks))
<br><sub>[source](null/blob/null/src/bsless/keys.clj#L83-L93)</sub>
## `select*`
``` clojure

(select* _)
(select* m k0)
(select* m k0 k1)
(select* m k0 k1 k2)
(select* m k0 k1 k2 k3)
(select* m k0 k1 k2 k3 k4)
(select* m k0 k1 k2 k3 k4 k5)
(select* m k0 k1 k2 k3 k4 k5 k6)
(select* m k0 k1 k2 k3 k4 k5 k6 k7)
```


Like [[select-keys]] but the provided keys don't have to be provided as a vector.
  Aggressively in-lines iteration.
<br><sub>[source](null/blob/null/src/bsless/keys.clj#L59-L71)</sub>
## `select-as`
``` clojure

(select-as kmap)
(select-as m kmap)
```


Like [[clojure.core/select-as-keys]] but faster and renames keys according to kmap.
  If called without a map, returns a function with two arities which
  will select-as the provided keys.
  The returned function is operationally equivalent to:
  ((select-as kmap) m) == (select-as m kmap)
  ((select-as kmap) init m) == (merge init (select-as m kmap))
<br><sub>[source](null/blob/null/src/bsless/keys.clj#L163-L173)</sub>
## `select-as*`
``` clojure

(select-as* _)
(select-as* m k0 k0')
(select-as* m k0 k0' k1 k1')
(select-as* m k0 k0' k1 k1' k2 k2')
(select-as* m k0 k0' k1 k1' k2 k2' k3 k3')
(select-as* m k0 k0' k1 k1' k2 k2' k3 k3' k4 k4')
(select-as* m k0 k0' k1 k1' k2 k2' k3 k3' k4 k4' k5 k5')
(select-as* m k0 k0' k1 k1' k2 k2' k3 k3' k4 k4' k5 k5' k6 k6')
(select-as* m k0 k0' k1 k1' k2 k2' k3 k3' k4 k4' k5 k5' k6 k6' k7 k7')
```


Like [[select-as-keys]] but the keys are provided as rest arguments.
  Aggressively in-lines iteration.
<br><sub>[source](null/blob/null/src/bsless/keys.clj#L145-L157)</sub>
## `select-into*`
``` clojure

(select-into* to _)
(select-into* to m k0)
(select-into* to m k0 k1)
(select-into* to m k0 k1 k2)
(select-into* to m k0 k1 k2 k3)
(select-into* to m k0 k1 k2 k3 k4)
(select-into* to m k0 k1 k2 k3 k4 k5)
(select-into* to m k0 k1 k2 k3 k4 k5 k6)
(select-into* to m k0 k1 k2 k3 k4 k5 k6 k7)
```


Like [[select-keys]] but the provided keys don't have to be provided as a vector.
  Aggressively in-lines iteration.
<br><sub>[source](null/blob/null/src/bsless/keys.clj#L32-L57)</sub>
## `select-into-as*`
``` clojure

(select-into-as* to)
(select-into-as* to m k0 k0')
(select-into-as* to m k0 k0' k1 k1')
(select-into-as* to m k0 k0' k1 k1' k2 k2')
(select-into-as* to m k0 k0' k1 k1' k2 k2' k3 k3')
(select-into-as* to m k0 k0' k1 k1' k2 k2' k3 k3' k4 k4')
(select-into-as* to m k0 k0' k1 k1' k2 k2' k3 k3' k4 k4' k5 k5')
(select-into-as* to m k0 k0' k1 k1' k2 k2' k3 k3' k4 k4' k5 k5' k6 k6')
(select-into-as* to m k0 k0' k1 k1' k2 k2' k3 k3' k4 k4' k5 k5' k6 k6' k7 k7')
```


Like [[select-as-keys]] but the keys are provided as rest arguments.
  Aggressively in-lines iteration.
<br><sub>[source](null/blob/null/src/bsless/keys.clj#L117-L143)</sub>
## `select-key`
``` clojure

(select-key acc m k)
```

<sub>[source](null/blob/null/src/bsless/keys.clj#L4-L9)</sub>
## `select-key!`
``` clojure

(select-key! m)
(select-key! acc k)
(select-key! acc m k)
```

<sub>[source](null/blob/null/src/bsless/keys.clj#L11-L30)</sub>
## `select-key-as!`
``` clojure

(select-key-as! acc m k k')
```

<sub>[source](null/blob/null/src/bsless/keys.clj#L102-L115)</sub>

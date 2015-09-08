# dump

A Clojure `hexdump -C` facility.

## Usage

This library is available on
[Clojars](https://clojars.org/thunknyc/dump); here is the Leiningen
dependency for the most recently released version:

![Clojars Project](http://clojars.org/thunknyc/dump/latest-version.svg)

The most basic example:

```
user> (require '[dump.core :refer [print-dump]])
;; => nil
user> (print-dump (take 250 (slurp "http://example.com/")))
00000000  3c 21 64 6f 63 74 79 70  65 20 68 74 6d 6c 3e 0a  |<!doctype html>.|
00000010  3c 68 74 6d 6c 3e 0a 3c  68 65 61 64 3e 0a 20 20  |<html>.<head>.  |
00000020  20 20 3c 74 69 74 6c 65  3e 45 78 61 6d 70 6c 65  |  <title>Example|
00000030  20 44 6f 6d 61 69 6e 3c  2f 74 69 74 6c 65 3e 0a  | Domain</title>.|
00000040  0a 20 20 20 20 3c 6d 65  74 61 20 63 68 61 72 73  |.    <meta chars|
00000050  65 74 3d 22 75 74 66 2d  38 22 20 2f 3e 0a 20 20  |et="utf-8" />.  |
00000060  20 20 3c 6d 65 74 61 20  68 74 74 70 2d 65 71 75  |  <meta http-equ|
00000070  69 76 3d 22 43 6f 6e 74  65 6e 74 2d 74 79 70 65  |iv="Content-type|
00000080  22 20 63 6f 6e 74 65 6e  74 3d 22 74 65 78 74 2f  |" content="text/|
00000090  68 74 6d 6c 3b 20 63 68  61 72 73 65 74 3d 75 74  |html; charset=ut|
000000a0  66 2d 38 22 20 2f 3e 0a  20 20 20 20 3c 6d 65 74  |f-8" />.    <met|
000000b0  61 20 6e 61 6d 65 3d 22  76 69 65 77 70 6f 72 74  |a name="viewport|
000000c0  22 20 63 6f 6e 74 65 6e  74 3d 22 77 69 64 74 68  |" content="width|
000000d0  3d 64 65 76 69 63 65 2d  77 69 64 74 68 2c 20 69  |=device-width, i|
000000e0  6e 69 74 69 61 6c 2d 73  63 61 6c 65 3d 31 22 20  |nitial-scale=1" |
000000f0  2f 3e 0a 20 20 20 20 3c  73 74                    |/>.    <st|
000000fa                                                    
;; => nil
user> 
```

With a predefined, non-standard format...

```
user> (require '[dump.core :refer [print-dump with-dump-format] :as dump])
;; => nil
user> (with-dump-format dump/wide
        (print-dump (take 250 (slurp "http://example.com/"))))
00000000  3c 21 64 6f 63 74 79 70  65 20 68 74 6d 6c 3e 0a  3c 68 74 6d 6c 3e 0a 3c  68 65 61 64 3e 0a 20 20  |<!doctype html>.<html>.<head>.  |
00000020  20 20 3c 74 69 74 6c 65  3e 45 78 61 6d 70 6c 65  20 44 6f 6d 61 69 6e 3c  2f 74 69 74 6c 65 3e 0a  |  <title>Example Domain</title>.|
00000040  0a 20 20 20 20 3c 6d 65  74 61 20 63 68 61 72 73  65 74 3d 22 75 74 66 2d  38 22 20 2f 3e 0a 20 20  |.    <meta charset="utf-8" />.  |
00000060  20 20 3c 6d 65 74 61 20  68 74 74 70 2d 65 71 75  69 76 3d 22 43 6f 6e 74  65 6e 74 2d 74 79 70 65  |  <meta http-equiv="Content-type|
00000080  22 20 63 6f 6e 74 65 6e  74 3d 22 74 65 78 74 2f  68 74 6d 6c 3b 20 63 68  61 72 73 65 74 3d 75 74  |" content="text/html; charset=ut|
000000a0  66 2d 38 22 20 2f 3e 0a  20 20 20 20 3c 6d 65 74  61 20 6e 61 6d 65 3d 22  76 69 65 77 70 6f 72 74  |f-8" />.    <meta name="viewport|
000000c0  22 20 63 6f 6e 74 65 6e  74 3d 22 77 69 64 74 68  3d 64 65 76 69 63 65 2d  77 69 64 74 68 2c 20 69  |" content="width=device-width, i|
000000e0  6e 69 74 69 61 6c 2d 73  63 61 6c 65 3d 31 22 20  2f 3e 0a 20 20 20 20 3c  73 74                    |nitial-scale=1" />.    <st|
000000fa                                                                                                      
;; => nil
user>
```

Formats look like this:

```
user> (clojure.pprint/pprint dump/*dump-format*)
{:bytes-per-line 16, :groups-per-line 2, :offset-width 8}
;; => nil
user> 
```

Have fun!

## License

Copyright © 2015 Edwin Watkeys.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

# Maverick

A chess board which allows variant rules of chess. 

## Status

Very early days...

At the moment, only displays a basic board which enforces classical chess rules where both sides must
play on same browser.

### TODO

Visualization:
* Board a-h/1-8 labeling.
* Previous move arrow graphics.
* Move history in PGN.
* Time navigation VCR buttons.
* Per-square threat count indicators.

Chess Rules: 
* King basic movement and captures.
* Castling.
* Enforce pinning pieces and prevent moving into check.
* Checkmate.
* Stalemate. 
* Threefold repitition. 
* Fifty move rule.
* Draw rules: 
  * King vs. King
  * King and Bishop vs. King
  * King and Knight vs. King
  * King and Bishop vs. King and Bishop (of same color).
  * Mutual agreement.

* Alternative rules:
  * [960](https://en.wikipedia.org/wiki/Chess960).
  * [Seiriwan](https://en.wikipedia.org/wiki/Seirawan_chess).
  * [Bughouse](https://en.wikipedia.org/wiki/Bughouse_chess).

Architecture: 
* Multiplayer with matchmaking.
* Permanently saved games.
* Elo ratings.
* Running on AWS: K8s?
* Distributed backend: Kafka?
* OAuth: Google, Facebook, etc.

And lots more probably...


## Development Mode

### Setup Browser 

Enable custom formatters for CLJS DevTools:

https://github.com/binaryage/cljs-devtools/blob/master/docs/installation.md

Install React DevTools plugin:

https://chrome.google.com/webstore/detail/react-developer-tools/fmkadmapgofadopljbjfkapdkoienihi


### Start Cider from Emacs

Put this in your Emacs config file:

```
(setq cider-cljs-lein-repl "(do (use 'figwheel-sidecar.repl-api) (start-figwheel!) (cljs-repl))")
```

Navigate to a clojurescript file and start a figwheel REPL with `cider-jack-in-clojurescript` or (`C-c M-J`)

### Compile CSS

Compile CSS file once.

```
lein garden once
```

Automatically recompile css file on change.

```
lein garden auto
```

### Run Application

```
lein clean
lein figwheel dev
```

Figwheel will automatically push cljs changes to the browser.

Wait a bit, then browse to [http://localhost:3449](http://localhost:3449).


## Production Build

To compile all Clojurescript to Javascript:

```
lein do clean, cljsbuild once min
```

Build an Uberjar with everything (HTML, CSS, Clojurescript, Clojure):

```
lein do clean, garden once, ring uberjar
```

Run the application from the Uberjar:

```
cd target
java -jar maverick-standalone.jar 
```

Build a Docker image:

```
cd docker
make container
```

Run the Docker image locally: 

```
cd docker
make run 
```

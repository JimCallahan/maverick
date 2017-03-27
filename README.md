# maverick

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
** King vs. King
** King and Bishop vs. King
** King and Knight vs. King
** King and Bishop vs. King and Bishop (of same color).
** Mutual agreement.
* Seriwan variant.

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
lein clean
lein cljsbuild once min
```

Build an Uberjar with everything (HTML, CSS, Clojurescript, Clojure):

```
lein clean
lein garden once
lein ring uberjar
```

Run the application from the Uberjar:

```
cd target
java -jar maverick-0.1.0-SNAPSHOT-standalone.jar 
```

# Scala.js Showcase

## Development

- Launch `sbt`.

- For continuous compilation of your changes type `~fastOptJS`.

- Then open the following URL within your favorite browser: [http://localhost:12345/target/scala-2.11/classes/index-dev.html](http://localhost:12345/target/scala-2.11/classes/index-dev.html)

## Deployment

- Launch `sbt` and type `fullOptJS`.

- Then extract the following files:
```
target/scala-2.11/
  +- scalajs-showcase-opt.js
  +- scalajs-showcase-jsdeps.min.js
  +- classes/
          +- index.html
          +- style.css
```

- Finally open the `index.html` in the browser of your choice.
# AsciiMAth to Katex

These files were used to render ascii math in a latex like format using katex.

index.js was modified from [tylerlong's GitHub](https://github.com/tylerlong/asciimath-to-latex).

[Working Example](https://github.com/pianomanfrazier/asciimath-to-katex)

## Dependencies

- [Browserify](https://browserify.org)
- [asciimath-to-latex](https://github.com/tylerlong/asciimath-to-latex)
- [Katex](https://katex.org/) of course ;)

## Build

```bash
npm install
npm run build # outputs the bundle.js
```

## Run It Locally

```bash
npm install
npm run build
npx browser-sync --watch
```
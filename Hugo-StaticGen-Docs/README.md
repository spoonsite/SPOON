# Open Storefront Documentation

This documentation is built with [Hugo](https://gohugo.io), a static website generator.

To install Hugo follow the directions [here](https://gohugo.io/getting-started/installing/) or download the latest release [here](https://github.com/gohugoio/hugo/releases).

The theme used for this documentation is [DocDock](http://docdock.netlify.com). Refer to the DocDock documentation for information on the theme such as shortcodes available, configuration, and site organization.

## How to Write Documentation

The files to edit are found in `openstorefront/Hugo-StaticGen-Docs/content`. Hugo uses this content to generate the website.

You have two flavors of markdown available provided by Hugo: standard github style markdown and [mmark](https://github.com/miekg/mmark/wiki/Syntax). To use mmark create a file with the `.mmark` extension or specify `markup = "mmark"` in the toml front matter of the markdown file. Mmark adds several features such as figures, citations, and different ordered list styles (i.e. 1,2,3 or a,b,c or A,B,C).

## Development Environment

Hugo provides a live development server for viewing changes as you are editing the files. Go to `openstorefront/Hugo-StaticGen-Docs/` and run `hugo server -w`. This will run a local dev server at `localhost:1313`, any changes made to the files will trigger a rebuild and refresh your browser.

## How to build the Documentation

This should only be done once per release.  For Development use `hugo server -w` which will build to the build folder which is ignored.

1. Delete docs folder

2. In the terminal go to `openstorefront/Hugo-StaticGen-Docs/` and run `hugo -d ../docs`. This will build the website in `openstorefront/docs`.
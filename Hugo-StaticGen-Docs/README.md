# SPOON Documentation

This documentation is built with [Hugo](https://gohugo.io), a static website generator.

To install Hugo follow the directions [here](https://gohugo.io/getting-started/installing/) or download the latest release and put it in your path [here](https://github.com/gohugoio/hugo/releases).

The theme used for this documentation is [DocDock](http://docdock.netlify.com). Refer to the DocDock documentation for information on the theme such as shortcodes available, configuration, and site organization.

## How to Write Documentation

The files to edit are found in `openstorefront/Hugo-StaticGen-Docs/content`. Hugo uses this content to generate the website.

You have two flavors of markdown available provided by Hugo: standard github style markdown and [mmark](https://mmark.nl/post/syntax/). To use mmark create a file with the `.mmark` extension or specify `markup = "mmark"` in the toml front matter of the markdown file. Mmark adds several features such as figures, citations, and different ordered list styles (i.e. 1,2,3 or a,b,c or A,B,C).

Images should be placed in `openstorefront/Hugo-StaticGen-Docs/static/images`. When you reference an image in the markdown file use the relative path for example `![My Image](/images/some-image.png)`.

If creating a new page use the Hugo cli with `hugo new path/to/new-doc.md` and Hugo will generate a new file with front matter at the specified path.

## Development Environment

Download the latest release of Hugo at https://github.com/gohugoio/hugo/releases and add the binary to your path.

Hugo provides a live development server for viewing changes as you are editing the files. Go to `openstorefront/Hugo-StaticGen-Docs/` and run `hugo server --baseURL localhost:1313 --watch`. This will run a local dev server at `localhost:1313`, any changes made to the files will trigger a rebuild and refresh your browser.

## How to build the Documentation

This should only be done once per release.  For Development use `hugo server -b localhost:1313 -w` which will run a live reloading development server. To see perform a build run `hugo` from `openstorefront/Hugo-StaticGen-Docs/` which will build to the `./build` by default which is in the gitignore.

The documentation is hosted at https://spoonsite.github.io using github pages. To create a new build clone spoonsite.github.io

1. Clone spoonsite.github.io

2. Build the documentation in the spoonsite repo

3. Remove the contents of spoonsite.github.io and replace it with the new build

4. Commit and push the new build

This process can be automated with the following script:

```bash
#!/bin/bash

# The location of the spoonsite repo
SPOON="/c/dev/spoonsite"

cd $SPOON/Hugo-StaticGen-Docs

# Also assumes spoonsite.github.io has also been cloned
DEPLOY="../../spoonsite.github.io"
DATE=$(date)

echo "building site"
rm -r public/*
hugo

echo "cleaning out old site"
rm -r $DEPLOY/*

echo "copying build to $DEPLOY"
cp -r public/* $DEPLOY

cd $DEPLOY
git add -A
git commit -m "Build on $DATE"
git push
```
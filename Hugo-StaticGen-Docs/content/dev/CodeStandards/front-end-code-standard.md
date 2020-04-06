+++
title = "Front-end Code Standards"
description = ""
weight = 3
+++

These are the code standards for this project.
<!--more-->

## Style

Currently supporting Edge, Chrome, Firefox (latest LTS release)

Safari, Opera, IE 11 or others should work but we are not testing for them

## VueJS

There are two VueJS single page applications being used for SPOON. These can be found in `/client/login` and `/client/desktop`.

The projects were generated with the Vue CLI. See the [Vue CLI documentation](https://cli.vuejs.org/) for more information about the structure of the SPA.

Developers should follow the linting rules defined inside the application. Run `npm run lint` or use your IDE's plugin system to integrate eslint.

## Legacy ExtJS

SPOON is currently in the process of updating it's UI to VueJS. There are still old pages in ExtJS. The following were the code standard for this code.

### Project Specific

1. Use the Ext.js docs.  Look at them first for utility functions as they are normalize for the browser (format, string manipulation, etc.)
2. When making reusable components don't use "ID"s properties. "ID"s are OK on individual pages; must be unique; "ID" properties are Global
3. Match validation from server, to make it easier for the user to get the right values. Provide friendly but direct messages
4. Pack related components (notification panel, notification) into a single script file to reduce loading time
5. Avoid deeply nested component definitions as it is easier to read fewer layers
6. Avoid ES 6 features until browsers' support is consistent (mainly IE 11)
7. A `{` should be on the end of the line

Example:

```js
function demo(){
    return 0;
}
```

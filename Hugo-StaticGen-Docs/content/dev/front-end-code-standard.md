+++
title = "Front-end Code Standards"
description = ""
weight = 3
+++

1. Use the Ext.js docs.  Look at them first for utility functions  (format, string manipulation, etc...).  
2. When making reusable components don't use "ID"s properties. Ids are OK on individual pages; must be unique; "ID" properties are Global.  
3. Match validation from server, where possible, to make it easier for the user to get the right values. Also, provide friendly but, direct messages.
4. Pack related components (notification panel, notification) into a single script file. To reduced loading.
5. Avoid deeply nested components definitions as it easier to read a fewer layers.


## IE 9 - Tips
----

**Window sizes:** need height defined (minHeight, height) percent height will work only with maximizable on.

**Store Dates:** date capture in a store need to be set to type: 'date' with a format of 'c'

**Don't use custom UI buttons**

**Watch CSS**

[33mcommit 6dd00bb24b8cf3f902953e576bcbe8682e5ab914[m[33m ([m[1;36mHEAD -> [m[1;32mSTORE-2883_sub-category-assignment-tool[m[33m, [m[1;31morigin/STORE-2883_sub-category-assignment-tool[m[33m)[m
Author: Michael Hoffmann <mhoffmann@usurf.usu.edu>
Date:   Fri Mar 9 12:02:18 2018 -0700

    I can't figure out the exception, but it's probably got to do with the data model, which we were told not to touch in this branch. Added Save and Cancel buttons that don't do anything yet, and don't match the style of the site b/c IDK how to do that. Made some comments and TODOs, and cleaned up the code a bit.

[33mcommit 5de922c7bab3db97df92b12b8d496c10c97addda[m
Author: Michael Hoffmann <mhoffmann@usurf.usu.edu>
Date:   Fri Mar 9 10:12:07 2018 -0700

    Got the filter combobox working, and added an identical one for the left panel. Made an override for the EntryTypeWindow.show method, so the grids are refreshed when the window is shown. That keeps things up to date if the user closes our popup, changes an entry's active status, and then opens our popup again. Now trying to fix an exception that's thrown whenever a drag-and-drop is completed.

[33mcommit ab22e430f5684e2bdc6b5ea5e934371e3bff6d9f[m
Author: Michael Hoffmann <mhoffmann@usurf.usu.edu>
Date:   Thu Mar 8 18:10:50 2018 -0700

    Trying to get the combobox to update the right panel's grid. It's hard because Ext dissolves and reassigns a lot of references when the Ext.create function is used.

[33mcommit 6506c373df443733d3e2aad50bc3d3f6a7d1d219[m
Author: Michael Hoffmann <mhoffmann@usurf.usu.edu>
Date:   Thu Mar 8 17:27:05 2018 -0700

    Fixed the indentation and moved would-be global variables to the right scope. Now trying to add some text to the left column and get the combobox in right column to filter by status.

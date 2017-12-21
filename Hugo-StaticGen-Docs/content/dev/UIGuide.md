+++
title = "User Interface Guide"
description = ""
weight = 6
+++

## General Items
1. Remember this is a guide there can be variations as appropriate to the feature.  Focus on the usability and having a clear workflow.
2. Dialog bodies should be padded 10px
3. Use toolbars for actions and enable/disable buttons to guide the user to valid actions.
4. Add text to icons on button so it's very clear as to what they are.  (Where possible)
5. Form action should be left to right with the Action on right and the anti-action on the left.  English is left to right.  Action should be common choise and the anti-action the uncommon.
6. Most windows should be resizeable so they should adjust their layout accordingly.   Make sure window adjust to smaller screen sizes.  Use the percentages where appropriate but remember IE 9 can have issues.  Making the window maximizable can resolve the issues.
7. Client should handle data rendering in most cases.
8. CSS should all be kept in the dynamic CSS file for branding.  However, page/feature specific styling can be appropriate especially if it's layout related specific to the page.
9. Use the appropriate font-awesome icons for the action. See other pages.


![ui-guide](/openstorefront/images/dev/ui-guide.png)  

---    

## Buttons and Icons
This section is a guide to the naming convention of buttons, icons used with the buttons, and the colors associated with those icons.  Each button belongs to a category based on action and CSS properties.  Icons used in this application come from Font Awesome 4.7.0.
### Category 1: Warning
**Note:** All icons in this category have the CSS class *icon-button-color-warning*.  

1. **Delete Button**  
    * Icon: trash can (class = "fa fa-trash").  

2. **Close Button**  
    * Icon: times symbol (class = "fa fa-close").

3. **Cancel Button**  
    * Icon: 'X' (class = "fa fa-close").  

4. **Clear or Clear All Button**  
    * Icon: trash can (class = "fa fa-trash").

5. **Uninstall**  
    * Icon: trash can (class = "fa fa-trash").

6. **Flush Cache**  
    * Icon: trash can (class = "fa fa-trash").  

7. **Rollback**  
    * Returns the database to its previous state.
    * Icon: 'X' (class = "fa fa-close").
    
### Category 2: Save  
**Note:** All icons in this category have the CSS class *icon-button-color-save*.  

1. **Add Button**  
    * Icon: plus symbol (class = "fa fa-plus")   

2. **Create Button**
    * Icon: plus symbol (class = "fa fa-plus")  

3. **Save Button**  
    * Icon: floppy disk (class = "fa fa-save")   

4. **Apply Button**  
    * Icon: check mark (class = "fa fa-check")  

5. **Approve Button**  
    * Icon: box with check mark (class = "fa fa-check-square-o")  

6. **Comment/Post Button**  
    * Icon: speech bubble (class = "fa fa-comment")

7. **Publish**  
    * Icon: book (class = "fa fa-book")  

8. **Mark Complete**  
    * Icon: box with check mark (class = "fa fa-check-square-o")  


### Category 3: Refresh  
**Note:** All icons in this category have the CSS class *icon-button-color-refresh*.  

1. **Refresh Button**  
    * Icon: cycle symbol (class = "fa fa-refresh")   

2. **Restart Button**  
    * Icon: redo arrow (class = "fa fa-repeat")   

3. **Reprocess Button**  
    * Icon: undo arrow (class = "fa fa-undo")  

### Category 4: View  
**Note:** All icons in this category have the CSS class *icon-button-color-view*.  

1. **View/Preview Button**  
    
    * Icon: eye (class = "fa fa-eye")  

### Category 5: Edit  
**Note:** All icons in this category have the CSS class *icon-button-color-edit*.  

1. **Edit Button**  

    * Icon: pencil in square (class = "fa fa-edit")

### Category 6: Run  
**Note:** All icons in this category have the CSS class *icon-button-color-run*.  

1. **Run Button**  
    * Icon: lightning bolt (class = "fa fa-bolt")   

2. **Start Button**  
    * Icon: play (class = "fa fa-play-circle") 

3. **Process Messages**  
    * Icon: lightning bolt (class = "fa fa-bolt")   

### Category 7: Default  
**Note:** All icons in this category have the CSS class *icon-button-color-default*.  

1. **Toggle Status Button**  
    * Icon: power off/on (class = "fa fa-power-off") 

2. **Download Button**  
    * Icon: download/import (class = "fa fa-download")   

3. **Upload Button**  
    * Icon: upload/export (class = "fa fa-upload")  

4. **Export Button**  
    * Icon: download/import (class = "fa fa-download")

5. **Import Button**  
    * Icon: upload/export (class = "fa fa-upload")  

6. **Stop Button**  
    * Icon: square in circle (class = "fa fa-stop-circle")  

7. **Previous/Next Buttons**  
    * Icon: left/right arrows (class = "fa fa-arrow-right", class = "fa fa-arrow-left") 

8. **Home Button**  
    * Icon: home (class = "fa fa-home")  

9. **Cleanup**  
    * Icon: eraser (class = "fa fa-eraser")  

10. **Copy**  
    * Icon: two copies (class = "fa fa-clone")  

11. **Merge**  
    * Icon: two arrows merging (class = "fa fa-compress")  

12. **References**  
    * Icon: chain links (class = "fa fa-link")

### Icon Sizes  
* For large buttons, icons are given Font Awesome size of&nbsp; *fa-2x*.  
* For all other buttons, icons are given Font Awesome size of&nbsp; *fa-lg*.  

### Available Icon Alignments  
  
**Available alignment corrections for Font Awesome icons of size *fa-2x*:**

1. *icon-vertical-correction*  
    * Shifts icon up 5 pixels.

2. *icon-vertical-correction-send*  
    * Shifts icon left 10 pixels and up 3 pixels.

3. *icon-vertical-correction-view*  
    * Shifts icon left 5 pixels and up 2 pixels.

4. *icon-vertical-correction-edit*  
    * Shifts icon left 4 pixels and up 1 pixel.

5. *icon-horizontal-correction*  
    * Shifts icon left 3 pixels.

6. *icon-vertical-correction-search-tools*  
    * Shifts icon left 4 pixels and up 6 pixels.

**Available alignment corrections for Font Awesome icons of size *fa-lg*:**  

1. *icon-small-vertical-correction*  
    * Shifts icon down 5 pixels.

2. *icon-small-vertical-correction-media-table*  
    * Shifts icon down 2 pixels.

3. *icon-small-vertical-correction-book*  
    * Shifts icon down 3 pixels.

4. *icon-small-horizontal-correction-left*  
    * Shifts icon left 8 pixels.

5. *icon-small-horizontal-correction-right*  
    * Shifts icon right 5 pixels.

6. *icon-vertical-correction-eraser*  
    * Shifts icon left 5 pixels and up 2 pixels.

**Available alignment corrections for text/icon spacing for windows:**  

1. *shift-window-text-right*  

    * Shifts text to the right 8 pixels.  

  




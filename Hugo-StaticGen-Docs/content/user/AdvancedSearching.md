+++
title = "Advanced Searching"
description = ""
weight = 9999
+++

A guide to advanced searching.
<!--more-->

## Searching

Application Search provides a way to find entry lists and articles
matching search criteria. Several types of searches are supported. The
types of searches that can be performed are:

1. Searching across multiple fields (entry name and description, tags, attribute types, codes, and descriptions)
1. Wildcard searching (\* and ?)
1. Exact phrase searching
1. Case insensitivity
1. Filtered Searches
1. Tags
1. Comparing
1. Customizing the display

**Note:** The type ahead on the search field only looks at the title to see if it contains the text.  
The user can then select the suggestion by clicking on it or continue with a full text search.

### Search Field Differentiation or Specificity

To search on different fields using a keyword search, you will need to change your search options. These
options allow you to filter your search by only matching on certain fields. This is done on the bar just
left of the search bar, labeled search options.

### Fuzzy Matching and Wildcards

Fuzzy matching is defined as the ability to perform inexact matches on
terms in the search index. For example, this could be used to find all
variations of prefixed or suffixed words and multiple spellings of a
word.

Fuzzy matching is done with the * and the ? characters acting as
wildcards. It is also known as wildcard searching. The * character
represents a string of letters, whereas the ? acts as a placeholder for
only a single character. The process works as shown in the queries
below:

Query: `offi*` Matches office, officer, official, and so on

Query: `off*r` Matches offer, officer, officiator, and so on

Query: `off?r` Matches offer, but not officer

### Phrase Searches and Exact Searches

Exact searches must be enclosed by double quotes.

Search example: `"create a common framework"`
Search example: `"Air Force"` or `"Company Name Inc."`

Wildcards are only meant to work on individual search terms, not on
phrase searches/exact searches, as demonstrated by this example:

Works: `eng?neering` _(wildcard properly used)_
Does not work: `"eng?neering b?ckground"` _(quotes are treated as a literal string not a wildcard)_

In the second example, the search would only return results that had the
? character exactly in their respective places instead of
accepting any character in those locations as the user intended.

### Case Insensitivity

Searching is case **insensitive** and allows mixing of case. In the
examples below, the exact same results would be returned regardless of
the capitalization.

Search examples:
`create a common framework`, `Create a Common Framework`, `cReatE A CoMmoN FraMewoRk`

### Filtered Searches

From the search results screen the left-most panel is the Filters panel.  
Here, results can be narrowed by filtering on various aspects such as:

* **Name:**  The name of the entry
* **Tag:**  Type the name of a tag or select from the drop-down.  Tags can be combined and added to the filter or removed.
* **Topic:**  DI2E Component, Article, etc.
* **User Rating:**  This will show all entries with the average star rating selected (or better, i.e. or a **higher** average star rating)
* **Vitals:**  This gives a fine-tuned and very detailed approach to filtering by many different vitals such as DI2E Intent, State, and Alignment, for example.

### Using Tags

Users can "Tag" listings so that they can be used to later find and refine search results. User-created tags are visible to all users. However, only the
owner of the tag or an admin user may remove a tag. Tags are used in the search field shown at the top of the screen and on the home page.

**How to create a tag:**

1. From the search results screen, click on the entry title.  The details of the entry will be displayed.
1. Click on the "Tag" icon to open the tag panel.
1. Enter a new tag in the text field OR choose an existing tag by clicking on the drop down arrow.  Click "Add" to apply the tag to the entry.

**How to remove a tag you created:**

1. From the search results screen, click on the entry title.  The details of the entry will be displayed.
1. Click on the "Tag" icon to open the tag panel. Tags will be displayed below the "Add Tag" text field.
1. Locate the tag and click on the drop down arrow and select "Delete".

### Comparing

The comparison feature allows for side-by-side viewing of multiple
listings. For example, there may be several entries that provide the
same kind of features (e.g. Cesium and Google maps). You can use the
comparison feature to view their details together to find the entry
better suited your project's needs.

**How to compare entries:**

1. From the Search Results screen check the "Add to Compare" box on 2 or more entries.
1. Click on the **Compare** button at the top of the Search Results window. A side-by-side comparison will appear in a new window.
1. If more than two entries were selected, you can select which entries to compare by using the drop-down list at the top to select the various entries.
1. To remove all selected entries,  click on the drop-down arrow next to the **Compare** button and select "Clear All Selected Entries".

### Customizing the Search Display

On the search results page, filtering can be done by Name, Tag, Topic, User Rating, or Vitals.

The search results display can also be customized by clicking on the gear icon
and down arrow at the top-right of the search results.  Check or uncheck the
items to be displayed or hidden such as:

* Organization
* Badges
* Description
* Last Update
* Vitals
* Tags
* Average User Rating
* Approved Date
* Index Relevance

To reset all filters, click on the "Reset Filters" button at the bottom of the
Filters column.

Finally, the columns can be collapsed or expanded by clicking on the left and right
arrows in the column headers by Filters, Search Results, and Details.

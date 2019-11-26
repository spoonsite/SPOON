+++
title = "Features and Summary of SPOON"
description = ""
weight = 99999
+++

This is a list of all the feature on SPOON, down to minute detail.

## Landing

### Searching

* Typing into the global search text box, will perform an indexed search with any selected entry type (drop-down to the left) as additional filters.  
* Clicking the Search button with no text entered searches everything (a.k.a "global search").
* Entry types displayed are both Active and Inactive and will display nested sub-categories in a tree structure.  
* Only selected entry types will be used in the search.  Selecting parent doesn't search children unless selected

### Search Tools

* Tags will bring up the tag cloud with only tags from  ,  , and   entries. Active Approved Unrestricted (Based User Restricted) The more tags the Bigger the text and the less tag the smaller (down to a minimal).  The point of tag cloud is to provide a visual of the types and relative amounts of information in the database.
* Organization should show only organizations that have Entries.  Allows for easy searching for entries for an organization.
* Relationships tool - allow for quick UI to the relationship page.  The user should pick a starting point and then the application takes them to the relationship without prompting.
* Advanced Search - allow the user to build their own search criteria and save searches.  This is expected for power-users that have very specific searching needs.  This may need a separate test section.

### Quick Launch

* Admin defined links to pages into the application.  Clicking should take the user to the specified page.

### Highlights

* Displays Active Highlights  (will be able to read more with full HTML display)
* Displays 3-5 most recently approved items  (view should take user to search results with the view open
* Highlights are HTML and can have Media and Display Saved Searches (global ones).

### Topics

* Lists top-level (root) entry types ... clicking them takes the user to the search results page with entries for the topic (and all of its children).

## Notifications

System generate or admin generated messages. Primarily intended is to notify the user that some background process (IE. reports) have completed.

* Notification should be monitoring events on all pages
* The application will pop-up a message when it receives the message.  (this is either done by pulling or via websocket which depend on admin setting and the server configuration)
* Message are only persisted for X days (admin configured; see system configuration)
* Message can be marked read (only for that user).  Global message create a single message that the user can flag individually.
* Message can be deleted (only for that user).  Global message create a single message that the user can delete individually.
* Click the check box to mark as read

## Menu

* What the user can access is based on their roles . Regular users will only see the buttons that lead to features they have permission for. Upon signing into SPOON, users are directed first to pages that have been configured as their homepage, for example, the internal review user will see the Workplan Progress page upon signing in.

## Search Results

Displays the results of a search. Searching can be performed from a variety of  areas ut this page is the primary UI for the results.


* Relevance is default.  Relevance only applies to index searches advance by their nature all results are relevant.  
* Search Results View can be customized as to what it displays in the results (it only persists for the current search) 
* Clicking search result will display selected The result should be paged The export should produce a CSV file of the current page of the results (* export I think this should export all pages of the search results ) 
* Only Active/Approved/User accessible (data restrictions) entries will be displayed in search results

### Filtering

* Options depend on the search results and be based on the results.  
* Filtering should be applied across all pages 
* Previous Filtering options should persist regardless of results 
* All filter behave like an "AND" expect for the attributes.  Attributes Type should be "AND" but, the codes (checkboxes) should be "OR"

### Compare

* The user should be able to select multiple (1 or more entries to compare).  
* Click compare will open a window that shows a side-by-side of the first two selection
* User can not compare entries against themselves
* If the user select entries only those selected will by available in their drop-downs 
* On the view, only the sections with information will be displayed 

## Detail View

* SPOON - should be configure to allow Guest access to the view.  It should be read only....meaning it requires a login to post a review /question.
* Top Section is fix (meaning not user definable);  Everything else is user(admin) definable...meaning the layout and the content. (see Admin Entry Templates)
* Clicking the Entry Type will perform a search on the entry type 
* Clicking organization will show all the entries that belong to that organization 
* Full path to the entry type should be shown  Fruit → apple 
* Badges (icons) which come for attribute that have badges are displayed under the organization (there can be multiple badges)
* All user can submit correction through the feedback window (See Feedback window) 
* User can request ownership for entries they don't own.  The application should present a form where the user can add their justification.  (See Feedback window) 
* If the user is the owner of the entry then only menu item will  "submit correction" and click it will take the user to the submission page after a prompt. 
* User that have watches; changes since they last viewed should be highlight at a block level (ie. contacts not the specific contact).  After, the user view the entry than the highlight will go away.

### Tags:

* User can add tags (based on permission) 
* Everyone can click tag and view related entries  
* Tag owner can edit and delete their tags 
* Can not add duplicate tags For new tag; the application will prompt for suggest tags (if there are suggestion based on the entry type)

### Watches: 

* Users can add/remove entry from their watch list. 
*  Entries being watch will highlight sections that change (on the detail display)
* Change to entries will be email ONLY if the user flag that they want email in the user tool watch screen. (Separate step)


### Print

* Will bring up a window with a print view of the entry data
* The user can pick the section that will be included in the print
* The print button in the window will bring up the browsers print preview screen allowing the user to customize the print options according to the browser support

### Full Screen

* This should open the details into a new tab fully expanded 
* Note the full screen button is now link to home/landing page 

### Reviews

* All users can view approved reviews (if reviews are part of the entry display) 
* Users can post reviews by filling form that pops up Review may be Moderated (require approval) or auto approved (default behavior) 
* If the Review requires approval then it will display a pending banner for the owner of the review only (otherwise it will not show)
* Owners of the review can edit and remove their reviews
* Admins with the correct permissions can also "delete" (in-activate) reviews
* All Reviews should be summarized for the entry (at the top section of the review display) 

### Question / Answers

* All users can view approved questions (if questions are part of the entry display)
* Users can post question by filling form that pops up
* Users can post answers to any questions by filling form that pops up
* Owners of the question and answers can edit and remove their questions or answers
* Admins with the correct permissions can also "delete" (in-activate) reviews
* Question and answers may be Moderated (require approval) or auto approved (default behavior)
* If the Question or  Answers require approval then it will display a pending banner for the owner of the question/answer only (otherwise it will not show)

## Dashboard

Every Landing Area has a link to a dashboard (It the same dashboard; there is only one dashboard).

* The dashboard is user definable (pick widget, order them and change the setting like color and title)
* The dashboard configuration auto saves on changes 
* The purpose is to allow "mashups" of information on one screen and quick access to common functionality. 
* The Widgets are predefined (however some like the search can configured to show differently. Every widget has its own permission.  So user only can access the widgets they have permission for.  (Note: widget may access APIs that require additional permission) 
* There is no limit to how many widget can be added.  Most widget widget can be added only once expect for "Saved Searches"

Widgets:

**Entry Stats** -  read only of entry statistics. \
**System Stats** - read only of system information \
**Notifications** - shows Notification event and allow for reading, deleting of notifications \
**Outstanding Feedback** - Allow for viewing feedback and marking them complete \
**Pending Approval Request** - Show all pending entries and pending change request.  The User should be able to view the both types of request and approve (if they have permission to approve)  IMPORTANT \ **Questions** - shows the User questions for all entries and allow view responses to those question \
**Recent User Data** - shows questions, reviews, tags, contacts added/updated in the past 30 days (ready-only) \
**Reports** -  Show the users report history and allow for download \
**Saved Search** - show the search results the user has saved from advance searches.  The user need to select a search to use.  There can be multiples of this widget. \
**Submission Status**  - show the User Submissions and there status \
**User Stats** -  Show admin stats on user \
**Watches** - Show all of the users watches \

## **Admin Tools**

## Data Management

### Attributes

Manage all Attributes available in the application. (User-defined)
* 
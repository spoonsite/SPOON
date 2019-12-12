+++
title = "Features and Summary of SPOON"
description = ""
weight = 99999
+++

This is a list of all the features on SPOON.

## Landing

Features that are on the Landing page.

### Searching

* Typing into the global search text box, will perform an indexed search with any selected search options (drop-down to the left)
* Clicking the Search button with no text entered searches everything (a.k.a "global search")

### Browse By Category

* Clicking the title of each card will do a search on any entry in that category and any children categories
* Clicking a child category in one of the cards will do a search on any entry in just that category

### Highlights

* Displays Active Highlights  (will be able to read more with full HTML display)
* Highlights are HTML and can have Media

---

## Notifications

Notifications are system generated or admin generated messages. They are primarily intended is to notify the user that some background process (IE. reports) have completed.

* Notification should be monitoring events on all pages
* The application will pop-up a message when it receives the message (Usually in the bottom right-hand corner of the screen)
* Message are only persisted for X days (admin configured; see system configuration)
* Message can be marked read (only for that user). Global message create a single message that the user can flag individually
* Message can be deleted (only for that user). Global message create a single message that the user can delete individually
* Click the check box to mark as read

---

## Menu

* What the user can access is based on their roles . Regular users will only see the buttons that lead to features they have permission for. Upon signing into SPOON, users are directed first to pages that have been configured as their homepage, for example, the internal review user will see the Workplan Progress page upon signing in

---

## Search Results

* Results are sorted according to user preference. Relevance (a measure of how relevant the entry is to the search query) is the default setting. These can be set in the search options tab (the cog on the left)
* Results can be shown in ascending or descending order, this can be changed in the search options tab
* Users can customize what fields are shown for each search result in the search options tab
* Only Active/Approved/User accessible (data restrictions) entries will be displayed in search results

### Filtering

* Users can filter the search results (see the filter tab, the funnel, on the left) based on:
  * Entry Type (Category)
  * Organization
  * Tags
  * Attributes
* When a user is filtering a search, pills will appear below the search bar to indicate how the search is being filtered

### Compare

* The user should be able to select multiple (2 or more entries to compare)
* Click compare will open a window that shows a side-by-side of the selected entries

---

## Detail View

* This page shows all of the information about an entry
* Users can:
  * Watch the part for changes
  * Print the page
  * Contact the vendor
  * Submit corrections
  * Request ownership of the entry
  * Add tags
  * Add reviews
  * Ask questions
  * Answer questions
* You must be logged in to see this page

### &nbsp;Tags

* Clicking on tags will send them to the search page, while doing a search on that tag
* Users can add tags (based on permission)
* Tag owner can edit and delete their tags
* Can not add duplicate tags

### Watches

* Users can add/remove entry from their watch list
* Users will be alerted when watched entries are updated
* Change to entries will be email ONLY if the user flag that they want email in the user tool watch screen. (Separate step)

### Print

* Will bring up a window with a print view of the entry data
* The user can pick the section that will be included in the print
* The print button in the window will bring up the browsers print preview screen allowing the user to customize the print options according to the browser support

### Reviews

* All users can view approved reviews (if reviews are part of the entry display)
* Users can post reviews by filling form that pops up Review may be Moderated (require approval) or auto approved (default behavior)
* If the Review requires approval then it will display a pending banner for the owner of the review only (otherwise it will not show)
* Owners of the review can edit and remove their reviews
* Admins with the correct permissions can also "delete" (in-activate) reviews
* All Reviews should be summarized for the entry (at the top section of the review display)

### Question / Answers

* All users can view approved questions (if questions are part of the entry display)
* Users can post question by filling form that pops up
* Users can post answers to any questions by filling form that pops up
* Owners of the question and answers can edit and remove their questions or answers
* Admins with the correct permissions can also "delete" (in-activate) reviews
* Question and answers may be Moderated (require approval) or auto approved (default behavior)
* If the Question or  Answers require approval then it will display a pending banner for the owner of the question/answer only (otherwise it will not show)

---

## Dashboard

Every Landing Area has a link to a dashboard (It the same dashboard; there is only one dashboard).

* The dashboard is user definable (select widgets, order them, and change the setting like color and title)
* The dashboard configuration auto saves on changes
* The purpose is to allow "mashups" of information on one screen and quick access to common functionality
* The Widgets are predefined (however some like the search can configured to show differently. Every widget has its own permission. So user only can access the widgets they have permission for (Note: widget may access APIs that require additional permission)
* There is no limit to how many widget can be added

Widgets:

* **Entry Stats** -  read only of entry statistics. \
* **System Stats** - read only of system information \
* **Notifications** - shows Notification event and allow for reading, deleting of notifications \
* **Outstanding Feedback** - Allow for viewing feedback and marking them complete \
* **Pending Approval Request** - Show all pending entries and pending change request. The User should be able to view the both types of request and approve (if they have permission to approve)
* **Questions** - shows the User questions for all entries and allow view responses to those question \
* **Recent User Data** - shows questions, reviews, tags, contacts added/updated in the past 30 days (ready-only) \
* **Reports** -  Show the users report history and allow for download \
* **Saved Search** - show the search results the user has saved from advance searches. The user need to select a search to use. There can be multiples of this widget. \
* **Submission Status**  - show the User Submissions and there status \
* **User Stats** -  Show admin stats on user \
* **Watches** - Show all of the users watches \

---

## User tools

### Submissions

This is for users to submit entries. Entries are then reviewed and accepted.

### Tools

See [Reviews](#reviews) and [Questions and answers](#question-answers)

### Change Password

This is for users to update their password.

---

## Admin tools

See [Application Admin](/applicationadmin)

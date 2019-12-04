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

* **Add/Edit Type**  -On Edit the entry type restriction can be set.  (Not associated with entry....don't allow selection associated  (option), associated (required)).
* **Type Support** both Number / Text values  (All values are stored as text but, this affects the UI and the searching for this attribute.  Where it's a number then the codes are all expected to be numbers)
Type Flags

* **Visible** - Show in the search filters. **Not longer used as of
 2.7; It still affects the Search Tools Categories and determines what shows here.** 

* **Important** - Show in the Categories Widget on the Landing page

* **Architecture** - Denotes that this attribute represent an
 architecture which allow it work in the architecture search tool **'Architechture'** is a concept not used on Spoon, however, there are still remnants of references to it on the site until it is completely removed in future versions.

* **Allow Multiple** -Allows an entry to have multiple code of this
 type.

* **Type Applicability** (Optional, Required, Unassociated)

* **Allow User-Create Codes** - When selected; Forms will allow the user
 to enter new values for the attribute type.

* **Import** - this will add or update Attribute Type and their codes
 from one file

* **Export** - export the selected attributes from the table and there
 related codes

* **Tools: Entry Assignment** - Allows the Attribute to be bulk assigned
 to entries or view attribute assignment across entries

* **Bulk: flags** - Allows setting flag on multiple attributes

* **Inactivating Type** - This will CASCADE to all entries that have the
 type and it will inactivate all of the attribute type; Keep in mind
 reactivating will not reactivate entry attributes.

* **Delete Type -** This will CASCADE to all entries that have the type
 and will remove that attribute

* **Special permission** - \*Allow User create attributes Type. This
 allow users to create types on the custom submission form (Attribute
 Table) Codes

* **Toggle Status -** This will CASCADE to all entries that have the
 type and it will inactivate all of the attribute type; Keep in mind
 reactivating will not reactivate entry attributes.

* **Delete -** This will CASCADE to all entries that have the type and
 will remove that attribute

* **Details -** This will show in the Search Tools and Detail View
 \...related items

* **Group Code -** This can be used to group code together (Currently
 not used. but it can be used in the entry template for a custom element)

* **Sort Order -** This allow forcing an sort order from low to high

* **Architecture -** This is special code that allows for hierarchy of
 codes.

* **Badge -** This will display a small badge in search results, entry
 view and printing when entry has this specific value. (Not supported
 on Mobile?)

* **Highlight -** This will colorize the code in the entry view if they
 entry has this value.

* **Attachments -** Allows for a code to have external data associated
 with it. The Attachment can be downloaded for the user when the select
 the attribute in the detail view and related entry shows.

### Contact

Manage all entry contacts in the system

* **Add -** Adds an Unassociated contact

* **Edit -** Edit the contact

* **View References -** Shows all entries that have this contact

* **Toggle Status -** Removes or make Contact available in drop-downs;
 Optionally, inactivates contact on all related entries

* **Merge -** Takes the references from contact A and moves them to
 contact B

* **Delete -** Removes a contact; only if is not associated with any
 entries

### Entries

Manage Entries (this is the admin primary data management tool)

* Add
  * Adds a new entry to the database (General tab is the only tab
 available until save). Tabs/forms available is based on entry types.

* Edit
  * Allow administrator update all the data allowed by the entry type.

  * **Change History** - show a log of all the different change that
 happen on the entry. It can be noisy because different changes occur
 at different type or what appears to be simple change can have a
 ripple effect.

  * **Message Submitter** - Allows emailer the submitter contact; if
 available but otherwise it can be used to email anyone.

  * **Integration** (See integration admin section). - This provides a
 shortcut to that UI.

  * Potential Forms:

      * **Attributes** - Manage optional (one at time attributes) (add, edit,
 delete), add Multiple attributes (with descriptions), **Special
 permission** - \*Allow User create attributes Type. This allow users
 to create types on the this form (Attribute Table)

      * **Relationships** - Add direct relationships

      * **Contacts** - Manage Contacts (pay attention to selecting
 pre-existing)

      * **Resources** - Manage Resource (There is special handling of edit
 local resources) Should only allow link or upload

      * **External Dependencies** - Manage information on external
 dependencies (This has low usage)

      * **Media** - Manage Media (There is special handling of edit local
 resources); Should only allow link or upload; Inline flag - is just
 for warnings. it can be inaccurate depending on what the user does.
 Hide In

      * Carousel affects the view. Icon show the image in the search results.
 External links can\'t be used as icons.

  * **Questions** - Read only; (can approve or inactivate questions)
  * **Reviews** - Read only; (can approve or inactivate questions)
  * **Tags** - Manage all tags on this entry.

  * **Evaluation Scores** - This allow adding and updating \"old\" summary
 scores. If the there is no score then that section shouldn\'t be
 displayed.

  * **Comments** - Internal private comments from an admin (typically)
 but, it can contain comments from the owner. As this form shows all
 entry comments.

* **View**

  * Shows the entry from the users perspective

* **Approve**

  * Approves a pending entry. notify owner as requested

* **Approve - Related**

  * Approves a pending entry. notify owner as requested; It will also show
 related (via relationship) entries that need approval.

* **Change Owner**

  * Updated the owner (allow the entry to be show in the users submission
 for change requests). It should prompt for an optional comment.

* **Change Type**

  * Changes the entry type which affects the field that can be entered. It
 doesn\'t affect existing data or attributes.

* **Change Requests**

  * Change requests allow creating changes to an approved entry and

* **Copy**

  * Creates a copy of the entry with all associated data (except
 evaluations and change requests)

* **Versions**

  * Allows created Full snapshots of the entry only.

* **Toggle Status**

  * This all making the record searchable by user or not

* **Delete**

  * This is a Hard delete\...currently not recoverable unless pulled from
 a backup. This will delete all associated data (including change
 request and evaluations)

* **Import**
  * Allows user to upload a bulk entry via a supported format. (Some
 formats are built-in, others are genetically mappable things, and
 still others may be added via plugins)

* **Export**
  * Exports selected entries (it doesn\'t export associated evaluations).
 The export is in the storefront standard format.

### **Entry Type**

Define Categories of information in the application. It supports
 multi-levels and many feature rely on the entry type to determine the
 capturing and display of the data.

* **Add** - Adds a new Type with all fields, Template Override - allows
 changing display of entries of this type, Data entry flag affect the
 entry form where information users can enter. The Icon Url applies an
 icon for all types. Setting the parent allows for nesting. (Can\'t
 have multiple parent.)

* **Edit** - Edit all fields except the Type Code

* **Entry Assignment** - Independent tool that allows for bulk
 reassignment of entry from one type to another.

* **Attribute Assignment** - allows adjust associated attribute to a
 specific entry type in a bulk way.

* **Move Top** - Make the entry type a root type

* **Toggle Status** Inactivating meaning new entry can be added to
 the type. But, inactive entry types are available to search. So
 deleting is the only way to completely get rid of it.

* **Delete** - Prompt to move the delete type to a new type. This move
 related entries to the the new type and move children to the new
 parent where appropriate.

### Entry Template

* Allows creation of display template that can be used in conjunction
 with Entry display.

* **Add/Edit** - Allow creating/updating template. This tool fairly
 complex.

  * The user must select an initial layout (only one) or one component

  * They user can + add component to main layout or drag n\' drop to
 existing layout to be added to that layout Component property can be
 added. see UI toolkit docs (extjs)

  * Custom components can be create and added to the tool palette (this is
 for all accessible users) The data tab allow for using existing data
 to populate the template to see how it would look

* **Toggle Status** - this remove template from being select as an
 override. (Existing entry that have the override remain intact until
 edited.

* **Delete** - Removes the template and it associations to entry type

### Submission Template

 Allows for creation of submission form for different entry types.

* Form that are pending need to be Verified

* Only verified and active forms can be used by users for the given
 entry types (Also, the entry type must allow submissions to show in
 the submission form pick list)

* Open the form in user mode will prompt for at least the name

* **Add Form/Edit Form** - This is complex form which allow for creating
 forms to capture information about an entry.

  * Create/edit section Add Questions:
  
  * All question have:
  
        * Question Number (which can be any text) Label - (Allows html)
        
        * Label Tooltip - Which shows a ? with the message the admin put in
        Required flag - makes the user have to have this field

  * Mappable Fields (Entry top-level fields see mapping)
  
  * Attributes (there is several varies of this and it also is effected by
   the attribute type self where it accept number or text); This is
   complex with a lot options.
  
  * Grids (allow multiple of the specific data type) Form (specific full
   form of data)
  
        * Static content (text block, or line, or image) Html

        * Image

* **Copy** - Copies a form; If the default is copied the default flag
 should not be set as there should only be one.

* **Toggle Active Status** - Only Activates verified templates. It will
 inactivate any forms for the same entry type if they are active.

* **Verify** - This actual create the a submission and if the submission
 is successfully submitted then the form should be valid. **Preview** -
 Allow view the form and entry data (The data enter is not saved); If
 there is no entry type then it prompt for that. **Import -** Imports
 the standard format (created by the export)

* **Export -** Exports the given form into a standard format

* **Delete** - This a hard delete of the template; if if was the active
 one then the entry type will fallback to using the default

### Partial Submissions

Admin view/support of unsubmitted entry.

* **Edit** - This allows admin to adjust the submission in progress to
 provide support. This will save as behave like the user submission.

* **Change Owner** - Allows transferring submission to a new owner

* **Delete** - Hard delete of submission

### Support Media (Admin)

Use to manage tutorials; (**Branding must have Show Tutorial Menu
 checked**)

* **Add/Edit** - Allows for managing support video; Note: only Video
 Media type is supported at this time

* **View Training** - Displays the select training video **Download** -
 save off the support media locally **Delete** - Hard removal of the
 support media

### FAQ

Use to manage FAQ as part of the user menu help (**Branding must have
 Show FAQ Menu checked**)

* **Add/Edit -** Allow an admin to post a question and answer (to allow
 user to see answers common questions)

* **Toggle Status -** Makes question and related answer public or
 private

* **Delete -** Hard deletes question and related answer

### Highlights

Use to manage highlights shown of the landing page

* **Add -** Add highlights that shows on the landing/home page of
 desktop version. This is for news or other items of interest.

      * Supports media (Using General Media) 

      * Support Saved

* **Edit -** Existing highlight

* **Display Position -** Forces a display order.

* **Delete -** Hard delete of highlight Integrations

### Imports

 Central UI for Imports and Import history. This primarily for
 third-party import\...but, it also used form system to system imports.
 Import History records only stay for specified amount of time then
 they are removed. This is primarily a 'developers-only' feature of the
 site, and it is not supported past v2.11.

* **Import -** Allows uploading data into application using a variety of
 format. (Standard built in formats, Mappable formats (Dynamic; see
 below), custom format that can be added via plugin)

* **Reprocess** - Re-run the file with the options same originally
 uploaded

* **Rollback -** This will remove records that were added/updated. For
 records that were update the record will be reverted to a previous
 snapshot taken during import. Added records will be removed. Keep in
 mind only the last file import marking will be recorded. So multiple
 changes to the same records will not necessarily results in a rollback
 for that record. The intent here is allow the last import to be
 rollback not one from long time ago or for rollback of \"stacked\"
 imports.

* **View Details -** Shows warning and error message captured from the
 upload process.

* **Download -** Allows for downloading previously uploaded file.

* **Mapping** (Beta feature)

  * This allow for creating custom field mapping and attribute map
   handling. Mapping file can be associated with file import that can
   have mapping. File import handling can actually be hybrid of mapping
   some fields and direct handling of others.
  
  * Add/Edit - Mapping can be complex. Common case is to load a sample
   file to extract fields and then map a field in the file to one in the
   storefront application. You apply several transformation to be applied
   to the data or field to aid in matching.
  
  * The Attribute import support different features then entry imports
   Copy - Copies a mapping configuration
  
  * Preview - Allows the user to upload a sample file and view how the
   mapping handled the data. This is a helpful tool to troubleshoot
   matching issues
  
  * Import - Import the exported mapping files in the export format
  
  * Export - Exports the mapping (use this to transfer mapping files from
   one system to another. IE develop to production)
  
  * Delete - Hard removal mapping

### Lookups

These are enumeration that are user-definable and populate a lot of
 the drop-down throughout the system. Table Show Tables that are
 available.

* Edit codes

  * **Add** - Allows adding a new code to populate drop-down
  
  * **Edit -** Can't change code but can
  
  * **Toggle Status -** Codes can not be delete; inactive code are not
   available to be added to new information
  
  * **Import -** Allow for importing a csv file of code for bulk changes
  
  * **Export -** Create csv of the active codes

### Media

 Manages all non-entry or section media used through the system in
 various contexts. Submission

* **Add -** Name must be unique as it used to access the media. Format
 supports depends on the browser. The application does not any specific
 format as it depend on the admin intended use.

* **View -** Shows select media, if possible

* **Copy URL -** Copies the resource url to the clipboard

* **Download -** Allows download the select record

* **Delete -** Hard delete of file

### Organizations

 Manage all organizations (both user and entries).

* **Add -** Create a new organization. Most of the time organization
 will be created automatically when an entry or user has a organization
 that doesn't exist it will be added. Only the name is carried over,
 the other fields can only be changed with this tool.

* **Edit -** Allows for updating org fields. If the name changes then associated records
 are updated to reflect that. Keep in mind global management was a
 later change and the name is the key. However there is a hashed key
 which is based on the name.

* **Logo -** If a logo is added it will be show on the organization
 search.

* **Reference -** Show all related data that has the selected
 organization

* **No Organization -** Finds orphan records. User profiles don't
 require organizations. At one point the external user management
 system didn't send that information.

* **Merge -** Moves all references from a org to the target org and
 removes the origin org.

* **Run extraction -** Used to sync the global with existing data.
 Rarely used \....everything should stay in sync. \*I haven\'t seen a
 drift since original implementation

* **Delete -** Hard remove of organization. Only allowed if there is no
 references.

### Relationships (relationships are not heavily used in Spoon)

Allows managing **direct** relationships between entities (this is
 often easier then use the admin entries which only does one entry at a
 time)

* **Creating relations -** create one or more relationship; create new
 relationship types via lookups. Relationships are meant to be only one
 direction. A → B not (A→B B→A) the inverse is automatically navigable.

* **Deleting -** removes relationship (hard remove)

* **Viewing -** View existing relationships (origin entry shows number
 of relations for an entry.

### Searches

Allows creating/managing shared search for use in entries and
 highlights

* **Add -** See Advanced Search

* **Edit -** See Advanced Search (edit existing)

* **Toggle Status -** removes it from the picklist

### User Data

* Questions

  * **Viewing questions** - Read only view of the question and answers.
 (Questions are always associated with an entry.) **Updating Status** -
 Questions and Answers can be auto approved or moderated. (via System
 Properties setting) Default is auto approve. To be approve the
 question/answer has to be active. Pending question/answer will only be
 visible to the owner.

* Reviews

  * **Activate/Approve/Inactive** - Allows for view and change the status
 to moderate the reviews. The admin is not allow to edit. This is done
 to prevent conflicts with users. An admin should reach out to the
 owner to request change.

* Watches

  * **Toggle Status** - Inactive watches will no alert user on entry
 changes

  * **View -** Allow viewing the entry with changes marked since last
 viewed

* Tags

  * **Creating -** Allows for adding new tags and associating them with an
 entry. For a tag to exist it must be assigned to at least one entry.
 There is no free floating tags.

  * **Tagging Entries -** This should show all entries that have a tag
 regardless of status (approval and active)

* User Profiles

  * **Message** - Allows sending emails to the selected users; Additional
 email can be added/removed. (This is a convenience to allow sending
 email through the application)

  * **Edit -** Allows for editing a user profile; keep in mind if the
 profile is externally synced then changes may only be temporary.

  * **Toggle Status -** Activate and Inactivate formats

* Export

  * **All -** Export all profiles

  * **Profile -** This can be the current page of the table or a selected
 profiles.

### Workplan Progress

Shows the state the entries or change request are in and this
 pages allow for moving entries through the workflow.

The entry shown are based on the user accessing the page. Some
 permission all for see all states (like a super admin). Others user
 only see entries of certain entry types that are unassigned or items
 that have been assigned to them.

User may only be able to see/change item in certain workplan states
 depending on the workplan configuration

* **View** - Displays the Entry and allows for admin to send comment to
 owner and other admin to complete workflow tasks

* **Work Process -** Shows the current step instructs and allows user to
 progress the entry/change request to the next or previous work step.

* Assign

  * Group - Assign the selected record to Role Group (Requires a admin
 permission)
  * me - Assign the selected record to me

  * Unassign - Unassign selected record

  * re-assign - Assign the selected record to specific individual (Requires a admin permission)

  * Assign to admin

### Workplan Management

* Workplan represents a series of steps that the entry take to get from
 submission to public approval. The workplans are very dynamic and can
 have many states. However, it only support moving from current step to
 next or previous.

* The application provides Default workplan that represent a simple
 path. In this way, using the workplans is optional. Workplan are tied
 to specific entry types. So each entry type may have a different
 workplan.

* **Add** - Creates a new workplan that an admin can add steps to. Steps
 can have actions.

  * Admin Role - specific which group is the admin group (so that can be
   used to determine who the \"Assign to admin\" is.
  
  * Status color change the colors of the states that the workflow can go
   through.
  
  * Workplan For - This only support Entry. (we were planning on adding
   support for evaluation at some point)
  
  * Entry type: One or more entry types to apply to (also support apply to
   children type so they don\'t have to be selected
  
  * Steps:
  
    * Short Description is the \"instructions for the step\" and will be
       shown workplan users and end user of the state

      * Approval state match - match the state with the state of the entry.
       This allow for external changes to reflect on the work plan. Also, to
       provide a bridge to the existing data.

      * Active On: Triggers step change based on events the occur in the
       system. The application will generate events that can be capture to
       switch workplan steps

      * Role Access: require a specific role to enter this step.

      * Actions: -apply when the entry moves into this step; Step run in the
       order specified

        * Email

          * Assign Entry Approve Entry Activate Entry

          * Assign Entry to group (Entry Type group) Inactivate Entry

          * Set Entry to Pending

* **Edit** - Take caution in edit an active workflow as the saving
 changes apply immediately. Steps change go through process moving
 entry to different states. (This can get quite complex; depending the
 adds and removes. But, it propagate series with the step of changes.
 like a series of events.)

* **Toggle Status** - Only active workplans will be used if inactive
 then the entry type will use default. Only one workplan can be active
 for a given entry type.

* **Delete** - Hard removal, This will prompt to allow moving existing
 records to a new work plan and different steps in that plan.

## **Evaluation Management**

### Evaluation

Allow for collection formalize / comparable detail for entries

* **Add** - Pick a template to base the evaluation on then a set of
 forms shown. These forms are split into entry and evaluation. An entry
 is the main listing item that may have multiple evaluations. (See
 evaluator guide for more information. This is a complex feature with
 many options.

  * Entry view only show the forms that entry type allows (except for old
   evaluation summaries)
  
  * Change History tracks all changes between saves
  
  * Comments can be applied to form and are tied to the evaluation (The
   comments are always private)

  * Previous published evaluations (read-only) are also shown allow for context

* **Edit** - Allow for filling in all evaluation fields

* **View** - View an evaluation as it would be show when published. It
 also allow publishing from here.

* **Toggle Status** - Inactive eval remove evals from evaluator lists to
 be worked on. Published inactive evaluations will be not be public

* Action

  * **Publish** - Make an the public sections of the evaluation public and
   approved the changes to the entry. It possible to just approve the
   entry portion separately. (See evaluation publish guide in the
   documentation) Once published you can\'t edit it. To edit unpublish
   and then republish.
  
  * **UnPublish** - This make a public eval back private again so it can
   be worked on. Changes made to the entry still are applied.
  
  * **Copy** - Makes a copy of evaluation (the copy will be unpublished);
   use this to create a new version of an existing evaluation
  
  * **Assign Group** - Assigns an evaluation to a group for filtering and
   reporting
  
  * **Assign User** - Assigns an evaluation to an user for filtering and
   reporting
  
  * **Toggle New Sections** - Allows an evaluator to add or remove section
   to an evaluation
  
  * **Toggle Question Management** - Allows questions to be added or
   removed from an evaluation
  
  * **Delete** - Hard removal of the evaluation (Doesn\'t affect entry)

### Evaluation templates

This is master template that is constructed of a checklist template
 and section templates.. (The other templates should be created first.
 see guide)

* **Add** - Creates a new evaluation template

* **Edit** - Edits existing template and it can apply changes to
* unpublished evaluations **Toggle Status** - Inactive evaluations
 can\'t be selected when creating a new **Delete** - Removes evaluation
 template only if it\'s no used on an evaluation

### Checklist templates

* Set of questions using to evaluate an entry

  * **Add** - creates a new checklist

* When adding question the default question order is determined by the
 order of the question in the checklist. User can reorder by
 drag\'n\'drop.

* **Edit** - edit an existing checklist and it can apply changes to
 unpublished evaluation that associated with it

* **View** - Give example view

* **Toggle Status** - Inactive checklist can\'t be added to a new
 evaluation template. It doesn\'t affect existing templates or
 evaluations

* **Copy** - makes a copy the whole checklist

* **Delete** - Delete a checklist only if it not part of a template and
 no evaluation use it

### Checklist Question

Question pool of question that can be added to a checklist

* **Add** - Adds a question to the question pool;

  * QID (Question Id) needs to be unique

  * Sections can be managed in the lookups

  * Tags; Allows grouping question to aid in searching when creating new
 evaluation templates

* **Edit** - Allow for change the question. Questions that are part of
 an evaluation will be updated to reflect the current question text.
 This is done to allow for correcting spelling or add clarification.
 Admin should be cautioned to not change the question text meaning as
 if will affect the integrity of the answers. If the question is
 different then they should create a new question.

* **View** - View question with all the narration and scoring criteria

* **Toggle Status** - Inactivate question can\'t be added to a new
 checklist template but, doesn\'t remove them from existing template or
 evaluations

* **Copy** - makes a copy of the question with a new QID

* **Import** - imports a set of questions. There is only one standard
 format but other can be added via plugins

* **Export** - all questions are export to allow transfer from one
 system to another

* **Delete** - Removes the question; only if it\'s not part of template
 and there is no evaluation associated with it

### Section Template

This represent a block of information like: Testing, Architecture,
 Notes. etc

* **Add** - Create a new section that may have 0 to many sub-sections.
 This allows for organizing information into well-define block of
 information. Section and sub-section can be public or private.
 Sub-section can also have custom form

* fields. However, fields are only used as a way to enter information.
 (beta feature; hasn\'t been used).

* **Edit** - Allows for changing the section. It doesn\'t change
 existing evaluation; it only affects the template. **Toggle Status** -
 Inactive sections are not shown in the pick list to be added to eval
 template or existing evals. **Delete** - Hard remove of a section.
 Only removes if it\'s not associated with a template or eval.

Application Management

### Alerts

 Are subscriptions to events that give information(sent via email) when
 specified events occur in the application. The intent is to allow for
 more reactive administration.

 The alerts are aggregated (multi-events in one email) per event type.

 Alerts by default have \~10 minute delay before being sent out to
 reduce emails.

* **Add -** Allows creating an Subscription to watch alerts. The alerts
 are aggregated (multi-events in one email) per event type.

  * **Change Request** - Alerts when a change request is submitted by a
   user
  
  * **Entry Comment** - Alerts when a non-admin creates an entry comment
  
  * **Entry Submission** - Alerts when a user submit an entry via
   submission form; If they submit and then unsubmit before the Alert
   message is sent then no email will be generated. The alert can be
   filter to on alert on specific entry types
  
  * **System Error** - Allow for watch different system error based on
   selected error types
  
  * **User Data** - This alerts on changes to tags, reviews (user
   reviews), questions/responses, contacts updates, user- created
   attribute code
  
  * **User Management** - Options include: User Registration, users that
 need approval

* **Edit -** Allows for editing subscription

* **Toggle Status** - Allow suspending a subscription or enabling

* **Delete** - Hard removal of subscription

### Branding

Allows for change colors, title, styles, logo\...to re-brand the
 software for other uses.

* **Add** - Creates a new branding record; There can only be one active
 at a time. There is a lot of options that can effect: colors, landing
 page and login

  * General branding has items that effect landing, search and menuing
  * Login page item to allow for controlling the content of that page
  * Support handling for help and feedback control
  
  * Security Marking (classification support) 
  
  * Color and logos that can be applied
  
  * Landing Page can be customized by added pre-built block and configuring
   them (Add/removing links\...etc) 

  * CSS (Ready-only) but, it help for overriding

* **Edit** - Allow editing the selected branding. Changes are applied
 immediately. Beware that landing may set to blank; if it\'s not
 created and the default landing page flag is not set.

* **Duplicate** - Duplicate the Default branding or selected record

* **Toggle Branding Status** - if setting to active; any other active
 record will be set to inactive. Only allows one active.

* **Reset to Default** - Reset the back to the default template which
 built in

* **Delete** - Hard removal; if the branding being deleted is active
 then the applicate just resets to defaults

### Feedback

 This shows all feedback regardless whether it was email or a Jira
 ticket created.

* **View** - Allows for viewing the feedback created by user via the
 various feedback forms.

* **Mark Outstanding -** This is just a flag to note feedback that needs
 to be addressed.

* **Mark Complete -** Flag items that are done

* **Delete -** Hard delete of feedback.

### Jobs

* **Resume** - starts a pause job

* **Pause** - Stops execute of a timed job (it will not effect a current
 running job)

* **Run** - Run job immediately

* **Pause Scheduler** - Pauses / resumes all jobs

* Tasks (Background 1 off tasks)

  **Stop Task** - attempt to cancel a

  * **Delete** - Removes a task record; typically a timeout period will
 clear this.

### Messages

* This shows message for both queued and non-queued items. Queued item
 include: Watches, submission notifications and Alerts. No queued
 notification like system event may only be shown in the application.
 (desktop only)

* **View** - View the messages and if it was sent then it will show who
 it was sent to.

* **Process Messages Now** - Runs the message job now (default is run
 every 10 minutes). The job will pick up active message, aggregate
 where possible and send them to the request users.

* **Cleanup Messages** - Removes message that have been sent out that
 have expired base on the configure expired time

* **Delete** - Remove message; hard removal

* Event Notifications

  * **Create Admin Message** - These messages are shown to the user in
 their notifications box; Leaving the send to blank or set to \'All\'
 will send the message to all active users.

  * **Delete** - hard remove of message

### Reports

This allow for generating reports on entry and usage; It a report
 error an admin can view the errors in the system UI

* **New Report -** Creates reports Can run now or

  * Options available depends on the report

  * Output and outputs depend on what the report support and what
 permission the \"Action\", \"Report on items that need administrative
 attention.\"

  * \"Entries by Category\", \"Reports on entries in a category.\"

  * \"Entries by Organization\", \"Reports on entries that belong to an
 organization\" \"Entry Detail\", \"Exports entry details\"

  * \"Entry Listing\", \"List approved entries in a summary\" \"Entry\",
 \"Reports on entry statistic and usage.\"

  * \"Entry Status\", \"Gathers information about entry status for a time
 period.\" \"Evaluation Status\", \"Reports on the status of
 unpublished evaluations.\" \"Link Validation\", \"Reports on
 potentially broken external url links.\" \"Submissions\", \"Reports on
 entry submissions.\"

  * \"Usage\", \"Reports on overall application usage.\"

  * \"User Organization\", \"Reports on the user organizations in the
 application and their usage.\" \"User\", \"Reports on users and their
 usage of the application.\"

  * \"Workplan Status\", \"Shows the current status of items in
 workplans\"

* **Scheduled Report -** Show all of your scheduled reports and allows
 for management of those reports. There is a permission that allows an
 admin to see all reports scheduled.

  * Report edit will then run if schedule on an interval Add new reports

  * View the configuration on details

  * Toggle status to \"pause\" or \"resume\" reports Delete - Hard remove
 of report configuration

  * **View** - Shows html render preview of the report **Download -**
 Downloads a completed report to **Details -** Shows all configuration
 option on a report

### Security

* **Save General Form** - Saves security policy which should apply
 immediately

  * Disable User Information Editing - Block users from editing their
 profile; Meaning they should use an external system to change
 name/email\...etc

  * CSRF - enable token generation and passing; It make using the API more
 secure but, it can cause issue with stale clients

  * JSON-P - is used for cross-site API access (old method for support
 where CORS is not supported) CORS - is used for cross-site API access

  * Allow Registration - turns on user registration on or off

  * Require Proof of Citizenship - NOT USED; concept abandon after it was
 determined not to be needed

  * Require Admin to unlock - If the user fails attempt then they can lock
 their account; either the admin or system will unlock the account.

* **Shiro Config** - Allows for viewing config. It can be used to change
 properties. (Changes don\'t apply until the server restarts). The save
 creates a backup of previous setting on the server. (only last back is
 saved)

### Security Roles

* **Add** - creates a new role with no permission and data restrictions

* **Edit** - The name cannot be edited as it\'s the key to the role. To
 Rename: create a new role and then delete(

* **Manage Users** - Add/Remove users from role

* **Manage Permissions** - The permission are group by feature with
 fine-grain control. However, it doesn\'t prevent invalid combination
 such as write with no read or page access. It\'s up to an
 administrator to assign permissions in a meaningful way. See role
 guide for more information.

* **Manage Data Restrictions** - Allows for setting data source and
 sensitivity restrictions. The values can be managed in the
 \"lookups\". By adding restriction a user of that role can now add
 restriction to the data which may prevent other user from seeing the
 data.

* **Delete -** removes role and optionally move old role user to a new
 role

### System

Troubleshooting and control tools

* **Status** - show all system status, threads (what is the server
 currently doing), properties

* **Error Tickets** - Allow for view the last X amount tickets
 generated. (5000 default; as new one come it pushes old ones out so
 the application doesn\'t get overloaded)

* **Application State Properties** - Properties that are stored in the
 DB and represent state of the application
 
* **System Configuration Properties** - This hold the configuration for
 the application and controls optional feature and integration
 parameters. It\'s backed by the openstorefront.properties file

* **Logs and Logging** - Allows for runtime control of logging detail
 and allow for capturing logs into the db for easy access. Logs are
 always sent to the file system via console depending on the server
 config

* **Plugins** - Allows for add/removing plugin to extend the storefront

* **Managers** - Manager control external resource and this allow
 managing the application and restart effect parts from configuration
 control

* **Cache** - Shows all caches in the system and allow for manual
 clearing

* **Search Control** - Control the search search integration (resetting
 the search index)

* **Recent Changes Email** - control the recent change email which is a
 global email that users can subscribe that notifies then about changes
 in the data over a period of time

### System Archive

Make sure your have enough disk space to create the archive; see Admin
 → Application Management -\> System. The archive is create on the same
 machine.

* **Generate** - create an archive to allow moving data from one system to other. **This feature is unsupported, depreciated**

 DB Export - exports just the db base in a format that db handler can
 understand.

* Full Export - Create a DB export and pull all of the files on disk to
 create a complete export. Note: it skips the archive folder to avoid
 pulling itself and reducing size

* General - Allow for creating archives of specific entities and also
 supports pulling related entities to create a complete entity.

* **Import Archive** - Allows for uploading a previously generated
 archive into the current system. The application will read the
 manifest file and attempt to restore archive. Replacing records. (some
 records still follow the same save process as other

 import. However, db import are a full replacement wiping out previous
 records. DB Import will take the application offline until complete

* **View Error** - Show error generated when exporting or importing an
 archive

* **Download** - download the selected archive

* **Delete** - Hard delete of archive.

### Tracking

* Used to show gather usage metric for reports

* **User** (login events)

* **view** - View login events

* **export** - Allow exporting current search to a CSV for further work

#### Entry

* **view** - View entry events

* **export** - Allow exporting current search to a CSV for further work

### User Management (Only available; if using builtin user management)

 Manage actual login accounts and registration for new accounts.

* **Approve -** Approves an account so they can now login

 **Reset Password -** Reset password to a admin created value; change
 is effect immediately

* **Manage Roles -** Add or remove roles to a selected user

* **Message -** Email the selected user

* **Unlock Account -** Unlock a locked account

* **Lock Account -** Locks the account out from logging in.

* **Reset Failed Logins -** Resets the count of fails so the user can
 try login again with ricking locking.

* **Delete -** Removes the user. user profile and their registration

#### Registrations

* **Add -** Create a new Registration filling a similar form as the
 users

* **Message** - Email register / or pending register user

* **Delete -** Removes Registration; Account, if created, is unaffected.

### API Documentation

* This is generated from the source code live so it always reflects the
 current code.

* Navigation (view) - All view resources and service end-points

* Check Lookup table - Show the current values in the enum tables. This
 is useful for users of the API to make sure there values are
 acceptable

* Check Attribute Table -Show the current values of attributes

* Entities - This describes all data models used in the application and
 their relationships and constraints.

* Print - This will create a printable view of all APIs. This is quite
 large as it\'s not normalize as per all the modules. Also there is a
 lot of end-points.

## **User Tools**

### User Profile

* Security policy can be set to allow editing of the user profile or not editing.

* **Test Email -** This should send an email to the address currently
 enter in the form.

* **Save** - This should update all user controlled fields

### Submissions

 **New Submission -** Allows use to create new entries. The entry type
 available depends on the entry type config. The form generated depend
 on Custom Submission form feature. The forms are dynamic and allows
 for guided entry of data.

* **Edit** - involves a complex process potential transform an entry
 into a partial submission and back again on submit. You can\'t edit a
 submission that has an active evaluation as this would cause a
 deletion.

* **Request Change** (Only on approved)

* **Comments -** Allow owners to send comments to admin and for owner to
 read comments sent back from an admin. Comments should persist
 throughout the process of submitting, editing, and change request.
 Some comment become locked. (meaning no editing

 Option

* **Preview -** Shows what the submission will look like on the
 application when approved.

* **Copy -** creates a copy of submission (only of approved or pending)

* **Toggle Notify -** Notifies submitter when the submission is approved

* **Request Removal** (Only on approved) - Send feedback that the owner
 would like to remove an entry. Admin would need to do the actual
 remove. The admin should just inactive in most cases.

* **Delete** - Only allows remove of non-submit entry. This hard removal
 of a partial submission.

### Watches

 Allows users manage all watches in place

* **View** -- The entry with the watch

 **Toggle Email** -- By default watch only show diffs in browser
 however the user can subscribe to email changes. The Email is only
 explain section of information changed (IE. Contacts)

* **Delete** -- remove the watch (hard removal)

### Questions

 Show all the users posted questions/answers

* **View the entry** -- view full details

 **Edit --** Allows updating the question/answer (If moderation is
 on....it should admin approval to make the change public. The
 question/answer would no longer be public (it should go to pending)

* **Delete** -- Inactivates the record (Admin Recoverable)

### Reviews

* **Edit -** Allows updating the question/answer (If moderation is
 on....it should admin approval to make the change public. The
 question/answer would no longer be public (it should go to pending)

* **Delete --** Inactivates the record (Admin Recoverable)

### Searches

 Saved Advanced Searches (See advanced search)

* **Add** - Add a new Search

 **Edit** -- Edits a search

* **Search -** Performs selected search and takes user to the search
 screen

* **Delete** -- Hard delete of a search

### Relationships

* Show how data is connect to each other either through direct or
 indirect relationships. The purpose of this for SPOON is to all the
 ability to show form the Satellite to the components/parts that make
 up the satellite.

* **View** - Support several views data angles:

* **Entries** - Show Direct relationships

* **Organization** - Shows entries related to an organization
* **Attribute** - Show entries with the selected attribute code 
* **Tag** - Show entries with the selected tag

* **Navigation -** Allows drilling down or into other relationships like
 attributes and organizations

* **Details -** shows the details about the selected node; where it be
 entry, organization, attribute..etct

* **Download Image -** Allow capturing a screenshot of the current
 diagram. **(Doesn\'t support large screens beyond (1920x1080))**

### Change Password (Internal User management only)

* **Update password -** Replace the old password with a new user created
 password. (The new password will be expected to be used next login)

Evaluation Tools

### Evaluations

* **View** - Previews the entry with the evaluation

* **Edit** - Allows evaluator a to fill in eval. (Unpublished only).
 (See Admin evaluations for details)

* **Assign user** - Assigns to any user in the system (eventually should
 only be evaluators.) The assignment doesn\'t send any emails it just
 makes it easier for the evaluator to find the evaluation that they are
 working on and for reporting.

Help

* Help is dynamic generated based on the user access (in coarse-grain
 way; mainly page by page)

* **Updating** - The help is bundled with the application and is only
 changeable at build time. The help is all in one markdown document
 that gets converted and parsed so can be dynamically generated.

* **Search** - Client side search of the help content; focused on the
 section header.

* **View** - Popup window in page and

 **Print** - This formats all section into a more print friendly view.

Contact/Feedback Form

 Contact form is used a few different ways.

* It can be configure to submit ticket JIRA It can submit the
 information to an email All feedback is captured in the database

* The User profile in can be read only or provide a way to update (if
 allow via the security policy)

Tutorials (Support Media)

 This is from the user perspective. (See: Admin Support Media) Users
 can select and view support media

 The Window is not modal to allow a user to follow along

* Can be opened in a separate tab

Classification Marking Support

* Support for adding a classified warning banner and marking at entity
 level security markings.

* Enabled in branding (See security section)

* Lookup Security Marking Populate the drop-downs on the forms (security
 marking field) The security Banner once set will show on all screens
 (desktop and mobile)

* If enable and there are markings available then the follow form will
 show the security drop-down:

* Admin Entries, Evaluation Edit, Submission form, Admin Attributes,

* Details Review and Question/Answers

 If there are security marking in the detail information of the entry
 then the UI in the search results and detail will show the security
 marking

Data Restrictions

* Allow filtering the entries based on data source and sensitivity. This
 can be control per role or as a combination of roles.

Login

* Used for internal user management based logins. (Effects SPOON, local
 developers)

* Login -brandable and customizable to certain extent like adding video
 or customizing folder, Login should all Branding effects on login

* Forgot Username -

* Forgot Password - Email the user based on the email associated with
 their user name. The password change doesn\'t occur until the email
 link has been clicked.

Registration

* Allows for user to sign up for an account. Only supported via Internal
 user Management.

* -The form support auto/manual approving of account via the security
 policy

* User have to verify their account via a code sent to email. (This has
 been problematic due to email servers blocking the email due to spam
 and whitelists)

Mobile UI (through at the year; tracking show very little use) (At conference time in August)

System

* **Initial first deploy** should create a database and setup defaults.
 There shouldn\'t be any entries. Re-deployment of the application
 should remain stable

* Solr Support (remove sometime in future)

* Docker Hub Release; Should provide a working release on docker hub for
 external users. LDAP Syncing (SPOON doesn\'t use this)

* Open Am integration (Header based login) (SPOON doesn\'t use this)
 Elastic Search

## Supporting Documentation

Roles:


 **Administrator** (Standard)

 **Librarian** (Standard)

 **User** (Standard - **DEFAULT**) **Evaluator** (Standard)

 **Guest User** (Standard; this is for user that are not logged in)

 **SME** (SPOON - Admin defined)

 **IR** (SPOON - Admin defined)

 **Integration** (SPOON - Admin defined)


<span style="font-size: 24px;">User Guide</span>

Version 1.6 [BETA]
                                                      


# 1. Overview
------

The DI2E Clearinghouse application is a software cataloging system that
is used to catalog entries of interest to the DI2E community. 
Entries include Government off the shelf (GOTS), commercial off the
shelf (COTS), and Open Source software (OSS). The evaluations
done by DI2E's Centers of Excellence are displayed in the Clearinghouse 
and give details on the following:

-   Ownership
-   Where/How to access the software
-   Software vitals
-   Conformance
-   Links to documentation, source code and other artifacts
-   Evaluation information

**DI2E Clearinghouse is developed by Space Dynamics Laboratory for DI2E and is
licensed under Apache V2.**

![Logo](../../../../../../client/openstorefront/app/images/logo2.png)
![Logo](../../../../../../client/openstorefront/app/images/help/sdl.png)

## 1.1 Release Notes
------

**Insert the build notes here of major tickets**




### 1.1.1 Known Issues, v1.6 * 
 * JIRA [STORE-662](https://jira.di2e.net/browse/STORE-662): Search -> Wildcard Searches:  If using wildcards in multiple words you need to include that in quotes such as "softwar* eng?neering".  Help documentation and/ or website operation needs to be updated.
 * JIRA [STORE-721](https://jira.di2e.net/browse/STORE-721): Admin Tools -> Data Management -> Attributes -> New Type -> Manage Codes:  When adding a new code, it may not show up in the table below even after refreshing the table (refresh button click).  WORKAROUND:  Exit this section and come back into it by selecting the type again and then Manage Codes.
 * JIRA [STORE-731](https://jira.di2e.net/browse/STORE-731): Admin Tools -> Data Management -> Integrations -> Jira Configuration tab:  Drop-down mennu information is not in alphabetical order as expected.  When delting a mapping the row is deleted above, but the data persists in the boxes below and is not cleared.
 * JIRA [STORE-722](https://jira.di2e.net/browse/STORE-722): Admin Tools -> Data Management -> Organizations -> Merge: **NOTE** that the merge feature ONLY merges references and NOT the organization information.
 * JIRA [STORE-674](https://jira.di2e.net/browse/STORE-674): Admin Tools -> Data Management -> User Data -> User Profiles:  When filtering by Status a blank or none status assumes only Active (Inactive results are not included).
 


# 2.  User Features
------

The features that are available to users to be able to access the information in a variety of ways.

##   2.1  Searching
------


Clearinghouse Search provides a way to find entry lists and articles
matching search criteria. Several types of searches are supported. The
types of searches that can be performed are:

1.  Searching across multiple fields (entry name and description,
    tags, attribute types, codes, and descriptions)

2.  Wildcard searching (\* and ?)

3.  Exact phrase searching

4.  Case insensitivity

**Note:** The type ahead on the search field only looks at the title to see if  it contains the text.  
The user can then select the suggestion by clicking on it or continue with a full text search.

### 2.1.1 Search Field Differentiation or Specificity
-------

Currently, there is no differentiation among entry Name, entry
Description, Tags, or Attributes as to the preference, specificity, or
coverage of the search. A search term or phrase is searched via a single
search box, and applied across all fields equally. Advanced search per a
specific field is not currently available.

### 2.1.2 Fuzzy Matching and Wildcards
------

Fuzzy matching is defined as the ability to perform inexact matches on
terms in the search index. For example, this could be used to find all
variations of prefixed or suffixed words and multiple spellings of a
word.

Fuzzy matching is done with the \* and the ? characters acting as
wildcards. It is also known as wildcard searching. The \* character
represents a string of letters; whereas the ? acts as a placeholder for
only a single character. The process works as shown in the queries
below:

Query: offi\* Matches office, officer, official, and so on

Query: off\*r Matches offer, officer, officiator, and so on

Query: off?r Matches offer, but not officer

### 2.1.3 Phrase Searches and Exact Searches
------

Exact searches must be enclosed by double quotes.

Search example: "create a common framework"

Wildcards are only meant to work on individual search terms, not on
phrase searches / exact searches, as demonstrated by this example:

Works: softwar\* eng?neering

Does not work: "softwar\* eng?neering"

In the second example, the search would only return results that had the
\* and ? characters exactly in their respective places instead of
accepting any character in those locations as the user intended.

### 2.1.4 Case Insensitivity
------

Searching is case **insensitive** and allows mixing of case. In the
examples below, the exact same results would be returned regardless of
the capitalization.

Search examples: "create a common framework" **OR** "Create a Common
Framework"

Search examples: create common framework **OR** cReatE CoMmoN
FraMewoRk

### 2.1.5 Noise Words
------

Avoid searching with noise words, unless they are part of a
double-quote-enclosed exact phrase search. Some common noise words
include: a, an, and, are, as, at, be, but, by, for, if, in, into, is,
it, no, not, of, on, or, such, that, the, their, then, there, these,
they, this, to, was, will, with.

Search example: create a common framework

This search works, but the 'a' will return more results than are desired
since results with the word 'a' will be returned in the search.

Search example: create common framework

This search works, and will return fewer, more specific results. It will
return *only* those places where the three words *create*, *common*,
and *framework* are found.

The only time that including noise words may be recommended is when
performing an exact search and the words are a necessary part of the
phrase being searched. For more information on exact searches, see
section 1.1.3.

## 2.2 Search Tools and other search methods
------

To the left of the search text field is an icon for Search Tools.  Click
on the icon to display the Search Tools.  Three types of unique searches 
are available:

1.  Topic
2.  Category
3.  Architecture

### 2.2.1 Topic Search Tool
------

The topic tab in the Search Tools window allows searching by Articles, 
DI2E Components, or other topic search types.  

After clicking on the topic on the left the results are shown on the right.  
Click on the column name to sort or click the link button to link to the 
individual Clearinghouse entry.  Results of all of the topics can also be 
shown in a new window.


### 2.2.2 Category Search Tool
------

The Category tab in the Search Tools window displays many categories that
are expandable by clicking on the down arrow icon.  Click on the lowest 
subcategory to display search results.  Clicking on the column name sorts
the table by that column.  Clicking on the link button opens the 
individual entry, or all results can be displayed in a new window.

### 2.2.3 Architecture Search Tool
------

The Architecture tab window displays a heiarchy of results per SvcV-4 
taxonomy.  Expand All and Collapse All buttons are provided.  Clicking 
on the **+** button expands an individual folder and **-** collapses the folder.  
Search results are displayed with sortable columns and links to individual 
results or the entire search results can be opened in a new page.

### 2.2.4 Tags  
------
Users can "Tag" listings so that they can be used to later find and
refine search results. User-created tags are visible to all users.
However, only the owner of the tag or an admin user may remove a tag.
Tags are used in the search field shown at the top of the screen and 
on the home page.

**To create a tag:**

 1.  From the search results screen, view the details by by clicking on the title.
 2.  Click on the View/ Edit Tag icon to open the tag panel.
 3.  Enter a new tag in the text field OR a single space to view existing tags.

### 2.2.5 Questions  
---------

Questions or "Q&A" allow users to post questions about components and
allow other users to answer those questions based on their experience
with the component.

**To post a question:**

1.  From the search results screen, view the details by by clicking on the title.
2.  Click on the Q&A tab.
3.  Fill out the form at the bottom.
4.  Click the **Post** button.

**To answer a question:**

1.  Expand the question by clicking on the expand (down arrow) icon.
2.  Fill out the form under Give an Answer.
3.  Click **Post**.

Once you have answered a question, your answer may be edited and removed
using the buttons next to the answer.

**Message User***
Clicking on this next to a question or answer will allow a message (email) to be
sent to the user that submitted the question or answer.  This can be used to 
gain additional clarification.

### 2.2.6 Comparing
---------

The comparison feature allows for side-by-side viewing of multiple
listings. For example, there may be several components that provide the
same kind of features (e.g. Cesium and Google maps). You can use the
comparison feature to view their details together to find the component
better suited your project's needs.

**To compare components:**

1.  From the Search Results Screen click **Compare All** or click the
    check on 2 or more components and then click on the
    **Compare** button.

2.  In the compare screen, click on two of the listings to display
    them side-by-side.

3.  You can customize the details being viewed by checking the items
    available in the **Sections to compare** area.


## 2.3 User Tools
------------

All user information can be set on the main page of **User Tools**. 
Initially the user profile is populated with information from the user's 
external security account.

**To edit your profile:**

1.  Select **User Tools** from the user menu.
2.  Update the form.
3.  Click **Save**.

### 2.3.1 Submissions
-----------

This page displays all of the submissions you, the current logged in user, have
made.  In the User Submissions table the columns can be sorted by clicking on 
the column header.  

In addition icons for Edit, Preview, Copy, Toggle Notify, Unsubmit, and 
Request Removal can be found along the top of the table.  Finally,
a **+ New submission** button allows for a new entry to be made.

#### 2.3.1.1 Submission Form- Getting Started
-----------

The first tab of a new submission should be pre-filled with your logged in user information.  
Make any necessary corrections and click on the Next button at the bottom.

Please be aware:
This form will submit a component to the DI2E Framework PMO for review and consideration.
A DI2E Storefront Manager will contact you regarding your submission. 
For help, contact helpdesk@di2e.net


#### 2.3.1.2 Submission Form- Required Information
-----------

The following fields are on the Required Information section:
| Field | Values | Notes |
| -----: | :------ | :----- |
| Entry Type | DI2E Component, Article, etc. | What you enter here will determine what other fields show up on this entry form below |
| Entry Name | The name of the article or component | | 
| Description | Detailed description body | You can use highlights, colored text, links, etc. |
| Organization | Name of Company or responsible party for the entry | These can be set by an administrator |
| Attributes | Required attributes are shown here and must be selected | |


#### 2.3.1.3 Submission Form- Details
-----------

This allows for many different items to be added.  Each section can have one or multiple 
items added with the use of the Add, Edit, and Remove buttons.  When finished click the
Next button to go to the final section.

Items that can be added here depend on the Entry Type selected in Required Information,
but may include:
Additional Attributes, Contacts, Resources, External Links, Media, Dependencies, Metadata, 
Relationships, Tags, Evaluation Information, Reviews, Questions, and so forth.


#### 2.3.1.4 Submission Form- Review
-----------

On the final submission screen all of the previously entered information is available
for review.  The entry can then be submitted for review.


### 2.3.2 Tools
-----------

The Tools drop-down contains three options:

1.  Watches

2.  Reviews

3.  Reports

#### 2.3.2.1 Manage Watches
------------

The user can create watches that can send notifications to the user. The
messages that the watches generate are viewable from the User Message
Tool. The User Message Tool allows for queued messages to be processed
after a delayed time period. The queue delay allows for messages to be
pooled by type thereby reducing the amount of emails rapid changes can
create. The User Message can also be used to view previously sent
messages in cases of troubleshooting whether the system sent a
notification or not. The tools also allow for manually processing the
queue immediately, but in most cases this is not needed or desired as
the automatic process will take handling the message queue.

A watch is set on a component to allow for a user to be notified of
changes to a component. There are two ways notifications occur in the
application and there is no limit to the amount of watches a user may
place.

1.  The watched component's changes are visually shown to the user
    anytime changes have been made since they last viewed the component.
    This is seen in the watches tab and when viewing the details of
    a component.

2.  The user may opt to have an email sent anytime there is a change. To
    opt in, see the steps listed below. The email will list the section
    of the component information as to what has changed. A user then can
    login to view the exact changes.

**To activate email:**

1.  Make sure your user profile contains a valid email. View your
    profile from the Profile button at the top of the screen in User Tools.

2.  Check the Send Email Notification box on any of the watches.

**To remove a watch:**

1.  Click on the delete icon.

2.  Click OK to confirm the watch deletion.

**NOTE:** Watches can also be removed from the component detail view 
(in search results) of the component being watched.

**To create a watch:**

1.  From the search results screen of an entry:

2.  Click on the Watch icon ![watch](../../../../../../client/openstorefront/app/images/help/view.png) to add the component
    to your watch list.


#### 2.3.2.2 Manage Reviews
------------

From this screen in the User Tools you can inspect, edit, and delete your component reviews.

**To inspect a review:**

Expand the desired review by clicking the expand (down arrow) icon.


**To edit a review:**

1.  Once the review has been expanded by clicking on the down arrow, 
click the **Edit** button and adjust the information in the form.

2.  Click **Update Review**.

**To remove a review:**

1.  Expand the desired review by clicking the expand (down arrow) icon.

3.  Click on the **Delete** button.

**To create a review:**

1.  From a search results screen, view Component Details by clicking on the title.
2.  Click on the Reviews tab.
3.  Click on **Write a Review**.
4.  Fill out the form starting with marking the Stars according to
    your experience.
5.  Save the review. The review will now show in the review tab for
    other users to read.

**NOTE:** A review may also be removed from the component detail screen
by then clicking on the delete icon next to the title of the review. 
You can only remove reviews you post.


#### 2.3.2.3 Manage Reports
--------

This section displays scheduled user reports as well as the history or log of reports that have ran.

**Schedule tab**
This default view shows all of the scheduled reports.  A drop-down at the top
allows viewing of the Active or Inactive reports.  Select a report in the table
and click Toggle Status to change from one status to the other.

Reports can be added or edited.  Selctions can be made to run a report one time
only or at regular intervals.  Some reports have the option of HTML or Comma-
Seperated Values as the report format.  If slecting Previous days, the 
selected number of prior days will be included in the report output.  

Types of reports available include:
  
  - Component
  - Component by Organization
  - Component Detail
  - Components by Category
  - Link Validation
  - Submissions
  - Usage
  - User
  - User Organization

**History tab**
This table is helpful to view old reports or see what has been run and when.  Select
a report and click view to see the output of the report and that particular date and
time.  A report can be downloaded / exported for archiving or offline use.

## 2.4 Changes Notification
--------------------

The application sends out notification emails about any changes that
have occurred since the last time a recent change notification email was
sent out. This allows the user to be aware of all the new and modified
listings on the application.

**To receive the notification:**

1.  Select **User Tools** from the drop-down menu in the upper-right with the username.
2.  Ensure that a valid email address is present in the **User Profile** section.
4.  Check the checkbox, **Receive periodic email about recent changes**.
5.  Click **Save**.

## 2.5  Printing
--------

Many areas of the website have a **Go To Print View** icon to facilitate printing
of the displayed information.  In the print view, various customizations are
available such as creating a custom template or including and excluding certain
labels or display areas.  


# 3. Admin Tools *
----------

Admin tools allow for the management of all data in the system.
 
(Sections marked with a * requires admin account access)


## 3.1 Dashboard *
--------

The dashboard is the homepage of the Admin Tools, where various statistics are shown 
about the Clearinghouse.  There are four main sections in the dashboard: 
Entry Statistics, Notifications, User Statistics, and System Statistics.


## 3.2 Data Management *
--------

The Data Management menu is used to navigate to different tools for managing the data
for Storefront.


### 3.2.1 Attributes *
-------

Attributes are used to group and filter catalog items such as components and articles. 
To access the attributes page, go to Admin Tools > Data Management > Attributes.

Some of the major functionality of Managing Attributes includes:

  1. Types
  2. Codes
  3. Exporting
  4. Importing
    
#### 3.2.1.1 Types *
------

When adding or editing an attribute the first tab is the Type tab.

Attribute Types represent a related group of specific categories or
represent a single attribute of a listing (Eg. "Funded" would be a type
with the corresponding value "Yes" or "No"). A type may have many codes.  

Deleting a type will delete the associated codes and component
attributes. Inactivating a type will inactivate associated data such as
component attributes.

**NOTE**: Avoid "/" and special characters in type codes. For example,
use: \[A-Z\]\[0-9\].

**To add or edit a type:**

1.  From the Attribute Tool, click on the **Add** or the
    **Edit** button.
2.  Fill in the form.
3.  Save the form and you can proceed to editing the codes for the type.

**NOTE:** The Attribute Type Code field should be all caps and should not
contain spaces or "/".

 The Import will be processed in the background. The task notification
 area next to the user menu will indicate when the import task has
 completed.

 The processing behavior is: for every code not in the import, the
 existing code will be inactivated. If the code already exists and
 doesn't match, then it will be updated. For code that matches, the
 code in the file will not be processed. If a type doesn't exist, a new
 one will be created. If the type matches, it will be updated. Other
 existing types not found in the file will not be affected.


#### 3.2.1.2 Codes *
------

An attribute code is a specific category value that can be applied to a
component. For example: Component listing may contain and attribute for
"Funded" as "Yes".

**To add and edit a code:**

1.  In the Edit or add window under the tab **Manage Codes**
2.  Click **Add New Code** or the edit icon next to an existing code.
3.  Fill out the form.
4.  Click **Save**.

Field  Descriptions:

-  **Label**               - Human readable value of the code
-  **Code**                - System reference key
-  **Description**         - This is a detail description of the meaning of the value. It's used in popup to help users interpolate the data.
-  **Group Code**          - Using to create sub groups in the code. Typically used only in special cases.
-  **Sort Order**          - Use to force a specific sort order. (Default is sort by Label)
-  **Architecture Code**   - Used for special hieratical sorting. Only applies to Architecture attribute types.
-  **Badge URL**           - Set to add a graphical badge for a code
-  **Highlight Class**     - Used to add color emphasis to a code.


#### 3.2.1.3 Exporting Attributes *
------

From the Manage Attributes page select one or more attributes.  Click the Export button.
Follow the browser download instructions to download the JSON File.

#### 3.2.1.4  Importing Attributes or SvcV-4 *
------

Clicking the **Import** button from Manage Attributes will show buttons to brows and upload
files for either Attributes or SvcV-4.  
1.  Browse to the desired file to be uploaded (imported).
2.  Make sure the Attributes or SvcV-4 file is in the correct format (CVS) and it
    contains the correct columns. (See warning on upload for the
    expected fields)
3.  Select the proper file.
4.  Click **Upload**.

 **WARNING:** UID in the file should match what is stored in application.
    The application supports both 1 and 0001 matching to 1.


### 3.2.2 Entries
-----

#### 3.2.2.1 Managing *
----------

Components can be managed using the Components Tool.  Components are created by an application administrator.

**To create a component:**

1.  Click on the **Add** button in the toolbar.
2.  In the Add/Edit dialog, fill in the form.
3.  Click **Save**.

**NOTE:** Setting a component's Approval State to **Approved** will make
the component immediately searchable. It's recommended to initially set
the component to **Pending** until all data is entered.

1.  After saving, several tabs are available which allows for entering
    Additional information such as Attributes, Contacts, and
    Resources, etc. Each tab has independent forms allowing for
    fine-grain editing which is important for the watches feature.

**To edit component:**

1.  Click on the edit icon ![edit](../../../../../../client/openstorefront/app/images/help/edit.png) in the action column
    on the component you wish to edit.

2.  Make changes by filling out the appropriate form.

**To preview component:**

1.  Click on the preview ![view](../../../../../../client/openstorefront/app/images/help/viewsm.png) icon in the action
    column on the component you wish to view. A new tab in browser will
    open and display the component detail page.

**To toggle active status:**

1.  Click on the activate/inactivate ![toggle](../../../../../../client/openstorefront/app/images/help/toggle.png) icon in the
    action column on the component you wish to edit.

**NOTE:** Use Filter Options to show active/inactive components.
Inactive components are not visible to or searchable by users.


**To remove a component:**

**WARNING:** This is a hard delete and it will remove all associated data
(media, user reviews, questions, etc.)

1.  Click the delete icon ![trash](../../../../../../client/openstorefront/app/images/help/trash.png) to remove.

2.  Confirm that you want to delete the component by clicking **OK** on
    the confirmation dialog.

**To export a component:**

1.  Select the component(s) you want exported.

2.  Click the **Export** button in the toolbar.

 Components are exported as zip files containing a json array of the
 component data, media and local resources associated with the selected
 components.

**To import a component:**

1.  Select a file (zip or json text file).

2.  Select import options (i.e. the user data you want to import).

3.  Click **Upload**. The processing will happen in the background and a
    new task will be created to handle the import. The application will
    notify the admin when the task is complete or if there was an issue.

#### 3.2.2.2 Approval *
------

Approved components are searchable by all users. Pending/Not submitted
components are viewable only by an admin. The approval date is
considered to be the Recently Added date for the Recently Added list on
the home page.

**To approve component:**

1.  Navigate to the Component Tool.

2.  Click on the Edit icon ![edit](../../../../../../client/openstorefront/app/images/help/edit.png) on the component
    to approve.

3.  Select **Approved** in the approval status dropdown.

4.  Click **Save**.

**NOTE:** An admin can mark a component as **Pending** by following the
step above and selecting **Pending**. Marking the component as
**Pending** makes it unapproved.


#### 3.2.2.3 General *
------

General information about the component is found here such as the Name, 
Description, Organization, etc.

**To edit General fields:**

1.  Navigate to the Component Tool.

2.  Click on the **Edit** icon ![edit](../../../../../../client/openstorefront/app/images/help/edit.png) on the component.

3.  Click the **General** tab.

**Other Fields**  
a. Approval State:  Pending | Approved | Not Submitted  
b. Component Type:  ARTICLE *(allows for watches to be put on the article)* | Component  
c. Security Type:   *select the security level of the component or article*
  

### 3.2.3 Entry Types
------

Currently, the main entry types are DI2E Component and Article.  However, other custom 
types can be added, edited, activated, and made inactive here.  

On the Add form data entry can be selected such as whether or not to allow on the 
submission form, and to allow or not allow the following:

Attributes, Relationships, Contacts, Resources, Media, Dependencies, Metadata,
Evaluation Information, Reviews, and Questions.


### 3.2.4 Highlights
-------

A highlight to the home page can be added, deleted, activated, or deactivated deactivated here. 
A highlight code (Article, Component, etc.) can be added or edited as well as a link,
 and the description.


### 3.2.5 Integrations
-------

Component Integration allows for pulling information from an external
system and mapping that back to a component in the application. This
allows for automatic sync of the data source. Currently the application
only supports integration with Jira.

**To setup a Jira Integration:**

1.  Navigate to Integration Management Admin Tool using the admin
    navigation menu.

**Add Mappings:**

1.  Select Jira Project.
2.  Select Issue Type.
3.  Select Attribute in application.
4.  Select Jira field to which to map.
5.  Create mapping by moving them from the available list to
        attribute codes.
6.  Click **Save**.

 **NOTE:** More than one field in JIRA can be mapped to an attribute
        code.

**Add Configuration:**

1.  Create a new Configuration by clicking **Add** on the Component
    Configuration tab.
2.  Select a Component.
3.  Click **Add**
4.  Select JIRA Project (only projects with mappings will be available).
5.  Enter the Jira Issue Number (Eg. ASSET-14)
6.  Click **Save**.

The Component Job will be scheduled when it's saved. The job will run on
an interval based on the global refresh rate. The default is once a day
at 10:00 am server time. The component configuration table will show
when the integration last ran. Also, the scheduled integration job can
be viewed using the Jobs Tool by clicking **Show Integration Jobs**.

The Integration Tool allows for manual control. However, it's not
necessary to run the integration manually as the automated job will
handle that.

If the integration fails for any reason, the configuration table will so
indicate and an error ticket will be created for further analysis. The
integration may fail if the server it's connecting to is unavailable.
This may be a temporary condition. The system will try again on the next
interval. If the configuration job continues to have issues it may
deactivated manually using the tools and then reactivated when the issue
is resolved.

### 3.2.6 Imports
-------

This allows for data imports and mappings.  This is done by importing a ZIP
or JSON file.  Once imported warnings and/ or errors can be viewed, the data 
can be reprocessed, or rolled back.

**NOTE:**  The history is only kept for **180 days** in this section.


### 3.2.7 Lookups
--------

This is used to organize and classify data in a consistent manner.  The codes can
be added, deleted, edited, and made active or inactive.  In addition they can be
imported or exported as .csv files.

Some examples of Lookups are:
Media Types, Data Source, Contact Types, Relationship Types, and Organization Types


### 3.2.8 Organizations
--------

 Organizations provide information on specific organizations that are linked with the components listed on the site. The information these objects contain include:

 - **Description** -            What the organization does
 - **Type** -                   The type of organization (i.e. Commercial Business, Contractor, US Organization, etc.)
 - **Website** -                The website for the specific organization if provided
 - **Address** -                The address for the organization
 - **Contact Information** -    A way to contact the organization

#### 3.2.8.1 Managing *
--------------

**To add a new organization:**

1.  Click **Add New Organization** from the Organizations section of the Admin Tools.

2.  Enter the information and click **Save Changes**.

**Note:** When a new component is added if an unrecognized organization is added, it will automatically be saved to the 
  organization list.  However, details such as the Description and Main Contact Information will need to be added here.

**To edit an organization**

1.  From Organizations in the Admin Tools, select an organization.

2.  Click on the edit icon ![edit](../../../../../../client/openstorefront/app/images/help/edit.png) next to the organization.

    a.  Update Organization information as desired including:  Name, Description, Organization Link, and Organization Type.

    b.  Update Main Contact information as desired including:  Contact Name, Phone Number, Email, Agency, Department, 
         Address, and Security Type.
    
3.  When finished, click **Save Changes**.

**To remove an organization:**

1.  From Organizations in the Admin Tools, select an organization.

2.  Click on the delete icon ![trash](../../../../../../client/openstorefront/app/images/help/trash.png) next to the organization.

3.  Confirm that you wish to delete the organization by cliking **OK**.


**"No Organization" References:**
This displays a list of references where no organization is listed.  

**Run Extract:**
This runs a query to get current component and organization information to update the table.  

**Show Contact info:**
Clicking this button ![Show Contact Info](../../../../../../client/openstorefront/app/images/help/showcontactinfo.png)
 shows the Agency, Department, Contact Name, Contact Phone, and Contact Email for the organization.

**View References:**
Clicking this icon ![View References](../../../../../../client/openstorefront/app/images/help/viewreferences.png)
 shows all of the references for the organization.  

 
### 3.2.9 User Data
---------
Every user in the system has a user profile.  A user profile contains information
about the user and their user specific settings.   The initial creation of the profile
at first login will contain the information gathered from the external security application.

#### 3.2.9.1 Creation *
------

The application doesn't directly manage user-only profiles. When a user
first securely logs in, a profile is created. It's up to the applicable
security utility (Open AM, LDAP, Database…etc.) to define the users.

#### 3.2.9.2 Messaging *
------

Users can message other users using User Profiles. The message will be
emailed to the email address listed in their profile.

**To message a user or a group of users:**

1.  Navigate to the User Profiles Tool.

2.  Click on the message icon ![mail](../../../../../../client/openstorefront/app/images/help/mail.png) next to the user
    (or users) that you want to message.

3.  Fill out the form.

4.  Click **Send** to send the message immediately.

**NOTE:** Messages are sent to email addresses one at a time so no email
addresses are leaked to other users.

#### 3.2.9.3 Control *
------

An admin may edit, activate or inactivate a profile. Inactivating a
profile does not prevent login. Upon login, the user's profile will be
reactivated. Once a profile has been inactivated, that user's watches
and messages are also inactivated. Reactivating the profile will
activate the user's existing watches, but it won't send any previous
messages – just messages going forward.

**To toggle active status:**

1.  Navigate to User Profiles Tool.

2.  Click on the activate/inactivate ![toggle](../../../../../../client/openstorefront/app/images/help/toggle.png) icon and use
    the filter option to view active/inactive users.


## 3.3 Application Management *
--------

### 3.3.1 Alerts *
------

Alerts are triggers set up to watch the data that an administrator can
subscribe to. This allows for "reactive" administration of the
application by setting up alerts when events occur in the system. The
application will monitor the data as it flows in and trigger alerts that
have been setup.

Descriptions of Alert Triggers:

-  **Component Submission** - Alerts on component submissions and cancellations.
-  **System Error**         - Alerts on system error according to trigger options.
-  **User Data**            - Alerts on user data changes according to trigger options.

**To set up an Alert:**

1.  Navigate to the Alerts Tool.

2.  Click **Add**.

3.  Fill in the form and select an Alert Type.

4.  Select any appropriate report options.

5.  Click **Save**.

**To cancel an Alert:**

1.  Navigate to the Alerts Tool.

2.  Click **Inactivate** ![toggle](../../../../../../client/openstorefront/app/images/help/toggle.png) or **Delete**
    ![trash](../../../../../../client/openstorefront/app/images/help/trash.png) to cancel the scheduled report. Click
    **Inactivate** to temporarily pause it.
	

### 3.3.2 Jobs *
------
The Jobs Tool allows for viewing and managing both scheduled background
jobs and background tasks. An Admin can use the tool to pause, start,
and run scheduled jobs. This tool helps in troubleshooting and view of
the status of the application. If an operation is known to potentially
run for an extended period of time, then it will process in the
background allowing for faster feedback to the user while the operation
completes. Scheduled jobs that run periodically such as the notification
job also run in the background to provide automatic handling of
services.

### 3.3.3 Reports * 
------

Descriptions of the Reports:

-  **Component**        - Reports on a component's statistics
-  **Link Validation** - Checks all component links and verifies the status of the links to determine potentially bad links.
-  **Organization**   -  Reports on organization's statistics based on information in user profiles. The report attempts to group users by an organization.
-  **Usage**            -  Reports on the application's usage statistics for a specified time period.
-  **User**             - Reports on user statistics (views, logins, etc.)

A report can be either manually generated on demand or set to
auto-generate according to a specific schedule.

#### 3.3.3.1 Manually Generated Reports *
------

**To manually generate a report:**

1.  Navigate to the Report Tool.

2.  Click **New Report**.

3.  Select Report Type and Format.

4.  Set any options.

5.  Click **Generate**.

The Report is generated in the background and the status of the report
is shown in the table. Once the report is complete, it can be downloaded
by clicking the report title link.

#### 3.3.3.2 Scheduled Reports *
------

**To create a scheduled report:**

1.  Navigate to the Report Tool.

2.  Click **New Report**.

3.  Select Report Type and Format.

4.  Set any options.

5.  Check the **Schedule** box.

6.  Set the interval to run the report and enter an email address.

7.  Click **Generate** to save the scheduled report.

**NOTE:** The report will run initially and then it will run on the next
scheduled interval.

**To cancel a scheduled report:**

1.  Navigate to the Reports Tool.

2.  Select the Scheduled tab.

3.  Click **Inactivate** ![toggle](../../../../../../client/openstorefront/app/images/help/toggle.png) or **Delete**
    ![trash](../../../../../../client/openstorefront/app/images/help/trash.png) to cancel the scheduled report. Click
    **Inactivate** to temporarily pause it.
	
	
### 3.3.4 System
------
System Tools are provided to aid in troubleshooting.

System Tools and their Purpose:

-  **Search Control** -           Allows for re-indexing listings
-  **Recent Changes Email** -     Allows for sending/previewing a recent change email.
-  **Error Tickets** -            View error tickets generated by the system and view their details.
-  **Application Properties** -   Can be used to adjust application run state.
-  **System Configuration** -     Provides a read-only view in the current properties.
-  **Logging** -                  Allows for adjusting log levels at run time to log more/less information from a software component. **NOTE:** Logs are hierarchically organized so set a level from a parent will affect all children who are set to delegate to the parent logger. Loggers under: edu.usu.sdl.openstorefront are application specific. A logger will only appear when the software component is loaded.
-  **Status** -                   Shows the run status of the application (i.e. memory, load, thread status and properties). Refer to Java platform documentation for the interpretation of the information.


### 3.3.5 Tracking

The application tracks internal user logins, Component/Resources views,
and Article views. The application also can be integrated with external
analytics such as google analytics for additional information. Also, the
application server can be configured for access logging and the
application logs audit message for all admin API access.

The primary tracking events can be viewed using the Tracking Admin Tool.
As well as the events are aggregated into various system generated
report accessible via the Reports Admin Tool.

### 3.3.6 Messages
------

Shows a log of messages sent to users in the Inactive filtered table.  Allows for
the cleanup of old messages and the ability to process all queued messages now.


### 3.3.7 API Documentation
------

This contains the documentation for the Application Programming Interface, or API.  It
is viewable in a web page or is printable by clicking on Print View.



# 4.  Glossary
----------

Contains definitions of term using in the application and in the help.  (See the sub-sections.) 

##  4.1  User Terminology
----------------

-  **Article (Topic Landing Page)** -   An article is a central information page that contains information on a specific topic relating to an attribute.
-  **Attribute (Vital)** -             An attribute is a specific category that has been applied to a listing. A listing (component) may have many attributes associated with it.
-  **Component (Listing)** -            A listing in the catalog. A component has the attributes that classify the information being represented by the listing
-  **Highlight** -                      A news item shown on the home page.
-  **Watch** -                          A way to track changes on a listing.

## 4.2 Admin Terminology *
-----------------

-  **Attribute Type** -       A related group of specific categories representing a single attribute of a listing (Eg. "Funded would be a type with the corresponding value "Yes or "No").
-  **Attribute Code** -       A specific value for a type.
-  **Job** -                  A scheduled unit of work.
-  **Task** -                 Background process; used for long-running operations.
-  **Component Metadata** -   Non-filterable Component Vitals. It allows for an admin to put in non-restricted key/value pairs which get added to the vitals.

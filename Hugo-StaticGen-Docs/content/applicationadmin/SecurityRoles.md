+++
title = "Security Roles"
description = ""
weight = 8
date = 2019-02-27T11:30:53-07:00
markup = "mmark"
+++

**Security Roles define what users get what permissions in the application.  Several default roles are set up with default permissions and are listed below.  These can be reset to default or altered as needed.  New roles can also be added.**

## TOC
 **1. [Default Roles and Resetting to Default]({{<relref "#1-default-roles-and-resetting-to-default">}})**

 **2. [Default Permissions (300+)]({{<relref "#2-default-permissions">}})**

 **3. [Overlapping Permissions]({{<relref "#3-overlapping-permissions">}})**

 **4. [Default Data Restrictions]({{<relref "#4-default-data-restrictions">}})**
 
 
## 1. Default Roles and Resetting to Default
When setting up an instance of SPOON it comes with a few default roles as described below and as shown in **Admin Tools &rarr; Application Management &rarr; Security Roles** on the website *(must be logged in as an administrator to view)*

SECURITY ROLE NAME | EXPLANATION | DEFAULT PERMISSIONS NOTES
------------------:|-----------|----------------------------
GUEST-GROUP | Permissions for guest when they come to the site directly via URL, such as to view an entry. | NO Extra Permissions
DEFAULT-GROUP | What all users get and are a member of.  Baseline permissions. | A few, limited Permissions
INTERNAL_REVIEWER_ROLE | This role is for individuals to do a simple content check on submissions. | Workplan Management Permissions
FINAL_REVIEWER_ROLE | This role is for perofrming a final check to see if the content is ready to publish/Approve | Workplan Management Permissions
SME_(*Entry Type Name*) | SME Role for the Corssesponding Entry Type | View, Edit, Comment on Submissions 
SPOON-Support | Permissions consistent with a tech support user. | Administration of User Management tools
SPOON-Evaluators | This group writes evaluations of the entries | Evaluations Permissions
SPOON-Librarian | Data management librarian permissions | Many Administrator Permissions
SPOON-Admin | Website administrator, super permissions group | ALL Permissions

{{% notice note %}}
**If these permisions get changed and you would like to reset them to the default (new installatin) defaults, please do the following:**
1.  Go to **Admin Tools &rarr; Application Management &rarr; System** and click on the **Application State Properties** tab.
2.  Locate the Key **Security-Init_v2_LASTRUN_DTS** and click **Edit**
3.  Delete all of the text in the value box and click **Save**
4.  Repeat the prior two steps for the **Security-Init_v2_STATUS** key.
5.  The pre-made groups shown above (SPOON-Admin, etc.) are now reset to their new installation defaults.
{{% /notice %}}

**[GO TO TOP]({{<relref "#toc">}})**

 
## 2. Default Permissions
The following details the default permissions and what users in these security role groups can do.

<!--   Please SAVE these for copying and pasting in the table below.				        -->
<!--          -->
<!--   **SECTION NAME** | GUEST-GROUP | DEFAULT-GROUP | SPOON-Evaluators | SPOON-Librarian | SPOON-Admin -->


ABILITY OR PERMISSION TO: | GUEST-GROUP | DEFAULT-GROUP | SPOON-Evaluators | SPOON-Librarian | SPOON-Admin
------------------------: | :---------: | :-----------: | :--------------: | :-------------: | :---------:
**API** |
Ability to see API documentation |  |  |  |  | 
**ALERTS** |
Ability to Read all Alerts|  |  |  |  | 
Ability to Create new Alerts|  |  |  |  | 
Ability to Delete Alerts |  |  |  |  | 
Ability to Update Alerts |  |  |  |  | 
Access the admin Alerts Page |  |  |  |  | 
**ATTRIBUTES** | 
Create new Attributes |  |  |  |  | 
Delete existing Atrributes |  |  |  |  | 
Update existing Attributes |  |  |  |  | 
Access to the admin Attribute page |  |  |  |  | 
Manage attributes on entries from the admin tools manageme assignments tool |  |  |  |  | 
Create attribute types |  |  |  |  | 
**BRANDING** |  GUEST-GROUP | DEFAULT-GROUP | SPOON-Evaluators | SPOON-Librarian | SPOON-Admin
Create new Brandings |  |  |  |  | 
Delete Brandings |  |  |  |  | 
Access to the admin Branding page |  |  |  |  | 
Update Brandings |  |  |  |  | 
**CONTACTS** |
Create Contacts |  |  |  |  | 
Delete Contacts |  |  |  |  | 
Update existing Contacts |  |  |  |  | 
Access to the admin Contacts page |  |  |  |  | 
**DASHBOARD** |  GUEST-GROUP | DEFAULT-GROUP | SPOON-Evaluators | SPOON-Librarian | SPOON-Admin
Access to the Dashboard Page |  |  |  |  | 
View the Entry Status Widget |  |  |  |  | 
View the Evaluation Status Widget |  |  |  |  | 
View the Notifications Widget |  |  |  |  | 
View the Outstanding Feedback Widget |  |  |  |  | 
View the Pending Approvals Requests widget |  |  |  |  | 
View the Questions Widget |  |  |  |  | 
View the Reports Widget |  |  |  |  | 
View the Saved Search Widget |  |  |  |  | 
View the Submission Status Widget |  |  |  |  | 
View the System Status Widget |  |  |  |  | 
View the Recent User Data Widget |  |  |  |  | 
View the User Status Widget |  |  |  |  | 
View the Watches Widget |  |  |  |  | 
**ENTRIES** |  GUEST-GROUP | DEFAULT-GROUP | SPOON-Evaluators | SPOON-Librarian | SPOON-Admin
Access to the Admin Entries Page |  |  |  |  | 
Ability to Approve an Entry |   |  |  |  | 
Ability to Read Entries |  |  |  |  | 
Approve an Entry |  |  |  |  | 
Assign a Librarian to a Component (Entry) |  |  |  |  | 
Atrribute Management of Entry |  |  |  |  | 
Change Owner of the Entry |  |  |  |  | 
Manage Change Requests |  |  |  |  | 
Change an Entry Type |  |  |  |  | 
Manage Entry Comments |  |  |  |  | 
Conact Management |  |  |  |  | 
Create Entries |  |  |  |  | 
Delete and Existing Entry |  |  |  |  | 
Manage Evaluation Sections |  |  |  |  | 
Export an Entry |  |  |  |  | 
Media Management |  |  |  |  | 
Merge two Entries Together |  |  |  |  | 
Read Pendig Changes |  |  |  |  | 
Relationship Management for Entries |  |  |  |  | 
Resource Management for Entries |  |  |  |  | 
Tag Management |  |  |  |  | 
Toggle Status (Active/Inactive) of an Entry |  |  |  |  | 
Update Existing Entry |  |  |  |  | 
Delete Entry Version |  |  |  |  | 
Read Entry Version |  |  |  |  | 
ReSPOON Entry Version |  |  |  |  | 
**ENTRY TEMPLATES** |  GUEST-GROUP | DEFAULT-GROUP | SPOON-Evaluators | SPOON-Librarian | SPOON-Admin
Read Entry Templates |  |  |  |  | 
Create Entry Templates |  |  |  |  |  
Read Entry Templates |  |  |  |  | 
Update Entry Templates |  |  |  |  | 
Access to Entry Templates Page |  |  |  |  | 
**ENTRY TYPES** | 
Create Entry Types |  |  |  |  | 
Delete Entry types |  |  |  |  | 
Update Entry Types |  |  |  |  | 
Access Admin Entry Types Page |  |  |  |  | 
**EVALUATION (ADMIN)** |
Access to the Admin Evaluator Management Page |  |  |  |  | 
Activate an Evaluation |  |  |  |  | 
Allow New Sections |  |  |  |  | 
Allow Question Management |  |  |  |  | 
Delete a Comment |  |  |  |  | 
Create an Evaluation |  |  |  |  | 
Delete a Evaluation |  |  |  |  | 
Publsh just the Summary |  |  |  |  | 
Publish or Unplublish an Evaluation |  |  |  |  | 
**EVALUATION (USER)** | GUEST-GROUP | DEFAULT-GROUP | SPOON-Evaluators | SPOON-Librarian | SPOON-Admin
Access to the Evaluator Management Page |  |  |  |  | 
Allow Evaluator to View an Evaluation |  |  |  |  | 
Allow Evaluator to Assign a User to an Evaluation |  |  |  |  | 
Allow Evaluator to Edit an Evaluation |  |  |  |  | 
**EVALUATION TEMPLATES** |
Access to the Evaluation Templates Page |  |  |  |  | 
Read Evaluation Templates |  |  |  |  | 
Create Evaluation Templates |  |  |  |  | 
Delete Evaluation Templates |  |  |  |  | 
Update Evaluation Templates |  |  |  |  | 
**EVALUATION TEMPLATES CHECKLIST QUESTIONS** |
Access to the Evaluation Templates Checklist Questions Page |  |  |  |  | 
Read Eval Template Checklist Question Items  |  |  |  |  | 
Create Template Checklist Questions |  |  |  |  | 
Delete Template Checklist Questions |  |  |  |  | 
Update Template Checklist Questions |  |  |  |  | 
**EVALUATION TEMPLATES CHECKLISTS** | GUEST-GROUP | DEFAULT-GROUP | SPOON-Evaluators | SPOON-Librarian | SPOON-Admin
Access to the Evaluation Templates Checklists Page |  |  |  |  | 
Read Evaluation Template Checklist Items |  |  |  |  | 
Create Evaluation Template Checklist Items |  |  |  |  | 
Delete Evaluation Template Checklist Items |  |  |  |  | 
Update Evaluation Template Checklist Items |  |  |  |  | 
**EVALUATION TEMPLATES SECTIONS** | 
Access to the Evaluation Section Page |  |  |  |  | 
Read Evaluation Template Checklist Items |  |  |  |  | 
Create Evaluation Template Sections |  |  |  |  | 
Delete Evaluation Template Sections |  |  |  |  | 
Update Evaluation Template Sections |  |  |  |  | 
**FAQs** | GUEST-GROUP | DEFAULT-GROUP | SPOON-Evaluators | SPOON-Librarian | SPOON-Admin
Access to the Admin FAQ page |  |  |  |  | 
Read FAQs |  |  |  |  | 
Create FAQs |  |  |  |  | 
Delete FAQs |  |  |  |  | 
Update FAQs |  |  |  |  | 
**FEEDBACK** | 
Access Admin Feedback Page |  |  |  |  | 
Read Feedback |  |  |  |  | 
Remove Feedback |  |  |  |  | 
Modify Feedback |  |  |  |  | 
**HIGHLIGHTS** |
Create Highlights |  |  |  |  | 
Delete Highlights |  |  |  |  | 
Access to the Highlights page |  |  |  |  | 
Update Highlights |  |  |  |  | 
**IMPORT / EXPORT** |
Ability to Import and Export |  |  |  |  | 
Access to the admin Import Export Page |  |  |  |  | 
**INTEGRATIONS** | GUEST-GROUP | DEFAULT-GROUP | SPOON-Evaluators | SPOON-Librarian | SPOON-Admin
Access to the Integration Page |  |  |  |  | 
Read Integration |  |  |  |  | 
Create Integration |  |  |  |  | 
Remove Integration |  |  |  |  | 
Ability to Integrate with external sources |  |  |  |  | 
Run all Integrations |  |  |  |  | 
Run the Integration Configuration |  |  |  |  | 
Allows for Running One Integration |  |  |  |  | 
Toggle Active/Inactive Integration Status |  |  |  |  | 
Update Integration |  |  |  |  | 
**JOBS** | 
Read Jobs |  |  |  |  | 
Perform some action with a Job |  |  |  |  | 
Remove a Job |  |  |  |  | 
Access to Admin Jobs Page |  |  |  |  | 
**LOOKUPS** | GUEST-GROUP | DEFAULT-GROUP | SPOON-Evaluators | SPOON-Librarian | SPOON-Admin
Access the Lookups Page |  |  |  |  | 
Read Lookups |  |  |  |  | 
Create Lookups |  |  |  |  | 
Delete Lookups |  |  |  |  | 
Update a Lookup |  |  |  |  | 
**MEDIA** |
Delete Media |  |  |  |  | 
Access to the admin Media Page |  |  |  |  | 
Upload Media as Admin |  |  |  |  | 
Upload Support Media |  |  |  |  | 
**MESSAGES** |
Read User Notifcation Data |  |  |  |  | 
Post New User Notification Event |  |  |  |  | 
Delete User Notification Event |  |  |  |  | 
Update User Notification Event |  |  |  |  | 
Access to the admin Messages/Notification page |  |  |  |  | 
**NOTIFICATION EVENTS** | 
Post New Admin Notification Event |  |  |  |  | 
Delete Admin Notification Event |   |  |  |  | 
**ORGANIZATIONS** | GUEST-GROUP | DEFAULT-GROUP | SPOON-Evaluators | SPOON-Librarian | SPOON-Admin
Create Organization |   |  |  |  | 
Delete Organization |   |  |  |  | 
Organization Extraction |  |  |  |  | 
Merge Two Organizations |  |  |  |  | 
Acces the Admin Organiation Page |  |  |  |  | a
Update Organization |   |  |  |  | 
**PROFILE MANAGEMENT** |
Read User Profiles |  |  |  |  | 
Delete User Profile |  |  |  |  | 
Update User Profile |  |  |  |  | 
Access to admin User Profiles Page |  |  |  |  | 
Access to User Profile Page |  |  |  |  | 
**QUESTIONS** | GUEST-GROUP | DEFAULT-GROUP | SPOON-Evaluators | SPOON-Librarian | SPOON-Admin
Access admin Questions Page|  |  |  |  | 
Read Questions on Entries |  |  |  |  | 
Delete Questions on Entries |  |  |  |  | 
Update Questions on Entries |  |  |  |  | 
Access to the User Questions Page |  |  |  |  | 
**RELATIONSHIPS** |
Access to the admin Relationships Page |  |  |  |  | 
Access to the User Relationships Page |  |  |  |  | 
**REPORTS** |  GUEST-GROUP | DEFAULT-GROUP | SPOON-Evaluators | SPOON-Librarian | SPOON-Admin
General Reports Read |  |  |  |  | 
User Run Report as Attached Email |  |  |  |  | 
User Run Report as Content of Email body |  |  |  |  | 
View Reports From All Users |  |  |  |  | 
Create New Report |  |  |  |  | 
Delete a Report |  |  |  |  | 
Access to the Reports Page |  |  |  |  | 
Run the Action Report |  |   |  |  | 
Run Entries by Category Report |  |  |  |  | 
Run Entries by Organization Report |   |  |  |  | 
Run Entry Detail Report |   |  |  |  | 
Run Entry Listing Report |  |  |  |  | 
Run the Entry Report |  |  |  |  | 
Run the Entry Status Report |  |  |  |  | 
Run the Evaluation Status Report |  |  |  |  | 
Run the Link Validation Report |  |  |  |  | 
Run the Sumissions Report |  |  |  |  | 
Run the Useage Report |  |  |  |  | 
Run the User by Organiation Repeat |  |  |  |  | 
Run the User Report |  |  |  |  | 
Run the Workplan Status Report |  |  |  |  | 
**REPORTS SCHEDULE** | GUEST-GROUP | DEFAULT-GROUP | SPOON-Evaluators | SPOON-Librarian | SPOON-Admin
Create Scheduled Reports |  |  |  |  | 
Delete Scheduled Reports |  |  |  |  | 
Read Scheduled Reports |  |  |  |  | 
Edit Scheduled Reports |  |  |  |  | 
**REVIEWS** |
Access to the Admin Reviews Page |  |  |  |  | 
Read Reviews |   |  |  |  | 
Delete Reviews |   |  |  |  | 
Update Reviews |  |  |  |  | 
Access to the Reviews Page |   |  |  |  | 
**ROLES** |
Ability to Read Security Roles |  |  |  |  | 
Create Security Roles |  |  |  |  | 
Delete Security Roles |  |  |  |  | 
Update Security Roles |  |  |  |  | 
Access to Admin Security Roles Page |  |  |  |  | 
**SEARCH MANAGEMENT** | GUEST-GROUP | DEFAULT-GROUP | SPOON-Evaluators | SPOON-Librarian | SPOON-Admin
Read all Searches |   |  |  |  | 
Create a Search |   |  |  |  | 
Delete a Search |   |  |  |  | 
Update a Search |   |  |  |  | 
Search Page (User) |  |  |  |  | 
**SECURITY** | 
Access to the Admin Security Page |  |  |  |  | 
Access to the Security Policy Tab |  |  |  |  | 
Access to the Shiro Config Tab |  |  |  |  | 
**SUBMISSIONS (ADMIN)** | GUEST-GROUP | DEFAULT-GROUP | SPOON-Evaluators | SPOON-Librarian | SPOON-Admin
Access to Partial Submissions Page |  |  |  |  | 
Access to Admin Custom Submission Form Template Page |  |  |  |  | 
Read Submission Form Templates |  |  |  |  | 
Create Submission Form Templates |  |  |  |  | 
Delete Submission Form Templates |  |  |  |  | 
Update Submission Form Templates |  |  |  |  | 
Read User Sumissions |  |  |  |  | 
Delete User Submissions |  |  |  |  | 
Update User Submissions |  |  |  |  | 
**SUBMISSIONS (USER)** | 
Access to the User Submissions Page |  |  |  |  | 
Read User Submissions |  |  |  |  | 
Create Change Requests |  |  |  |  | 
Create User Submissions |  |  |  |  | 
Delete User Submissions |  |  |  |  | 
Update User Submissions |  |  |  |  | 
**SYSTEM** | GUEST-GROUP | DEFAULT-GROUP | SPOON-Evaluators | SPOON-Librarian | SPOON-Admin
Access to the Admin System Archives Page |  |  |  |  | 
General System Administration Permission |  |  |  |  | 
Update Applicaiton Properties |  |  |  |  | 
Create System Archives |  |  |  |  | 
Delete System Archives |  |  |  |  | 
Manage System Archives |  |  |  |  | 
Access to System Cache |  |  |  |  | 
Remove/ Clear Operations for the System |  |  |  |  | 
Read Application Meta Data |  |  |  |  | 
Update System Configuration |  |  |  |  | 
Error Ticket Mangaement |  |  |  |  | 
Permission Logging |  |  |  |  | 
System Managers |  |  |  |  | 
Management of Plugins |  |  |  | {{<icon name="fa-close"color="red">}} | 
System Recent Changes |  |  |  |  | 
System Search Management |  |  |  |  | 
System Stand By |  |  |  |  | 
System Status |  |  |  |  | 
Access the Admin System Page |  |  |  |  | 
**TAGS** | GUEST-GROUP | DEFAULT-GROUP | SPOON-Evaluators | SPOON-Librarian | SPOON-Admin
Access to the Admin Tags Page |  |  |  |  | 
**TRACKING** | 
Access to the Admin Tracking Page |  |  |  |  | 
Read Tracking |  |  |  |  | 
Delete Tracking from an Item |  |  |  |  | 
Update Tracking on an Item |  |  |  |  | 
**USER MANAGEMENT** | 
Access to the Admin User Management Page |  |  |  |  | 
Read other User's Submissions |  |  |  |  | 
Approve other User's Submissions |  |  |  |  | 
Create Submissions for Users |  |  |  |  | 
Delete other User's Submissions |  |  |  |  | 
Update other User's Submissions |  |  |  |  | 
**WATCHES** | 
Access to Admin Watches Page |  |  |  |  | 
Read Watches |  |  |  |  | 
Delete Watches |  |  |  |  | 
Update WAtches |  |  |  |  | 
Access to the User Watches Page |  |  |  |  | 
**WORKPLAN** | GUEST-GROUP | DEFAULT-GROUP | SPOON-Evaluators | SPOON-Librarian | SPOON-Admin 
Create Workplans |  |  |  |  | 
Delete Workplans |  |  |  |  | 
Access to the Workplans Page |  |  |  |  | 
Read Workplans as Administrator |  |  |  |  | 
Update Workplans |  |  |  |  | 
User can view current status of selected record in a workplan |  |  |  |  | 
**WORKPLAN PROGRESS MANAGEMENT** | 
Get Submission Comments for Role |  |  |  |  | 
Enable Assigning and Unassigning to Admin |  |  |  |  | 
Assign Any Entry to Any User |  |  |  |  | 
Enable Updating of Workflows |  |  |  |  | 
View Your Group Workflow Links |  |  |  |  | 
View ALL Workflow Links |  |  |  |  | 
Update Workflows |  |  |  |  | 
View Workplan Progress Management Page |  |  |  |  | 

**[GO TO TOP]({{<relref "#toc">}})**


## 3. Overlapping Permissions

{{% notice info %}}
**What happens if a user is a part of more than *one* Security Role?  If a user is part of multiple security roles, such as an Evaluator AND a Librarian (and the Default Group as all users are) then if a particular permission is granted to one group, it is grandted (ANDed) to the user.  For example:**

 * If user "bobsmith" has the Security Roles of Evaluator AND Librarian he will see,
 * BOTH the Evaluation Status Widget AND the Notifications Widget on the User Dashboard
 * If the user chooses to add it to his/her dashboard.  Both with be available to add.

Note the permissions table below:
{{% /notice %}}

ABILITY OR PERMISSION TO: |  DEFAULT-GROUP | SPOON-Evaluators | SPOON-Librarian | Bob Smith's (ANDed) Access
------------------------: | :------------: | :-------------------: | :------------------: | :----------------: 
View the System Status Widget |  |  |  | 
View the Evaluation Status Widget |  |  |  | 
View the Questions Widget |  |  |  | 
Allow Evaluator to Edit an Evaluation |  |  |  | 

**[GO TO TOP]({{<relref "#toc">}})**


## 4. Default Data Restrictions
{{% notice warning %}}
**To use these you MUST mark each individual entry or component in the database with the appropriate markings.  This admin area only serves to mark which Security Roles have access to which distributions.**
{{% /notice %}}

Data restrictions, if used, restrict users to seeing certain entries.  These are all turned **OFF** (disabled) by default.

#### Data Sources
ENABLED|CODE|DESCRIPTION
:-----:|:--:|:---------:
 | AEROSPACE_IMPORT | Aerospace Import Entries 
 | DI2E | DI2E Component Entry
 | ER2 | ER2 Asset

#### Data Sensitivity
ENABLED|CODE|DESCRIPTION
:-----:|:--:|:---------:
 |  DISTRO A | Government Distribution A
 |  DISTRO B | Government Distribution B
 |  DISTRO C | Government Distribution C
 |  DISTRO D | Government Distribution D
 |  DISTRO E | Government Distribution E
 |  DISTRO F | Government Distribution F
 |  ITAR | Internation Traffic in Arms Regulations
 |  PUBLIC | Available to the Public
 |  SENSITIVE | Sensitive Information Marking


**[GO TO TOP]({{<relref "#toc">}})**

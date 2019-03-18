+++
title = "Security Roles"
description = "Security Roles"
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
-----------------: | ----------- | -------------------------
GUEST-GROUP | Permissions for guest when they come to the site directly via URL, such as to view an entry. | NO Extra Permissions
DEFAULT-GROUP | What all users get and are a member of.  Baseline permissions. | A few, limited Permissions
SME_Approver | SME Role for Approving Entries | See dashboard pending requests.  Approve Requests for new entries and change requests.
STOREFRONT-Librarian | Data management librarian permissions | Many Administrator Permissions
STOREFRONT-Admin | Website administrator, super permissions group | ALL Permissions

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

ABILITY OR PERMISSION TO: | GUEST-GROUP | DEFAULT-GROUP | SME-Approver | STOREFRONT-Librarian | STOREFRONT-Admin 
------------------------: | :---------: | :-----------: | :----------: | :------------------: | :--------------: | 
**API** |
Ability to see API documentation | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}}
**ALERTS** | 
Ability to Read all Alerts | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}}
Ability to Create new Alerts | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}}
Ability to Delete Alerts  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}}
Ability to Update Alerts  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}}
Access the admin Alerts Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}}
**ATTRIBUTES** | 
Create new Attributes  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}}
Delete existing Atrributes  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}}
Update existing Attributes  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}}
Access to the admin Attribute page  | {{<x>}} | {{<c>}} | {{<x>}} | {{<x>}} | {{<c>}}
Manage attributes on entries from the admin tools manageme assignments tool  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}}
Create attribute types  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}}
**BRANDING** |  GUEST-GROUP | DEFAULT-GROUP | SME-Approver | STOREFRONT-Librarian | STOREFRONT-Admin
Create new Brandings  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Delete Brandings  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Access to the admin Branding page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Update Brandings  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
**CONTACTS** |
Create Contacts  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Delete Contacts  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Update existing Contacts  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Access to the admin Contacts page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
**DASHBOARD** |  GUEST-GROUP | DEFAULT-GROUP | SME-Approver | STOREFRONT-Librarian | STOREFRONT-Admin 
Access to the Dashboard Page  | {{<x>}} | {{<c>}} | {{<c>}} | {{<x>}} | {{<c>}} 
View the Entry Status Widget  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
View the Evaluation Status Widget  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
View the Notifications Widget  | {{<x>}} | {{<c>}} | {{<x>}} | {{<x>}} | {{<c>}} 
View the Outstanding Feedback Widget  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
View the Pending Approvals Requests widget  | {{<x>}} | {{<x>}} | {{<c>}} | {{<x>}} | {{<c>}} 
View the Questions Widget  | {{<x>}} | {{<c>}} | {{<x>}} | {{<x>}} | {{<c>}} 
View the Reports Widget  | {{<x>}} | {{<c>}} | {{<x>}} | {{<x>}} | {{<c>}} 
View the Saved Search Widget  | {{<x>}} | {{<c>}} | {{<x>}} | {{<x>}} | {{<c>}} 
View the Submission Status Widget  | {{<x>}} | {{<c>}} | {{<x>}} | {{<x>}} | {{<c>}} 
View the System Status Widget  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
View the Recent User Data Widget  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
View the User Status Widget  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
View the Watches Widget  | {{<x>}} | {{<c>}} | {{<x>}} | {{<x>}} | {{<c>}} 
**ENTRIES** |  GUEST-GROUP | DEFAULT-GROUP | SME-Approver | STOREFRONT-Librarian | STOREFRONT-Admin 
Access to the Admin Entries Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Ability to Approve an Entry | {{<x>}} | {{<x>}} | {{<c>}} | {{<x>}} | {{<c>}} 
Ability to Read Entries  | {{<x>}} | {{<x>}} | {{<c>}} | {{<x>}} | {{<c>}} 
Approve an Entry  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Assign a Librarian to a Component (Entry)  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Atrribute Management of Entry  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Change Owner of the Entry  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Manage Change Requests  | {{<x>}} | {{<x>}} | {{<c>}} | {{<x>}} | {{<c>}} 
Change an Entry Type  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Manage Entry Comments  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Conact Management  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Create Entries  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Delete and Existing Entry  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Manage Evaluation Sections  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Export an Entry  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Media Management  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Merge two Entries Together  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Read Pendig Changes  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Relationship Management for Entries  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Resource Management for Entries  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Tag Management  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Toggle Status (Active/Inactive) of an Entry  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Update Existing Entry  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Delete Entry Version  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Read Entry Version  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
ReSPOON Entry Version  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
**ENTRY TEMPLATES** |  GUEST-GROUP | DEFAULT-GROUP | SME-Approver | STOREFRONT-Librarian | STOREFRONT-Admin  
Read Entry Templates  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Create Entry Templates  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}}  
Read Entry Templates  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Update Entry Templates  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Access to Entry Templates Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
**ENTRY TYPES** | 
Create Entry Types  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Delete Entry types  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Update Entry Types  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Access Admin Entry Types Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
**EVALUATION (ADMIN)** |
Access to the Admin Evaluator Management Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Activate an Evaluation  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Allow New Sections  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Allow Question Management  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Delete a Comment  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Create an Evaluation  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Delete a Evaluation  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Publsh just the Summary  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Publish or Unplublish an Evaluation  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
**EVALUATION (USER)** | GUEST-GROUP | DEFAULT-GROUP | SME-Approver | STOREFRONT-Librarian | STOREFRONT-Admin 
Access to the Evaluator Management Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Allow Evaluator to View an Evaluation  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Allow Evaluator to Assign a User to an Evaluation  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Allow Evaluator to Edit an Evaluation  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
**EVALUATION TEMPLATES** |
Access to the Evaluation Templates Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Read Evaluation Templates  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Create Evaluation Templates  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Delete Evaluation Templates  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Update Evaluation Templates  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
**EVALUATION TEMPLATES CHECKLIST QUESTIONS** |
Access to the Evaluation Templates Checklist Questions Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Read Eval Template Checklist Question Items   | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Create Template Checklist Questions  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Delete Template Checklist Questions  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Update Template Checklist Questions  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
**EVALUATION TEMPLATES CHECKLISTS** | GUEST-GROUP | DEFAULT-GROUP | SME-Approver | STOREFRONT-Librarian | STOREFRONT-Admin  
Access to the Evaluation Templates Checklists Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Read Evaluation Template Checklist Items  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Create Evaluation Template Checklist Items  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Delete Evaluation Template Checklist Items  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Update Evaluation Template Checklist Items  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
**EVALUATION TEMPLATES SECTIONS** | 
Access to the Evaluation Section Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Read Evaluation Template Checklist Items  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Create Evaluation Template Sections  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Delete Evaluation Template Sections  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Update Evaluation Template Sections  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
**FAQs** | GUEST-GROUP | DEFAULT-GROUP | SME-Approver | STOREFRONT-Librarian | STOREFRONT-Admin 
Access to the Admin FAQ page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Read FAQs  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Create FAQs  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Delete FAQs  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Update FAQs  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
**FEEDBACK** | 
Access Admin Feedback Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Read Feedback  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Remove Feedback  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Modify Feedback  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
**HIGHLIGHTS** |
Create Highlights  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Delete Highlights  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Access to the Highlights page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Update Highlights  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
**IMPORT / EXPORT** |
Ability to Import and Export  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Access to the admin Import Export Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
**INTEGRATIONS** | GUEST-GROUP | DEFAULT-GROUP | SME-Approver | STOREFRONT-Librarian | STOREFRONT-Admin 
Access to the Integration Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Read Integration  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Create Integration  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Remove Integration  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Ability to Integrate with external sources  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Run all Integrations  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Run the Integration Configuration  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Allows for Running One Integration  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Toggle Active/Inactive Integration Status  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Update Integration  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
**JOBS** | 
Read Jobs  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Perform some action with a Job  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Remove a Job  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Access to Admin Jobs Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
**LOOKUPS** | GUEST-GROUP | DEFAULT-GROUP | SME-Approver | STOREFRONT-Librarian | STOREFRONT-Admin 
Access the Lookups Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Read Lookups  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Create Lookups  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Delete Lookups  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Update a Lookup  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
**MEDIA** |
Delete Media  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Access to the admin Media Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Upload Media as Admin  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Upload Support Media  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
**MESSAGES** |
Read User Notifcation Data  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Post New User Notification Event  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Delete User Notification Event  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Update User Notification Event  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Access to the admin Messages/Notification page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
**NOTIFICATION EVENTS** | 
Post New Admin Notification Event  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Delete Admin Notification Event |      
**ORGANIZATIONS** | GUEST-GROUP | DEFAULT-GROUP | SME-Approver | STOREFRONT-Librarian | STOREFRONT-Admin 
Create Organization  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Delete Organization  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Organization Extraction  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Merge Two Organizations  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Acces the Admin Organiation Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Update Organization  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
**PROFILE MANAGEMENT** |
Read User Profiles  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Delete User Profile  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Update User Profile  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Access to admin User Profiles Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Access to User Profile Page  | {{<x>}} | {{<c>}} | {{<x>}} | {{<x>}} | {{<c>}} 
**QUESTIONS** | GUEST-GROUP | DEFAULT-GROUP | SME-Approver | STOREFRONT-Librarian | STOREFRONT-Admin  
Access admin Questions Page    | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Read Questions on Entries  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Delete Questions on Entries  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Update Questions on Entries  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Access to the User Questions Page  | {{<x>}} | {{<c>}} | {{<x>}} | {{<x>}} | {{<c>}} 
**RELATIONSHIPS** |
Access to the admin Relationships Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Access to the User Relationships Page  | {{<x>}} | {{<c>}} | {{<x>}} | {{<x>}} | {{<c>}} 
**REPORTS** |  GUEST-GROUP | DEFAULT-GROUP | SME-Approver | STOREFRONT-Librarian | STOREFRONT-Admin  
General Reports Read  | {{<x>}} | {{<c>}} | {{<x>}} | {{<x>}} | {{<c>}} 
User Run Report as Attached Email  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
User Run Report as Content of Email body  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
View Reports From All Users  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Create New Report  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Delete a Report  | {{<x>}} | {{<c>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Access to the Reports Page  | {{<x>}} | {{<c>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Run the Action Report   | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Run Entries by Category Report  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Run Entries by Organization Report | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Run Entry Detail Report | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Run Entry Listing Report  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Run the Entry Report  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Run the Entry Status Report  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Run the Evaluation Status Report  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Run the Link Validation Report  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Run the Sumissions Report  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Run the Useage Report  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Run the User by Organiation Repeat  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Run the User Report  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Run the Workplan Status Report  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
**REPORTS SCHEDULE** | GUEST-GROUP | DEFAULT-GROUP | SME-Approver | STOREFRONT-Librarian | STOREFRONT-Admin 
Create Scheduled Reports  | {{<x>}} | {{<c>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Delete Scheduled Reports  | {{<x>}} | {{<c>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Read Scheduled Reports  | {{<x>}} | {{<c>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Edit Scheduled Reports  | {{<x>}} | {{<c>}} | {{<x>}} | {{<x>}} | {{<c>}} 
**REVIEWS** |
Access to the Admin Reviews Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Read Reviews  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}}      
Delete Reviews  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}}       
Update Reviews  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Access to the Reviews Page  | {{<x>}} | {{<c>}} | {{<x>}} | {{<x>}} | {{<c>}}      
**ROLES** |
Ability to Read Security Roles  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Create Security Roles  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Delete Security Roles  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Update Security Roles  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Access to Admin Security Roles Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
**SEARCH MANAGEMENT** | GUEST-GROUP | DEFAULT-GROUP | SME-Approver | STOREFRONT-Librarian | STOREFRONT-Admin 
Read all Searches  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}}      
Create a Search  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Delete a Search  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Update a Search  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Search Page (User)  | {{<x>}} | {{<c>}} | {{<x>}} | {{<x>}} | {{<c>}} 
**SECURITY** | 
Access to the Admin Security Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Access to the Security Policy Tab  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Access to the Shiro Config Tab  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
**SUBMISSIONS (ADMIN)** | GUEST-GROUP | DEFAULT-GROUP | SME-Approver | STOREFRONT-Librarian | STOREFRONT-Admin 
Access to Partial Submissions Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Access to Admin Custom Submission Form Template Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Read Submission Form Templates  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Create Submission Form Templates  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Delete Submission Form Templates  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Update Submission Form Templates  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Read User Sumissions  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Delete User Submissions  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Update User Submissions  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
**SUBMISSIONS (USER)** | 
Access to the User Submissions Page  | {{<x>}} | {{<c>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Read User Submissions  | {{<x>}} | {{<c>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Create Change Requests  | {{<x>}} | {{<c>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Create User Submissions  | {{<x>}} | {{<c>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Delete User Submissions  | {{<x>}} | {{<c>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Update User Submissions  | {{<x>}} | {{<c>}} | {{<x>}} | {{<x>}} | {{<c>}} 
**SYSTEM** | GUEST-GROUP | DEFAULT-GROUP | SME-Approver | STOREFRONT-Librarian | STOREFRONT-Admin 
Access to the Admin System Archives Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
General System Administration Permission  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Update Applicaiton Properties  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Create System Archives  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Delete System Archives  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Manage System Archives  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Access to System Cache  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Remove/ Clear Operations for the System  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Read Application Meta Data  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Update System Configuration  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Error Ticket Mangaement  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Permission Logging  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
System Managers  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Management of Plugins   | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}}   
System Recent Changes  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
System Search Management  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
System Stand By  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
System Status  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Access the Admin System Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
**TAGS** | GUEST-GROUP | DEFAULT-GROUP | SME-Approver | STOREFRONT-Librarian | STOREFRONT-Admin 
Access to the Admin Tags Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
**TRACKING** | 
Access to the Admin Tracking Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Read Tracking  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Delete Tracking from an Item  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Update Tracking on an Item  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
**USER MANAGEMENT** | 
Access to the Admin User Management Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Read other User's Submissions  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Approve other User's Submissions  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Create Submissions for Users  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Delete other User's Submissions  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
Update other User's Submissions  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} 
**WATCHES** | 
Access to Admin Watches Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Read Watches  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Delete Watches  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Update WAtches  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Access to the User Watches Page  | {{<x>}} | {{<c>}} | {{<x>}} | {{<x>}} | {{<c>}} 
**WORKPLAN** | GUEST-GROUP | DEFAULT-GROUP | SME-Approver | STOREFRONT-Librarian | STOREFRONT-Admin 
Create Workplans  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Delete Workplans  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Access to the Workplans Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Read Workplans as Administrator  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Update Workplans  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
User can view current status of selected record in a workplan  | {{<x>}} | {{<c>}} | {{<x>}} | {{<x>}} | {{<c>}} 
**WORKPLAN PROGRESS MANAGEMENT** | 
Get Submission Comments for Role  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Enable Assigning and Unassigning to Admin  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Assign Any Entry to Any User  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Enable Updating of Workflows  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
View Your Group Workflow Links  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
View ALL Workflow Links  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
Update Workflows  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 
View Workplan Progress Management Page  | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}} | {{<c>}} 

**[GO TO TOP]({{<relref "#toc">}})**


## 3. Overlapping Permissions

{{% notice info %}}
**What happens if a user is a part of more than *one* Security Role?  If a user is part of multiple security roles, such as a SME_Approver and STOREFRONT-Librarian (and the Default Group as all users are) then if a particular permission is granted to one group, it is grandted (ANDed) to the user.  For example:**

 * If user "bobsmith" has the Security Roles of SME_Approver **and** STOREFRONT-Librarian he will see,
 * BOTH the 
 * asdf


Note the permissions table below:
{{% /notice %}}

ABILITY OR PERMISSION TO: | DEFAULT-GROUP | SME_Approver | SPOON-Librarian | Bob Smith's (ANDed) Access
------------------------: | :-----------: | :----------: | :-------------: | :----------------: 
|
|
|
|

**[GO TO TOP]({{<relref "#toc">}})**


## 4. Default Data Restrictions
{{% notice warning %}}
**To use these you MUST mark each individual entry or component in the database with the appropriate markings.  This admin area only serves to mark which Security Roles have access to which distributions.**
{{% /notice %}}

Data restrictions, if used, restrict users to seeing certain entries.  These are all turned **OFF** (disabled) by default.

#### Data Sources
ENABLED|CODE|DESCRIPTION
:-----:|:--:|:---------:
 {{<x>}} | AEROSPACE_IMPORT | Aerospace Import Entries 
 {{<x>}} | DI2E | DI2E Component Entry
 {{<x>}} | ER2 | ER2 Asset

#### Data Sensitivity
ENABLED|CODE|DESCRIPTION
:-----:|:--:|:---------:
{{<x>}} |  DISTRO A | Government Distribution A
{{<x>}} |  DISTRO B | Government Distribution B
{{<x>}}  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}}  DISTRO C | Government Distribution C
{{<x>}}  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}}  DISTRO D | Government Distribution D
{{<x>}}  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}}  DISTRO E | Government Distribution E
{{<x>}} |  DISTRO F | Government Distribution F
{{<x>}}  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}}  ITAR | Internation Traffic in Arms Regulations
{{<x>}}  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}}  PUBLIC | Available to the Public
{{<x>}}  | {{<x>}} | {{<x>}} | {{<x>}} | {{<x>}} | {{<c>}}  SENSITIVE | Sensitive Information Marking


**[GO TO TOP]({{<relref "#toc">}})**
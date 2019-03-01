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
When setting up an instance of Storefront it comes with a few default roles as described below and as shown in **Admin Tools &rarr; Application Management &rarr; Security Roles** on the website *(must be logged in as an administrator to view)*

SECURITY ROLE NAME | EXPLANATION | DEFAULT PERMISSIONS NOTES
------------------:|-----------|----------------------------
GUEST-GROUP | Permissions for guest when they come to the site directly via URL, such as to view an entry. | NO Extra Permissions
DEFAULT-GROUP | What all users get and are a member of.  Baseline permissions. | A few, limited Permissions
STOREFRONT-Evaluators | This group writes evaluations of the entries | Evaluations Permissions
STOREFRONT-Librarian | Data management librarian permissions | Many Administrator Permissions
STOREFRONT-Admin | Website administrator, super permissions group | ALL Permissions

{{% notice note %}}
**If these permisions get changed and you would like to reset them to the default (new installatin) defaults, please do the following:**
1.  Go to **Admin Tools &rarr; Application Management &rarr; System** and click on the **Application State Properties** tab.
2.  Locate the Key **Security-Init_v2_LASTRUN_DTS** and click **Edit**
3.  Delete all of the text in the value box and click **Save**
4.  Repeat the prior two steps for the **Security-Init_v2_STATUS** key.
5.  The pre-made groups shown above (STOREFRONT-Admin, etc.) are now reset to their new installation defaults.
{{% /notice %}}

**[GO TO TOP]({{<relref "#toc">}})**

 
## 2. Default Permissions
The following details the default permissions and what users in these security role groups can do.

<!--   Please SAVE these for copying and pasting in the table below.				        -->
<!--   {{<icon name="fa-check" color="green">}}  {{<icon name="fa-close" color="red">}}     -->
<!--   **SECTION NAME** | GUEST-GROUP | DEFAULT-GROUP | STOREFRONT-Evaluators | STOREFRONT-Librarian | STOREFRONT-Admin -->


ABILITY OR PERMISSION TO: | GUEST-GROUP | DEFAULT-GROUP | STOREFRONT-Evaluators | STOREFRONT-Librarian | STOREFRONT-Admin
------------------------: | :---------: | :-----------: | :------------------: | :-------------------: | :--------------:
**API** |
Ability to see API documentation | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
**ALERTS** |
Ability to Read all Alerts| {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Ability to Create new Alerts| {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Ability to Delete Alerts | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Ability to Update Alerts | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Access the admin Alerts Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
**ATTRIBUTES** | 
Create new Attributes | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Delete existing Atrributes | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Update existing Attributes | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Access to the admin Attribute page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Manage attributes on entries from the admin tools manageme assignments tool | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Create attribute types | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
**BRANDING** |  GUEST-GROUP | DEFAULT-GROUP | STOREFRONT-Evaluators | STOREFRONT-Librarian | STOREFRONT-Admin
Create new Brandings | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Delete Brandings | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Access to the admin Branding page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Update Brandings | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
**CONTACTS** |
Create Contacts | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Delete Contacts | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Update existing Contacts | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Access to the admin Contacts page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
**DASHBOARD** |  GUEST-GROUP | DEFAULT-GROUP | STOREFRONT-Evaluators | STOREFRONT-Librarian | STOREFRONT-Admin
Access to the Dashboard Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
View the Entry Status Widget | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
View the Evaluation Status Widget | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
View the Notifications Widget | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
View the Outstanding Feedback Widget | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
View the Pending Approvals Requests widget | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
View the Questions Widget | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
View the Reports Widget | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
View the Saved Search Widget | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
View the Submission Status Widget | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
View the System Status Widget | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
View the Recent User Data Widget | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
View the User Status Widget | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
View the Watches Widget | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
**ENTRIES** |  GUEST-GROUP | DEFAULT-GROUP | STOREFRONT-Evaluators | STOREFRONT-Librarian | STOREFRONT-Admin
Access to the Admin Entries Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Ability to Approve an Entry |  {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Ability to Read Entries | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Approve an Entry | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Assign a Librarian to a Component (Entry) | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Atrribute Management of Entry | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Change Owner of the Entry | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Manage Change Requests | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Change an Entry Type | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Manage Entry Comments | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Conact Management | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Create Entries | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Delete and Existing Entry | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Manage Evaluation Sections | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Export an Entry | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Media Management | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Merge two Entries Together | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Read Pendig Changes | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Relationship Management for Entries | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Resource Management for Entries | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Tag Management | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Toggle Status (Active/Inactive) of an Entry | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Update Existing Entry | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Delete Entry Version | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Read Entry Version | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Restore Entry Version | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
**ENTRY TEMPLATES** |  GUEST-GROUP | DEFAULT-GROUP | STOREFRONT-Evaluators | STOREFRONT-Librarian | STOREFRONT-Admin
Read Entry Templates | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Create Entry Templates | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}} 
Read Entry Templates | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Update Entry Templates | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Access to Entry Templates Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
**ENTRY TYPES** | 
Create Entry Types | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Delete Entry types | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Update Entry Types | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Access Admin Entry Types Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
**EVALUATION (ADMIN)** |
Access to the Admin Evaluator Management Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Activate an Evaluation | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Allow New Sections | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Allow Question Management | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Delete a Comment | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Create an Evaluation | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Delete a Evaluation | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Publsh just the Summary | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Publish or Unplublish an Evaluation | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
**EVALUATION (USER)** | GUEST-GROUP | DEFAULT-GROUP | STOREFRONT-Evaluators | STOREFRONT-Librarian | STOREFRONT-Admin
Access to the Evaluator Management Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Allow Evaluator to View an Evaluation | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Allow Evaluator to Assign a User to an Evaluation | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Allow Evaluator to Edit an Evaluation | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
**EVALUATION TEMPLATES** |
Access to the Evaluation Templates Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Read Evaluation Templates | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Create Evaluation Templates | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Delete Evaluation Templates | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Update Evaluation Templates | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
**EVALUATION TEMPLATES CHECKLIST QUESTIONS** |
Access to the Evaluation Templates Checklist Questions Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Read Eval Template Checklist Question Items  | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Create Template Checklist Questions | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Delete Template Checklist Questions | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Update Template Checklist Questions | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
**EVALUATION TEMPLATES CHECKLISTS** | GUEST-GROUP | DEFAULT-GROUP | STOREFRONT-Evaluators | STOREFRONT-Librarian | STOREFRONT-Admin
Access to the Evaluation Templates Checklists Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Read Evaluation Template Checklist Items | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Create Evaluation Template Checklist Items | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Delete Evaluation Template Checklist Items | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Update Evaluation Template Checklist Items | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
**EVALUATION TEMPLATES SECTIONS** | 
Access to the Evaluation Section Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Read Evaluation Template Checklist Items | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Create Evaluation Template Sections | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Delete Evaluation Template Sections | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Update Evaluation Template Sections | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
**FAQs** | GUEST-GROUP | DEFAULT-GROUP | STOREFRONT-Evaluators | STOREFRONT-Librarian | STOREFRONT-Admin
Access to the Admin FAQ page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Read FAQs | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Create FAQs | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Delete FAQs | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Update FAQs | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
**FEEDBACK** | 
Access Admin Feedback Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Read Feedback | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Remove Feedback | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Modify Feedback | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
**HIGHLIGHTS** |
Create Highlights | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Delete Highlights | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Access to the Highlights page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Update Highlights | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
**IMPORT / EXPORT** |
Ability to Import and Export | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Access to the admin Import Export Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
**INTEGRATIONS** | GUEST-GROUP | DEFAULT-GROUP | STOREFRONT-Evaluators | STOREFRONT-Librarian | STOREFRONT-Admin
Access to the Integration Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Read Integration | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Create Integration | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Remove Integration | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Ability to Integrate with external sources | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Run all Integrations | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Run the Integration Configuration | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Allows for Running One Integration | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Toggle Active/Inactive Integration Status | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Update Integration | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
**JOBS** | 
Read Jobs | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Perform some action with a Job | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Remove a Job | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Access to Admin Jobs Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
**LOOKUPS** | GUEST-GROUP | DEFAULT-GROUP | STOREFRONT-Evaluators | STOREFRONT-Librarian | STOREFRONT-Admin
Access the Lookups Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Read Lookups | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Create Lookups | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Delete Lookups | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Update a Lookup | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
**MEDIA** |
Delete Media | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Access to the admin Media Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Upload Media as Admin | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Upload Support Media | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
**MESSAGES** |
Read User Notifcation Data | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Post New User Notification Event | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Delete User Notification Event | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Update User Notification Event | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Access to the admin Messages/Notification page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
**NOTIFICATION EVENTS** | 
Post New Admin Notification Event | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Delete Admin Notification Event |  {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
**ORGANIZATIONS** | GUEST-GROUP | DEFAULT-GROUP | STOREFRONT-Evaluators | STOREFRONT-Librarian | STOREFRONT-Admin
Create Organization |  {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Delete Organization |  {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Organization Extraction | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Merge Two Organizations | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Acces the Admin Organiation Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}a
Update Organization |  {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
**PROFILE MANAGEMENT** |
Read User Profiles | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Delete User Profile | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Update User Profile | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Access to admin User Profiles Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Access to User Profile Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
**QUESTIONS** | GUEST-GROUP | DEFAULT-GROUP | STOREFRONT-Evaluators | STOREFRONT-Librarian | STOREFRONT-Admin
Access admin Questions Page| {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Read Questions on Entries | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Delete Questions on Entries | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Update Questions on Entries | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Access to the User Questions Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
**RELATIONSHIPS** |
Access to the admin Relationships Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Access to the User Relationships Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
**REPORTS** |  GUEST-GROUP | DEFAULT-GROUP | STOREFRONT-Evaluators | STOREFRONT-Librarian | STOREFRONT-Admin
General Reports Read | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
User Run Report as Attached Email | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
User Run Report as Content of Email body | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
View Reports From All Users | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Create New Report | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Delete a Report | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Access to the Reports Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Run the Action Report | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}}  | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Run Entries by Category Report | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Run Entries by Organization Report |  {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Run Entry Detail Report |  {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Run Entry Listing Report | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Run the Entry Report | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Run the Entry Status Report | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Run the Evaluation Status Report | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Run the Link Validation Report | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Run the Sumissions Report | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Run the Useage Report | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Run the User by Organiation Repeat | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Run the User Report | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Run the Workplan Status Report | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
**REPORTS SCHEDULE** | GUEST-GROUP | DEFAULT-GROUP | STOREFRONT-Evaluators | STOREFRONT-Librarian | STOREFRONT-Admin
Create Scheduled Reports | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Delete Scheduled Reports | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Read Scheduled Reports | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Edit Scheduled Reports | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
**REVIEWS** |
Access to the Admin Reviews Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Read Reviews |  {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Delete Reviews |  {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Update Reviews | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Access to the Reviews Page |  {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
**ROLES** |
Ability to Read Security Roles | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Create Security Roles | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Delete Security Roles | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Update Security Roles | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Access to Admin Security Roles Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
**SEARCH MANAGEMENT** | GUEST-GROUP | DEFAULT-GROUP | STOREFRONT-Evaluators | STOREFRONT-Librarian | STOREFRONT-Admin
Read all Searches |  {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Create a Search |  {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Delete a Search |  {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Update a Search |  {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Search Page (User) | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
**SECURITY** | 
Access to the Admin Security Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Access to the Security Policy Tab | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Access to the Shiro Config Tab | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
**SUBMISSIONS (ADMIN)** | GUEST-GROUP | DEFAULT-GROUP | STOREFRONT-Evaluators | STOREFRONT-Librarian | STOREFRONT-Admin
Access to Partial Submissions Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Access to Admin Custom Submission Form Template Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Read Submission Form Templates | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Create Submission Form Templates | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Delete Submission Form Templates | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Update Submission Form Templates | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Read User Sumissions | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Delete User Submissions | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Update User Submissions | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
**SUBMISSIONS (USER)** | 
Access to the User Submissions Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Read User Submissions | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Create Change Requests | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Create User Submissions | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Delete User Submissions | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Update User Submissions | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
**SYSTEM** | GUEST-GROUP | DEFAULT-GROUP | STOREFRONT-Evaluators | STOREFRONT-Librarian | STOREFRONT-Admin
Access to the Admin System Archives Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
General System Administration Permission | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Update Applicaiton Properties | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Create System Archives | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Delete System Archives | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Manage System Archives | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Access to System Cache | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Remove/ Clear Operations for the System | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Read Application Meta Data | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Update System Configuration | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Error Ticket Mangaement | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Permission Logging | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
System Managers | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Management of Plugins | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close"color="red">}} | {{<icon name="fa-check" color="green">}}
System Recent Changes | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
System Search Management | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
System Stand By | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
System Status | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Access the Admin System Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
**TAGS** | GUEST-GROUP | DEFAULT-GROUP | STOREFRONT-Evaluators | STOREFRONT-Librarian | STOREFRONT-Admin
Access to the Admin Tags Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
**TRACKING** | 
Access to the Admin Tracking Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Read Tracking | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Delete Tracking from an Item | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Update Tracking on an Item | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
**USER MANAGEMENT** | 
Access to the Admin User Management Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Read other User's Submissions | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Approve other User's Submissions | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Create Submissions for Users | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Delete other User's Submissions | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Update other User's Submissions | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
**WATCHES** | 
Access to Admin Watches Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Read Watches | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Delete Watches | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Update WAtches | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Access to the User Watches Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
**WORKPLAN** | GUEST-GROUP | DEFAULT-GROUP | STOREFRONT-Evaluators | STOREFRONT-Librarian | STOREFRONT-Admin 
Create Workplans | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Delete Workplans | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Access to the Workplans Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Read Workplans as Administrator | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Update Workplans | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
User can view current status of selected record in a workplan | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
**WORKPLAN PROGRESS MANAGEMENT** | 
Get Submission Comments for Role | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Enable Assigning and Unassigning to Admin | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Assign Any Entry to Any User | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Enable Updating of Workflows | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
View Your Group Workflow Links | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
View ALL Workflow Links | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
Update Workflows | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
View Workplan Progress Management Page | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}

**[GO TO TOP]({{<relref "#toc">}})**


## 3. Overlapping Permissions

{{% notice info %}}
**What happens if a user is a part of more than *one* Security Role?  If a user is part of multiple security roles, such as an Evaluator AND a Librarian (and the Default Group as all users are) then if a particular permission is granted to one group, it is grandted (ANDed) to the user.  For example:**

 * If user "bobsmith" has the Security Roles of Evaluator AND Librarian he will see,
 * BOTH the Evaluation Status Widget AND the Notifications Widget on the User Dashboard
 * If the user chooses to add it to his/her dashboard.  Both with be available to add.

Note the permissions table below:
{{% /notice %}}

ABILITY OR PERMISSION TO: |  DEFAULT-GROUP | STOREFRONT-Evaluators | STOREFRONT-Librarian | Bob Smith's (ANDed) Access
------------------------: | :------------: | :-------------------: | :------------------: | :----------------: 
View the System Status Widget | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}}
View the Evaluation Status Widget | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}
View the Questions Widget | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}}
Allow Evaluator to Edit an Evaluation | {{<icon name="fa-close" color="red">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}} | {{<icon name="fa-check" color="green">}}

**[GO TO TOP]({{<relref "#toc">}})**


## 4. Default Data Restrictions
{{% notice warning %}}
**To use these you MUST mark each individual entry or component in the database with the appropriate markings.  This admin area only serves to mark which Security Roles have access to which distributions.**
{{% /notice %}}

Data restrictions, if used, restrict users to seeing certain entries.  These are all turned **OFF** (disabled) by default.

#### Data Sources
ENABLED|CODE|DESCRIPTION
:-----:|:--:|:---------:
{{<icon name="fa-close" color="red">}} | DI2E | DI2E Component Entry
{{<icon name="fa-close" color="red">}} | ER2 | ER2 Asset

#### Data Sensitivity
ENABLED|CODE|DESCRIPTION
:-----:|:--:|:---------:
{{<icon name="fa-close" color="red">}} |  DISTRO A | Government Distribution A
{{<icon name="fa-close" color="red">}} |  DISTRO B | Government Distribution B
{{<icon name="fa-close" color="red">}} |  DISTRO C | Government Distribution C
{{<icon name="fa-close" color="red">}} |  DISTRO D | Government Distribution D
{{<icon name="fa-close" color="red">}} |  DISTRO E | Government Distribution E
{{<icon name="fa-close" color="red">}} |  DISTRO F | Government Distribution F
{{<icon name="fa-close" color="red">}} |  ITAR | Internation Traffic in Arms Regulations
{{<icon name="fa-close" color="red">}} |  PUBLIC | Available to the Public
{{<icon name="fa-close" color="red">}} |  SENSITIVE | Sensitive Information Marking


**[GO TO TOP]({{<relref "#toc">}})**

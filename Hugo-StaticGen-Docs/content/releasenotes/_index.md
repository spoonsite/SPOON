+++
title = "Release Notes"
description = ""
weight = 9999
+++

For more information, please see [GitHub](https://github.com/spoonsite/SPOON/releases).

## Release Notes for v2.12.3

This is the final release before the handoff of SPOON to NASA. A lot of bug and security fixes.

### Task

- [SPOON-1472] - Redirect or remove UserTool>'Change Password'
- [SPOON-1474] - Clean unused code from the view.jsp page and associated pages/components
- [SPOON-1480] - Investigate missing default branding
- [SPOON-1485] - Investigate sending bulk email to SPOON users
- [SPOON-1489] - Update backup documentation
- [SPOON-1492] - Review SPOON documentation
- [SPOON-1494] - Investigate Mail Delivery Failure to Multiple Recipients


### Story

- [SPOON-1117] - Replace OrientDB with MongoDB
- [SPOON-1160] - Investigate Metadata exploit for api/v1/resource/components/{id}/metadata/(metadataid)
- [SPOON-1161] - Implement Weak Password Checking for api/v1/resource/users/currentuser/resetpassword ie... checking against the top 10000 worst passwords.
- [SPOON-1162] - Fix api/v1/resource/users/{username}/resetpassword
- [SPOON-1164] - Investigate api/v1/resource/userprofiles/{id}
- [SPOON-1171] - Fix api/v1/service/application/configproperties/{key}
- [SPOON-1172] - Fix api/v1/service/application/retrievemedia
- [SPOON-1173] - investigate api/v1/service/notification/contact-vendor
- [SPOON-1174] - Investigate api/v1/service/security/forgotusername
- [SPOON-1175] - Investigate api/v1/service/security/{username}/resetpassword
- [SPOON-1329] - Make error handling consistent in all Vue Files
- [SPOON-1354] - Change SPOON slides to the new wider view and new logo
- [SPOON-1446] - Prepare mockup designs to Craig and Lauren 4/22 meeting
- [SPOON-1453] - Log out doesn't redirect properly
- [SPOON-1464] - Delete All Code Related to the Old Submissions Form
- [SPOON-1465] - Remove advanced save search feature
- [SPOON-1469] - Remove old login and user pages
- [SPOON-1475] - Review the security of /api/v1/service/notification/contact-vendor-template


### Bug

- [SPOON-1027] - 48 LSP (4,4,3) Copying & Pasting External media (picture) claims to fail in Pop-up (But actually Works)
- [SPOON-1235] - Cancel on the contact form does nothing
- [SPOON-1237] - Logging on as a user (userdude/userDude1!) gives an error on the homepage about watches
- [SPOON-1238] - Tags, duplicate and confusing GUI button
- [SPOON-1239] - Tag deletion confirmation text is drastic and confusing
- [SPOON-1240] - When toggling status on an entry comments are not optional, but the ui says they are
- [SPOON-1241] - Searching for Attributes in Search Filters does not work properly
- [SPOON-1245] - Need to remove link in descripton toolbar for Insert Saved Search
- [SPOON-1246] - Recalcitrant Disclaimer Popup
- [SPOON-1250] - No longer able to send a test message from the profile page
- [SPOON-1254] - The word "Storefornt" appears in an admin setting
- [SPOON-1258] - Update Version to 2.11
- [SPOON-1316] - File generates logs like crazy
- [SPOON-1334] - Merging an organization dumps the approved parts of mergee- organization into Workplan Progress
- [SPOON-1368] - Unapproved/pending answers to questions should not be sent
- [SPOON-1371] - DO FIRST: Reports are now Admin only; remove ones that are OBE, fix or remove failing reports
- [SPOON-1375] - Deleted WorkPlan's Links Become Orphans
- [SPOON-1457] - Can't add attributes in admin add entry attribute tab
- [SPOON-1458] - Import exported file fails with SYSTEM error
- [SPOON-1463] - Investigate: Entry Listing report fails
- [SPOON-1468] - Admin Notification message links to wrong report path
- [SPOON-1470] - Registration 'Cancel' button should redirect back to Login Page
- [SPOON-1471] - Investigate branding error
- [SPOON-1473] - Form submitting on the "Forgot User" page even with invalid email when pressing the enter key
- [SPOON-1482] - Unable to Approve Component Update Submissions
- [SPOON-1483] - Duplicate external links on page refresh
- [SPOON-1493] - Duplicate username created
- [SPOON-1502] - Investigate Imports Failing

## Release Notes for v2.12.2

This is another patch release. The changes are mostly code cleanup. The user shouldn't notice any changes in behavior or in the UI.

- remove Confluence integration
- fix log out redirect
- update ehCache to latest version (v3 had large breaking changes)
- update login area to latest Vue and Vuetify

## Release Notes for v2.12.1

This is a patch release to fix bugs with the new submission form. There were a lot of issues with the attributes and tags.

There was also some work done on the workplans.

## Release Notes for v2.12

This release finished converting all user tools to VueJS. This effort was mostly to update the submission form.

* Updated up the emails
* Fixed login page redirect
* Removed JIRA plugin

## Release Notes for v2.11.1

* Fixed a small bug that prevented viewing of some entries for Admins reviewing submission

## Release Notes for v2.11

This release started the process to remove ExtJS pages and convert them to VueJS. Most of the **User Tools** area was implemented. A lot of work was done previously in VueJS to make a mobile version of the site. The mobile pages were taken and made responsive for desktop and mobile.

* Redesigned landing page
* Remove the separate mobile VueJS site
* Search speed improvements
* Search page user interaction redesign

## Release Notes for v2.10

* Removed Solr as a search engine option.
* Updated Elasticsearch to Version 7.2.0.
* Removed the "Move Submission" buttons in the WorkPlan if you did not have the correct permissions.
* Updated branding and removed non-functional sections of branding.

## Release Notes for v2.9.2

* Fix for mobile site not being able to load.

## Release Notes for v2.9.1

* Fix for commenting on WorkPlan submissions.

## Release Notes for v2.9

* Added a Vendor Last Update date to all entries.
* Vendor Last Update date is shown on all entry views.
* Emails are sent when an entry or change request is approved.
* User submissions will now enter a queued state while being processed.

## Release Notes for v2.8

* Added a unit field to attributes.
* Changed the format of the comparison view.
* Added support for rendering units in a pretty format.
* Added a bulk upload tool for zipped files.

## Release Notes for v2.7

* Improved feedback to users about submission status while editing a submission.
* Made major improvements to search including better search filters.
* Fixed issues with reports.
* Improved mobile search filters.

### Administrator Release Notes for v2.7

* Various improvements to Entries admin page.

## Release Notes for v2.6.6

* Issues fixed with "Contact Us" form.
* Video support added for media on mobile.

### Administrator Notes for v2.6.6

* SME WorkPlan approval now added to mobile.
* Additional user roles defined and created.

## Release Notes for v2.6.5

* Users can now see current WorkPlan state of submissions.
* Users and reviewers can now communicate directly through comments on submissions.
* Minor bug fixes on both desktop and mobile.

### Administrator Notes for v2.6.5

* New Workflow Management page added.
* New WorkPlan Progress page added.
* Assigned user roles will now have access to WorkPlan Progress page. Submissions assigned to them will show and can be moved to next state.

## Release Notes for v2.6.3

* New login page released.
* New mobile site released.
* Users can view entries on mobile and adjust their account settings such as changing a password or email.
* Mobile site supports viewing updates on watches, requesting feedback, registering for an account, updating account information, and more.

### Administrator Notes for v2.6.3

* Administrators now have limited functionality on mobile.

## Release Notes for v2.6.2

* Some adjustments to the Custom Submission Form.

## Release Notes for v2.6

* The Submission Process now allows for forms that more closely match the input expected for a given entry type.
* Multiple Entry types/Topics can now be selected in the search.

### Administrator Notes for v2.6

* Evaluation Form has been updated to improve distinguishing Entry from Evaluation.
* Published Evaluations are now visible.
* A Jira ticket link will now show in the evaluation table if configured.
* Improvements made to the Confluence report.
* Issues fixed with change request merging of tags, media, and resources.
* Attribute can now be flagged private and have comments.
* Resources can now be marked private.
* Entry type can now have sub type and be nested.

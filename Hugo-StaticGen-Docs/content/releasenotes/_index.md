+++
title = "Release Notes"
description = ""
weight = 9999
+++

For more information, please see [GitHub](https://github.com/spoonsite/SPOON/releases).

## Release Notes for v2.11.1

* Fixed a small bug that prevented viewing of some entries for Admins reviewing submission

## Release Notes for v2.11

This release started the process to remove ExtJS pages and convert them to VueJS. Most of the **User Tools** area was implemented. A lot of work had done previously in VueJS to make a mobile version of the site. The mobile pages were taken and made responsive to desktop and mobile.

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

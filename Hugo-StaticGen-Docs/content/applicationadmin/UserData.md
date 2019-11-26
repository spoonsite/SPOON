+++
title = "User Data Management"
description = ""
weight = 230

+++

![Diagram of main System sub-page](/images/AppAdmin/UserDataManagementMapping.png)

## Table of Contents

1. [Contacts](/applicationadmin/userdata/#contacts)
1. [Organizations](/applicationadmin/userdata/#organizations)
1. [Questions](/applicationadmin/userdata/#questions)
1. [Reviews](/applicationadmin/userdata/#reviews)
1. [Watches](/applicationadmin/userdata/#watches)
1. [Tags](/applicationadmin/userdata/#tags)
1. [User Profiles](/applicationadmin/userdata/#user-profiles)

## Contacts

Entries on Spoon can have a designated 'Contact', a person who is responsible for being the point of contact for that particular entry.

## Organizations

Here you can see all of the registered organizations on Spoon. There are tools for merging organizations when duplicates are found.

## Questions

Users can ask questions to entry's contacts, but before the question shows up publicly on Spoon, it must be approved by the Admin.

Question/Answer Status | EXPLANATION
-----------------: | -----------
Pending Questions/Answers | All newly created questions (created by users, that is) are viewable here. While a question is marked as a Pending Question, it is not visible to anyone by the user that posed the question. __The Admin will NOT receive any notification when a new question is placed.__ After a question has been asked, the Admin must *explicitly click on the Question* in the question queue to load answers to that question into the "Answers" queue on the far right column. __The Admin will NOT receive any notifications when a new answer is placed.__ Any edits to the question/answer by a user will reset the status of that question/answer to Pending.
Active Questions/Answers | Setting a question/answer as being "Active" makes it visible to everyone
Inactive Questions/Answers | Inactivating the question/answer archives it. Even the user who placed the question/answer will not be able to see it when he/she looks at the entry.

Also note that questions/answers made by the administrators are treated the same as a regular user: they must be approved through this process.

## Reviews

On the Review page the Admin must approve any Reviews on entries that are submitted by members.

Note the small "__Active Status__" dropdown in the top left corner. If you expect to see an item in the grid below but don't, you may be looking at the wrong Status for the Review you are looking for.

Review Status | EXPLANATION
-----------------: | -----------
Pending | All newly created reviews (created by users, that is) are viewable here. While a question is marked as a Pending, it is not visible to anyone by the user that posed the question. __The Admin will NOT receive any notification when a new review is placed.__ If you "__Action__" a review into Active status, but then the user who placed the review edits it, the whole review will be placed back into Pending status such that no one will be able to see it until the Admin re-approves the changed review.
Active | Setting a review as being "Active" makes it visible to everyone
Inactive | Inactivating the question/answer archives it. Even the user who placed the question/answer will not be able to see it when he/she looks at the entry.

## Watches

![Watches page screenshot](/images/AppAdmin/UserDataWatches.png)
All watches created by all users are visible here. The Admin can toggle off watches on any user, which makes the watch disappear (from the user's perspective).

## Tags

![Tags page screenshot](/images/AppAdmin/UserDataTags.png)
Any user can add or remove tags from an entry, no Admin approval required whatsoever (with exception of entries that said user could not see in the first place, such as entries that have security restrictions).

The "__Select A Tag__" column lists all the tags that currently exist on Spoon.

{{% notice info %}}
 A tag cannot exist unless it is assigned to at least one entry at a time. Be careful when un-assigning parts from a tag not to remove all of them all at once if you want to keep that tag. The only exception to this rule is when the Admin creates a brand new tag by clicking the "__Add__" button - then the Admin can assign parts to a tag that previously had no existing assignments. Otherwise, the tag will decease.
{{% /notice %}}

Selecting a tag populates the "__Associated Entries__" and "__Unassociated Entries__" columns on the right. If, upon checking the database, it is found that some entry the tag is assigned to is not submitted, pending, inactive, or not approved, *that entry is not placed in either column*. This can present a difficult scenario if the Admin needs to determine the assigned entries of a tag, but the only assigned entries of the tag are not available. The Admin will be alerted as to how many of the assigned entries are in an un-displayable state by a small message that will appear near the top of screen (see the screen shot above).

## User Profiles

![Tags page screenshot](/images/AppAdmin/UserDataUserProfile.png)
You may notice that this page is very similar in functionality to the [User Management page](/applicationadmin/applicationmanagement/#user-management) from Application Management.

Toggling a profile only switches it's "Active Status", a boolean (on/off) value that is attached to all profiles on Spoon. However, in v2.11, this value is not tied to anything. If the Admin toggles off a profile to make it inactive, it will not affect any user functions at all, and when the user logins into Spoon next the "Active Status" value will be switched back to "A" (which stands for Active, as opposed to "I", which signifies Inactive).

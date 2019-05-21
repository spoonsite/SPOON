+++
title = "Publish an Evaluation"
description = ""
weight = 14
+++


This guide goes over the different types of publishing and the various scenarios a librarian may run into when publishing an entry/evaluation.
<!--more-->

Different types of publishing:

1. Entry publishing - publishes the entry summary only
2. Partial publishing - publishes all default public fields except for those that have been marked as private.
3. Full publishing - publishes all default public fields.

**Reminder:** an entry is a single entity that can have one to many evaluations. &nbsp; An evaluation 
can include multiple sections, a checklist summary, recommendations, and questions. &nbsp; An entry can have a description, contacts, media, resources, tags, 
relationships, and dependencies

## Who is this for?

This guide is for anyone with the role of librarian or administrator. &nbsp; If unsure of current role(s), contact an administrator.

## Resolving Conflicts

A librarian may run into a conflict between multiple evaluators evaluating the same entry. &nbsp; To resolve this type of conflict use the change history tool to help determine the desired content.

If the conflict is related to the entry, view the entry's history by doing the following:

1. Go to **Entries** under **Data Management** 
2. Select the entry and click on the **Edit** button
3. On the entry form, click on the **Change History** button

If the conflict is related to the evaluation, view the evaluation's history by doing the following:

1. Go to **Evaluations** under **Evaluation Management**
2. Select the evaluation and click on the **Edit** button
3. On the evaluation form, click on the **Change History** button

Another conflict the librarian may run into is multiple change requests. &nbsp; To resolve conflicts between change requests use the entry change request tool to edit and approve changes.

1. Go to **Entries** under **Data Management**  
2. Select the entry and click on **Change Requests** under the **Action** drop-down menu  
3. Use the form to compare the current state of the entry to the proposed changes in the request.
4. Use the toolbar to approve change requests, delete change requests, edit entries, and sending messages to the submitter of a change request. 


## Common Publishing Scenarios Encountered By Librarians  

### Scenario 1  

An evaluator has completed an evaluation on an approved entry. &nbsp; There are no change requests pending for the entry and all
conflicts have been resolved.

__Solution:__ &nbsp;Librarian publishes the evaluation. &nbsp; (Note: When an evaluation is published, the approved entry is replaced with the evaluation entry)

### Scenario 2  

The evaluation is complete for an approved entry, but the librarian or administrator needs to make a change to the entry.

__Solution:__ &nbsp;Librarian or administrator will need to make the desired changes on the evaluation. &nbsp; After the changes are made and the evaluation has been saved, the evaluation can be published.

### Scenario 3  

An approved entry is in the process of being evaluated, but the librarian or administrator has just received a change request from the entry's submitter.

__Solution:__ &nbsp;The librarian will need to handle two separate change requests: submitters' change request and the evaluation change request.
If the librarian decides to approve the submitter's change request, the librarian will also need to manually update the evaluation change request to match the approved changes.
If the evaluation change request is approved, the system will create a new submission based on the current evaluation.

### Scenario 4  

The evaluator has begun the evaluation for an entry but due to lack of information can only fill in the summary section. &nbsp; There should be open dialog at this point between the librarian and the evaluator.  

__Solution 1:__ &nbsp;the librarian can publish the evaluation summary only and leave the evaluation in an unpublished state. &nbsp; If there is partial information worth hanging on to, change the evaluation's workflow status to **On Hold** (Note: **DO NOT** delete the evaluation). 

__Solution 2:__ &nbsp;the librarian can publish the evaluation and then delete the evaluation if the partial information is not worth saving.

### Scenario 5  

The evaluator has filled in the majority of the sections but was unable to install the software.

__Solution:__ &nbsp;the librarian should mark the install section as private and then publish the evaluation.  

### Scenario 6  

The evaluator has completed an evaluation on a pending entry. 

__Solution:__ &nbsp;the librarian should publish the evaluation. &nbsp; By publishing the evaluation, the entry is updated with the evaluation's information and the entry's status is changed from _Pending_ to _Approved_.

### Scenario 7  

The librarian has published an evaluation for an approved entry, but the owner of an entry has submitted a change request.  

__Solution:__ &nbsp;the librarian should approve the change request and the entry will be updated.  

### Scenario 8  

An evaluation is in progress on a pending entry, but the librarian needs to make a change to the entry.  

__Solution:__ &nbsp;the librarian should make changes to the evaluation **_NOT_** the entry.  

### Scenario 9  

The librarian has published an evaluation for an approved entry, but the asset or component has changed and it affects the evaluation. 

__Solution 1:__ &nbsp;librarian should unpublish the the evaluation, make desired changes, and then republish. 

__Solution 2:__ &nbsp;if the changes made to the entry warrant a new evaluation, the librarian should either copy the published evaluation and edit it or create a new evaluation for the entry.


### Scenario 10  

An evaluation has been published for a pending entry, but the librarian needs to make changes to the entry.  

__Solution:__ &nbsp;the librarian should update the entry in under **_Data Management &rarr; Entries_** and leave the evaluation published. &nbsp;After making changes, approve the entry. &nbsp;**Note:** the published evaluation will not be searchable by users until the entry is approved after changes made.

### Scenario 11  

The librarian needs to make changes to an approved entry with an unpublished evaluation but cannot publish the evaluation because it is not ready for a full or summary publication.  

__Solution 1:__ &nbsp;the librarian should manually apply changes to both the _approved_ entry and the _unpublished_ evaluation.  

__Solution 2:__ &nbsp;if the evaluation's entry can be published, publish the summary first and then update the actual entry under **_Data Management &rarr; Entries_**.  **_Note:_**&nbsp; The next time the evaluation is edited, it will create a new change request based on the current entry.
+++
title = "Workplans"
description = ""
weight = 240
+++

## Create a new Workplan

A step-by-step guide to creating a workplan.

### Create the Workplan

1. Navigate to __Admin Tools__ if you are not already there (click your username in the upper-right, then Admin Tools)
2. Select __Data Management &rarr; Workplan Management__
3. Click __+Add Workplan__ ![Workplan Creation Tool](/images/AppAdmin/AddWorkplan.JPG)
4. First fill in the Workplan Config Section
    * Workplan Name: Unique Workplan name
    * Workplan Admin Role: Choose the user group that will have access to entries in this workplan
    * Workplan For: Entry
    * Entry Type: Choose the entry type that this workplan will apply to

### 1.2  Define Workplan Steps

It is a good idea to define the steps to be taken with each entry before creating the actual steps.

1. Click __Create Step__ ![Workplan Add Step](/images/AppAdmin/WorkplanCreateStep.JPG)
1. Step Configuration:
    * **Step name**: Unique step name
    * **Short Description**: A description of the step. Keep it brief, but be sure to include enough information for the person responsible for the step
    * **Approval State to Match**: This will be the status of a entry while it is in this step
    * **Active On**: The state that an entry is currently in when it is assigned to that step
    * **Role Access**: What role group will be able to view this entry in their Workplan Progress page

1. Step Actions: Step Actions are events that will occur when a step becomes active

{{% notice warning %}}
In Spoon v2.12, most of the "Active On" options are in Alpha, which means that they will not work as expected because their functionaity has not been programmed into Spoon yet. Those options are there as placeholders (if you want these features, please email your adminstator/developer team). The "Update" and "Create" options do work however. Any of the in-alpha options will behave the same as if they were the "Create" option. See below for more details.
{{% /notice %}}

##### __Step Configuration:__ "Active On" VS "Approval State to Match"

It's important to know the difference between "Active On" and "Approval State to Match". Both define what events will cause a part to be sent to the Workplan Step in question. The difference is in the timing of when they take effect. "Active On" events are handled first, then the "Approval State" of a part is checked, and placed into the first step that has a "Approval State to Match".

For example, say that a part sitting in a Workplan is manaully set from having an Approval state of "Pending" to "Approved"  (an admin is able to do this through Admin Tools > Data Management > Entries, click "Edit"). When the part is re-saved into the database with this new Approval status, an approved event [should] be fired. In the Workplan, the first Step that has an "Active On" option of "Approved" will receive that part. Then the Approval State will be checked on the part, and the first step in the Workplan that has an "Approval State to Match" of Approved will recieve the part. Thus if there is a workplan step A that has "Active On" attribute Approved, and a workplan step B with an "Approval State to Match" of Approved, then after a part has been approved, the part will appear in step B.

Note that in v2.12 of Spoon, only "Create" and "Update" are supported, therefore some events are treated as if they are "Create": Activate, Deactivate, Pending Change Request, Published Evaluation (? untested), Unpublished Evaluation (? untested). The Approved and Pending events are treated like Update events. So setting those options will cause the step in question to catch Created and Updated parts, respectively.

## 2. Workplan Progress for Admins and SMEs

Each step of a workplan will be assigned to a certain group. That group will see the entries that are assigned to them when they access the Workplan Progress page (__Data Management &rarr; Workplan Progress__). Each group will complete the task assigned to them. Once completed it will move to the next group.

The comments feature has two uses. The first is to enable communication between reviewers, SMEs, and admins. To do this when viewing a submission and commenting, keep the __Private__ box checked. The second use is to communicate with the submission owner.

The __Assign__ box has a number of operations used to assign a submission to an admin, unassign or assign it to yourself, or reassign it to another user.

## 3. For Submission Owners

Submission owners will get notifications of comments on their submissions. Owners are expected to update material as recommended by SMEs and reviewers.

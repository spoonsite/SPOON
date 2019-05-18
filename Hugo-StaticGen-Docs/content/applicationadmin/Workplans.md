+++
title = "Workplans"
description = ""
weight = 2
+++
 
 This guide reviews how to create and manage a workplan.
 <!--more-->

## 1. Create a new Workplan

### 1.1 Create the Workplan

1. Navigate to __Admin Tools__ if you are not already there (click on your username in the upper-right, then Admin Tools)
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
2. Step Configuration:

  * Step name: Unique step name
  * Short Description: A description of the step. Keep it brief, but be sure to include enough information for the person responsible for the step
  * Approval State to Match: This will be the status of a entry while it is in this step
  * Active On: The state that an entry is currently in when it is assigned to that step
  * Role Access: What role group will be able to view this entry in their Workplan Progress page

3. Step Actions: Step Actions are events that will occur when a step becomes active

## 2. Workplan Progress For Admins and SMEs

Each step of a workplan will be assigned to a certain group. That group will see the entries that are assigned to them when they access the Workplan Progress page (__Data Management &rarr; Workplan Progress__). Each group will complete the task assigned to them. Once completed it will move to the next group.

The comments feature has two uses. First to enable communication between reviewers, SMEs, and admins. To do this when viewing a submission and commenting, keep the __Private__ box checked. The second use is to communicate with the submission owner.

The __Assign__ box has a number of operations used to assign a submission to an admin, unassign or assign it to yourself, or reassign it to another user.

## 3. For Submission Owners

Submission owners will get notifications of comments on their submissions. Owners are expected to update material as recommended by SMEs and reviewers.

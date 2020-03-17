+++
title = "Workplans"
description = ""
weight = 240
+++

![Simple illustration of a Work Plan diagram](/images/AppAdmin/Workplans/summarypic.png)

## Table of Contents

1. [**Workplans**]({{<ref "#workplans" >}})
  * [**Create the Workplan**]({{<relref "#create-the-workplan">}})
  * [**Define WorkPlan Steps**]({{<relref "#define-workplan-steps">}})
2. [**WorkPlan Progress (For Admins and SMEs)**]({{<relref "#workplan-progress-for-admins-and-smes">}})
3. [**Submissions Page**]({{<relref "#for-submission-owners">}})

## Workplans

A step-by-step guide to creating and managing workplans. There is a built in __Default WorkPlan__ that cannot be deleted. There is no limit on the number of WorkPlans that can be created.

 The __"Active" Workplans__ will be used by the system to classify all entries on Spoon.
  All entries should be in an active workplan. To enforce this, approximately every 10 minutes the WorkPlanSync [Job]({{<relref "ApplicationManagement.md#jobs">}}) is ran.
  This job checks every entry on Spoon to ensure that it is represented in an active WorkPlan. For those that are not,
  possibly because they used to reside in a recently de-activated WorkPlan, they are reassgined to the WorkPlan marked
  for entries of their Entry Type or the Default WorkPlan if no active WorkPlans have an appropriate type^[ There is a
  notable exception to this rule: If an entry is in a WorkPlan that is inactivated and there is NOT another active
  WorkPlan with a matching Entry Type, the entry will stay in the inactivated Workplan, it will NOT be moved to the
  default Workplan. So it is possible for a entry to be in an inactivated plan. In this state these entries can be
  acted upon in the same manner as normal entries. ].

  Workplans can be assigned to one or more [Entry Types]({{<relref "#create-the-workplan">}})
  simultaneously. Learn about [creating Entry Types]({{<relref "Attributes.md#entry-types">}}), or learn about [setting
  a Workplan's Entry Types]({{<relref "#create-the-workplan">}})

  If there is no active WorkPlan that is marked to accept entries of the Entry Type needed, that entry will be
  assigned to the default WorkPlan.

  If an entry is in a Workplan that is inactiaved, and there is another, active WorkPlan that has a matching Entry Type,
  the entry will be moved to that WorkPlan.

### Create the Workplan

1. Navigate to __Admin Tools__ if you are not already there (click your username in the upper-right, then Admin Tools)
2. Select __Data Management &rarr; Workplan Management__
3. Click __+Add Workplan__ ![Workplan Creation Tool](/images/AppAdmin/AddWorkplan.JPG)
4. First fill in the Workplan Config Section
    * Workplan Name: Unique Workplan name
    * Workplan Admin Role: Choose the user group that will have access to entries in this workplan
    * Workplan For: Entry
    * Entry Type: Choose the entry type that this workplan will apply to

###  Define Workplan Steps

It is a good idea to define the steps to be taken with each entry before creating the actual steps.

1. Click __Create Step__ ![Workplan Add Step](/images/AppAdmin/WorkplanCreateStep.JPG)
1. Step Configuration:
    * **Step name**: Unique step name
    * **Short Description**: A description of the step. Keep it brief, but be sure to include enough information for the person responsible for the step
    * **Approval State to Match**: is only used to place new entries into matching WorkPlan steps in instances where an entry has never been represented in
        the particular WorkPlan before. When a new WorkPlan is created, this is what is used to decide where entries should be placed in the new WorkPlan.
        Transitions between WorkPlans are the only time this marker is used.

        For example, imagine that the current WorkPlan had a number of entries moving through it such that at any given moment there was a fair number
        of parts in every WorkPlan step. Let there be a new WorkPlan was created that may or may not have a different number of steps, different actions,
        triggers, etc. When the new WorkPlan is made active, and the WorkPlanSync [Job]({{<relref "ApplicationManagement.md#jobs">}}) is run (without running manually, it will run automatically every
        15 minutes or so), all of the entries in the old, recently inactivated WorkPlan will be placed under WorkPlan step.

        The diagram below shows where entries (the brown cards marked with their current Approval Status) would be placed in a newly created
        WorkPlan with a different number of steps. Note that when new steps are added to an existing, active WorkPlan, this matching process
        does not happen.

        ![Example Diagram of Approval State To Matches effect](/images/AppAdmin/Workplans/approvaltomatch.png)

    * **Role Access**: What role group will be able to view or move this entry in their Workplan Progress page. Role groups who are put here will be able to see "Forward" and "Back" buttons on their display.
    * **Active On**: "Triggers" or "Traps" for component events. When an entry experiences an event, it will be sent to the WorkPlan step that has a matching Active On status as that event.

    |      Role Access Option        |                                                                                                                                                                                                                                                     Meaning                                                                                                                                                                                                                                                     |
    |:------------------------------:|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
    | Active                         | Fires when a component is submitted to the server as needing to  be activated. *                                                                                                                                                                                                                                                                                                                                                                                                                                |
    | Approve                        | Fires when a component is submitted to the server to be approved.*                                                                                                                                                                                                                                                                                                                                                                                                                                              |
    | Create                         | Fires when component is first created.                                                                                                                                                                                                                                                                                                                                                                                                                                                                          |
    | Deactivated                    | Fires when a component is submitted to the server as needing to be  deactivated. *                                                                                                                                                                                                                                                                                                                                                                                                                              |
    | Delete                         | This trigger fires, but does nothing. If there is some specific behavior  you would like to see from this event firing, please contact the  development team.                                                                                                                                                                                                                                                                                                                                                   |
    | Entry Delete                   | This trigger does nothing.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
    | New Submission (Not Submitted) | Only fires when a entry is submitted to be saved, before it is submitted to as pending, where the entry is a "Partial Submission" object, not a "Component"  object. If you are using SPOON v1.12 or greater, you will likely not need this as all user created entries will be "Component" objects.                                                                                                                                                                                                                    |
    | Pending                        | Fires when a Not-Submitted component is submitted to the server. This trigger is NOT fired when an Admin user manually sets a component's Approval Status to Pending.                                                                                                                                                                                                                                                                                                                                           |
    | Pending Change Request         | Fires when a change request is submitted to the server.                                                                                                                                                                                                                                                                                                                                                                                                                                                         |
    | Published Evaluation           | Fires when an evaluation is submitted to the server as to be Published.                                                                                                                                                                                                                                                                                                                                                                                                                                         |
    | Unpublished Evaluation         | Fires when an evaluation is submitted to the server as being un-Published.                                                                                                                                                                                                                                                                                                                                                                                                                                      |
    | Update                         | Fires anytime a component is saved for any reason, in any way. Even editing & saving an entry from the Admin Tools > Data Management > Entries > "Edit" screen fires this trigger. If you attach this trigger to a WorkPlan step, an entry may appear to be "stuck" in that step, as any Action of a step that alters the entry enough to re-save it will trigger the Update trap which sends the entry back to the Update-marked WorkPlan step (An infinite loop, basically). For this reason we do not recommend you use this trigger unless you desire this behavior. Moving a entry manually between steps will not fire the Update trigger.  |
    __In cases marked with *, note that this trigger is not fired if the entry's status is manually changed by the Admin user from the Admin Tools >
    Data Management > Entries > "Edit" screen. These triggers are only fired when the status change happens through other means, such as Action Steps.__

3. Step Actions: Step Actions are events that will occur when a step becomes active

## Workplan Progress for Admins and SMEs

Each step of a workplan will be assigned to a certain group. That group will see the entries that are assigned to them when they access the Workplan Progress page
(__Data Management &rarr; Workplan Progress__). Each group will complete the task assigned to them. Once completed it will move to the next group.

The comments feature has two uses. The first is to enable communication between reviewers, SMEs, and admins. To do this when viewing a submission and commenting, keep the __Private__ box checked.
 The second use is to communicate with the submission owner.

The __Assign__ box has a number of operations used to assign a submission to an admin, unassign or assign it to yourself, or reassign it to another user.

## For Submission Owners

Submission owners will get notifications of comments on their submissions. Owners are expected to update material as recommended by SMEs and reviewers.

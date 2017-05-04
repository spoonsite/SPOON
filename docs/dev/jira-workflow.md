# JIRA - Issue Tracking  Workflow
----

## Ticket Types

-  New Features, Improvements, Bugs

-  Task

-  Build

## New Features, Improvements, Bugs Stages

1. Pending Review - Ticket in product backlog.

2. Review - Ticket reviewed by developer. Developer adds details such as
implementation plan, time estimate, sub-tasks, and other relevant changes.

3. Approval - Business owner or Project Manager approves/rejects an issue.
 (Typically during sprint planning or sprint review)

4. Schedule - Waiting to be scheduled for a release version.

5. Working - Currently in development. Refer to the Git Workflow guide for more information on branching style.

6. Ready for Development Move - Development is complete but ticket not moved to the Integration environment.

7. Integration - In Integration environment, subjected to automated testing.

8. Ready for Acceptance Move - Passed integration testing and waiting to be moved to the acceptance state.

9. Acceptance Test - Ticket ready for QA to test.

10. Ready for Staging - Ticket has passed QA and is now ready to move to staging.

11. In Staging - Ticket subject to final review and testing in staging environment before being moved to production.

12. Close - Ticket moved to production. Any new changes require creation of a new ticket.


## Task

A task ticket is created to represent a general task that isn't subject to the workflow above.

Example: Updating development documentation.

## Build
A build ticket is created for each release.  It contains all move instructions, environment changes, and configuration changes needed for the release.

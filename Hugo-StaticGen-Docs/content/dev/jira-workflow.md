+++
title = "Jira Issue Tracking Workflow"
description = ""
weight = 4
+++

## Ticket Types

-  New Features, Improvements, Bugs

-  Task

-  Build

## New Features, Improvements, Bugs Stages

* Pending Review - Holding state for new tickets awaiting developers initial triage.

* Waiting for information - Back to requestor for more details on requirements and/or screenshots.

* Getting PMO approval - Business owner or Project Manager approves/rejects an issue.

* Awaiting Design Review - Holding state awaiting developer resources.

* Design Review - Ticket reviewed by developer. Developer adds details such as implementation plan, time estimate, sub-tasks, and other relevant changes.

* Design Complete - Waiting for Peer approval.

* Ready to Work - Product Backlog
 * An ordered list of everything that might be needed in the product and is the single source of requirements
 * Items scheduled for the next release
 * Set of items selected for the Sprint


* Pending Verification - Item has aged in the backlog and needs to be revaluated

* Being Verified - Checking if the issue still needs addressed.

* Working - Currently in development. Refer to the Git Workflow guide for more information on branching style.

* Development Complete
 * Development work complete
 * Unit and Integration tests passing
 * Pull request submitted


* Awaiting build - Pull request reviewed and merged, but not moved to the Integration environment.

* Awaiting Testing - Code pushed to testing server ready for QA to test. (testing queue)

* Testing - Tester is currently testing the ticket.

* Ready for Staging - Ticket has passed QA and is now ready to move to staging.  Any new changes require creation of a new ticket.

* In Staging - Ticket subject to final review and testing in staging environment before being moved to production.

* Close - Ticket moved to production.


## Task

A task ticket is created to represent a general task that isn't subject to the workflow above.

Example: Updating development documentation.

## Build
A build ticket is created for each release.  It contains all move instructions, environment changes, and configuration changes needed for the release.

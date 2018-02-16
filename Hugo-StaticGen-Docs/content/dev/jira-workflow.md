+++
title = "Jira Issue Tracking Workflow"
description = ""
weight = 4
+++

Always check JIRA for latest changes. This is meant and general guide.

## Ticket Types

-  New Features, Improvements, Bugs

-  Task

-  Build

## New Features, Improvements, Bugs Stages

* **Pending Review** - Holding state for new tickets awaiting developers initial triage.

* **Waiting for information** - Back to requestor for more details on requirements and/or screenshots.

* **Ready to Work - Product Backlog**
 * An ordered list of everything that might be needed in the product and is the single source of requirements
 * Items scheduled for the next release
 * Set of items selected for the Sprint


* **Pending Verification** - Item has aged in the backlog and needs to be revaluated

* **Being Verified** - Checking if the issue still needs addressed.

* **Working** - Currently in development. Refer to the Git Workflow guide for more information on branching style.

* **Development Complete**
 * Development work complete
 * Unit and Integration tests passing
 * Code Review and/or Pull request submitted


* **Awaiting Testing** - Code pushed to testing server ready for QA to test. (testing queue)

* **Testing** - Tester is currently testing the ticket.

* **Ready to Merge** - Ticket has passed testing and is ready to merge to dev. (See developer guide for process)

* **Complete** - Ticket has passed QA and is now ready to move to staging.  Any new changes require creation of a new ticket.

* **Close** - Ticket moved to production.

## Design

Design Documents should be maintained in confluence under the Storefront space

* **Open** - Pending design item waiting for approval

* **In Backlog** - Approved waiting for assignment to work

* **Story Decomposition** - Business story need to be create and added to the design document

* **Working** - Fill in all business and technical design (follow template)

* **Awaiting Peer Review** - Design is ready for peer review

* **Awaiting PMO Approval** - (OPTIONAL) if PMO Review is need this should occur here.

* **Complete** - Done with design and all jira tickets are created.

* **Close** - when the release for the design is done.  Each design should be marked with a release number.

## Task

A task ticket is created to represent a general task that isn't subject to the workflow above.

Example: Updating development documentation.

## Build

A build ticket is created for each release.  It contains all move instructions, environment changes, and configuration changes needed for the release.

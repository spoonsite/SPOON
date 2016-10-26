#JIRA - Issue Tracking  Workflow
----

##Ticket Types

-  New Features, Improvements, Bugs

-  Task

-  Build

##New Features, Improvements, Bugs

1. Pending Review (sometimes refer to as backlog)

2. Review - This where a developer would review the ticket and add more details such as:
Implementation plan, Estimate, Sub-tasks, and what needs to change.  

3. Approve - This would where Business owner or PM would approve. 
 (Typically, we approve via sprint planning for the next release or on sprint reviews)

4. Schedule - Waiting to be scheduled for a release version

5. Working - currently in development

6. Ready for Development Move - development is complete but it has not moved to the Integration environment.

7. Integration - In Integration environment where auto tests are being ran.

8. Ready for Acceptance Move - Passed integration and is wait for the next move to acceptance.

9. Acceptance Test - The ticket is now in acceptance and is ready for QA to test.

10. Ready for Staging - Ticket has passed QA and is now ready to move to stagging. 

11. In Staging - Ticket are getting a final look in staging before production.

12. Close - Tickets have moved to production. If there any new changes then a new ticket should be created.


##Task

A task ticket is creating to represent a general task that doesn't need to be flow through the more main workflow.  
An Example is: updating development documentation.

##Build
A build ticket is created for each release.  It contains all move instruction, environment changes, and configure changes needed for the release.





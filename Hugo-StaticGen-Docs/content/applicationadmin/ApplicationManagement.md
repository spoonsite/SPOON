+++
title = "App Management"
description = ""
weight = 100
+++

![Diagram of main System sub-page](/images/AppAdmin/AppManageMapping.png)

## Table of Contents

1. [**Alerts**](/applicationadmin/applicationmanagement/#alerts)
2. [**Feedback**](/applicationadmin/applicationmanagement/#feedback)
3. [**Jobs**](/applicationadmin/applicationmanagement/#jobs)
4. [**Messages**](/applicationadmin/applicationmanagement/#messages)
5. [**Tracking**](/applicationadmin/applicationmanagement/#tracking)
6. [**User Management**](/applicationadmin/applicationmanagement/#user-management)
7. [**Reports**](/applicationadmin/applicationmanagement/#reports)

Sections not covered in this page:

1. [**Branding**](/applicationadmin/branding/)
1. [**Security**](/applicationadmin/securityroles/)
1. [**Security Roles**](/applicationadmin/securityroles/)
1. [**System**](/applicationadmin/system/)

## Alerts

![ Display](/images/AppAdmin/Alerts.png)
Customized alerts can be setup to send an email to a specified email address. You can see what events are available to trigger alerts by clicking "**Add**".

## Feedback

![ Display](/images/AppAdmin/Feedback.png)
Every SPOON user can submit feedback by clicking on their Username in the top right corner of SPOON, then clicking "Contact Us". That feedback appears on this sub-page.

{{% notice note %}}
Warning! Do note that entries are by default displayed by Create Date ascending, meaning that the oldest tickets will appear at the top. Don't miss the new tickets that get generated.
{{% /notice %}}

## Jobs

![Display](/images/AppAdmin/Jobs.png)

1. Jobs are tasks that SPOON performs in the background, at pre-set time intervals. None of the jobs are system-critical, so it is safe to pause all of them at the same time (as of SPOON v2.11). "**Run Job**" triggers the job to run immediately; you can see an instance of the job run in the "**Tasks**" sub-page (click the tab).

2. Jobs that are actively running appear in this sub-page. However, since most jobs run quickly, the Admin may not see anything in the grid.

In SPOON v2.11, there is an option near the top to "**Show Integration Jobs**", Yes or No. SPOON at one point in the past supported integration with a programming tool named JIRA. However, support for JIRA as of v2.11 has been discontinued, and so there will not be any integration jobs that will appear in this page.

## Messages

![ Display](/images/AppAdmin/Message.png)

1. User messages are queued messages from users. The primary usage for messages is from watches. This tool allows for viewing of queued messages as well as viewing of archived messages.

2. Event Notifications. Notifications are messages sent internally to user to notify them of event in the application. You can also see these notifications by clicking "**Notifications**" in the top right corner of the screen. The "**Create Admin Message**" allows you to make a notification that can be visible to all SPOON users; the "Send to" input box searches according to the recorded First, Last Name, or Email address of a user - NOT according to the user name. If you enter a user's username into the "Send to" box you will not find who you are looking for.

## Tracking

![ Display](/images/AppAdmin/Tracking.png)

1. User Tracking. The Admin can see every person that has logged in within a specified time range, see what browsers, environments and locals SPOON users are using. The Admin can even export the data table as a .csv file.

2. Entry. The Admin can also observe which parts are receiving the most attention by users, by seeing on this page which parts users are viewing. There are two kinds of Event Types, "View", where it recorded that a user looked at the profile of a Component, and "External LInk Click", which records when the user clicks on a resource (such as a .pdf file or image) or external link. This data will also export to a .csv file.

## User Management

![ Display](/images/AppAdmin/UserManagement.png)
As of SPOON v2.11, when a user attempts to register for an account with SPOON, their details are sent to SPOON's database as a "registration" for an account, but not a full account until the user has responded to the email that is automatically sent to the user's email account. To see all individuals that count as a registrant, click the "**Registrations**" tab. Once users have used the registration code that is mailed to them, their account is upgraded to full account status and the Admin will be able to search from them specifically from this sub-page. The Admin will able to assign Security Roles to the users (see the Roles page for more on that).

Using the "**Message**" button on the screen sends an email from the support@SPOONsite email address (depending on how the server's configurations are setup).

{{% notice note %}}
It is a known issue that SPOON's emails seem to be particularly susceptible to being caught in spam filters, so it is a frequent occurrence for attempted registrants of SPOON to contact the administrator that they are not being sent the confirmation code. It is first best to ask them to check their spam and then if they are still not able to find it, to register them manually. Go to the Registrations tab, then click "**Add**". Be careful entering in the registrant's data in manually, as the verification for a new user on from this side is less stringent then the verification on the public-facing signup. Email the registrant his new username and password from an email account that is not the main SPOON support email address and verify the registrant's reception of the email via other means (phone perhaps).
{{% /notice %}}

Be aware that part of the functionality of this page can also be found at the [User Data > User Profiles page](/applicationadmin/userdata/#UserProfiles)

## Reports

Reports are automatically generated statistics of SPOON. They can be scheduled to run once, daily, weekly, monthly, or a custom schedule based on a [cron expression](https://www.freeformatter.com/cron-expression-generator-quartz.html). These reports can be run on entries, users, or SPOON usage. To run a report go to the Application Management button and select reports. Then click the add button, a popup will appear, which you can then choose which type of report you would like to run.

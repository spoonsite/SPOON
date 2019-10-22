+++
title = "App Management"
description = ""
weight = 2
+++
 
There are numerous tools available to manage the Application itself, this guide covers most of those. 

[__What do I do if user claim they are not receiving the account confirmation email?__](/applicationadmin/applicationmanagement/#UserManagement)   \
[__How can I be notified when a user puts in a change request on a part?__](/applicationadmin/applicationmanagement/#Alerts)   \
[__How can I be notified when a user makes a comment on a part?__](/applicationadmin/applicationmanagement/#Alerts)   \
[__How can I be notified when a user submits a new part?__](/applicationadmin/applicationmanagement/#Alerts)   \
[__How can I be notified when there is a system error?__](/applicationadmin/applicationmanagement/#Alerts)   \
[__How can I be notified when a user makes a new tag, review, or attribute code?__](/applicationadmin/applicationmanagement/#Alerts)   \
[__How can I be notified when a new user registers for a Spoon account?__](/applicationadmin/applicationmanagement/#Alerts)   \
[__Where can I see user feedback?__](/applicationadmin/applicationmanagement/#Feedback)   \
[__How do I pause Spoon's automatic tasks?__](/applicationadmin/applicationmanagement/#Jobs)   \
[__What processes does Spoon run in the background and when? Can I control them?__](/applicationadmin/applicationmanagement/#Jobs)   \
[__What are Jobs on Spoon?__](/applicationadmin/applicationmanagement/#Alerts)   \
[__Who is on Spoon at this particular moment, and what are they looking at?__](/applicationadmin/applicationmanagement/#Tracking)   \
[__How do I control user privileges on Spoon?__](/applicationadmin/applicationmanagement/#Alerts)   \
[__How can I be notified when a user puts in a change request on a part?__](/applicationadmin/applicationmanagement/#Alerts)   \

 <!--more-->

 ![Diagram of main System sub-page](/images/AppAdmin/AppManageMapping.png)
 (If you are wondering, where is the documentation for the rest of the options? Check the rest of this site, likely the option you looking for is covered in it's own separate page.)

<a name="Alerts"></a>

### 1 - Alerts
![ Display](/images/AppAdmin/Alerts.png)
Customized alerts can be setup to send an email to a specified email address. You can see what events are available to trigger alerts by clicking "__Add__". 


<a name="Feedback"></a>

### 2 - Feedback
![ Display](/images/AppAdmin/Feedback.png)
Every Spoon user can submit feedback by clicking on their Username in the top right corner of Spoon, then clicking "Contact Us". That feedback appears on this sub-page. 

Warning! Do note that entries are by default displayed by Create Date ascending, meaning that the oldest tickets will appear at the top. Don't miss the new tickets that get generated. 


<a name="Jobs"></a>

### 3 - Jobs
![Display](/images/AppAdmin/Jobs.png)

1. Jobs are tasks that Spoon performs in the background, at pre-set time intervals. None of the jobs are system-critical, so it is safe to pause all of them at the same time (as of Spoon v2.11). "__Run Job__" triggers the job to run immediately; you can see an instance of the job run in the "__Tasks__" sub-page (click the tab).

2. Jobs that are actively running appear in this sub-page. However, since most jobs run quickly, the Admin may not see anything in the grid.

In Spoon v2.11, there is an option near the top to "__Show Integration Jobs__", Yes or No. Spoon at one point in the past supported integration with a programming tool named JIRA. However, support for JIRA as of v2.11 has been discontinued, and so there will not be any integration jobs that will appear in this page. 


<a name="Messages"></a>

### 4 - Messages
![ Display](/images/AppAdmin/Message.png)

1.  User messages are queued messages from users. The primary usage for messages is from watches. This tool allows for viewing of queued messages as well as viewing of archived messages.

2. Event Notifications. Notifications are messages sent internally to user to notify them of event in the application. You can also see these notifications by clicking "__Notifications__" in the top right corner of the screen. The "__Create Admin MEssage__" allows you to make a notification that can be visible to all Spoon users; the "Send to" input box searches according to the recorded First, Last Name, or Email address of a user - NOT according to the user name. If you enter a user's username into the "Send to" box you will not find who you are looking for. 




<a name="Tracking"></a>

### 5 - Tracking
![ Display](/images/AppAdmin/Tracking.png)

1. User Tracking. The Admin can see every person that has logged in within a specified time range, see what browsers, environments and locals Spoon users are using. The Admin can even export the data table as a .csv file.

2. Entry. The Admin can also observe which parts are receiving the most attention by users, by seeing on this page which parts users are viewing. There are two kinds of Event Types, "View", where it recorded that a user looked at the profile of a Component, and "External LInk Click", which records when the user clicks on a resource (such as a .pdf file or image) or external link. This data will also export to a .csv file.
<a name="UserManagement"></a>

### 6 - User Management
![ Display](/images/AppAdmin/UserManagement.png)
As of Spoon v2.11, when a user attempts to register for an account with Spoon, their details are sent to Spoon's database as a "registration" for an account, but not a full account until the user has responded to the email that is automatically sent to the user's email account. To see all individuals that count as a registrant, click the "__Registstrations__" tab. Once users have used the registration code that is mailed to them, their account is upgraded to full account status and the Admin will be able to search from them specifically from this sub-page. The Admin will able to assign Security Roles to the users (see the Roles page for more on that). 

Using the "__Message__" button on the screen sends an email from the support@spoonsite email address (depending on how the server's configurations are setup). 

WARNING! As of v2.11 of Spoon, it is a known issue that Spoon's emails seem to be particularly susceptible to being caught in spam filters, so it is a frequent occurrence for attempted registrants of Spoon to log complaints that they are not being sent the confirmation code. In these instances it is best to manually create a new account for these individuals. Go to the Registrations tab, then click "__Add__". Be careful entering in the registrant's data in manually, as the verifications for a new user on from this side is less stringent then the verification on the public-facing signup. Email the registrant his new username and password from an email account that is not the main spoon support email address and verify the registrant's reception of the email via other means (phone perhaps).


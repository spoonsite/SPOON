+++
title = "Data Management"
description = ""
weight = 2
+++
 
 The definitive guide on managing videos, home page widgets, some security options, part relationships and more data on Spoon. 

[__Where can I change what shows on the front page carousel?__](/applicationadmin/datamanagement/#Highlights)   \

 <!--more-->

 ![Diagram of main System sub-page](/images/AppAdmin/DataManagementMapping.png)

<a name="HelpFrequentlyAskedQuestions"></a>

## 1 - Help > Frequently Asked Questions
In previous versions of Spoon, the "__Help__" button in the top right site menu would bring up a pop-up that had a list of Frequently Asked Questions. In v2.11, that button now redirects the user to the [spoonsite.github.io](https://spoonsite.github.io/) site. Therefore this page is currently obsolete.

## 2 - Help > Support Media
In the top right site menu there is a "__Tutorials__" button that shows the list of video tutorials uploaded onto Spoon. It is also obsolete.

<a name="Highlights"></a>

## 3 - Highlights
"Highlights" is the name of Spoon's carousel home-page widget. Here you can set is on that carousel, and in what order the slides are displayed. 

<a name="Lookups"></a>

## 4 - Lookups
Spoon was designed to be very customizable for the Admin user, and Lookups can be a great tool when the Admin needs to create new states and standards that were not in the original design of Spoon. 

The list of Editable Tables as of v2.11:
 
 TABLE | EXPLANATION
-----------------: | ----------- 
ResourceType | 
ReviewPro | 
RelationshipType | 
OrganizationType | The list of different kinds of organizations that organizations can be identified as.
WorkPlanSubStatusType | Entries that have been submitted for review to be published in Spoon go through the Workplan Progress progression. To progress an entry from one stage to the next in the Workplan, the user authorized to move the entry at that step (whether that be Internal Reviewers, SME Reviewers, etc) can instead choose to attach a status to that entry that marks it as being unacceptable for progression. For instance, if something ambiguous was found in the entry description, the reviewer could mark the entry as "Vendor Clarification Needed (Code: VENCLAR)". The list of statuses that the reviewer could choose to mark the entry from is set here.
UserTypeCode |  The list of different kinds of organizations that users can choose to identify themselves as being a part of. When a new user goes to register for an account, they must choose from a drop-down options list of organizations types one of which they must declare themselves as being a part of. That drop-down list is from here.
SecurityMarkingType | Spoon has the ability to designate parts as belonging to a specific level of security. For instance, one Spoon entry can be marked as being "Unclassified (Code: UNL)", while another can be marked as being "Top Secret (TSC)". Most parts have no security designations at all. This is the list of possible security markings on Spoon.
EvaluationSection | [Evaluations](../evaluations/) on Spoon site can have sections of different types, and here is where that list of possible evaluation sections is set.
ReviewCon | [Evaluations](../evaluations/) on Spoon make use of this list when generating Evaluation templates.
WorkflowStatus | [Workplans](../workplans) use this. No more information about this can be documented at this time.
DataSource | 
Contact Type | [Contacts](../userdata/#Contacts) can be of a certain type, this is where those types are set. 
FaqCategoryType | [Help > Questions](#HelpFrequentlyAskedQuestionis) can have different categories of questions. This is where they are set. 
ExperienceTimeType | User who are creating [Entry Reviews](../userdata/#UserDataReviews) must declare how much time they've spend with the product in question. That list can be set here. 
RecommendationType | [Evaluations](../evaluations/) use this. No more information about this can be documented at this time.
DataSensitivity | Similar to the SecurityMarkingType, the Data Sensitivity is a property of Spoon entries that delimits what level of authority is necessary to see the entry. This is the list of Data Sensitivity levels, any entry given any one of the Data Sensitivity levels will become invisible to a default user. The Admin can set who can see the Data Sensitivity levels defined in this list by editing the [Security Roles](../SecurityRoles/#DefaultDataRestrictions).

<a name="Media"></a>

## 5 - Media
This page lists all the video and images that are used by the site. Note that the "__Copy Link__" only copies the back half of the url to get to see an image, so you will need to prepend the domain name yourself.

<a name="Relationships"></a>

## 6 - Relationships
![Relationships Screenshot](/images/AppAdmin/DataManagementRelationships.png)


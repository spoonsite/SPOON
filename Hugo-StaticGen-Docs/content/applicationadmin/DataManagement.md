+++
title = "Data Management"
description = ""
weight = 200
+++

![Diagram of main System sub-page](/images/AppAdmin/DataManagementMapping.png)

## Table of Contents

1. [Frequently Asked Question](/applicationadmin/datamanagement/#frequently-asked-questions)
1. [Support Media](/applicationadmin/datamanagement/#support-media)
1. [Highlights](/applicationadmin/datamanagement/#highlights)
1. [Lookups](/applicationadmin/datamanagement/#lookups)
1. [Media](/applicationadmin/datamanagement/#media)
1. [Relationships](/applicationadmin/datamanagement/#relationships)

### Frequently Asked Questions

This list of questions is accessible to each user and should hold a couple of common questions.

### Support Media

In the top right site menu there is a "**Tutorials**" button that shows the list of video tutorials uploaded onto SPOON. All this information is provided externally, on this site ([spoonsite.github.io](https://spoonsite.github.io/))

### Highlights

"Highlights" is the name of SPOON's carousel home-page widget. Here you can set is on that carousel and in which order the slides are displayed.

### Lookups

SPOON was designed to be very customizable for the Admin user, and **Lookups** can be a great tool when the Admin needs to create new states and standards that were not in the original design of SPOON.

Explanations of each table:

TABLE | EXPLANATION
-----------------: | -----------
ResourceType | Not used in SPOON
ReviewPro | Not used in SPOON
RelationshipType | How parts can be related to each other (component of, included in, depends on, etc.)
OrganizationType | The list of different kinds of organizations that organizations can be identified as
WorkPlanSubStatusType | Statuses that appear on the Workplan Progress Page (Vendor contacted, Waiting for response, ect.)
UserTypeCode | The list of different kinds of organizations that users can select during registration. When a new user registers for an account, they must choose from a drop-down list of organization types, one of which they must declare themselves as being a part of. That drop-down list is specified here
SecurityMarkingType | SPOON has the ability to designate parts as belonging to a specific level of security. For instance, one SPOON entry can be marked as being “Unclassified (Code: UNL)”, while another can be marked as being “Top Secret (TSC)”. Most parts have no security designations at all. This is the list of possible security markings on SPOON
EvaluationSection | [Evaluations](/applicationadmin/evaluations/) on SPOON site can have sections of different types, and here is where that list of possible evaluation sections is set
ReviewCon | [Evaluations](/applicationadmin/evaluations/) on SPOON make use of this list when generating Evaluation templates
WorkflowStatus | [Workplans](/applicationadmin/workplans) use this. No more information about this can be documented at this time
DataSource |
Contact Type | [Contacts](/applicationadmin/userdata/#contacts) can be of a certain type, this is where those types are set
FaqCategoryType | [Help > Questions](#frequently-asked-questions) can have different categories of questions. This is where they are set
ExperienceTimeType | Users who are creating [Entry Reviews](/applicationadmin/userdata/#UserDataReviews) must declare how much time they've spend with the product in question. That list can be set here
RecommendationType | [Evaluations](/applicationadmin/evaluations/) use this. No more information about this can be documented at this time
DataSensitivity | Similar to the SecurityMarkingType, the Data Sensitivity is a property of SPOON entries that delimits what level of authority is necessary to see the entry. This is the list of Data Sensitivity levels, any entry given any one of the Data Sensitivity levels will become invisible to a default user. The Admin can set who can see the Data Sensitivity levels defined in this list by editing the [Security Roles](/applicationadmin/securityroles/#default-data-restrictions)

### Media

This page lists all the video and images that are used by the site. Note that the **Copy Link** only copies the back half of the URL to get to see an image, so you will need to prepend the domain name yourself.

### Relationships

![Relationships Screenshot](/images/AppAdmin/DataManagementRelationships.png)

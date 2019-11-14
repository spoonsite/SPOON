+++
title = "Attributes and Entry Types"
description = ""
weight = 2
+++
 
 The definitive guide on Spoon's Attributes, a unique part of the site that grants a great deal of utility. 

[__How to add a new Spoon entry category-__](/applicationadmin/attributes/#Attributes)   \

 <!--more-->

One of Spoon's most useful utilities to the Small Satellite community is the ability to hold Satellite part's scientific measurements in a machine-friendly way. In the future users might be able to easily manipulate Satellite part data from a publicly accessible API, and the fact that part attributes are stored in a machine-friendly will make it possible for user-made programs to easily work with part data gained from Spoon. This page will explore all of the Admin Tools related to organizing the data related to Entry Attributes and Entry Types. 


![AttributesEtcMapping](/images/AppAdmin/AttributesEtcMapping.png)

<a name="Attributes"></a>

## 1 - Attributes

![Attributes Page](/images/AppAdmin/Attributes.png)

An "attribute" on Spoon is usually a scientific measurement of a Spoon entry, such as the Height, Width, and Length of a part. 

If you want to add a new attribute, or edit an existing one, click "__Add New Type__" or "__Edit Attribute__" respectively, and a popup with options will appear.

![Screenshot of the Edit popup](/images/AppAdmin/AttributesEditAttributesPopup.png)

The Options in that Popup | EXPLANATION 
-----------------: | ----------- 
Label | The label is the name of the attribute that a user would actually see. 
Type Code | The TypeCode is the attribute ID that is used internally to keep track of this attribute, as such this should be unique. Any alphabetical character you enter will be converted to upper case, so as such it doesn't matter if you put in a code in all lowercase, when it is entered into the database it is converted to all uppercase. Numbers are acceptable. By convention the Type Code is the same as the label except for when special characters or excessive length in the label necessitate slight difference.  
Default Code | An attribute '__Code__' is a value for the attribute, for instance a __Code__ for a 'height' attribute could be 46, or also a __Code__ for an 'TRL' attribute could be 'Level 9'. 
Detailed Description | Any information entered here will be available to the user when they go to add this attribute to their part entry, by way of a tooltip:  <i class="fa fa-question-circle" data-qtip="This is a tooltip."></i>
Code Value Type | Choose whether this is a number or a Text-like value. 
Default Unit | Enter a scientific measurement made up of base units (to see what codes to use, see the list here: [Katex Units](/user/advancedusertopics/unitlegend/)). 
Compatible Unit List | Users may not use the same unit that you do for this particular attribute. For instance you might make an attribute named Height and expect the user to enter the value in millimeters (mm), but for the convenience of the user you may allow alternative measurements (meters, inches, feet).
Flags | Visible: No information on this can be documented at this time. <br> Important: This may give precedence to this part when displaying the order of attributes and search results. <br> Allow Multiple: Permits a part to have more than one instance of this attribute. <br> Allow User-Created odes: Allows the users to set their own value for the attribute, for instance, set the 'Height' attribute to be a '8'. The only time you would want to leave this box unchecked is when you want to force the user to choose from a pre-made list of values, for instance set 'Inflatable' to be either 'True' or 'False', not permiting the user to enter 'Maybe' or 'Truu'.  <br> Hide on Submission: The attribute will be automatically added to the component without the user being able to change it. Hiding a required attribute requires a default code. Codes must be created before this flag can be set.


<a name="Entries"></a>

## 2 - Entries
The Admin can add or remove Spoon entries from this screen, but *only if the part in question has already been approved in the past*. Only approved parts show on this page, if you are looking for a part that has just recently been submitted and that has not gone through the approval process, you must look for that part in [Partial Submission](/applicationadmin/Attributes/#PartialSubmissions).

<a name="Entry Types"></a>

## 3 - Entry Types

![Entry Types Page Screenshot](/images/AppAdmin/EntryTypesPage.png)

Option | EXPLANATION 
-----------------: | -----------
Type Code | Internal ID for catagories, by default anything given will be converted to uppercase upon entry into the database and the convention for naming is an acronym of the category title. 
Label | Name of the category that the user will see.
Description | Description. There is no where on the site in v2.11 that displays this information to the user. 
Parent Type | Parent catagories can have multiple children.
 Assigned User(s) | 
 Assigned Group(S) |  Groups are a function of [Security roles](/applicationadmin/securityroles/); assigning a group to this catagories will ensure that the group has permission to see parts of this type ( to review them as they are going through the Workplan Progress).
 Entry Display Template | No information about this can be documented at this time. 
 Icon URL | No information about this can be documented at this time. 

<a name="SubmissionsFormTemplates"></a>

## 4 - Submissions Form Templates
The Submissions Forms Templating systems was created to prepare for the eventuality that users submitting different parts would need to see a different screen. That vision was never fully realized, and in v2.11, the Vendor Form (Default) will be the submission template no matter what, *even if you inactivate the Vendor Form (Default) on the front end*.

The Admin can still make edits to Vendor Form (Default).

<a name="Partial Submissions"></a>

## 5 - Partial Submissions

All of the unfinished, incomplete Entry submissions that users are in process of uploaded to Spoon appear here. Any part that is seen in the Workplan Progress page will also be found here. 

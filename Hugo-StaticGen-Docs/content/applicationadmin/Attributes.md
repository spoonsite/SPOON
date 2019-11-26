+++
title = "Attributes and Entry Types"
description = ""
weight = 210
+++

![AttributesEtcMapping](/images/AppAdmin/AttributesEtcMapping.png)

1. [Attributes](/applicationadmin/attributes/#attributes)
2. [Entries](/applicationadmin/attributes/#entries)
3. [Entry Types](/applicationadmin/attributes/#entry-types)
4. [Submission Form Templates](/applicationadmin/attributes/#submission-form-templates)
5. [Partial Submissions](/applicationadmin/attributes/#partial-submissions)

## Attributes

![Attributes Page](/images/AppAdmin/Attributes.png)

An "attribute" on Spoon is usually a scientific measurement of a Spoon entry, for example height, width, or length of a part.

If you want to add a new attribute or edit an existing one, click "**Add New Type**" or "**Edit Attribute**" respectively, and a popup with options will appear.

![Screenshot of the Edit popup](/images/AppAdmin/AttributesEditAttributesPopup.png)

The Options in that Popup | EXPLANATION
-----------------: | -----------
Label | The label is the name of the attribute that a user would actually see
Type Code | The TypeCode is the attribute ID that is used internally to keep track of this attribute, as such this should be unique. Any alphabetical character you enter will be converted to upper case, so as such it doesn't matter if you put in a code in all lowercase, when it is entered into the database it is converted to all uppercase. Numbers are acceptable. By convention the Type Code is the same as the label except for when special characters or excessive length in the label necessitate slight difference
Default Code | An attribute '__Code__' is a value for the attribute, for instance a __Code__ for a 'height' attribute could be 46, or also a __Code__ for an 'TRL' attribute could be 'Level 9'
Detailed Description | Any information entered here will be available to the user when they go to add this attribute to their part entry, by way of a tooltip:  <i class="fa fa-question-circle" data-qtip="This is a tooltip."></i>
Code Value Type | Choose whether this is a number or a Text-like value
Default Unit | Enter a scientific measurement made up of base units (to see what codes to use, see the list here: [Katex Units](/user/unitlegend/))
Compatible Unit List | Users may not use the same unit that you do for this particular attribute. For instance you might make an attribute named Height and expect the user to enter the value in millimeters (mm), but for the convenience of the user you may allow alternative measurements (meters, inches, feet)
__Flags__ |
Visible | No information on this can be documented at this time
Important | This may give precedence to this part when displaying the order of attributes and search results
Allow Multiple | Permits a part to have more than one instance of this attribute
Allow User-Created codes | Allows the users to set their own value for the attribute, for instance, set the 'Height' attribute to be a '8'. The only time you would want to leave this box unchecked is when you want to force the user to choose from a pre-made list of values, for instance set 'Inflatable' to be either 'True' or 'False', not permitting the user to enter 'Maybe' or 'Truu'
Hide on Submission | The attribute will be automatically added to the component without the user being able to change it. Hiding a required attribute requires a default code. Codes must be created before this flag can be set

## Entries

{{% notice note %}}
An entry is a part that is added to the database.
{{% /notice %}}

The Administrator can manage all entries on this page. Admins have to ability to create, edit, delete, and toggle entries. Some user submissions may not appear in this screen, so you will have to go to the Data Management -> Submissions -> Partial Submissions page.

## Entry Types

![Entry Types Page Screenshot](/images/AppAdmin/EntryTypesPage.png)

Option | EXPLANATION
-----------------: | -----------
Type Code | Internal ID for catagories, by default anything given will be converted to uppercase upon entry into the database and the convention for naming is an acronym of the category title
Label | Name of the category that the user will see
Description | The description of the entry type
Parent Type | Parent catagories can have multiple children
Assigned User(s) | Single users that are in charge of reviewing that parts of that category
Assigned Group(S) |  Groups are a function of [Security roles](/applicationadmin/securityroles/); assigning a group to this catagories will ensure that the group has permission to see parts of this type ( to review them as they are going through the Workplan Progress)
Entry Display Template | This feature was deprecated in V2.11. It allowed administrators to create custom templates, for the search results page, for each entry type
Icon URL | The url for the icon that will be shown for that entry type on the search results page

## Submission Form Templates

{{% notice warning %}}
This feature will be deprecated in V2.12
{{% /notice %}}

The submission form templates are a way for the administrator to have different submission forms for different entry types. Currently, this feature is used to make changes to the submission form, but all entry types use the same submission form.

## Partial Submissions

All of the incomplete entry submissions that users are in process of uploaded to Spoon appear here. Any part that is seen in the Workplan Progress page will also be found here.

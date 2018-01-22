+++
title = "Evaluators Guide"
description = ""
weight = 10
+++

## TL;DR
* Administrators create and publish evaluations. Evaluators fill out evaluations.
* While editing an evaluation you will not see exactly what the user sees. The user sees things bundled under a Details tab and an Evaluation tab. In an evaluation everything under the Entry section applies to that Details tab while everything under the Sections and Checklist sections apply to that Evaluations tab.
* Storefront assumes you know what all of the various questions mean, what the asset types available to you mean, and how your implementation of Storefront works. If you don't, ask your administrator.

## Overview
In this guide we will go over how to correctly add information to an evaluation inside of the Storefront. All of the information in this guide has been developed for version 2.5 (2017-07-26 15:03:18 MDT). The general process of doing evaluations will be the same for previous versions but some of the specifics may be different.

Entries that have been added to storefront can be evaluated. Entries can be added via user submission from the User Tools area or directly added by an administrator. Go [here](/openstorefront/user/quick-overview-of-submitting-a-new-component/) for a quick overview of how to submit a new entry.

Any entry can have multiple evaluations and each evaluation will be tracked separately. This allows for multiple versions of a software package or service to be evaluated based on the same entry. Each evaluation is based on a template defined in advance by the administrator and the template determines everything from the look and feel of the evaluation to the questions that will be asked.

## Who is this for?
This guide is for anyone who will be performing an evaluation on the Storefront application. We will cover a general work flow and use that to discuss intent and best practices. Please note that the exact work flow for any given evaluation will depend on the template used to create that evaluation. Thus you will need to contact your administrator for any questions related to your particular work flow.

Storefront assumes that people doing the evaluations have familiarity with the system and, more importantly, with how your organization uses your data.

## Before you begin
Make sure your administrator has created an evaluation for you to use and that you are part of the STOREFRONT-Evaluators group (or equivalent).

For our example below we created templates based on the information [here](/openstorefront/evaluations/librarian/basic-entry-template-for-storefront/).

## General Process  
For this demo we will be doing an evaluation of a fictional repository called the [Contoso](https://en.wikipedia.org/wiki/Contoso) Repository (name taken from examples from Microsoft). An entry for the repository has been approved and added to Storefront, and an empty evaluation (based on the above template) has been created. As the evaluation has not been published it doesn't show up on Storefront but the entry it is based on does.

![Repository Details Pre Evaluation](/openstorefront/images/Screenshots/Repository%20Details%20Pre%20Evaluation.png)

To start an evaluation, click on your user name in the upper right corner of the page and select __Evaluation Tools__. From the __Evaluation Tools Dashboard__ click the __Evaluations__ button. By default, the list will only show you evaluations that you have been assigned to. In our example, the evaluation was assigned to the general __STOREFRONT-Evaluators__ group and not to any particular user. Therefore, to get the evaluation to show up, change __Assigned User__ to __All__ and change __Assign to Group__ to __STOREFRONT-Evaluators__.

![Locating Evaluation](/openstorefront/images/Screenshots/Locating%20Evaluation.png)

It is best practice to assign yourself to any evaluation you are working on. This helps you to find it later and helps to signify which evaluations have been started and which have not. To do so, first select the evaluation from the list and click on the __Assign User__ button and pick yourself from the window that pops up before clicking __Save__.  

To start working on an evaluation select it from the list and click on the __Edit__ button. Evaluations will always open up to the __Evaluations Info__ section.

![Evaluations Info Page](/openstorefront/images/Screenshots/Evaluations%20Info%20Page.png)

On the left hand side of the window you will see blue boxes which will take you to all of the available sections of the evaluation. The center of the window shows details for the currently selected section. On the right of the window is an expandable section for comments. Comments can be added to almost any section of an evaluation, but the comments will not be visible on the final published evaluation. The intention of the comments is for review, approval, communication, etc. amongst the evaluation team.  

The exact set of sections for any particular evaluation is dependent on how your system has been configured and how the templates were set up. That said, all evaluations will, generally, have at least the sections shown above. We will now talk about each of these sections, although the order presented does not imply that an evaluation should be done in this order. It is expected that an evaluation is going to be an iterative and non-linear process.

The difference between an entry and an evaluation needs to be emphasized. While performing an evaluation you will have the opportunity to correct and update information about the entry as well as the evaluation. One way of thinking about the difference is to look at what happens once an evaluation is published. All information related to an entry will show up in the Details tab. All information relating to a particular evaluation will be under the Evaluations tab. When looking at editing an evaluation, *everything under the Entry header applies to that Details tab while everything under the Sections and Checklist headers apply to that Evaluations tab.*

### Evaluation

  1. Info
      ![Evaluation Info](/openstorefront/images/Screenshots/Evaluations%20Info%20Page%202.png)

      This section gives you a high level status about the evaluation itself. Here you can change the version of the evaluation. How to best use the Version of an evaluation will be dependent on your particular implementation.

      ![Evaluation Status](/openstorefront/images/Screenshots/Evaluation%20Status.png)

      The Status of an evaluation can be Open, In-Progress, Waiting for more information, Hold or Complete. It is best practice to update the status of an evaluation here whenever it changes. However, the status on this page, or any other page of the evaluation, is for the benefit of the evaluator and does not impact if or how an evaluation can be published by an administrator. Also note that even if an evaluation is marked as Complete that it will NOT be published into the Storefront search results. An evaluation will only be so published if an administrator publishes an evaluation.

      Changes to the elements on this page are automatically saved when the changed field loses focus.

  2. Review

      ![Evaluation Review Section](/openstorefront/images/Screenshots/Review%20Pane.png)

      All of the comments people make on your evaluation are listed here. If desired, you can mark any particular comment as acknowledged, or edit/delete any comment, or even reply to comments. None of these comments will show up in the final published evaluation.

### Entry

  1. Summary

      ![Entry Summary](/openstorefront/images/Screenshots/Entry%20Summary.png)

      This summary is for the entry. Here we want to describe what the entry is, what it does, how it does it, and any other info to help show why people might be interested in it. This should be an objective condensation of the facts of the entry and not just a copy and paste of the marketing materials. It is also important to make sure that only publicly available material is added to any of these entries. Finally, it is important to note that this information is NOT related to any particular evaluation.

      If possible, add information about when the latest release happened and what version that release was.

  2. Attributes

      ![Entry Attributes](/openstorefront/images/Screenshots/Entry%20Attributes.png)

      Attributes are used to categorize an entry, build category relationships, and improve searching. Available attribute types and codes are setup by an administrator and are controlled, in part, by the template used to create the evaluation and the entry type. Each entry should have as many attributes added to it as make sense for that entry. It is important that attributes are carefully curated for each entry. Some attribute types can be added only once, while others can be added multiple times (the expectation is that each entry would have a different attribute code).

      The best practice is to use elements from the drop downs on the attribue code or stick to a pre-determined list of entries for free-form fields. Other information is better placed into the Contacts, Resources, Media, or Tags sections. In other words, semantics for the attribute codes must be determined ahead of time and you need to refer to those external semantics when determining which attribute to add.

      To add a new attribute, select the appropriate attribute type from the list and then select or fill out the attribute code and click save.

      Ultimately, attributes become Entry Vitals on the Details tab for an entry. Here is an example:

      ![Entry Details](/openstorefront/images/Screenshots/Entry%20Details%20-%20Attributes%20to%20Vitals.png)

  3. Relationships

      ![Entry Relationships](/openstorefront/images/Screenshots/Entry%20Relationships.png)

      Relationships show how various entries are related to one another. This helps with discovery of entries while searching. A thorough knowledge of existing entries is usually needed to add good entries here, but can be useful if done correctly.

  4. Contacts

      ![Entry Contacts](/openstorefront/images/Screenshots/Entry%20Contacts.png)

      This section lets you manage all of the people related to the entry. If an entry was user submitted, then their contact will already be present. If possible, you should always have a technical or vendor Point Of Contact. Add other contacts here as appropriate and available.

      Generally, when you have the option of having an item added as an attribute or as a contact, it is better to add it as a contact.

      Ultimately, contacts become Points of Contact on the Details tab for an entry. Here is an example:

      ![Entry Details](/openstorefront/images/Screenshots/Entry%20Details%20-%20Contacts%20to%20POC.png)

  5. Resources

     ![Entry Resources](/openstorefront/images/Screenshots/Entry%20Resources.png)

      The resources section is for managing links, documentation, etc. pertaining to the entry. Various specific resource types will be available, depending on the configuration of your system. At a minimum, you should ensure that there is a link to the homepage of the entry. Relevant documentation not otherwise available on the web, but still marked for distribution, can be uploaded here as well.

      Generally, when you have the option of having an item added as an attribute or as a resource, it is better to add it as a resource.

      Ultimately, resources become Location of Entry Artifacts on the Details tab for an entry. Here is an example:

      ![Entry Details](/openstorefront/images/Screenshots/Entry%20Details%20-%20Resources%20to%20Artifacts.png)

  6. Media

      ![Entry Media](/openstorefront/images/Screenshots/Entry%20Media.png)

      The media section is for adding media specific to the entry in general. The media for an evaluation is different from the media for an entry. Media added from other sections in the Entry set of sections will show up to be managed here.

      This is where you should add screenshots and videos related to the entry. Videos should be in mp4 format as they can be shown directly in the site. Other video formats will need to be download by the users.  

      Youtube or external videos should be added in the "Resource" section and not here. This sections is for direct links to the media.

      Note: Media added to the Entry Summary section should typically be hidden from the carousel by checking the appropriate box for that media.

  7. Tags

      ![Entry Tags](/openstorefront/images/Screenshots/Entry%20Tags.png)

      Anyone can add tags to an entry at any time. This screen allows an evaluator to add new tags or manage existing tags. Tags are searchable and filterable on Storefront, so it is important that tags are properly curated.

### Section

  1. Repo Overview (An basic example section)

      ![Repo Overview](/openstorefront/images/Screenshots/Repo%20Overview.png)

      This is an example of a very basic custom section. Some templates will include a variety of sub-sections or other custom formatting. Generally, the sections allow you to focus on different aspects of the evaluation as determined by your implementation.

      The text box is a rich text box which means it supports formatting, images, tables, equations, code snippets, etc. The top of the text box has tools that you can use but it also supports the normal keyboard shortcuts (like __Ctrl + B__ for bold). If you make a change and click off to a different section your changes will be lost! If you want to save your changes you can either click the save button at the bottom of the text box or change the status at the bottom of the window. However, your changes should auto-save after a timeout which is set to take place a minute or so after the last made change.

      Media (pictures, videos, etc.) that have been added to one of these sections can be managed from the "Manage Media" button in that section. This is for diagrams or screenshots that are part of the section and are discussed in that section. There is no general media collection for an evaluation as a whole.

  2. Private Notes (A more complex example section)

      ![Private Notes](/openstorefront/images/Screenshots/Private%20Notes.png)

      Here is an example of a more complex section template where the base section contains sub-sections. Again, changes made to any of the sub-sections will NOT be automatically saved. You must click the Save button at the bottom of the window to save your changes.

      It is usually safe to assume that anything added to an area labeled Private Notes will not be published with the final, user facing, evaluation.

### Checklist

  1. Summary

      ![Checklist Summary](/openstorefront/images/Screenshots/Checklist%20Summary.png)

      This summary is for the evaluation. After having investigated the entry, the summary should be filled out to summarize that investigation and the results thereof. The evaluator should put in their own words their experience of the evaluation. This would be the narration of part of a consumer report. It should summarize what was found in the checklist questions. (E.g. The asset was easily discoverable however, we found the documentation was lacking.) It is important to both remain objective and to realize that this is not just a regurgitation of the original entry summary. One way of thinking about it is that the entry summary tells you what the entry *should* be about, but this summary tells you what an entry was *actually* about.

      The Recommendations section is where you can list your recommendations for improvements about the entry. The intention is to communicate back to the source of the entry what you think should be done to make what they are doing better. These recommendations must be categorized into one of a set of options:

      ![Recommendation Options](/openstorefront/images/Screenshots/Recommendation%20Categories.png)

  2. All Questions

      ![All Questions](/openstorefront/images/Screenshots/All%20Questions.png)

      This section gives a sortable and modifiable listing of all of the questions associated with your evaluation. As every question can have a status assigned to it you can use this overview to quickly find those questions that haven't yet been answered or are on hold if you are coming back to continue working on an evaluation.

      To edit a question either click on it once to select it, then click Edit, or double click on the question. The window that comes up to answer that question will be similar to, but different from, the window that is available if you click on the question in the left hand sidebar. The biggest gap is that there is no way to display the scoring criteria from the pop up window. To get the scoring criteria you have to turn the column on in the table or click on the question in the sidebar.

  3. Discoverable (An example question)

      ![Checklist Question](/openstorefront/images/Screenshots/Checklist%20Question.png)

      Here is an example of a typical question. All questions are rated on a 1 to 5 rating scale with 5 being the best possible, 3 being normal and acceptable, and 1 being completely non-conforming. A typical question will have scoring criteria to help an evaluation be objective and consistent, an objective to help evaluators consider the same intent when looking at a question, the question itself, a free form response area, and private notes.

      It is best practice for every question to have an associated response explaining why a question was scored as it was. Even if the scoring criteria appears to be clear and unambiguous, there is always more to the story than what can be said with only one number. The response area is where you should tell that story. In terms of worth, the response is generally more valuable than the number assigned.

      Private notes are for use by the evaluator. Nothing in the private notes section will be visible on the final published evaluation. That said, the notes will be preserved for later reference.  

      NOTE: Changes will NOT be saved unless you click on the Save button, change the status, or wait for the auto-save.

------------

Once published, our sample entry looks like this:

Details

![Details Page for Contoso Repository](/openstorefront/images/Screenshots/Details%20Page%20for%20Contoso%20Repository.png)

![Entry Details](/openstorefront/images/Screenshots/Entry%20Details.png)

Evaluation

![Evaluation Page for Contoso Repository](/openstorefront/images/Screenshots/Evaluation%20Page%20for%20Contoso%20Repository.png)

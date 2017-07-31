# Storefront Guide for Evaluators

-----

## Overview
In this guide we will go over how to correctly add information to an evaluation inside of the Storefront. All of the information in this guide has been developed for version 2.5 (2017-07-26 15:03:18 MDT). The general process of doing evaluations will be the same for previous versions but some of the specific menu items and verbiage will be different. 

Entries that have been added to storefront, and been approved, can be evaluated. Entries can be added via user submission from the User Tools area and then approved by an administrator, or directly added by an administrator. Go [here](https://github.com/Aaron-Fine/openstorefront/blob/Storefront-Guide/docs/Quick%20Overview%20of%20Submitting%20a%20New%20Component.md) for a quick overview of how to submit a new entry.

Any entry can have multiple evaluations and each evaluation will be tracked separately. This allows for multiple versions of a software package or service to be evaluated based on the same entry. Each evaluation is based on a template defined in advance by the administrator and the template determines everything from the look and feel of the evaluation to the questions that will be asked. 

## Who is this for?
This guide is for anyone who will be performing an evaluation on the Storefront application. We will cover a general work flow and use that to discuss intent and best practices. Please note that the exact work flow for any given evaluation will depend on the template used to create that evaluation. Thus you will need to contact your administrator for any questions related to your particular work flow.

Storefront assumes that people doing the evaluations have familiarity with the system and, more importantly, with how your organization uses your data. 

## Before you begin
Make sure your administrator has created an evaluation for you to use and that you are part of the STOREFRONT-Evaluators group (or equivalent). 

For our example below we created templates based on the information [here](https://github.com/Aaron-Fine/openstorefront/blob/Storefront-Guide/docs/Basic%20Entry%20Template%20for%20Storefront.md).

## General process
For this demo we will be doing an evaluation of a fictional repository called the Contoso Repository (with apologies to Microsoft). An entry for the repository has been approved and added to Storefront, and an empty evaluation (based on the above template) has been created. As the evaluation has not been published it doesn't show up on Storefront but the entry it is based on does. 

![Repository Details Pre Evaluation](https://github.com/Aaron-Fine/openstorefront/blob/Storefront-Guide/docs/images/Screenshots/Repository%20Details%20Pre%20Evaluation.png)

To start an evaluation, click on your user name in the upper right corner of the page and select `Evaluation Tools`. From the `Evaluation Tools Dashboard` click the `Evaluations` button. By default, the list will only show you evaluations that you have been assigned to. In our example, the evaluation was assigned to the general `STOREFRONT-Evaluators` group and not to any particular user. Therefore, to get the evaluation to show up, change `Assigned User` to `All` and change `Assign to Group` to `STOREFRONT-Evaluators`.

![Locating Evaluation](https://github.com/Aaron-Fine/openstorefront/blob/Storefront-Guide/docs/images/Screenshots/Locating%20Evaluation.png)

It is best practice to assign yourself to any evaluation you are working on. This helps you to find it later and helps to signify which evaluations have been started and which have not. To do so, first select the evaluation from the list and click on the `Assign User` button and pick yourself from the window that pops up before clicking `Save`.  

To start working on an evaluation select it from the list and click on the `Edit` button. Evaluations will always open up with to the `Evaluations Info` page. 

![Evaluations Info Page](https://github.com/Aaron-Fine/openstorefront/blob/Storefront-Guide/docs/images/Screenshots/Evaluations%20Info%20Page.png)

On the left hand side of the window you will see blue boxes which will take you to all of the available sections of the evaluation. The center of the window shows details for the currently selected section. On the right of the window is an expandable section for comments. Comments can be added to any section of an evaluation but will not be visible on the final published evaluation. The intention of the comments is for review, approval, etc. amongst the evaluation team.  

The exact set of sections for any particular evaluation is dependent on how your system has been configured and how the templates were set up. That said, all evaluations will, generally, have at least the sections shown above. We will now talk about each of these sections although the order presented does not imply that an evaluation must be done in any particular order. It is expected that an evaluation is going to be an iterative and non-linear process.

The difference between an entry and an evaluation needs to be emphasized. While performing an evaluation you will have the opportunity to correct and update information about the entry as well as the evaluation. The easiest way of thinking about the difference is to look at what happens once an evaluation is published. All information related to an entry will show up in the Details tab. All information relating to a particular evaluation will be under the Evaluations tab. When looking at editing an evaluation, *everything under the Entry section applies to that Details tab while everything under the Sections and Checklist sections apply to that Evaluations tab.* 

**Evaluation**
  1. Info
      This is high level status about the evaluation itself. Here you can change the version of the evaluation which can be useful to signal that there are major changes to the evaluation. How to best use the Version of an evaluation will be dependent on your particular implementation. 
      
      ![Evaluation Status](https://github.com/Aaron-Fine/openstorefront/blob/Storefront-Guide/docs/images/Screenshots/Evaluation%20Status.png)
      
      The Status of an evaluation can be Open, In-Progress, Waiting for more information, Hold or Complete. It is best practice to update the status of an evaluation here whenever it changes. However, the status on this page, or any other page of the evaluation, is for the benefit of the evaluator and does not impact if or how an evaluation can be published by an administrator. Also note that even if an evaluation is marked as Complete that it will NOT be published into the Storefront search results. An evaluation will only be so published if an administrator publishes an evaluation. 
      
      Changes to the elements on this page are automatically saved when the changed field loses focus. 
      
  2. Review
  
      ![Review Pane](https://github.com/Aaron-Fine/openstorefront/blob/Storefront-Guide/docs/images/Screenshots/Review%20Pane.png)
      
      All of the comments people make on your evaluation are listed here. If desired you can mark any particular comment as acknowledged or edit/delete any comment or even reply to comments. None of these comments will show up in the final published evaluation. 
        
**Entry**
  1. Summary
  
      ![Entry Summary](https://github.com/Aaron-Fine/openstorefront/blob/Storefront-Guide/docs/images/Screenshots/Entry%20Summary.png)
      
      This summary is for the entry. Here we want to describe what the entry is, what it does, how it does it, and any other info to help show why people might be interested in it. This should be an objective condensation of the facts of the entry and not just a copy and paste of the marketing materials. It is also important to make sure that only publicly available material is added to any of these entries. Finally, it is important to note that this information is NOT related to any particular evaluation. 
      
      If possible, add information about when the latest release happened and what version that release was. In our case of doing an evaluation of a repository the best we could do would be to put in the date that the most recent entry was added.  
      
  2. Attributes
  
      ![Entry Attributes](https://github.com/Aaron-Fine/openstorefront/blob/Storefront-Guide/docs/images/Screenshots/Entry%20Attributes.png)
      
      Each entry should have as many attributes added to it as make sense for that entry. Storefront allows you to search and sort on these attributes so it is important that attributes are carefully curated for each entry. The available attributes will depend on how the templates were set up for your evaluation and entry type. Some attribute types can be added only once, while others can be added multiple times. 
      
      Care should be taken to only add properly sortable information to the attribute. The best practice is to use elements from the drop downs on the attribue code or stick to a pre-determined list of entries for free-form fields. Other information is better placed into the Contacts, Resources, Media, or Tags sections. In other words, semantics for the attributes must be determined ahead of time and are dependent on your implementation and you need to refer to those external semantics when determining which attribute to add. 
      
      To add a new attribute, select the appropriate attribute type from the list and then select or fill out the attribute code and click save. 
      
      Ultimately, attributes become Entry Vitals on the Details tab for an entry. Here is an example: 
      
      ![Entry Details](https://github.com/Aaron-Fine/openstorefront/blob/Storefront-Guide/docs/images/Screenshots/Entry%20Details.png)
      
  3. Relationships
  
      ![Entry Relationships](https://github.com/Aaron-Fine/openstorefront/blob/Storefront-Guide/docs/images/Screenshots/Entry%20Relationships.png)
      
      Relationships show how various entries are related to one another. This helps with discovery of entries while searching. A thorough knowledge of existing entries is usually needed to add good entries here, but can be very useful if done correctly. 
      
  4. Contacts
  
      ![Entry Contacts](https://github.com/Aaron-Fine/openstorefront/blob/Storefront-Guide/docs/images/Screenshots/Entry%20Contacts.png)
      
      This section lets you manage all of the people related to the entry. If an entry was user submitted, then their contact will already be present. If possible, you should always have a technical or vendor Point Of Contact. Add other contacts here as appropriate and available. 
      
      Generally, when you have the option of having an item added as an attribute or as a contact, it is better to add it as a contact. 
      
      Ultimately, contacts become Points of Contact on the Details tab for an entry. Here is an example: 
      
      ![Entry Details](https://github.com/Aaron-Fine/openstorefront/blob/Storefront-Guide/docs/images/Screenshots/Entry%20Details.png)
      
  5. Resources
     
     ![Entry Resources](https://github.com/Aaron-Fine/openstorefront/blob/Storefront-Guide/docs/images/Screenshots/Entry%20Resources.png)
     
      The best place to manage links, documentation, etc. pertaining to the entry is the Resources section. Various specific resource types will be available, depending on the configuration of your system. At a minimum, you should have links to the homepage of what it is that you are evaluating. Relevant documentation not otherwise available on the web but still marked for distribution can be uploaded to the storefront here as well. 
     
      Generally, when you have the option of having an item added as an attribute or as a resource, it is better to add it as a resource. 
      
      Ultimately, resources become Location of Entry Artifacts on the Details tab for an entry. Here is an example: 
      
      ![Entry Details](https://github.com/Aaron-Fine/openstorefront/blob/Storefront-Guide/docs/images/Screenshots/Entry%20Details.png)
      
  6. Media
      
      ![Entry Media](https://github.com/Aaron-Fine/openstorefront/blob/Storefront-Guide/docs/images/Screenshots/Entry%20Media.png)
      
      The Media tab is for adding media specific to the entry in general. The media for an evaluation is different from the media for an entry. Media added from other sections in the Entry set of sections will show up to be managed here. 
      
  7. Tags
      
      ![Entry Tags](https://github.com/Aaron-Fine/openstorefront/blob/Storefront-Guide/docs/images/Screenshots/Entry%20Tags.png)
      
      Anyone can add tags to an entry at any time. This screen allows an evaluator to add new tags or manage existing tags. Tags are searchable and filterable on Storefront, so it is important that tags are properly curated. 
      
**Section**
  1. Repo Overview (An example section) 
      
      ![Repo Overview](https://github.com/Aaron-Fine/openstorefront/blob/Storefront-Guide/docs/images/Screenshots/Repo%20Overview.png)
      
      This is an example of a very basic custom section. Some templates will include a variety of sub-sections or other custom formatting. Generally, the sections allow you to focus on different aspects of the evaluation as determined by your implementation. 
      
      The text box is a rich text box which means it supports formatting, images, tables, equations, code snippets, etc. The top of the text box has tools that you can use but it also supports the normal keyboard shortcuts (`Ctrl + B` for bold, etc.). Note that there is no auto save in place. If you make a change and click off to a different section your changes will be lost! If you want to save your changes you can either click the save button at the bottom of the text box or change the status at the bottom of the window.  
      
  2. Private Notes (A more complex example section)
      
      ![Private Notes](https://github.com/Aaron-Fine/openstorefront/blob/Storefront-Guide/docs/images/Screenshots/Private%20Notes.png)
      
      Here is an example of a more complex section template where the base section contains sub-sections. Again, changes made to any of the sub-sections will NOT be automatically saved. You must click the Save button at the bottom of the window to save your changes. 
        
**Checklist**
  1. Summary
      
      ![Checklist Summary](https://github.com/Aaron-Fine/openstorefront/blob/Storefront-Guide/docs/images/Screenshots/Checklist%20Summary.png)
      
      The checklist is supposed to be a guided examination of an entry in such a way as to allow results to be compared across entries. After having investigated the entry the summary should be filled out to summarize that investigation and the results thereof. It is important to both remain objective and to realize that this is not just a regurgitation of the original entry summary. One way of thinking about it is that the entry summary tells you what the entry *should* be about, but this summary tell you what an entry was *actually* about. 
      
      The Recommendations section is where you can list your recommendations for improvements about the entry. The intention is to communicate back to the source of the entry what you think should be done to make what they are doing better. These recommendations must be categorized into one of a selections of options: 
      
      ![Recommendation Options](https://github.com/Aaron-Fine/openstorefront/blob/Storefront-Guide/docs/images/Screenshots/Recommendation%20Categories.png)
      
  2. All Questions
      
      ![All Questions](https://github.com/Aaron-Fine/openstorefront/blob/Storefront-Guide/docs/images/Screenshots/All%20Questions.png)
      
      This section gives a sortable and modifiable listing of all of the questions associated with your evaluation. As every question can have a status assigned to it you can use this overview to quickly find those questions that haven't yet been answered or are on hold if you are coming back to continue working on an evaluation. 
      
      To edit a question either click on it once to select it, then click Edit, or double click on the question. The window that comes up to answer that question will be similar to, but different from, the window that is available if you click on the question in the left hand sidebar. The biggest gap is that there is no way to display the scoring criteria from the pop up window. To get the scoring criteria you have to turn the column on in the table or click on the question in the sidebar. 
      
  3. Discoverable (An example question)
      
      ![Checklist Question](https://github.com/Aaron-Fine/openstorefront/blob/Storefront-Guide/docs/images/Screenshots/Checklist%20Question.png)
      
      Here is an example of a typical question. All questions are rated on a 1 to 5 rating scale with 5 being the best possible, 3 being normal and acceptable, and 1 being completely non-conforming. A typical question will have scoring criteria to help an evaluation be objective and consistent, an objective to help evaluators consider the same intent when looking at a question, the question itself, a free form response area, and private notes. 
      
      It is best practice for every question to have an associated response explaining why a question was scored as it was. Even if the scoring criteria seem clear and unambiguous, there is always more to the story than what can be said with only one number. The response area is where you should tell that story. In terms of value, the response is generally more useful than the number assigned. 
      
      Private notes are for use by the evaluator. Nothing in the private notes section will be visible on the final published evaluation. That said, the notes will be preserved for later reference.  
      
      NOTE: Changes will NOT be saved unless you click on the Save button or change the status. 

Once published, our sample entry looks like this:

Details

![Details Page for Contoso Repository](https://github.com/Aaron-Fine/openstorefront/blob/Storefront-Guide/docs/images/Screenshots/Details%20Page%20for%20Contoso%20Repository.png)

![Entry Details](https://github.com/Aaron-Fine/openstorefront/blob/Storefront-Guide/docs/images/Screenshots/Entry%20Details.png)

Evaluation

![Evaluation Page for Contoso Repository](https://github.com/Aaron-Fine/openstorefront/blob/Storefront-Guide/docs/images/Screenshots/Evaluation%20Page%20for%20Contoso%20Repository.png)

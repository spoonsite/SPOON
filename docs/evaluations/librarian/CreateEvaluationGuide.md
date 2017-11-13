# Guide to Creating an Evaluation (Librarian)

## Overview

This guide goes over creating Section templates, Checklist questions and templates, and Evaluation templates.  This guide assumes a librarian has a basic understanding of what an entry is and has an entry that needs to be evaluated.  Once the librarian has created checklists and templates, an evaluation can be created.

## Who is this for?

This guide is for anyone with the role of librarian or administrator.  If unsure of current role(s), contact an administrator.

## Before Starting an Evaluation

Librarian has an entry that needs to be evaluated or reevaluated.

## Step 1: Creating a Section Template

*Go to Evaluation Management &rarr; Templates &rarr; Section Templates*

A Section template allows a librarian to create a custom layout for an evaluation 
form.  The librarian can give each section a title and a description of the section.
Subsections can be added to individual sections and require the librarian to enter a title.  
The _Private_, _Hide Title_, and _No Content_ flags are optional.  The Default Content
section is for the librarian to give instructions to an evaluator.  The Default Content
text area is optional but recommended.  A librarian is able top add custom fields
to a subsection by clicking on the Add Custom Field button. 

__Description of Flags:__

* *Private:* section/subsection will be filled out by an evaluator but content will be hidden from the user.
* *Hide Title:* title of section/subsection will be hidden from user.
* *No Content:* removes the evaluator's ability to input content under a section header.

## Step 2: Adding Checklist Questions

*Go to Evaluation Management &rarr; Templates &rarr; Checklist Questions*

Before creating a Checklist Template, the librarian will need to create a question pool.  A question
will be answered by an evaluator when performing an evaluation on an entry.
To create a question, the librarian will fill out an **Add/Edit Question** form.  The following
is a detailed list of the fields on the form:

* **QID:** an identifier that is unique across all evaluations (Required Field)
* **Add Tag:** allows a librarian to categorize sets of questions.  This helps a librarian to
create a checklist template based on a specific category.  Tags are not visible to
evaluators and end users.
* **Section:** a way to categorize question for the end user.  These sections are created under 
the Lookups section of the application.
* **Question:** text area for the actual question.
* **Narrative:** a detailed explanation about the question.
* **Objective:** the purpose or goal of the question.
* **Score Criteria:** a definition of what scores mean (Example: 5 = Excellent, 4 = Good, 3 = Average, 2 = Below Average, 1 = Poor).  
By default the scores are 1-5 without meaning.

Save the question when form complete.

**Note:** Changing/editing a question will affect both published and unpublished evaluations.
This is helpful if a question needs only a minor change, such as a typo.  If you are going to
completely reword the question or change its meaning, it is recommended that a new question
be created.

## Step 3: Creating Checklist Templates 

*Go to Evaluation Management &rarr; Templates &rarr; Checklist Templates*

A Checklist template allows a librarian to group questions related to a particular evaluation.
Under the **Details** tab of the **Add/Edit Checklist Template** form, the librarian gives the 
template a name and a description of the template's purpose.  An optional text area exists if 
the librarian would like to provide further instructions to an evaluator on what to fill in. Instructions are optional.  
There is an optional checkbox called "Update unpublished Evaluations."  If checked, any changes
made to the checklist template will be applied to any unpublished evaluation using the template.
Under the **Questions** tab of the **Add/Edit Checklist Template** for, the librarian will drag questions
from the **Question Pool** to **Questions in Template**.  Questions can be filtered based on a specific category or tag name.
Questions can be rearranged by dragging and dropping them in any order.

Once the form is complete, save the template.

## Step 4: Creating Evaluation Templates  

*Go to Evaluation Management &rarr; Templates &rarr; Evaluation Templates*

The Evaluation template is a snapshot of what the evaluation will look like to an evaluator.
On the **Add/Edit Evaluation Template** form, the librarian will first give the template a name and
description.  The second thing is to select the appropriate Checklist Template for this evaluation.  If
the librarian would like future changes/edits to the template to be reflected on unpublished evaluations, the **Update
unpublished Evaluations** box should be selected, otherwise leave unchecked.  The last thing the librarian will 
need to do is select the type of sections that will appear in the evaluation.  Drag and drop sections from the **Section Pool**
to the **Sections in Template** to add a section to evaluation.  Under **Sections In Template**, the librarian can rearrange the order
in which sections will appear on the evaluation by dragging and dropping.  Once the form is filled out completely, save the template.

## Step 5: Create Evaluation

*Go to Evaluation Management &rarr; Evaluation*

To add an evaluation, the librarian will fill out the **Create Evaluation** form.  An evaluation requires there to 
be an entry.  The librarian will select an evaluation template and give the version number of the evaluation.  It is 
recommended that the version number match the entry's version number.  A status is applied to the evaluation and the
types of statuses are controlled in the Lookup section of the application.

The librarian can then assign the evaluation to a specific group and/or user.  By checking the **Allow Adding Sections** box,
the librarian is giving permission to the evaluator to add sections/subsections to the evaluation as he or she deems necessary.
By checking the **Allow Question Management** checkbox, the librarian is giving permission to an evaluator to add questions
from the question pool to the evaluation and/or to remove questions currently assigned to the evaluation.

When the **Create Evaluation** form is complete, save the evaluation.


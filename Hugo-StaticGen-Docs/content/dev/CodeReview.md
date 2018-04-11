+++
title = "Code Review"
description = ""
weight = 8
+++

## Introduction
Here are documented the steps and protocol to follow for code reviews. Before a code review takes place the developer is working 
through a Jira ticket on the Kanban board. While they are working though this ticket they will be making commits to their development branch.
The standard format make commits is shown in the image below.
![My Image](/images/gitcommit.PNG)
Notice how the commit message contained the STORE-2853.
This is needed for all commits to a development branch.

## Creating the Code Review
To create the Code Review, go to the ticket on Jiran under "Development", and click on the commits.

![My Image](/images/jiracommits.PNG)

When you do that a page will open you then need to select "Create review for all commits". The project should already be chosen (DI2E Storefront Redesign (STORE)) now click the "Create Review" button.
The final screent will be the "Edit Review Details" where reivewers can be written in, objectives can be stated, and other info can be reviewed.
######   
When a developer is ready to have their code reviewed they need to decide who is going to review their code.
The developer should place their Jira ticket in the Code Review section of the Kanban board and and assign it to the first reviewer.
If left unassigned then the ticket should be picked up as appropriate.
If unassigned and a reviewer begins the code review, they can assign the Jira ticket to another individual if it is appropriate.

## Review the Code
When a reviewer, or multiple reviewers are reviewing the code, all changed files should be reviewed and the checklist below should be followed.

{{<mermaid align="left">}}
graph LR;
A[Build];
B[SonarQube];
C[Code Standard];
D[Style Check];
E[Check Business Logic];
F[Update Documentation];
A-->B;
B-->C;
C-->D;
D-->E;
E-->F;
{{< /mermaid >}}
   
1. Branch build is passed (Green). These results should be verified through using SonarQube/Sonarcloud.

2. It's expected the formating and basic style is handled by the IDE setting.
Check that those were applied. (quick scan) The netbeans configuration can be found [here](/files/nbformating.zip).
   
3. Check Coding Standards ([Front-end](/dev/front-end-code-standard) and [Server](/dev/server-code-standard))

4. Check UI against [Style Guide](/dev/uiguide)

5. Check for known business rule breaks (missing cross cutting concerns; regression is expected to catch some of this)

6. Make sure any required documentation changes are made.

###### 
From here there are a few things that can happen

{{<mermaid align="left">}}
graph TD;
A[Developer Creates Code Review];
B[Developer assigns reviewer or reviewers];
C[The Code Review is started by a reveiwer];
D[If there was a problem with the review];
E[If there wasn't a problem with the review];
F[The corresponding Jira ticket is updated and the developer is notified];
G[The next reviewer works through the code review];
H[If last reviewer];
I[Close/Complete the fisheye ticket, update the jira ticket, else update the fisheye jira tickets and assign to next reviewer];
A-->B;
B-->C;
C-->D;
C-->E;
D-->F;
E-->G;
G-->H;
H-->I;
{{< /mermaid >}}

###### 
The basic idea is that when a Code review is completed, based on the outcome, tickets are handled approprietly and individuals are notified.

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
When a developer is ready to have their code reviewed they need to decide who is going to review their code.
The developer should place their Jira ticket in the Code Review section of the Kanban board and and assign it to the first reviewer.
If left unassigned then the ticket should be picked up as appropriate.
If unassigned and a reviewer begins the code review, they can assign the Jira ticket to another individual if it is appropriate.

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

1. Branch build is passed. (Green) and sonar qube! and other stuff passed   

2. It's expected the formating and basic style is handled by the IDE setting.
Check that those were applied. (quick scan) The netbeans configuration can be found [here](/files/nbformating.zip).
   
3. Check Coding Standards ([Front-end](/dev/front-end-code-standard) and [Server](/dev/server-code-standard))

4. Check UI against [Style Guide](/dev/uiguide)

5. Check for known business rule breaks (missing cross cutting concerns; regression is expected to catch some of this)

6. Make sure any required documentation changes are made.

(It may NOT be all required documentation needed for final release; just the documentation related to the ticket)

7. Move ticket as ready for testing (meaning code review passed) or back to fix for adjustments.

On Failure:

Note specifically what failed and why.  Also, suggest a correction path; where possible.

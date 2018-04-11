+++
title = "Code Review"
description = ""
weight = 8
+++

Here are documented the steps and protocol to follow for code reviews. Before a code review takes place the developer is working 
through a Jira ticket on the Kanban board. While they are working though this ticket they will be making commits to their development branch.
The standard format make commits is shown in the image below.

![Image of Yaktocat](https://octodex.github.com/images/yaktocat.png)

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

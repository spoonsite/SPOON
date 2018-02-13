+++
title = "Merge Branch Back Into Devlop"
description = ""
weight = 7
+++

# Process for Merging a Branch back into main Develop Branch

## Assumptions

* A pull request exists in GitHub
* The Jira ticket for the branch shows that the branch tests have passed
* Jenkins shows Green for the ticket indicating that all its tests have passed

## GitHub

1. Navigate to the [Pull Requests](https://github.com/di2e/openstorefront/pulls) tab on GitHub and select your branch

2. If GitHub tells you:
    > This branch has no conflicts with the base branch
    >
    > Merging can be performed automatically.

    then click on the button to merge the request.

    2.1. If there are conflicts that are simple to resolve then fix the conflicts and merge the branch.

    2.2. Otherwise, _do not merge_ the branch. Instead, update the state in Jira to be Need Fix. Once the conflicts have been resolved, the updated code will have to go through Code Review and subsequent processes again.

3. Once merged, click on the button to delete the branch. If you are cleaning up an old branch, follow the directions [here](https://help.github.com/articles/deleting-and-restoring-branches-in-a-pull-request/).

## Jenkins

1. Navigate to the [Jenkins](http://sf-jenkins.usurf.usu.edu:8080/) server

2. Click on the branch name relating to the Jira ticket

3. Click on Delete Project to get rid of the branch. NOTE: This action cannot be undone

## Portainer

1. Navigate to the [Portainer](http://sf-jenkins.usurf.usu.edu:9000) server and log in.

2. Select the Docker container relating to the ticket

3. Click on the Delete Container button. The container does not need to be stopped or paused first. NOTE: This action cannot be undone

## Jira

1. Open the ticket in Jira

2. Move the ticket to the Complete state
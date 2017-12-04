+++
title = "Git Workflow"
description = ""
weight = 3
+++

We are following the Git Flow style of branching.

### Master

Follows current release


### Develop

Holds the integrated development (CI server is pointed to this)

### Feature Branches

Based from development and hold the changes for that feature.  
They are merged back into develop via a pull request when they are development complete.  Branches can be pruned at that point or when after the release that contains them.
Branch name are base on the top level ticket number for easy tracking (i.e. feature/STORE-1234).
Subtasks of a feature should be branched off the feature branch and named accordingly. For example feature 1234 with subtask 1235 would be on feature/STORE-1234/STORE-1235. They are merged back into the feature via a pull request when the task is complete.

### Release Branches

Created when the features that are all slated for the release have be completed and we are now in a QA/Bug fix phase.
Release branches will get removed after the release is complete and the branch has been merged to Master and pushed back to develop.
All bugfixes/adjustment should be made on this branch for the release or branched off of this branch to create pull requests.

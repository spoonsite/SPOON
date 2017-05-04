# Git Workflow

We following the Git Flow style of branching.

### **Master** 
-------
Follows current release


### **Develop**
-------
Holds the integrated development (CI server is pointed to this)

### **Feature Branches** 
-------
Based from development and hold the changes for that feature.  
They are merged back into develop via a pull request when they are development complete.  Branches can be pruned at that point or when after the release that contains them.
Branch name are base on the top level ticket number for easy tracking.
                   
### **Release Branches**
-------
Created when the features that are all slated for the release have be completed and we are now in a QA/Bug fix phase.
Release branches will get removed after the release is complete and the branch has been merged to Master and pushed back to develop.
All bugfixes/adjustment should be made on this branch for the release or branched off of this branch to create pull requests.
                    



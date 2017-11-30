+++
title = "Data Migration Guide"
description = ""
weight = 5
+++

This guide provide help for importing data from external sources as well as 
moving data from one instance to another instance.

## 1. Importing/Export  (Third parties)


The application provides set of standard format that it accepts to import Attributes/Categories
as well as Entries.  It also provides a set of custom formats that can have mappings applied. These
custom formats allow taking a variety of data with so simple handling.

See Admin Tools -> Data Management -> Imports

### 1.1 Custom Mapping *

An admin can create field mapping from a file field to a field in application.
Some pre-defined transformation can be applied such as lowercasing the field.

In some cases, field mapping is insufficient to import a source.  A plugin can be 
created to provide more handling control (see Developer Guide)


*This feature is in beta
 
## 2. System Archives (System to System / restore)

See Admin Tools -> Application Management -> System Archives

System archive are meant for storefront application to storefront application 
data migration or for saving and restoring data.  The application will package all
of the different data parts needed to fully reconstruct the data. 

The application supports several different archive types:

**DB Export** - This just export database (no media or local resources). This exports the whole database.

{{% notice warning %}}
On import note the application will be unavailable until import completes. 
{{% /notice %}}

**Full Export** - This packages a database export with all file (*expect system archives to avoid excessive disk space requirements) 
*Also see "Post Cleanup Considerations" in section 3*.

{{% notice warning %}}
On import note the application will be unavailable until import completes. 
{{% /notice %}}

**General Export** - This export allow for selective exporting.  Keep in mind it will
 export related data as needed to complete the export.

 
## 3. Full System / Manual Migration (System Admin)
       
Note: This will copy everything: Database, local resources, and configuration 

1. Stop the application (Generally done by stopping tomcat)
2. Copy /var/openstorefront directory and all sub-directories. (*location of directory may be different depending on configuration)

Post Cleanup Considerations:

1. User profiles should be in-activated to avoid sending notifications to users. 
2. If using built in security then users may need to be in-activated or removed to prevent access.
3. Alerts should be look at to make sure they still valid for the new setup.
4. Scheduled report should be turn off or looked at to see if they are valid.
5. Check branding and system configuration to make sure things are valid for the new instance.

 
## 4. Scenarios

In all cases, please be mindful of the disk space requirements.  All archive will need to be build on the originating system.

#### Scenario 1

External data that I need loading into the application.

**Solution:**

Use the Admin Tools -> Data Management -> Imports and depending on originating file form
create custom mapping or a plugin to match up the data.

----
#### Scenario 2

Export entries to use in another application.

**Solution:**

Export the entry via the Admin Tools -> Data Management -> Entries.  
Select entries and export.  

{{% notice warning %}}
This export will NOT export evaluations associated with the entry; only the entry.
If evaluations are needed then use the System Export General Export to get all information.
{{% /notice %}}

----
#### Scenario 3:

Export entries in another format for use in another application.

**Solution:**

Export the entry via the Admin Tools -> Data Management -> Entries.  
Select entries and select different supported format.

**Note** if a export format is not support. Request development for a the new format.
Please provide specs, if available, for the format.  

----
#### Scenario 4

I need to move entries and evaluation to another storefront application.

**Solution:**

Use the System Archive: General to select entries to export then import on desired system.

{{% notice warning %}}
compatibility is only tests for same version of the application.  IE. export from 2.5 to import 2.5.
Imports from other version may or may not work.  Contact developer/support for help.
{{% /notice %}}

----
#### Scenario 5

I need a full backup.

**Solution:**

Use System Archive - Full Backup  or  Full System / Manual Migration 

{{% notice warning %}}
 Make sure the system has enough space to run this.  Require about the same space free as it's currently using.
{{% /notice %}}

----
#### Scenario 6

I want a copy of all entries.

**Solution:**

Use System Archive - General Export with all entries selected.
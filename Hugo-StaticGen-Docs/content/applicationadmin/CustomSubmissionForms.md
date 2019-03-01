+++
title = "Custom Submission Forms"
description = ""
weight = 2
markup="mmark"
+++

Custom Submission Forms are used when you want to present a custom look and feel to the user when they submit an entry.  You can add a form with many required or optional fields depending on the Entry Type of the submission.  This form is then shown to the user when they are presented with the fields for a new entry of that type.

## 1. Create a Custom Form to be used

### 1.1 Create Needed Attributes

Before a form can be designed, any needed required or custom attributes should be added.  To do this:

a)  Navigate to **Admin Tools** if you are not already there (click on your username in the upper-right, then Admin Tools) 
b)  Select **Data Management** &rarr; **Attributes**
c)  Click **+Add New Type** and fill out the form as required for all attributes (required and optional) you will need to build your custom entry form in the next step.
![AddAttrib](/images/AppAdmin/AddAttrib.JPG)
d)  You may wish to enter some codes for the attribute by selecting it and then clicking **Manage Codes**.  This is critical for attributes that do not Allow User Created Codes as there is no way to enter those, and will cause problems on required attributes. 
![ManageCodes](/images/AppAdmin/ManageCodes.JPG)

### 1.2  Create/Design The Form

a.  Navigate to **Admin Tools** if you are not already there (click on your username in the upper-right, then Admin Tools) 
b.  Select Data Management &rarr; **Submissions &rarr; Submissions Form Templates**
c.  Click the **+ Add Form** button.  Select an Entry Type.  Fill in the Form Name, Description, and Entry Type.  Remember that these custom submission forms are tied to a specific entry type(s).  If needed, create a new entry type and then navigate back here (Data Mangement &rarr; Entry Types).  Click **Create Form.**
![addTemplate](/images/AppAdmin/addTemplate.JPG)
d.  In the Form Builder Tool Window add sections as needed.  Add fields, paragraphs, horizontal rules, images, and so forth. 
![customForm](/images/AppAdmin/customForm.JPG)
e.  Note the **Mappings** tab.  The Mappable Fields **MUST** be on the form as well as the Required Attributes.  These sections will have a red x or a green checkmark, depending on if they have been added to the form. 
![mappingsTab](/images/AppAdmin/mappingsTab.JPG)
f.  To add a mappable field:
  1. Click the **Add a field** button at the bottom of the form then **Mappable Field OR Attribute**.  
  2. Above in the form select the **Map To Field:** drop-down** OR the **Attribute Type:** drop down to select the attribute.  
  3. Be sure to check the **Required** box if the field or attribute is a required one.
  4. When you have finished the form and there are no more red checkmarks on the Mappings tab, save the form and close it.

## 2. Verify the form and activate it

a.  When you are finished with the form, from the main Custom Forms page, select the Form and then click on the **Verify** button.  This will prompt you to enter data to verify the validity of the form, that all of the required fields are able to be entered, and so forth.  

b.  On the final page, **Review Submission** ensure that all of the sections have a green checkmark.  When finished click the **Verify Submission** button.

{{% notice note %}}
**If you have all green checkmarks on the Review Submission page, but still get an error like the one below when Verifying:**
1.  Go back to Admin Tools and **Edit** the form
2.  Go to the Mappings tab 
3.  Ensure that the Mappaple Fields AND the Required Attributes sub-tabs are green- they have all been accounted for on the form.
![submitError](/images/AppAdmin/submitError.JPG)
{{% /notice %}}

c.  When the form is in the status of **Verified**, be sure to select it, then click **Toggle Active Status** so that **"A"** appears in the Active Status Column.

d.  If you get a message indicating that a custom form is already active for that entry type, deactivate (Toggle Active Status) the other custom form then activate yours that was just created.  An entry type cannot have more than one active custom form at any given time.

![CustomForms](/images/AppAdmin/CustomForms.JPG)

 
## 3. Entry and Approval using the Custom Form

a.  Click on your **username** in the upper-right then **User Tools &rarr; Submissions &rarr; + New Submission **

b.  From the **Select Entry Type Form** box select the Entry Type that you created your custom form for.  

c.  Click **+ Create** and then enter a unique initial submission name and click **Ok**.

d.  The submission form loads with your customization attributes and fields as designed prior.  Answer the required questions/fields and confirm on the **Review Submission** section that the entry has no red "x" marks, but only green checkmarks.  

e.  Click **Submit for Approval**

f.  Navigate to **Admin Tools &rarr; Data Management &rarr; Entries**  Sort by **Last Activity Date** descending to see the entry just entered at the top of the list.  Select the entry.

g.  Click on **Approve.**

h.  As a final check, go to the home page and **Search** for the new entry to ensure it is indexed and searchable.

![CustomSubmission](/images/AppAdmin/CustomSubmission.JPG)

+++
title = "Custom Submission Forms"
description = ""
weight = 220
markup="mmark"
+++

## Create a Custom Form

This is how to create a custom submission form for new submissions.

### Create Needed Attributes

Before a form can be designed, any required or custom attributes should be added. To do this:

1. Navigate to **Admin Tools** if you are not already there (click on your username in the upper-right, then Admin Tools
2. Select **Data Management** &rarr; **Attributes**
3. Click **+Add New Type** and fill out the form as required for all attributes (required and optional) you will need to build your custom entry form in the next step.
   ![AddAttrib](/images/AppAdmin/AddAttrib.JPG)
4. You may wish to enter some codes for the attribute by selecting it and then clicking **Manage Codes**. This is critical for attributes that do not Allow User Created Codes as there is no way to enter those, which will cause problems on required attributes.
   ![ManageCodes](/images/AppAdmin/ManageCodes.JPG)

### Create/Design the Form

1. Navigate to **Admin Tools** if you are not already there (click on your username in the upper-right, then Admin Tools)
2. Select **Data Management** &rarr; **Submissions &rarr; Submissions Form Templates**
3. Click the **+ Add Form** button. Select an Entry Type. Fill in the Form Name, Description, and Entry Type. Remember that these custom submission forms are tied to a specific entry type(s). If needed, create a new entry type and then navigate back here (Data Management &rarr; Entry Types). Click **Create Form.**
   ![addTemplate](/images/AppAdmin/addTemplate.JPG)
4. In the **Form Builder Tool's window** add sections as needed. Add fields, paragraphs, horizontal rules, images, and so forth.
   ![customForm](/images/AppAdmin/customFormAttributeFields.jpg)
5. Note the **Mappings** tab. The Mappable Fields **MUST** be on the form as well as the Required Attributes. These sections will have a red x or a green checkmark, depending on whether they have been added to the form.
   ![mappingsTab](/images/AppAdmin/customFormMapping.jpg)
6. To add a mappable field:
   1. Click the **Add a field** button at the bottom of the form then **Mappable Field OR Attribute**.
   2. Above in the form select the **"Map To Field:" drop-down** OR the **"Attribute Type**: drop down to select the attribute.
   3. Be sure to check the **Required** box if the field or attribute is a required one.
   4. When you have finished the form and there are no more red checkmarks on the **Mappings** tab, save the form and close it.

## Verify the Form and Activate it

1. When you are finished with the form, from the main Custom Forms page, select the Form and then click the **Verify** button. This will prompt you to enter data to verify the validity of the form, that all the required fields are able to be entered, and so forth.
2. On the final page, **Review Submission** ensure that all the sections have a green checkmark. When finished, click the **Verify Submission** button.
3. When the form is in the status of **Verified**, be sure to select it, then click **Toggle Active Status** so that **"A"** appears in the Active Status Column.
4. If you get a message indicating that a custom form is already active for that entry type, deactivate (toggle **Active Status**) the other custom form then activate the one that was just created. An entry type cannot have more than one active custom form at any given time.

{{% notice note %}}
**If you have all green checkmarks on the Review Submission page, but still get an error like the one below when Verifying:**

1. Go back to **Admin Tools** and **Edit** the form
1. Go to the **Mappings** tab
1. Ensure that the **Mappable Fields** AND the **Required Attributes** sub-tabs are green- they have all been accounted for on the form.
   ![submitError](/images/AppAdmin/submitError.JPG)
   {{% /notice %}}

![CustomForms](/images/AppAdmin/CustomForms.JPG)

## Entry and Approval using the Custom Form

1. Click your **username** in the upper-right then **User Tools &rarr; Submissions &rarr; + New Submission**
2. From the **Select Entry Type Form** box, select the Entry Type for which you created your custom form.
3. Click **+ Create** and then enter a unique initial submission name and click **Ok**.
4. The submission form loads with your customization attributes and fields as designed prior. Answer the required questions/fields and confirm on the **Review Submission** section that the entry has no red "x" marks, but only green checkmarks.
5. Click **Submit for Approval**
6. Navigate to **Admin Tools &rarr; Data Management &rarr; Entries** Sort by **Last Activity Date** descending to see the entry just entered at the top of the list. Select the entry.
7. Click **Approve.**
8. As a final check, go to the home page and **Search** for the new entry to ensure it is indexed and searchable.

![CustomSubmission](/images/AppAdmin/CustomSubmission.JPG)

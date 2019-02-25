+++
title = "Custom Submission Forms"
description = ""
weight = 2
+++
Custom Submission Forms are used when you want to present a custom look and feel to the user when they submit an entry.  You can add a form with many required or optional fields depending on the Entry Type of the submission.  This form is then shown to the user when they are presented with the fields for a new entry of that type. 

## 1. Create a Custom Form to be used

### 1.1 Create Needed Attributes

Before a form can be designed, any needed required or custom attributes should be added.  To do this: 
<p style="margin-left: 40px">
        a. Navigate to <b>Admin Tools</b> if you are not already there (click on your username in the upper-right, then Admin Tools) 
   <br> b. Select <b>Data Management &rarr; Attributes</b>
   <br> c. Click <b>+Add New Type</b> and fill out the form as required for all attributes (required and optional) you will need to build your custom entry form in the next step.
			<img src="/images/AppAdmin/AddAttrib.JPG" alt="add an attribute" style ="width:60%" />
   <br> d. You may wish to enter some codes for the attribute by selecting it and then clicking <b>Manage Codes</b>.  This is critical for attributes that do not Allow User Created Codes as there is no way to enter those, and will cause problems on required attributes. 
			<img src="/images/AppAdmin/ManageCodes.JPG" alt="manage codes" style ="width:60%" />
</p>

### 1.2  Create/Design The Form

<p style="margin-left: 40px">
        a. Navigate to <b>Admin Tools</b> if you are not already there (click on your username in the upper-right, then Admin Tools) 
   <br> b. Select Data Management &rarr; <b>Submissions &rarr; Submissions Form Templates</b>
   <br> c. Click the <b>+ Add Form</b> button.  Select an Entry Type.  Fill in the Form Name, Description, and Entry Type.  Remember that these custom submission forms are tied to a specific entry type(s).  If needed, create a new entry type and then navigate back here (Data Mangement &rarr; Entry Types).  Click <b>Create Form.</b>
   			<img src="/images/AppAdmin/addTemplate.JPG" alt="add a template" style ="width:40%" />
   <br> d. In the Form Builder Tool Window add sections as needed.  Add fields, paragraphs, horizontal rules, images, and so forth. 
			<img src="/images/AppAdmin/customForm.JPG" alt="customForm" style ="width:75%" />
   <br> e. Note the <b>Mappings</b> tab.  The Mappable Fields <b>MUST</b> be on the form as well as the Required Attributes.  These sections will have a red x or a green checkmark, depending on if they have been added to the form. <br>
			<img src="/images/AppAdmin/mappingsTab.JPG" alt="mappingsTab" style ="width:75%" />
   <br> f. To add a mappable field: 
   <p style="margin-left: 80px">
              i. Click the <b>Add a field</b> button at the bottom of the form then <b>Mappable Field OR Attribute</b>.  
		<br> ii. Above in the form select the <b>Map To Field:</b> drop-down</b> OR the <b>Attribute Type:</b> drop down to select the attribute.  
		<br>iii. Be sure to check the <b>Required</b> box if the field or attribute is a required one.
		<br> iv.  When you have finished the form and there are no more red checkmarks on the Mappings tab, save the form and close it.
   </p>
</p>

## 2. Verify the form and activate it.

<p style="margin-left: 40px">
        a. When you are finished with the form, from the main Custom Forms page, select the Form and then click on the <b>Verify</b> button.  This will prompt you to enter data to verify the validity of the form, that all of the required fields are able to be entered, and so forth.  When finished click the Verify Submission button.
   <br>	<b>NOTE:</b> If there are errors when trying to verify Edit the form and go to the Mappings tab and ensure that the Mappaple Fields AND the Required Attributes sub-tabs are green- they have all been accounted for on the form.
   <br> b. When the form is in the status of <b><line style="color:green">Verified</line></b>, be sure to select it, then click <b>Toggle Active Status</b> so that <b>"A"</b> appears in the Active Status Column.
   <br> c. If you get a message indicating that a custom form is already active for that entry type, deactivate (Toggle Active Status) the other custom form then activate yours that was just created.  An entry type cannot have more than one active custom form at any given time.
</p>
 
## 3. Entry and Approval using the Custom Form.

<p style="margin-left: 40px">
        a. Click on your <b>username</b> in the upper-right then <b>User Tools &rarr; Submissions &rarr; + New Submission </b>
   <br> b. From the <b>Select Entry Type Form</b> box select the Entry Type that you created your custom form for.  
   <br> c. Click <b>+ Create</b> and then enter a unique initial submission name and click <b>Ok</b>.
   <br> d. The submission form loads with your customization attributes and fields as designed prior.  Answer the required questions/fields and confirm on the <b>Review Submission</b> section that the entry has no red "x" marks, but only green checkmarks.  
   <br> e. Click <b>Submit for Approval</b>
   <br> f. Navigate to <b>Admin Tools &rarr; Data Management &rarr; Entries</b>  Sort by <b>Last Activity Date</b> descending to see the entry just entered at the top of the list.  Select the entry.
   <br> g. Click on <b>Approve.</b>
   <br> h. As a final check, go to the home page and <b>Search</b> for the new entry to ensure it is indexed and searchable.
</p>









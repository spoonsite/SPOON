+++
title = "The Branding Page"
description = ""
weight = 2
+++
 
All the facial, cosmetic aspects of Spoon are organized in what is called Branding. This tool allows the Admin to have the ability to set the graphic design and theme characteristics for the site.

[__How do I change what Spoon looks like?__](#gothere)   \

<!--more-->

![The Branding Page](/images/AppAdmin/Branding.png) \

  As usual, the "__Refresh__" button only refreshes this sub-page, it does not perform any operation on the Brandings listed in the grid. It is possible that the grid is completely empty; if this is the case, this means that your instance of Spoon is using the default Branding built-in to Spoon. In Spoon v2.11 the default branding has references to DI2E, an organization that developed an early form of Spoon's source code. 

 The "__Add__"" button launches the Add/Edit Branding popup form, which will allow you to create a new set of thematic elements for Spoon. The Admin is able to do this without any knowledge of HTML, CSS, or other web languages (but if the Admin does know CSS, some more advance tweaking is still possible through Branding). 

 Notice the "Status" column. There can only be one active branding (denoted by the 'A'). Clicking the "__Reset to Default__" button will switch whatever branding is active to inactive, and activate the Default Branding (which is not represented as a branding in the grid because it is not editable from the Admin Tools). 



## The Branding Form

 ![Diagram of main System sub-page](/images/AppAdmin/BrandingMapping.png)

<a name="GeneralBranding"></a>

### 1 - General Branding
Be wary of the fact that text color is preserved from the input boxes on this page to the destinations of the content. Meaning that, if in the Landing Page Footer input box the Admin entered black text, then the text would be black where it appears in the landing page's footer - this even if the background color of the landing page's footer is already a dark color that does not contrast well with black text. For this reason it is wise to coordinate the site's colors set in the "Colors/Logos" tab with the font color set in the General Branding tab. 

This easily lead to a mistake if one Admin makes the text of the contents of one of the input boxes white, which would make that text box appear to another Admin to be completely empty, even though there is white text inside (with no contrast against the white background). 

If the Admin knows HTML/CSS, (s)he can insert custom language by selecting the "__Source Edit__" option on the input boxes: ![Source Edit Button](/images/AppAdmin/sourceeditbutton.png).

<a name="LoginPage"></a>

### 2 - Login Page
This....
<a name="Support"></a>

### 1 - Support
This....
<a name="Security"></a>

### 1 - Security
This....
<a name="Colors/Logos"></a>

### 1 - Colors/Logos
This....
<a name="CurrentCSS"></a>

### 1 - Current CSS
This....

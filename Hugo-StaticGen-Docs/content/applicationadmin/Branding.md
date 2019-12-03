+++
title = "Branding"
description = ""
weight = 110
+++

![The Branding Page](/images/AppAdmin/Branding.png)

## Table of Contents

1. [General](/applicationadmin/branding/#general)
1. [Login Page](/applicationadmin/branding/#login-page)
1. [Support](/applicationadmin/branding/#support)
1. [Security](/applicationadmin/branding/#security)
1. [Colors/Logos](/applicationadmin/branding/#colors-logos)
1. [Current CSS](/applicationadmin/branding/#current-css)

{{% notice warning %}}
When updating or changing branding, you must toggle it back on. When a branding is created or edited it is automatically turned off. A branding is active only if the status column has an "A" in it or if the name has "active" next to it.
{{% /notice %}}

## The Branding Form

![Diagram of main System sub-page](/images/AppAdmin/BrandingMapping.png)

### General

Be wary of the fact that text color is preserved from the input boxes on this page to the destinations of the content. Meaning that, if in the Landing Page Footer input box the Admin entered black text, then the text would be black where it appears in the landing page's footer - this even if the background color of the landing page's footer is already a dark color that does not contrast well with black text. For this reason it is wise to coordinate the site's colors set in the "Colors/Logos" tab with the font color set in the General Branding tab.

This can lead to a mistake if one Admin makes the text of the contents of one of the input boxes white, which would make that text box appear to another Admin to be completely empty, even though there is white text inside (with no contrast against the white background).

If the Admin knows HTML/CSS, they can insert custom language by selecting the "**Source Edit**" option on the input boxes:

![Source Edit Button](/images/AppAdmin/sourceeditbutton.png)

The "**Show link to mobile site in menu**" checkbox does nothing in SPOON v2.11.

### Login Page

Here you can set the content seen on the login page.

### Support

In this section you are able to set your feedback handing method, such as email or internal storage.

{{% notice note %}}
JIRA is a selection in the dropdown but support has been dropped for JIRA integration in V2.11.
{{% /notice %}}

You are also able to add a analytics tracking code, as to learn more about what your user are doing on the site.

### Security

Admins can set a security banner for the application as needed ( *i.e.* "Secret", "Top Secret", etc.).

![Security Banner](/images/AppAdmin/SecurityBanner.png)

### Colors/Logos

As of SPOON v2.11, there are two sets of branding. One for the legacy Ext.js and another for the new Vue.js client.

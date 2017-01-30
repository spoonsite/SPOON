Sample and Test Database
============

A Sample and Test Database is included here to:

 - Provide a sample database new users can install to get a feel for the Storefront website.  The database contains a 
   familiar organization of music including 14 artists and 105 albums.  By browsing the information the user will get
   a feel for how the Storefront is organized and displayed, without having to worry about technical entries.
   
 - Establish a static test database for automated regression testing.  The database can be imported and used for unit
     and integration automated testing.  Queries for certain known entries with known search results, for example, can
     be established.
     
 - **NOTE:** The data contained in the sample database is used with permission from http://musicbrainz.org
     
Setup and Data Import Instructions:
============

 1.  With the Storefront website up and running **log in** as an **administrator**.  This works best from an empty database with
     no prior entries. 
 
 2.  As the admin user in the website click on the *username* in the upper-right of the web page, then **Admin Tools.**
 
 3.  Under **Data Management** click on **Entry Templates**.  Click the **+ Add** button.
 
 4.  Give the template the name:  *Artist Entry Template* and add the following from the **Template Blocks** section
     in this order:
     a.  Description
     b.  Media
     c.  Vitals
     d.  Relationship
     Click **Save/Continue** when finished.  Close the window if needed.

 5.  Click the **+ Add** button to add an additional template.  Call this one *Album Entry Template*.  Add the follwoing
     from the **Template Blocks** section in this order:
     a.  Vitals
     b.  Relationship
     c.  Media
     d.  Description
     Click **Save/Continue** when finished.  Close the window if needed.
     
 6.  Click on **Data Management** then **Entry Types**.  Click **+ Add** to add a new type.  Fill out the form as follows:
     Type Code:  **ARTIST**
     Label:  **Artist**
     Description: **The artist**
     Data Entry:  Check **all** boxes *except* for **Evaluation Invormation**
     Click **Save**
 
 7.  Click **+ Add** to add a new type.  Fill out the form as follows:
      Type Code:  **ALBUM**
      Label:  **Album**
      Description: **The album**
      Data Entry:  Check **all** boxes *except* for **Evaluation Invormation**
      Click **Save**
      
 8.  If Article and DI2E Component are present:
     a.  Select them (one at a time) and click **Toggle Status** at the top of the table.
     b.  They should now show "**I**" in the Active Status column, meaning that they are Inactive.
    
 9.  Click on **Data Management** then **Attributes**.  Click on **Import**.  In the Import Data Window:
     File Type  **Attribute**
     File Format  **Standard Format (JSON)**
     Data Mapping ***\<leave blank\>***
     Data Source  **DI2E Component**
     Import *\<select the file from your local hard drive downloaded from this section **attributes.json**\>*
     
     Click on **Upload**
     After successful upload you should see Genre, Record Label, and Summary in your attributes list.
     
 10.  Click on **Data Management** then **Entries**.  Click on **Import**
      File Type  **Component**
      File Format  **Standard Format (ZIP, JSON)**
      Data Mapping ***\<leave blank\>***
      Data Source  **DI2E Component**
      Import *\<select the file from your local hard drive downloaded from this section **ExportedComponents.zip**\>*
      Options:  Check **all boxes**
      
      Click on **Upload**
      After successful upload yo should see a list of 119 Entries.
      
 11.  Return to the Home Page.  Click the **magnifying glass icon** to search all entries. 
      
     
NOTE:  The site can be customized by going to (In Admin Tools) **Application Management** then **Branding**.  Highlights
can also be added to the home page from **Data Management** and **Highlights**.  Enjoy exploring and further customization.

# How to Use Storefront Docker
1. Install Docker on a host Windows, Mac or Linux
2. Copy this folder to the host VM with docker installed
3. From the Bash Window on the VM change the execute permissions on all of the the .sh files 
<pre>$ chmod 755 *.sh</pre>
4. build the images 
<pre>$./buildSF.sh</pre>
5. All the images should be built 
6. run the application
<pre>./runSF.sh</pre>
7. You should be able to see the solr admin in a browser from http://[ipaddress of host vm]:8983/solr
8. You should be able to access the storefront in a browser from http://[ipaddress of host vm]:8080/openstorefront
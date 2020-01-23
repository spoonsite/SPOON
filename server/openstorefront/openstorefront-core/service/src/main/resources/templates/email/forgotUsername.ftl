<#import "base/emailBase.ftl" as u>

<@u.page>
    <h2>${applicationName} - Forgot Username</h2>
    <p>Hi there,</p>
    <#if username??>
        Your username is: <br><br> ${username} <br>
        <br>
        If you have forgotten your password. Click on "Forgot Password" on the login page.<br>
        <br>
        *Note: There may be multiple accounts associated with your email.<br>
        <br>
    <#else>
        <b>Unable to find a username associated with your email.</b> <br><br>
        The email address must match the email used to create the user. (case sensitive)<br>
        Email address can be changed on your user profile after logging in.<br>
        <br>
    </#if>
</@u.page>
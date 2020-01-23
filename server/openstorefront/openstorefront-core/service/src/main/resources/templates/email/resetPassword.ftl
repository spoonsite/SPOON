<#import "base/emailBase.ftl" as u>

<@u.page>
    <h2>${applicationName} - Password Reset</h2>
    <p>Hi ${username},</p>
    <p>
        A password reset was requested for your user.
        <br>
        Click or Copy/Paste the link into your web browser to Approve the request.
    </p>
    <p>${resetPasswordLink}</p>
    <br>
    <p>
        Your password will not change until you go to the above link.
        <br>
        <br>
        If you are not the intended user of this message, please delete this message.
        <br>
    </p>
</@u.page>
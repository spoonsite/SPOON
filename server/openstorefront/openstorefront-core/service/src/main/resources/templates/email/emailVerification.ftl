<#import "base/emailBase.ftl" as u>

<@u.page>
    <h2>Email verification for ${applicationName}</h2>
    <p>Hi there,</p>
    <p>Here is your user registration email verification code.<sup>*</sup></p>
    <br>
    <p class="text-block">${verificationCode}</p>
    <br>
    <p>If you are not the intended user of this message, please delete this message.</p>
    <br>
    <p><small><sup>*</sup>NOTE: Please enter the verification code exactly as it appears it is case sensitive</small></p>
</@u.page>
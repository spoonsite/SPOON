<#import "base/emailBase.ftl" as u>

<@u.page>
    <h2>Request for information about ${partName}</h2>
    <p>Hi there,</p>
    <p>${userName} on ${applicationName} was looking to contact you about <a href="${partUrl}">${partName}</a>.</p>
    <p>Here is their message:</p>
    <p class="text-block">${message}</p>
</@u.page>
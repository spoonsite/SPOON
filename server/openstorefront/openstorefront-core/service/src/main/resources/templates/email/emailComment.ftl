<#import "base/emailBase.ftl" as u>

<@u.page>
    <h2>${entryName} has been updated.</h2>
    <p>Hi there,</p>
    <p>
        This is an auto generated email to show the comments that were made to a component
        that you are subscribed to.
        <br>
        <strong>Entry:</strong> ${entryName}
        <br>
        <strong>Current-Step:</strong> ${currentStep}
        <br>
        <strong>author:</strong> ${author}
        <br><br>
        <strong>Comment:</strong> ${comment}
        <br><br>
        <strong>Reply-Instructions:</strong> ${replyInstructions}
        <br>
    </p>
</@u.page>


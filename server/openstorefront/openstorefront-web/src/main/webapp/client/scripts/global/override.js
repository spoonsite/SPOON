/* 
 * Overrides for defaults/extensions
 * 
 */

/* global CoreApp */

//Extensions

Ext.apply(Ext.selection.RowModel.prototype, {
	selectPrevious: function(){
		if (this.hasSelection())
		{
			for (var i=0; i<this.store.getCount(); i++)
			{
				if (this.isSelected(i))
				{
					this.select(i - 1, false, true);
					break;
				}
			}
		}
		else
		{
			if (this.store.getCount() > 0)
			{
				this.select(this.store.getCount() - 1);
			}
		}		
	},
	
	selectNext: function(){
		if (this.hasSelection())
		{
			for (var i=0; i<this.store.getCount(); i++)
			{
				if (this.isSelected(i))
				{
					this.select(i + 1, false, true);
					break;
				}
			}
		}
		else
		{
			if (this.store.getCount() > 0)
			{
				this.select(0);
			}
		}
	},
	
	hasPrevious: function(){
		var previous = false;
		if (this.hasSelection())
		{
			for (var i=0; i<this.store.getCount(); i++)
			{
				if (this.isSelected(i))
				{
					if (i > 0)
					{
						previous = true;
						break;
					}
				}
			}
		}
		return previous;		
	},
	
	hasNext: function(){
		var next = false;
		if (this.hasSelection())
		{
			for (var i=0; i<this.store.getCount(); i++)
			{
				if (this.isSelected(i))
				{
					if (i < (this.store.getCount() -1))
					{
						next = true;
						break;
					}
				}
			}
		}
		return next;
	}
	
});

Ext.define('LookupDataModel', {
		extend: 'Ext.data.Model',
		fields: [
			{name: 'code', type: 'string'},
			{name: 'description', type: 'string'}
		]
});



//Overrides
Ext.define('Tess.defaults.fieldbase', {
    override: 'Ext.form.field.Base',

	msgTarget: 'side'

});

Ext.define('Tess.defaults.Window', {
    override: 'Ext.window.Window',
	
  ghost: false,
  closeAction: 'hide'
	
});

Ext.define('Tess.defaults.CheckboxModel', {
    override: 'Ext.selection.CheckboxModel',
	
	ignoreRightMouseSelection : true	
});

Ext.define('Tess.defaults.fieldTime', {
    override: 'Ext.form.field.Time',
	
	altFormats : 'g:ia|g:iA|g:i a|g:i A|h:i|g:i|H:i|ga|ha|gA|h a|g a|g A|gi|hi|gia|hia|g|H|gi a|hi a|giA|hiA|gi A|hi A|H:i:s'
});

Ext.define('Tess.defaults.ComboBox', {
    override: 'Ext.form.field.ComboBox'/*,
			
	onBlur: function(cb, e, opts){
		this.clearValue();
	}*/
			
});




//Defaults
// Ext.Ajax.setDefaultHeaders({
//   'Accept': 'application/json, text/plain, */*'
// });

//IE Security adjustments
//Ext.BLANK_IMAGE_URL = '/images/s.gif';
//Ext.SSL_SECURE_URL = '/blank.html';

Ext.onReady(function() {
    
    Ext.tip.QuickTipManager.init();
    Ext.util.History.init();

  /**
    Ext.apply(Ext.tip.QuickTipManager.getQuickTip(), {
        maxWidth: 200,
        minWidth: 100,
        showDelay: 50,
        trackMouse: true
    });
    **/
    Ext.ns('CoreApp');
   
    CoreApp.BTN_OK = 'ok';
    CoreApp.BTN_YES = 'yes';
    
    // 1 min. before notifying the user session will expire. Change this to a reasonable interval.    
    CoreApp.SESSION_ABOUT_TO_TIMEOUT_PROMT_INTERVAL_IN_MIN = 25;
    // 1 min. to kill the session after the user is notified.
    CoreApp.GRACE_PERIOD_BEFORE_EXPIRING_SESSION_IN_MIN = 1;
    // The page that kills the server-side session variables.
    CoreApp.SESSION_KILL_URL = '/openstorefront/Login.action?Logout';
    CoreApp.toMilliseconds = function (minutes) {
      return minutes * 60 * 1000;
    };
    
    CoreApp.sessionAboutToTimeoutPromptTask = new Ext.util.DelayedTask(function () {
        Ext.Msg.confirm(
            'Your Session is About to Expire',
            String.format('Your session will expire in {0} minute(s). Would you like to continue your session?',
                CoreApp.GRACE_PERIOD_BEFORE_EXPIRING_SESSION_IN_MIN),
            function (btn, text) {
                if (btn == CoreApp.BTN_YES) {
                    // Simulate resetting the server-side session timeout timer
                    // by sending an AJAX request.
                    CoreApp.simulateAjaxRequest();
                } else {
                    // Send request to kill server-side session.
                    window.location.replace(CoreApp.SESSION_KILL_URL);
                }
            }
        );
        CoreApp.killSessionTask.delay(CoreApp.toMilliseconds(
        CoreApp.GRACE_PERIOD_BEFORE_EXPIRING_SESSION_IN_MIN));
    });
    CoreApp.killSessionTask = new Ext.util.DelayedTask(function () {        
        window.location.replace(CoreApp.SESSION_KILL_URL);
    });
   
    Ext.Ajax.timeout = 300000;
    Ext.Ajax.listeners = {
      
      requestcomplete: function (conn, response, options) {
       if (options.url !== CoreApp.SESSION_KILL_URL) {
            // Reset the client-side session timeout timers.
            // Note that you must not reset if the request was to kill the server-side session.
            CoreApp.sessionAboutToTimeoutPromptTask.delay(CoreApp.toMilliseconds(CoreApp.SESSION_ABOUT_TO_TIMEOUT_PROMT_INTERVAL_IN_MIN));
            CoreApp.killSessionTask.cancel();
        } else {
            // Notify user her session timed out.
            Ext.Msg.alert(
                'Session Expired',
                'Your session expired. Please login to start a new session.',
                function (btn, text) {
                    if (btn == CoreApp.BTN_OK) {
                        window.location.replace('/openstorefront/Login.action');
                    }
                }
            );
        }

        
      },
      
      requestexception: function(conn, response, options, eOpts) {
       if (response.result) {
          var data = response.result; 
          
          var message = 'Server was unable to process request.';
          if (data.message) {
            message = 'Message: ' + data.message;
          }
          if (data.potentialResolution) {
            message = message + '<br> Potential Resolution: ' + data.potentialResolution;
          }
          if (data.errorTicketNumber) {
            message = message + '<br> Error Ticket: ' + data.errorTicketNumber;
          }
          
          Ext.Msg.show({
              title: 'Server Error',
              message: message,
              buttons: Ext.Msg.OK,
              icon: Ext.Msg.Error,
              fn: function(btn) {                  
              }
          });          
        } else {
          Ext.Msg.show({
            title: 'Connection Error',
            message: 'Unable to connect to server or there was internal server error.',
            buttons: Ext.Msg.OK,
            icon: Ext.Msg.Error,
            fn: function (btn) {
            }
          });
        }
      }
    };
});

//if (Ext.supports.LocalStorage)
//{
//	Ext.state.Manager.setProvider(new Ext.state.LocalStorageProvider());
//}
//else
//{
//	Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
//}



 
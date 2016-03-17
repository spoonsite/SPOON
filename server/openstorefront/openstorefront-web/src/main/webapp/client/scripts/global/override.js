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
Ext.define('OSF.defaults.fieldbase', {
    override: 'Ext.form.field.Base',

	msgTarget: 'side',
   labelClsExtra: 'field-label-basic'

});

Ext.define('OSF.defaults.fieldcontainer', {
    override: 'Ext.form.FieldContainer',

	msgTarget: 'side',
   labelClsExtra: 'field-label-basic'

});

Ext.define('OSF.defaults.checkbox', {
    override: 'Ext.form.field.Checkbox',

	 inputValue : 'true'

});

Ext.define('OSF.defaults.Window', {
    override: 'Ext.window.Window',
	
  ghost: false,
  closeAction: 'hide',
  onRender: function (ct, position) {
	  this.callParent();
	  var win = this;
	  if (Ext.isIE9) {		  
		  win.toggleMaximize(); 
		  win.restore();		  
		  win.setHeight(win.getHeight());
	  }
  },
  afterRender: function(){
		this.callParent();
		
		var win = this;
		
		win.getEl().on('resize', function(){
			win.updateLayout(true, true);
		});
	}
	
});

Ext.define('OSF.defaults.Toast', {
    override: 'Ext.window.Toast',
	
  alwaysOnTop: true
	
});

Ext.define('OSF.defaults.CheckboxModel', {
    override: 'Ext.selection.CheckboxModel',
	
	ignoreRightMouseSelection : true	
});

Ext.define('OSF.defaults.fieldTime', {
    override: 'Ext.form.field.Time',
	
	altFormats : 'g:ia|g:iA|g:i a|g:i A|h:i|g:i|H:i|ga|ha|gA|h a|g a|g A|gi|hi|gia|hia|g|H|gi a|hi a|giA|hiA|gi A|hi A|H:i:s'
});

Ext.define('OSF.defaults.ComboBox', {
    override: 'Ext.form.field.ComboBox',
		
	queryMode: 'local'
	
	/*		
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
	Ext.enableAria = false;
	Ext.enableAriaButtons=false;
	Ext.enableAriaPanels=false;
	
	/**
	 Ext.apply(Ext.tip.QuickTipManager.getQuickTip(), {
	 maxWidth: 200,
	 minWidth: 100,
	 showDelay: 50,
	 trackMouse: true
	 });
	 **/


	Ext.Ajax.timeout = 300000;
	Ext.Ajax.on('requestexception', function (conn, response, options, eOpts) {
		
		var feedbackButtonConfig = {
			yes: 'Ok',
			no: 'Report Issue'
		};
		
		var feedbackHandler = function(details){
			var requestMessage = '';			
			var request = response.request;
			if (request) {
				requestMessage = '( URL: ' + request.url + ' | ';
			     requestMessage += '  Method: ' + request.requestOptions.method + ' | ';
				if (request.requestOptions.data) {
					try {
						requestMessage += '  Data: ' + Ext.JSON.encode(request.requestOptions.data);
					} catch (e) {						
					}
				}
			}
			
			if (details) {
				requestMessage += ' \n '  + details;
			}
			
			var feedbackWin = Ext.create('OSF.component.FeedbackWindow',{				
				closeAction: 'destory',
				hideType: 'Application Issue',
				labelForDescription: 'Please provide detail as to what you are trying to accomplish',
				extraDescription: 'Request: ' + requestMessage
			});
			feedbackWin.show();
		};
		
		var requestUrl = '';
		if (response.request) {
			requestUrl = '<br><hr> ('+ response.request.url + ' <b>' + response.request.requestOptions.method + '</b>)'; 
		}
		
		if (response.status === 403) {
			Ext.Msg.show({
				title: 'Forbidden (403)',
				message: 'Check request.  User may not have access or the request is invalid.' + requestUrl,
				buttons: Ext.MessageBox.YESNO,
				buttonText: feedbackButtonConfig,
				icon: Ext.Msg.Error,
				fn: function (btn) {
					if (btn === 'no') {
						feedbackHandler();
					}
				}
			});			
		} else if (response.status === 404) {
			Ext.toast('Refresh and try again.  Also, check request.' + requestUrl, 'Resource Not Found (404)');
		} else if (response.status === 405) {
			Ext.Msg.show({
				title: 'Bad Client Request (405)',
				message: 'HTTP method is not allowed.  Check request.' + requestUrl,
				buttons: Ext.MessageBox.YESNO,
				buttonText: feedbackButtonConfig,
				icon: Ext.Msg.Error,
				fn: function (btn) {
					if (btn === 'no') {
						feedbackHandler();
					}
				}
			});			
		} else if (response.status === 415) {
			Ext.Msg.show({
				title: 'Bad Client Request (415)',
				message: 'Unsupported content type on the request.  Check request.' + requestUrl,
				buttons: Ext.MessageBox.YESNO,
				buttonText: feedbackButtonConfig,
				icon: Ext.Msg.Error,
				fn: function (btn) {
					if (btn === 'no') {
						feedbackHandler();
					}					
				}
			});			
		} else if (response.status === 400) {
			Ext.Msg.show({
				title: 'Bad Client Request (400)',
				message: 'Check request. ' + requestUrl,
				buttons: Ext.MessageBox.YESNO,
				buttonText: feedbackButtonConfig,
				icon: Ext.Msg.Error,
				fn: function (btn) {
					if (btn === 'no') {
						feedbackHandler();
					}					
				}
			});			
		} else if (response.status === 502) {
			Ext.toast('This likely a temporary condition.  Please try again later.<br>(Attention Admin): Application server is not responding to proxy. <br> Check connection and status of the Application Server. ', 'Server Communication Failure (502)');			
		} else if (response.status === 503) {
			Ext.toast('This likely a temporary condition.  Please try again later.<br>Application is unavaliable. ', 'Service Unavailable (503)');			
		} else {
			var errorTicket = null;
			try {
				errorTicket = Ext.decode(response.responseText);
			} catch (e){		
				Ext.log({level: 'error', stack: true}, 'Unable to decode response');
			}
			if (errorTicket) {
				var data = errorTicket;

				var message = 'Server was unable to process request.';
				if (data.message) {
					message = data.message;
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
					buttons: Ext.MessageBox.YESNO,
					buttonText: feedbackButtonConfig,
					icon: Ext.Msg.Error,
					fn: function (btn) {
						if (btn === 'no') {
							feedbackHandler(message);
						}
					}
				});
			} else {				
				if (response.responseText && response.responseText.indexOf('login.jsp') !== -1) {
					window.parent.location.href = "/openstorefront/login.jsp?gotoPage="+window.parent.location.pathname;
				} else {				
					Ext.toast('Unable to connect to server or there was internal server error.' + requestUrl, 'Connection Error');
				}
			}
		}
	});
    
});

//if (Ext.supports.LocalStorage)
//{
//	Ext.state.Manager.setProvider(new Ext.state.LocalStorageProvider());
//}
//else
//{
//	Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
//}



 
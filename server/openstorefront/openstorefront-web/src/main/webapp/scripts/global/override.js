/* 
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * See NOTICE.txt for more information.
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

/**
 * workaround for bug in ExtJs 6.2.0.
 * Resolved in current yet unreleased version
 */
Ext.define('Mb.override.dom.Element', 
{
    override: 'Ext.dom.Element'
},
function(){
    var additiveEvents = this.prototype.additiveEvents,
        eventMap = this.prototype.eventMap;
    if(Ext.supports.TouchEvents && Ext.firefoxVersion >= 52 && Ext.os.is.Desktop){
        eventMap['touchstart'] = 'mousedown';
        eventMap['touchmove'] = 'mousemove';
        eventMap['touchend'] = 'mouseup';
        eventMap['touchcancel'] = 'mouseup';
        eventMap['click'] = 'click';
        eventMap['dblclick'] = 'dblclick';
        additiveEvents['mousedown'] = 'mousedown';
        additiveEvents['mousemove'] = 'mousemove';
        additiveEvents['mouseup'] = 'mouseup';
        additiveEvents['touchstart'] = 'touchstart';
        additiveEvents['touchmove'] = 'touchmove';
        additiveEvents['touchend'] = 'touchend';
        additiveEvents['touchcancel'] = 'touchcancel';

        additiveEvents['pointerdown'] = 'mousedown';
        additiveEvents['pointermove'] = 'mousemove';
        additiveEvents['pointerup'] = 'mouseup';
        additiveEvents['pointercancel'] = 'mouseup';
    }
});

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

Ext.define('OSF.defaults.toolbar', {
    override: 'Ext.toolbar.Toolbar',

	overflowHandler: 'menu'

});

Ext.define('OSF.defaults.Window', {
    override: 'Ext.window.Window',
	
  ghost: false,
  closeAction: 'hide',
  constrain: true,
  monitorResize: true,
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
	
  alwaysOnTop: true,
  closeAction: 'destroy'
	
});

Ext.define('OSF.defaults.CheckboxModel', {
    override: 'Ext.selection.CheckboxModel',
	
	ignoreRightMouseSelection : true	
});

Ext.define('OSF.defaults.fieldTime', {
    override: 'Ext.form.field.Time',
	
	altFormats : 'g:ia|g:iA|g:i a|g:i A|h:i|g:i|H:i|ga|ha|gA|h a|g a|g A|gi|hi|gia|hia|g|H|gi a|hi a|giA|hiA|gi A|hi A|H:i:s'
});

Ext.define('OSF.defaults.fieldDate', {
    override: 'Ext.form.field.Date',
	
	altFormats : 'c|m/d/Y|n/j/Y|n/j/y|m/j/y|n/d/y|m/j/Y|n/d/Y|m-d-y|m-d-Y|m/d|m-d|md|mdy|mdY|d|Y-m-d|n-j|n/j'
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
	
	Ext.MessageBox.alwaysOnTop=99999999;
	
	//Disable backspace navigation
	var parent = Ext.isIE ? document : window;
	Ext.get(parent).addListener('keydown', function (e, focused) {
		 if (e.getKey() == e.BACKSPACE && (!/^input$/i.test(focused.tagName) && !/^textarea$/i.test(focused.tagName)) || focused.disabled || focused.readOnly) {
			 e.stopEvent();
		 }
	 });
	
	
	/**
	 Ext.apply(Ext.tip.QuickTipManager.getQuickTip(), {
	 maxWidth: 200,
	 minWidth: 100,
	 showDelay: 50,
	 trackMouse: true
	 });
	 **/


	Ext.Ajax.timeout = 590000;

	Ext.Ajax.on('beforerequest', function(conn, option, eOpts){
		var token = Ext.util.Cookies.get('X-Csrf-Token');
		if (token) {
			conn.setDefaultHeaders({
				'X-Csrf-Token': token
			});
		}
	});	
	
	Ext.Ajax.on('requestcomplete', function (conn, response, options, eOpts) {
		if (response.responseText && response.responseText.indexOf('***USER-NOT-LOGIN***') !== -1) {
			var currentlocation = window.parent.location.pathname.replace('/openstorefront', '');
			if (response.request.url.indexOf('service/security/shiroconfig') === -1 &&					
				response.request.url.indexOf('resource/errortickets') === -1) {	
				currentlocation = currentlocation + window.parent.location.search;
				window.parent.location.href = "/openstorefront/Login.action?gotoPage="+encodeURIComponent(currentlocation);
			}
		}		
	});
	
	Ext.Ajax.on('requestexception', function (conn, response, options, eOpts) {
		
		//When logout with OPEN AM it cause a CORS failure in which redirect to login
		//this also occurs on timeout or loss connection
		if (response.status === 0) {
			Ext.Msg.show({
				title: 'Logged Out/Timeout?',
				message: 'You may need to login to continue.',
				buttons: Ext.MessageBox.YESNO,	
				buttonText: {
					yes: 'Login',
					no: 'Cancel'
				},
				icon: Ext.Msg.Error,
				fn: function (btn) {
					if (btn === 'yes') {
						var currentlocation = window.parent.location.pathname.replace('/openstorefront', '');
						currentlocation = currentlocation + window.parent.location.search;
			
						window.parent.location.href = "/openstorefront/Login.action?gotoPage="+encodeURIComponent(currentlocation);					
					}
				}
			});				
		}
		
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
		
		if (response.status === 400) {
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
		} else if (response.status === 403) {
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
			// If the request is a 'retrievemedia' request for backend inline media handling,
			// don't show this toast.
			if (requestUrl.indexOf('retrievemedia') === -1) {
				Ext.toast('Refresh and try again.  Also, check request.' + requestUrl, 'Resource Not Found (404)');
			}
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
		} else if (response.status === 409) {
			var message = "Resource conflict. Check data.";
			try {
				var responseText = Ext.decode(response.responseText);
				for (i = 0; i < responseText.errors.entry.length; i++) {
					message += responseText.errors.entry[i].value;
					if ((i + 1) < responseText.errors.entry.length) {
						message += "<br /><br />";
					}
				}
			} catch (e) {
				//ignore; just use default message.
			}				
			Ext.Msg.show({
				title: 'Conflict (409)',
				message: message,
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
				title: 'Unsupported Content Type (415)',
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
		} else if (response.status === 502) {
			Ext.toast('This likely a temporary condition.  Please try again later.<br>(Attention Admin): Application server is not responding to proxy. <br> Check connection and status of the Application Server. ', 'Server Communication Failure (502)');			
		} else if (response.status === 503) {
			Ext.toast('This likely a temporary condition.  Please try again later.<br>Application is unavaliable. ', 'Service Unavailable (503)');			
		} else if (response.status === 504) {
			Ext.toast('This likely a temporary condition.  Please try again later.<br>Application is unavaliable. ', 'Gateway Timeout (504)');			
		} else if (String(response.status).charAt(0) === 5) {
			Ext.toast('This likely a temporary condition.  Please try again later.<br>Application is unavaliable. ', '5xx Server Error: ' + response.status);			
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
					var currentlocation = window.parent.location.pathname.replace('/openstorefront', '');
					currentlocation = currentlocation + window.parent.location.search;					
					window.parent.location.href = "/openstorefront/Login.action?gotoPage=" + encodeURIComponent(currentlocation);
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

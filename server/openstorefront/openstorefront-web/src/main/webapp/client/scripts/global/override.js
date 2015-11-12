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

   
    Ext.Ajax.timeout = 300000;
    Ext.Ajax.on('requestexception', function(conn, response, options, eOpts){
       var errorTicket = Ext.decode(response.responseText);
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



 
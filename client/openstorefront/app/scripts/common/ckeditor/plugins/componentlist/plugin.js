CKEDITOR.plugins.add( 'componentlist', {
  // This plugin requires the Widgets System defined in the 'widget' plugin.
  requires: 'widget',

  // Register the icon used for the toolbar button. It must be the same
  // as the name of the widget.
  icons: 'componentlist',

  // The plugin initialization logic goes inside this method.
  init: function( editor ) {
    // Register the componentlist widget.
    editor.ui.addButton( 'ComponentButton',
    {
      label: 'Insert Component List',
      command: 'componentlist',
      icon: this.path + '../../../../../images/logo/logo-stamp.svg'
    });

    editor.widgets.add( 'componentlist', {
      // Allow all HTML elements and classes that this widget requires.
      // Read more about the Advanced Content Filter here:
      // * http://docs.ckeditor.com/#!/guide/dev_advanced_content_filter
      // * http://docs.ckeditor.com/#!/guide/plugin_sdk_integration_with_acf
      allowedContent: true,

      // Minimum HTML which is required by this widget to work.
      requiredContent: 'div(componentlist)',

      // Define two nested editable areas.
      editables: {
        // title: {
        //   // Define a CSS selector used for finding the element inside the widget element.
        //   selector: '.noshow.editables',
        //   // Define content allowed in this nested editable. Its content will be
        //   // filtered accordingly and the toolbar will be adjusted when this editable
        //   // is focused.
        //   allowedContent: true
        // }
        hidemore: {
          selector: '.componentlist-hide-more',
          allowedContent: 'br strong em'
        },
        clickcallback: {
          selector: '.componentlist-click-callback',
          allowedContent: 'br strong em'
        },
        classlist: {
          selector: '.componentlist-class-list',
          allowedContent: 'br strong em'
        },
        title: {
          selector: '.componentlist-title',
          allowedContent: 'br strong em'
        },
        data: {
          selector: '.componentlist-data',
          allowedContent: 'br strong em'
        },
        cols: {
          selector: '.componentlist-cols',
          allowedContent: 'br strong em'
        },
        type: {
          selector: '.componentlist-type',
          allowedContent: 'br strong em'
        },
        key: {
          selector: '.componentlist-key',
          allowedContent: 'br strong em'
        },
        filters: {
          selector: '.componentlist-filters',
          allowedContent: 'br strong em'
        },
        setfilters: {
          selector: '.componentlist-set-filters',
          allowedContent: 'br strong em'
        },
        search: {
          selector: '.componentlist-search',
          allowedContent: 'br strong em'
        },
      },

      // Define the template of a new Simple Box widget.
      // The template will be used when creating new instances of the Simple Box widget.
      template: '<div class="componentlist"> ###Component List###' + 
      '<div ng-show="false">' +
      '<label>Hide-more: </label><div class="componentlist-hide-more attribute"><p>hidemore...</p></div><br>' +
      '<label>Click-callback: </label><div class="componentlist-click-callback attribute"><p>clickcallback...</p></div><br>' +
      '<label>Class-list: </label><div class="componentlist-class-list attribute"><p>classlist...</p></div><br>' +
      '<label>Title: </label><div class="componentlist-title attribute"><p>title...</p></div><br>' +
      '<label>Data: </label><div class="componentlist-data attribute"><p>data...</p></div><br>' +
      '<label>Cols: </label><div class="componentlist-cols attribute"><p>cols...</p></div><br>' +
      '<label>Type: </label><div class="componentlist-type attribute"><p>type...</p></div><br>' +
      '<label>Key: </label><div class="componentlist-key attribute"><p>key...</p></div><br>' +
      '<label>Filters: </label><div class="componentlist-filters attribute"><p>filters...</p></div><br>' +
      '<label>Setfilters: </label><div class="componentlist-set-filters attribute"><p>setfilters...</p></div><br>' +
      '<label>Search: </label><div class="componentlist-search attribute"><p>search...</p></div><br>' +
      '</div>' +
      '</div>',
      // template: '<div class="componentlist"><span class="editablespans">hide-more</span></div>',

      // Check the elements that need to be converted to widgets.
      //
      // Note: The "element" argument is an instance of http://docs.ckeditor.com/#!/api/CKEDITOR.htmlParser.element
      // so it is not a real DOM element yet. This is caused by the fact that upcasting is performed
      // during data processing which is done on DOM represented by JavaScript objects.
      upcast: function( element ) {
        // Return "true" (that element needs to converted to a Simple Box widget)
        // for all <div> elements with a "componentlist" class.
        return element.name == 'div' && element.hasClass( 'componentlist' );
      }
    } );
}
} );
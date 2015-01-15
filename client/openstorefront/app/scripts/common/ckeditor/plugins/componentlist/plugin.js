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
          allowedContent: true,
        },
        clickcallback: {
          selector: '.componentlist-click-callback',
          allowedContent: true,
        },
        classlist: {
          selector: '.componentlist-class-list',
          allowedContent: true,
        },
        title: {
          selector: '.componentlist-title',
          allowedContent: true,
        },
        data: {
          selector: '.componentlist-data',
          allowedContent: true,
        },
        cols: {
          selector: '.componentlist-cols',
          allowedContent: true,
        },
        type: {
          selector: '.componentlist-type',
          allowedContent: true,
        },
        key: {
          selector: '.componentlist-key',
          allowedContent: true,
        },
        filters: {
          selector: '.componentlist-filters',
          allowedContent: true,
        },
        setfilters: {
          selector: '.componentlist-set-filters',
          allowedContent: true,
        },
        search: {
          selector: '.componentlist-search',
          allowedContent: true,
        },
      },

      // Define the template of a new Simple Box widget.
      // The template will be used when creating new instances of the Simple Box widget.
      template: '<div class="componentlist"> <div class="componentlistBody" ng-show="false"><h2>Component List</h2>' + 
      '<div ng-show="false">' +
      '<div class="col-sm-6">' +
      '<label>Title: </label><div class="attribute"><p class="componentlist-title" data-attributeLabel="title"></p></div><br>' +
      '<label>Type: </label><div class="attribute"><p class="componentlist-type" data-attributeLabel="type"></p></div><br>' +
      '<label>Key: </label><div class="attribute"><p class="componentlist-key" data-attributeLabel="key"></p></div><br>' +
      '<label>Class-list: </label><div class="attribute"><p class="componentlist-class-list" data-attributeLabel="classlist"></p></div><br>' +
      '<label>Hide-more: </label><div class="attribute"><p class="componentlist-hide-more" data-attributeLabel="hidemore"></p></div><br>' +
      '</div>' +
      '<div class="col-sm-6">' +
      '<label>Filters: </label><div class="attribute"><p class="componentlist-filters" data-attributeLabel="filters">filters</p></div><br>' +
      '<label>Click-callback: </label><div class="attribute"><p class="componentlist-click-callback" data-attributeLabel="clickcallback">updateDetails</p></div><br>' +
      '<label>Data: </label><div class="attribute"><p class="componentlist-data" data-attributeLabel="data">data.data</p></div><br>' +
      '<label>Cols: </label><div class="attribute"><p class="componentlist-cols" data-attributeLabel="cols">3</p></div><br>' +
      '<label>Search: </label><div class="attribute"><p class="componentlist-search" data-attributeLabel="search">doSearchKey</p></div><br>' +
      '</div>' +
      '<div style="clear:both">' +
      '</div>' +
      '</div>' +
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
  }//
} );
// Generated on 2014-05-30 using generator-angular 0.8.0
'use strict';


// Code here will be linted with JSHint.
/* jshint ignore:start */
// Code here will be linted with ignored by JSHint.



// # Globbing
// for performance reasons we're only matching one level down:
// 'test/spec/{,*/}*.js'
// use this if you want to recursively match all subfolders:
// 'test/spec/**/*.js'

module.exports = function (grunt) {

  // Load grunt tasks automatically
  require('load-grunt-tasks')(grunt);

  // Time how long tasks take. Can help when optimizing build times
  require('time-grunt')(grunt);

    grunt.loadNpmTasks('grunt-war');
  grunt.loadNpmTasks("grunt-protractor-runner");

  // Define the configuration for all the tasks
  grunt.initConfig({

     //construct-war
//     war: {
//      target: {
//        options: {
//          war_dist_folder: '<%= yeoman.jvmdist %>',
//          war_verbose: false,
//          war_name: 'openstorefront',
//          webxml_welcome: 'index.html',
//          webxml_display_name: 'Open Storefront',
//          webxml_mime_mapping: [ 
//          { 
//            extension: 'woff', 
//            mime_type: 'application/font-woff' 
//          } ]
//        },
//        files: [
//        {
//          expand: true,
//          cwd: '<%= yeoman.dist %>',
//          src: ['**'],
//          dest: ''
//        }
//        ]
//      }
//    },

    // Project settings
    yeoman: {
      // configurable paths
      app: require('./bower.json').appPath || 'app',
      jvmdist: 'distwar',
      dist: 'dist'
    },

    // Watches files for changes and runs tasks based on the changed files
    watch: {
      bower: {
        files: ['bower.json'],
        tasks: ['bowerInstall']
      },
      js: {
        files: ['<%= yeoman.app %>/scripts/**/*.js'],
        tasks: ['newer:jshint:all'],
        options: {
          livereload: true
        }
      },
      jsTest: {
        files: ['test/spec/**/*.js'],
        tasks: ['newer:jshint:test', 'karma']
      },
      styles: {
        files: ['<%= yeoman.app %>/styles/**/*.css'],
        tasks: ['newer:copy:styles', /*'autoprefixer'*/]
      },
      compass: {
      files: ['<%= yeoman.app %>/styles/{,*/}*.{scss,sass}'],
      tasks: ['compass:server', /*'autoprefixer'*/]
    },
    gruntfile: {
      files: ['Gruntfile.js']
    },
    livereload: {
      options: {
        livereload: '<%= connect.options.livereload %>'
      },
      files: [
      '<%= yeoman.app %>/**/*.html',
      '.tmp/styles/**/*.css',
      '<%= yeoman.app %>/images/**/*.{PNG,png,jpg,jpeg,gif,webp,svg}',
    '<%= yeoman.app %>/styles/{,*/}*.{scss,sass}'
    ]
  }
},

    // The actual grunt server settings
    connect: {
          options: {
           port: 9000,
        // Change this to '0.0.0.0' to access the server from outside.
        hostname: 'localhost',
        livereload: 35729
      },
      livereload: {
        options: {
          open: true,
          base: [
          '.tmp',
          '<%= yeoman.app %>'
          ]
        }
      },
      test: {
        options: {
          port: 9001,
          base: [
          '.tmp',
          'test',
          '<%= yeoman.app %>'
          ]
        }
      },
      dist: {
        options: {
          base: '<%= yeoman.dist %>'
        }
      }
    },

    // Compiles Sass to CSS and generates necessary files if requested
    compass: {
      options: {
        sassDir: '<%= yeoman.app %>/styles',
        cssDir: '.tmp/styles',
        // generatedImagesDir: '.tmp/images/generated',
        imagesDir: /*grunt.option('appPath')? grunt.option('appPath') + '/images' :*/  '<%= yeoman.app %>/images',
        httpImagesPath: grunt.option('appPath')? grunt.option('appPath') + '/images': '/images',
        javascriptsDir: '<%= yeoman.app %>/scripts',
        importPath: '<%= yeoman.app %>/bower_components',
        httpGeneratedImagesPath: '/images/generated',
        fontsDir: grunt.option('appPath')? grunt.option('appPath') + '/fonts' : '<%= yeoman.app %>/fonts',
        httpFontsPath: grunt.option('appPath')? grunt.option('appPath') + '/fonts' : '/fonts',
        relativeAssets: false,
        assetCacheBuster: false,
        raw: 'Sass::Script::Number.precision = 10\n'
      },
      dist: {
        options: {
        }
      },
      server: {
        options: {
          debugInfo: true
        }
      }
    },

    // Make sure code styles are up to par and there are no obvious mistakes
    jshint: {
      options: {
        jshintrc: '.jshintrc',
        jshintignore: '.jshintignore',
        reporter: require('jshint-stylish')
      },
      all: [
      'Gruntfile.js',
      '<%= yeoman.app %>/scripts/common/*.js',
      '<%= yeoman.app %>/scripts/controllers/*.js',
      '<%= yeoman.app %>/scripts/directives/*.js',
      '<%= yeoman.app %>/scripts/filters/*.js',
      '<%= yeoman.app %>/scripts/page_specific/*.js',
      '<%= yeoman.app %>/scripts/services/*.js',
      '<%= yeoman.app %>/scripts/*.js'
      ],
      test: {
        options: {
          jshintrc: 'test/.jshintrc'
        },
        src: ['test/spec/**/*.js']
      }
    },

    // Empties folders to start fresh
    clean: {
      dist: {
        files: [{
          dot: true,
          src: [
          '.tmp',
          '<%= yeoman.dist %>/*',
          '!<%= yeoman.dist %>/.git*'
          ]
        }]
      },
      server: '.tmp',
      serverweb: {
        options: { 
          force: true 
        },
        files: [{
          dot: true,          
          src: [                  
            '../../server/openstorefront/openstorefront-web/src/main/webapp/bower_components',
            '../../server/openstorefront/openstorefront-web/src/main/webapp/fonts',
            '../../server/openstorefront/openstorefront-web/src/main/webapp/images',
            '../../server/openstorefront/openstorefront-web/src/main/webapp/scripts',
            '../../server/openstorefront/openstorefront-web/src/main/webapp/styles',
            '../../server/openstorefront/openstorefront-web/src/main/webapp/views'
          ]
        }]
      }      
    },

    // Add vendor prefixed styles
    autoprefixer: {
      options: {
        browsers: ['last 1 version']
      },
      dist: {
        files: [{
          expand: true,
          cwd: '.tmp/styles/',
          src: '**/*.css',
          dest: '.tmp/styles/'
        }]
      }
    },

    // Automatically inject Bower components into the app
    bowerInstall: {
      app: {
        src: ['<%= yeoman.app %>/index.html'],
        ignorePath: '<%= yeoman.app %>/'
      },
      sass: {
      src: ['<%= yeoman.app %>/styles/{,*/}*.{scss,sass}'],
      ignorePath: '<%= yeoman.app %>/bower_components/'
    }
  },

    // Renames files for browser caching purposes
    rev: {
      dist: {
        files: {
          src: [
          '<%= yeoman.dist %>/scripts/**/*.js',
          '<%= yeoman.dist %>/styles/**/*.css',
          ]
        }
      }
    },

    // Reads HTML for usemin blocks to enable smart builds that automatically
    // concat, minify and revision files. Creates configurations in memory so
    // additional tasks can operate on them
    useminPrepare: {
      html: ['<%= yeoman.app %>/index.html','<%= yeoman.app %>/views/results.html', '<%= yeoman.app %>/views/single.html', '<%= yeoman.app %>/views/main.html', '<%= yeoman.app %>/views/admin.html'],
      options: {
        dest: '<%= yeoman.dist %>',
        flow: {
          html: {
            steps: {
              js: ['concat', 'uglifyjs'],
              css: ['cssmin']
            },
            post: {}
          }
        }
      }
    },

    // Performs rewrites based on rev and the useminPrepare configuration
    usemin: {
      html: ['<%= yeoman.dist %>/**/*.html'],
      css: ['<%= yeoman.dist %>/styles/**/*.css'],
      options: {
        assetsDirs: ['<%= yeoman.dist %>']
      }
    },

    // The following *-min tasks produce minified files in the dist folder
    cssmin: {
      options: {
        root: '<%= yeoman.app %>'
      }
    },

    imagemin: {
      dist: {
        files: [{
          expand: true,
          cwd: '<%= yeoman.app %>/images',
          src: '**/*.{PNG,png,jpg,jpeg,gif}',
          dest: '<%= yeoman.dist %>/images'
        }]
      }
    },

    svgmin: {
      dist: {
        files: [{
          expand: true,
          cwd: '<%= yeoman.app %>/images',
          src: '**/*.svg',
          dest: '<%= yeoman.dist %>/images'
        }]
      }
    },

    htmlmin: {
      dist: {
        options: {
          collapseWhitespace: true,
          collapseBooleanAttributes: true,
          removeCommentsFromCDATA: true,
          removeOptionalTags: true
        },
        files: [{
          expand: true,
          cwd: '<%= yeoman.dist %>',
          src: ['*.html', 'views/**/*.html'],
          dest: '<%= yeoman.dist %>'
        }]
      }
    },

    // ngmin tries to make the code safe for minification automatically by
    // using the Angular long form for dependency injection. It doesn't work on
    // things like resolve or inject so those have to be done manually.
    ngmin: {
      dist: {
        files: [{
          expand: true,
          cwd: '.tmp/concat/scripts',
          src: '*.js',
          dest: '.tmp/concat/scripts'
        }]
      }
    },

    // Replace Google CDN references
    cdnify: {
      dist: {
        html: ['<%= yeoman.dist %>/*.html']
      }
    },

    // Copies remaining files to places other tasks can use
    copy: {
      dist: {
        files: [{
          expand: true,
          dot: true,
          cwd: '<%= yeoman.app %>',
          dest: '<%= yeoman.dist %>',
          src: [
          '*.{ico,txt}',
          '.htaccess',
          '*.html',
          'views/**/*.html',
          'images/**/*',
          'bower_components/fontawesome/css/font-awesome.css',
          'bower_components/fontawesome/fonts/*',
          'bower_components/bootstrap/dist/fonts/*',
          'bower_components/bootstrap/dist/css/bootstrap.css',
          'bower_components/angular-mocks/angular-mocks.js',
          'scripts/common/angular-lightbox.js',
          'scripts/common/ng-ckeditor.js',
          'bower_components/ckeditor/**/*',
          'styles/*.css',
          'scripts/esapi4js/**/*',
          'scripts/common/data.js',
          'scripts/common/dropzone.js',
          'scripts/common/jquery-cron.js',
          'scripts/common/angular-multi-select.js',
          'scripts/common/cronGen.js'
          ]
        }, {
          expand: true,
          cwd: '<%= yeoman.app %>',
          dest: '<%= yeoman.dist %>/fonts',
          src: [
          'bower_components/fontawesome/fonts/*',
          'bower_components/bootstrap/dist/fonts/*',
          'bower_components/typicons/fonts/*'
          ],
          flatten: true  
        }, {
          expand: true,
          cwd: '.tmp/styles/',
          dest: '<%= yeoman.dist %>/styles',
          src:'print.css'
        }]
      },
      styles: {
        expand: true,
        cwd: '<%= yeoman.app %>/styles',
        dest: '.tmp/styles/',
        src: '**/*.css'
      },
      fonts: {
        expand: true,
        cwd: '<%= yeoman.app %>/fonts',
        dest: '<%= yeoman.dist %>/fonts',
        src: '**/*'
      },
      server: {
        expand: true,
        cwd: '<%= yeoman.dist %>',
        dest: '../../server/openstorefront/openstorefront-web/src/main/webapp',
        src: '**/*'        
      },
      compstyles: {
        expand: true,
        cwd: '.tmp/styles',
        dest: '<%= yeoman.dist %>/styles',
        src: '**/*.css'
      },      
      all: {
          expand: true,
          dot: true,
          cwd: '<%= yeoman.app %>',
          dest: '<%= yeoman.dist %>',
          src: [
            'bower_components/**/*',
            'fonts/**/*',
            'images/**/*',
            'scripts/**/*',
            'views/**/*',
            '404.html',
            'favicon.ico',
            'index.html',
            'robots.txt'
          ]
      }
    },

    // Run some tasks in parallel to speed up the build process
    concurrent: {
      server: [
      'compass:server',
      'copy:styles'
      ],
      test: [
      'compass',
      'copy:styles'
      ],
      dist: [
      'compass:dist',
      'copy:styles',
      'svgmin'
      ]//,
      // prod: [
      //   'compass:prod',
      //   'copy:styles',
      //   'svgmin'
      // ]
    },

    // By default, your `index.html`'s <!-- Usemin block --> will take care of
    // minification. These next options are pre-configured if you do not wish
    // to use the Usemin blocks.
    // cssmin: {
    //   dist: {
    //     files: {
    //       '<%= yeoman.dist %>/styles/main.css': [
    //         '.tmp/styles/**/*.css',
    //         '<%= yeoman.app %>/styles/**/*.css'
    //       ]
    //     }
    //   }
    // },
    // uglify: {
    //   dist: {
    //     files: {
    //       '<%= yeoman.dist %>/scripts/scripts.js': [
    //         '<%= yeoman.dist %>/scripts/*.js'
    //       ]
    //     }
    //   }
    // },
    // concat: {
    //   dist: {}
    // },

    // Test settings
    karma: {
      unit: {
        configFile: 'karma.conf.js',
        singleRun: true
      }
    },
    protractor: {
      options: {
        keepAlive: true,
        configFile: "test/protractor.conf.js",
        args: {
          seleniumServerJar: 'node_modules/protractor/selenium/selenium-server-standalone-2.42.0.jar',
          //ieDriver: 'node_modules/protractor/selenium/IEDriverServer.exe',
          chromeDriver: 'node_modules/protractor/selenium/chromedriver.exe'
        }
      },
      run: {}
    },
  });


grunt.registerTask('serve', function (target) {
  if (target === 'dist') {
    return grunt.task.run(['build', 'connect:dist:keepalive']);
  }

  grunt.task.run([
    'clean:server',
    'bowerInstall',
    'concurrent:server',
      // 'autoprefixer',
      'connect:livereload',
      'watch'
      ]);
});

grunt.registerTask('server', function (target) {
  grunt.log.warn('The `server` task has been deprecated. Use `grunt serve` to start a server.');
  grunt.task.run(['serve:' + target]);
});

grunt.registerTask('test', [
  'clean:server',
  'concurrent:test',
    // 'autoprefixer',
    'connect:test',
    'karma'
    ]);
    
grunt.registerTask('test-e2e', [
  'clean:server',
  'protractor:run'
    ]);

grunt.registerTask('build', function (target) {
  grunt.task.run([
    'clean:dist',
    'bowerInstall',
    'useminPrepare',
    'concurrent:dist',
        // 'autoprefixer',
        'concat',
        'ngmin',
        'copy:dist',
        'copy:fonts',
        'cdnify',
        'cssmin',
        'uglify',
        'rev',
        'usemin',
        'htmlmin'
        //'war'
        ]);
});

grunt.registerTask('buildprod', function (target) {
  grunt.option('appPath', '/openstorefront');
  grunt.task.run([ 
    'build',
    'clean:serverweb',
    'copy:server'  
  ]);
});  

grunt.registerTask('build-debug', function (target) {
  grunt.option('appPath', '/openstorefront');
  grunt.task.run([
    'clean:dist',
    'clean:serverweb',
    'bowerInstall',
    'copy:all',
    'concurrent:dist',
    'copy:compstyles',    
    'cdnify',
    'copy:server'
    ]);
});

grunt.registerTask('default', [
  'newer:jshint',
  'test',
  'build'
  ]);
};
/* jshint ignore:end */

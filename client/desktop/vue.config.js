module.exports = {
  devServer: {
    proxy: {
      '/openstorefront': {
        // ***PRODUCTION INSTANCES***
        // 'target': 'https://spoonsite.com',
        // ***STAGING ENVIRONMENTS***
        // 'target': 'http://spoon-staging.usurf.usu.edu',
        // 'target': 'http://store-accept.usurf.usu.edu',
        'target': 'http://localhost:8080',
        'changeOrigin': true
      },
      '/images': {
        // urlrewrite will handle this in production
        // to resolve properly in development it needs to be passed back
        // through the proxy

        // ***PRODUCTION INSTANCES***
        // 'target': 'https://spoonsite.com',
        // ***STAGING ENVIRONMENTS***
        // 'target': 'http://spoon-staging.usurf.usu.edu',
        // 'target': 'http://store-accept.usurf.usu.edu/openstorefront',
        'target': 'http://localhost:8080/openstorefront',
        'changeOrigin': true
      },
      '/Branding.action': {
        // again this is for the urlrewrite to work
        // TODO: handle these cases better for the dev server
        'target': 'http://localhost:8080/openstorefront',
        'changeOrigin': true
      }
    }
  },
  // Sets where the client will look for the css, js files on the server
  publicPath: process.env.NODE_ENV === 'production' ? '/openstorefront/desktop' : '/',
  // Sets where the compiled Vue code (aka the 'dist' folder ) will be placed.
  outputDir: '../../server/openstorefront/openstorefront-web/src/main/webapp/desktop',
  // sets where assets (js,css) are placed after compilation relative to outputDir
  assetsDir: ''
}

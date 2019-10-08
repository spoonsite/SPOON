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
  }
}

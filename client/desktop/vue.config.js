module.exports = {
  devServer: {
    proxy: {
      '/openstorefront': {
        // ***PRODUCTION INSTANCES***
        // 'target': 'https://spoonsite.com',
        // 'target': 'https://storefront.di2e.net/openstorefront/',
        // ***STAGING ENVIRONMENTS***
        // 'target': 'http://spoon-staging.usurf.usu.edu',
        // 'target': 'http://store-accept.usurf.usu.edu',
        'target': 'http://localhost:8080',
        'changeOrigin': true
      }
    }
  }
}

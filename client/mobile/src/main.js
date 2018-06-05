// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue';
import App from './App';
import router from './router';
import Vuetify from 'vuetify';
import 'vuetify/dist/vuetify.min.css';
import format from 'date-fns/format';
import 'babel-polyfill';
import VueTruncate from 'vue-truncate-filter';
import axios from 'axios';

Vue.config.productionTip = false;

Vue.prototype.$http = axios;

// Add CSRF Token if it comes in on a cookie
let csrfToken;
axios.interceptors.request.use(
  function (config) {
    if (!csrfToken) {
      let split = document.cookie.split('=');
      let tokenIndex = split.indexOf('X-Csrf-Token');
      if (tokenIndex !== -1) {
        csrfToken = split[tokenIndex + 1];
        axios.defaults.headers.common = {
          'X-Requested-With': 'XMLHttpRequest',
          withCredentials: true,
          'Access-Control-Allow-Credentials': true,
          'X-Csrf-Token': csrfToken
        };
      }
    }
    return config;
  },
  function (error) {
    // Do something with request error
    return Promise.reject(error);
  }
);

Vue.use(Vuetify, {
  theme: {
    primary: '#2a2d35',
    secondary: '#333842',
    accent: '#565656',
    error: '#C62828',
    info: '#3F51B5',
    warning: '#FFA000',
    success: '#388E3C'
  }
});

Vue.use(VueTruncate);
Vue.filter('formatDate', value => {
  return format(value, 'YYYY/MM/DD');
});
Vue.filter('prettyJSON', function (value) {
  return JSON.stringify(JSON.parse(value));
});

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
});

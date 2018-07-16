// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue';
import App from './App';
import router from './router';
import store from './store';

import Vuetify from 'vuetify';
import 'vuetify/dist/vuetify.min.css';
import format from 'date-fns/format';
import 'babel-polyfill';
import VueTruncate from 'vue-truncate-filter';
import axios from 'axios';
import Cookies from 'js-cookie';
import VueQuillEditor from 'vue-quill-editor';
import 'quill/dist/quill.core.css';
import 'quill/dist/quill.snow.css';
import Toasted from 'vue-toasted';

Vue.config.productionTip = false;

Vue.prototype.$http = axios;

// Add CSRF Token on every request
axios.interceptors.request.use(
  function (config) {
    let csrfToken = Cookies.get('X-Csrf-Token');
    config.headers = {
      'X-Requested-With': 'XMLHttpRequest',
      withCredentials: true,
      'Access-Control-Allow-Credentials': true,
      'X-Csrf-Token': csrfToken
    };
    return config;
  },
  function (error) {
    // Do something with request error
    return Promise.reject(error);
  }
);

Vue.use(VueQuillEditor, {
  modules: { toolbar: [[{'header': 1}, {'header': 2}], ['bold', 'italic'], [{'list': 'bullet'}, {'list': 'ordered'}], ['clean']]
  }
});
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
Vue.use(Toasted, {
  iconPack: 'fontawesome',
  duration: 5000
});

Vue.prototype.$http = axios;
Vue.use(VueTruncate);
Vue.filter('formatDate', value => format(value, 'YYYY/MM/DD'));
Vue.filter('formatWatchDate', value => format(value, 'YYYY/MM/DD hh:mm'));
Vue.filter('prettyJSON', value => JSON.stringify(JSON.parse(value)));

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
});

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

Vue.config.productionTip = false;

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
Vue.filter('formatDate', (value) => {
  return format(value, 'YYYY/MM/DD');
});

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
});

// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue';
import Vuetify from 'vuetify';
import 'vuetify/dist/vuetify.min.css';
import Toasted from 'vue-toasted';
import 'babel-polyfill';
import axios from 'axios';
import App from './App';
import router from './router';
import store from './store';

Vue.config.productionTip = false;

Vue.prototype.$http = axios;
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

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: {
    App
  },
  template: '<App/>'
});

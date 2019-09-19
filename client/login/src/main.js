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

store.dispatch('getSecurityPolicy');
store.dispatch('getBranding', () => {
  Vue.use(Vuetify, {
    theme: {
      primary: store.state.branding.vuePrimaryColor,
      secondary: store.state.branding.vueSecondaryColor,
      accent: store.state.branding.vueAccentColor,
      error: store.state.branding.vueErrorColor,
      info: store.state.branding.vueInfoColor,
      warning: store.state.branding.vueWarningColor,
      success: store.state.branding.vueSuccessColor
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
});

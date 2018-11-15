import Vue from 'vue';
import Vuex from 'vuex';
import axios from 'axios';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    securitypolicy: {},
    branding: {}
  },
  mutations: {
    setSecurityPolicy (state, response) {
      state.securitypolicy = response.data;
    },
    setBranding (state, response) {
      state.branding = response.data;
    }
  },
  actions: {
    getSecurityPolicy (context) {
      axios.get('/openstorefront/api/v1/resource/securitypolicy')
        .then(response => {
          context.commit('setSecurityPolicy', response);
        });
    },
    getBranding (context, callback) {
      axios.get('/openstorefront/api/v1/resource/branding/current')
        .then(response => {
          context.commit('setBranding', response);
        })
        .finally(() => {
          callback();
        });
    }
  },
  getters: {

  }
});

import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    securitypolicy: {},
    branding: {}
  },
  mutations: {
    setTitle (state, payload) {
      state.aTitle = payload.title;
    },
    setSecurityPolicy (state, response) {
      state.securitypolicy = response.data;
    },
    setBranding (state, response) {
      state.branding = response.data.filter(branding => branding.activeStatus === 'A')[0];
      state.branding.loginLogoBlock = state.branding.loginLogoBlock.replace(/branding\.action/i, '/openstorefront/Branding.action');
    }
  },
  actions: {
    getSecurityPolicy (context, axios) {
      axios.get('/openstorefront/api/v1/resource/securitypolicy')
        .then(response => {
          context.commit('setSecurityPolicy', response);
        });
    },
    getBranding (context, axios) {
      axios.get('/openstorefront/api/v1/resource/branding')
        .then(response => {
          context.commit('setBranding', response);
        });
    }
  },
  getters: {

  }
});

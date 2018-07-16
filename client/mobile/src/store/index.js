import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    currentUser: {},
    branding: {}
  },
  // mutations must be synchronous
  mutations: {
    setCurrentUser (state, response) {
      state.currentUser = response.data;
    },
    setBranding (state, response) {
      state.branding = response.data;
      state.branding.loginLogoBlock = state.branding.loginLogoBlock.replace(/branding\.action/i, '/openstorefront/Branding.action');
    }
  },
  actions: {
    setCurrentUser (context, axios) {
      axios.get('/openstorefront/api/v1/resource/userprofiles/currentuser')
        .then(response => {
          context.commit('setCurrentUser', response);
        });
    },
    getBranding (context, config) {
      config.axios.get('/openstorefront/api/v1/resource/branding/current')
        .then(response => {
          context.commit('setBranding', response);
        })
        .finally(() => {
          if (config.callback) {
            config.callback();
          }
        });
    }
  },
  getters: {

  }
});

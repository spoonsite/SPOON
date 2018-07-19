import Vue from 'vue';
import Vuex from 'vuex';
import axios from 'axios';

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
      for (var key in state.branding) {
        if (typeof state.branding[key] === 'string') {
          state.branding[key] = state.branding[key].replace(/branding\.action/ig, '/openstorefront/Branding.action');
        }
      }
    }
  },
  actions: {
    setCurrentUser (context) {
      axios.get('/openstorefront/api/v1/resource/userprofiles/currentuser')
        .then(response => {
          context.commit('setCurrentUser', response);
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

import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    currentUser: {}
  },
  // mutations must be synchronous
  mutations: {
    setCurrentUser (state, response) {
      state.currentUser = response.data;
    }
  },
  actions: {
    setCurrentUser (context, config) {
      config.axios.get('/openstorefront/api/v1/resource/userprofiles/currentuser')
        .then(response => {
          context.commit('setCurrentUser', response);
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

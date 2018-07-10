import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    aTitle: 'SPOON',
    aSubtitle: 'SmallSat Parts On Orbit Now(SPOON) is a registry of Small - Satellite Equipment, Software and Services',
    securitypolicy: {}
  },
  mutations: {
    setTitle (state, payload) {
      state.aTitle = payload.title;
    },
    setSecurityPolicy (state, response) {
      state.securitypolicy = response.data;
    }
  },
  actions: {
    getSecurityPolicy (context, axios) {
      axios.get('/openstorefront/api/v1/resource/securitypolicy')
        .then(response => {
          context.commit('setSecurityPolicy', response);
        });
    }
  },
  getters: {

  }
});

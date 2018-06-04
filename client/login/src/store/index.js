import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    aTitle: 'SPOON',
    aSubtitle: 'SmallSat Parts On Orbit Now(SPOON) is a registry of Small - Satellite Equipment, Software and Services'
  },
  mutations: {
    setTitle (state, payload) {
      state.aTitle = payload.title;
    }
  },
  getters: {

  }
});

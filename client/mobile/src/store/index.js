import Vue from 'vue';
import Vuex from 'vuex';
import axios from 'axios';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    currentUser: {},
    branding: {},
    securitypolicy: {},
    permissionMap: []
  },
  // mutations must be synchronous
  mutations: {
    setSecurityPolicy (state, response) {
      state.securitypolicy = response.data;
    },
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
    },
    setPermissionMap (state, response) {
      response.data.roles.forEach(roles => {
        roles.permissions.forEach(permission => {
          let found = false;
          state.permissionMap.forEach(search => {
            if (permission.permission === search) found = true;
          });
          if (!found) state.permissionMap.push(permission.permission);
        });
      });
    }
  },
  actions: {
    getSecurityPolicy (context) {
      axios.get('/openstorefront/api/v1/resource/securitypolicy')
        .then(response => {
          context.commit('setSecurityPolicy', response);
        });
    },
    setCurrentUser (context) {
      axios.get('/openstorefront/api/v1/resource/userprofiles/currentuser')
        .then(response => {
          context.commit('setCurrentUser', response);
          context.commit('setPermissionMap', response);
        })
        .finally(() => {
          if (context.callback) {
            context.callback();
          }
        });
    },
    getBranding (context, callback) {
      axios.get('/openstorefront/api/v1/resource/branding/current')
        .then(response => {
          context.commit('setBranding', response);
        })
        .finally(() => {
          if (callback) {
            callback();
          }
        });
    }
  },
  getters: {
    // call this.$store.getters.hasPermission('ADMIN-...')
    hasPermission: (state) => (search) => state.permissionMap.indexOf(search)
  }
});

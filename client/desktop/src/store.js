import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    currentUser: {},
    branding: {},
    securityPolicy: {},
    permissionMap: [],
    appVersion: '',
    componentTypeList: [],
    selectedComponentTypes: []
  },
  // mutations must be synchronous
  mutations: {
    setSecurityPolicy (state, response) {
      state.securityPolicy = response.data
    },
    setCurrentUser (state, response) {
      state.currentUser = response.data
    },
    setAppVersion (state, response) {
      state.appVersion = response.data
    },
    setBranding (state, response) {
      state.branding = response.data
    },
    setPermissionMap (state, response) {
      response.data.roles.forEach(roles => {
        roles.permissions.forEach(permission => {
          let found = false
          state.permissionMap.forEach(search => {
            if (permission.permission === search) found = true
          })
          if (!found) state.permissionMap.push(permission.permission)
        })
      })
    },
    setComponentTypeList (state, response) {
      state.componentTypeList = response.data
    },
    setSelectedComponentTypes (state, response) {
      state.selectedComponentTypes = response.data
    }
  },
  actions: {
    getSecurityPolicy (context) {
      axios.get('/openstorefront/api/v1/resource/securitypolicy')
        .then(response => {
          context.commit('setSecurityPolicy', response)
        })
    },
    getCurrentUser (context) {
      axios.get('/openstorefront/api/v1/resource/userprofiles/currentuser')
        .then(response => {
          context.commit('setCurrentUser', response)
          context.commit('setPermissionMap', response)
        })
        .finally(() => {
          if (context.callback) {
            context.callback()
          }
        })
    },
    getAppVersion (context) {
      axios.get('/openstorefront/api/v1/service/application/version')
        .then(response => {
          context.commit('setAppVersion', response)
        })
    },
    getBranding (context, callback) {
      axios.get('/openstorefront/api/v1/resource/branding/current')
        .then(response => {
          context.commit('setBranding', response)
        })
        .finally(() => {
          if (callback) {
            callback()
          }
        })
    },
    getComponentTypeList (context) {
      axios.get('/openstorefront/api/v1/resource/componenttypes')
        .then(response => {
          context.commit('setComponentTypeList', response)
        })
    }
  },
  getters: {
    // call this.$store.getters.hasPermission('ADMIN-...')
    hasPermission: (state) => (search) => state.permissionMap.indexOf(search) >= 0,
    getSelectedComponentTypes: (state) => state.selectedComponentTypes
  }
})

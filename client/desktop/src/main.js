import Vue from 'vue'
import App from './App.vue'
import router from './router.js'
import store from './store.js'

import Vuetify from 'vuetify'
import 'vuetify/dist/vuetify.min.css'
import format from 'date-fns/format'
import 'babel-polyfill'
import VueTruncate from 'vue-truncate-filter'
import axios from 'axios'
import Cookies from 'js-cookie'
import VueQuillEditor from 'vue-quill-editor'
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import Toasted from 'vue-toasted'

Vue.config.productionTip = false

Vue.prototype.$http = axios
Vue.prototype.$cookies = Cookies

// Add CSRF Token on every request
axios.interceptors.request.use(
  function (config) {
    let csrfToken = Cookies.get('X-Csrf-Token')
    config.headers = {
      'X-Requested-With': 'XMLHttpRequest',
      withCredentials: true,
      'Access-Control-Allow-Credentials': true,
      'X-Csrf-Token': csrfToken
    }
    return config
  },
  function (error) {
    // Do something with request error
    return Promise.reject(error)
  }
)
Vue.prototype.$http = axios

Vue.prototype.$jsonparse = (json) => {
  let parsed = {}
  try {
    parsed = JSON.parse(json)
  } catch (e) {
    console.log('Failed to parse json: ', e)
  }
  return parsed
}

Vue.use(Toasted, {
  iconPack: 'fontawesome',
  duration: 5000
})
Vue.use(VueTruncate)
Vue.use(VueQuillEditor, {
  modules: { toolbar: [
    [{ 'header': 1 }, { 'header': 2 }], ['bold', 'italic'],
    [{ 'list': 'bullet' }, { 'list': 'ordered' }], ['clean']]
  }
})

Vue.filter('formatDate', function (value, formatString) {
  if (formatString) {
    return format(value, formatString)
  } else {
    return format(value, 'YYYY/MM/DD')
  }
})
Vue.filter('prettyJSON', value => JSON.stringify(JSON.parse(value)))
store.dispatch('getSecurityPolicy')
store.dispatch('getHelpUrl')
store.dispatch('getBranding', () => {
  Vue.use(Vuetify, {
    theme: {
      primary: store.state.branding.vuePrimaryColor,
      secondary: store.state.branding.vueSecondaryColor,
      accent: store.state.branding.vueAccentColor,
      error: store.state.branding.vueErrorColor,
      info: store.state.branding.vueInfoColor,
      warning: store.state.branding.vueWarningColor,
      success: store.state.branding.vueSuccessColor,
    }
  })

  Vue.config.productionTip = false

  new Vue({
    router,
    store,
    render: h => h(App)
  }).$mount('#app')
})

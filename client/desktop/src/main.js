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
store.dispatch('getBranding', () => {
  Vue.use(Vuetify, {
    theme: {
      primary: store.state.branding.primaryColor,
      secondary: store.state.branding.secondaryColor,
      accent: '#424242', // accent should be dark, used as button default
      error: '#C62828',
      info: '#3F51B5',
      warning: '#FFA000',
      success: '#388E3C'
    }
  })

  Vue.config.productionTip = false

  new Vue({
    router,
    store,
    render: h => h(App)
  }).$mount('#app')
})

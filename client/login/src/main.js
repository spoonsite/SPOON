import Vue from 'vue'
import Vuetify from 'vuetify'
import 'vuetify/dist/vuetify.min.css'
import Toasted from 'vue-toasted'
import Cookies from 'js-cookie'
import axios from 'axios'
import App from './App.vue'
import router from './router'
import store from './store'

Vue.config.productionTip = false

// Add CSRF Token on every request
axios.interceptors.request.use(
  function (config) {
    const csrfToken = Cookies.get('X-Csrf-Token')
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

store.dispatch('getSecurityPolicy')
store.dispatch('getBranding', () => {
  Vue.use(Vuetify)

  const vuetify = new Vuetify({
    theme: {
      themes: {
        light: {
          primary: store.state.branding.vuePrimaryColor,
          secondary: store.state.branding.vueSecondaryColor,
          accent: store.state.branding.vueAccentColor,
          error: store.state.branding.vueErrorColor,
          info: store.state.branding.vueInfoColor,
          warning: store.state.branding.vueWarningColor,
          success: store.state.branding.vueSuccessColor
        }
      }
    }
  })

  Vue.config.productionTip = false

  new Vue({
    router,
    store,
    vuetify,
    render: h => h(App)
  }).$mount('#app')
})

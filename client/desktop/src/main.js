import Vue from 'vue'
import App from './App.vue'
import router from './router.js'
import store from './store.js'
import scientificToDecimal from './util/scientificToDecimal'

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

Vue.filter('crushNumericString', function (inputNumber) {
  // If inputNumber is not a number return.
  if (isNaN(inputNumber)) {
    return inputNumber
  }
  // If it contains an E or e don't touch it and return.
  if (inputNumber.indexOf('E') !== -1) {
    inputNumber = scientificToDecimal.scientificToDecimal(inputNumber)
  }
  if (inputNumber.indexOf('e') !== -1) {
    inputNumber = scientificToDecimal.scientificToDecimal(inputNumber)
  }

  var magnitudeIsGreaterThanOne = false
  var numberLength = inputNumber.length

  // Is the absolute value of this number bigger than one?
  if (Math.abs(inputNumber) > 1) {
    magnitudeIsGreaterThanOne = true
  }

  // If it is take this route
  if (magnitudeIsGreaterThanOne) {
    if (inputNumber.indexOf('.') !== -1) {
      if ((numberLength - inputNumber.indexOf('.')) > 5) {
        return parseFloat(parseFloat(inputNumber.slice(0, inputNumber.indexOf('.') + 4)).toFixed(3))
      }
      return parseFloat(inputNumber)
    }
  }

  // Otherwise take this route
  if (!magnitudeIsGreaterThanOne) {
    // Find first non zero thing after the decimal and show 3 decimal places after it.
    var firstNonZeroIndex
    for (var i = 0; i < numberLength; i++) {
      if ((inputNumber[i] === '-') || (inputNumber[i] === '.') || (inputNumber[i] === '0')) {
        continue
      }
      firstNonZeroIndex = i
      break
    }
    if (numberLength - firstNonZeroIndex > 5) {
      return parseFloat(parseFloat(inputNumber.slice(0, firstNonZeroIndex + 4)).toFixed(firstNonZeroIndex + 1))
    }
    return parseFloat(inputNumber)
  }
  return parseFloat(inputNumber)
})

Vue.filter('prettyJSON', value => JSON.stringify(JSON.parse(value)))
store.dispatch('getSecurityPolicy')
store.dispatch('getBranding', () => {
  Vue.use(Vuetify, {
    theme: {
      primary: store.state.branding.vuePrimaryColor,
      secondary: store.state.branding.vueSecondaryColor,
      accent: '#757575',
      accentColor: store.state.branding.vueAccentColor,
      error: store.state.branding.vueErrorColor,
      info: store.state.branding.vueInfoColor,
      warning: store.state.branding.vueWarningColor,
      success: store.state.branding.vueSuccessColor
    }
  })

  Vue.config.productionTip = false

  new Vue({
    router,
    store,
    render: h => h(App)
  }).$mount('#app')
})

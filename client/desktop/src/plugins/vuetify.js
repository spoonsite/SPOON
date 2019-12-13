import Vue from 'vue'
import Vuetify from 'vuetify/lib'

Vue.use(Vuetify)

export default new Vuetify({
  theme: {
    themes: {
      light: {
        primary: '#252931',
        secondary: '#183a4c',
        accent: '#757575',
        error: '#c62828',
        info: '#3f51b5',
        warning: '#ffa000',
        success: '#388e3c'
      }
    }
  }
})

import Vue from 'vue'
import Router from 'vue-router'
import 'vuetify/dist/vuetify.min.css'
import Vuetify from 'vuetify'
import LoginPage from '@/components/LoginPage'
import ForgotUserPage from '@/components/ForgotUserPage'
import ForgotPasswordPage from '@/components/ForgotPasswordPage'
import RegistrationPage from '@/components/RegistrationPage'

Vue.use(Router)
Vue.use(Vuetify, {
  theme: {
    primary: '#2a2d35',
    secondary: '#333842',
    accent: '#565656',
    error: '#C62828',
    info: '#3F51B5',
    warning: '#FFA000',
    success: '#388E3C'
  }
})

export default new Router({
  routes: [
    {
      path: '/',
      name: 'loginPage',
      component: LoginPage
    },
    {
      path: '/forgot-username',
      name: 'forgotUsername',
      component: ForgotUserPage
    },
    {
      path: '/forgot-password',
      name: 'forgotPassword',
      component: ForgotPasswordPage
    },
    {
      path: '/registration',
      name: 'registration',
      component: RegistrationPage
    }
  ]

})

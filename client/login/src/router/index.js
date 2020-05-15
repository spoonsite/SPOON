import Vue from 'vue'
import VueRouter from 'vue-router'
// import Home from '../views/Home.vue'

import LoginPage from '@/views/Login'
import ForgotUserPage from '@/views/ForgotUser'
import ForgotPasswordPage from '@/views/ForgotPassword'
import RegistrationPage from '@/views/Registration'
import ContactUsPage from '@/views/ContactUs'
import FAQPage from '@/views/FAQ'

Vue.use(VueRouter)

const routes = [
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
  },
  {
    path: '/contact-us',
    name: 'contactUs',
    component: ContactUsPage
  },
  {
    path: '/faq',
    name: 'faq',
    component: FAQPage
  },
  {
    path: '*',
    redirect: '/' // Page not found
  }
]

const router = new VueRouter({
  routes
})

export default router

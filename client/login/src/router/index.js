import Vue from 'vue';
import Router from 'vue-router';
import LoginPage from '@/components/LoginPage';
import ForgotUserPage from '@/components/ForgotUserPage';
import ForgotPasswordPage from '@/components/ForgotPasswordPage';
import RegistrationPage from '@/components/RegistrationPage';
import FAQPage from '@/components/FAQPage';

Vue.use(Router);

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
    },
    {
      path: '/faq',
      name: 'faq',
      component: FAQPage
    }
  ]

});

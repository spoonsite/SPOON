import Vue from 'vue'
import Router from 'vue-router'
import Landing from './views/Landing.vue'
import Profile from './views/Profile.vue'
import Search from './views/Search.vue'
import FAQ from './views/FAQ.vue'
import ContactUs from './views/ContactUs.vue'
import EntryDetail from './views/EntryDetail.vue'
import Watches from './views/Watches.vue'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      alias: '/home',
      name: 'Home',
      component: Landing
    },
    {
      path: '/search',
      name: 'Search',
      component: Search
    },
    {
      path: '/faq',
      name: 'FAQ',
      component: FAQ
    },
    {
      path: '/contact',
      name: 'Contact',
      component: ContactUs
    },
    {
      path: '/profile',
      name: 'Profile',
      component: Profile
    },
    {
      path: '/entry-detail/:id',
      name: 'Entry Detail',
      component: EntryDetail
    },
    {
      path: '/watches',
      name: 'Watches',
      component: Watches
    }
    // {
    //   path: '/about',
    //   name: 'about',
    //   // route level code-splitting
    //   // this generates a separate chunk (about.[hash].js) for this route
    //   // which is lazy-loaded when the route is visited.
    //   component: () => import(/* webpackChunkName: "about" */ './views/About.vue')
    // }
  ]
})

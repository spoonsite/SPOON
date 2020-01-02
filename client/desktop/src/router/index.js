import Vue from 'vue'
import VueRouter from 'vue-router'
import Landing from '@/views/Landing'
import Search from '@/views/Search'
import FAQ from '@/views/FAQ'
import ContactUs from '@/views/ContactUs'
import Profile from '@/views/Profile'
import EntryDetail from '@/views/EntryDetail'
import Watches from '@/views/Watches'

Vue.use(VueRouter)

const routes = [
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
  //   component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  // }
]

const router = new VueRouter({
  routes
})

export default router

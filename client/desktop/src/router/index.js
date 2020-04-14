import Vue from 'vue'
import VueRouter from 'vue-router'
import Landing from '@/views/Landing'
import Search from '@/views/Search'
import FAQ from '@/views/FAQ'
import ContactUs from '@/views/ContactUs'
import Profile from '@/views/Profile'
import EntryDetail from '@/views/EntryDetail'
import Watches from '@/views/Watches'
import SubmissionForm from '@/views/SubmissionForm'
import Submissions from '@/views/Submissions'
import Questions from '@/views/Questions'
import Reviews from '@/views/Reviews'
import AdminBase from '@/views/admin/AdminBase'
import Dashboard from '@/views/admin/Dashboard'

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
  },
  {
    path: '/submission-form/:id',
    alias: '/submissionform/:id',
    name: 'SubmissionForm',
    component: SubmissionForm
  },
  {
    path: '/submissions',
    name: 'Submissions',
    component: Submissions
  },
  {
    path: '/questions',
    name: 'Questions',
    component: Questions
  },
  {
    path: '/reviews',
    name: 'Reviews',
    component: Reviews
  },
  {
    path: '/admin',
    component: AdminBase,
    children: [{ path: '', name: 'AdminDashboard', component: Dashboard }]
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

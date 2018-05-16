import Vue from 'vue'
import Router from 'vue-router'
import LandingPage from '@/components/LandingPage'
import SearchPage from '@/components/SearchPage'
import EntryDetailPage from '@/components/EntryDetailPage'
import UserProfilePage from '@/components/UserProfilePage'
import FAQPage from '@/components/FAQPage'
import WatchesPage from '@/components/WatchesPage'
import ResetPasswordPage from '@/components/ResetPasswordPage'
import SubmissionStatusPage from '@/components/SubmissionStatusPage'
import EntryCommentsPage from '@/components/EntryCommentsPage'
import SMEApprovalPage from '@/components/SMEApprovalPage'
import ContactPage from '@/components/ContactPage'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'LandingPage',
      component: LandingPage
    },
    {
      path: '/faq',
      name: 'FAQ',
      component: FAQPage
    },
    {
      path: '/search',
      name: 'SearchPage',
      component: SearchPage
    },
    {
      path: '/entry-detail',
      name: 'EntryDetailPage',
      component: EntryDetailPage
    },
    {
      path: '/profile',
      name: 'UserProfilePage',
      component: UserProfilePage
    },
    {
      path: '/watches',
      name: 'WatchesPage',
      component: WatchesPage
    },
    {
      path: '/reset-password',
      name: 'ResetPasswordPage',
      component: ResetPasswordPage
    },
    {
      path: '/submission-status',
      name: 'SubmissionStatusPage',
      component: SubmissionStatusPage
    },
    {
      path: '/entry-comments',
      name: 'EntryCommentsPage',
      component: EntryCommentsPage
    },
    {
      path: '/sme-approval',
      name: 'SMEApprovalPage',
      component: SMEApprovalPage
    },
    {
      path: '/contact',
      name: 'ContactPage',
      component: ContactPage
    }
  ]
})

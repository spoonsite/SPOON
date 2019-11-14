<template>
  <div id="app">
    <v-app>
      <header>
        <div :style="topbarStyle">
        <v-toolbar color="primary" dense dark flat>
          <v-btn icon to="/" active-class=""><v-icon>fas fa-home</v-icon></v-btn>
          <v-spacer></v-spacer>
          <v-toolbar-title class="white--text">{{ $route.name }}</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-toolbar-items>
            <Notifications/>
            <v-tooltip bottom>
              <v-btn
                slot="activator"
                icon
                @click="nav('profile')"
              >
                <v-icon>fas fa-user</v-icon>
              </v-btn>
              <span>User Profile</span>
            </v-tooltip>
            <!-- <v-btn icon @click="alert = !alert"><v-icon>fas fa-times</v-icon></v-btn> -->
          </v-toolbar-items>
          <v-menu offset-y>
            <v-toolbar-side-icon slot="activator"></v-toolbar-side-icon>
            <v-list>
              <!-- Add permissions check. -->
              <v-list-tile
                v-for="link in filteredLinks"
                :key="link.name"
                class="menu-item"
                :to="link.link ? link.link : undefined"
                :href="link.href ? link.href : undefined"
                active-class="menu-item-active"
              >
                <v-list-tile-action>
                  <v-icon>fa fa-{{ link.icon }}</v-icon>
                </v-list-tile-action>
                <v-content>
                  <v-list-tile-title>{{ link.name }}</v-list-tile-title>
                </v-content>
              </v-list-tile>
              <v-divider></v-divider>
              <v-list-tile class="menu-item" @click="logout()">
                <v-list-tile-action>
                  <v-icon>fas fa-sign-out-alt</v-icon>
                </v-list-tile-action>
                <v-content>
                  <v-list-tile-title>Logout</v-list-tile-title>
                </v-content>
              </v-list-tile>
            </v-list>
          </v-menu>
        </v-toolbar>
        </div>
        <v-alert
          :value="alert"
          color="warning"
          style="margin: 0; height: 30px; text-align: center;"
        >Security Banner</v-alert>
      </header>

      <!-- Request Error Dialog -->
      <v-dialog
        v-model="errorDialog"
        >
        <v-card>
          <v-card-title>
            <h2>Error</h2>
          </v-card-title>
          <v-card-text>
            <p>Oops! Something went wrong. Please contact the admin.</p>
            <v-btn depressed small v-if="currentError" @click="showErrorDetails = !showErrorDetails">Details</v-btn>
            <div v-if="showErrorDetails && currentError">
              <strong>STATUS CODE:</strong> {{ currentError.status }} {{ currentError.statusText }}
              <br>
              <strong>METHOD:</strong>{{ currentError.config.method }}
              <br>
              <strong>URL:</strong>{{ currentError.config.url}}
              <span v-if="parseJSON(currentError.config.data)">
              <br>
              <strong>REQUEST DATA:</strong><pre style="overflow-x: scroll;">{{ parseJSON(currentError.config.data) }}</pre>
              </span>
            </div>
          </v-card-text>
          <v-card-actions>
            <v-btn @click="submitErrorReport" color="success">Send Error Report</v-btn>
            <v-btn @click.stop="errorDialog = false">Close</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

      <!-- First Time User Dialog -->
      <!-- TODO: welcome user to storefront for the first time -->
      <!-- TODO: show a tutorial of basic features -->
      <!-- <v-dialog
        v-model="firstTimeDialog"
        max-width="300px"
        >
        <v-card>
          <v-card-title>
            <h2 v-if="this.$store.state.branding">Welcome to {{ this.$store.state.branding.applicationName }} mobile</h2>
          </v-card-title>
          <v-card-text>
          </v-card-text>
          <v-card-actions>
            <v-btn @click="firstTimeDialog = false">Close</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog> -->

      <main class="offset-banner" :class="{ offset: !alert }">
        <router-view/>
      </main>

    </v-app>
  </div>
</template>

<script>
import router from './router.js'
import safeParse from 'safe-json-parse/callback'
import permissions from './util/permissions.js'
import Notifications from './components/Notifications'

export default {
  name: 'App',
  components: {
    Notifications
  },
  mounted () {
    this.$http.interceptors.response.use(response => {
      if (typeof response.data === 'string' &&
          response.data.includes('<!-- ***USER-NOT-LOGIN*** -->') &&
          !this.loggingOut) {
        window.location.href = 'openstorefront'
      }
      return response
    },
    error => {
      this.errors.push(error.response)
      this.currentError = error.response
      this.errorDialog = true
      return Promise.reject(error)
    })

    this.checkFirstTime()
    // pass in current axios instance
    this.$store.dispatch('getCurrentUser', this.checkWatches)
    this.$store.dispatch('getAppVersion')
    this.$store.dispatch('getComponentTypeList')
    this.$store.dispatch('getAttributeMap')
    this.checkWatches()
  },
  computed: {
    filteredLinks () {
      return this.links.filter(link => this.checkPermissions(link.permissions))
    }
  },
  data () {
    return {
      errors: [],
      currentError: {},
      errorDialog: false,
      showErrorDetails: false,
      messagesDialog: false,
      firstTimeDialog: false,
      loggingOut: false,
      drawer: false,
      watchNumber: 0,
      links: [ // Leave a permission array empty if no permissions are needed.
        { link: '/',
          icon: 'home',
          name: 'Home',
          permissions: [] },
        { href: '/openstorefront/AdminTool.action',
          icon: 'cog',
          name: 'Admin Tools',
          permissions: permissions.ADMIN },
        { href: '/openstorefront/UserTool.action',
          icon: 'user',
          name: 'User Tools',
          permissions: [] },
        { link: '/watches',
          icon: 'binoculars',
          name: 'Watches',
          permissions: [] },
        { link: '/faq',
          icon: 'question',
          name: 'F.A.Q.',
          permissions: [] },
        { link: '/contact',
          icon: 'comment',
          name: 'Contact',
          permissions: [] },
        { href: (this.$store.state.helpUrl ? this.$store.state.helpUrl : 'https://spoonsite.github.io/'),
          icon: 'question-circle',
          name: 'Help',
          newTab: true,
          permissions: [] }
        // { link: '/sme-approval',
        //   icon: 'check',
        //   name: 'SME Approval',
        //   permissions: [ 'WORKPLAN-PROGRESS-MANAGEMENT-PAGE' ] },
        // { link: '/submission-status',
        //   icon: 'sticky-note',
        //   name: 'Submission Status',
        //   permissions: [] },
        // { link: '/profile',
        //   icon: 'user-edit',
        //   name: 'Manage Profile',
        //   permissions: [] },
        // { link: '/reset-password',
        //   icon: 'key',
        //   name: 'Reset Password',
        //   permissions: [] }
      ],
      topbarStyle: {
        'border-bottom': `4px solid ${this.$store.state.branding.vueAccentColor}`
      },
      alert: false
    }
  },
  methods: {
    nav (url, newTab = false) {
      if (!newTab) {
        router.push(url)
      } else {
        window.open(url)
      }
    },
    logout () {
      this.loggingOut = true
      this.$http.get('/openstorefront/Login.action?Logout')
        .then(response => {
          window.location.href = 'openstorefront'
        })
        .catch(e => this.errors.push(e))
    },
    parseJSON (obj) {
      safeParse(obj, function (err, json) {
        if (err) {
          return undefined
        } else {
          return json
        }
      })
    },
    submitErrorReport () {
      this.errorDialog = false
      if (this.currentError) {
        router.push({
          name: 'Contact',
          params: {
            ticket: this.currentError.statusText
          }
        })
      } else {
        router.push({ name: 'Contact' })
      }
    },
    checkFirstTime () {
      if (this.$cookies.get('visited') !== 'yes') {
        this.firstTimeDialog = true
        this.$cookies.set('visited', 'yes')
      }
    },
    checkPermissions (has) {
      let ret = false
      if (has.length === 0) {
        ret = true
      } else {
        has.forEach(perm => {
          if (this.$store.getters.hasPermission(perm)) {
            ret = true
          }
        })
      }
      return ret
    },
    checkWatches () {
      if (this.$store.state.currentUser) {
        this.$http.get('/openstorefront/api/v1/resource/userprofiles/' + this.$store.state.currentUser.username + '/watches')
          .then(response => {
            if (response.data && response.data.length > 0) {
              for (var i = 0; i < response.data.length; i++) {
                if (response.data[i].lastViewDts < response.data[i].lastUpdateDts) {
                  this.watchNumber++
                }
              }
            }
          })
          .catch(e => this.errors.push(e))
          .finally(() => {
            if (this.watchNumber > 0) {
              this.$toasted.show(this.watchNumber + (this.watchNumber === 1 ? ' entry has' : ' entries have') + ' been updated.', {
                icon: 'binoculars',
                action: {
                  text: 'View',
                  onClick: (e, toastObject) => {
                    this.$router.push('Watches')
                    toastObject.goAway(0)
                  }
                }
              })
            }
          })
      }
    }
  }
}
</script>

<style lang="scss">
$toolbar-height: 48px;
$goldbar-height: 4px;
$banner-height: 30px;

$offset: $toolbar-height + $goldbar-height;
$offset-banner: $offset + $banner-height;

html {
  overflow: auto !important;
}
#app {
  font-family: "Roboto";
  color: #333;
}
.menu-item:hover {
  background-color: rgba(0,0,0,0.1);
  cursor: pointer;
}
.menu-item-active {
  background-color: rgba(0,0,0,0.1);
}
.offset-banner {
  margin-top: $offset-banner;
}
.offset {
  margin-top: $offset;
}
header {
  position: fixed;
  top: 0;
  width: 100%;
  z-index: 5;
}
.nav-drawer {
  background-color: white;
  z-index: 999;
}
</style>

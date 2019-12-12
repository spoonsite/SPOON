<template>
  <div id="app">
    <v-app>
      <header>
        <div :style="topbarStyle">
        <v-toolbar color="primary" dense dark flat>
          <router-link style="height: 100%;" to="/"><img height="100%" src="./assets/SPOONlogohorz.png" alt="SPOON logo"></router-link>
          <v-spacer></v-spacer>
          <v-toolbar-title class="white--text">{{ $route.name }}</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-toolbar-items>
            <Notifications/>
            <v-tooltip bottom>
              <v-btn
                slot="activator"
                icon
                to="/profile"
              >
                <v-icon>fas fa-user</v-icon>
              </v-btn>
              <span>User Profile</span>
            </v-tooltip>
            <!-- <v-btn icon @click="alert = !alert"><v-icon>fas fa-times</v-icon></v-btn> -->
          </v-toolbar-items>
          <v-menu offset-y
            :close-on-content-click="closeMenu"

          >
            <v-toolbar-side-icon slot="activator"></v-toolbar-side-icon>
            <v-list>
              <!-- Add permissions check. -->
              <v-list-tile
                v-for="link in filteredBeginningLinks"
                :key="link.name"
                class="menu-item"
                :to="link.link ? link.link : undefined"
                :href="link.href ? link.href : undefined"
                active-class="menu-item-active"
                @mouseover="closeMenu = true"
              >
                <v-list-tile-action>
                  <v-icon>fa fa-{{ link.icon }}</v-icon>
                </v-list-tile-action>
                <v-content>
                  <v-list-tile-title>{{ link.name }}</v-list-tile-title>
                </v-content>
              </v-list-tile>

              <v-list-group
                v-for="link in filteredGroupLinks"
                :key="link.name"
                :prepend-icon="'fa fa-' + link.icon"
              >
                <v-list-tile
                  slot="activator"
                  @mouseover="closeMenu = false"
                >
                  <v-list-tile-content>
                    <v-list-tile-title>{{ link.name }}</v-list-tile-title>
                  </v-list-tile-content>
                </v-list-tile>
                <v-list-tile
                  v-for="children in link.children"
                  :key="children.name"
                  style="background-color: #EEEEEE;"
                  class="menu-item"
                  :to="children.link ? children.link : undefined"
                  :href="children.href ? children.href : undefined"
                  active-class="menu-item-active"
                  @mouseover="closeMenu = true"
                >
                  <v-list-tile-action>
                    <v-icon>fa fa-{{ children.icon }}</v-icon>
                  </v-list-tile-action>
                  <v-list-tile-content>
                    <v-list-tile-title>{{ children.name }}</v-list-tile-title>
                  </v-list-tile-content>
                </v-list-tile>
              </v-list-group>

              <v-list-tile
                v-for="link in filteredEndLinks"
                :key="link.name"
                class="menu-item"
                :to="link.link ? link.link : undefined"
                :href="link.href ? link.href : undefined"
                active-class="menu-item-active"
                @mouseover="closeMenu = true"
              >
                <v-list-tile-action>
                  <v-icon>fa fa-{{ link.icon }}</v-icon>
                </v-list-tile-action>
                <v-content>
                  <v-list-tile-title>{{ link.name }}</v-list-tile-title>
                </v-content>
              </v-list-tile>

              <v-list-tile
                class="menu-item"
                @click.stop="showDisclaimer = true"
                role="button"
                aria-pressed="false"
              >
                <v-list-tile-action>
                  <v-icon>fas fa-exclamation-triangle</v-icon>
                </v-list-tile-action>
                <v-content>
                  <v-list-tile-title>Disclaimer</v-list-tile-title>
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
        max-width="75em"
      >
        <v-card>
          <ModalTitle title='Error' @close='errorDialog = false'/>
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
            <v-spacer/>
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
      <DisclaimerModal v-model="showDisclaimer" @close="showDisclaimer=false"></DisclaimerModal>

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
import DisclaimerModal from './components/DisclaimerModal'
import ModalTitle from '@/components/ModalTitle'

export default {
  name: 'App',
  components: {
    Notifications,
    DisclaimerModal,
    ModalTitle
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
  },
  computed: {
    filteredBeginningLinks () {
      return this.beginningLinks.filter(link => this.checkPermissions(link.permissions))
    },
    filteredGroupLinks () {
      return this.groupLinks.filter(link => this.checkPermissions(link.permissions))
    },
    filteredEndLinks () {
      return this.endLinks.filter(link => this.checkPermissions(link.permissions))
    }
  },
  data () {
    return {
      errors: [],
      currentError: {},
      errorDialog: false,
      showErrorDetails: false,
      showDisclaimer: false,
      messagesDialog: false,
      firstTimeDialog: false,
      loggingOut: false,
      drawer: false,
      closeMenu: true,
      watchNumber: 0,
      beginningLinks: [ // Leave a permission array empty if no permissions are needed.
        { link: '/',
          icon: 'home',
          name: 'Home',
          hasChildren: false,
          permissions: [] },
        { href: '/openstorefront/AdminTool.action',
          icon: 'cog',
          name: 'Admin Tools',
          hasChildren: false,
          permissions: permissions.ADMIN }
      ],
      groupLinks: [ // Leave a permission array empty if no permissions are needed.
        { href: '',
          icon: 'user',
          name: 'User Tools',
          hasChildren: true,
          children: [
            { link: '/watches',
            icon: 'binoculars',
            name: 'Watches',
            hasChildren: false,
            permissions: [] },
            { href: '/openstorefront/UserTool.action',
            icon: 'list',
            name: 'Submissions',
            hasChildren: false,
            permissions: [] },
            { link: '/change-password',
            icon: 'key',
            name: 'Change Password',
            hasChildren: false,
            permissions: [] },
            { href: '/openstorefront/UserTool.action',
            icon: 'question-circle',
            name: 'Questions',
            hasChildren: false,
            permissions: [] },
            { href: '/openstorefront/UserTool.action',
            icon: 'star-half-alt',
            name: 'Reviews',
            hasChildren: false,
            permissions: [] },
          ],
          permissions: [] }
      ],
      endLinks: [ // Leave a permission array empty if no permissions are needed.
        { link: '/faq',
          icon: 'question',
          name: 'F.A.Q.',
          hasChildren: false,
          permissions: [] },
        { link: '/contact',
          icon: 'comment',
          name: 'Contact',
          hasChildren: false,
          permissions: [] },
        { href: (this.$store.state.helpUrl ? this.$store.state.helpUrl : 'https://spoonsite.github.io/'),
          icon: 'question-circle',
          name: 'Help',
          newTab: true,
          hasChildren: false,
          permissions: [] }
      ],
      topbarStyle: {
        'border-bottom': `4px solid ${this.$store.state.branding.vueAccentColor}`
      },
      alert: false
    }
  },
  methods: {
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
.menu-item:hover, .v-list__group__header:hover {
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
.v-list__group--active::after, .v-list__group--active::before {
    background: white !important;
}
</style>

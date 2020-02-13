<template>
  <div id="app">
    <v-app>
      <v-app-bar app color="primary" dark dense elevate-on-scroll :height="appBarHeight">
        <router-link style="height: 100%;" to="/">
          <img height="100%" src="./assets/SPOONlogohorz.png" alt="SPOON logo" />
        </router-link>
        <v-spacer></v-spacer>
        <Notifications />
        <v-menu offset-y :close-on-content-click="false" v-model="menu" max-width="500px">
          <template v-slot:activator="{ on }">
            <v-btn icon v-on="on" @click="menu = true">
              <v-icon>fa fa-bars</v-icon>
            </v-btn>
          </template>
          <v-card>
            <v-list>
              <v-list-item
                v-for="link in filteredBeginningLinks"
                :key="link.name"
                link
                @click="menu = false"
                :to="link.link ? link.link : undefined"
                :href="link.href ? link.href : undefined"
              >
                <v-list-item-action>
                  <v-icon>fa fa-{{ link.icon }}</v-icon>
                </v-list-item-action>
                <v-list-item-title v-text="link.name" />
              </v-list-item>
              <v-list-group
                prepend-icon="fa fa-user-circle"
                :append-icon="userToolsExpand ? 'fa fa-chevron-up' : 'fa fa-chevron-down'"
                :value="userToolsExpand"
              >
                <template v-slot:activator>
                  <v-list-item-title style="margin-right: 2em;">User Tools</v-list-item-title>
                </template>
                <v-list-item
                  v-for="link in userToolLinks"
                  :key="link.name"
                  link
                  @click="menu = false"
                  :to="link.link ? link.link : undefined"
                  :href="link.href ? link.href : undefined"
                  style="margin-left: 2em"
                >
                  <v-list-item-action>
                    <v-icon>fa fa-{{ link.icon }}</v-icon>
                  </v-list-item-action>
                  <v-list-item-title v-text="link.name" />
                </v-list-item>
              </v-list-group>
              <v-list-item
                v-for="link in filteredEndLinks"
                :key="link.name"
                link
                @click="menu = false"
                :to="link.link ? link.link : undefined"
                :href="link.href ? link.href : undefined"
              >
                <v-list-item-action>
                  <v-icon>fa fa-{{ link.icon }}</v-icon>
                </v-list-item-action>
                <v-list-item-title v-text="link.name" />
              </v-list-item>
              <v-list-item class="menu-item" @click.stop="showDisclaimer = true" role="button" aria-pressed="false">
                <v-list-item-action>
                  <v-icon>fas fa-exclamation-triangle</v-icon>
                </v-list-item-action>
                <v-list-item-title>Disclaimer</v-list-item-title>
              </v-list-item>
              <v-divider></v-divider>
              <v-list-item class="menu-item" @click="logout()">
                <v-list-item-action>
                  <v-icon>fas fa-sign-out-alt</v-icon>
                </v-list-item-action>
                <v-list-item-title>Logout</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-card>
        </v-menu>
      </v-app-bar>
      <div :hidden="hideSecurityBanner" class="securityDiv" :style="securityBannerColors">
        {{ $store.state.branding.securityBannerText }}
      </div>

      <!-- Request Error Dialog -->
      <v-dialog v-model="errorDialog" max-width="75em">
        <v-card>
          <ModalTitle title="Error" @close="errorDialog = false" />
          <v-card-text>
            <p>Oops! Something went wrong. Please contact the admin.</p>
            <v-btn depressed small v-if="currentError" @click="showErrorDetails = !showErrorDetails">Details</v-btn>
            <div v-if="showErrorDetails && currentError">
              <strong>STATUS CODE:</strong>
              {{ currentError.status }} {{ currentError.statusText }}
              <br />
              <strong>METHOD:</strong>
              {{ currentError.config.method }}
              <br />
              <strong>URL:</strong>
              {{ currentError.config.url }}
              <span v-if="parseJSON(currentError.config.data)">
                <br />
                <strong>REQUEST DATA:</strong>
                <pre style="overflow-x: scroll;">{{ parseJSON(currentError.config.data) }}</pre>
              </span>
            </div>
          </v-card-text>
          <v-card-actions>
            <v-spacer />
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
      </v-dialog>-->
      <DisclaimerModal v-model="showDisclaimer" @close="showDisclaimer = false"></DisclaimerModal>

      <main :style="hideSecurityBanner ? 'padding-top: 48px' : ''">
        <router-view />
      </main>
    </v-app>
  </div>
</template>

<script>
import router from '@/router'
import safeParse from 'safe-json-parse/callback'
import permissions from '@/util/permissions'
import Notifications from '@/components/Notifications'
import DisclaimerModal from '@/components/DisclaimerModal'
import ModalTitle from '@/components/ModalTitle'

export default {
  name: 'App',
  components: {
    Notifications,
    DisclaimerModal,
    ModalTitle
  },
  mounted() {
    this.$http.interceptors.response.use(
      response => {
        if (
          typeof response.data === 'string' &&
          response.data.includes('<!-- ***USER-NOT-LOGIN*** -->') &&
          !this.loggingOut
        ) {
          sessionStorage.setItem('gotoUrl', window.location.href)
          window.location.href = 'openstorefront'
        }
        return response
      },
      error => {
        console.error(error.response)
        this.currentError = error.response
        this.errorDialog = true
        return Promise.reject(error)
      }
    )

    this.checkFirstTime()
    // pass in current axios instance
    this.$store.dispatch('getCurrentUser', this.checkWatches)
    this.$store.dispatch('getAppVersion')
    this.$store.dispatch('getComponentTypeList')
    this.$store.dispatch('getAttributeMap')
  },
  computed: {
    filteredBeginningLinks() {
      return this.beginningLinks.filter(link => this.checkPermissions(link.permissions))
    },
    filteredGroupLinks() {
      return this.groupLinks.filter(link => this.checkPermissions(link.permissions))
    },
    filteredEndLinks() {
      return this.endLinks.filter(link => this.checkPermissions(link.permissions))
    },
    securityBannerColors() {
      return `background-color: ${this.$store.state.branding.securityBannerBackgroundColor};
        color: ${this.$store.state.branding.securityBannerTextColor};
      ${this.hideSecurityBanner ? '' : 'margin-top: 48px;'}`
    },
    accentBarColor() {
      return `background-color: ${this.$store.state.branding.vueAccentColor};`
    },
    hideSecurityBanner() {
      if (this.$store.state.branding['securityBannerText']) return false
      else return true
    }
  },
  data() {
    return {
      appBarHeight: '48px',
      menu: false,
      currentError: {},
      errorDialog: false,
      showErrorDetails: false,
      showDisclaimer: false,
      messagesDialog: false,
      firstTimeDialog: false,
      loggingOut: false,
      drawer: false,
      watchNumber: 0,
      beginningLinks: [
        // Leave a permission array empty if no permissions are needed.
        {
          link: '/',
          icon: 'home',
          name: 'Home',
          hasChildren: false,
          permissions: []
        },
        {
          link: '/profile',
          icon: 'user',
          name: 'Profile',
          hasChildren: false,
          permissions: []
        },
        {
          href: '/openstorefront/AdminTool.action',
          icon: 'cog',
          name: 'Admin Tools',
          hasChildren: false,
          permissions: permissions.ADMIN
        }
      ],
      userToolsExpand: false,
      userToolLinks: [
        {
          link: '/watches',
          icon: 'binoculars',
          name: 'Watches',
          hasChildren: false,
          permissions: []
        },
        {
          link: '/submissions',
          icon: 'list',
          name: 'Submissions',
          hasChildren: false,
          permissions: []
        },
        {
          link: '/questions',
          icon: 'comments',
          name: 'Q&A',
          hasChildren: false,
          permissions: []
        },
        {
          link: '/reviews',
          icon: 'star-half-alt',
          name: 'Reviews',
          hasChildren: false,
          permissions: []
        }
      ],
      endLinks: [
        // Leave a permission array empty if no permissions are needed.
        {
          link: '/faq',
          icon: 'question',
          name: 'F.A.Q.',
          hasChildren: false,
          permissions: []
        },
        {
          link: '/contact',
          icon: 'envelope',
          name: 'Contact',
          hasChildren: false,
          permissions: []
        },
        {
          href: this.$store.state.helpUrl ? this.$store.state.helpUrl : 'https://spoonsite.github.io/',
          icon: 'question-circle',
          name: 'Help',
          newTab: true,
          hasChildren: false,
          permissions: []
        }
      ],
      topbarStyle: {
        'border-bottom': `4px solid ${this.$store.state.branding.vueAccentColor}`
      },
      alert: false
    }
  },
  methods: {
    logout() {
      this.loggingOut = true
      this.$http
        .get('/openstorefront/Login.action?Logout')
        .then(response => {
          window.location.href = 'openstorefront'
        })
        .catch(error => {
          this.$toasted.error('There was an error logging out')
          console.error(error)
        })
    },
    parseJSON(obj) {
      safeParse(obj, function(err, json) {
        if (err) {
          return undefined
        } else {
          return json
        }
      })
    },
    submitErrorReport() {
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
    checkFirstTime() {
      if (this.$cookies.get('visited') !== 'yes') {
        this.firstTimeDialog = true
        this.$cookies.set('visited', 'yes')
      }
    },
    checkPermissions(has) {
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
    checkWatches() {
      this.$http
        .get('/openstorefront/api/v1/resource/userprofiles/' + this.$store.state.currentUser.username + '/watches')
        .then(response => {
          if (response.data && response.data.length > 0) {
            for (var i = 0; i < response.data.length; i++) {
              if (response.data[i].lastViewDts < response.data[i].lastUpdateDts) {
                this.watchNumber++
              }
            }
          }
        })
        .catch(error => {
          this.$toasted.error('An error occurred retrieving watches')
          console.error(error)
        })
        .finally(() => {
          if (this.watchNumber > 0) {
            this.$toasted.show(
              this.watchNumber + (this.watchNumber === 1 ? ' entry has' : ' entries have') + ' been updated.',
              {
                icon: 'binoculars',
                action: {
                  text: 'View',
                  onClick: (e, toastObject) => {
                    this.$router.push('Watches')
                    toastObject.goAway(0)
                  }
                }
              }
            )
          }
        })
    }
  }
}
</script>

<style lang="scss">
$appBarHeight: 48px;

html {
  overflow: auto !important;
}
#app {
  font-family: 'Roboto';
  color: #333;
}
.menu-item:hover {
  background-color: rgba(0, 0, 0, 0.1);
  cursor: pointer;
}
.menu-item-active {
  background-color: rgba(0, 0, 0, 0.1);
}
.accentDiv {
  height: 3px;
  margin-top: $appBarHeight;
}
.securityDiv {
  height: 20px;
  text-align: center;
  font-size: small;
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
.v-card__title {
  word-break: normal;
}
</style>

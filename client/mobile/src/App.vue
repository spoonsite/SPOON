<template>
  <div id="app">
    <v-app>
      <header>
        <div class="toolbar">
        <v-toolbar color="primary" dense dark flat>
          <v-spacer></v-spacer>
          <v-toolbar-title class="white--text">{{ $route.name }}</v-toolbar-title>
          <v-spacer></v-spacer>
          <!-- <v-toolbar-items>
            <v-btn icon @click="nav('profile')"><v-icon>fas fa-user</v-icon></v-btn>
          </v-toolbar-items> -->
          <!-- <v-toolbar-items>
            <v-btn icon @click="alert = !alert"><v-icon>fas fa-times</v-icon></v-btn>
          </v-toolbar-items> -->
          <v-toolbar-side-icon @click="drawer = !drawer"></v-toolbar-side-icon>
        </v-toolbar>
        </div>
        <v-alert :value="alert" color="warning" style="margin: 0; height: 30px; text-align: center;">Security Banner</v-alert>
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
            <v-btn depressed small @click="showErrorDetails = !showErrorDetails">Details</v-btn>
            <div v-if="showErrorDetails && errors.length > 0">
              <strong>STATUS CODE:</strong> {{ currentError.status }} {{ currentError.statusText }}
              <br>
              <strong>METHOD:</strong>{{ currentError.config.method }}
              <br>
              <strong>URL:</strong>{{ currentError.config.url}}
              <br>
              <strong>REQUEST DATA:</strong><pre style="overflow-x: scroll;">{{ JSON.parse(currentError.config.data) }}</pre>
            </div>
          </v-card-text>
          <v-card-actions>
            <v-btn @click="submitErrorReport" color="success">Send Error Report</v-btn>
            <v-btn @click.stop="errorDialog = false">Close</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

      <!-- Login Expired Dialog -->
      <v-dialog
        v-model="loginExpiredDialog"
        max-width="300px"
        >
        <v-card>
          <v-card-title>
            <h2>Authentication Error</h2>
          </v-card-title>
          <v-card-text>
            Oops! It looks like you are not logged in.
          </v-card-text>
          <v-card-actions>
            <v-btn block href="openstorefront">Login</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

      <!-- First Time User Dialog -->
      <v-dialog
        v-model="firstTimeDialog"
        max-width="300px"
        >
        <v-card>
          <v-card-title>
            <h2 v-if="this.$store.state.branding">Welcome to {{ this.$store.state.branding.applicationName }} mobile</h2>
          </v-card-title>
          <v-card-text>
           <p>The mobile version of this application provides convenience on small screens. However, its features are limited compared to the desktop version.</p>
           <p>If you need more advanced features please <a href="/openstorefront">go to the dekstop version.</a></p>
          </v-card-text>
          <v-card-actions>
            <v-btn @click="firstTimeDialog = false">Close</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

      <v-navigation-drawer right fixed width="200" v-model="drawer" class="nav-drawer" touchless temporary>
        <v-list>
          <v-list-tile v-for="link in links" :key="link.name" class="menu-item" @click="nav(link.link)">
            <v-list-tile-action>
              <v-icon>fas fa-{{ link.icon }}</v-icon>
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
          <v-footer absolute height="auto" style="background-color: white;">
            <v-card flat tile style="margin: auto;">
              <v-card-text>
                <a href="/openstorefront">Go to desktop version</a>
              </v-card-text>
            </v-card>
          </v-footer>
        </v-list>
      </v-navigation-drawer>

      <main class="offset-banner" :class="{ offset: !alert }">
        <router-view/>
      </main>

    </v-app>
  </div>
</template>

<script>
import router from './router/index';

export default {
  name: 'App',
  mounted () {
    this.$http.interceptors.response.use(response => {
      if (typeof response.data === 'string' &&
          response.data.includes('<!-- ***USER-NOT-LOGIN*** -->') &&
          !this.loggingOut) {
        this.loginExpiredDialog = true;
      }
      return response;
    },
    error => {
      this.errors.push(error.response);
      this.currentError = error.response;
      this.errorDialog = true;
      return Promise.reject(error);
    });

    this.$store.dispatch('setCurrentUser', this.$http); // pass in current axios instance
    this.$store.dispatch('getBranding', {axios: this.$http, callback: this.checkFirstTime});
  },
  data () {
    return {
      errors: [],
      currentError: {},
      errorDialog: false,
      showErrorDetails: false,
      loginExpiredDialog: false,
      firstTimeDialog: false,
      loggingOut: false,
      drawer: false,
      links: [
        { link: '/', icon: 'home', name: 'Home' },
        { link: '/watches', icon: 'binoculars', name: 'Watch Page' },
        { link: '/sme-approval', icon: 'check', name: 'SME Approval' },
        { link: '/submission-status', icon: 'sticky-note', name: 'Submission Status' },
        { link: '/faq', icon: 'question', name: 'F.A.Q.' },
        { link: '/contact', icon: 'comment', name: 'Contact' },
        { link: '/profile', icon: 'user-edit', name: 'Manage Profile' }
      ],
      alert: false
    };
  },
  methods: {
    nav (url) {
      router.push(url);
    },
    logout () {
      this.loggingOut = true;
      this.$http.get('/openstorefront/Login.action?Logout')
        .then(response => {
          window.location.href = 'openstorefront';
        })
        .catch(e => this.errors.push(e));
    },
    submitErrorReport () {
      this.errorDialog = false;
      router.push({
        name: 'Contact',
        params: {
          ticket: this.currentError.statusText
        }
      });
    },
    checkFirstTime () {
      console.log('check first time');
      if (this.$cookies.get('visited') !== 'yes') {
        this.firstTimeDialog = true;
        this.$cookies.set('visited', 'yes');
      }
    }
  }
};
</script>

<style lang="scss">
$toolbar-height: 52px;
$goldbar-height: 4px;
$banner-height: 30px;

$offset: $toolbar-height + $goldbar-height;
$offset-banner: $offset + $banner-height;

#app {
  font-family: "Roboto";
  color: #333;
}
.menu-item:hover {
  background-color: rgba(0,0,0,0.1);
  cursor: pointer;
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
.toolbar {
  border-bottom: 4px solid gold;
}
.nav-drawer {
  background-color: white;
  z-index: 999;
}
</style>

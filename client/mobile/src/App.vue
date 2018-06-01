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
        </v-list>
      </v-navigation-drawer>

      <main class="offset-banner" :class="{ offset: !alert }">
        <router-view/>
      </main>

      <!-- <v-footer absolute height="auto" color="secondary" dark>
        <v-card flat tile color="secondary" style="margin: auto;">
          <v-card-text>
            Contact <a style="color:white;" href="mailto:support@spoonsite.com">support@spoonsite.com</a>
          </v-card-text>
        </v-card>
      </v-footer> -->
    </v-app>
  </div>
</template>

<script>
import router from './router/index'

export default {
  name: 'App',
  data () {
    return {
      drawer: false,
      links: [
        { link: '/', icon: 'home', name: 'Home' },
        { link: '/sme-approval', icon: 'check', name: 'SME Approval' },
        { link: '/faq', icon: 'question', name: 'F.A.Q.' },
        { link: '/contact', icon: 'comment', name: 'Contact' },
        { link: '/profile', icon: 'user-edit', name: 'Manage Profile' }
      ],
      alert: false
    }
  },
  methods: {
    nav (url) {
      router.push(url)
    }
  }
}
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
  z-index: 2;
}
.toolbar {
  border-bottom: 4px solid gold;
}
.nav-drawer {
  background-color: white;
  z-index: 999;
}
</style>

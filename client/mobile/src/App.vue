<template>
  <div id="app">
      <v-toolbar fixed class="indigo" dark>
        <v-spacer></v-spacer>
        <v-toolbar-title class="white--text">{{ $route.name }}</v-toolbar-title>
        <v-spacer></v-spacer>
        <!-- <v-toolbar-items>
          <v-btn icon @click="nav('profile')"><v-icon>fas fa-user</v-icon></v-btn>
        </v-toolbar-items> -->
        <v-toolbar-side-icon @click="drawer = !drawer"></v-toolbar-side-icon>
      </v-toolbar>
      <!-- Spacer -->
      <div style="height: 5em;"></div>

      <v-navigation-drawer right temporary absolute v-model="drawer" style="background-color: white; width: 15em;">
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

      <router-view/>
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
        { link: '/contact', icon: 'comment', name: 'Contact' }
      ]
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
#app {
  font-family: "Roboto"
}
.menu-item:hover {
  background-color: rgba(0,0,0,0.1);
}
</style>

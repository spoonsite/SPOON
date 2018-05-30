<template>
  <div>
    <div class="topbar">
      <v-toolbar dense flat dark color="primary">
        <v-spacer></v-spacer>
        <v-toolbar-title >{{CTitle}}</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-toolbar-side-icon @click="drawer = !drawer"></v-toolbar-side-icon>
      </v-toolbar>
    </div>
    <div class="subtitle dark secondary">
      {{ CSubtitle }}
    </div>

    <v-navigation-drawer right fixed width="200" v-model="drawer" class="nav-drawer">
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
  </div>
</template>

<script>
import router from '../../router/index'

export default {
  name: 'TitleBarComp',
  props: ['CTitle', 'CSubtitle'],
  data () {
    return {
      drawer: false,
      links: [
        { link: '/', icon: 'home', name: 'Home' },
        { link: '/faq', icon: 'question', name: 'FAQ' },
        { link: '/contact', icon: 'comment', name: 'Contact Us' }
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

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.topbar {
  border-bottom: 4px solid gold;
}
.subtitle {
  text-align: center;
  color: white;
  padding: 0.8em 0;
  font-size: 1em;
  font-style: italic;
}
</style>

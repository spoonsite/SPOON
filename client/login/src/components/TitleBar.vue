<template>
  <div>
    <v-toolbar dense flat dark color="primary">
      <v-spacer></v-spacer>
      <router-link style="height: 100%;" to="/">
        <img height="100%" src="../assets/SPOONlogohorz.png" alt="SPOON logo" />
      </router-link>
      <!-- <v-toolbar-title >{{CTitle}}</v-toolbar-title> -->
      <v-spacer></v-spacer>
      <v-btn icon @click="drawer = !drawer">
        <v-icon>fa fa-bars</v-icon>
      </v-btn>
    </v-toolbar>
    <div class="subtitle dark secondary">
      {{ CSubtitle }}
    </div>

    <v-navigation-drawer temporary right fixed width="200" v-model="drawer" class="nav-drawer">
      <v-list>
        <v-list-item-group>
          <v-list-item v-for="link in links" :key="link.name" class="menu-item" :to="link.link">
            <v-list-item-icon>
              <v-icon>fas fa-{{ link.icon }}</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>{{ link.name }}</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list-item-group>
      </v-list>
    </v-navigation-drawer>
  </div>
</template>

<script>
export default {
  name: 'TitleBar',
  props: ['CTitle', 'CSubtitle'],
  data () {
    return {
      drawer: false,
      links: [
        { link: '/', icon: 'sign-in-alt', name: 'Login' },
        { link: '/faq', icon: 'question', name: 'FAQ' },
        { link: '/contact-us', icon: 'comment', name: 'Contact Us' }
      ],
      topbarStyle: {
        'border-bottom': `4px solid ${this.$store.state.branding.vueAccentColor}`
      }
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.subtitle {
  text-align: center;
  color: white;
  padding: 0.8em 0;
  font-size: 1em;
  font-style: italic;
}
</style>

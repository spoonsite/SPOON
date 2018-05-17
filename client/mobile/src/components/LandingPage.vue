<template lang="html">

  <section>
    <div class="logo-wrapper">
      <img class="logo" src="../assets/img/SPOON_logo.png" width="300" alt="SPOON Logo">
    </div>

    <SearchBar class="mx-3 my-4"></SearchBar>

    <div style="margin: 0 1.5em;">
      <div style="max-width: 36em; margin: 0 auto;">

      <h2 style="margin-bottom: 1em;">Browse Categories</h2>

      <v-list v-for="category in categories" :key="category.title">
        <v-list-tile>
          <v-list-tile-avatar>
            <v-icon>fas fa-{{ category.icon }} fa-2x pa2</v-icon>
          </v-list-tile-avatar>
          <v-list-tile-content>

          <router-link :to="{ path: 'search', query: { q: category.title }}" style="width: 100%;">
              {{ category.title}}
          </router-link>

          </v-list-tile-content>
        </v-list-tile>
        <v-divider></v-divider>
      </v-list>
      </div>
    </div>

  </section>

</template>

<script lang="js">
import SearchBar from './subcomponents/SearchBar'

export default {
  name: 'landing-page',
  components: {
    SearchBar
  },
  props: [],
  mounted () {

  },
  data () {
    return {
      categories: [
        {
          title: 'Space',
          icon: 'rocket'
        },
        {
          title: 'Space Shuttle That is a Superty Long Title',
          icon: 'space-shuttle'
        },
        {
          title: 'Moon',
          icon: 'moon'
        },
        {
          title: 'Parts',
          icon: 'wrench'
        },
        {
          title: 'Moon',
          icon: 'moon'
        },
        {
          title: 'Parts',
          icon: 'wrench'
        },
        {
          title: 'Space Shuttle That is a Superty Long Title',
          icon: 'moon'
        }
      ]
    }
  },
  methods: {
    link (query) {
      return `/search?q=${query}`
    }
  },
  computed: {

  }
}
</script>

<style scoped lang="scss">
.shadow {
  box-shadow: 0 3px 1px -2px rgba(0,0,0,.2),0 2px 2px 0 rgba(0,0,0,.14),0 1px 5px 0 rgba(0,0,0,.12);
}
.link-tile:hover {
  background-color: rgba(0,0,0,0.2);
}
.logo-wrapper {
  max-width: 100%;
  text-align: center;
  background-color: #252931;
  margin-bottom: 2em;
}
.logo {
  padding: 2em;
  max-width: 100%;
}
</style>

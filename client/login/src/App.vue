<template>
  <div id="app">
    <v-app>
      <title-bar-comp :CTitle="title" :CSubtitle="subTitle"/>
      <router-view/>
      <div class="v-spacer"></div>
      <v-footer color="primary" dark absolute>
        <p>Some footer text</p>
      </v-footer>
    </v-app>
  </div>
</template>

<script>
import TitleBarComp from './components/SubComps/TitleBarComp'

export default {
  name: 'App',
  components: {
    TitleBarComp
  },
  computed: {
    title () {
      return this.$store.state.aTitle
    },
    subTitle () {
      return this.$store.state.aSubtitle
    }
  }
}
</script>

<style>
#app {
  font-family: "Roboto", "Franklin Gothic Medium", "Arial Narrow", Arial,
    sans-serif;
}
.v-spacer {
  height: 3em;
}
</style>

<template>
  <div id="app">
    <ul>
      <li><router-link to="/"><v-icon>fas fa-home</v-icon> Home</router-link></li>
      <li><router-link to="/sme-approval"><v-icon>fas fa-check</v-icon> SME Approval</router-link></li>
      <li><router-link to="/faq"><v-icon>fas fa-question</v-icon> F.A.Q</router-link></li>
      <li><router-link to="/profile"><v-icon>fas fa-user</v-icon> Profile</router-link></li>
      <li><router-link to="/contact"><v-icon>fas fa-comment</v-icon> Contact</router-link></li>
    </ul>
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App',
  data () {
    return {
    }
  }
}
</script>

<style lang="scss">
#app {
  font-family: "Roboto"
}
</style>

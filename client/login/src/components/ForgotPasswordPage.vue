<template>
  <div>
    <title-bar-comp :CTitle="title" :CSubtitle="subTitle" />
    <v-layout align-center justify-center>
      <forgot-password-comp/>
    </v-layout>
  </div>
</template>

<script>
import ForgotPasswordComp from './SubComps/ForgotPasswordComp'
import TitleBarComp from './SubComps/TitleBarComp'

export default {
  name: 'ForgotPasswordPage',
  components: {
    ForgotPasswordComp,
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

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.login-comp {
  width: 50%;
  height: 100%;
  margin: 2em;
}
.signup-comp {
  width: 50%;
  height: 100%;
  margin: 2em;
}
</style>

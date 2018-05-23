<template>
  <div>
    <title-bar-comp :CTitle="title" :CSubtitle="subTitle" />
    <v-layout align-center justify-center>
      <forgot-username-comp/>
    </v-layout>
  </div>
</template>

<script>
import ForgotUsernameComp from './SubComps/ForgotUsernameComp'
import TitleBarComp from './SubComps/TitleBarComp'

export default {
  name: 'ForgotUserPage',
  components: {
    ForgotUsernameComp,
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

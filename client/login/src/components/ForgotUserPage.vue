<template>
  <div>
    <v-layout align-center justify-center>
      <forgot-username-comp/>
    </v-layout>
  </div>
</template>

<script>
import ForgotUsernameComp from './SubComps/ForgotUsernameComp'

export default {
  name: 'ForgotUserPage',
  components: {
    ForgotUsernameComp
  },
  computed: {
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

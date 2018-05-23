<template>
  <div>
    <v-layout align-center justify-center>
      <forgot-password-comp/>
    </v-layout>
  </div>
</template>

<script>
import ForgotPasswordComp from './SubComps/ForgotPasswordComp'

export default {
  name: 'ForgotPasswordPage',
  components: {
    ForgotPasswordComp
  },
  computed: {
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

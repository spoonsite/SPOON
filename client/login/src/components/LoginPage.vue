<template>
  <div class="login-page pa-4">
    <video controls class="video elevation-5" src="https://www.spoonsite.com/openstorefront/Branding.action?GeneralMedia&name=SPOON%20Overview;" poster="https://www.spoonsite.com/openstorefront/Branding.action?GeneralMedia&amp;name=SPOON%20Overview%20Poster"></video>
    <!-- <signup-comp></signup-comp> -->
    <login-comp></login-comp>
  </div>
</template>

<script>
import LoginComp from './SubComps/LoginComp'
import SignupComp from './SubComps/SignupComp'

export default {
  name: 'LoginPage',
  components: {
    LoginComp,
    SignupComp
  },
  computed: {
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.login-page {
  max-width: 50em;
  margin-left: auto;
  margin-right: auto;
}
.video {
  width: 100%;
  display: block;
}
</style>

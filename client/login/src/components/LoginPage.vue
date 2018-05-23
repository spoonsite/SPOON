<template>
  <div>
    <video controls class="video" src="https://www.spoonsite.com/openstorefront/Branding.action?GeneralMedia&name=SPOON%20Overview;" poster="https://www.spoonsite.com/openstorefront/Branding.action?GeneralMedia&amp;name=SPOON%20Overview%20Poster"></video>
    <div class="wrapper">
      <signup-comp></signup-comp>
      <login-comp></login-comp>
    </div>
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
.video {
  width: 100%;
  max-width: 45em;
  display: block;
  margin-left: auto;
  margin-right: auto;
  margin-top: 1em;
}
.wrapper {
  max-width: 45em;
  margin-left: auto;
  margin-right: auto;
}
</style>

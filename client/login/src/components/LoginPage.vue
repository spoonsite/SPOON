<template>
<v-layout mt-3 mx-3>
<v-flex xs12 sm4 offset-sm4>
    <video controls class="video elevation-5" src="https://www.spoonsite.com/openstorefront/Branding.action?GeneralMedia&name=SPOON%20Overview" poster="https://www.spoonsite.com/openstorefront/Branding.action?GeneralMedia&amp;name=SPOON%20Overview%20Poster"></video>
    <login-comp></login-comp>
  </v-flex>
</v-layout>
</template>

<script>
import LoginComp from './SubComps/LoginComp';

export default {
  name: 'LoginPage',
  components: {
    LoginComp
  }
};
</script>

<style scoped>
.video {
  width: 100%;
  display: block;
}
</style>

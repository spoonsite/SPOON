<template>
<v-layout mt-3 mx-3>
<v-flex xs12 md4 offset-md4 sm6 offset-sm3>
    <video controls class="video elevation-5" v-if="$store.state.branding.loginOverviewVideoUrl" :src="$store.state.branding.loginOverviewVideoUrl" :poster="$store.state.branding.loginOverviewVideoPosterUrl"></video>
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

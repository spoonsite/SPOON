<template>
  <div>
    <v-layout align-center justify-center>
      <forgot-password-comp/>
    </v-layout>
  </div>
</template>

<script>
import ForgotPasswordComp from './SubComps/ForgotPasswordComp';

export default {
  name: 'ForgotPasswordPage',
  components: {
    ForgotPasswordComp
  },
  computed: {
  }
};
</script>

<style scoped>
</style>

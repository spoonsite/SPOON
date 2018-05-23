<template>
  <div class="wrapper">
      <h2>Registration - Fill out the form to signup for an account</h2>
      <registration-credentials-comp/>
      <registration-user-comp/>
      <registration-email-verify-comp/>
      <div class="button-wrapper">
        <div class="btn1">
          <v-btn block color="accent" @click="signup()"><v-icon light>check</v-icon> &nbsp; Signup</v-btn>
        </div>
        <div class="btn2">
        <v-btn block color="accent" @click="cancel()"><v-icon light>cancel</v-icon> &nbsp; Cancel</v-btn>
        </div>
      </div>
  </div>
</template>

<script>
import RegistrationUserComp from './SubComps/RegistrationUserComp'
import RegistrationEmailVerifyComp from './SubComps/RegistrationEmailVerifyComp'
import RegistrationCredentialsComp from './SubComps/RegistrationCredentialsComp'

export default {
  name: 'RegistrationPage',
  components: {
    RegistrationUserComp,
    RegistrationEmailVerifyComp,
    RegistrationCredentialsComp
  },
  methods: {
    signup () {
      return 0
    },
    cancel () {
      return 0
    }
  },
  computed: {
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.wrapper {
  max-width: 45em;
  padding: 1em;
  margin-right: auto;
  margin-left: auto;
}
.button-wrapper {
  width: 100%;
}
.btn1 {
  padding-right: 1em;
  width: 50%;
  float: left;
}
.btn2 {
  padding-left: 1em;
  width: 50%;
  float: left;
}
@media screen and (max-width: 599px) {
  .btn1 {
    padding-right: 0;
    width: 100%;
  }
  .btn2 {
    padding-left: 0;
    width: 100%;
  }
}
</style>

<template>
  <div>
    <title-bar-comp :CTitle="title" :CSubtitle="subTitle" />
    <v-layout align-center justify-center column>
      <v-toolbar class="topbar">
        <v-spacer></v-spacer>
        <v-toolbar-title>Registration - Fill out the form to signup for an account</v-toolbar-title>
         <v-spacer></v-spacer>
      </v-toolbar>
      <registration-credentials-comp class="rcc"/>
      <registration-user-comp class="ruc"/>
      <registration-email-verify-comp class="revc"/>
      <v-toolbar class="botbar">
        <v-btn color="accent" style="margin-left:2em;" @click=""><v-icon light>check</v-icon> &nbsp Signup</v-btn>
        <v-spacer></v-spacer>
        <v-btn color="accent" style="margin-right:2em;" @click=""><v-icon light>cancel</v-icon> &nbsp Cancel</v-btn>
      </v-toolbar>
    </v-layout>
  </div>
</template>

<script>
import RegistrationUserComp from "./SubComps/RegistrationUserComp"
import TitleBarComp from "./SubComps/TitleBarComp"
import RegistrationEmailVerifyComp from './SubComps/RegistrationEmailVerifyComp'
import RegistrationCredentialsComp from './SubComps/RegistrationCredentialsComp'

export default {
  name: "RegistrationPage",
  components: {
    RegistrationUserComp,
    RegistrationEmailVerifyComp,
    RegistrationCredentialsComp,
    TitleBarComp
  },
  computed: {
    title(){
      return this.$store.state.aTitle;
    },
    subTitle(){
      return this.$store.state.aSubtitle;
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
 .rcc{
   width:85%;
 }
 .ruc{
   width:85%;
 }
 .revc{
   width:85%;
 }
 .topbar{
   width:85%;
   margin-top:2em !important;
 }
 .botbar{
   width:85%;
   margin-top:1em !important;
 }

</style>

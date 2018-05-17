<template>
  <div>
    <title-bar-comp :CTitle="title" :CSubtitle="subTitle" />
    <v-layout align-center justify-center>
      <video-comp class="video-comp"/>
    </v-layout>
    <v-layout align-center justify-center>
      <signup-comp class="signup-comp" />
      <login-comp class="login-comp" />
    </v-layout>
  </div>
</template>

<script>
import TitleBarComp from "./SubComps/TitleBarComp";
import LoginComp from "./SubComps/LoginComp";
import SignupComp from "./SubComps/SignupComp";
import VideoComp from "./SubComps/VideoComp";

export default {
  name: "LoginPage",
  components: {
    LoginComp,
    SignupComp,
    VideoComp,
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
.video-comp{
  margin-top:2em;
   width:40%;
}
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

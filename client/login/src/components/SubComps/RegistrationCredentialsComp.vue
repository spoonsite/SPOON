<template>
  <v-layout mt-3>
    <v-flex xs12>
      <v-card class="elevation-5">
        <v-toolbar color="primary" dark dense>
          <v-toolbar-title>Login Credentials</v-toolbar-title>
        </v-toolbar>
        <v-card-text>
          <v-form v-model="valid">
            <v-layout row>
              <v-text-field prepend-icon="person" name="username" label="Username" type="text" :rules="usernameRules"></v-text-field>
            </v-layout>
            <v-layout row>
            <v-text-field prepend-icon="lock" name="password" label="Password" type="text" :rules="passwordRules"></v-text-field>
            </v-layout>
            <v-layout row>
            <v-text-field prepend-icon="lock" name="confirmpassword" label="Confirm Password" type="text" :rules="confirmPasswordRules"></v-text-field>
            </v-layout>
          </v-form>
        </v-card-text>
        <v-card-actions>
        </v-card-actions>
      </v-card>
    </v-flex>
  </v-layout>
</template>

<script>
export default {
  name: "RegistrationCredentialsComp",
  data: () => ({
    valid: false,
    email: "",
    emailRules: [
      v => !!v || "E-mail is required",
      v =>
        /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/.test(v) ||
        "E-mail must be valid"
    ]
  }),
  methods:{
    testAPI: function(event){
      console.log("Button Clicked Calling API");
      if(event){
           this.$api.getTest();
      }
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>

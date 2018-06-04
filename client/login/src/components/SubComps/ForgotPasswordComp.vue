<template>
  <v-layout ma-3>
    <v-flex xs12 sm6 offset-sm3>
      <v-card class="elevation-5">
        <v-toolbar color="primary" dark dense>
          <v-toolbar-title>Forgot Password</v-toolbar-title>
        </v-toolbar>
        <v-card-text>
          <v-form v-model="valid">
            <v-text-field prepend-icon="email" name="email" label="Enter Email" type="text" :rules="emailRules"></v-text-field>
          </v-form>
        </v-card-text>
        <v-card-action>
          <v-btn color="accent" style="margin-bottom:2em;" @click="testAPI()">Send password</v-btn>
        </v-card-action>
      </v-card>
    </v-flex>
  </v-layout>
</template>

<script>
export default {
  name: 'ForgotPasswordComp',
  data: () => ({
    valid: false,
    email: '',
    emailRules: [
      v => !!v || 'E-mail is required',
      v =>
        /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/.test(v) ||
        'E-mail must be valid'
    ]
  }),
  methods: {
    testAPI: function (event) {
      console.log('Button Clicked Calling API');
      if (event) {
        this.$api.getTest();
      }
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>

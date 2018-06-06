<template>
<div>
  <v-layout mt-3 mx-3>
    <v-flex xs12 sm4 offset-sm4>
      <v-card class="elevation-5 mt-3 mx-3">
        <v-toolbar color="primary" dark dense>
          <v-toolbar-title>Forgot Username</v-toolbar-title>
        </v-toolbar>
        <v-card-text>
          <v-form v-model="valid" @submit.prevent="submitEmail()">
            <v-text-field
              prepend-icon="email"
              name="email"
              label="Enter Email"
              type="text"
              v-model="email"
              :rules="emailRules"
            ></v-text-field>
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-btn :disabled="!valid" block color="accent" @click="submitEmail()">Send Username</v-btn>
        </v-card-actions>
      </v-card>
    </v-flex>
  </v-layout>

  <v-dialog v-model="dialog" max-width="300px">
    <v-card tile>
      <v-card-text>Check your email for you username(s).</v-card-text>
      <v-card-actions>
        <v-btn @click="$router.push('/')"><v-icon>fas fa-return</v-icon> Return to Login</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</div>
</template>

<script>
export default {
  name: 'ForgotUsernameComp',
  data: () => ({
    dialog: false,
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
    submitEmail () {
      this.$http.get(`/openstorefront/api/v1/service/security/forgotusername?emailAddress=${this.email}`)
        .then(response => {
          this.dialog = true;
        })
        .catch(error => console.log(error));
    }
  }
};
</script>

<style scoped>
</style>

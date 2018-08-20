<template>
<div>
  <v-layout ma-3>
    <v-flex xs12 md4 offset-md4 sm6 offset-sm3>
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
              :rules="[rules.required, rules.email]"
            ></v-text-field>
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-btn :loading="loading" :disabled="!valid" block color="accent" @click="submitEmail()">Send Username</v-btn>
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
import validators from '../util/validators';

export default {
  name: 'ForgotUsernameComp',
  mixins: [validators],
  data: () => ({
    dialog: false,
    valid: false,
    email: '',
    loading: false
  }),
  methods: {
    submitEmail () {
      this.loading = true;
      this.$http.get(`/openstorefront/api/v1/service/security/forgotusername?emailAddress=${this.email.toLowerCase()}`)
        .then(response => {
          this.dialog = true;
          this.loading = false;
        })
        .catch(error => console.error(error));
    }
  }
};
</script>

<style scoped>
</style>

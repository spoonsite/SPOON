<template>
<div>
  <v-layout ma-3>
    <v-flex xs12 md4 offset-md4 sm6 offset-sm3>
      <v-card class="elevation-5 mt-3 mx-3">
        <v-toolbar color="accent" dark dense>
          <v-toolbar-title>Forgot Password</v-toolbar-title>
        </v-toolbar>
        <v-card-text>
          <v-form v-model="valid" @submit.prevent="submitEmail()">
            <v-text-field
              prepend-icon="person"
              name="username"
              label="Username"
              type="text"
              v-model="username"
              :rules="usernameRules"
            ></v-text-field>
            <v-text-field
              prepend-icon="lock"
              name="password1"
              label="New Password"
              type="password"
              v-model="password1"
              :rules="password1Rules"
            ></v-text-field>
            <v-text-field
              prepend-icon="lock"
              name="password2"
              label="Verify Password"
              type="password"
              v-model="password2"
              :rules="password2Rules"
            ></v-text-field>
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-btn :loading="loading" :disabled="!valid" block color="accent" @click="submitReset()">Send Email Approval</v-btn>
        </v-card-actions>
      </v-card>
    </v-flex>
  </v-layout>

  <v-dialog v-model="dialog" max-width="300px">
    <v-card tile>
      <v-card-text>Check your email associated with your user. Follow the instructions in the email to complete the reset.</v-card-text>
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
  data: function () {
    return {
      dialog: false,
      valid: false,
      loading: false,
      username: '',
      usernameRules: [
        v => !!v || 'Username is required',
        v =>
          /^\w+$/.test(v) ||
          'Username must be valid'
      ],
      password1: '',
      password2: '',
      password1Rules: [
        v => !!v || 'Password is required',
        v => {
          let regex = new RegExp('^(?=.*[A-Z])(?=.*\\d)(?=.*[~`!@#$%^&*()-+=<>:;"\',.?])[A-Za-z\\d~`!@#$%^&*()-+=<>:;"\',.?]{' + String(this.$store.state.securitypolicy.minPasswordLength) + ',}$');
          return regex.test(v) ||
          `Password must contain 1 uppercase, 1 number, 1 special character (i.e. @$!%*#?&), and be at least ${this.$store.state.securitypolicy.minPasswordLength} characters`;
        }
      ],
      password2Rules: [
        v => !!v || 'Password verification is required',
        v => this.password1 === this.password2 || 'Passwords must match'
      ]
    };
  },
  methods: {
    submitReset () {
      this.loading = true;
      this.$http.put(`/openstorefront/api/v1/service/security/${this.username}/resetpassword`,
        {
          username: this.username,
          password: this.password1,
          confirmPassword: this.password2
        })
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

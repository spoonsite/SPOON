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
              <v-text-field
                prepend-icon="person"
                name="username"
                label="Username"
                type="text"
                :rules="usernameRules"
                v-model="value.username"
              ></v-text-field>
            </v-layout>
            <v-layout row>
              <v-text-field
                prepend-icon="lock"
                name="password"
                label="Password"
                type="password"
                :rules="password1Rules"
                v-model="value.password1"
              ></v-text-field>
            </v-layout>
            <v-layout row>
              <v-text-field
                prepend-icon="lock"
                name="confirmpassword"
                label="Confirm Password"
                type="password"
                :rules="password2Rules"
                v-model="value.password2"
              ></v-text-field>
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
  name: 'RegistrationCredentialsComp',
  props: ['value'],
  data: function () {
    return {
      valid: false,
      usernameRules: [
        v => !!v || 'Username is required',
        v =>
          /^\w+$/.test(v) ||
          'Username must be valid'
      ],
      password1Rules: [
        v => !!v || 'Password is required',
        v =>
          /^(?=.*[A-Za-z])(?=.*\d)(?=.*[~`!@#$%^&*()-+=<>:;"',.?])[A-Za-z\d~`!@#$%^&*()-+=<>:;"',.?]{8,}$/.test(v) ||
          'Password must contain 1 uppercase, 1 number, and 1 special character (i.e. @$!%*#?&)'
      ],
      password2Rules: [
        v => !!v || 'Password verification is required',
        v => this.value.password1 === this.value.password2 || 'Passwords must match'
      ]
    };
  },
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

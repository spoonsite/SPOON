<template>
  <div>
    <v-card class="elevation-5 ma-3">
      <v-toolbar color="primary" dark dense>
        <v-toolbar-title>Log in</v-toolbar-title>
      </v-toolbar>
      <v-card-text>
        <v-form v-model="valid">
          <v-text-field prepend-icon="person" name="username" label="Username" type="text" :rules="usernameRules"></v-text-field>
          <v-text-field prepend-icon="lock" name="password" label="Password" type="password" :rules="passwordRules"></v-text-field>
        </v-form>
      </v-card-text>
      <v-card-actions>
        <div class="wrapper">
            <v-btn block color="accent" style="margin-bottom:1em;" @click="testAPI">Login</v-btn>
          <div class="buttons">
            <router-link :to="{name: 'forgotPassword'}" class="button-wrapper pr2">
              <div class="btn btn--block btn--outline">
                Forgot Password
              </div>
            </router-link>
            <router-link :to="{name: 'forgotUsername'}" class="button-wrapper pl2">
              <div class="btn btn--block btn--outline">
                Forgot Username
              </div>
            </router-link>
          </div>
        </div>
      </v-card-actions>
    </v-card>
  </div>
</template>

<script>
export default {
  name: 'LoginComp',
  data: () => ({
    valid: false,
    password: '',
    passwordRules: [
      v => !!v || 'Password is required'
      // v => (v. && v.length <= 20) || "Password must be less than 20 characters",
      // v => (!v && v.length >= 8) || "Password must be at least 8 characters"
    ],
    email: '',
    emailRules: [
      v => !!v || 'E-mail is required',
      v =>
        /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/.test(v) ||
        'E-mail must be valid'
    ],
    username: '',
    usernameRules: [
      v => !!v || 'Username is required'
    ]
  }),
  methods: {
    testAPI: function (event) {
      console.log('Button Clicked Calling API')
      if (event) {
        this.$api.getTest()
      }
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
a {
  text-decoration: none;
}
.wrapper {
  width: 100%;
  margin: 0 1em;
}
.buttons {
  width: 100%;
}
.button-wrapper {
  width: 50%;
  text-align: center;
  float: left;
  margin-bottom: 1em;
}
.pr2 {
  padding-right: 1em;
}
.pl2 {
  padding-left: 1em;
}
</style>

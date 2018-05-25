<template>
  <div>
    <v-card class="elevation-5 mt-4">
      <v-card-text>
        <v-form v-model="valid" class="clearfix">
          <v-text-field prepend-icon="person" name="username" label="Username" type="text" :rules="usernameRules" tabindex=1></v-text-field>
          <router-link :to="{name: 'forgotUsername'}" class="link" tabindex=5>Forgot Username</router-link>
          <v-text-field style="margin-top: 0 !important;" prepend-icon="lock" name="password" label="Password" type="password" :rules="passwordRules" tabindex=2></v-text-field>
          <router-link :to="{name: 'forgotPassword'}" class="link" tabindex=6>Forgot Password</router-link>
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-container style="margin: 0; padding: 0 1em 1em 1em;">
          <v-flex xs12>
            <v-btn block color="accent" style="margin-bottom:1em;" @click="testAPI" tabindex=3>Login</v-btn>
          </v-flex>
          <v-flex xs12 class="register">
            <router-link :to="{name: 'registration'}" tabindex=4>Register for a new account</router-link>
          </v-flex>
        </v-container>
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
    testAPI (event) {
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
a:hover {
  text-decoration: underline;
}
.clearfix:after {
  content: '';
  clear: both;
  display: table;
}
.link {
  float: right;
  position: relative;
  top: -1.5em;
}
.register {
  text-align: center;
  width: 100%;
  font-size: 1.4em;
}
</style>

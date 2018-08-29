<template>
  <div>
    <v-card class="elevation-5 my-4">
      <v-card-text>
        <v-form v-model="valid"  ref="form" class="clearfix">
          <v-text-field
            prepend-icon="person"
            name="username"
            label="Username"
            autofocus=true
            type="text"
            :rules="usernameRules"
            v-model="username"
            :error-messages="usernameError"
            tabindex=1
          ></v-text-field>
          <router-link :to="{name: 'forgotUsername'}" class="forgot-link" tabindex=5>Forgot Username</router-link>
          <v-text-field
            style="margin-top: 0 !important;"
            prepend-icon="lock"
            name="password"
            label="Password"
            type="password"
            :rules="passwordRules"
            :error-messages="passwordError"
            v-model="password"
            v-on:keyup.enter="login"
            tabindex=2
          ></v-text-field>
          <router-link :to="{name: 'forgotPassword'}" class="forgot-link" tabindex=6>Forgot Password</router-link>
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-container style="margin: 0; padding: 0 1em 1em 1em;">
          <v-flex xs12>
            <v-btn
              block
              color="accent"
              style="margin-bottom:1em;"
              @click="login"
              :disabled="!valid"
              :loading="loading"
              tabindex=3
            >
            Login
            </v-btn>
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
    loading: false,
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
    ],
    response: {}
  }),
  computed: {
    passwordError () {
      if (this.response.data && !this.response.data.success && this.response.data.errors.password) {
        return this.response.data.errors.password;
      }
    },
    usernameError () {
      if (this.response.data && !this.response.data.success && this.response.data.errors.username) {
        return this.response.data.errors.username;
      }
    },
    gotoPage () {
      return this.$route.query.gotoPage;
    }
  },
  methods: {
    getCookie (cname) {
      let name = `${cname}=`;
      let decodedCookie = decodeURIComponent(document.cookie);
      let ca = decodedCookie.split(';');
      for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) === ' ') {
          c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
          return c.substring(name.length, c.length);
        }
      }
      return '';
    },
    login () {
      if (this.$refs.form.validate()) {
        this.loading = true;
        let data = new FormData();
        data.append('username', this.username);
        data.append('password', this.password);
        data.append('gotoPage', '');

        let token = this.getCookie('X-Csrf-Token');
        this.$http.post('/openstorefront/Login.action?Login',
          data,
          {
            headers: {
              'X-Csrf-Token': token
            }
          })
          .then(response => {
            if (response.data.success) {
              if (this.gotoPage) {
                window.location.href = this.gotoPage;
              } else {
                window.location.href = response.data.message;
              }
            } else {
              this.loading = false;
            }
            this.response = response;
          })
          .catch(error => console.error(error));
      }
    }
  }
};
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
.forgot-link {
  float: right;
  position: relative;
  top: -1.5em;
}
@media screen and (max-width: 450px) {
  .forgot-link {
    top: -0.5em;
  }
}
.register {
  text-align: center;
  width: 100%;
  font-size: 1.4em;
}
</style>

<template>
  <div>
    <v-card class="elevation-5">
      <v-toolbar id="toolbar-title" dense>
        <v-toolbar-title>Log in</v-toolbar-title>
      </v-toolbar>
      <v-card-text>
        <v-form v-model="valid">
          <v-text-field prepend-icon="person" name="username" label="Username" type="text"></v-text-field>
          <v-text-field prepend-icon="lock" name="password" label="Password" type="password" :rules="passwordRules"></v-text-field>
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-container fluid grid-list-x>
          <v-layout row>
            <v-flex xs2 ml-5>
              <v-btn id="login-button" style="margin-bottom:2em;">Login</v-btn>
            </v-flex>
          </v-layout>
          <v-layout row justify-start>
            <v-flex>
              <v-btn color="secondary" flat small>Forgot Password</v-btn>
            </v-flex>
            <v-flex>|</v-flex>
            <v-flex>
              <v-btn color="secondary" flat small>Forgot Username</v-btn>
            </v-flex>
          </v-layout>
        </v-container>
      </v-card-actions>
    </v-card>
  </div>
</template>

<script>
export default {
  name: "LoginComp",
  data: () => ({
    valid: false,
    password: "",
    passwordRules: [
      v => !!v || "Password is required"
      //v => (v. && v.length <= 20) || "Password must be less than 20 characters",
      //v => (!v && v.length >= 8) || "Password must be at least 8 characters"
    ],
    email: "",
    emailRules: [
      v => !!v || "E-mail is required",
      v =>
        /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/.test(v) ||
        "E-mail must be valid"
    ]
  })
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
hr {
  background: goldenrod;
  height: 7px;
}
#topbar {
  background-color: #252931;
  color: white;
}
#subbar {
  background-color: #303540;
  color: white;
}
#subtitle {
  font-size: 1em;
  font-style: italic;
}
#menubutton {
  color: white;
}
#toolbar-title {
  background-color: #303540 !important;
  color: white;
}
#login-button {
  background-color: #303540 !important;
  color: white;
}
</style>

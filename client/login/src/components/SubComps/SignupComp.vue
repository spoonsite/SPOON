<template>
  <div>
    <v-card class="elevation-5 ma-3">
      <v-toolbar color="primary" dark dense>
        <v-toolbar-title>Sign up for an Account</v-toolbar-title>
      </v-toolbar>
      <v-list two-line>
        <v-list-tile v-for="(value, index) in signupSteps" :key="index">
            <v-list-tile-avatar>
              <svg width="40" height="40">
                <circle cx="20" cy="20" r="20" fill="#333842" />
                <text x="50%" y="50%" text-anchor="middle" fill="white" font-size="20px" dy=".3em">{{ index + 1 }}</text>
                Sorry, your browser does not support inline SVG.
              </svg>
            </v-list-tile-avatar>
          <v-list-tile-content>
            {{ value }}
          </v-list-tile-content>
        </v-list-tile>
      </v-list>
      <v-card-actions>
        <v-container>
          <v-layout row fluid wrap>
            <v-flex class="btn1" xs12 sm6>
              <router-link :to="{name: 'registration'}" class="btn btn--block accent">
                <div class="btn__content">
                  Sign Up
                </div>
              </router-link>
              </v-flex>
              <v-flex class="btn2" xs12 sm6>
              <v-btn block color="accent">How to Register
                &nbsp;<v-icon light>play_circle_outline</v-icon>
              </v-btn>
            </v-flex>
          </v-layout>
        </v-container>
      </v-card-actions>
    </v-card>
  </div>
</template>

<script>
export default {
  name: 'SignUpComp',
  data () {
    return {
      signupSteps: [
        'Go to the Sign-Up page.',
        'FoFollow the steps on the screen to set up your account.',
        'Use the account you created to Sign In.'
      ]
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.btn1 {
  padding-right: 1em;
}
.btn2 {
  padding-left: 1em;
}
@media screen and (max-width: 599px) {
  .btn1 {
    padding-right: 0;
    margin-bottom: 1em;
  }
  .btn2 {
    padding-left: 0;
  }
}
</style>

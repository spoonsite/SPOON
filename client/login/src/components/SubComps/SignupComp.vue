<template>
  <v-layout mt-3 align-center justify-center>
    <v-flex xs12 sm8>
      <v-card class="elevation-5">
        <v-toolbar color="primary" dark dense>
          <v-toolbar-title>Sign up for an Account</v-toolbar-title>
        </v-toolbar>
        <v-card-text>
          <div>
            <ol class="circles-list">
              <li>Go to Sign-up page</li>
              <li>Fill out the form to setup your account</li>
              <li>Use the account you created to log in</li>
            </ol>
          </div>
        </v-card-text>
        <v-card-actions>
          <v-container fluid grid-list-x>
            <v-layout row>
              <v-btn color="accent" style="margin:.75em;">Sign up</v-btn>
              <v-btn color="accent" style="margin:.75em;">How to Register
                &nbsp;<v-icon light>play_circle_outline</v-icon>
              </v-btn>
            </v-layout>
          </v-container>
        </v-card-actions>
      </v-card>
    </v-flex>
  </v-layout>
</template>

<script>
export default {
  name: "SignUpComp"
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
#signup-button {
  background-color: #303540 !important;
  color: white;
}
#how-button {
  background-color: #303540 !important;
  color: white;
}
div ol {
  width: 80%;
  font-family: "Roboto", "Franklin Gothic Medium", "Arial Narrow", Arial,
    sans-serif;
}
ol.circles-list {
  list-style-type: none;
  list-style-type: decimal !ie; /*IE 7- hack*/

  margin: 0;
  margin-left: 4em;
  padding: 0;

  counter-reset: li-counter;
}
ol.circles-list > li {
  position: relative;
  margin-bottom: 20px;
  padding-left: 0.5em;
  min-height: 3em;
}
ol.circles-list > li:before {
  position: absolute;
  top: 0;
  left: -1.33em;
  width: 1.2em;
  height: 1.2em;

  font-size: 2.5em;
  line-height: 1.2;
  text-align: center;
  color: #f5f5f5;

  border: 3px solid #c5c5c5;
  border-radius: 50%;
  background-color: #464646;
  content: counter(li-counter);
  counter-increment: li-counter;
}
li:content {
  font-size: 5em;
}
</style>

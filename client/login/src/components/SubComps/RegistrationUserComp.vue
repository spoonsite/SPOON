<template>
  <v-layout mt-3>
    <v-flex xs12>
      <v-card class="elevation-5">
        <v-toolbar color="primary" dark dense>
          <v-toolbar-title>User Information</v-toolbar-title>
        </v-toolbar>
        <v-card-text>
          <v-form v-model="valid">
            <v-layout row>
              <v-text-field name="first-name" label="First Name" type="text" :rules="fNameRules"></v-text-field>
            </v-layout>
            <v-layout row>
              <v-text-field name="last-name" label="Last Name" type="text" :rules="lNameRules"></v-text-field>
            </v-layout>
            <v-layout row>
              <v-select  name="organization" label="Organization" required></v-select>
            </v-layout>
            <v-layout row>
              <v-text-field name="position" label="Position Title" type="text" :rules="positionTitleRules"></v-text-field>
            </v-layout>
            <v-layout row>
              <v-text-field name="email" label="Business Email" type="text" :rules="emailRules"></v-text-field>
            </v-layout>
            <v-layout row>
              <v-text-field name="phone" label="Business Phone" type="text" :rules="phoneRules"></v-text-field>
            </v-layout>
            <v-layout row>
              <v-select name="usertype" label="User Type"></v-select>
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
  name: 'RegistrationUserComp',
  data: () => ({
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
</style>

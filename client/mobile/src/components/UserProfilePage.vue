<template lang="html">

  <section class="user-profile-page">
    <h1 class="text-xs-center">user-profile-page Component</h1>
    <v-container>
      <div v-if="errors.length > 0">
        <ul>
          <li v-for="error in errors" :key="error">
            {{ error }}
          </li>
        </ul>
      </div>

      <v-layout column align-center>
        <v-layout row wrap align-center>
          <!-- Avatar Icon -->
          <v-card raised>
            <v-layout align-center justify-space-around mt-2>
              <v-icon size="8em">fas fa-user-tie</v-icon>
            </v-layout>
            <v-card-actions>
              <v-btn flat color="orange">Change Avatar</v-btn>
            </v-card-actions>
          </v-card>
          <v-spacer></v-spacer>
          <!-- Password Reset -->
          <v-form>
            <v-btn color="accent" mb-2 @click="resetPassword">Reset Password</v-btn>
          </v-form>
        </v-layout>
        <v-layout column>
          <!-- Contact Details -->
          <v-container grid-list-md>
            <v-layout row wrap>
              <v-flex xs12 sm6>
                <v-text-field
                  ref="first"
                  v-model="first"
                  :rules="[() => !!first || 'This field is required']"
                  box
                  required
                  placeholder="John"
                  label="First Name"></v-text-field>
              </v-flex>
              <v-flex xs12 sm6>
                <v-text-field
                  ref="last"
                  v-model="last"
                  :rules="[() => !!last || 'This field is required']"
                  box
                  required
                  placeholder="Doe"
                  label="Last Name"></v-text-field>
              </v-flex>
              <v-flex xs12>
                <v-text-field
                  ref="email"
                  v-model="email"
                  :rules="[
                    () => !!email || 'This field is required',
                    () => validateEmail(email) || 'Email address not in valid format'
                  ]"
                  box
                  required
                  placeholder="name@example.com"
                  label="Email address"
                  hint="Enter your email. Example: my.name@example.com"
                ></v-text-field>
              </v-flex>
              <v-flex xs12>
                <!-- Consider using a true parser later for validation of phone number
                https://github.com/googlei18n/libphonenumber -->
                <v-text-field
                  mask="phone"
                  v-model="phone"
                  box
                  placeholder="(123) 456-7890"
                  label="Phone number"
                  hint="Enter your phone number"
                ></v-text-field>
              </v-flex>
            </v-layout>
          </v-container>
          <!-- Notification Checkboxes -->
        </v-layout>
      </v-layout>
      <!-- Save Button -->
    </v-container>
  </section>

</template>

<script lang="js">
export default {
  name: 'user-profile-page',
  props: [],
  mounted () {

  },
  data () {
    return {
      errors: [],
      first: '',
      last: '',
      email: '',
      phone: ''
    };
  },
  methods: {
    resetPassword () {},
    validateEmail (email) {
      // From https://stackoverflow.com/a/9204568
      var re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      return re.test(email);
    }
  },
  computed: {

  }
};
</script>

<style scoped lang="scss">
.user-profile-page {
}
</style>

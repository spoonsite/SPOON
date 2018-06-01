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
          <v-flex>
            <v-card raised>
              <v-layout column align-center>
                <v-flex pt-2>
                  <v-icon size="8em">fas fa-user-tie</v-icon>
                </v-flex>
                <v-card-actions>
                  <v-btn flat color="orange">Change Avatar</v-btn>
                </v-card-actions>
              </v-layout>
            </v-card>
          </v-flex>
          <v-spacer></v-spacer>
          <!-- Password Reset -->
          <v-flex>
            <router-link :to="{name: 'Reset Password'}" style="text-decoration: none;">
              <v-btn
                color="accent"
                mb-2
              >Reset Password</v-btn>
            </router-link>
          </v-flex>
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
                  counter=80
                  placeholder="(123) 456-7890"
                  label="Phone number"
                  hint="Enter your phone number"
                ></v-text-field>
              </v-flex>
              <v-flex xs12>
                <v-select
                  :items="organizations"
                  :filter="orgFilter"
                  :rules="[() => !!currentOrg || 'This field is required']"
                  v-model="currentOrg"
                  item-text="description"
                  label="Select an Organization"
                  autocomplete
                  required
                  hint="Type to filter or click to select"
                ></v-select>
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
import axios from 'axios';

export default {
  name: 'user-profile-page',
  props: [],
  mounted () {
    this.getOrgs();
  },
  data () {
    return {
      errors: [],
      first: '',
      last: '',
      email: '',
      phone: '',
      organizations: [],
      currentOrg: undefined
    };
  },
  methods: {
    resetPassword () {},
    validateEmail (email) {
      // From https://stackoverflow.com/a/9204568
      var re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      return re.test(email);
    },
    orgFilter (item, queryText, itemText) {
      const hasValue = val => val != null ? val : '';
      const text = hasValue(item.description);
      const query = hasValue(queryText);
      return text.toString()
        .toLowerCase()
        .indexOf(query.toString().toLowerCase()) > -1;
    },
    getOrgs () {
      let that = this;
      axios
        .get('/openstorefront/api/v1/resource/organizations/lookup')
        .then(response => {
          that.organizations = response.data;
        })
        .catch(e => this.errors.push(e));
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

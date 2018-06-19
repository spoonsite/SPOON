<template lang="html">

<section class="user-profile-page">
<!-- Errors -->
<v-flex xs12>
  <div v-if="errors.length > 0">
    <ul>
      <li v-for="error in errors" :key="error" v-html='error'></li>
    </ul>
  </div>
</v-flex>

<v-form ref="form" v-model="valid" lazy-validation>
<v-container grid-list-xl text-xs-center>
<v-layout row wrap>
  <!-- Contact Details -->
  <v-flex xs12 sm6 pt-0 pb-0>
    <v-text-field
      ref="firstName"
      v-model="firstName"
      name="fname"
      :rules="[rules.required]"
      required
      counter=80
      maxLength="80"
      placeholder="John"
      label="First Name"
    ></v-text-field>
  </v-flex>
  <v-flex xs12 sm6 pt-0 pb-0>
    <v-text-field
      ref="lastName"
      v-model="lastName"
      name="lname"
      :rules="[rules.required]"
      required
      counter=80
      maxLength="80"
      placeholder="Doe"
      label="Last Name"
    ></v-text-field>
  </v-flex>
  <v-flex xs12 pt-0 pb-0>
    <v-text-field
      ref="email"
      type="email"
      name="email"
      id="email"
      v-model="email"
      :rules="[rules.required, rules.email]"
      required
      maxLength="1000"
      placeholder="name@example.com"
      label="Email address"
      hint="Enter your email. Example: my.name@example.com"
    ></v-text-field>
  </v-flex>
  <v-flex xs12 pt-0 pb-0>
    <!-- Consider using a true parser later for validation of phone number
    https://github.com/googlei18n/libphonenumber -->
    <v-text-field
      ref="phone"
      v-model="phone"
      name="phone"
      type="tel"
      maxLength="80"
      placeholder="(123) 456-7890"
      label="Phone number"
      hint="Enter your phone number"
    ></v-text-field>
  </v-flex>
  <v-flex xs12 pt-0 pb-0>
    <v-select
      ref="user_org"
      :items="organizations"
      name="organization"
      item-text="description"
      item-value="description"
      :filter="orgFilter"
      :rules="[rules.required]"
      v-model="currentOrg"
      label="Select an Organization"
      required
      maxLength="120"
      combobox
      hint="Type to filter or click to select"
    ></v-select>
  </v-flex>
  <v-flex xs12 pt-0 pb-0>
    <v-text-field
      ref="user_position"
      v-model="position"
      name="organization-title"
      maxLength="255"
      label="Position Title"
      hint="Enter your current title"
    ></v-text-field>
  </v-flex>
  <v-flex xs12 pt-0>
    <v-select
      ref="user_app_role"
      :items="userTypeCodes"
      item-text="description"
      item-value="description"
      :rules="[rules.required]"
      v-model="userTypeCode"
      label="User Role"
      required
      hint="Click to select a role"
    ></v-select>
  </v-flex>
  <!-- Notification Checkboxes -->
  <v-flex xs1>
    <v-tooltip bottom>
      <v-icon slot="activator">fas fa-question-circle</v-icon>
      <span>Receive a periodic email about recent changes</span>
    </v-tooltip>
  </v-flex>
  <v-flex xs11>
    <v-switch
      ref="periodic_notify"
      label="Notify about Updates"
      ripple
      v-model="notify"
      id="notify"
    ></v-switch>
  </v-flex>
  <!-- Reset Button -->
  <v-flex xs12 sm2>
    <v-btn
      block
      v-on:click="reset"
      color="accent"
    >Reset Form</v-btn>
  </v-flex>
  <!-- Save Button -->
  <v-flex xs12 sm10>
    <v-alert v-model="saved" type="success" dismissible>
      Updated User Profile
    </v-alert>
    <v-alert v-model="errorOnSave" type="error" dismissible>
      Error saving updates to User Profile
    </v-alert>
    <v-btn
      v-if="!saved && !errorOnSave"
      :disabled="!valid"
      block
      v-on:click="updateProfile"
      color="accent"
    >Save</v-btn>
  </v-flex>
</v-layout>
</v-container>
</v-form>
</section>

</template>

<script lang="js">
import validators from '../util/validators';

export default {
  name: 'user-profile-page',
  mixins: [validators],
  mounted () {
    this.getCurrentUserName();
    this.getOrgs();
    this.getRoles();
  },
  data () {
    return {
      errors: [],
      username: '',
      firstName: '',
      lastName: '',
      email: '',
      phone: '',
      organizations: [],
      currentOrg: undefined,
      position: '',
      userTypeCodes: [],
      userTypeCode: undefined,
      notify: false,
      monthly: false,
      saved: false,
      errorOnSave: false,
      valid: true
    };
  },
  methods: {
    getRestOfUserData () {
      let url = '/openstorefront/api/v1/resource/userprofiles/' + this.username;
      this.$http
        .get(url)
        .then(response => {
          let org;
          if (response.data.organization) {
            org = {'code': '', 'description': response.data.organization};
          } else {
            org = '';
          }
          this.firstName = response.data.firstName;
          this.lastName = response.data.lastName;
          this.email = response.data.email;
          this.phone = response.data.phone;
          this.currentOrg = org;
          this.position = response.data.positionTitle;
          this.userTypeCode = response.data.userTypeDescription;
          this.notify = response.data.notifyOfNew;
        })
        .catch(e => this.errors.push(e));
    },
    getCurrentUserName () {
      this.$http
        .get('/openstorefront/api/v1/resource/userprofiles/currentuser')
        .then(response => {
          this.username = response.data.username;
          if (this.username !== 'ANONYMOUS') { this.getRestOfUserData(); }
        })
        .catch(e => this.errors.push(e));
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
      this.$http
        .get('/openstorefront/api/v1/resource/organizations/lookup')
        .then(response => {
          this.organizations = response.data;
        })
        .catch(e => this.errors.push(e));
    },
    getRoles () {
      this.$http
        .get('/openstorefront/api/v1/resource/lookuptypes/UserTypeCode')
        .then(response => {
          this.userTypeCodes = response.data.filter(item => {
            return item.activeStatus === 'A';
          });
        })
        .catch(e => this.errors.push(e));
    },
    updateProfile () {
      if (this.$refs.form.validate()) {
        let newProfile = {
          'userTypeCode': this.userTypeCodes.find(each => each.description === this.userTypeCode).code, // User Role
          'firstName': this.firstName,
          'lastName': this.lastName,
          'email': this.email,
          'phone': this.phone,
          'positionTitle': this.position,
          'organization': typeof this.currentOrg === 'string' ? this.currentOrg : this.currentOrg.description,
          'notifyOfNew': this.notify
        };

        this.$http
          .put('/openstorefront/api/v1/resource/userprofiles/' + this.username, newProfile)
          .then(response => {
            this.saved = true;
            this.getRestOfUserData();
          })
          .catch(e => {
            this.errors.push(e);
            this.errorOnSave = true;
          });
      }
    },
    reset () {
      this.getRestOfUserData();
    }
  }
};
</script>

<style scoped lang="scss">
</style>

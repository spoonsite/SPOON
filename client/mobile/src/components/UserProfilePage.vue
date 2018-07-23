<template lang="html">

<section class="user-profile-page">
<v-alert :value="disableForm" type="info">Editing user information has been disabled by the admin.</v-alert>

<v-form ref="form" v-model="valid" lazy-validation>
<v-container grid-list-xl text-xs-center>
<v-layout row wrap>
  <!-- Contact Details -->
  <v-flex xs12 sm6 pt-0 pb-0>
    <v-text-field
      ref="firstName"
      v-model="user.firstName"
      name="fname"
      :rules="[rules.required]"
      required
      counter=80
      maxLength="80"
      placeholder="John"
      label="First Name"
      :disabled="disableForm"
    ></v-text-field>
  </v-flex>
  <v-flex xs12 sm6 pt-0 pb-0>
    <v-text-field
      ref="lastName"
      v-model="user.lastName"
      name="lname"
      :rules="[rules.required]"
      required
      counter=80
      maxLength="80"
      placeholder="Doe"
      label="Last Name"
      :disabled="disableForm"
    ></v-text-field>
  </v-flex>
  <v-flex xs12 pt-0 pb-0>
    <v-text-field
      ref="email"
      type="email"
      name="email"
      id="email"
      v-model="user.email"
      :rules="[rules.required, rules.email]"
      required
      maxLength="1000"
      placeholder="name@example.com"
      label="Email address"
      hint="Enter your email. Example: my.name@example.com"
      :disabled="disableForm"
    ></v-text-field>
  </v-flex>
  <v-flex xs12 pt-0 pb-0>
    <!-- Consider using a true parser later for validation of phone number
    https://github.com/googlei18n/libphonenumber -->
    <v-text-field
      ref="phone"
      v-model="user.phone"
      name="phone"
      type="tel"
      maxLength="80"
      placeholder="(123) 456-7890"
      label="Phone number"
      hint="Enter your phone number"
      :disabled="disableForm"
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
      v-model="user.currentOrg"
      label="Select an Organization"
      required
      maxLength="120"
      combobox
      hint="Type to filter or click to select"
      :disabled="disableForm"
    ></v-select>
  </v-flex>
  <v-flex xs12 pt-0 pb-0>
    <v-text-field
      ref="user_position"
      v-model="user.position"
      name="organization-title"
      maxLength="255"
      label="Position Title"
      hint="Enter your current title"
      :disabled="disableForm"
    ></v-text-field>
  </v-flex>
  <v-flex xs12 pt-0>
    <v-select
      ref="user_app_role"
      :items="userTypeCodes"
      item-text="description"
      item-value="description"
      :rules="[rules.required]"
      v-model="user.userTypeCode"
      label="User Role"
      required
      hint="Click to select a role"
      :disabled="disableForm"
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
      v-model="user.notify"
      id="notify"
      :disabled="disableForm"
    ></v-switch>
  </v-flex>
  <!-- Save Button -->
  <v-flex xs12 sm10>
    <v-btn
      :disabled="!valid || disableForm || !formChanged"
      :loading="saving"
      block
      @click="updateProfile"
      color="accent"
    >Save</v-btn>
  </v-flex>
  <!-- Reset Button -->
  <v-flex xs12 sm2>
    <v-btn
      :disabled="!valid || disableForm || !formChanged"
      block
      @click="reset"
      color="accent"
    >Reset Form</v-btn>
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
    this.setUserInfo();
    this.getOrgs();
    this.getRoles();
  },
  data () {
    return {
      errors: [],
      user: {
        firstName: '',
        lastName: '',
        email: '',
        phone: '',
        currentOrg: '',
        position: '',
        userTypeCode: '',
        notify: false
      },
      cachedUser: {},
      username: '',
      organizations: [],
      userTypeCodes: [],
      monthly: false,
      saving: false,
      valid: true,
      formChanged: false
    };
  },
  computed: {
    disableForm () {
      return !!this.$store.state.securitypolicy.disableUserInfoEdit;
    }
  },
  watch: {
    user: {
      handler (after, before) {
        this.formChanged = JSON.stringify(this.user) !== JSON.stringify(this.cachedUser);
      },
      deep: true
    }
  },
  methods: {
    setUserInfo () {
      this.user.firstName = this.$store.state.currentUser.firstName;
      this.user.lastName = this.$store.state.currentUser.lastName;
      this.user.email = this.$store.state.currentUser.email;
      this.user.phone = this.$store.state.currentUser.phone;
      this.user.currentOrg = this.$store.state.currentUser.organization;
      this.user.position = this.$store.state.currentUser.positionTitle;
      this.user.userTypeCode = this.$store.state.currentUser.userTypeDescription;
      this.user.notify = this.$store.state.currentUser.notifyOfNew;

      this.username = this.$store.state.currentUser.username;

      this.cachedUser = JSON.parse(JSON.stringify(this.user));
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
        });
    },
    getRoles () {
      this.$http
        .get('/openstorefront/api/v1/resource/lookuptypes/UserTypeCode')
        .then(response => {
          this.userTypeCodes = response.data.filter(item => {
            return item.activeStatus === 'A';
          });
        });
    },
    updateProfile () {
      if (this.$refs.form.validate()) {
        this.saving = true;
        let newProfile = {
          'firstName': this.user.firstName,
          'lastName': this.user.lastName,
          'email': this.user.email,
          'phone': this.user.phone,
          'organization': this.user.currentOrg,
          'positionTitle': this.user.position,
          'userTypeCode': this.userTypeCodes.find(each => each.description === this.user.userTypeCode).code, // User Role
          'notifyOfNew': this.user.notify
        };

        this.$http
          .put('/openstorefront/api/v1/resource/userprofiles/' + this.username, newProfile)
          .then(response => {
            // resyncing the user data
            this.$store.dispatch('setCurrentUser', () => {
              this.setUserInfo();
            });
            this.$toasted.show('Profile updated');
            this.saving = false;
            this.formChanged = false;
          })
          .catch(e => {
            this.$toasted.error('Error updating profile');
            this.saving = false;
          });
      }
    },
    reset () {
      this.user = JSON.parse(JSON.stringify(this.cachedUser));
    }
  }
};
</script>

<style scoped lang="scss">
</style>

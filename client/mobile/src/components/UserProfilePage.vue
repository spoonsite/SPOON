<template lang="html">

<section class="user-profile-page">
<v-container grid-list-xl text-xs-center>
<v-layout row wrap>
  <!-- Errors -->
  <v-flex xs12>
    <div v-if="errors.length > 0">
      <ul>
        <li v-for="error in errors" :key="error" v-html='error'></li>
      </ul>
    </div>
  </v-flex>
  <!-- Avatar Icon -->
  <v-flex xs12 sm5 md3>
    <v-card raised>
      <v-layout column align-center>
        <v-flex pt-2>
          <v-icon size="8em">fas fa-user-tie</v-icon>
        </v-flex>
        <!-- Can't yet change or add avatar icons. Uncomment and set up once we can
        <v-card-actions>
          <v-btn flat color="orange">Change Avatar</v-btn>
        </v-card-actions> -->
      </v-layout>
    </v-card>
  </v-flex>
  <!-- Password Reset -->
  <v-flex xs12 sm5 md5 offset-xs0 offset-lg2>
    <router-link :to="{name: 'Reset Password'}" style="text-decoration: none;">
      <v-btn
        color="accent"
        mb-2
      >Reset Password</v-btn>
    </router-link>
  </v-flex>
  <!-- Contact Details -->
  <v-flex xs12 sm6>
    <v-text-field
      ref="first"
      v-model="first"
      :rules="[() => !!first || 'This field is required']"
      required
      placeholder="John"
      label="First Name"
    ></v-text-field>
  </v-flex>
  <v-flex xs12 sm6>
    <v-text-field
      ref="last"
      v-model="last"
      :rules="[() => !!last || 'This field is required']"
      required
      placeholder="Doe"
      label="Last Name"
    ></v-text-field>
  </v-flex>
  <v-flex xs12>
    <v-text-field
      ref="email"
      v-model="email"
      :rules="[
        () => !!email || 'This field is required',
        () => validateEmail(email) || 'Email address not in valid format'
      ]"
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
      v-model="phone"
      counter=80
      placeholder="(123) 456-7890"
      label="Phone number"
      hint="Enter your phone number"
    ></v-text-field>
  </v-flex>
  <v-flex xs12>
    <v-select
      :items="organizations"
      item-text="description"
      item-value="description"
      :filter="orgFilter"
      :rules="[() => !!currentOrg || 'This field is required']"
      v-model="currentOrg"
      label="Select an Organization"
      required
      combobox
      hint="Type to filter or click to select"
    ></v-select>
  </v-flex>
    <v-flex xs12>
      <v-text-field
        ref="position"
        v-model="position"
        label="Position Title"
        hint="Enter your current title"
      ></v-text-field>
  </v-flex>
  <v-flex xs12>
    <v-select
      :items="userTypeCodes"
      item-text="description"
      item-value="description"
      :rules="[() => !!userTypeCode || 'This field is required']"
      v-model="userTypeCode"
      label="User Role"
      required
      hint="Click to selecta role"
    ></v-select>
  </v-flex>
  <!-- Notification Checkboxes -->
  <v-flex xs12>
    <v-switch
      label="Receive periodic email"
      ripple
      v-model="notify"
      id="notify"
    ></v-switch>
  </v-flex>
  <!-- Monthly email not set up yet. Once it is, uncomment this section
  <v-flex xs12>
    <v-switch
      label="Receive monthly email"
      ripple
      v-model="monthly"
      id="monthly"
      disabled
    ></v-switch>
  </v-flex> -->
  <!-- Save Button -->
  <v-flex xs10 offset-xs1>
    <v-alert v-model="saved" type="success" dismissible>
      Updated User Profile
    </v-alert>
    <v-alert v-model="errorOnSave" type="error" dismissible>
      Error saving updates to User Profile
    </v-alert>
    <v-btn
      v-if="!saved && !errorOnSave"
      block
      round
      v-on:click="updateProfile"
      color="accent"
    >Save</v-btn>
  </v-flex>
</v-layout>
</v-container>
</section>

</template>

<script lang="js">

export default {
  name: 'user-profile-page',
  props: [],
  mounted () {
    this.getCurrentUserName();
    this.getOrgs();
    this.getRoles();
  },
  data () {
    return {
      errors: [],
      username: '',
      first: '',
      last: '',
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
      errorOnSave: false
    };
  },
  methods: {
    getRestOfUserData () {
      let that = this;
      let url = '/openstorefront/api/v1/resource/userprofiles/' + that.username;
      this.$http
        .get(url)
        .then(response => {
          that.first = response.data.firstName;
          that.last = response.data.lastName;
          that.email = response.data.email;
          that.phone = response.data.phone;
          that.currentOrg = {'code': '', 'description': response.data.organization};
          that.position = response.data.positionTitle;
          that.userTypeCode = response.data.userTypeDescription;
          that.notify = response.data.notifyOfNew;
        })
        .catch(e => this.errors.push(e));
    },
    getCurrentUserName () {
      let that = this;
      this.$http
        .get('/openstorefront/api/v1/resource/userprofiles/currentuser')
        .then(response => {
          that.username = response.data.username;
          that.getRestOfUserData();
        })
        .catch(e => this.errors.push(e));
    },
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
      this.$http
        .get('/openstorefront/api/v1/resource/organizations/lookup')
        .then(response => {
          that.organizations = response.data;
        })
        .catch(e => this.errors.push(e));
    },
    getRoles () {
      let that = this;
      this.$http
        .get('/openstorefront/api/v1/resource/lookuptypes/UserTypeCode')
        .then(response => {
          that.userTypeCodes = response.data.filter(item => {
            return item.activeStatus === 'A';
          });
        })
        .catch(e => this.errors.push(e));
    },
    updateProfile () {
      let that = this;
      let org = '';
      if (typeof this.currentOrg === 'string') {
        org = this.currentOrg;
      } else {
        org = this.currentOrg.description;
      }
      let newProfile = {
        'userTypeCode': this.userTypeCodes.find(each => each.description === this.userTypeCode).code, // User Role
        'firstName': this.first,
        'lastName': this.last,
        'email': this.email,
        'phone': this.phone,
        'positionTitle': this.position,
        'organization': org,
        'notifyOfNew': this.notify
      };

      this.$http
        .put('/openstorefront/api/v1/resource/userprofiles/' + this.username, newProfile)
        .then(response => {
          that.saved = true;
          that.getRestOfUserData();
        })
        .catch(e => {
          that.errors.push(e);
          that.errorOnSave = true;
        });
    }
  },
  computed: {

  }
};
</script>

<style scoped lang="scss">
</style>

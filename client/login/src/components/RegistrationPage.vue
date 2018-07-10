<template>
  <v-layout mt-3 mx-3>
    <v-flex xs12 md4 offset-md4 sm6 offset-sm3>

      <h2>Registration - Fill out the form to signup for an account</h2>

      <v-layout row wrap>
      <v-flex xs12 mt-3>
      <v-card class="elevation-5">
        <v-toolbar color="primary" dark dense>
          <v-toolbar-title>Login Credentials</v-toolbar-title>
        </v-toolbar>
        <v-card-text>
          <v-form v-model="credentials.valid" ref="credentialsForm">
            <v-layout row>
              <v-text-field
                prepend-icon="person"
                name="username"
                label="Username"
                type="text"
                :rules="usernameRules"
                v-model="credentials.username"
              ></v-text-field>
            </v-layout>
            <v-layout row>
              <v-text-field
                prepend-icon="lock"
                name="password"
                label="Password"
                type="password"
                :rules="password1Rules"
                v-model="credentials.password1"
              ></v-text-field>
            </v-layout>
            <v-layout row>
              <v-text-field
                prepend-icon="lock"
                name="confirmpassword"
                label="Confirm Password"
                type="password"
                :rules="password2Rules"
                v-model="credentials.password2"
              ></v-text-field>
            </v-layout>
          </v-form>
        </v-card-text>
        <v-card-actions>
        </v-card-actions>
      </v-card>
    </v-flex>

    <v-flex xs12 mt-3>
    <v-card class="elevation-5">
      <v-toolbar color="primary" dark dense>
        <v-toolbar-title>User Information</v-toolbar-title>
      </v-toolbar>
      <v-card-text>
        <v-form v-model="userInformation.valid" ref="userInformationForm">
            <v-text-field
              name="firstname"
              label="First Name"
              type="text"
              v-model="userInformation.firstName"
              :rules="firstNameRules"
              required
            ></v-text-field>

            <v-text-field
              name="lastname"
              label="Last Name"
              type="text"
              v-model="userInformation.lastName"
              :rules="lastNameRules"
              required
            ></v-text-field>

            <v-select
              name="organization"
              label="Organization"
              :items="organizationsList"
              combobox
              item-text="description"
              v-model="userInformation.organization"
              required
            ></v-select>

            <v-text-field
              name="position"
              label="Position Title"
              type="text"
              v-model="userInformation.positionTitle"
            ></v-text-field>

            <v-text-field
              name="email"
              label="Business Email"
              type="text"
              v-model="userInformation.email"
              :rules="[rules.required, rules.email]"
              required
            ></v-text-field>

            <v-text-field
              name="phone"
              label="Business Phone"
              type="text"
              v-model="userInformation.phone"
              :rules="[v => !!v || 'Phone number is required']"
              required
            ></v-text-field>

            <v-select
              name="usertype"
              v-model="userInformation.userType"
              :items="userTypesList"
              item-text="description"
              item-value="code"
              label="User Type"
            ></v-select>
        </v-form>
      </v-card-text>
    </v-card>
    </v-flex>

    <v-flex xs12 mt-3>
    <v-card class="elevation-5">
      <v-toolbar color="primary" dark dense>
        <v-toolbar-title>Email Verification Code</v-toolbar-title>
      </v-toolbar>
      <v-card-text>
        <v-form v-model="verification.valid" ref="verifyForm">
          <v-btn color="accent" :disabled="!verificationValid" style="margin-bottom:2em;" @click="register('POST')">Get Verification Code</v-btn>
          <v-text-field
            prepend-icon="lock"
            name="verifycode"
            label="Enter the verification code from your email here"
            type="text"
            :rules="[ v => !!v || 'Validation code is required' ]"
            v-model="verification.code"
          ></v-text-field>
        </v-form>
      </v-card-text>
      <v-card-actions>
      </v-card-actions>
    </v-card>
    </v-flex>
    </v-layout>

    <div class="button-wrapper mt-2">
      <div class="btn1">
        <v-btn block color="accent" :disabled="!valid" @click="register()"><v-icon class="icon" light>check</v-icon>Signup</v-btn>
      </div>
      <div class="btn2">
      <v-btn block color="accent" @click="cancel()"><v-icon light class="icon">cancel</v-icon>Cancel</v-btn>
      </div>
    </div>

    <v-dialog v-model="verificationDialog" max-width="300px">
      <v-card tile>
        <v-card-text>Verificaton code sent. Check your email.</v-card-text>
        <v-card-actions>
          <v-btn @click="verificationDialog = false"><v-icon class="icon">fas fa-times</v-icon>Close</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="successDialog" max-width="300px">
      <v-card tile>
        <v-card-text>Registration Success! Return to login screen to login with your new username and password.</v-card-text>
        <v-card-actions>
          <v-btn @click="$router.push('/')"><v-icon class="icon">fas fa-sign-in-alt</v-icon>Return to Login</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

  </v-flex>
</v-layout>
</template>

<script>
import validators from '../util/validators';

export default {
  name: 'RegistrationPage',
  mixins: [validators],
  mounted: function () {
    this.getOrganizations();
    this.getUserTypes();
  },
  data: function () {
    return {
      verificationDialog: false,
      successDialog: false,
      credentials: {
        valid: false,
        username: '',
        password1: '',
        password2: ''
      },
      userInformation: {
        valid: false,
        firstName: '',
        lastName: '',
        organization: '',
        positionTitle: '',
        email: '',
        phone: '',
        userType: ''
      },
      registrationId: '',
      verification: {
        code: '',
        valid: false
      },
      usernameRules: [
        v => !!v || 'Username is required',
        v =>
          /^\w+$/.test(v) ||
          'Username must be valid'
      ],
      password1Rules: [
        v => !!v || 'Password is required',
        v =>
          /^(?=.*[A-Za-z])(?=.*\d)(?=.*[~`!@#$%^&*()-+=<>:;"',.?])[A-Za-z\d~`!@#$%^&*()-+=<>:;"',.?]{8,}$/.test(v) ||
          'Password must contain 1 uppercase, 1 number, and 1 special character (i.e. @$!%*#?&)'
      ],
      password2Rules: [
        v => !!v || 'Password verification is required',
        v => this.credentials.password1 === this.credentials.password2 || 'Passwords must match'
      ],
      organizationsList: [],
      userTypesList: [],
      firstNameRules: [
        v => !!v || 'First name is required'
      ],
      lastNameRules: [
        v => !!v || 'Last name is required'
      ],
      filter (item, queryText, itemText) {
        const hasValue = val => val != null ? val : '';
        const text = hasValue(item.description);
        const query = hasValue(queryText);
        return text.toString()
          .toLowerCase()
          .indexOf(query.toString().toLowerCase()) > -1;
      }
    };
  },
  methods: {
    getOrganizations () {
      this.$http.get('/openstorefront/api/v1/resource/organizations/lookup')
        .then(response => {
          this.organizationsList = response.data;
        })
        .catch(e => this.errors.push(e));
    },
    getUserTypes () {
      this.$http.get('/openstorefront/api/v1/resource/lookuptypes/UserTypeCode')
        .then(response => {
          this.userTypesList = response.data;
        })
        .catch(e => this.errors.push(e));
    },
    register (verb) {
      // call POST for getting the email verification code
      // call PUT to create a user with valid verification code
      let data = {
        username: this.credentials.username,
        password: this.credentials.password1,
        confirmPassord: this.credentials.password2,
        firstName: this.userInformation.firstName,
        lastName: this.userInformation.lastName,
        organization: typeof this.userInformation.organization === 'string' ? this.userInformation.organization : this.userInformation.organization.description,
        positionTitle: this.userInformation.positionTitle,
        email: this.userInformation.email,
        phone: this.userInformation.phone,
        userTypeCode: this.userInformation.userType,
        registrationId: this.registrationId,
        verificationCode: this.verification.code
      };
      if (verb === 'POST') {
        this.$http.post('/openstorefront/api/v1/resource/userregistrations', data)
          .then(response => {
            this.verificationDialog = true;
            this.registrationId = response.data.registrationId;
            this.verificationCode = response.data.verificationCode;
          })
          .catch(e => this.errors.push(e));
      } else {
        this.$http.put('/openstorefront/api/v1/resource/userregistrations', data)
          .then(response => {
            this.successDialog = true;
          })
          .catch(e => this.errors.push(e));
      }
    },
    signup () {
      return 0;
    },
    cancel () {
      this.$refs.userInformationForm.reset();
      this.$refs.verifyForm.reset();
      this.$refs.credentialsForm.reset();
    }
  },
  computed: {
    valid () {
      return this.credentials.valid && this.userInformation.valid && this.verification.valid;
    },
    verificationValid () {
      return this.credentials.valid && this.userInformation.valid;
    }
  }
};
</script>

<style scoped>
.button-wrapper {
  width: 100%;
}
.btn1 {
  padding-right: 1em;
  width: 50%;
  float: left;
}
.btn2 {
  padding-left: 1em;
  width: 50%;
  float: left;
}
.icon {
  padding-right: 0.5em;
}
@media screen and (max-width: 599px) {
  .btn1 {
    padding-right: 0;
    width: 100%;
  }
  .btn2 {
    padding-left: 0;
    width: 100%;
  }
}
</style>

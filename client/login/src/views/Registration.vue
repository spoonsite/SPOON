<template>
  <v-layout mt-3 mx-3>
    <v-flex xs12 md4 offset-md4 sm6 offset-sm3>

      <h2 class="headline text-xs-center">Registration
        <v-btn
          v-if="$store.state.branding.loginRegistrationVideoUrl"
          @click="videoDialog = true"
          icon
          small
          style="position: relative; bottom: 1em; right: 0.5em;"
        ><v-icon style="font-size: 16px;">fas fa-question</v-icon></v-btn>
      </h2>

      <v-dialog
        v-if="$store.state.branding.loginRegistrationVideoUrl"
        v-model="videoDialog"
        max-width="800px"
      >
        <video ref="registrationVideo" controls class="video elevation-5" :src="$store.state.branding.loginRegistrationVideoUrl"></video>
      </v-dialog>

      <v-layout row wrap>
      <v-flex xs12 mt-3>
      <v-card class="elevation-5">
        <v-toolbar color="primary" dark dense>
          <v-toolbar-title>Login Credentials</v-toolbar-title>
        </v-toolbar>
        <v-card-text>
          <v-form
            v-model="credentials.valid"
            ref="credentialsForm"
            v-on:submit.prevent
          >
            <v-layout row>
              <v-text-field
                prepend-icon="person"
                name="username"
                label="Username"
                type="text"
                :rules="usernameRules"
                :error-messages="errors.username"
                v-model="credentials.username"
              ></v-text-field>
            </v-layout>
            <v-layout row>
              <v-text-field
                prepend-icon="lock"
                name="password"
                label="Password"
                type="password"
                :rules="[rules.required, rules.password]"
                :error-messages="errors.password"
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
                :error-messages="errors.confirmPassword"
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
        <v-form
          v-model="userInformation.valid"
          ref="userInformationForm"
          v-on:submit.prevent
        >
            <v-text-field
              name="firstname"
              label="First Name"
              type="text"
              v-model="userInformation.firstName"
              :rules="[rules.required]"
              :error-messages="errors.firstName"
              required
            ></v-text-field>

            <v-text-field
              name="lastname"
              label="Last Name"
              type="text"
              v-model="userInformation.lastName"
              :rules="[rules.required]"
              :error-messages="errors.lastName"
              required
            ></v-text-field>

            <v-select
              name="organization"
              label="Organization"
              :items="organizationsList"
              :rules="[rules.required]"
              combobox
              item-text="description"
              v-model="userInformation.organization"
              :error-messages="errors.organization"
              required
            ></v-select>

            <v-text-field
              name="position"
              label="Position Title"
              type="text"
              v-model="userInformation.positionTitle"
              :error-messages="errors.positionTitle"
            ></v-text-field>

            <v-text-field
              name="email"
              label="Business Email"
              type="text"
              v-model="userInformation.email"
              :rules="[rules.required, rules.email]"
              :error-messages="errors.email"
              required
            ></v-text-field>

            <v-text-field
              name="phone"
              label="Business Phone"
              type="text"
              v-model="userInformation.phone"
              :rules="[rules.required]"
              :error-messages="errors.phone"
              required
            ></v-text-field>

            <v-select
              name="usertype"
              v-model="userInformation.userType"
              :items="userTypesList"
              :rules="[rules.required]"
              item-text="description"
              item-value="code"
              label="User Type"
              :error-messages="errors.userTypeCode"
              required
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
        <v-form
          v-model="verification.valid"
          ref="verifyForm"
          v-on:submit.prevent
        >
          <v-btn
            color="primary"
            :loading="verificationLoading"
            style="margin-bottom:2em;"
            @click="register('POST')"
          >Get Verification Code</v-btn>
          <v-text-field
            prepend-icon="lock"
            name="verifycode"
            label="Enter the verification code from your email here"
            type="text"
            :rules="[rules.required]"
            :error-messages="errors.verificationCode"
            v-model="verification.code"
          ></v-text-field>
        </v-form>
      </v-card-text>
    </v-card>
    </v-flex>
    </v-layout>

    <div class="button-wrapper mt-2">
      <div class="btn1 pb-2">
        <v-btn
          block
          :loading="signupLoading"
          @click="register()"
        >
          <v-icon class="custon-icon" light>check</v-icon>Signup
        </v-btn>
      </div>
      <div class="btn2 pb-2">
      <v-btn block dark to="/"><v-icon light class="custom-icon">cancel</v-icon>Return to Login</v-btn>
      </div>
    </div>

    <v-dialog v-model="successDialog" max-width="300px">
      <v-card tile>
        <v-card-title>
          <h2>Registration Success!</h2>
          <v-alert
            type="info"
            style="width: 100%;"
            :value="!$store.state.securitypolicy.autoApproveUsers"
          >You application has been submitted and is awaiting approval by the admin.</v-alert>
        </v-card-title>
        <v-card-text>
          Return to login screen to login with your new username and password.
        </v-card-text>
        <v-card-actions>
          <v-btn to="/"><v-icon class="custom-icon">fas fa-sign-in-alt</v-icon>Return to Login</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

  </v-flex>
</v-layout>
</template>

<script>
import validators from '@/util/validators'

export default {
  name: 'RegistrationPage',
  mixins: [validators],
  mounted: function () {
    this.getOrganizations()
    this.getUserTypes()
  },
  data: function () {
    return {
      verificationLoading: false,
      signupLoading: false,
      successDialog: false,
      videoDialog: false,
      errors: {
        username: [],
        password: [],
        confirmPassord: [],
        firstName: [],
        lastName: [],
        organization: [],
        positionTitle: [],
        email: [],
        phone: [],
        userTypeCode: [],
        registrationId: [],
        verificationCode: []
      },
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
        v => /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(v) ||
          /^\w{5,}$/.test(v) ||
          'Username must be at least 5 characters, no special characters, or email'
      ],
      password2Rules: [
        v => !!v || 'Password verification is required',
        v => this.credentials.password1 === this.credentials.password2 || 'Passwords must match'
      ],
      organizationsList: [],
      userTypesList: []
    }
  },
  methods: {
    getOrganizations () {
      this.$http.get('/openstorefront/api/v1/resource/organizations/lookup')
        .then(response => {
          this.organizationsList = response.data
        })
    },
    getUserTypes () {
      this.$http.get('/openstorefront/api/v1/resource/lookuptypes/UserTypeCode')
        .then(response => {
          this.userTypesList = response.data
        })
    },
    register (verb) {
      // call POST for getting the email verification code
      // call PUT to create a user with valid verification code
      const data = {
        username: this.credentials.username,
        password: this.credentials.password1,
        confirmPassord: this.credentials.password2,
        firstName: this.userInformation.firstName,
        lastName: this.userInformation.lastName,
        organization: typeof this.userInformation.organization === 'string' ? this.userInformation.organization : this.userInformation.organization.description,
        positionTitle: this.userInformation.positionTitle,
        email: this.userInformation.email.toLowerCase(),
        phone: this.userInformation.phone,
        userTypeCode: this.userInformation.userType,
        registrationId: this.registrationId,
        verificationCode: this.verification.code
      }
      if (verb === 'POST') {
        this.verificationLoading = true
        this.$http.post('/openstorefront/api/v1/resource/userregistrations', data)
          .then(response => {
            this.clearErrors()
            if (response.data.success === false) {
              this.responseErrorHandler(response, 'Error getting registration code')
            } else {
              this.$toasted.success('Verification code sent to your email')
              this.registrationId = response.data.registrationId
              this.verificationCode = response.data.verificationCode
            }
            this.verificationLoading = false
            this.signupLoading = false
          })
      } else {
        this.signupLoading = true
        this.$http.put('/openstorefront/api/v1/resource/userregistrations', data)
          .then(response => {
            this.clearErrors()
            if (response.data.success === false) {
              this.responseErrorHandler(response, 'Registration error')
            } else {
              this.successDialog = true
            }
            this.verificationLoading = false
            this.signupLoading = false
          })
      }
    },
    signup () {
      return 0
    },
    responseErrorHandler (response, message) {
      this.$toasted.error(message)
      if (response.data.errors.entry) {
        response.data.errors.entry.forEach((el) => {
          this.errors[el.key] = [el.value]
        })
      }
    },
    clearErrors () {
      for (const key in this.errors) {
        this.errors[key] = []
      }
    }
  },
  watch: {
    videoDialog (newState, oldState) {
      if (!newState) {
        this.$refs.registrationVideo.pause()
      } else {
        this.$refs.registrationVideo.play()
      }
    }
  }
}
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
.custom-icon {
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
.video {
  width: 100%;
  display: block;
}
</style>

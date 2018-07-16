<template lang="html">
  <div>

    <v-layout mt-3 mx-3>
      <v-flex xs12 md4 offset-md4 sm6 offset-sm3>
        <v-card class="elevation-5 mt-3 mx-3">
          <v-card-text>
            <v-form v-model="valid" @submit.prevent="submitEmail()">
              <v-text-field
                prepend-icon="vpn_key"
                name="exesting-password"
                label="Existing Password"
                type="password"
                v-model="existing"
                :rules="existingRules"
              ></v-text-field>
              <v-text-field
                prepend-icon="lock"
                name="password1"
                label="New Password"
                type="password"
                v-model="password1"
                :rules="password1Rules"
              ></v-text-field>
              <v-text-field
                prepend-icon="lock"
                name="password2"
                label="Verify New Password"
                type="password"
                v-model="password2"
                :rules="password2Rules"
              ></v-text-field>
            </v-form>
          </v-card-text>
          <v-card-actions>
            <v-btn :disabled="!valid" block color="accent" @click="submitUpdate()">Update Password</v-btn>
          </v-card-actions>
        </v-card>
      </v-flex>
    </v-layout>

    <v-dialog v-model="confirmationDialog" max-width="300px">
      <v-card tile>
        <v-card-text>Password has been updated.</v-card-text>
        <v-card-actions>
          <v-btn @click="$router.push('/')"><v-icon>fas fa-return</v-icon>Return to Search</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="errorDialog" max-width="300px">
      <v-card tile>
        <v-card-text>{{ errorText }}</v-card-text>
        <v-card-actions>
          <v-btn @click="errorDialog = false"><v-icon>fas fa-return</v-icon>Try again</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script lang="js">
export default {
  name: 'reset-password-page',
  props: [],
  mounted () {

  },
  data: function () {
    return {
      confirmationDialog: false,
      errorDialog: false,
      errorText: 'Error',
      valid: false,
      existing: '',
      existingRules: [
        v => !!v || 'Username is required'
      ],
      password1: '',
      password2: '',
      password1Rules: [
        v => !!v || 'Password is required',
        v => {
          let regex = new RegExp('^(?=.*[A-Z])(?=.*\\d)(?=.*[~`!@#$%^&*()-+=<>:;"\',.?])[A-Za-z\\d~`!@#$%^&*()-+=<>:;"\',.?]{8,}$');
          return regex.test(v) ||
          `Password must contain 1 uppercase, 1 number, 1 special character (i.e. @$!%*#?&), and be at least 8 characters`;
        }
      ],
      password2Rules: [
        v => !!v || 'Password verification is required',
        v => this.password1 === this.password2 || 'Passwords must match'
      ]
    };
  },
  methods: {
    submitUpdate () {
      this.$http.post(`/openstorefront/api/v1/service/security/checkPassword`,
        {
          existingPassword: this.existing,
          password: this.password1,
          confirmPassword: this.password2
        })
        .then(postResponse => {
          this.submitPutRequest();
        })
        .catch(error => console.log(error));
    },
    submitPutRequest () {
      this.$http.put('/openstorefront/api/v1/resource/users/currentuser/resetpassword',
        {
          existingPassword: this.existing,
          password: this.password1,
          confirmPassword: this.password2
        })
        .then(putResponse => {
          if (putResponse) {
            if (putResponse.data.errors.entry) {
              this.errorText = putResponse.data.errors.entry[0].value;
              this.errorDialog = true;
              console.log(putResponse.data.errors.entry);
            }
          }
        })
        .finally(() => {
          if (this.errorDialog === false) {
            this.confirmationDialog = true;
          }
        })
        .catch(error => console.log(error));
    }

  },
  computed: {

  }
};
</script>

<style scoped lang="scss">
</style>

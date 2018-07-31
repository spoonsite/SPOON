<template lang="html">
  <div>

    <v-layout mt-3 mx-4>
    <v-flex xs12 sm6 offset-sm3 md4 offset-md4>
      <v-form v-model="valid" @submit.prevent="submitEmail()">
        <v-text-field
          prepend-icon="vpn_key"
          name="exesting-password"
          label="Existing Password"
          type="password"
          v-model="existing"
          :rules="[rules.required]"
        ></v-text-field>
        <v-text-field
          prepend-icon="lock"
          name="password1"
          label="New Password"
          type="password"
          v-model="password1"
          :rules="[rules.required, rules.password]"
        ></v-text-field>
        <v-text-field
          prepend-icon="lock"
          name="password2"
          label="Verify New Password"
          type="password"
          v-model="password2"
          :rules="password2Rules"
        ></v-text-field>
        <v-btn :disabled="!valid" block color="accent" @click="submitUpdate()">Update Password</v-btn>
      </v-form>
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

    <LoadingOverlay v-model="isLoading"></LoadingOverlay>
  </div>
</template>

<script lang="js">
import validators from '../util/validators';
import LoadingOverlay from './subcomponents/LoadingOverlay';

export default {
  name: 'reset-password-page',
  mixins: [validators],
  components: {
    LoadingOverlay
  },
  mounted () {
  },
  data: function () {
    return {
      confirmationDialog: false,
      errorDialog: false,
      errorText: 'Error',
      isLoading: false,
      valid: false,
      existing: '',
      password1: '',
      password2: '',
      password2Rules: [
        v => !!v || 'Password verification is required',
        v => this.password1 === this.password2 || 'Passwords must match'
      ]
    };
  },
  methods: {
    submitUpdate () {
      this.isLoading = true;
      this.$http.post(`/openstorefront/api/v1/service/security/checkPassword`,
        {
          existingPassword: this.existing,
          password: this.password1,
          confirmPassword: this.password2
        })
        .then(postResponse => {
          this.submitPutRequest();
        })
        .finally(() => {
          this.isLoading = false;
        });
    },
    submitPutRequest () {
      this.isLoading = true;
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
            }
          }
        })
        .finally(() => {
          this.isLoading = false;
          if (this.errorDialog === false) {
            this.confirmationDialog = true;
          }
        });
    }

  },
  computed: {

  }
};
</script>

<style scoped lang="scss">
</style>

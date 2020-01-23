<template lang="html">
  <div>
    <v-form>
      <h2>Change Password</h2>
      <ul>
        <li>At least 1 Capital Letter</li>
        <li>At least 1 Number</li>
        <li>At least {{ $store.state.securityPolicy.minPasswordLength }} Characters</li>
        <li>At least 1 Special Character (?!@#$%*)</li>
      </ul>
      <v-text-field
        required
        label="Existing Password*"
        v-model="existingPassword"
        :append-icon="showExisting ? 'fas fa-eye' : 'fas fa-eye-slash'"
        :type="showExisting ? 'text' : 'password'"
        :error-messages="existingPasswordError"
        @click:append="showExisting = !showExisting"
      ></v-text-field>
      <v-text-field
        required
        label="New Password*"
        v-model="newPassword"
        :append-icon="showNew ? 'fas fa-eye' : 'fas fa-eye-slash'"
        :type="showNew ? 'text' : 'password'"
        :error-messages="errorMessages"
        @click:append="showNew = !showNew"
      ></v-text-field>
      <v-text-field
        required
        label="Confirm New Password*"
        v-model="confirmPassword"
        :append-icon="showConfirmation ? 'fas fa-eye' : 'fas fa-eye-slash'"
        :type="showConfirmation ? 'text' : 'password'"
        @click:append="showConfirmation = !showConfirmation"
      ></v-text-field>
      <v-flex xs12 pt-0 pb-0>
        <v-btn
          block
          style="margin-left: 0px;"
          color="success"
          :disabled="existingPassword == '' || newPassword == '' || confirmPassword == ''"
          @click="submitPassword()"
        >
          Update Password
        </v-btn>
      </v-flex>
    </v-form>
  </div>
</template>

<script lang="js">

export default {
  name: 'change-password-page',
  data() {
    return {
      existingPassword: '',
      newPassword: '',
      confirmPassword: '',
      errorMessages: [],
      existingPasswordError: [],
      showExisting: false,
      showNew: false,
      showConfirmation: false
    }
  },
  methods: {
    submitPassword() {
      this.errorMessages = []
      if (this.newPassword !== this.confirmPassword) {
        this.errorMessages.push("The new password and confirmation don't match.")
      } else {
        this.checkPasswordChange()
      }
    },
    checkPasswordChange() {
      let data = {
        password: this.newPassword,
        existingPassword: this.existingPassword
      }
      this.$http.post(`/openstorefront/api/v1/service/security/checkPassword`, data)
        .then(response => {
          var validation = response.data
          if (validation.success === false) {
            this.errorMessages.push(validation.errors.entry[0].value)
          } else {
            this.resetUserPassword()
            this.confirmPassword = ''
            this.existingPassword = ''
            this.newPassword = ''
          }
        })
        .catch(e => this.$toasted.error('There was a problem submitting your password change request.'))
    },
    resetUserPassword() {
      let data = {
        password: this.newPassword,
        existingPassword: this.existingPassword
      }
      this.$http.put(`/openstorefront/api/v1/resource/users/currentuser/resetpassword`, data)
        .then(response => {
          var validation = response.data
          if (validation.success === false) {
            this.existingPasswordError.push(validation.errors.entry[0].value)
          } else {
            this.$toasted.show('Password changed successfully.')
          }
        })
        .catch(e => this.$toasted.error('There was a problem submitting your password change request.'))
    }
  },
  watch: {
    existingPassword: function() {
      this.existingPasswordError = []
    }
  }
}
</script>

<style scoped lang="scss"></style>

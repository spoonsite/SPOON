<template>
  <div>
    <v-layout ma-3>
      <v-flex xs12 md4 offset-md4 sm6 offset-sm3>
        <v-card class="elevation-5">
          <v-toolbar color="primary" dark dense>
            <v-toolbar-title>Contact Us</v-toolbar-title>
          </v-toolbar>
          <v-form ref="form" v-model="valid" lazy-validation>
            <v-card-text>

              <v-select
                label="Choose Type"
                :items="contactTypeOptions"
                :rules="contactTypeRules"
                v-model="contactType"
                required
              ></v-select>

              <v-text-field
                v-model="subject"
                :rules="subjectRules"
                :counter="255"
                label="Subject"
                required
              ></v-text-field>

              <v-text-field
                v-model="description"
                :rules="descriptionRules"
                :counter="4096"
                label="Description"
                textarea
                required
              ></v-text-field>

              <v-card-title>
                <h2>Contact Information</h2>
              </v-card-title>

              <v-text-field
                v-model="name"
                :rules="nameRules"
                :counter="80"
                label="Name"
                required
              ></v-text-field>

              <v-text-field
                v-model="email"
                :rules="[rules.required, rules.email]"
                label="Email"
                required
               ></v-text-field>

              <v-text-field
                v-model="phone"
                :rules="phoneRules"
                :counter="80"
                label="Phone"
               ></v-text-field>

              <v-text-field
                v-model="organization"
                :rules="organizationRules"
                :counter="120"
                label="Organization"
               ></v-text-field>

            </v-card-text>
            <v-card-actions>
              <v-container fluid grid-list-x>
                <v-layout row >
                  <v-flex>
                    <v-btn
                      block
                      color="secondary"
                      style="margin-bottom:2em;"
                      :disabled="!valid"
                      @click="submit"
                      :loading="isLoading"
                      >Submit</v-btn>
                  </v-flex>
                </v-layout>
                <v-layout row justify-center>
                  <v-flex>
                    <v-btn
                      block
                      color="secondary"
                      @click="cancel"
                      >Cancel</v-btn>
                  </v-flex>
                </v-layout>
              </v-container>
            </v-card-actions>
          </v-form>
        </v-card>
      </v-flex>
    </v-layout>

    <v-dialog v-model="confirmationDialog" max-width="300px">
      <v-card title>
        <v-card-text>Feedback has been submitted.</v-card-text>
        <v-card-actions>
          <v-btn @click="$router.push('/')"><v-icon>fas fa-return</v-icon>Return to Homepage</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

  </div>
</template>

<script>
import validators from '@/util/validators'

export default {
  name: 'ContactUsPage',
  mixins: [validators],
  data: () => ({
    valid: true,
    confirmationDialog: false,
    isLoading: false,
    contactType: null,
    contactTypeRules: [
      v => !!v || 'Type is required'
    ],
    contactTypeOptions: [
      'Report Issue',
      'Help',
      'Improvement',
      'New Feature'
    ],
    subject: '',
    subjectRules: [
      v => !!v || 'Subject is required',
      v => (v && v.length <= 255) || 'Maximum length for this field is 255'
    ],
    description: '',
    descriptionRules: [
      v => !!v || 'Description is required',
      v => (v && v.length <= 4096) || 'Maximum length for this field is 4096'
    ],
    name: '',
    nameRules: [
      v => !!v || 'Name is required',
      v => v.length <= 80 || 'Name must be less than 80 characters'
    ],
    email: '',
    phone: '',
    phoneRules: [
      v => (v || ' ').length <= 80 || 'Maximum length for this field is 80'
    ],
    organization: '',
    organizationRules: [
      v => (v || ' ').length <= 120 || 'Maximum length for this field is 120'
    ]
  }),
  methods: {
    submit () {
      if (this.$refs.form.validate()) {
        // Native form submission is not yet supported
        this.isLoading = true
        this.$http.post('/openstorefront/api/v1/resource/feedbacktickets', {
          summary: this.subject,
          description: this.description,
          fullname: this.name,
          email: this.email,
          organization: this.organization,
          phone: this.phone,
          ticketType: this.contactType,
          webInformation: {
            location: window.location.href,
            userAgent: navigator.userAgent,
            referrer: navigator.referrer,
            screenResolution: 'Height: ' + window.innerHeight + ', Width:' + window.innerWidth
          }
        })
          .then(response => {
            this.isLoading = false
            this.$refs.form.reset()
            this.confirmationDialog = true
          })
          .catch(error => {
            console.error(error)
            this.toasted.error('There was a problem sending your feedback.')
          })
      }
    },
    cancel () {
      this.$refs.form.reset()
    }
  }
}
</script>

<style scoped>
</style>

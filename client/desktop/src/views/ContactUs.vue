<template lang="html">
  <div>
    <v-layout my-5 mx-3>
      <v-form class="centeralign" ref="form" lazy-validation>
        <v-container grid-list-xl text-xs-left>
          <v-layout row wrap>
            <v-flex xs12 pt-0 pb-0>
              <v-select
                label="Choose Type"
                :items="contactTypeOptions"
                :rules="contactTypeRules"
                v-model="contactType"
                required
              ></v-select>
            </v-flex>

            <v-flex xs12 pt-0 pb-0>
              <v-text-field
                v-model="subject"
                :rules="subjectRules"
                :counter="255"
                label="Subject"
                required
              ></v-text-field>
            </v-flex>

            <v-flex xs12 md6 pt-0 pb-0>
              <v-textarea
                v-model="description"
                :rules="descriptionRules"
                :counter="4096"
                label="Description"
                outline
                required
              ></v-textarea>
            </v-flex>

            <v-flex xs12 md6 pt-0 pb-0>
              <h2>Contact Information</h2>
              <p>
                <strong>Name: </strong
                >{{ this.$store.state.currentUser.firstName + this.$store.state.currentUser.lastName }}
              </p>
              <p><strong>Email: </strong>{{ this.$store.state.currentUser.email }}</p>
              <p><strong>Phone: </strong>{{ this.$store.state.currentUser.phone }}</p>
              <p><strong>Organization: </strong>{{ this.$store.state.currentUser.organization }}</p>
            </v-flex>

            <v-flex xs12 pt-0 pb-0>
              <v-btn
                block
                color="success"
                style="margin-bottom:2em;"
                :disabled="valid"
                @click="submit"
                :loading="isLoading"
              >
                Submit
              </v-btn>
            </v-flex>
            <v-flex xs12 pt-0 pb-0>
              <v-btn block color="accent" :disabled="!cancelable" @click="cancel">
                Clear
              </v-btn>
            </v-flex>
          </v-layout>
        </v-container>
      </v-form>
    </v-layout>

    <v-dialog v-model="confirmationDialog" max-width="25em">
      <v-card>
        <ModalTitle title="Submitted Feedback" @close="confirmationDialog = false" />
        <v-card-text>Feedback has been submitted.</v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn @click="$router.push('/')">Return to Homepage</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script lang="js">
import ModalTitle from '@/components/ModalTitle'

export default {
  name: 'contact-page',
  components: {
    ModalTitle
  },
  mounted() {
    if (this.$route.params.ticket) {
      this.ticket = this.$route.params.ticket
      this.description = 'Error Description: ' + this.ticket
      this.contactType = 'Report Issue'
    }
  },
  data: () => ({
    ticket: '',
    confirmationDialog: false,
    isLoading: false,
    contactType: '',
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
    ]
  }),
  methods: {
    submit() {
      if (this.$refs.form.validate()) {
        // Native form submission is not yet supported
        this.isLoading = true
        this.$http
          .post('/openstorefront/api/v1/resource/feedbacktickets', {
            summary: this.subject,
            description: this.description,
            fullname: this.$store.state.currentUser.firstName + this.$store.state.currentUser.lastName,
            email: this.$store.state.currentUser.email,
            organization: this.$store.state.currentUser.organization,
            phone: this.$store.state.currentUser.phone,
            ticketType: this.contactType,
            webInformation: {
              location: window.location.href,
              userAgent: navigator.userAgent,
              referrer: navigator.referrer,
              screenResolution: 'Height: ' + window.innerHeight + ', Width:' + window.innerWidth
            }
          })
          .then(response => {
            this.cancel()
            this.confirmationDialog = true
            this.isLoading = false
          })
      }
    },
    cancel() {
      this.contactType = ''
      this.subject = ''
      this.description = ''
    }
  },
  computed: {
    cancelable() {
      return this.contactType !== '' ||
        this.subject !== '' ||
        this.description !== ''
    },
    valid() {
      return !(this.contactType !== '' && this.subject !== '' && this.description !== '')
    }
  }
}
</script>

<style scoped lang="scss">
.centeralign {
  margin: 0 auto;
}
</style>

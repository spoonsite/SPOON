<template lang="html">
  <div>
    <v-layout>
      <v-form class="centeralign" ref="form" v-model="valid" lazy-validation>
        <v-container grid-list-xl text-xs-left>
          <v-layout row wrap>
            <v-flex xs12 sm6 pt-0 pb-0>
              <v-select
                label="Choose Type"
                :items="contactTypeOptions"
                :rules="contactTypeRules"
                v-model="contactType"
                required
              ></v-select>
            </v-flex>

            <v-flex xs12 sm6 pt-0 pb-0>
              <v-text-field
                v-model="subject"
                :rules="subjectRules"
                :counter="255"
                label="Subject"
                required
              ></v-text-field>
            </v-flex>

            <v-flex  xs12 sm6 pt-0 pb-0>
              <v-text-field
                v-model="description"
                :rules="descriptionRules"
                :counter="4096"
                label="Description"
                textarea
                required
              ></v-text-field>
            </v-flex>

            <v-flex  xs12 sm6 pt-0 pb-0>
              <h2>Contact Information</h2>
              <p>
                <strong>Name: </strong>{{this.$store.state.currentUser.firstName + this.$store.state.currentUser.lastName}}
              </p>
              <p>
                <strong>Email: </strong>{{this.$store.state.currentUser.email}}
              </p>
              <p>
                <strong>Phone: </strong>{{this.$store.state.currentUser.phone}}
              </p>
              <p>
                <strong>Organization: </strong>{{this.$store.state.currentUser.organization}}
              </p>
            </v-flex>

            <v-flex xs12 pt-0 pb-0>
              <v-btn
                block
                color="accent"
                style="margin-bottom:2em;"
                :disabled="!valid"
                @click="submit"
                >Submit</v-btn>
            </v-flex>
            <v-flex xs12 pt-0 pb-0>
              <v-btn
                block
                color="accent"
                @click="cancel"
                >Cancel</v-btn>
            </v-flex>
          </v-layout>
        </v-container>
      </v-form>
    </v-layout>

    <v-dialog v-model="confirmationDialog" max-width="300px">
      <v-card title>
        <v-card-text>Feedback has been submitted.</v-card-text>
        <v-card-actions >
          <v-btn @click="$router.push('/')">Return to Homepage</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

  </div>
</template>

<script lang="js">
export default {
  name: 'contact-page',
  props: [],
  mounted () {
    if (this.$route.params.ticket) {
      this.ticket = this.$route.params.ticket;
      this.description = 'Error Description: ' + this.ticket;
      this.contactType = 'Report Issue';
    }
  },
  data: () => ({
    ticket: '',
    valid: true,
    confirmationDialog: false,
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
    ]
  }),
  methods: {
    submit () {
      if (this.$refs.form.validate()) {
        // Native form submission is not yet supported
        this.$http.post('/openstorefront/api/v1/resource/feedbacktickets', {
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
            this.$refs.form.reset();
            this.confirmationDialog = true;
          });
      }
    },
    cancel () {
      this.$refs.form.reset();
    }

  },
  computed: {

  }
};
</script>

<style scoped lang="scss">
.centeralign {
  margin-right: auto;
  margin-left: auto;
}
</style>

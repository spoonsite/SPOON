<template lang="html">
  <div>
    <v-layout mt-3 mx-3>
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

              <p>
                <v-text><strong>Name: </strong>{{this.$store.state.currentUser.firstName + this.$store.state.currentUser.lastName}}</v-text>
              </p>

              <p>
                <v-text><strong>Email: </strong>{{this.$store.state.currentUser.email}}</v-text>
              </p>

              <p>
                <v-text><strong>Phone: </strong>{{this.$store.state.currentUser.phone}}</v-text>
              </p>

              <p>
                <v-text><strong>Organization: </strong>{{this.$store.state.currentUser.organization}}</v-text>
              </p>

            </v-card-text>
            <v-card-actions>
              <v-container fluid grid-list-x>
                <v-layout row >
                  <v-flex>
                    <v-btn
                      block
                      color="accent"
                      style="margin-bottom:2em;"
                      :disabled="!valid"
                      @click="submit"
                      >Submit</v-btn>
                  </v-flex>
                </v-layout>
                <v-layout row justify-center>
                  <v-flex>
                    <v-btn
                      block
                      color="accent"
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
          })
          .catch(error => console.log(error));
        this.$refs.form.reset();
        this.confirmationDialog = true;
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
  .contact-page {

  }
</style>

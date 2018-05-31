<template>
  <div>
    <v-layout mt-3>
      <v-flex xs12 sm6 offset-sm3>
        <v-card class="elevation-5">
          <v-toolbar color="primary" dark dense>
            <v-toolbar-title>Contact Us</v-toolbar-title>
          </v-toolbar>
          <v-form ref="form" v-model="valid" lazy-validation>
            <v-card-text>
              <v-select
                label="Choose Type"
                :items="contactTypeOptions"
                v-model="contactType"
              ></v-select>
            </v-card-text>
            <v-card-text>

                <v-text-field
                  v-model="subject"
                  :rules="subjectRules"
                  :counter="200"
                  label="Subject"
                  required
                  ></v-text-field>

            </v-card-text>
            <v-card-text>

                <v-text-field
                  v-model="description"
                  :rules="descriptionRules"
                  :counter="4096"
                  label="Description"
                  required
                  ></v-text-field>

            </v-card-text>
            <v-container dark class="grey lighten-2">
              <v-card-text>

                <v-text-field
                  v-model="name"
                  :rules="nameRules"
                  :counter="40"
                  label="Name"
                  required
                  ></v-text-field>

              </v-card-text>
              <v-card-text>

                <v-text-field
                  v-model="email"
                  :rules="emailRules"
                  label="Email"
                  required
                  ></v-text-field>

              </v-card-text>
              <v-card-text>

                <v-text-field
                  v-model="phone"
                  label="Phone"
                  ></v-text-field>

              </v-card-text>
              <v-card-text>

                <v-text-field
                  v-model="organization"
                  :rules="y=organizationRules"
                  :counter="200"
                  label="Organization"
                  ></v-text-field>

              </v-card-text>
            </v-container>
            <v-card-actions>
              <v-container fluid grid-list-x>
                <v-layout row center>
                  <v-flex>
                    <v-btn
                      color="accent"
                      style="margin-bottom:2em;"
                      :disabled="!valid"
                      @click="submit"
                      >Submit</v-btn>
                  </v-flex>
                  <v-flex>
                    <v-btn
                      color="accent"
                      style="margin-bottom:2em;"
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
  </div>
</template>

<script>

export default {
  name: 'ContactUsPage',
  components: {
  },
  computed: {
  },
  data: () => ({
    valid: true,
    contactType: null,
    contactTypeRules: [
      [v => !!v || 'Type is required']
    ],
    contactTypeOptions: [
      { text: 'Report Issue' },
      { text: 'Help' },
      { text: 'Improvement' },
      { text: 'New Feature' }
    ],
    subject: '',
    subjectRules: [
      v => !!v || 'Subject is required',
      v => (v && v.length <= 200) || 'Maximum length for this field is 200'
    ],
    description: '',
    descriptionRules: [
      v => !!v || 'Description is required',
      v => (v && v.length <= 4096) || 'Maximum length for this field is 4096'
    ],
    name: '',
    nameRules: [
      v => !!v || 'Name is required',
      v => v.length <= 10 || 'Name must be less than 10 characters'
    ],
    email: '',
    emailRules: [
      v => !!v || 'E-mail is required',
      v =>
        /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/.test(v) ||
        'E-mail must be valid'
    ],
    phone: '',
    organization: '',
    organizationRules: [
      v => (v.length <= 200) || 'Maximum length for this field is 200'
    ],

  }),
  methods: {
    submit() {
      if (this.$refs.form.validate()) {
          // Native form submission is not yet supported
          axios.post('/api/submit', {
            contactType: this.select,
            subject: this.subject,
            description: this.description,
            name: this.name,
            email: this.email,
            phone: this.phone,
            organization: this.organization
          })
        }
    },
    cancel () {
      this.$refs.form.reset();
    }
  }
}
</script>

<style scoped>
.login-comp {
  width: 50%;
  height: 100%;
  margin: 2em;
}
.signup-comp {
  width: 50%;
  height: 100%;
  margin: 2em;
}
</style>

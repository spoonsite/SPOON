<template>
  <div>
    <h1 class="text-center">Submission Form</h1>
    <div class="text-center" style="color: red;">
      <h2>Caution!</h2>
      <p>{{ this.$store.state.branding.userInputWarning }}</p>
    </div>
    <v-form v-model="isFormValid" ref="submissionForm" style="width: 80%;" class="mx-auto">
      <fieldset>
        <legend>Entry Details*</legend>
        <v-text-field
          label="Entry Title*"
          v-model="entryDetails.entryTitle"
          required
          :rules="[rules.required]"
          class="mx-4"
          autofocus
        />
        <v-text-field
          label="Entry Type*"
          v-model="entryDetails.entryType"
          required
          :rules="[rules.required]"
          class="mx-4"
        />
        <v-text-field
          label="Organization*"
          v-model="entryDetails.organization"
          required
          :rules="[rules.required]"
          class="mx-4"
        />
        <v-text-field
          label="Security Marking*"
          v-model="entryDetails.securityMarking"
          required
          :rules="[rules.required]"
          class="mx-4"
        />
      </fieldset>
      <fieldset>
        <legend>Primary Point of Contact*</legend>
        <v-text-field
          label="First Name*"
          v-model="primaryPOC.firstName"
          required
          :rules="[rules.required]"
          class="mx-4"
          autofocus
        />
        <v-text-field
          label="Last Name*"
          v-model="primaryPOC.lastName"
          required
          :rules="[rules.required]"
          class="mx-4"
        />
        <v-text-field label="Email*" v-model="primaryPOC.email" required :rules="[rules.required]" class="mx-4" />
        <v-text-field label="Phone*" v-model="primaryPOC.phone" required :rules="[rules.required]" class="mx-4" />
      </fieldset>
      <fieldset>
        <legend>Image Upload</legend>
        <v-btn color="grey lighten-2" class="ma-4" @click="addImage"><v-icon left>mdi-plus</v-icon>Add image</v-btn>
        <v-row v-for="(item, index) in images" :key="index" class="mx-4">
          <v-col cols="12" md="1">
            <v-img :src="item.img" alt="No preview available" max-height="80px" max-width="80px" contain />
          </v-col>
          <v-col cols="12" md="5">
            <v-file-input
              v-model="item.file"
              label="Upload Image*"
              required
              :rules="[rules.required]"
              @change="imageChange(index)"
            />
          </v-col>
          <v-col cols="12" md="5">
            <v-text-field v-model="item.caption" label="Image Caption*" required :rules="[rules.required]" />
          </v-col>
          <v-col cols="12" md="1">
            <v-btn @click="removeImage(index)"><v-icon left>mdi-delete</v-icon>Delete</v-btn>
          </v-col>
        </v-row>
      </fieldset>
      <fieldset>
        <legend>Description*</legend>
        <v-textarea label="Description*" required :rules="[rules.required]" class="mx-4" />
      </fieldset>
      <fieldset>
        <legend>Attributes</legend>
        <h3>Attributes here</h3>
        <div class="mx-4 mt-4">
          <strong>Request New Attribute</strong>
          <p class="mb-0">Please describe the attribute you would like to have added.</p>
          <p class="mb-0">
            Include the value for your entry, a brief description, and how your part is defined by the attribute.
          </p>
          <v-textarea label="Request New Attribute (opt.)" />
        </div>
      </fieldset>
      <fieldset>
        <legend>Resources</legend>
        <h3>Resources here</h3>
        <v-text-field label="Media Description" />
        <v-file-input label="Add file" />
      </fieldset>
      <fieldset>
        <legend>Tags</legend>
        <h3>Tags here</h3>
      </fieldset>
      <fieldset>
        <legend>Contacts</legend>
        <h3>Contacts here here</h3>
      </fieldset>
      <v-btn color="primary">
        Save and close
      </v-btn>
      <v-btn :disabled="!isFormValid" color="success" class="mr-4" @click="submit">
        Submit
      </v-btn>
    </v-form>
  </div>
</template>

<script>
export default {
  name: 'SubmissionForm',
  mounted() {},
  data: () => ({
    isFormValid: false,
    entryDetails: {
      entryTitle: '',
      entryType: '',
      organization: '',
      securityMarking: ''
    },
    primaryPOC: {
      firstName: '',
      lastName: '',
      email: '',
      phone: ''
    },
    images: [],
    description: '',
    attributes: {
      required: [],
      optional: []
    },

    rules: {
      required: value => !!value || 'Required'
    }
  }),
  methods: {
    submit() {
      if (this.$refs.submissionForm.validate()) {
        console.log('Form is valid')
      } else {
        console.log('Form is invalid')
      }
    },
    addImage() {
      this.images.push({ file: null, caption: '', img: '' })
    },
    removeImage(index) {
      this.images.splice(index, 1)
    },
    imageChange(index) {
      let e = this.images[index]
      let reader = new FileReader()
      reader.onloadend = function() {
        e.img = reader.result
      }
      if (e.file) {
        reader.readAsDataURL(e.file)
      } else {
        e.img = ''
      }
    }
  },
  watch: {
    entryType: function(oldVal, newVal) {
      console.log(oldVal)
      console.log(newVal)
    }
  }
}
</script>

<style>
fieldset {
  border: 0px;
  background-color: hsl(0, 0%, 90%);
  border-radius: 10px;
  margin: 2em 0;
}
legend {
  margin-left: 1em;
}
</style>

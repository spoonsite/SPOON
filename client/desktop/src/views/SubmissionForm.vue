<template>
  <div>
    <h1 class="text-center">Submission Form</h1>
    <div class="text-center" style="color: red;">
      <h2>Caution!</h2>
      <p>{{ this.$store.state.branding.userInputWarning }}</p>
    </div>
    <v-form v-model="isFormValid" ref="submissionForm" style="width: 80%;" class="mx-auto">
      <fieldset>
        <legend>Title</legend>
        <v-text-field label="Entry Title*" required :rules="[rules.required]" class="mx-4" autofocus />
        <v-text-field label="Entry Title*" required :rules="[rules.required]" class="mx-4" />
        <v-text-field label="Organization*" required :rules="[rules.required]" class="mx-4" />
      </fieldset>
      <fieldset>
        <legend>Image Upload</legend>
        <v-btn color="grey lighten-2" class="ma-2" @click="addImage"><v-icon left>mdi-plus</v-icon>Add image</v-btn>
        <div v-for="(item, index) in images" :key="index">
          <v-file-input v-model="item.file" label="Upload Image" />
          <v-text-field v-model="item.caption" label="Image Caption" />
          <v-btn icon><v-icon>mdi-delete</v-icon></v-btn>
        </div>
      </fieldset>
      <fieldset>
        <legend>Description</legend>
        <v-textarea label="Description*" required :rules="[rules.required]" class="mx-4" />
      </fieldset>
      <fieldset>
        <legend>Attributes</legend>
        <h3>Attributes here</h3>
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
    rules: {
      required: value => !!value || 'Required'
    },
    images: []
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
      this.images.push({ file: null, caption: '' })
    }
  },
  watch: {
    images: function() {
      console.log(this.images)
    }
  }
}
</script>

<style>
fieldset {
  border: 0px;
  /* background-color: hsl(0, 0%, 90%); */
  border-radius: 10px;
  margin: 2em 0;
}
legend {
  margin-left: 1em;
}
</style>

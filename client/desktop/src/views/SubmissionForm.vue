<template>
  <div>
    <h1 class="text-center">Submission Form</h1>
    <v-form v-model="isFormValid" ref="submissionForm" style="width: 80%;" class="mx-auto">
      <fieldset>
        <legend>Title</legend>
        <v-text-field label="Entry Title*" required :rules="[rules.required]" class="mx-4" autofocus />
        <v-text-field label="Entry Title*" required :rules="[rules.required]" class="mx-4" />
        <v-text-field label="Organization*" required :rules="[rules.required]" class="mx-4" />
      </fieldset>
      <fieldset>
        <legend>Image Upload</legend>
        <v-file-input label="Upload Image" />
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
  mounted() {
    this.$ref.entryTitle.focus()
  },
  data: () => ({
    isFormValid: false,
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
    }
  }
}
</script>

<style></style>

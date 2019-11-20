<template>
  <v-dialog
    v-model="value"
    max-width="300px"
    persistent
    >
    <v-card>
      <v-card-title>
        <h2 v-if="this.$store.state.branding">Site Disclaimer</h2>
      </v-card-title>
      <v-card-text v-html="this.$store.state.branding.disclaimerMessage"></v-card-text>
      <v-card-actions>
        <v-btn @click="$emit('input', false)">Close</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  name: "DisclaimerModal",
  props: {
    value: Boolean
  }
}
</script>

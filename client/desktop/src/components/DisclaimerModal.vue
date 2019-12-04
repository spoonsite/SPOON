<template>
  <v-dialog
    :value="value"
    @input="close"
    max-width='75em'
  >
    <v-card>
      <ModalTitle v-if='this.$store.state.branding' title='Site Disclaimer' @close='close' />
      <v-card-text v-html='this.$store.state.branding.disclaimerMessage'></v-card-text>
      <v-card-actions>
        <v-spacer/>
        <v-btn @click='close'>Close</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import ModalTitle from '@/components/ModalTitle'

export default {
  name: 'DisclaimerModal',
  props: ["value"],
  components: {
    ModalTitle
  },
  methods: {
    close () {
      this.$emit('close')
    }
  },
}
</script>

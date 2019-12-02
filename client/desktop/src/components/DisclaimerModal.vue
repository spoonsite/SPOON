<template>
  <v-dialog v-model='value' max-width='300px' persistent>
    <v-card>
      <ModalTitle v-if='this.$store.state.branding' title='Site Disclaimer' @close='close' />
      <v-card-text v-html='this.$store.state.branding.disclaimerMessage'></v-card-text>
      <v-card-actions>
        <v-btn @click='close'>Close</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import ModalTitle from '@/components/ModalTitle.vue'

export default {
  name: 'DisclaimerModal',
  props: {
    value: Boolean
  },
  components: {
    ModalTitle
  },
  methods: {
    close () {
      this.$emit('input', false)
    }
  }
}
</script>

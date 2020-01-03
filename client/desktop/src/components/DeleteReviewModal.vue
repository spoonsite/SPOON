<template>
  <v-dialog :value="value" @input="close" width="25em">
    <v-card>
      <ModalTitle title="Confirm Review Deletion" @close="close" />
      <v-card-actions>
        <v-spacer />
        <v-btn color="warning" @click="deleteReviewConfirmation()">Delete</v-btn>
        <v-btn @click="close">Cancel</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import ModalTitle from '@/components/ModalTitle'

export default {
  name: 'ReviewsModal',
  props: {
    value: {
      type: Boolean,
      default: false
    },
    review: {}
  },
  components: {
    ModalTitle
  },
  methods: {
    close() {
      this.$emit('close')
    },
    deleteReviewConfirmation() {
      this.$http
        .delete(
          `/openstorefront/api/v1/resource/components/${this.review.componentId}/reviews/${this.review.editReviewId}`
        )
        .then(response => {
          this.$toasted.show('Review Deleted')
          this.close()
        })
    }
  }
}
</script>

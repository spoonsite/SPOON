<template lang="html">
  <div>
    <h2 class="text-center">Reviews</h2>
    <v-btn class="ma-4" @click="getUserReviews()">Refresh</v-btn>
    <v-data-table :headers="tableHeaders" :items="reviewsData" class="elevation-1" :loading="isLoading">
      <template v-slot:item.activeStatus="{ item }">
        <div v-if="item.activeStatus === 'A'">Active</div>
        <div v-else-if="item.activeStatus === 'P'">Pending</div>
        <div v-else>{{ item.activeStatus }}</div>
      </template>
      <template v-slot:item.comment="{ item }">
        <div v-html="item.comment" />
      </template>
      <template v-slot:item.updateDate="{ item }">
        {{ item.updateDate | formatDate('MM/dd/yyyy') }}
      </template>
      <template v-slot:item.actions="{ item }">
        <v-btn icon class="" @click.stop="setUpEditDialog(item)"><v-icon>fas fa-pencil-alt</v-icon></v-btn>
        <v-btn icon class="" @click.stop="setUpDeleteDialog(item)"><v-icon>fas fa-trash</v-icon></v-btn>
      </template>
      <template v-slot:no-data>
        No entries have been reviewed...
      </template>
    </v-data-table>

    <ReviewModal
      v-model="editReviewDialog"
      @close="
        editReviewDialog = false
        getUserReviews()
      "
      :componentId="currentReview.componentId"
      title="Edit A Review"
      :editReview="currentReview"
    >
    </ReviewModal>
    <DeleteReviewModal
      v-model="deleteReviewDialog"
      @close="
        deleteReviewDialog = false
        getUserReviews()
      "
      :review="currentReview"
    ></DeleteReviewModal>
  </div>
</template>

<script lang="js">
import ReviewModal from '@/components/ReviewModal'
import DeleteReviewModal from '@/components/DeleteReviewModal'

export default {
  name: 'reviews-page',
  components: {
    // StarRating,
    ReviewModal,
    DeleteReviewModal
  },
  mounted() {
    if (this.$store.state.currentUser.username) {
      this.username = this.$store.state.currentUser.username
      this.getUserReviews()
    } else {
      this.$store.watch(
        (state, getters) => state.currentUser,
        (newValue, oldValue) => {
          this.username = this.$store.state.currentUser.username
          this.getUserReviews()
        }
      )
    }
  },
  data() {
    return {
      isLoading: false,
      tableHeaders: [
        { text: 'Entry', value: 'name' },
        { text: 'Title', value: 'title' },
        { text: 'Rating', value: 'rating' },
        { text: 'Status', value: 'activeStatus' },
        { text: 'Comment', value: 'comment', sortable: false },
        { text: 'Update Date', value: 'updateDate' },
        { text: 'Actions', value: 'actions', sortable: false }
      ],
      reviewsData: [],
      username: '',
      editReviewDialog: false,
      deleteReviewDialog: false,
      currentReview: {
        title: '',
        rating: 0,
        lastUsed: '',
        timeUsed: '',
        pros: [],
        cons: [],
        comment: '',
        editReviewId: '',
        componentId: ''
      }
    }
  },
  methods: {
    getUserReviews() {
      this.isLoading = true
      this.$http.get(`/openstorefront/api/v1/resource/components/reviews/${this.username}`)
        .then(response => {
          this.isLoading = false
          this.reviewsData = []
          this.reviewsData = response.data
        })
    },
    setUpEditDialog(tableReview) {
      this.currentReview = tableReview
      this.editReviewDialog = true
    },
    setUpDeleteDialog(tableReview) {
      this.currentReview = tableReview
      this.currentReview.editReviewId = tableReview.reviewId
      this.deleteReviewDialog = true
    }
  }
}
</script>

<style scoped lang="scss"></style>

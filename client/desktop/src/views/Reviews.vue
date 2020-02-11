<template lang="html">
  <div>
    <h2 class="text-center">Reviews</h2>
    <v-btn class="ma-4" @click="getUserReviews()">Refresh</v-btn>
    <v-data-table
      :headers="tableHeaders"
      :items="reviewsDisplay"
      class="elevation-1"
      :loading="isLoading"
      :hide-default-footer="isLoading || reviewsDisplay.length === 0"
    >
      <!-- <td>{{ props.item.entry }}</td>
            <td>{{ props.item.title }}</td>

            <td>
              <star-rating
                :rating="props.item.rating"
                :read-only="true"
                :show-rating="false"
                :increment="0.01"
                :star-size="25"
              ></star-rating>
            </td>
            <td>{{ props.item.status }}</td>

            <td style="word-wrap: break-word" v-if="props.item.comment.length < 200">{{ props.item.comment }}</td>
            <td style="word-wrap: break-word" v-else>{{ props.item.comment.substring(0, 200) + '...' }}</td>
            <td>{{ props.item.updateDate }}</td> -->
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
    <ReviewModal v-model="editReviewDialog" @close="editReviewDialog = false" :review="currentReview"></ReviewModal>
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
// import StarRating from 'vue-star-rating'
import ReviewModal from '@/components/ReviewModal'
import DeleteReviewModal from '@/components/DeleteReviewModal'
import format from 'date-fns/format'

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
        { text: 'Entry', value: 'entry' },
        { text: 'Title', value: 'title' },
        { text: 'Rating', value: 'rating' },
        { text: 'Status', value: 'status' },
        { text: 'Comment', value: 'comment', sortable: false },
        { text: 'Update Date', value: 'updateDate' },
        { text: 'Actions', value: 'actions', sortable: false }
      ],
      reviewsData: [],
      reviewsDisplay: [],
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
          this.reviewsDisplay = []
          this.reviewsData = response.data
          this.setUpTableArray()
        })
    },
    removeCommentHtml(review) {
      var comment = review.comment
      var tmp = document.createElement('div')
      tmp.innerHTML = comment
      comment = tmp.innerText
      return comment
    },
    changeDateFormat(review) {
      var updateDate = new Date(review.updateDate)
      return updateDate
    },
    determineActiveOrPending(review) {
      if (review.activeStatus === 'A') {
        return 'Approved'
      } else {
        return 'Pending'
      }
    },
    setUpTableArray() {
      for (var review in this.reviewsData) {
        this.reviewsDisplay.push({
          entry: this.reviewsData[review].name,
          title: this.reviewsData[review].title,
          rating: this.reviewsData[review].rating,
          status: this.determineActiveOrPending(this.reviewsData[review]),
          comment: this.removeCommentHtml(this.reviewsData[review]),
          updateDate: this.reviewsData[review].updateDate,
          pros: this.reviewsData[review].pros,
          cons: this.reviewsData[review].cons,
          lastUsed: this.reviewsData[review].lastUsed,
          timeUsed: this.reviewsData[review].userTimeDescription,
          editReviewId: this.reviewsData[review].reviewId,
          componentId: this.reviewsData[review].componentId
        })
      }
    },
    setUpEditDialog(tableReview) {
      this.getCurrentItemData(tableReview)
      this.editReviewDialog = true
    },
    setUpDeleteDialog(tableReview) {
      this.getCurrentItemData(tableReview)
      this.deleteReviewDialog = true
    },
    getCurrentItemData(tableReview) {
      this.currentReview.title = tableReview.title
      this.currentReview.rating = tableReview.rating
      this.currentReview.lastUsed = format(new Date(tableReview.lastUsed), 'yyyy-MM-d')
      this.currentReview.timeUsed = tableReview.timeUsed
      this.currentReview.pros = tableReview.pros
      this.currentReview.cons = tableReview.cons
      this.currentReview.comment = tableReview.comment
      this.currentReview.editReviewId = tableReview.editReviewId
      this.currentReview.componentId = tableReview.componentId
    }
  }
}
</script>

<style scoped lang="scss"></style>

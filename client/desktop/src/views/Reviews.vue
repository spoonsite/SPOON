<template lang="html">
  <div>
    <v-form style="padding: 1em; padding-top: 2em;">
      <div>
        <v-btn class="top-buttons" @click="getUserReviews()">Refresh</v-btn>
      </div>
      <div style="display: flex;">
        <v-data-table
          :headers="tableHeaders"
          :items="reviewsDisplay"
          class="tableLayout"
        >
          <template slot="items" slot-scope="props">
            <td>{{ props.item.entry }}</td>
            <td>{{ props.item.title }}</td>

            <td>
              <star-rating :rating="props.item.rating" :read-only="true" :show-rating="false" :increment="0.01" :star-size="25"></star-rating>
            </td>
            <td>{{ props.item.status }}</td>

            <td style="word-wrap: break-word" v-if="props.item.comment.length<200">{{ props.item.comment }}</td>
            <td style="word-wrap: break-word" v-else>{{ props.item.comment.substring(0,200)+"..." }}</td>
            <td>{{ props.item.updateDate }}</td>
            <td>
              <v-btn small fab class="grey lighten-2" @click.stop="setUpEditDialog(props.item)"><v-icon>fas fa-pencil-alt</v-icon></v-btn>
              <v-btn small fab class="table-buttons red lighten-3"><v-icon>fas fa-trash</v-icon></v-btn>
            </td>
            <td>
            </td>
          </template>
        </v-data-table>
        <ReviewModal v-model="editReviewDialog" @close="editReviewDialog = false" :review="currentReview"></ReviewModal>
      </div>
    </v-form>
  </div>
</template>

<script lang="js">
import StarRating from 'vue-star-rating'
import ModalTitle from '@/components/ModalTitle'
import ReviewModal from '../components/ReviewModal'
import format from 'date-fns/format'
export default {
  name: 'reviews-page',
  components: {
    StarRating,
    ModalTitle,
    ReviewModal
  },
 mounted () {
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
    var tables = document.getElementsByClassName('v-datatable__actions')
    tables[0].style.bottom = 0
    tables[0].style.left = 0
    tables[0].style.right = 0
    tables[0].style.position = 'fixed'
  },
  data () {
    return {
      tableHeaders: [
        { text: 'Entry', value: 'entry' },
        { text: 'Title', value: 'title' },
        { text: 'Rating', value: 'rating' },
        { text: 'Status', value: 'status' },
        { text: 'Comment', value: 'comment' },
        { text: 'Update Date', value: 'updateDate' },
        { text: 'Actions', value: 'actions' }
      ],
      reviewsData: [],
      reviewsDisplay: [],
      username: '',
      editReviewDialog: false,
      currentReview: {
        title: '',
        rating: 0,
        lastUsed: '',
        timeUsed: '',
        pros: [],
        cons: [],
        comment: ''
      }
    }
  },
  methods: {
    getUserReviews () {
      this.$http.get(`/openstorefront/api/v1/resource/components/reviews/${this.username}`)
        .then(response => {
          this.reviewsData = []
          this.reviewsDisplay = []
          this.reviewsData = response.data
          this.setUpTableArray()
          console.log(this.reviewsData)
        })
    },
    removeCommentHtml (review) {
      var comment = review.comment
      var tmp = document.createElement('div')
      tmp.innerHTML = comment
      comment = tmp.innerText
      return comment
    },
    changeDateFormat (review) {
      var updateDate = new Date(review.updateDate)
      return updateDate.toDateString()
    },
    determineActiveOrPending (review) {
      if (review.activeStatus === 'A') {
        return 'Approved'
      }
      else {
        return 'Pending'
      }
    },
    setUpTableArray () {
      for (var review in this.reviewsData) {
        this.reviewsDisplay.push({
          entry: this.reviewsData[review].name,
          title: this.reviewsData[review].title,
          rating: this.reviewsData[review].rating,
          status: this.determineActiveOrPending(this.reviewsData[review]),
          comment: this.removeCommentHtml(this.reviewsData[review]),
          updateDate: this.changeDateFormat(this.reviewsData[review]),
          pros: this.reviewsData[review].pros,
          cons: this.reviewsData[review].cons,
          lastUsed: this.reviewsData[review].lastUsed,
          timeUsed: this.reviewsData[review].userTimeDescription,
          editReviewId: this.reviewsData[review].reviewId,
          componentId: this.reviewsData[review].componentId
        })
      }
    },
    setUpEditDialog (tableReview) {
      this.getCurrentItemData(tableReview)
      this.editReviewDialog = true
    },
    getCurrentItemData (tableReview) {
      this.currentReview.title = tableReview.title
      this.currentReview.rating = tableReview.rating
      this.currentReview.lastUsed = format(tableReview.lastUsed, 'YYYY-MM-DD')
      this.currentReview.timeUsed = tableReview.timeUsed
      this.currentReview.pros = tableReview.pros
      this.currentReview.cons = tableReview.cons
      this.currentReview.comment = tableReview.comment
      this.currentReview.timeUsed = tableReview.timeUsed
      this.currentReview.editReviewId = tableReview.editReviewId
      this.currentReview.componentId = tableReview.componentId
    }
  }
}
</script>

<style scoped lang="scss">
  .top-buttons {
    text-transform: none;
    background-color: #E0E0E0 !important;
  }
  .tableLayout {
    display: flex;
    flex-direction: column;
    flex-grow: 1;
  }
.tablePaging {
    position: fixed !important;
    bottom: 0 !important;
}
</style>

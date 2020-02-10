<template>
  <v-dialog :value="value" @input="$emit('close')" max-width="50em">
    <v-card>
      <ModalTitle title="Write a Review" @close="$emit('close')" />
      <v-card-text>
        <v-alert class="w-100" type="warning" :value="true" v-if="$store.state.branding.userInputWarning !== null"
          ><span v-html="$store.state.branding.userInputWarning"></span
        ></v-alert>
        <v-alert
          class="w-100"
          type="info"
          :value="!autoApprove"
          v-if="$store.state.branding.submissionFormWarning !== null"
          ><span v-html="$store.state.branding.submissionFormWarning"></span
        ></v-alert>
        <v-form v-model="reviewValid">
          <v-container>
            <v-text-field
              v-model="review.title"
              :rules="reviewTitleRules"
              :counter="255"
              label="Title*"
              required
            ></v-text-field>

            <p>
              <strong>Rating*</strong>
            </p>

            <star-rating
              v-model="review.rating"
              :rating="review.rating"
              :read-only="false"
              :increment="1"
              :star-size="30"
            ></star-rating>

            <v-spacer style="height: 1.5em"></v-spacer>

            <p>
              <strong>Last date asset was used*</strong>
            </p>

            <v-text-field
              v-model="review.lastUsed"
              :rules="lastUsedRules"
              label="Last Used"
              readonly
              required
              disabled
            ></v-text-field>

            <v-date-picker
              v-model="review.lastUsed"
              :allowed-dates="todaysDateFormatted"
              color="primary"
              no-title
              reactive
              full-width
            >
              <v-spacer></v-spacer>
              <v-btn text color="primary" @click="review.lastUsed = ''">Cancel</v-btn>
            </v-date-picker>

            <v-spacer style="height: 1em"></v-spacer>

            <v-select
              v-model="review.timeUsed"
              :items="timeSelectOptions"
              :rules="timeUsedRules"
              label="How long have you used it*"
              required
            ></v-select>

            <v-select v-model="review.pros" :items="prosSelectOptions" label="Pros" chips multiple></v-select>

            <v-select v-model="review.cons" :items="consSelectOptions" label="Cons" chips multiple></v-select>

            <p>Comment*</p>

            <quill-editor
              style="background-color: white;"
              v-model="review.comment"
              :rules="commentRules"
              required
            ></quill-editor>
            <div v-if="review.comment === ''" class="red--text ml-1">A comment is required</div>
          </v-container>
          <v-card-actions>
            <v-spacer />

            <v-btn color="success" :disabled="!reviewSubmit" @click="submitReview()">Submit</v-btn>
            <v-btn @click="$emit('close')">Cancel</v-btn>
          </v-card-actions>
        </v-form>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
import ModalTitle from '@/components/ModalTitle'
import StarRating from 'vue-star-rating'
import isFuture from 'date-fns/isFuture'

export default {
  name: 'ReviewsModal',
  props: {
    value: {
      type: Boolean,
      default: false
    },
    review: Object
  },
  components: {
    ModalTitle,
    StarRating
  },
  mounted() {
    this.timeSelectOptions = []
    this.prosSelectOptions = []
    this.consSelectOptions = []
    this.lookupTypes()
    this.$http
      .get(`/openstorefront/api/v1/service/application/configproperties/userreview.autoapprove`)
      .then(response => (this.autoApprove = response.data.description))
  },
  data() {
    return {
      autoApprove: false,
      timeSelectOptions: [],
      prosSelectOptions: [],
      consSelectOptions: [],
      reviewSubmit: false,
      reviewValid: false,
      reviewTitleRules: [
        v => !!v || 'Title is required',
        v => (v && v.length <= 255) || 'Title must be less than 255 characters'
      ],
      lastUsedRules: [v => !!v || 'Date is required'],
      timeUsedRules: [v => !!v || 'Time used is required'],
      commentRules: [v => !!v || 'Comment is required']
    }
  },
  watch: {
    'review.comment': function(val) {
      if (val !== '' && this.reviewValid && this.review.rating !== 0) {
        this.reviewSubmit = true
      } else {
        this.reviewSubmit = false
      }
    },
    'review.rating': function(val) {
      if (val !== 0 && this.reviewValid && this.review.comment !== '') {
        this.reviewSubmit = true
      } else {
        this.reviewSubmit = false
      }
    },
    reviewValid: function(val) {
      if (val && this.review.comment !== '' && this.review.rating !== 0) {
        this.reviewSubmit = true
      } else {
        this.reviewSubmit = false
      }
    }
  },
  methods: {
    todaysDateFormatted(val) {
      return !isFuture(new Date(val))
    },
    lookupTypes() {
      this.$http
        .get('/openstorefront/api/v1/resource/lookuptypes/ExperienceTimeType')
        .then(response => {
          if (response.data) {
            this.timeOptions = response.data
            response.data.forEach(element => {
              this.timeSelectOptions.push(element.description)
            })
          }
        })
        .catch(e => this.$toasted.error('An error occurred retrieving component use times'))

      this.$http
        .get('/openstorefront/api/v1/resource/lookuptypes/ReviewPro')
        .then(response => {
          if (response.data) {
            this.prosOptions = response.data
            response.data.forEach(element => {
              this.prosSelectOptions.push(element.description)
            })
          }
        })
        .catch(e => this.$toasted.error('An error occurred retrieving component pros'))

      this.$http
        .get('/openstorefront/api/v1/resource/lookuptypes/ReviewCon')
        .then(response => {
          if (response.data) {
            this.consOptions = response.data
            response.data.forEach(element => {
              this.consSelectOptions.push(element.description)
            })
          }
        })
        .catch(e => this.$toasted.error('An error occurred retrieving component cons'))
    },
    submitReview() {
      let data = {
        comment: this.review.comment,
        cons: [],
        dataSensitivity: '',
        lastUsed: this.review.lastUsed + 'T00:00:00',
        organization: this.$store.state.currentUser.organization,
        pros: [],
        rating: this.review.rating,
        reviewId: '',
        securityMarkingType: '',
        title: this.review.title,
        userTimeCode: '',
        userTypeCode: this.$store.state.currentUser.userTypeCode
      }

      this.consOptions.forEach(consElement => {
        this.review.cons.forEach(selectElement => {
          if (consElement.description === selectElement) {
            data.cons.push({ text: consElement.code })
          }
        })
      })

      this.prosOptions.forEach(prosElement => {
        this.review.pros.forEach(selectElement => {
          if (prosElement.description === selectElement) {
            data.pros.push({ text: prosElement.code })
          }
        })
      })

      this.timeOptions.forEach(element => {
        if (this.review.timeUsed === element.description) {
          data.userTimeCode = element.code
        }
      })
      if (this.review.editReviewId) {
        this.$http
          .put(
            `/openstorefront/api/v1/resource/components/${this.review.componentId}/reviews/${this.review.editReviewId}/detail`,
            data
          )
          .then(response => {
            this.review.editReviewId = ''
            this.$toasted.show('Review Submitted')
            this.$emit('close')
          })
          .catch(e => this.$toasted.error('There was a problem submitting the review.'))
      } else {
        this.$http
          .post(`/openstorefront/api/v1/resource/components/${this.review.componentId}/reviews/detail`, data)
          .then(response => {
            this.$toasted.show('Review Submitted')
            this.$emit('close')
          })
          .catch(e => this.$toasted.error('There was a problem submitting the review.'))
      }
    }
  }
}
</script>

<style>
.w-100 {
  width: 100%;
}
</style>

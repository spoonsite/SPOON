<template>
  <v-dialog :value="value" @input="close" max-width="50em">
    <v-card>
      <ModalTitle :title="title" @close="close" />
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
        <v-form>
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

            <p class="mt-4"><strong>Would use again*</strong></p>

            <v-checkbox v-model="review.recommend" class="pt-0 mt-0"></v-checkbox>

            <p>
              <strong>Last used*</strong>
            </p>

            <v-date-picker
              v-model="rawDate"
              class="mt-4"
              :max="today"
              header-color="primary"
              color="secondary"
              full-width
              :show-current="false"
            ></v-date-picker>

            <v-spacer style="height: 1em"></v-spacer>

            <v-select
              v-model="review.userTimeCode"
              :items="timeOptions"
              :rules="timeUsedRules"
              item-text="description"
              item-value="code"
              label="How long have you used it*"
              required
            ></v-select>

            <v-select
              v-model="review.pros"
              :items="prosOptions"
              item-text="description"
              item-value="code"
              label="Pros"
              chips
              deletable-chips
              multiple
            ></v-select>

            <v-select
              v-model="review.cons"
              :items="consOptions"
              item-text="description"
              item-value="code"
              label="Cons"
              chips
              deletable-chips
              multiple
            ></v-select>

            <p class="mb-2"><strong>Comment*</strong></p>

            <quill-editor
              style="background-color: white;"
              v-model="review.comment"
              :rules="commentRules"
              required
            ></quill-editor>
          </v-container>
          <v-card-actions>
            <v-spacer />

            <v-btn color="success" :disabled="!isFormValid" @click="submitReview()">Submit</v-btn>
            <v-btn @click="close">Cancel</v-btn>
          </v-card-actions>
        </v-form>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
import ModalTitle from '@/components/ModalTitle'
import StarRating from 'vue-star-rating'
import format from 'date-fns/format'
import _ from 'lodash'

export default {
  name: 'ReviewsModal',
  props: {
    value: {
      type: Boolean,
      default: false
    },
    editReview: Object,
    componentId: String,
    title: String
  },
  components: {
    ModalTitle,
    StarRating
  },
  mounted() {
    this.lookupTypes()
    this.$http
      .get(`/openstorefront/api/v1/service/application/configproperties/userreview.autoapprove`)
      .then(response => (this.autoApprove = response.data.description))
  },
  data() {
    return {
      autoApprove: false,
      review: {},
      rawDate: this.today,
      timeOptions: [],
      prosOptions: [],
      consOptions: [],
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
    value: function() {
      if (this.value) {
        if (this.editReview) {
          this.review = _.cloneDeep(this.editReview)
          this.review.rawLastUsed = format(new Date(this.editReview.lastUsed), 'yyyy-MM-dd')
          this.rawDate = format(new Date(this.editReview.lastUsed), 'yyyy-MM-dd')

          let pros = this.review.pros.slice()
          this.review.pros = []
          pros.forEach(e => this.review.pros.push(e.code))

          let cons = this.review.cons.slice()
          this.review.cons = []
          cons.forEach(e => this.review.cons.push(e.code))
        } else {
          this.review = {
            title: '',
            rating: 0,
            recommend: false,
            rawLastUsed: this.today,
            userTimeCode: '',
            pros: [],
            cons: [],
            comment: '',
            organization: this.$store.state.currentUser.organization,
            userTypeCode: this.$store.state.currentUser.userTypeCode
          }
          this.rawDate = this.today
        }
      }
    }
  },
  computed: {
    isFormValid() {
      return (
        this.review.title &&
        this.review.title !== '' &&
        this.review.rating &&
        this.review.rating !== 0 &&
        this.review.rawLastUsed &&
        this.review.rawLastUsed !== '' &&
        this.review.userTimeCode &&
        this.review.userTimeCode !== '' &&
        this.review.comment &&
        this.review.comment !== ''
      )
    },
    today() {
      return format(new Date(), 'yyyy-MM-dd')
    }
  },
  methods: {
    close() {
      this.review = {}
      this.$emit('close')
    },
    lookupTypes() {
      this.$http
        .get('/openstorefront/api/v1/resource/lookuptypes/ExperienceTimeType')
        .then(response => {
          if (response.data) {
            this.timeOptions = response.data
          }
        })
        .catch(error => {
          this.$toasted.error('An error occurred retrieving component use times')
          console.error(error)
        })

      this.$http
        .get('/openstorefront/api/v1/resource/lookuptypes/ReviewPro')
        .then(response => {
          if (response.data) {
            this.prosOptions = response.data
          }
        })
        .catch(error => {
          this.$toasted.error('An error occurred retrieving component pros')
          console.error(error)
        })

      this.$http
        .get('/openstorefront/api/v1/resource/lookuptypes/ReviewCon')
        .then(response => {
          if (response.data) {
            this.consOptions = response.data
          }
        })
        .catch(error => {
          this.$toasted.error('An error occurred retrieving component cons')
          console.error(error)
        })
    },
    submitReview() {
      this.review.lastUsed = new Date(this.rawDate).toISOString()
      this.review.reviewId = ''

      this.review.prosRaw = this.review.pros
      this.review.pros = []
      this.review.prosRaw.forEach(e => {
        this.review.pros.push({ text: e })
      })

      this.review.consRaw = this.review.cons
      this.review.cons = []
      this.review.consRaw.forEach(e => {
        this.review.cons.push({ text: e })
      })

      if (this.editReview === undefined) {
        this.$http
          .post(`/openstorefront/api/v1/resource/components/${this.componentId}/reviews/detail`, this.review)
          .then(response => {
            this.$toasted.show('Review Submitted')
            this.close()
          })
          .catch(error => {
            this.$toasted.error('There was a problem submitting the review')
            console.error(error)
          })
      } else {
        this.review.reviewId = this.editReview.reviewId
        this.review.userTypeCode = this.$store.state.currentUser.userTypeCode

        this.$http
          .put(
            `/openstorefront/api/v1/resource/components/${this.componentId}/reviews/${this.review.reviewId}/detail`,
            this.review
          )
          .then(response => {
            this.$toasted.show('Review Submitted')
            this.close()
          })
          .catch(error => {
            this.$toasted.error('There was a problem submitting the review')
            console.error(error)
          })
      }
    }
  }
}
</script>

<style scoped>
.w-100 {
  width: 100%;
}
p {
  margin-bottom: 0 !important;
}
</style>

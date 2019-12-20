<template>
  <div>
    <v-dialog
      v-model="editReviewDialog"
      max-width="50em"
    >
      <v-card>
        <ModalTitle title='Write a Review' @close='editReviewDialog = false' />
        <v-card-text>
          <v-alert class="w-100" type="warning" :value="true"><span v-html="$store.state.branding.userInputWarning"></span></v-alert>
          <v-alert class="w-100" type="info" :value="true"><span v-html="$store.state.branding.submissionFormWarning"></span></v-alert>

          <v-form>

            <v-container>
              <v-text-field
                v-model="currentReview.title"
                :rules="reviewTitleRules"
                :counter="255"
                label="Title"
                required
              ></v-text-field>

              <p>
                <strong>Rating*</strong>
              </p>

              <star-rating
                v-model="currentReview.rating"
                :rating="currentReview.rating"
                :read-only="false"
                :increment="1"
                :star-size="30"
              ></star-rating>

              <v-spacer style="height: 1.5em"></v-spacer>

              <p>
                <strong>Last date asset was used*</strong>
              </p>

              <v-text-field
                v-model="currentReview.lastUsed"
                :rules="lastUsedRules"
                label="Last Used"
                readonly
                required
                disabled
              ></v-text-field>

              <v-date-picker
                v-model="currentReview.lastUsed"
                :allowed-dates="todaysDateFormatted"
                no-title
                reactive
                full-width
              >
                <v-spacer></v-spacer>
                <v-btn flat color="accent" @click="currentReview.lastUsed=''">Cancel</v-btn>
              </v-date-picker>

              <v-spacer style="height: 1em"></v-spacer>

              <v-select
                v-model="currentReview.timeUsed"
                :items="timeSelectOptions"
                :rules="timeUsedRules"
                label="How long have you used it"
                required
              ></v-select>

              <v-select
                v-model="currentReview.pros"
                :items="prosSelectOptions"
                label="Pros"
                chips
                multiple
              ></v-select>

              <v-select
                v-model="currentReview.cons"
                :items="consSelectOptions"
                label="Cons"
                chips
                multiple
              ></v-select>

              <p>
                Comment: <span v-if="currentReview.comment === ''" class="red--text">comment is required *</span>
              </p>

              <quill-editor
                style="background-color: white;"
                v-model="currentReview.comment"
                :rules="commentRules"
                required
              ></quill-editor>

            </v-container>
            <v-card-actions>
              <v-spacer/>
              
              <v-btn color="success" :disabled="!reviewSubmit" @click="submitReview()">Submit</v-btn>
              <v-btn @click="writeReviewDialog = false; currentReview.comment=''">Cancel</v-btn>
            </v-card-actions>
          </v-form>
        </v-card-text>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import StarRating from 'vue-star-rating'
import ModalTitle from '@/components/ModalTitle'
import isFuture from 'date-fns/is_future'
export default {
  name: 'Review',
  props: ['list'],
  components: {
    StarRating,
    ModalTitle
  },
  mounted () {
    
  },
  data () {
    return {
      reviewTitleRules: [
        v => !!v || 'Title is required',
        v => (v && v.length <= 255) || 'Title must be less than 255 characters'
      ],
      lastUsedRules: [
        v => !!v || 'Date is required'
      ],
      timeUsedRules: [
        v => !!v || 'Time used is required'
      ],
      commentRules: [
        v => !!v || 'Comment is required'
      ],
      prosSelectOptions: [],
      consSelectOptions: [],
      timeSelectOptions: [],
      editReviewDialog: false
    }
  },
  methods: {
    todaysDateFormatted (val) {
      return !isFuture(val)
    },
    lookupTypes () {
      this.$http.get('/openstorefront/api/v1/resource/lookuptypes/ExperienceTimeType')
        .then(response => {
          if (response.data) {
            this.timeOptions = response.data
            response.data.forEach(element => {
              this.timeSelectOptions.push(element.description)
            })
          }
        })
        .catch(e => this.errors.push(e))

      this.$http.get('/openstorefront/api/v1/resource/lookuptypes/ReviewPro')
        .then(response => {
          if (response.data) {
            this.prosOptions = response.data
            response.data.forEach(element => {
              this.prosSelectOptions.push(element.description)
            })
          }
        })
        .catch(e => this.errors.push(e))

      this.$http.get('/openstorefront/api/v1/resource/lookuptypes/ReviewCon')
        .then(response => {
          if (response.data) {
            this.consOptions = response.data
            response.data.forEach(element => {
              this.consSelectOptions.push(element.description)
            })
          }
        })
        .catch(e => this.errors.push(e))
    },
  }
}
</script>

<style>

</style>

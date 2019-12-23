

<template>
  <v-dialog
    :value="value"
    @input="close"
    max-width='50em'
  >
    <v-card>
      <ModalTitle title='Write a Review' @close='close' />
      <v-card-text>
        <v-alert class="w-100" type="warning" :value="true"><span v-html="$store.state.branding.userInputWarning"></span></v-alert>
        <v-alert class="w-100" type="info" :value="true"><span v-html="$store.state.branding.submissionFormWarning"></span></v-alert>
        <v-form>
          <v-container>
            <v-text-field
              v-model="review.title"
              :rules="reviewTitleRules"
              :counter="255"
              label="Title"
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
              <strong>{{ review }}</strong>
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
              no-title
              reactive
              full-width
            >
              <v-spacer></v-spacer>
              <v-btn flat color="accent" @click="review.lastUsed=''">Cancel</v-btn>
            </v-date-picker>

            <v-spacer style="height: 1em"></v-spacer>

            <v-select
              v-model="review.timeUsed"
              :items="timeSelectOptions"
              :rules="timeUsedRules"
              label="How long have you used it"
              required
            ></v-select>

            <v-select
              v-model="review.pros"
              :items="prosSelectOptions"
              label="Pros"
              chips
              multiple
            ></v-select>

            <v-select
              v-model="review.cons"
              :items="consSelectOptions"
              label="Cons"
              chips
              multiple
            ></v-select>

            <p>
              Comment: <span v-if="review.comment === ''" class="red--text">comment is required *</span>
            </p>

            <quill-editor
              style="background-color: white;"
              v-model="review.comment"
              :rules="commentRules"
              required
            ></quill-editor>
          </v-container>
          <v-card-actions>
            <v-spacer/>

            <v-btn color="success">Submit</v-btn>
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

export default {
  name: 'ReviewsModal',
  props: {
    value: false,
    review: {}
  },
  components: {
    ModalTitle,
    StarRating
  },
  data () {
    return {
      timeSelectOptions: [],
      prosSelectOptions: [],
      consSelectOptions: [],
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
    }
  },
  methods: {
    close () {
      this.$emit('close')
    }
  },
}
</script>

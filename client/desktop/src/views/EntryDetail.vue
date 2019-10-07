<template lang="html">
  <v-layout
    row
    justify-center
    align-center
    v-if="isLoading"
    >
    <v-flex xs1>
      <v-progress-circular
        color="primary"
        :size="60"
        :width="6"
        indeterminate
        class="spinner"
      ></v-progress-circular>
    </v-flex>
  </v-layout>

  <div v-else class="entry-detail-page">
    <div class="entry-details-top">
      <div v-if="detail.componentMedia && detail.componentMedia.length > 0">
        <Lightbox
          :list="lightboxList"
          class="entry-media"
        ></Lightbox>
      </div>
      <div v-else class="no-media"></div>
      <div class="detail-header">
        <div class="component-name">
          <img v-if="detail.componentTypeIconUrl" :src="baseURL + detail.componentTypeIconUrl" width="40px">
          <p class="headline">{{detail.name}}</p>
        </div>
        <div class="detail-header-body">
          <div class="detail-header-left">
            <div class="dates">
              <p class="date"><strong>Organization:</strong> {{ detail.organization }}
              <p class="date" v-if='detail.componentTypeLabel.includes(">")'>
                <strong>Category:</strong>
                {{ detail.componentTypeLabel }}
              <p class="date"><strong>Last Updated:</strong> {{ detail.lastActivityDts | formatDate}}</p>
              <p class="date"><strong>Approved Date:</strong> {{ detail.approvedDate | formatDate}}</p>
            </div>
            <div
                style="padding-bottom: 1em;"
                class="clearfix tags"
                v-if="detail.tags && detail.tags.length !== 0"
            >
              <span
                v-for="tag in detail.tags"
                :key="tag.text"
                style="margin-right: 0.8em; cursor: pointer;"
              >
                <v-icon style="font-size: 14px; color: #f8c533;">fas fa-tag</v-icon>
                {{ tag.text }}
              </span>
            </div>
          </div>
          <div class="detail-header-right">
            <v-switch class="watching" color="success" :label="watchSwitch ? 'Watching' : 'Not Watching'" v-model="watchSwitch"></v-switch>
            <p>
              <strong>Average User Rating:</strong>
              <star-rating :rating="computeAverageRating(detail)" :read-only="true" :increment="0.01" :star-size="30"></star-rating>
            </p>
          </div>
        </div>
      </div>
    </div>

    <v-divider></v-divider>

    <div class="entry-details-bottom">
      <v-expansion-panels>
        <v-expansion-panel class="expansion-spacing" value="1">
          <v-expansion-panel-content>
            <div slot="header"><h2>Description</h2></div>
            <div class="expansion-content" v-html="detail.description"></div>
          </v-expansion-panel-content>
        </v-expansion-panel>

        <v-expansion-panel class="expansion-spacing" value="1">
          <v-expansion-panel-content>
            <div slot="header"><h2>Attributes</h2></div>
            <div class="expansion-content">
              <v-data-table dense
              :headers="attributeTableHeaders"
              :items="detail.attributes"
              hide-actions
              item-key="name"
              >
                <template slot="items" slot-scope="props">
                  <td>{{ props.item.typeDescription }}</td>
                  <td>{{ props.item.codeDescription }} <span v-if="props.item.unit" v-html="props.item.unit"></span></td>
                </template>
              </v-data-table>
            </div>
          </v-expansion-panel-content>
        </v-expansion-panel>

        <v-expansion-panel class="expansion-spacing">
          <v-expansion-panel-content>
            <div slot="header"><h2>Resources</h2></div>
            <div class="expansion-content"> 
              <div v-for="item in detail.resources"
                :key="item.resourceId"
              >
                <strong >{{ item.resourceTypeDesc }}</strong>
                <v-btn flat icon :href="baseURL+item.actualLink"><v-icon>link</v-icon></v-btn>
                <div style="overflow-x: auto; white-space: nowrap;">
                  <a :href="baseURL+item.actualLink" style="display: block; margin-bottom: 0.5em;">
                    <span v-if="item.description">{{ item.description }}</span><span v-else>{{ item.link }}</span>
                  </a>
                </div>
              </div>
            </div>
          </v-expansion-panel-content>
        </v-expansion-panel>

        <v-expansion-panel class="expansion-spacing">
          <v-expansion-panel-content>
            <div slot="header">
              <h2>Reviews</h2>
            </div>
            <div class="expansion-content">
              <strong>Average User Rating:</strong>
              <star-rating :rating="computeAverageRating(detail)" :read-only="true" :increment="0.01" :star-size="30"></star-rating>
              <v-btn @click="writeReviewDialog = true">Write a Review</v-btn>
            </div>
            <div v-if="detail.reviews && detail.reviews.length !== 0">
              <div
                v-for="review in detail.reviews"
                :key="review.reviewId"
              >
                <div style="background-color: white; padding: 0.5em; margin-bottom: 1em;" class="elevation-2">
                  <h2>{{ review.title }}</h2>
                  <v-alert type="warning" :value="review.activeStatus === 'P'">This review is pending admin approval.</v-alert>
                  <p>
                    <star-rating :rating="review.rating" :read-only="true" :increment="0.01" :star-size="30"></star-rating>
                  </p>
                  <p>{{ review.username + " (" + review.userTypeCode + ") - " }} {{ review.updateDate | formatDate }}</p>
                  <p class="reviewPar"><strong>Organization: </strong>{{ review.organization }}</p>
                  <p class="reviewPar"><strong>Experience: </strong>{{ review.userTimeDescription }}</p>
                  <p class="reviewPar"><strong>Last Used: </strong>{{ review.lastUsed | formatDate }}</p>
                  <div v-if="review.pros.length > 0 || review.cons.length > 0">
                    <v-layout row justify-space-around>
                      <v-flex v-if="review.pros.length > 0" xs4>
                          <v-card-text class="px-0">
                            <p><strong>Pros</strong></p>
                            <li v-for="pro in review.pros" :key="pro.code">{{ pro.text }}</li>
                          </v-card-text>
                      </v-flex>
                      <v-flex v-if="review.cons.length > 0" xs4>
                          <v-card-text class="px-0">
                            <p><strong>Cons</strong></p>
                            <p v-for="cons in review.cons" :key="cons.code">{{ cons.text }}</p>
                          </v-card-text>
                      </v-flex>
                    </v-layout>
                  </div>
                  <p class="reviewPar"><strong>Comments:</strong></p>
                  <p v-html="review.comment"></p>
                  <v-btn v-if="review.username === $store.state.currentUser.username"
                    @click="editReviewSetup(review)"
                    small
                  >Edit
                  </v-btn>
                  <v-btn v-if="review.username === $store.state.currentUser.username"
                    @click="deleteReviewDialog=true; deleteRequestId=review.reviewId;"
                    small
                  >Delete
                  </v-btn>
                </div>
              </div>
            </div>
            <div v-else class="expansion-content">
              <p>There are no reviews for this entry.</p>
            </div>
          </v-expansion-panel-content>
        </v-expansion-panel>

        <v-dialog
        v-model="writeReviewDialog"
        max-width="500px"
        >
        <v-card>
          <v-card-title>
            <h2 class="w-100">Write a Review</h2>
            <v-alert class="w-100" type="warning" :value="true"><span v-html="$store.state.branding.userInputWarning"></span></v-alert>
            <v-alert class="w-100" type="info" :value="true"><span v-html="$store.state.branding.submissionFormWarning"></span></v-alert>
          </v-card-title>

          <v-form v-model="reviewValid">

          <v-container>
            <v-text-field
              v-model="newReview.title"
              :rules="reviewTitleRules"
              :counter="255"
              label="Title"
              required
            ></v-text-field>

            <p>
              <strong>Rating*</strong>
            </p>

            <star-rating
              v-model="newReview.rating"
              :rating="newReview.rating"
              :read-only="false"
              :increment="1"
              :star-size="30"
            ></star-rating>

            <v-spacer style="height: 1.5em"></v-spacer>

            <p>
              <strong>Last date asset was used*</strong>
            </p>

            <v-text-field
              v-model="newReview.lastUsed"
              :rules="lastUsedRules"
              label="Last Used"
              readonly
              required
              disabled
              solo
            ></v-text-field>

            <v-date-picker
              v-model="newReview.lastUsed"
              :allowed-dates="todaysDateFormatted"
              no-title
              reactive
              full-width
            >
              <v-spacer></v-spacer>
              <v-btn flat color="accent" @click="newReview.lastUsed=''">Cancel</v-btn>
            </v-date-picker>

            <v-spacer style="height: 1em"></v-spacer>

            <v-select
              v-model="newReview.timeUsed"
              :items="timeSelectOptions"
              :rules="timeUsedRules"
              label="How long have you used it"
              required
            ></v-select>

            <v-select
              v-model="newReview.pros"
              :items="prosSelectOptions"
              label="Pros"
              chips
              multiple
            ></v-select>

            <v-select
              v-model="newReview.cons"
              :items="consSelectOptions"
              label="Cons"
              chips
              multiple
            ></v-select>

            <p>
              Comment: <span v-if="newReview.comment === ''" class="red--text">comment is required *</span>
            </p>

            <quill-editor
              style="background-color: white;"
              v-model="comment"
              :rules="commentRules"
              required
            ></quill-editor>

          </v-container>
            <v-card-actions>
              <v-btn :disabled="!reviewSubmit" @click="submitReview()">Submit</v-btn>
              <v-btn @click="writeReviewDialog = false; newReview.comment='';">Cancel</v-btn>
            </v-card-actions>
          </v-form>
        </v-card>
      </v-dialog>

      <v-dialog v-model="deleteReviewDialog">
        <v-card>
          <v-card-title>Confirm Review Deletion</v-card-title>
          <v-btn @click="deleteReviewConfirmation()">OK</v-btn>
          <v-btn @click="deleteReviewDialog = false; deleteRequestId=''">Cancel</v-btn>
        </v-card>
      </v-dialog>

        <v-expansion-panel class="expansion-spacing">
          <v-expansion-panel-content>
            <div slot="header"><h2>Questions and Answers</h2></div>
            <div class="expansion-content">
              <v-btn @click="askQuestionDialog = true">Ask a Question</v-btn>
              <Question v-for="question in questions" :key="question.question" @questionDeleted="deleteQuestion(question)" :question="question"></Question>
              <div style="margin-top: 0.5em;" v-if="questions.length === 0">There are no questions for this entry.</div>
            </div>

          </v-expansion-panel-content>
        </v-expansion-panel>
        <v-dialog
        v-model="askQuestionDialog"
        >
          <v-card>
            <v-card-title>
              <h2 class="w-100">Ask a Question</h2>
              <v-alert class="w-100" type="warning" :value="true"><span v-html="$store.state.branding.userInputWarning"></span></v-alert>
              <v-alert class="w-100" type="info" :value="true"><span v-html="$store.state.branding.submissionFormWarning"></span></v-alert>
            </v-card-title>
            <v-card-text>
              <quill-editor
              style="background-color: white;"
              v-model="newQuestion"
              ></quill-editor>
            </v-card-text>
            <v-card-actions>
              <v-btn @click="submitQuestion()">Submit</v-btn>
              <v-btn @click="askQuestionDialog = false; newQuestion = '';">Cancel</v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>

        <v-expansion-panel class="expansion-spacing">
          <v-expansion-panel-content>
            <div slot="header"><h2>Contacts</h2></div>
            <v-card class="expansion-content">
              <v-card-text v-if="detail.contacts && detail.contacts.length > 0">
                <h2>Points of Contact</h2>
                <div
                  v-for="contact in detail.contacts"
                  :key="contact.contactId"
                >
                  <hr>
                  <p class="contactPar"><strong>Name: </strong>{{ contact.name }}</p>
                  <p class="contactPar"><strong>Organization: </strong>{{ contact.organization }}</p>
                  <p class="contactPar"><strong>Position: </strong>{{ contact.positionDescription }}</p>
                  <p class="contactPar"><strong>Phone: </strong><a :href="`tel: ${contact.phone}`">{{ contact.phone }}</a></p>
                  <p class="contactPar"><strong>Email: </strong><a :href="`mailto:${contact.email}`">{{ contact.email }}</a></p>
                </div>
              </v-card-text>
              <v-card-text v-else>
                <p>There are no contacts for this entry.</p>
              </v-card-text>
            </v-card>
          </v-expansion-panel-content>
        </v-expansion-panel>

      </v-expansion-panels>

    </div>
    <v-footer color="primary" dark height="auto" display="flex" style="justify-content: center;">
      <v-card color="primary" dark flat class="footer-wrapper">
        <div class="footer-block" v-html="$store.state.branding.landingPageFooter"></div>
        <p style="text-align: center;" v-html="$store.state.appVersion"></p>
      </v-card>
    </v-footer>
  </div>
</template>

<script lang="js">
import StarRating from 'vue-star-rating'
import _ from 'lodash'
import Lightbox from '../components/Lightbox'
import Question from '../components/Question'
import format from 'date-fns/format'
import isFuture from 'date-fns/is_future'
import router from '../router'

export default {
  name: 'entry-detail-page',
  components: {
    StarRating,
    Lightbox,
    Question
  },
  mounted () {
    if (this.$route.params.id) {
      this.id = this.$route.params.id
    }

    this.lookupTypes()
    this.getDetail()
    this.getQuestions()
    this.checkWatch()
  },
  data () {
    return {
      baseURL: '/openstorefront/',
      isLoading: true,
      askQuestionDialog: false,
      newQuestion: '',
      writeReviewDialog: false,
      deleteReviewDialog: false,
      deleteRequestId: '',
      editReviewId: '',
      reviewSubmit: false,
      newReview: {
        title: '',
        rating: 0,
        recommend: false,
        lastUsed: '',
        timeUsed: '',
        pros: [],
        cons: []
      },
      comment: '',
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
      timeOptions: [],
      timeSelectOptions: [],
      prosOptions: [],
      prosSelectOptions: [],
      consOptions: [],
      consSelectOptions: [],
      reviewValid: false,
      todaysDate: new Date(),
      detail: {},
      addDetail: {},
      questions: {},
      watchSwitch: false,
      watchId: 'holder',
      watchBeingChecked: true,
      hasImage: false,
      lightboxList: [],
      errors: [],
      mediaDetailsDialog: false,
      currentMediaDetailItem: {},
      mediaIconMap: {
        'VID': 'file-video',
        'TEX': 'file-alt',
        'AUD': 'file-audio',
        'ARC': 'file-archive',
        'IMG': 'file-image',
        'OTH': 'file'
      },
      attributeTableHeaders: [
        { text: 'Attribute Type', value: 'typeDescription' },
        { text: 'Value', value: 'codeDescription' }
      ]

    }
  },
  methods: {
    checkWatch () {
      this.$http.get(`/openstorefront/api/v1/resource/userprofiles/${this.$store.state.currentUser.username}/watches`)
        .then(response => {
          if (response) {
            if (response.data && response.data.length > 0) {
              let tempWatch = false
              for (var i = 0; i < response.data.length; i++) {
                if (response.data[i].componentId === this.id) {
                  this.watchSwitch = true
                  tempWatch = true
                  this.watchId = response.data[i].watchId
                  break
                }
              }
              if (tempWatch === false) {
                this.watchSwitch = false
              }
            }
          }
        })
        .finally(() => {
          this.watchBeingChecked = false
        })
    },
    computeAverageRating (detail) {
      var temp = 0
      var averageRating = 0
      if (detail.reviews) {
        for (var i = 0; i < detail.reviews.length; i++) {
          if (detail.reviews[i].rating) {
            temp += detail.reviews[i].rating
          } else {
            return 0
          }
        }
      } else {
        return 0
      }
      if (detail.reviews.length !== 0) {
        averageRating = temp / detail.reviews.length
      }
      return averageRating
    },
    computeHasImage () {
      if (this.detail.componentMedia) {
        for (var i = 0; i < this.detail.componentMedia.length; i++) {
          if (this.detail.componentMedia[i].mediaTypeCode === 'IMG') {
            this.hasImage = true
            return
          }
        }
      }
    },
    deleteQuestion (question) {
      this.questions = this.questions.filter(el => el.questionId !== question.questionId)
    },
    deleteReviewConfirmation () {
      this.$http.delete(`/openstorefront/api/v1/resource/components/${this.id}/reviews/${this.deleteRequestId}`)
        .then(response => {
          this.$toasted.show('Review Deleted')
          this.deleteReviewDialog = false
          this.getDetail()
        })
    },
    editReviewSetup (review) {
      this.writeReviewDialog = true
      this.newReview.title = review.title
      this.newReview.rating = review.rating
      this.newReview.recommend = review.recommend
      this.newReview.lastUsed = format(review.lastUsed, 'YYYY-MM-DD')
      this.newReview.timeUsed = review.userTimeDescription
      review.pros.forEach(element => {
        this.newReview.pros.push(element.text)
      })
      review.cons.forEach(element => {
        this.newReview.cons.push(element.text)
      })
      this.comment = review.comment
      this.editReviewId = review.reviewId
    },
    filterLightboxList () {
      if (this.detail.componentMedia) {
        this.lightboxList = _.filter(this.detail.componentMedia, function (o) {
          return (o.mediaTypeCode === 'IMG' || o.mediaTypeCode === 'VID') && !o.hideInDisplay
        })
      }
    },
    getAddDetail () {
      this.$http.get(`/openstorefront/api/v1/resource/components/${this.id}`)
        .then(response => {
          this.addDetail = response.data
        })
        .finally(() => {
          this.isLoading = false
        })
    },
    getAnswers (qid) {
      this.isLoading = true
      this.$http.get(`/openstorefront/api/v1/resource/components/${this.id}/questions/${qid}/responses`)
        .then(response => {
          this.answers[qid] = response.data
          this.isLoading = false
        })
        .catch(e => this.errors.push(e))
    },
    getDetail () {
      this.isLoading = true
      this.$http.get(`/openstorefront/api/v1/resource/components/${this.id}/detail`)
        .then(response => {
          this.detail = response.data
        })
        .catch(e => this.errors.push(e))
        .finally(() => {
          this.computeHasImage()
          this.filterLightboxList()
          this.getAddDetail()
        })
    },
    getQuestions () {
      this.isLoading = true
      this.$http.get(`/openstorefront/api/v1/resource/components/${this.id}/questions`)
        .then(response => {
          this.questions = response.data
        })
        .catch(e => this.errors.push(e))
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
    showMediaDetails (item) {
      this.currentMediaDetailItem = item
      this.mediaDetailsDialog = true
    },
    submitQuestion () {
      let data = {
        dataSensitivity: '',
        organization: this.$store.state.currentUser.organization,
        question: this.newQuestion,
        securityMarkingType: '',
        userTypeCode: this.$store.state.currentUser.userTypeCode
      }
      this.$http.post(`/openstorefront/api/v1/resource/components/${this.id}/questions`, data)
        .then(response => {
          this.questions.push(response.data)
          this.newQuestion = ''
          this.askQuestionDialog = false
          this.$toasted.show('Question submitted.')
        })
        .catch(e => this.$toasted.error('There was a problem submitting the question.'))
    },
    submitReview () {
      this.isLoading = true

      let data = {
        comment: this.comment,
        cons: [],
        dataSensitivity: '',
        lastUsed: this.newReview.lastUsed + 'T00:00:00',
        organization: this.$store.state.currentUser.organization,
        pros: [],
        rating: this.newReview.rating,
        reviewId: '',
        securityMarkingType: '',
        title: this.newReview.title,
        userTimeCode: '',
        userTypeCode: this.$store.state.currentUser.userTypeCode
      }

      this.consOptions.forEach(consElement => {
        this.newReview.cons.forEach(selectElement => {
          if (consElement.description === selectElement) {
            data.cons.push({ text: consElement.code })
          }
        })
      })

      this.prosOptions.forEach(prosElement => {
        this.newReview.pros.forEach(selectElement => {
          if (prosElement.description === selectElement) {
            data.pros.push({ text: prosElement.code })
          }
        })
      })

      this.timeOptions.forEach(element => {
        if (this.newReview.timeUsed === element.description) {
          data.userTimeCode = element.code
        }
      })

      if (this.editReviewId) {
        this.$http.put(`/openstorefront/api/v1/resource/components/${this.id}/reviews/${this.editReviewId}/detail`, data)
          .then(response => {
            this.writeReviewDialog = false
            this.editReviewId = ''
            this.$toasted.show('Review Submitted')
          })
          .finally(() => {
            this.isLoading = false
            this.getDetail()
          })
          .catch(e => this.$toasted.error('There was a problem submitting the review.'))
      } else {
        this.$http.post(`/openstorefront/api/v1/resource/components/${this.id}/reviews/detail`, data)
          .then(response => {
            this.writeReviewDialog = false
            this.$toasted.show('Review Submitted')
          })
          .finally(() => {
            this.isLoading = false
            this.getDetail()
          })
          .catch(e => this.$toasted.error('There was a problem submitting the review.'))
      }
    },
    todaysDateFormatted (val) {
      return !isFuture(val)
    }
  },
  watch: {
    comment: function (val) {
      if (val !== '' && this.reviewValid) {
        this.reviewSubmit = true
      } else {
        this.reviewSubmit = false
      }
    },
    reviewValid: function (val) {
      if (val && this.comment !== '') {
        this.reviewSubmit = true
      } else {
        this.reviewSubmit = false
      }
    },
    watchSwitch: function (val) {
      if (!this.watchBeingChecked) {
        this.watchBeingChecked = true
        if (this.watchSwitch === true) {
          let watch = {
            componentId: this.detail.componentId,
            lastViewDts: new Date(),
            username: this.$store.state.currentUser.username,
            notifyFlg: false
          }
          this.$http.post(`/openstorefront/api/v1/resource/userprofiles/${this.$store.state.currentUser.username}/watches`, watch)
            .then(response => {
              if (response.errors) {
                this.watchSwitch = false
              }
            })
            .finally(() => {
              this.checkWatch()
            })
        } else {
          this.$http.delete(`/openstorefront/api/v1/resource/userprofiles/${this.$store.state.currentUser.username}/watches/${this.watchId}`)
            .finally(() => {
              this.checkWatch()
            })
        }
      }
    },
    writeReviewDialog: function (val) {
      if (val === false) {
        this.newReview.title = ''
        this.newReview.rating = 0
        this.newReview.recommend = false
        this.newReview.lastUsed = ''
        this.newReview.timeUsed = ''
        this.newReview.pros = []
        this.newReview.cons = []
        this.editReviewId = ''
        this.comment = ''
      }
    }
  },
  computed: {
    commentsViewable () {
      // TODO: look at me when the endpoints are implemented
      if (this.$store.state.currentUser.username === this.addDetail.ownerUser) {
        return true
      }
      if (this.$store.getters.hasPermission('ADMIN-ENTRY-COMMENT-MANAGEMENT')) {
        return true
      }
      if (this.$store.getters.hasPermission('WORKFLOW-ADMIN-SUBMISSION-COMMENTS')) {
        return true
      }
      return false
    }
  }
}
</script>

<style scoped lang="scss">

  p {
    margin: 0px;
  }
  button {
    background-color: white !important;
  }
  .entry-media {
    display: flex;
    margin: 15px;
    margin-bottom: 2em;
    height: auto;
    width: auto;
    max-width: 500px;
  }
  .no-media {
    flex-grow: 27;
    max-width: 500px;
    max-height: 500px;
    margin: 15px;
  }
  .entry-detail-page {
    display: flex;
    flex-direction: column;
  }
  .entry-details-top {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
  }
  .detail-header {
    display: flex;
    flex-direction: column;
    flex-grow: 100;
    width: auto;
  }
  .component-name {
    display: flex;
    align-items: center;
    padding-top: 15px;
    padding-left: 15px;
  }
  .headline {
    padding-left: 10px;
  }
  .detail-header-body {
    display: flex;
    flex-wrap: wrap;
  }
  .detail-header-left {
    flex-grow: 2;
  }
  .detail-header-right {
    padding-left: 15px;
    padding-bottom: 10px;
  }
  .tags {
    display: flex;
    flex-wrap: wrap;
  }
  .tags {
    margin: 15px 0px 0px 15px;
  }
  .detail-header-right {
    flex-grow: 1;
  }
  .entry-details-bottom {
    display: flex;
    flex-direction: column;
  }
  .dates {
    padding: 10px 0px 0px 15px;
  }
  .date {
    padding-bottom: 10px;
  }
  .watching {
    margin: 0px;
    padding: 0px;
  }
  .expansion-spacing {
    margin: auto;
    max-width:85em;
    margin-bottom: 5px;
  }
  .expansion-content {
    padding: 15px;
    padding-bottom: 5px;
    background-color: #EEEEEE !important;
  }
  .spinner {
    margin-top: 7em;
  }
  .carousel {
    margin-bottom: 1em;
  }
  .contactPar {
    margin-bottom: 0.5em;
  }
  .reviewPar {
    margin-bottom: 0.5em;
  }
  hr {
    color: #333;
    margin-bottom: 1em;
  }
  .icon {
    margin-right: 0.3em;
  }
  .w-100 {
    width: 100%;
  }
  .icon-2x {
    font-size: 20px;
  }
  .media-link {
    text-decoration: none;
  }
  .media-link:hover {
    text-decoration: underline;
  }
  .list-item {
    line-height: 2.4em;
  }
  .centeralign {
    margin-right: auto;
    margin-left: auto;
  }
</style>

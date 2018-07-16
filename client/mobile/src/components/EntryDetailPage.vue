<template lang="html">

  <div class="entry-detail-page">
    <v-card class="grey darken-3 white--text text-md-center">
      <v-card-text>
        <h1 class="title">{{detail.name}}</h1>
      </v-card-text>
    </v-card>

    <Lightbox
      v-if="detail.componentMedia && detail.componentMedia.length > 0"
      :list="lightboxList"
    ></Lightbox>

    <v-expansion-panel popout class="mt-3">

      <v-expansion-panel-content>
        <div slot="header">Summary</div>
        <v-card class="grey lighten-4">
          <v-card-text>
            <h2>Component Type</h2>
            <hr>
            <p>
              <img v-if="detail.componentTypeIconUrl" :src="baseURL + detail.componentTypeIconUrl" width="30" >
              <router-link
                :to="{path: '/search', query: { comp: detail.componentType }}"
              >
                {{ detail.componentTypeLabel }}
              </router-link>
            </p>
            <p
              style="padding-bottom: 1em;"
              class="clearfix"
              v-if="detail.tags && detail.tags.length !== 0"
            >
              <span
                v-for="tag in detail.tags"
                :key="tag.text"
                style="float: left; margin-right: 0.8em; cursor: pointer;"
              >
                <v-icon style="font-size: 14px;">fas fa-tag</v-icon>
                {{ tag.text }}
              </span>
            </p>
            <h2>Details</h2>
            <hr>
            <p><strong>Organization:</strong> {{ detail.organization }}</p>
            <p><strong>Last Updated:</strong> {{ detail.lastActivityDts | formatDate}}</p>
            <p><strong>Approved Date:</strong> {{ detail.approvedDate | formatDate}}</p>
            <h2>Description</h2>
            <hr>
            <div style="overflow: auto;" v-html="detail.description" class="detail"></div>
          </v-card-text>
        </v-card>
      </v-expansion-panel-content>

      <v-expansion-panel-content v-if="detail.attributes && detail.attributes.length !== 0">
        <div slot="header">Attributes</div>
        <v-card class="grey lighten-4">
          <v-card-text>
            <v-data-table
              :headers="attributeTableHeaders"
              :items="detail.attributes"
              hide-actions
              item-key="name"
            >
              <template slot="items" slot-scope="props">
                <td>{{ props.item.typeDescription }}</td>
                <td class="text-xs-right">{{ props.item.codeDescription }}</td>
              </template>
            </v-data-table>
          </v-card-text>
        </v-card>
      </v-expansion-panel-content>

      <v-expansion-panel-content v-if="detail.componentMedia && detail.componentMedia.length > 0">
        <div slot="header">Media Download</div>
        <v-card class="grey lighten-4">
          <v-card-text>
            <ul class="fa-ul">
            <li
              v-for="item in detail.componentMedia"
              v-if="item.link"
              :key="item.componentType"
              class="list-item"
            >
              <span class="fa-li"><v-icon class="icon-2x">fas fa-{{ mediaIconMap[item.mediaTypeCode] }}</v-icon></span>
              <a
                :href="item.link"
                class="media-link"
              >
                <span v-if="item.caption">{{ item.caption }}</span><span v-else>{{ item.originalFileName }}</span>
              </a>
              <v-btn small icon @click="showMediaDetails(item)"><v-icon style="color: #999">fas fa-info-circle</v-icon></v-btn>
            </li>
            </ul>
          </v-card-text>
        </v-card>

        <v-dialog
          v-model="mediaDetailsDialog"
          >
          <v-card>
            <v-card-title>
              <h2 class="w-100">Media Details</h2>
            </v-card-title>
            <v-card-text>
              <div v-if="currentMediaDetailItem.mediaTypeCode === 'IMG'" style="text-align: center; padding-bottom: 2em;">
                <h3 v-if="currentMediaDetailItem.caption">{{ currentMediaDetailItem.caption }}</h3>
                <img style="width: 100%;" :src="currentMediaDetailItem.link" :alt="currentMediaDetailItem.caption">
              </div>
              <ul style="overflow: auto;padding-bottom: 0.5em;">
                <li v-if="currentMediaDetailItem.originalFileName"><strong>File Name: </strong>{{ currentMediaDetailItem.originalFileName }}</li>
                <li><strong>Media Type:  </strong> {{ currentMediaDetailItem.contentType }}</li>
                <li><strong>Link:        </strong> <a :href="currentMediaDetailItem.link">{{ currentMediaDetailItem.link}}</a></li>
                <li><strong>Last Updated:</strong> {{ currentMediaDetailItem.updateDts | formatDate }}</li>
              </ul>
            </v-card-text>
            <v-card-actions>
              <v-btn @click="mediaDetailsDialog = false;">Close</v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>

      </v-expansion-panel-content>

      <v-expansion-panel-content v-if="detail.resources && detail.resources.length !== 0">
        <div slot="header">Resources</div>
        <v-card class="grey lighten-4">
          <v-card-text>
            <div v-for="item in detail.resources"
              :key="item.resourceId"
            >
              <strong >{{ item.resourceTypeDesc }}</strong>
              <v-btn flat icon :href="baseURL+item.actualLink"><v-icon>link</v-icon></v-btn>
              <div style="overflow-x: auto; white-space: nowrap;">
                <a :href="item.link" style="display: block; margin-bottom: 0.5em;">
                  <span v-if="item.description">{{ item.description }}</span><span v-else>{{ item.link }}</span>
                </a>
              </div>
            </div>
          </v-card-text>
        </v-card>
      </v-expansion-panel-content>

      <v-expansion-panel-content>
        <div slot="header">Reviews</div>
        <v-card class="grey lighten-4">
          <v-card-text>
            <p>
              <strong>Average User Rating:</strong>
              <star-rating :rating="computeAverageRating(detail)" :read-only="true" :increment="0.01" :star-size="30"></star-rating>
            </p>

            <div v-if="detail.reviews && detail.reviews.length !== 0">
              <div
                v-for="review in detail.reviews"
                :key="review.title"
              >
                <hr>
                <h2>{{ review.title }}</h2>
                <p>
                  <star-rating :rating="review.rating" :read-only="true" :increment="0.01" :star-size="30"></star-rating>
                </p>
                <p>{{ review.username + " (" + review.userTypeCode + ") - " }} {{ review.updateDate | formatDate }}</p>
                <p class="reviewPar"><strong>organization: </strong>{{ review.organization }}</p>
                <p class="reviewPar"><strong>Experience: </strong>{{ review.userTimeDescription }}</p>
                <p class="reviewPar"><strong>Last Used: </strong>{{ review.lastUsed | formatDate }}</p>
                <p class="reviewPar"><strong>Comments:</strong></p>
                <p v-html="review.comment"></p>
              </div>
            </div>
            <div v-else>
              <p>There are no reviews for this entry.</p>
            </div>
          </v-card-text>
        </v-card>
      </v-expansion-panel-content>

      <!-- TODO: do this once we're integrated with DI2E -->
      <v-expansion-panel-content v-if="detail.fullEvailation">
        <div slot="header">Evaluation</div>
        <v-card class="grey lighten-4">
          <v-card-text>

          </v-card-text>
        </v-card>
      </v-expansion-panel-content>

      <!-- TODO: allow authorized users to add tags to the entry -->
      <!-- <v-expansion-panel-content v-if="detail.tags && detail.tags.length !== 0">
        <div slot="header">Tags</div>
        <v-card class="grey lighten-4">
          <v-card-text>

            <div
              v-for="tag in detail.tags"
              :key="tag.text"
              style="margin-right: 0.8em; cursor: pointer;"
            >
              <p>
                <v-icon style="font-size: 14px;">fas fa-tag</v-icon>
                {{ tag.text }}
              </p>
            </div>

          </v-card-text>
        </v-card>
      </v-expansion-panel-content> -->

      <v-expansion-panel-content v-if="detail.contacts && detail.contacts.length !== 0">
        <div slot="header">Contacts</div>
        <v-card class="grey lighten-4">
          <v-card-text v-if="detail.contacts && detail.contacts.length > 0">
            <h2>Points of Contact</h2>
            <div
              v-for="contact in detail.contacts"
              :key="contact.name"
            >
              <hr>
              <p class="contactPar"><strong>Name: </strong>{{ contact.name }}</p>
              <p class="contactPar"><strong>Organization: </strong>{{ contact.organization }}</p>
              <p class="contactPar"><strong>Position: </strong>{{ contact.positionDescription }}</p>
              <p class="contactPar"><strong>Phone: </strong>{{ contact.phone }}</p>
              <p class="contactPar"><strong>Email: </strong>{{ contact.email }}</p>
            </div>
          </v-card-text>
          <v-card-text v-else>
            <p>There are no contacts for this entry.</p>
          </v-card-text>
        </v-card>
      </v-expansion-panel-content>

      <v-expansion-panel-content>
        <div slot="header">Watches</div>
        <v-card class="grey lighten-4">
          <v-card-text>
            <p>Watch this entry?</p>
            <!-- TODO: make watch api calls -->
            <v-switch :label="`Watch: ${watchSwitch.toString()}`" v-model="watchSwitch"></v-switch>
          </v-card-text>
        </v-card>
      </v-expansion-panel-content>

      <v-expansion-panel-content>
        <div slot="header">Questions and Answers</div>
        <v-card class="grey lighten-4">
          <v-card-text>
            <v-btn color="white" @click="askQuestionDialog = true">Ask a Question</v-btn>
            <Question v-for="question in questions" :key="question.question" @questionDeleted="deleteQuestion" :question="question"></Question>
          </v-card-text>
        </v-card>
      </v-expansion-panel-content>

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

    </v-expansion-panel>

    <LoadingOverlay v-model="isLoading"></LoadingOverlay>
  </div>
</template>

<script lang="js">
import StarRating from 'vue-star-rating';
import _ from 'lodash';
import Lightbox from './subcomponents/Lightbox';
import LoadingOverlay from './subcomponents/LoadingOverlay';
import Question from './subcomponents/Question';

export default {
  name: 'entry-detail-page',
  components: {
    StarRating,
    Lightbox,
    LoadingOverlay,
    Question
  },
  mounted () {
    if (this.$route.params.id) {
      this.id = this.$route.params.id;
    }

    this.getDetail();
    this.getQuestions();
  },
  data () {
    return {
      baseURL: '/openstorefront/',
      isLoading: true,
      askQuestionDialog: false,
      newQuestion: '',
      detail: {},
      questions: {},
      watchSwitch: false,
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
        {
          text: 'Attribute Type',
          align: 'left',
          sortable: true,
          value: 'typeDescription'
        },
        { text: 'Value', value: 'codeDescription' }
      ]

    };
  },
  methods: {
    showMediaDetails (item) {
      this.currentMediaDetailItem = item;
      this.mediaDetailsDialog = true;
    },
    getDetail () {
      this.isLoading = true;
      this.$http.get(`/openstorefront/api/v1/resource/components/${this.id}/detail`)
        .then(response => {
          this.detail = response.data;
          // parse the description for `Media.action` relative paths and change to absolute paths
          this.detail.description = this.detail.description.replace(/media\.action/i, '/openstorefront/Media.action');
          this.detail.resources.forEach(function (el) {
            el.link = el.link.replace(/resource\.action/i, '/openstorefront/Resource.action');
          });
          this.detail.componentMedia.forEach(function (el) {
            if (el.link) {
              el.link = el.link.replace(/media\.action/i, '/openstorefront/Media.action');
            }
          });
        })
        .catch(e => this.errors.push(e))
        .finally(() => {
          this.computeHasImage();
          this.filterLightboxList();
          this.isLoading = false;
        });
    },
    getQuestions () {
      this.isLoading = true;
      this.$http.get(`/openstorefront/api/v1/resource/components/${this.id}/questions`)
        .then(response => {
          this.questions = response.data;
        })
        .catch(e => this.errors.push(e));
    },
    submitQuestion () {
      let data = {
        dataSensitivity: '',
        organization: this.$store.state.currentUser.organization,
        question: this.newQuestion,
        securityMarkingType: '',
        userTypeCode: this.$store.state.currentUser.userTypeCode
      };
      this.$http.post(`/openstorefront/api/v1/resource/components/${this.id}/questions`, data)
        .then(response => {
          this.questions.push(response.data);
          this.newQuestion = '';
          this.askQuestionDialog = false;
          this.$toasted.show('Question submitted.');
        })
        .catch(e => this.$toasted.error('There was a problem submitting the question.'));
    },
    deleteQuestion (question) {
      console.log(question);
    },
    getAnswers (qid) {
      this.isLoading = true;
      this.$http.get(`/openstorefront/api/v1/resource/components/${this.id}/questions/${qid}/responses`)
        .then(response => {
          this.answers[qid] = response.data;
          this.isLoading = false;
        })
        .catch(e => this.errors.push(e));
    },
    computeAverageRating (detail) {
      var temp = 0;
      var averageRating = 0;
      if (detail.reviews) {
        for (var i = 0; i < detail.reviews.length; i++) {
          if (detail.reviews[i].rating) {
            temp += detail.reviews[i].rating;
          } else {
            return 0;
          }
        }
      } else {
        return 0;
      }
      if (detail.reviews.length !== 0) {
        averageRating = temp / detail.reviews.length;
      }
      return averageRating;
    },
    filterLightboxList () {
      if (this.detail.componentMedia) {
        this.lightboxList = _.filter(this.detail.componentMedia, function (o) {
          return o.mediaTypeCode === 'IMG' && !o.hideInDisplay;
        });
      }
    },
    computeHasImage () {
      if (this.detail.componentMedia) {
        for (var i = 0; i < this.detail.componentMedia.length; i++) {
          if (this.detail.componentMedia[i].mediaTypeCode === 'IMG') {
            this.hasImage = true;
            return;
          }
        }
      }
    }
  }
};
</script>

<style scoped lang="scss">
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
</style>

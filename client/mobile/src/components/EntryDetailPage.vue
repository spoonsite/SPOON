<template lang="html">

  <div class="entry-detail-page">
    <v-card class="black white--text text-md-center">
      <v-card-text>
        <h1>{{detail.name}}</h1>
      </v-card-text>
    </v-card>

    <!-- MEDIA carousel -->
    <div
      v-if="detail.componentMedia && detail.componentMedia.length !== 0"
      class="mediaWrapper  grey lighten-2"
      >
      <div class="mediaItem">
        <img
          v-for="(item, index) in lightboxList"
          :key="item.link"
          :src="baseURL+item.link"
          class="mediaImage elevation-4"
          @click="lightboxOn(index)"
        >
      </div>
    </div>

    <!-- LIGHTBOX Popup -->
    <div v-if="lightbox" class="lightbox">
      <div class="lightboxImageWrapper">
        <img :src="lightboxCurrentImage" class="lightboxImage elevation-3">
      </div>

      <div class="lightboxControl">
        <v-btn v-if="lightboxList.length > 1" flat icon small color="yellow" @click="lightboxPrev()">
          <v-icon dark>navigate_before</v-icon>
        </v-btn>

        <v-btn flat icon small color="yellow" style="margin-left: 2em; margin-right: 2em;" @click="lightbox = false;">
          <v-icon dark>clear</v-icon>
        </v-btn>

        <v-btn v-if="lightboxList.length > 1" flat icon small color="yellow" @click="lightboxNext()">
          <v-icon dark>navigate_next</v-icon>
        </v-btn>
      </div>
    </div>

    <v-expansion-panel popout>

      <v-expansion-panel-content>
        <div slot="header">Summary</div>
        <v-card class="grey lighten-5">
          <v-card-text>
            <p>
              <img :src="baseURL + detail.componentTypeIconUrl" width="30" >
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
        <v-card class="grey lighten-5">
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

      <v-expansion-panel-content>
        <div slot="header">Media Download</div>
        <v-card class="grey lighten-5">
          <v-card-text>
            <p
              v-for="item in detail.componentMedia"
              :key="item.componentType"
              style="overflow-x: scroll; white-space: nowrap;"
            >

              <v-btn flat icon :href="baseURL+item.link">
                <v-icon>cloud_download</v-icon>
              </v-btn>
                <strong>{{ item.contentType }}:</strong>
                {{ item.originalFileName }}
            </p>
          </v-card-text>
        </v-card>
      </v-expansion-panel-content>

      <v-expansion-panel-content v-if="detail.resources && detail.resources.length !== 0">
        <div slot="header">Resources</div>
        <v-card class="grey lighten-5">
          <v-card-text>
            <div v-for="item in detail.resources"
              :key="item.resourceType"
            >
              <p>
                <strong >{{ item.resourceTypeDesc }}</strong>
                <v-btn flat icon :href="baseURL+item.actualLink"><v-icon>link</v-icon></v-btn>
              </p>
              <a :href="item.link" style="overflow-x: scroll; white-space: nowrap;">
                {{ item.link }}
              </a>
            </div>
          </v-card-text>
        </v-card>
      </v-expansion-panel-content>

      <v-expansion-panel-content>
        <div slot="header">Reviews</div>
        <v-card class="grey lighten-5">
          <v-card-text>
            <p>
              <strong>Average User Rating:</strong>
              <star-rating :rating="computeAverageRating(detail)" :read-only="true" :increment="0.01" :star-size="30"></star-rating>
            </p>

            <div v-if="detail.review && detail.reviews.length !== 0">
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
          </v-card-text>
        </v-card>
      </v-expansion-panel-content>

      <!-- TODO: do this once we're integrated with DI2E -->
      <v-expansion-panel-content v-if="detail.fullEvailation">
        <div slot="header">Evaluation</div>
        <v-card class="grey lighten-5">
          <v-card-text>

          </v-card-text>
        </v-card>
      </v-expansion-panel-content>

      <v-expansion-panel-content v-if="detail.tags && detail.tags.length !== 0">
        <div slot="header">Tags</div>
        <v-card class="grey lighten-5">
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
      </v-expansion-panel-content>

      <v-expansion-panel-content v-if="detail.contacts !== 0">
        <div slot="header">Contacts</div>
        <v-card class="grey lighten-5">
          <v-card-text>
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
        </v-card>
      </v-expansion-panel-content>

      <v-expansion-panel-content>
        <div slot="header">Watches</div>
        <v-card class="grey lighten-5">
          <v-card-text>
            <p>Watch this entry?</p>
            <!-- TODO: make watch api calls -->
            <v-switch :label="`Watch: ${watchSwitch.toString()}`" v-model="watchSwitch"></v-switch>
          </v-card-text>
        </v-card>
      </v-expansion-panel-content>

      <!-- TODO: DO this later -->
      <v-expansion-panel-content>
        <div slot="header">Questions and Answers</div>
        <v-card class="grey lighten-5">
          <v-card-text>

          </v-card-text>
        </v-card>
      </v-expansion-panel-content>

    </v-expansion-panel>

    <div v-if="isLoading" class="overlay">
      <v-progress-circular
        color="teal"
        :size="70"
        :width="7"
        indeterminate
        class="center"
      ></v-progress-circular>
    </div>

  </div>
</template>

<script lang="js">
import StarRating from 'vue-star-rating';
import _ from 'lodash';

export default {
  name: 'entry-detail-page',
  components: {
    StarRating
  },
  mounted () {
    if (this.$route.params.id) {
      this.id = this.$route.params.id;
    }

    this.getDetail();
  },
  data () {
    return {
      baseURL: '/openstorefront/',
      isLoading: true,
      detail: {},
      watchSwitch: false,
      hasImage: false,
      lightboxList: [],
      lightbox: false,
      lightboxCurrentImage: '',
      lightboxCurrentIndex: 0,
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
    getDetail () {
      this.isLoading = true;
      this.$http.get('/openstorefront/api/v1/resource/components/' + this.id + '/detail')
        .then(response => {
          this.detail = response.data;
        })
        .catch(e => this.errors.push(e))
        .finally(() => {
          this.computeHasImage();
          this.filterLightboxList();
          this.isLoading = false;
        });
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
        this.lightboxList = _.filter(this.detail.componentMedia, { 'mediaTypeCode': 'IMG' });
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
    },
    lightboxOn (index) {
      this.lightbox = true;
      this.lightboxCurrentIndex = index;
      this.lightboxCurrentImage = this.baseURL + this.lightboxList[index].link;
      this.lightboxSetImage();
    },
    lightboxSetImage () {
      this.lightboxCurrentImage = this.baseURL + this.lightboxList[this.lightboxCurrentIndex].link;
    },
    lightboxNext () {
      this.lightboxCurrentIndex = this.lightboxList.length % (this.lightboxCurrentIndex + 1);
      this.lightboxSetImage();
    },
    lightboxPrev () {
      this.lightboxCurrentIndex = this.lightboxList.length > 0 ? this.lightboxCurrentIndex - 1 : this.lightboxList.length;
      this.lightboxSetImage();
    }
  },
  computed: {
  }
};
</script>

<style scoped lang="scss">
  // .entry-detail-page {
  // }
  .carousel {
    margin-bottom: 1em;
  }
  .center {
    z-index: 991;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }
  .contactPar {
    margin-bottom: 0.5em;
  }
  .lightbox {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 990;
    background-color: rgba(71, 71, 71, 0.7);
    pointer-events: all;
  }
  .lightboxControl {
    bottom: 0%;
    position: fixed;
    width: 100%;
    z-index: 991;
    text-align: center;
    background-color: rgba(39, 39, 39, 0.7);
  }
  .lightboxImage {
    background: #fff;
    max-width: 98%;
    max-height: 90%;
  }
  .lightboxImageWrapper {
    margin-top: 1em;
    width: 100%;
    max-height: 90%;
    position: absolute;
    top: 45%;
    left: 50%;
    transform: translate(-50%, -50%);
    text-align: center;
  }
  .mediaWrapper {
    overflow-x: scroll;
    overflow-y: hidden;
    white-space: nowrap;
    margin-bottom: 1em;
  }
  .mediaItem{
    float: left;
    display: block;
  }
  .mediaImage {
    background: #fff;
    margin: 1em;
    max-height: 8em;
  }
  .overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 990;
    background-color: rgba(255,255,255, 0.7);
    pointer-events: all;
  }
  .reviewPar {
    margin-bottom: 0.5em;
  }
  hr {
    color: #333;
    margin-bottom: 1em;
  }
</style>

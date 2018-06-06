<template lang="html">

  <section class="entry-detail-page">
    <v-card class="black white--text text-md-center">
      <v-card-text>
        <h1>{{detail.name}}</h1>
      </v-card-text>
    </v-card>

    <div
      v-if="detail.componentMedia"

    >
      <v-carousel
        v-if="detail.componentMedia.length !== 0"
        class="carousel"
      >
          <v-carousel-item
            v-for="item in detail.componentMedia"
            :key="item"
            v-if="item.contentType === 'Image'"
            :src="baseURL + item.link"
            transition="fade"
            reverse-transition="fade"
          ></v-carousel-item>
      </v-carousel>
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
              v-if="detail.tags.length !== 0"
            >
              <span
                v-for="tag in detail.tags"
                :key="tag"
                style="float: left; margin-right: 0.8em; cursor: pointer;"
              >
                <v-icon style="font-size: 14px;">fas fa-tag</v-icon>
                {{tag.text}}
              </span>
            </p>
            <h2>Details</h2>
            <hr>
            <p><strong>Organization:</strong> {{ detail.organization }}</p>
            <p>
              <strong>Average User Rating:</strong>
              <star-rating :rating="computeAverageRating(detail)" :read-only="true" :increment="0.01" :star-size="30"></star-rating>
            </p>
            <p><strong>Last Updated:</strong> {{ detail.lastActivityDts | formatDate}}</p>
            <p><strong>Approved Date:</strong> {{ detail.approvedDate | formatDate}}</p>
            <h2>Description</h2>
            <hr>
            <div v-html="detail.description"></div>
          </v-card-text>
        </v-card>
      </v-expansion-panel-content>

      <v-expansion-panel-content v-if="detail.attributes.length !== 0">
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
        <div slot="header">Reviews</div>
        <v-card class="grey lighten-5">
          <v-card-text>
            <p>
              <strong>Average User Rating:</strong>
              <star-rating :rating="computeAverageRating(detail)" :read-only="true" :increment="0.01" :star-size="30"></star-rating>
            </p>

            <div v-if="detail.reviews.length !== 0">
              <div
                v-for="review in detail.reviews"
                :key="review"
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

      <v-expansion-panel-content v-if="detail.tags.length !== 0">
        <div slot="header">Tags</div>
        <v-card class="grey lighten-5">
          <v-card-text>

            <div
              v-for="tag in detail.tags"
              :key="tag"
              style="margin-right: 0.8em; cursor: pointer;"
            >
              <p>
                <v-icon style="font-size: 14px;">fas fa-tag</v-icon>
                {{tag.text}}
              </p>
            </div>

          </v-card-text>
        </v-card>
      </v-expansion-panel-content>

      <v-expansion-panel-content v-if="detail.contacts !== 0">
        <div slot="header">Contact</div>
        <v-card class="grey lighten-5">
          <v-card-text>
            <h2>Points of Contact</h2>
            <div
              v-for="contact in detail.contacts"
              :key="contact"
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
  </section>

</template>

<script lang="js">
import StarRating from 'vue-star-rating';

export default {
  name: 'entry-detail-page',
  components: {
    StarRating
  },
  mounted () {
    if (this.$route.params.id) {
      this.id = this.$route.params.id;
    }
    this.getComponent();
    this.getDetail();
    this.computeAverageRating();
  },
  data () {
    return {
      baseURL: '/openstorefront/',
      component: {},
      detail: {},
      watchSwitch: false,
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
    getComponent () {
      let that = this;
      this.$http.get('/openstorefront/api/v1/resource/components/' + this.id)
        .then(response => {
          that.component = response.data;
        })
        .catch(e => this.errors.push(e));
    },
    getDetail () {
      let that = this;
      this.$http.get('/openstorefront/api/v1/resource/components/' + this.id + '/detail')
        .then(response => {
          that.detail = response.data;
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
  .reviewPar {
    margin-bottom: 0.5em;
  }
  .contactPar {
    margin-bottom: 0.5em;
  }
  hr {
    color: #333;
    margin-bottom: 1em;
  }
</style>

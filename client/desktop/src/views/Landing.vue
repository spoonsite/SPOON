<template lang="html">
  <div>

  <v-img
    :src='$store.state.branding.homebackSplashUrl'
    style="overflow: visible; position:relative; z-index:1"
  >
    <div class="mx-3">
      <SearchBar
        @submitSearch="submitSearch"
        v-model="searchQuery"
        :hideSuggestions="hideSearchSuggestions"
        style="margin: 6em auto; max-width: 46em;"
        :overlaySuggestions="true"
      ></SearchBar>
    </div>

    <h2>
        <span style="padding: 0.5em; background-color: white; border-radius: 2px;">
          Browse by Category
        </span>
    </h2>
    <v-container>
      <v-layout row wrap>
        <v-flex
          v-for="(item,i) in nestedComponentTypesList.children"
          class="mb-2"
          :key="i"
          xs12
          sm6
          md4
          lg3
          xl2
        >
          <v-card
            style="height: 100%;"
            class="ma-2 entry-card"
          >
            <v-card-title
              class="mb-3"
              style="border-bottom: 1px solid; height: 6em;"
            >
              <router-link
                :to="{ path: 'search', query: { comp: item.componentType.componentType, children: true }}"
                style="display: flex; align-items: stretch;"
              >
                <img
                  :src="'/openstorefront/' + item.componentType.iconUrl"
                  height="50"
                  style="vertical-align: middle;" 
                  class="icon-img pr-3"
                >
                <span>{{ item.componentType.label }}</span>
              </router-link>
            </v-card-title>
            <ul v-if="item.children.length > 0" class="ml-3">
              <li v-for="child in item.children" :key="child.componentType.componentType">
                <router-link
                  :to="{ path: 'search', query: { comp: child.componentType.componentType, children: true }}"
                >
                  {{ child.componentType.label }}
                </router-link>
              </li>
            </ul>
            <div v-else class="ml-3">No sub-categories</div>
          </v-card>
        </v-flex>
      </v-layout>
    </v-container>

    </v-img>

    <h2>
      <span style="background-color: #FAFAFA; border-radius: 1px;" class="pa-2">
      Quick Launch
      </span>
    </h2>
    <v-container text-xs-center>
      <v-layout row wrap>
        <v-flex
          v-for="(item,i) in quickLaunchLinks"
          :key="i"
          xs12
          sm4
          md4
        >
          <v-hover>
          <v-card
            :href="item.href"
            slot-scope="{ hover }"
            :class="`elevation-${hover ? 6 : 2} ma-2 pt-2 px-2`"
          >
            <v-icon class="launch-icon">fas fa-{{ item.icon }}</v-icon>
            <v-card-title primary-title>
              <v-card-text class="headline pa-0">{{ item.title }}</v-card-text>
            </v-card-title>
          </v-card>
          </v-hover>
        </v-flex>
      </v-layout>
    </v-container>

    <h2>Highlights</h2>
    <v-container
      v-for="(item,i) in highlights"
      :key="i"
    >
      <h3 class="headline pb-0 mb-0">{{ item.title }}</h3>
      <date class="mb-4 grey--text text--darken-1" style="display: block;">{{ item.updateDts | formatDate("MMMM d, YYYY") }}</date>
      <div v-html="item.description"></div>
      <v-btn dark :href="item.link">View More</v-btn>
    </v-container>

    <v-footer color="primary" dark height="auto">
      <v-card color="primary" dark flat class="footer-wrapper">
        <div class="footer-block" v-html="$store.state.branding.landingPageFooter"></div>
        <p style="text-align: center;" v-html="$store.state.appVersion"></p>
      </v-card>
    </v-footer>

  </div>
</template>

<script lang="js">
import SearchBar from '../components/SearchBar'

export default {
  name: 'landing-page',
  components: {
    SearchBar
  },
  props: [],
  mounted () {
    this.getNestedComponentTypes()
    this.getHighlights()
    this.getAttributes()
  },
  data () {
    return {
      searchQuery: '',
      nestedComponentTypesList: [],
      errors: [],
      highlights: [],
      attributes: [],
      quickLaunchLinks: [
        {
          img: '/openstorefront/images/dash.png',
          href: '/openstorefront/UserTool.action?load=Dashboard',
          title: 'Dashboard',
          icon: 'chart-line'
        },
        {
          img: '/openstorefront/images/submission.png',
          href: '/openstorefront/UserTool.action?load=Submissions',
          title: 'Submit a SmallSat part',
          icon: 'file-upload'
        },
        {
          img: '/openstorefront/images/dash.png',
          href: '#/contact', // we have a feedback page in client/mobile
          title: 'Feedback',
          icon: 'comments'
        }
      ]
    }
  },
  methods: {
    link (query) {
      return `/search?q=${query}`
    },
    submitSearch (additionalOptions) {
      this.$router.push(encodeURI(`/search?q=${this.searchQuery}`) + additionalOptions)
    },
    getNestedComponentTypes () {
      this.$http
        .get(
          '/openstorefront/api/v1/resource/componenttypes/nested'
        )
        .then(response => {
          this.nestedComponentTypesList = response.data
        })
    },
    action (type) {
      alert(type)// switch on the type of action
    },
    getHighlights () {
      this.$http
        .get('/openstorefront/api/v1/resource/highlights')
        .then(response => {
          this.highlights = response.data
        })
    },
    getAttributes () {
      this.$http
        .get('/openstorefront/api/v1/resource/attributes?important=true&page=1&start=0&limit=25')
        .then(response => {
          this.attributes = response.data
        })
    },
    isSpoon () {
      return this.$store.state.branding.applicationName === 'SPOON'
    }
  },
  computed: {
    hideSearchSuggestions () {
      return this.searchQuery.length === 0
    }
  }
}
</script>

<style scoped lang="scss">
h2 {
  text-align: center;
  font-size: 2em;
  margin-bottom: 0;
  margin-top: 3em;
}
h3 {
  margin-bottom: 1em;
}
.footer-wrapper {
  width: 100%;
}
.footer-block a {
  color: white;
  text-decoration: none;
}
.launch-icon {
  color:#333 !important;
  font-size: 64px;
}
</style>

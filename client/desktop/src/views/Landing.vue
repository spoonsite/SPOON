<template lang="html">
  <div>

  <v-img
    :src='$store.state.branding.homebackSplashUrl'
    style="overflow: visible; position:relative; z-index:1"
  >

  <div class="text-right pt-2">
    <div style="display: inline-block; background:#01080D; border-radius: 8px;" class="pa-1 pl-3">
      <span class="display-1 white--text" style="vertical-align: middle;">
        Are you a vendor?
      </span>
      <v-btn color="success" large href="/openstorefront/UserTool.action?load=Submissions">
        Submit a part
      </v-btn>
    </div>
  </div>

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
        <span class="pa-2" style="color: #F8C533; background-color:#060B13; border-radius: 2px;">
          Browse by Category
        </span>
    </h2>
    <v-container>
      <v-layout row wrap justify-center>
        <v-flex
          v-for="(item,i) in nestedComponentTypesList.children.filter(item => item.children.length > 0)"
          class="mb-3"
          :key="i"
          xs12
          sm6
          md4
          xl3
        >
          <v-card
            style="height: 100%;"
            class="mx-2 category-card"
          >
            <router-link
              :to="{ path: 'search', query: { comp: item.componentType.componentType, children: true }}"
              style="display: flex; align-items: center; min-height: 6em;"
              class="pa-2"
            >
              <img
                :src="'/openstorefront/' + item.componentType.iconUrl"
                height="50"
                class="pr-3"
              >
              <span class="headline" style="vertical-align: top;">{{ item.componentType.label }}</span>
            </router-link>
            <v-divider class="d-xs-none"></v-divider>
            <ul class="d-xs-none">
              <li v-for="child in item.children" :key="child.componentType.componentType">
                <router-link
                  :to="{ path: 'search', query: { comp: child.componentType.componentType, children: true }}"
                  class="title font-weight-regular"
                >
                  {{ child.componentType.label }}
                </router-link>
              </li>
            </ul>
          </v-card>
        </v-flex>
      </v-layout>
    </v-container>

    </v-img>

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
.category-card {
  a {
    text-decoration: none;
  }
  a:hover {
    background-color: rgba(0,0,0,0.05);
  }
  ul {
    margin: 0;
    padding: 0;
  }
  li {
    list-style: none;
    margin: 0;
    padding: 0;
  }
  li a {
    width: 100%;
    display: block;
    padding-top: 0.5em;
    padding-bottom: 0.5em;
    padding-left: 2em;
  }
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
.submission-btn {
  text-decoration: none;
  background: #212121;
  color: white;
  border-radius: 8px;
  padding: 14px;
  font-size: 2em;
  text-transform: uppercase;
}
.text-right {
  text-align: right;
}
@media only screen and (max-width: 800px) {
  .d-xs-none {
    display: none;
  }
}
</style>

<template lang="html">
  <div>

  <v-img
    :src='$store.state.branding.homebackSplashUrl'
  >
    <div class="mx-3">
      <SearchBar
        v-on:submitSearch="submitSearch()"
        v-model="searchQuery"
        :hideSuggestions="hideSearchSuggestions"
        style="margin: 6em auto; max-width: 46em;"
        :overlaySuggestions="true"
      ></SearchBar>
    </div>

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
            slot-scope="{ hover }"
            :class="`elevation-${hover ? 12 : 2} ma-2 pt-2 px-2`"
          >
          <a :href="item.href" class="" style="text-decoration: none;">
            <v-icon class="launch-icon">fas fa-{{ item.icon }}</v-icon>
            <v-card-title primary-title>
              <v-card-text class="headline pa-0">{{ item.title }}</v-card-text>
            </v-card-title>
          </a>
          </v-card>
          </v-hover>
        </v-flex>
      </v-layout>
    </v-container>

    </v-img>

      <h2>
          Browse Topics
      </h2>
      <v-container v-if="isSpoon()" text-xs-center>
        <v-layout row wrap>
          <v-flex
            v-for="(item,i) in nestedComponentTypesList.children"
            class="mb-2"
            :key="i"
            xs6
            sm4
            md2
          >
            <v-hover>
            <v-card
              slot-scope="{ hover }"
              flat
              :class="`elevation-${hover ? 8 : 0} ma-2 pt-2 px-2`"
              style="background-color: rgba(0,0,0,0);"
            >
              <router-link
                :to="{ path: 'search', query: { comp: item.componentType.componentType, children: true }}"
                style="width: 100%; text-decoration: none;"
              >
                <img :src="'/openstorefront/' + item.componentType.iconUrl" width="50" class="icon-img">
                <v-card-text>{{ item.componentType.label }}</v-card-text>
              </router-link>
            </v-card>
            </v-hover>
          </v-flex>
        </v-layout>
      </v-container>

  <v-container>
      <h2>Highlights</h2>
      <v-container>
        <v-flex xs10 offset-xs1>
          <v-carousel
            class="elevation-3 mt-4"
            height="500"
            light
            hide-delimiters
            :cycle="false"
          >
            <v-carousel-item
              v-for="(item,i) in highlights"
              :key="i"
              light
            >
              <div slot="default" class="px-5 py-3" style="height: 500px; overflow: auto;">
                <h3 class="headline" style="text-align: center;">{{ item.title }}</h3>
                <span v-html="item.description"></span>
                <v-btn dark :href="item.link">View More</v-btn>
                <div style="height: 50px;"></div>
              </div>
            </v-carousel-item>
          </v-carousel>
        </v-flex>
      </v-container>

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
      searchToolLinks: [
        {
          icon: 'cloud',
          href: '#', // open tag cloud
          title: 'Tags'
        },
        {
          icon: 'sitemap',
          href: '#', // search by organization popup selector
          title: 'Organizations'
        },
        {
          icon: 'share-alt',
          href: '#', // relationship view picker popup -> /openstorefront/UserTool.action?load=Relationships&viewType={type}&entityId={id}&entityName={name}
          title: 'Relationships'
        },
        {
          icon: 'search-plus',
          href: '#', // advanced search creator popup
          title: 'Advanced'
        }
      ],
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
          title: 'Submissions',
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
    submitSearch () {
      this.$router.push(`/search?q=${this.searchQuery}`)
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
.shadow {
  box-shadow: 0 3px 1px -2px rgba(0,0,0,.2),0 2px 2px 0 rgba(0,0,0,.14),0 1px 5px 0 rgba(0,0,0,.12);
}
.link-tile:hover {
  background-color: rgba(0,0,0,0.2);
}
.logo-wrapper {
  max-width: 100%;
  text-align: center;
  margin-bottom: 2em;
}
.logo {
  padding: 2em;
  max-width: 100%;
}
.footer-wrapper {
  width: 100%;
}
.footer-block a {
  color: white;
  text-decoration: none;
}
.action-btn {
  border: none;
}
.dim:hover {
  /* box-shadow: inset 0 0 100px 100px rgba(255, 255, 255, 0.1); */
  filter: brightness(130%);
}
.darken {
  background-color:rgba(0,0,0,.1);
}
.launch-icon {
  color:#333 !important;
  font-size: 64px;
}
</style>

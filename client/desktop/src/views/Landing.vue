<template lang="html">
  <div>

  <v-img
    :src='$store.state.branding.homebackSplashUrl'
  >
    <SearchBar
      v-on:submitSearch="submitSearch()"
      v-model="searchQuery"
      :hideSuggestions="hideSearchSuggestions"
      style="margin: 6em auto; max-width: 46em;"
      :overlaySuggestions="true"
    ></SearchBar>

      <h2><span style="background-color: #FAFAFA; border-radius: 1px;" class="pa-2">Browse Topics</span></h2>
      <!-- Different for SPOON and DI2E -->

      <!-- SPOON -->
      <v-container v-if="isSpoon()" text-xs-center>
        <v-layout row wrap>
          <v-flex
            v-for="(item,i) in nestedComponentTypesList.children"
            :key="i"
            xs6
            md2
          >
            <v-hover>
            <v-card
              slot-scope="{ hover }"
              flat
              width="150"
              height="170"
              :class="`elevation-${hover ? 8 : 0} ma-2 pt-2 px-2`"
              style="background-color: #FAFAFA;"
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

      <!-- DI2E -->
      <v-container v-else>
        <v-layout row wrap justify-center>
          <v-flex
            v-for="(item,i) in attributes"
            :key="i"
            xs6
            md4
          >
            <v-card class="ma-2 elevation-2">
              <v-toolbar color="primary" dark dense>
                <v-toolbar-title>{{ item.description }}</v-toolbar-title>
              </v-toolbar>
              <v-list dense style="height: 16em; overflow: auto;">
                <v-hover
                  v-for="code in item.codes"
                  :key="code.code"
                >
                <!-- TODO: hit search page with the item -->
                <a
                  href="#"
                  slot-scope="{ hover }"
                  style="text-decoration:none;"
                >
                  <v-list-tile
                  :class="`${hover ? 'darken' : 0}`"
                  >
                    <v-list-tile-content v-html="code.label">
                    </v-list-tile-content>
                  </v-list-tile>
                </a>
                </v-hover>
              </v-list>

            </v-card>
          </v-flex>
        </v-layout>
      </v-container>

    </v-img>

      <h2>Search Tools</h2>

      <v-container text-xs-center mb-5>
        <v-layout row wrap>
          <v-flex
            v-for="(item,i) in searchToolLinks"
            :key="i"
            xs
            xs6
            sm3
          >
            <v-hover>
            <v-card
              slot-scope="{ hover }"
              :class="`elevation-${hover ? 12 : 2} ma-2`"
              dark
            >
              <button @click="action(item.title)" class="action-btn pa-4 primary" style="width: 100%;">
                <i :class="'fas fa-6x fa-' + item.icon"></i>
                <v-card-title primary-title>
                  <v-card-text class="headline pa-0">{{ item.title }}</v-card-text>
                </v-card-title>
              </button>
            </v-card>
            </v-hover>
          </v-flex>
        </v-layout>
      </v-container>

      <h2>Quick Launch</h2>

      <v-container text-xs-center>
        <v-layout row wrap>
          <v-flex
            v-for="(item,i) in quickLaunchLinks"
            :key="i"
            xs6
            md3
          >
            <v-hover>
            <v-card
              slot-scope="{ hover }"
              :class="`elevation-${hover ? 12 : 2} ma-2 pt-2 px-2`">
            <a :href="item.href" class="" style="text-decoration: none;">
              <img
                :src="item.img"
                width="100%"
              />
              <v-card-title primary-title>
                <v-card-text class="headline pa-0">{{ item.title }}</v-card-text>
              </v-card-title>
            </a>
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
          title: 'Dashboard'
        },
        {
          img: '/openstorefront/images/submission.png',
          href: '/openstorefront/UserTool.action?load=Submissions',
          title: 'Submissions'
        },
        {
          img: '/openstorefront/images/savedsearch.png',
          href: '/openstorefront/UserTool.action?load=Searches',
          title: 'My Searches'
        },
        {
          img: '/openstorefront/images/dash.png',
          href: '/openstorefront/feedback.jsp', // we have a feedback page in client/mobile
          title: 'Feedback'
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
      // return this.$store.state.branding.applicationName === 'SPOON'
      return false
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
</style>

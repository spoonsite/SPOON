<template lang="html">
  <div>

  <v-container>

    <SearchBar
      v-on:submitSearch="submitSearch()"
      v-model="searchQuery"
      :hideSuggestions="hideSearchSuggestions"
      class="my-3 mb-5"
      style="margin-left: auto; margin-right: auto; max-width: 46em;"
    ></SearchBar>

      <h2>Search Tools</h2>

      <v-container text-xs-center>
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
              <div slot="default" class="pa-5" style="height: 500px; overflow: auto;">
                <h3 class="headline" style="text-align: center;">{{ item.title }}</h3>
                <span v-html="item.description"></span>
                <v-btn dark :href="item.link">View More</v-btn>
                <div style="height: 50px;"></div>
              </div>
            </v-carousel-item>
          </v-carousel>
        </v-flex>
      </v-container>

      <h2>Browse Topics</h2>
      <!-- Different for SPOON and DI2E -->

      <v-container text-xs-center>
        <v-layout row wrap>
          <v-flex
            v-for="(item,i) in nestedComponentTypesList.children"
            :key="i"
            xs6
            md3
          >
            <v-hover>
            <v-card
              slot-scope="{ hover }"
              flat
              :class="`elevation-${hover ? 8 : 0} ma-2 pt-2 px-2`"
              style="background-color: #FAFAFA;"
            >
              <router-link
                :to="{ path: 'search', query: { comp: item.componentType.componentType, children: true }}"
                style="width: 100%; text-decoration: none;"
              >
                <img :src="'/openstorefront/' + item.componentType.iconUrl" width="80" class="icon-img">
                <v-card-title>
                  <v-card-text>{{ item.componentType.label }}</v-card-text>
                </v-card-title>
              </router-link>
            </v-card>
            </v-hover>
          </v-flex>
        </v-layout>
      </v-container>

  </v-container>

    <v-footer color="primary" dark height="auto">
      <v-card color="primary" dark flat class="footer-wrapper">
        <div v-html="$store.state.branding.landingPageFooter"></div>
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
  },
  data () {
    return {
      searchQuery: '',
      nestedComponentTypesList: [],
      errors: [],
      highlights: [],
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
  margin-top: 1em;
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
.action-btn {
  border: none;
}
.dim:hover {
  /* box-shadow: inset 0 0 100px 100px rgba(255, 255, 255, 0.1); */
  filter: brightness(130%);
}
.darken:hover {
  filter: brightness(95%);
}
</style>

<template lang="html">
  <div>
    <v-img :src="$store.state.branding.homebackSplashUrl" style="overflow: visible; position:relative; z-index:1">
      <div class="text-right pt-2">
        <div style="display: inline-block; background:#01080D; border-radius: 8px;" class="pa-1 pl-3">
          <span class="title white--text" style="vertical-align: middle;">
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
      <div class="d-flex justify-center">
        <v-btn
          class="ma-2"
          color="black"
          dark
          x-large
          @click="getRecentActivity()"
          style="font-weight: bold; text-transform: none; font-size: 2rem; letter-spacing: 0;">
          {{ showRecentActivity ?'Hide Recent Activity':'Show Recent Activity'}}
        <v-icon v-if="!showRecentActivity" right>fas fa-chevron-up</v-icon>
        <v-icon v-else right>fas fa-chevron-down</v-icon>
      </v-btn>
    </div>
      <v-container v-if="showRecentActivity">
        <v-layout row wrap justify-center>
          <v-flex v-for="(item, i) in recentActivityData" class="mb-3" :key="i" xs12 sm6 md4 xl3>
            <v-card style="height: 100%;" class="mx-2 recent-activity-card d-flex flex-column">
              <div
                style="background-color: #3C3C3C;color: white; display: flex; align-items: center; min-height: 6em;"
                class="pa-2"
              >
                <div
                  class="mr-3 ml-1 pa-2"
                  style="height: 70; width: 70; display: flex; background-color: white; border-radius: 50%;"
                >
                  <v-icon x-large color="black" class="pa-1">fas fa-{{item.img}}</v-icon>
                </div>
                <span class="headline" style="vertical-align: top;">{{item.title}}</span>
              </div>
              <v-divider class="d-xs-none"></v-divider>
              <v-layout row justify-center align-center v-if="isLoading" style="height:100%;" class="d-flex flex-column">
                <v-flex xs1>
                  <v-progress-circular color="primary" :size="60" :width="6" indeterminate class="spinner"></v-progress-circular>
                </v-flex>
              </v-layout>
              <div v-else>
                <table v-if="item.title ==='Submissions'">
                  <th class="title font-weight-bold">Entry Name</th>
                  <th class="title font-weight-bold">Status</th>
                  <th class="title font-weight-bold">Actions</th>
                  <tr v-for="component in submissionData.slice(0,5)" :key="component.componentName">
                    <td class="title font-weight-regular">{{component.name}}</td>
                    <td v-if="component.approvalState === 'A'" class="title font-weight-regular">Active</td>
                    <td v-else-if="component.approvalState === 'P'" class="title font-weight-regular">Pending</td>
                    <td v-else class="title font-weight-regular">Not Submitted</td>
                    <td v-if="component.approvalState === 'N'" style="text-align: center;">
                      <v-btn icon>
                        <v-icon>fas fa-pencil-alt</v-icon>
                      </v-btn>
                    </td>
                    <td v-else class="title font-weight-regular" style="text-align: center;">
                      <v-btn
                      icon
                      :to="{ name: 'Entry Detail', params: { id: component.componentId } }"
                    >
                      <v-icon>fas fa-eye</v-icon>
                    </v-btn>
                    </td>
                  </tr>
                </table>
                <table v-if="item.title ==='Watches'">
                  <th class="title font-weight-bold">Entry Name</th>
                  <th class="title font-weight-bold">Updated</th>
                  <tr
                    v-for="watch in watchesData.slice(0,6)"
                    :key="watch.componentName"
                    @click="goToWatchedPage(watch)"
                    style="cursor: pointer;"
                    class="title font-weight-regular"
                  >
                    <td class="pa-1">{{watch.componentName}}</td>
                    <td
                      v-if="watch.lastUpdateDts > watch.lastViewDts"
                      style="text-align: center;"
                      class="pa-1"
                    >
                      <v-icon color="success">fas fa-check</v-icon>
                    </td>
                    <td v-else></td>
                  </tr>
                </table>
              </div>
              <v-spacer />
              <div class="d-flex flex-row">
                <v-spacer />
                <v-btn
                  class="ma-4"
                  v-if="item.title ==='Submissions'"
                  :to="{ name: 'Submissions' }"
                >
                  Manage
                </v-btn>
                <v-btn
                  class="ma-4"
                  v-else
                  :to="{ name: 'Watches' }"
                >
                  Manage
                </v-btn>
              </div>
            </v-card>
          </v-flex>
        </v-layout>
      </v-container>
      <h2>
        <span class="pa-2" style="color: white; background-color:#060B13; border-radius: 2px;">
          Browse by Category
        </span>
      </h2>
      <v-container>
        <v-layout row wrap justify-center>
          <v-flex v-for="(item, i) in filteredComponentList" class="mb-3" :key="i" xs12 sm6 md4 xl3>
            <v-card style="height: 100%;" class="mx-2 category-card">
              <router-link
                :to="{ path: 'search', query: { comp: item.componentType.componentType, children: true } }"
                style="background-color: #3C3C3C;color: white; display: flex; align-items: center; min-height: 6em;"
                class="pa-2"
              >
                <div
                  class="mr-3 ml-1 pa-2"
                  style="height: 70; width: 70; display: flex; background-color: white; border-radius: 50%;"
                >
                  <img :src="'/openstorefront/' + item.componentType.iconUrl" height="50" width="50" class="pa-1" />
                </div>
                <span class="headline" style="vertical-align: top;">{{ item.componentType.label }}</span>
              </router-link>
              <v-divider class="d-xs-none"></v-divider>
              <ul class="d-xs-none">
                <li v-for="child in item.children" :key="child.componentType.componentType">
                  <router-link
                    :to="{ path: 'search', query: { comp: child.componentType.componentType, children: true } }"
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
    <v-container v-for="(item, i) in highlights" :key="i">
      <h3 class="headline pb-0 mb-0">{{ item.title }}</h3>
      <time class="mb-4 grey--text text--darken-1" style="display: block;">{{
        item.updateDts | formatDate('LLLL d, yyyy')
      }}</time>
      <div v-html="item.description"></div>
      <v-btn dark :href="item.link">View More</v-btn>
    </v-container>

    <DisclaimerModal v-model="showDisclaimer" @close="showDisclaimer = false"></DisclaimerModal>

    <v-footer color="primary" dark height="auto">
      <v-card color="primary" dark flat class="footer-wrapper">
        <div class="footer-block" v-html="$store.state.branding.landingPageFooter"></div>
        <div style="display: flex; align-items: center; justify-content: center;">
          <p style="text-align: center;" class="ma-0" v-html="$store.state.appVersion"></p>
          <v-btn dark color="grey darken-3" class="ml-4" @click.stop="showDisclaimer = true">Disclaimer</v-btn>
        </div>
      </v-card>
    </v-footer>
  </div>
</template>

<script lang="js">
import SearchBar from '@/components/SearchBar'
import DisclaimerModal from '@/components/DisclaimerModal'

export default {
  name: 'landing-page',
  components: {
    SearchBar,
    DisclaimerModal
  },
  mounted() {
    this.getNestedComponentTypes()
    this.getHighlights()
    this.getAttributes()
    this.getSubmissionData()
    if (this.$store.state.currentUser.username) {
      this.getWatchesData()
    } else {
      this.$store.watch(
        (state, getters) => state.currentUser,
        (newValue, oldValue) => {
          this.userEmail = this.$store.state.currentUser.email
          this.getWatchesData()
        }
      )
    }
    this.showRecentActivity = JSON.parse(window.localStorage.getItem('showRecentActivity'))
  },
  data() {
    return {
      searchQuery: '',
      nestedComponentTypesList: [],
      errors: [],
      highlights: [],
      attributes: [],
      submissionData: [],
      watchesData: [],
      showDisclaimer: false,
      showRecentActivity: false,
      isLoading: false,
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
      ],
      recentActivityData: [
        {
          title: 'Submissions',
          img: 'list'
        },
        {
          title: 'Watches',
          img: 'binoculars'
        }
      ]
    }
  },
  methods: {
    link(query) {
      return `/search?q=${query}`
    },
    submitSearch(additionalOptions) {
      this.$router.push(encodeURI(`/search?q=${this.searchQuery}`) + additionalOptions)
    },
    getNestedComponentTypes() {
      this.$http
        .get(
          '/openstorefront/api/v1/resource/componenttypes/nested'
        )
        .then(response => {
          this.nestedComponentTypesList = response.data
        })
    },
    action(type) {
      alert(type)// switch on the type of action
    },
    getHighlights() {
      this.$http
        .get('/openstorefront/api/v1/resource/highlights')
        .then(response => {
          this.highlights = response.data
        })
    },
    getAttributes() {
      this.$http
        .get('/openstorefront/api/v1/resource/attributes?important=true&page=1&start=0&limit=25')
        .then(response => {
          this.attributes = response.data
        })
    },
    isSpoon() {
      return this.$store.state.branding.applicationName === 'SPOON'
    },
    getRecentActivity() {
      this.showRecentActivity ? this.showRecentActivity = false : this.showRecentActivity = true
      window.localStorage.setItem('showRecentActivity', JSON.stringify(this.showRecentActivity))
    },
    getSubmissionData() {
      this.isLoading = true
      this.$http.get('/openstorefront/api/v1/resource/componentsubmissions')
        .then(response => {
          this.submissionData = response.data
          this.submissionData.sort((a, b) => (a.updateDts < b.updateDts ? 1 : -1))
          this.isLoading = false
        }).catch(error => {
          this.errors.push(error)
          this.isLoading = false
        })
    },
    getWatchesData() {
      this.$http.get('/openstorefront/api/v1/resource/userprofiles/' + this.$store.state.currentUser.username + '/watches')
        .then(response => {
          this.watchesData = response.data
          this.watchesData.sort((a, b) => (a.lastUpdateDts < b.lastUpdateDts ? 1 : -1))
          for (var i = this.watchesData.length - 1; i > 0; i--) {
            if (this.watchesData[i].lastUpdateDts > this.watchesData[i].lastViewDts) {
              this.watchesData.unshift(this.watchesData.splice(i, 1)[0])
            }
          }
        })
    },
    goToWatchedPage(watch) {
      this.$router.push({ name: 'Entry Detail', params: { id: watch.componentId } })
    }
  },
  computed: {
    hideSearchSuggestions() {
      return this.searchQuery.length === 0
    },
    filteredComponentList() {
      if (this.nestedComponentTypesList && this.nestedComponentTypesList.children) {
        return this.nestedComponentTypesList.children.filter(item => item.children.length > 0)
      } else {
        return []
      }
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
    background-color: rgba(0, 0, 0, 0.05);
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
.recent-activity-card {
  a {
    text-decoration: none;
  }
  table {
    border-collapse: collapse;
  }
  td {
    padding-left: 0.5em;
  }
  tr:hover {
    cursor: default;
    background-color: rgba(0, 0, 0, 0.05);
  }
  th {
    padding: 0.5em;
  }
  table {
    width: 100%;
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
  color: #333 !important;
  font-size: 64px;
}
.submission-btn {
  text-decoration: none;
  background: #212121;
  color: white;
  padding: 14px;
  font-size: 2em;
  text-transform: uppercase;
}
.text-right {
  text-align: right;
}
@media only screen and (max-width: 598px) {
  .d-xs-none {
    display: none;
  }
}
</style>

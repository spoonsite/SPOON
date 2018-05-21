<template>
<div class="mx-3 mt-4">
  <form v-on:submit.prevent="newSearch()"  class="clearfix">
    <div class="searchbar">
      <input v-model="searchQuery" class="searchfield" type="text" placeholder="Search">
      <v-icon class="search-icon" @click="newSearch()">search</v-icon>
    </div>

    <div style="display: inline-block; float: right;">
      <v-btn small @click="show = !show" flat icon>
        <v-icon style="font-size: 1.2em;">fas fa-filter</v-icon>
      </v-btn>
      <v-btn small flat @click.stop="showOptions = true" icon><v-icon style="font-size: 1.2em;">fas fa-cog</v-icon>
      </v-btn>
    </div>

    <!-- Search Options Dialog -->
    <v-dialog
      v-model="showOptions"
      max-width="300px"
      >
      <v-card>
        <v-card-title>
          <h2>Search Options</h2>
          <v-spacer></v-spacer>
          <v-btn @click.stop="showOptions = false" icon>
            <v-icon>close</v-icon>
          </v-btn>
        </v-card-title>
        <div style="margin-left: 2em; margin-right: 1em;">
          <h3>Sort Order</h3>
          <v-radio-group v-model="searchSortOrder">
            <v-radio label="Ascending" value="ASC"></v-radio>
            <v-radio label="Descending" value="DESC"></v-radio>
          </v-radio-group>
          <h3>Sort by:</h3>
          <v-radio-group v-model="searchSortField">
            <v-radio label="Search Relevance" value="searchScore"></v-radio>
            <v-radio label="Description" value="description"></v-radio>
            <v-radio label="Name" value="name"></v-radio>
          </v-radio-group>
          <h3>Page Size</h3>
          {{ searchPageSize }}
          <v-slider v-model="searchPageSize" step="5" min="5" thumb-label></v-slider>
          <!-- <v-checkbox v-model="showAll" label="Show all search results"></v-checkbox> -->
          <v-btn @click="resetOptions()">Reset Options</v-btn>
        </div>
      </v-card>
    </v-dialog>

    <!-- Filter pills if there are any -->
    <v-btn small @click="clear()" v-if="filters.component !== '' || filters.organizations.length !== 0 || filters.tags.length !== 0">Clear Filters</v-btn>
    <div style="padding: 0 0.5em 0.8em 0.8em;">
      <span v-if="filters.component !== ''"><v-chip close small @input="filters.component = ''" color="teal lighten-2" text-color="white">{{ filters.component }}</v-chip></span>
      <span v-if="filters.tags.length !== 0"><v-chip v-for="tag in filters.tags" :key="tag" close small @input="deleteTag(tag)">{{ tag }}</v-chip></span>
      <span v-if="filters.organizations.length !== 0"><v-chip v-for="org in filters.organizations" :key="org" close small color="indigo lighten-2" text-color="white" @input="deleteOrg(org)">{{ org }}</v-chip></span>
    </div>

    <!-- Search Filters Dialog -->
    <v-dialog
      v-model="show"
      max-width="300px"
      >
      <v-card>
        <v-card-title>
          <h2>Search Filters</h2>
          <v-spacer></v-spacer>
          <v-btn @click.stop="show = false" icon>
            <v-icon>close</v-icon>
          </v-btn>
        </v-card-title>
        <v-card-text>
        <v-select
          v-model="filters.component"
          :items="components"
          label="Component Type"
        ></v-select>
        <v-select
          v-model="filters.tags"
          :items="tags"
          label="Tags"
          multiple
          chips
        ></v-select>
        <v-select
          v-model="filters.organizations"
          :items="organizations"
          label="Organizations"
          multiple
          chips
        ></v-select>
        </v-card-text>
      </v-card>
    </v-dialog>

  </form>

  <!-- Search Results -->
  <h2 v-if="searchResults.data" style="text-align: center">Search Results</h2>

  <p v-if="searchResults.data && searchResults.data.totalNumber === 0">No Search Results</p>
  <p v-else-if="searchResults.data">
    <span v-if="searchQueryIsDirty">Fetching</span><span v-else>Showing</span>
    {{ offset + 1 }} -
    {{ totalSearchResults > offset + searchPageSize ? offset + searchPageSize : totalSearchResults }}
    of
    {{ searchResults.data.totalNumber }} results
  </p>

  <div v-if="searchResults.data" style="margin-bottom: 1em; padding-bottom: 0.5em; overflow: auto; white-space: nowrap;">
    <v-chip v-for="stat in searchResults.data.resultTypeStats" :key="stat" @click="searchCategory(stat.componentType)" small color="teal" text-color="white">
      <v-avatar size="24px !important" class="teal darken-2">{{ stat.count }}</v-avatar>
      {{ stat.componentTypeDescription }}
    </v-chip>
  </div>

  <div style="width: 100%; text-align: center">
    <v-progress-circular
      v-if="searchQueryIsDirty"
      indeterminate color="primary"
    ></v-progress-circular>
  </div>

  <div v-if="searchResults.data">
    <v-expansion-panel>
      <v-expansion-panel-content style="grey" v-for="item in searchResults.data.data" :key="item">
        <div slot="header">
          <div style="float: left;" v-if="item.componentTypeIconUrl">
            <img :src="'/openstorefront/' + item.componentTypeIconUrl" width="30" style="margin-right: 1em;">
          </div>
          <div>
            {{ item.name }}
          </div>
        </div>
        <v-card>
          <v-card-text>
            <div
            style="float: left; margin-bottom: 0.5em;"
            v-for="(comp, i) in item.componentTypeDescription.split('>')"
            :key="comp"
            >
              <router-link
                :to="{ path: 'search', query: { comp: item.componentType }}">
                {{ comp }}
              </router-link>
              <v-icon
              v-if="i + 1 < item.componentTypeDescription.split('>').length">
                chevron_right
              </v-icon>
            </div>
            <table class="table">
              <tbody>
                <tr>
                  <td>Organization:</td>
                  <td>{{ item.organization }}</td>
                </tr>
                <tr>
                  <td>Last Updated:</td>
                  <td>{{ item.updateDts | formatDate }}</td>
                </tr>
              </tbody>
            </table>
            <!-- TODO: display more details and a link to the details page for that component -->
          </v-card-text>
          <v-card-text v-html="item.description">
          </v-card-text>
          <v-card-actions>
            <v-btn color="info">More Information</v-btn>
          </v-card-actions>
        </v-card>
      </v-expansion-panel-content>
    </v-expansion-panel>
  </div>

  <div v-if="errors.length > 0">
    <ul>
      <li v-for="error in errors" :key="error">
        {{ error }}
      </li>
    </ul>
  </div>

  <!-- Pagination -->
  <div class="pagination">
    <v-btn
      flat
      icon
      v-if="offset > 0" @click="prevPage()">
    <v-icon x-large style="color: #333;">chevron_left</v-icon>
    </v-btn>
    <button
      class="pageButton"
      v-bind:class="{activePage: searchPage === i - 1}"
      v-for="i in getPagination(searchPage)"
      :key="i"
      @click="getPage(i-1)">{{ i }}</button>
    <v-btn
      flat
      icon
      v-if="offset + searchPageSize < totalSearchResults" @click="nextPage()">
      <v-icon x-large style="color: #333;">chevron_right</v-icon>
    </v-btn>
  </div>

  <div class="v-spacer"></div>

</div>
</template>

<script>
import _ from 'lodash'
import axios from 'axios'

export default {
  name: 'SearchPage',
  mounted () {
    if (this.$route.query.q) {
      this.searchQuery = this.$route.query.q
    }
    if (this.$route.query.comp) {
      this.filters.component = this.$route.query.comp
    }
    this.newSearch()
  },
  beforeRouteUpdate (to, from, next) {
    this.searchQuery = to.query.q
    this.filters.component = to.query.comp
    this.newSearch()
  },
  methods: {
    clear () {
      this.filters = {
        component: '',
        tags: [],
        organizations: []
      }
    },
    resetOptions () {
      this.searchPageSize = 10
      this.searchSortField = 'searchScore'
      this.searchSortOrder = 'DESC'
    },
    deleteTag (tag) {
      this.filters.tags = _.remove(this.filters.tags, n => n !== tag)
    },
    deleteOrg (org) {
      this.filters.organizations = _.remove(this.filters.organizations, n => n !== org)
    },
    submitSearch (componentType) {
      this.searchQueryIsDirty = true
      let that = this
      let searchElements = [
        {
          mergeCondition: 'AND',
          searchType: 'INDEX',
          value: '***'
        }
      ]

      if (that.filters.component && that.searchQuery) {
        searchElements = [
          {
            mergeCondition: 'AND',
            searchType: 'INDEX',
            value: that.searchQuery.trim() ? `*${that.searchQuery}*` : '***'
          },
          {
            caseInsensitive: false,
            field: 'componentType',
            mergeCondition: 'AND',
            searchType: 'COMPONENT',
            stringOperation: 'EQUALS',
            value: that.filters.component
          }
        ]
      } else if (that.filters.component) {
        searchElements = [
          {
            mergeCondition: 'AND',
            searchType: 'INDEX',
            value: '***'
          },
          {
            caseInsensitive: false,
            field: 'componentType',
            mergeCondition: 'AND',
            searchType: 'COMPONENT',
            stringOperation: 'EQUALS',
            value: that.filters.component
          }
        ]
      } else if (that.searchQuery !== undefined) {
        searchElements = [
          {
            mergeCondition: 'AND',
            searchType: 'INDEX',
            value: that.searchQuery.trim() ? `*${that.searchQuery}*` : '***'
          }
        ]
      }
      axios
        .post(
          `/openstorefront/api/v1/service/search/advance?paging=true&sortField=${
            that.searchSortField
          }&sortOrder=${that.searchSortOrder}&offset=${that.searchPage *
            that.searchPageSize}&max=${that.searchPageSize}`,
          {
            searchElements
          }
        )
        .then(response => {
          that.searchResults = response
          that.totalSearchResults = response.data.totalNumber
          that.searchQueryIsDirty = false
        })
        .catch(e => this.errors.push(e))
        .finally(() => {
          that.searchQueryIsDirty = false
        })
    },
    newSearch () {
      this.searchPage = 0
      this.show = false
      this.submitSearch()
    },
    searchCategory (category) {
      this.filters.component = category
      this.submitSearch()
    },
    nextPage () {
      this.searchPage += 1
      this.submitSearch()
    },
    prevPage () {
      if (this.searchPage > 0) {
        this.searchPage -= 1
        this.submitSearch()
      }
    },
    getPage (n) {
      this.searchPage = n
      this.submitSearch()
    },
    getNumPages () {
      // compute number of pages of data based on page size
      return Math.floor(this.totalSearchResults / this.searchPageSize)
    },
    getPagination (currentPage) {
      // show 4 pages
      if (this.getNumPages() === 0) return []
      return _.range(
        currentPage - 1 > 0 ? currentPage - 1 : 1,
        currentPage + 4 > this.getNumPages()
          ? this.getNumPages() + 2
          : currentPage + 4
      )
    }
  },
  watch: {
    // searchQuery: _.throttle(function () {
    //   this.searchQueryIsDirty = true;
    //   // some expensive query
    // }, 1000),
    filters: {
      handler: function () {
        if (!this.show) {
          this.newSearch()
        }
      },
      deep: true
    },
    show () {
      if (this.show === false) {
        this.newSearch()
      }
    },
    showOptions () {
      if (this.showOptions === false) {
        this.newSearch()
      }
    }
  },
  computed: {
    offset () {
      return this.searchPage * this.searchPageSize
    }
  },
  data () {
    return {
      components: [
        'ISP-3U',
        'GNC-PTS',
        'GNC-DETSEN',
        'SMM-FRAMES'
      ],
      tags: [
        'ISP-3U',
        'GNC-PTS',
        'GNC-DETSEN',
        'SMM-FRAMES'
      ],
      organizations: [
        'NASA',
        'DI2E',
        'Raytheon',
        'Space Dynamics Lab'
      ],
      selected: [],
      show: false,
      showOptions: false,
      searchQuery: '',
      filters: {
        component: '',
        tags: [],
        organizations: []
      },
      searchResults: {},
      searchQueryIsDirty: false,
      errors: [],
      searchPage: 0,
      searchPageSize: 10,
      totalSearchResults: 0,
      searchSortOrder: 'DESC',
      showAll: false,
      searchSortField: 'searchScore'
    }
  }
}
</script>

<style scoped>
/* Paging */
.pageButton {
  padding: 0.2em 0.8em;
  margin: 0.8em 0.2em;
  border-radius: 2px;
  /* color: rgba(0,0,0,.4); */
}
.pagination {
  text-align: center;
  display: block;
  margin: auto;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: white;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
}
.activePage {
  background-color: #e0e0e0;
}
.pageButton:hover {
  background-color: #e0e0e0;
}
.v-spacer {
  height: 5em;
}
/* Search Bar */
form {
  max-width: 40em;
  margin: 0 auto;
}
.searchbar {
  border-radius: 2px;
  box-shadow: 0 3px 1px -2px rgba(0, 0, 0, 0.2), 0 2px 2px 0 rgba(0, 0, 0, 0.14),
    0 1px 5px 0 rgba(0, 0, 0, 0.12);
  padding: 0.7em 1.2em;
  margin-left: auto;
  margin-right: auto;
  font-size: 120%;
  transition: box-shadow 0.7s;
}
.searchfield {
  display: inline-block;
  width: 80%;
}
.search-icon {
  float: right;
}
input {
  caret-color: #3467c0;
}
input:focus {
  outline: none;
}
input:focus + .icon {
  color: #3467c0;
}
.pill {
  border-radius: 10px;
  color: white;
  background-color: #555;
  padding: 0.1em 0.5em;
  margin-right: 0.3em;
  margin-top: 0.3em;
}
.statpill {
  border-radius: 10px;
  color: white;
  background-color: #555;
  padding: 0.1em 0.5em;
  margin-right: 0.3em;
  margin-top: 0.3em;
  display: inline-block;
  font-size: 85%;
}
.statpillnum {
  border-radius: 10px;
  background-color: #333;
  margin: 0;
  margin-left: 0.3em;
  padding: 0 0.4em;
  display: inline-block;
}
.fade-enter-active {
  transition: opacity 0.2s;
}
.fade-leave-active {
  transition: opacity 0.1s;
}
.fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */ {
  opacity: 0;
}
.clearfix:after {
  content: '';
  clear: both;
  display: table;
}
</style>

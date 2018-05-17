<template>
<div class="mx-3 mt-5">
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
        </div>
        <v-btn @click.stop="showOptions = false">
          <v-icon>save</v-icon> &nbsp; Save
        </v-btn>
      </v-card>
    </v-dialog>

    <!-- Filter pills if there are any -->
    <v-btn small @click="clear()" v-if="selected.length !== 0">Clear Filters</v-btn>
    <div v-if="selected.length !== 0" style="padding: 0 0.5em 0.8em 0.8em;">
      <transition-group name="fade">
        <div
          class="pill"
          style="display: inline-block"
          v-for="item in selected"
          v-bind:key="item">
          {{ item }}
          <div
            style="display: inline-block; cursor: pointer;"
            @click="deleteItem(item)"
          >&times;</div>
        </div>
      </transition-group>
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
        <v-list>
          <v-expansion-panel>
            <v-expansion-panel-content
              v-for="item in items"
              :key="item.label"
              expand-icon="arrow_drop_down"
            >
              <div slot="header">{{ item.label }}</div>
              <v-list-tile v-for="data in item.data" :key="data">
                <v-list-tile-action>
                  <v-checkbox v-model="selected" :value="data"></v-checkbox>
                </v-list-tile-action>
                <v-list-tile-title>{{ data }}</v-list-tile-title>
              </v-list-tile>
            </v-expansion-panel-content>
          </v-expansion-panel>
        </v-list>
        <v-btn @click.stop="show = false">
          <v-icon>save</v-icon> &nbsp; Save
        </v-btn>
      </v-card>
    </v-dialog>
  </form>

  <!-- Search Results -->
  <h2 v-if="searchResults.data" style="text-align: center">Search Results</h2>

  <p v-if="searchResults.data">
    <span v-if="searchQueryIsDirty">Fetching</span><span v-else>Showing</span>
    {{ offset + 1 }} -
    {{ totalSearchResults > offset + searchPageSize ? offset + searchPageSize : totalSearchResults }}
    of
    {{ searchResults.data.totalNumber }} results
  </p>
  <p v-if="getNumPages() === 0 && searchResults.data && searchResults.data.data.length === 0">No Search Results</p>

  <div style="width: 100%; text-align: center">
    <v-progress-circular
      v-if="searchQueryIsDirty"
      indeterminate color="primary"
    ></v-progress-circular>
  </div>

  <div v-if="searchResults.data">
    <v-expansion-panel popout>
      <v-expansion-panel-content style="grey" v-for="item in searchResults.data.data" :key="item">
        <div slot="header">{{ item.name }}</div>
        <v-card>
          <v-card-text>
            <!-- TODO: display more details and a link to the details page for that component -->
            {{ item.description }}
          </v-card-text>
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
  <div style="text-align: center;">
    <v-btn
      flat
      icon
      v-if="offset > 0" @click="prevPage()">
    <v-icon x-large>chevron_left</v-icon>
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
      <v-icon x-large>chevron_right</v-icon>
    </v-btn>
    <!-- <v-btn icon flat>
      <v-icon>fas fa-angle-double-right</v-icon>
    </v-btn> -->
  </div>

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
      this.newSearch()
    }
  },
  beforeRouteUpdate (to, from, next) {
    if (to.query.q !== from.query.q) {
      this.searchQuery = to.query.q
      this.newSearch()
    }
  },
  methods: {
    clear () {
      this.selected = []
    },
    deleteItem (item) {
      this.selected = _.remove(this.selected, n => n !== item)
    },
    submitSearch () {
      this.searchQueryIsDirty = true
      let that = this
      axios
        .post(`/openstorefront/api/v1/service/search/advance?paging=true&sortField=${that.searchSortField}&sortOrder=${that.searchSortOrder}&offset=${that.searchPage * that.searchPageSize}&max=${that.searchPageSize}`, {
          searchElements: [
            {
              mergeCondition: 'AND',
              searchType: 'INDEX',
              value: that.searchQuery.trim() ? `*${that.searchQuery}*` : '***'
            }
          ]
        })
        .then((response) => {
          that.searchResults = response
          that.totalSearchResults = response.data.totalNumber
          that.searchQueryIsDirty = false
        })
        .catch(e => (this.errors.push(e)))
        .finally(() => { that.searchQueryIsDirty = false })
    },
    newSearch () {
      this.searchPage = 0
      this.show = false
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
      return _.range(currentPage - 1 > 0 ? currentPage - 1 : 1, currentPage + 4 > this.getNumPages() ? this.getNumPages() + 2 : currentPage + 4)
    }
  },
  watch: {
    // searchQuery: _.throttle(function () {
    //   this.searchQueryIsDirty = true;
    //   // some expensive query
    // }, 1000),
  },
  computed: {
    offset () {
      return this.searchPage * this.searchPageSize
    }
  },
  data () {
    return {
      items: [
        { label: 'Category', color: 'teal', data: ['Shooters', 'Telescopes', 'Rockets', 'Propulsion', 'Blasters', 'Guns', 'Bullets', 'Gear'] },
        { label: 'Organization', color: 'blue', data: ['NASA', 'Navy', 'SpaceX', 'Starsem', 'Orbital ATK', 'Lockheed Martin', 'Rockwell'] }
      ],
      selected: [],
      show: false,
      showOptions: false,
      searchQuery: '',
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
  margin-right: 0.2em;
  border-radius: 2px;
}
.activePage {
  background-color: #E0E0E0;
}
.pageButton:hover {
  background-color: #E0E0E0;
}
/* Search Bar */
form {
  max-width: 40em;
  margin: 0 auto;
}
.searchbar {
  border-radius: 2px;
  box-shadow: 0 3px 1px -2px rgba(0,0,0,.2),0 2px 2px 0 rgba(0,0,0,.14),0 1px 5px 0 rgba(0,0,0,.12);
  padding: 0.7em 1.2em;
  margin-bottom: 0.3em;
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
    caret-color: #3467C0;
}
input:focus {
  outline: none;
}
input:focus + .icon {
  color: #3467C0;
}
.pill {
  border-radius: 10px;
  color: white;
  background-color:#555;
  padding: 0.1em 0.5em;
  margin-right: 0.3em;
  margin-top: 0.3em;
}
.fade-enter-active, {
  transition: opacity .2s;
}
.fade-leave-active {
  transition: opacity .1s;
}
.fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */ {
  opacity: 0;
}
.clearfix:after {
  content: "";
  clear: both;
  display: table;
}
</style>

<template>
<div class="mt-4">

  <v-container style="max-width: 90%;">
    <v-layout>
    <v-flex xs3 style="border-right: 1px solid #DDD;" class="pr-3">
      <h2>Search Filters</h2>
      <v-select
        v-model="filters.component"
        :items="componentsList"
        item-text="componentTypeDescription"
        item-value="componentType"
        label="Category"
        clearable
        multi-line
      >
        <template slot="selection" slot-scope="data">
          ({{ data.item.count }}) {{ data.item.componentTypeDescription }}
        </template>
        <template slot="item" slot-scope="data">
          <v-list-tile-content><v-list-tile-title>({{ data.item.count }}) {{ data.item.componentTypeDescription }}</v-list-tile-title></v-list-tile-content>
        </template>
      </v-select>
      <v-checkbox class="ma-0" label="Include Sub-Categories" v-model="filters.children"></v-checkbox>
      <v-select
        v-model="filters.tags"
        hide-details
        :items="tagsList"
        :disabled="!tagsList || tagsList.length === 0"
        item-text="tagLabel"
        item-value="tagLabel"
        :label="!tagsList || tagsList.length === 0 ? 'No Tags' : 'Tags'"
        multiple
        small-chips
        clearable
      >
        <template slot="selection" slot-scope="data">
          <v-chip close small @input="deleteTag(data.item.tagLabel)" >
            <v-avatar class="grey lighten-1">{{ data.item.count }}</v-avatar>
            {{ data.item.tagLabel}}
          </v-chip>
        </template>
        <template slot="item" slot-scope="data">
          <v-list-tile-content><v-list-tile-title>({{ data.item.count }}) {{ data.item.tagLabel}}</v-list-tile-title></v-list-tile-content>
        </template>
      </v-select>
      <v-radio-group row label="Tag Search Condition: " v-model="filters.tagCondition">
        <v-radio label="And" value="AND"></v-radio>
        <v-radio label="Or" value="OR"></v-radio>
      </v-radio-group>
      <v-autocomplete
        v-model="filters.organization"
        :items="organizationsList"
        label="Organization"
        item-text="organization"
        item-value="organization"
        clearable
      >
        <template slot="selection" slot-scope="data">
          ({{ data.item.count }}) {{ data.item.organization }}
        </template>
        <template slot="item" slot-scope="data">
          <v-list-tile-content><v-list-tile-title>({{ data.item.count }}) {{ data.item.organization }}</v-list-tile-title></v-list-tile-content>
        </template>
      </v-autocomplete>
      <!-- <v-btn block class="success" @click="submitSearch()">Submit</v-btn> -->
      <v-btn block class="primary" @click="clear()">Clear Filters</v-btn>
    </v-flex>
    <v-flex xs9>
  <!-- Search Bar and menu  -->
  <div class="clearfix centeralign px-3" style="max-width: 46em;">
    <SearchBar
      v-on:submitSearch="submitSearch()"
      v-on:clear="submitSearch()"
      :hideSuggestions="hideSearchSuggestions"
      v-model="searchQuery"
    ></SearchBar>

    <!-- SearchBar Menu Buttons -->
    <div style="display: inline-block; float: right;">
      <v-btn small flat @click.stop="showOptions = true" icon><v-icon style="font-size: 1.2em;">fas fa-cog</v-icon>
      </v-btn>
    </div>

    <!-- Search Options Dialog -->
    <v-dialog
      v-model="showOptions"
      max-width="300px"
      >
      <v-card>
        <v-toolbar color="primary" dark dense>
          <v-toolbar-title>Search Options</v-toolbar-title>
        </v-toolbar>
        <v-card-text>
          <h3>Sort Order</h3>
          <v-radio-group v-model="searchSortOrder">
            <v-radio label="Ascending" value="ASC"></v-radio>
            <v-radio label="Descending" value="DESC"></v-radio>
          </v-radio-group>
          <h3>Sort by:</h3>
          <v-select
            v-model="searchSortField"
            :items="searchSortFields"
          ></v-select>
          <h3>Page Size</h3>
          {{ searchPageSize }}
          <v-slider v-model="searchPageSize" step="5" min="5" thumb-label></v-slider>
        </v-card-text>
        <v-card-actions>
          <v-btn @click.stop="showOptions = false">Close Options</v-btn>
          <v-btn @click="resetOptions()">Reset Options</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

  </div><!-- Search Bar and menu  -->

  <!-- Search Results -->
  <div v-if="searchResults.data" class="clearfix centeralign px-3">
    <h2 style="text-align: center" class="mb-2">Search Results</h2>

    <p v-if="searchResults.data.totalNumber === 0">No Search Results</p>
    <p v-else-if="!searchQueryIsDirty" class="pl-5 ma-0">
      {{ offset + 1 }} -
      {{ totalSearchResults > offset + searchPageSize ? offset + searchPageSize : totalSearchResults }}
      of
      {{ searchResults.data.totalNumber }} results
    </p>

    <!-- SEARCH RESULTS DATA -->
    <v-layout
      row
      justify-center
      align-center
      v-if="searchQueryIsDirty"
    >
      <v-flex xs1>
        <v-progress-circular
          color="primary"
          :size="60"
          :width="6"
          indeterminate
          class="spinner"
        ></v-progress-circular>
      </v-flex>
    </v-layout>
    <div
      v-else
      v-for="item in searchResults.data.data"
      :key="item.name"
      class="mt-4"
      style="clear: left;"
    >
      <img
        v-if="item.includeIconInSearch && item.componentTypeIconUrl"
        :src="'/openstorefront/' + item.componentTypeIconUrl"
        style="max-width: 40px; margin-right: 1em; float: left;"
      >
      <div style="float: left;" class="mb-5">
        <h3>{{ item.name }}</h3>
        <p class="mb-0">{{ item.organization }}</p>
        <router-link
          :to="{ path: 'search', query: { comp: item.componentType }}"
        >
          {{ item.componentTypeDescription }}
        </router-link>
        <div
          style="padding-bottom: 1em;"
          v-if="item.tags.length !== 0"
        >
          <span
            v-for="tag in item.tags"
            :key="tag.text"
            style="float: left; margin-right: 0.8em; cursor: pointer;"
            @click="addTag(tag.text)"
          >
            <v-icon style="font-size: 14px;">fas fa-tag</v-icon> {{ tag.text }}
          </span>
        </div>
      </div>
    </div>

  </div>
    </v-flex>
    </v-layout>
  </v-container>

  <!-- Allow space for the pagination -->
  <div class="v-spacer"></div>

  <!-- Pagination -->
  <div class="pagination text-xs-center">
    <v-pagination
      v-model="searchPage"
      :length="getNumPages()"
      total-visible="7"
    ></v-pagination>
  </div>

</div>
</template>

<script>
import _ from 'lodash'
import SearchBar from '../components/SearchBar'
import StarRating from 'vue-star-rating'
import router from '../router.js'

export default {
  name: 'SearchPage',
  components: {
    SearchBar,
    StarRating
  },
  mounted () {
    if (this.$route.query.q) {
      this.searchQuery = this.$route.query.q
    }
    if (this.$route.query.comp) {
      this.filters.component = this.$route.query.comp
    }
    if (this.$route.query.children) {
      this.filters.children = this.$route.query.children
    }
    this.newSearch()
  },
  beforeRouteUpdate (to, from, next) {
    if (to.query.q) {
      this.searchQuery = to.query.q
    }
    if (to.query.comp) {
      this.filters.component = to.query.comp
    }
    if (to.query.children) {
      this.filters.children = to.query.children
    }
    this.newSearch()
  },
  methods: {
    clear () {
      this.filters = {
        component: '',
        tags: [],
        organization: '',
        children: false,
        tagCondition: 'AND'
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
    addTag (tag) {
      if (this.filters.tags.indexOf(tag) === -1) {
        this.filters.tags.push(tag)
      }
    },
    submitSearch () {
      let that = this
      // a new search clears the data and can trigger a watcher
      // sometimes 2 POST requests get sent out together
      if (that.searchQueryIsDirty) return
      that.searchQueryIsDirty = true
      let searchElements = [
        {
          mergeCondition: 'AND',
          searchType: 'INDEX',
          value: that.searchQuery.trim() ? `*${that.searchQuery}*` : '***'
        }
      ]
      if (that.filters.component) {
        searchElements.push(
          {
            caseInsensitive: false,
            field: 'componentType',
            mergeCondition: 'AND',
            searchType: 'ENTRYTYPE',
            searchChildren: that.filters.children,
            stringOperation: 'EQUALS',
            value: that.filters.component
          }
        )
      }
      if (that.filters.tags) {
        that.filters.tags.forEach(function (tag) {
          searchElements.push(
            {
              caseInsensitive: true,
              mergeCondition: that.filters.tagCondition,
              searchType: 'TAG',
              stringOperation: 'EQUALS',
              value: tag
            }
          )
        })
      }
      if (that.filters.organization) {
        searchElements.push(
          {
            caseInsensitive: false,
            mergeCondition: 'AND',
            searchType: 'COMPONENT',
            numberOperation: 'EQUALS',
            stringOperation: 'EQUALS',
            field: 'organization',
            value: that.filters.organization
          }
        )
      }
      this.$http
        .post(
          `/openstorefront/api/v1/service/search/advance?paging=true&sortField=${
            that.searchSortField
          }&sortOrder=${that.searchSortOrder}&offset=${(that.searchPage - 1) *
            that.searchPageSize}&max=${that.searchPageSize}`,
          {
            searchElements
          }
        )
        .then(response => {
          that.searchResults = response
          that.totalSearchResults = response.data.totalNumber
          that.organizationsList = _.sortBy(response.data.meta.resultOrganizationStats, [function (o) { return o.organization }])
          that.tagsList = _.sortBy(response.data.meta.resultTagStats, [function (o) { return o.tagLabel }])
          that.componentsList = _.sortBy(response.data.meta.resultTypeStats, [function (o) { return o.componentTypeDescription }])
          that.searchQueryIsDirty = false
        })
        .catch(e => that.errors.push(e))
        .finally(() => {
          that.searchQueryIsDirty = false
        })
    },
    getNestedComponentTypes () {
      this.$http
        .get(
          '/openstorefront/api/v1/resource/componenttypes/nested'
        )
        .then(response => {
          this.nestedComponentTypesList = response.data.data
        })
        .catch(e => this.errors.push(e))
    },
    newSearch () {
      this.searchPage = 1
      this.showFilters = false
      this.submitSearch()
    },
    searchCategory (category) {
      this.filters.component = category
      this.submitSearch()
    },
    getNumPages () {
      // compute number of pages of data based on page size
      if (this.totalSearchResults % this.searchPageSize === 0) return (this.totalSearchResults / this.searchPageSize) - 1
      return Math.floor(this.totalSearchResults / this.searchPageSize) + 1
    },
    moreInformation (componentId) {
      router.push({
        name: 'Entry Detail',
        params: {
          id: componentId
        }
      })
    }
  },
  watch: {
    filters: {
      handler: function () {
        if (!this.showFilters) {
          this.newSearch()
        }
      },
      deep: true
    },
    searchPage () {
      this.submitSearch()
    }
  },
  computed: {
    offset () {
      return (this.searchPage - 1) * this.searchPageSize
    },
    hideSearchSuggestions () {
      return this.searchQueryIsDirty || this.searchQuery.length === 0
    }
  },
  data () {
    return {
      componentsList: [],
      tagsList: [],
      organizationsList: [],
      selected: [],
      showFilters: false,
      showOptions: false,
      showHelp: false,
      searchQuery: '',
      filters: {
        component: '',
        tags: [],
        organization: '',
        children: false,
        tagCondition: 'AND'
      },
      searchResults: {},
      searchQueryIsDirty: false,
      errors: [],
      searchPage: 0,
      searchPageSize: 10,
      totalSearchResults: 0,
      searchSortOrder: 'DESC',
      showAll: false,
      searchSortField: 'searchScore',
      searchSortFields: [
        { text: 'Name', value: 'name' },
        { text: 'Organization', value: 'organization' },
        { text: 'User Rating', value: 'averageRating' },
        { text: 'Last Update', value: 'lastActivityDts' },
        { text: 'Approval Date', value: 'approvedDts' },
        { text: 'Relevance', value: 'searchScore' }
      ]
    }
  }
}
</script>

<style scoped>
/* Paging */
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
  z-index: 3;
}
.v-spacer {
  height: 3.2em;
}
.clearfix:after {
  content: '';
  clear: both;
  display: table;
}
.centeralign {
  margin-right: auto;
  margin-left: auto;
}
.spinner {
  margin-top: 7em;
}
hr {
  color: #333;
  margin-bottom: 1em;
}
</style>

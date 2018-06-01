<template>
<div class="mx-3 mt-4">

  <div v-if="errors.length > 0">
    <ul>
      <li v-for="error in errors" :key="error">
        {{ error }}
      </li>
    </ul>
  </div>

  <div class="clearfix centeralign" style="max-width: 36em;">
    <SearchBar v-on:submitSearch="submitSearch()" :hideSuggestions="searchQueryIsDirty" v-model="searchQuery"></SearchBar>

    <!-- SearchBar Menu Buttons -->
    <div style="display: inline-block; float: right;">
      <v-btn small @click="showFilters = !showFilters" flat icon>
        <v-icon style="font-size: 1.2em;">fas fa-filter</v-icon>
      </v-btn>
      <v-btn small flat @click.stop="showOptions = true" icon><v-icon style="font-size: 1.2em;">fas fa-cog</v-icon>
      </v-btn>
      <v-btn small flat @click.stop="showHelp = true" icon><v-icon style="font-size: 1.2em;">fas fa-question</v-icon>
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
        </v-card-title>
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
        <v-card-action>
          <v-btn @click.stop="showOptions = false">Submit</v-btn>
          <v-btn @click="resetOptions()">Reset Options</v-btn>
        </v-card-action>
      </v-card>
    </v-dialog>

    <!-- Filter pills if there are any -->
    <v-btn small @click="clear()" v-if="(filters.component || filters.organization || filters.tags.length !== 0)">Clear Filters</v-btn>
    <div style="padding: 0 0.5em 0.8em 0.8em;">
      <span v-if="filters.component"><v-chip close small @input="filters.component = ''" color="teal lighten-2" text-color="white">{{ filters.component | truncate(30) }}</v-chip></span>
      <span v-if="filters.tags.length !== 0"><v-chip v-for="tag in filters.tags" :key="tag" close small @input="deleteTag(tag)">{{ tag | truncate(30) }}</v-chip></span>
      <span v-if="filters.organization"><v-chip close small color="indigo lighten-2" text-color="white" @input="filters.organization = ''">{{ filters.organization | truncate(30) }}</v-chip></span>
    </div>

    <!-- Search Filters Dialog -->
    <v-dialog
      v-model="showFilters"
      max-width="500px"
      >
      <v-card>
        <v-card-title>
          <h2>Search Filters</h2>
        </v-card-title>
        <v-card-text class="clearfix">
          <v-select
            v-model="filters.component"
            :items="componentsList"
            item-text="parentLabel"
            item-value="componentType"
            label="Category"
            clearable
            multi-line
          ></v-select>
          <v-checkbox label="Include Sub-Categories" v-model="filters.children"></v-checkbox>
          <v-select
            v-model="filters.tags"
            hide-details
            :items="tagsList"
            item-value="text"
            label="Tags"
            multiple
            chips
            clearable
          ></v-select>
          <v-radio-group label="Tag Search Condition" v-model="filters.tagCondition">
            <v-radio label="And" value="AND"></v-radio>
            <v-radio label="Or" value="OR"></v-radio>
          </v-radio-group>
          <v-select
            v-model="filters.organization"
            :items="organizationsList"
            item-text="name"
            item-value="name"
            label="Organization"
            clearable
          ></v-select>
        </v-card-text>
        <v-card-action>
          <v-btn @click.stop="showFilters = false">Submit</v-btn>
          <v-btn @click="clear()">Clear Filters</v-btn>
        </v-card-action>
      </v-card>
    </v-dialog>
  </div> <!-- Search Bar and menu  -->

  <!-- Search Help Dialog -->
    <v-dialog
      v-model="showHelp"
      max-width="300px"
      >
      <v-card>
        <v-card-title>
          <h2>Search Help</h2>
        </v-card-title>
        <v-card-text>
          <h3>Filter Color Legend</h3>
          <v-chip close small color="teal lighten-2" text-color="white">Category Shortcode</v-chip>
          <v-chip close small>Tag</v-chip>
          <v-chip close small color="indigo lighten-2" text-color="white">Organization</v-chip>
        </v-card-text>
        <v-card-action>
          <v-btn @click="showHelp = !showHelp">Close</v-btn>
        </v-card-action>
      </v-card>
    </v-dialog>

  <!-- Search Results -->
  <h2 v-if="searchResults.data" style="text-align: center" class="mb-2">Search Results</h2>

  <p v-if="searchResults.data && searchResults.data.totalNumber === 0">No Search Results</p>
  <p v-else-if="searchResults.data" class="mb-0">
    <span v-if="searchQueryIsDirty">Fetching</span><span v-else>Showing</span>
    {{ offset + 1 }} -
    {{ totalSearchResults > offset + searchPageSize ? offset + searchPageSize : totalSearchResults }}
    of
    {{ searchResults.data.totalNumber }} results
  </p>

  <div v-if="searchResults.data" style="margin-bottom: 1em; padding-bottom: 0.5em; overflow: auto; white-space: nowrap;">
    <v-chip v-for="stat in searchResults.data.resultTypeStats" :key="stat" @click="searchCategory(stat.componentType)" color="teal" text-color="white">
      <v-avatar class="teal darken-2">{{ stat.count }}</v-avatar>
      {{ stat.componentTypeDescription }}
    </v-chip>
  </div>

  <div v-if="searchQueryIsDirty" class="overlay">
    <v-progress-circular
      color="teal"
      :size="70"
      :width="7"
      indeterminate
      :class="{center:searchQueryIsDirty}"
    ></v-progress-circular>
  </div>

  <div v-if="searchResults.data">
    <v-expansion-panel popout>
      <v-expansion-panel-content v-for="item in searchResults.data.data" :key="item">
        <div slot="header">
          <div style="float: left;" v-if="item.includeIconInSearch && item.componentTypeIconUrl">
            <img :src="'/openstorefront/' + item.componentTypeIconUrl" width="30" style="margin-right: 1em;">
          </div>
          <div>
            {{ item.name }}
          </div>
        </div>
        <v-card class="grey lighten-5">
          <v-card-text>
            <p>
              <router-link
                :to="{ path: 'search', query: { comp: item.componentType }}"
              >
                {{ item.componentTypeDescription }}
              </router-link>
            </p>
            <p
              style="padding-bottom: 1em;"
              class="clearfix"
              v-if="item.tags.length !== 0"
            >
            <span
              v-for="tag in item.tags"
              :key="tag"
              style="float: left; margin-right: 0.8em; cursor: pointer;"
              @click="addTag(tag.text)"
            >
              <v-icon style="font-size: 14px;">fas fa-tag</v-icon> {{ tag.text }}
            </span>
            </p>
            <h2>Details</h2>
            <hr>
            <p><strong>Organization:</strong> {{ item.organization }}</p>
            <p>
              <strong>Average User Rating:</strong>
              <star-rating :rating="item.averageRating" :read-only="true" :increment="0.01" :star-size="30"></star-rating>
            </p>
            <p><strong>Last Updated:</strong> {{ item.updateDts | formatDate }}</p>
            <p><strong>Approved Date:</strong> {{ item.approvedDts | formatDate }}</p>
            <h2>Description</h2>
            <hr>
            <div v-html="item.description"></div>
          </v-card-text>
          <v-card-actions>
            <!-- TODO: link to the details page for that component -->
            <v-btn color="info">More Information</v-btn>
          </v-card-actions>
        </v-card>
      </v-expansion-panel-content>
    </v-expansion-panel>
  </div>

  <!-- Pagination -->
  <div class="pagination">
    <v-btn
      flat
      icon
      style="margin:0;"
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
      style="margin:0;"
      v-if="offset + searchPageSize < totalSearchResults" @click="nextPage()">
      <v-icon x-large style="color: #333;">chevron_right</v-icon>
    </v-btn>
  </div>

  <div class="v-spacer"></div>

</div>
</template>

<script>
import _ from 'lodash';
import axios from 'axios';
import SearchBar from './subcomponents/SearchBar';
import StarRating from 'vue-star-rating';

export default {
  name: 'SearchPage',
  components: {
    SearchBar,
    StarRating
  },
  mounted () {
    if (this.$route.query.q) {
      this.searchQuery = this.$route.query.q;
    }
    if (this.$route.query.comp) {
      this.filters.component = this.$route.query.comp;
    }
    if (this.$route.query.children) {
      this.filters.children = this.$route.query.children;
    }
    this.getComponentTypes();
    this.getTags();
    this.getOrganizations();
    this.newSearch();
  },
  beforeRouteUpdate (to, from, next) {
    if (to.query.q) {
      this.searchQuery = to.query.q;
    }
    if (to.query.comp) {
      this.filters.component = to.query.comp;
    }
    if (to.query.children) {
      this.filters.children = to.query.children;
    }
    this.newSearch();
  },
  methods: {
    clear () {
      this.filters = {
        component: '',
        tags: [],
        organization: '',
        children: false,
        tagCondition: 'AND'
      };
    },
    resetOptions () {
      this.searchPageSize = 10;
      this.searchSortField = 'searchScore';
      this.searchSortOrder = 'DESC';
    },
    deleteTag (tag) {
      this.filters.tags = _.remove(this.filters.tags, n => n !== tag);
    },
    addTag (tag) {
      if (this.filters.tags.indexOf(tag) === -1) {
        this.filters.tags.push(tag);
      }
    },
    submitSearch () {
      this.searchQueryIsDirty = true;
      let that = this;
      let searchElements = [
        {
          mergeCondition: 'AND',
          searchType: 'INDEX',
          value: that.searchQuery.trim() ? `*${that.searchQuery}*` : '***'
        }
      ];
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
        );
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
          );
        });
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
        );
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
          that.searchResults = response;
          that.totalSearchResults = response.data.totalNumber;
          that.searchQueryIsDirty = false;
        })
        .catch(e => this.errors.push(e))
        .finally(() => {
          that.searchQueryIsDirty = false;
        });
    },
    getComponentTypes () {
      let that = this;
      axios
        .get(
          '/openstorefront/api/v1/resource/componenttypes'
        )
        .then(response => {
          that.componentsList = response.data;
        })
        .catch(e => this.errors.push(e));
    },
    getTags () {
      let that = this;
      axios
        .get(
          '/openstorefront/api/v1/resource/components/tagviews?approvedOnly=true'
        )
        .then(response => {
          that.tagsList = response.data;
        })
        .catch(e => this.errors.push(e));
    },
    getOrganizations () {
      let that = this;
      axios
        .get(
          '/openstorefront/api/v1/resource/organizations?componentOnly=true'
        )
        .then(response => {
          that.organizationsList = response.data.data;
        })
        .catch(e => this.errors.push(e));
    },
    getNestedComponentTypes () {
      let that = this;
      axios
        .get(
          '/openstorefront/api/v1/resource/componenttypes/nested'
        )
        .then(response => {
          that.nestedComponentTypesList = response.data.data;
        })
        .catch(e => this.errors.push(e));
    },
    newSearch () {
      this.searchPage = 0;
      this.showFilters = false;
      this.submitSearch();
    },
    searchCategory (category) {
      this.filters.component = category;
      this.submitSearch();
    },
    nextPage () {
      this.searchPage += 1;
      this.submitSearch();
    },
    prevPage () {
      if (this.searchPage > 0) {
        this.searchPage -= 1;
        this.submitSearch();
      }
    },
    getPage (n) {
      this.searchPage = n;
      this.submitSearch();
    },
    getNumPages () {
      // compute number of pages of data based on page size
      return Math.floor(this.totalSearchResults / this.searchPageSize);
    },
    getPagination (currentPage) {
      // show 4 pages
      if (this.getNumPages() === 0) return [];
      return _.range(
        currentPage - 1 > 0 ? currentPage - 1 : 1,
        currentPage + 4 > this.getNumPages()
          ? this.getNumPages() + 2
          : currentPage + 4
      );
    }
  },
  watch: {
    filters: {
      handler: function () {
        if (!this.showFilters) {
          this.newSearch();
        }
      },
      deep: true
    },
    showFilters () {
      if (this.showFilters === false) {
        this.newSearch();
      }
    },
    showOptions () {
      if (this.showOptions === false) {
        this.newSearch();
      }
    }
  },
  computed: {
    offset () {
      return this.searchPage * this.searchPageSize;
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
    };
  }
};
</script>

<style scoped>
/* Paging */
.pageButton {
  padding: 0.2em 0.8em;
  margin: 0.4em 0.2em;
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
  z-index: 3;
}
.activePage {
  background-color: #e0e0e0;
}
.pageButton:hover {
  background-color: #e0e0e0;
}
.v-spacer {
  height: 3.2em;
}
.clearfix:after {
  content: '';
  clear: both;
  display: table;
}
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 990;
  background-color: rgba(255,255,255, 0.7);
  pointer-events: all;
}
.center {
  z-index: 991;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
.centeralign {
  margin-right: auto;
  margin-left: auto;
}
hr {
  color: #333;
  margin-bottom: 1em;
}
</style>

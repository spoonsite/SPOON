<template>
<div>

  <div :class="`side-menu ${showFilters || showOptions ? 'open' : 'closed'}`">
    <!-- CONTROLS -->
    <div class="side-menu-btns">
      <div>
        <v-btn @click="showFilters = !showFilters; showOptions = false;" small fab dark icon :color="`primary ${showFilters ? 'lighten-4' : ''}`"><v-icon dark>fas fa-filter</v-icon></v-btn>
      </div>
      <div>
        <v-btn @click="showOptions = !showOptions; showFilters = false;" small fab dark icon :color="`primary ${showOptions ? 'lighten-4' : ''}`"><v-icon dark>fas fa-cog</v-icon></v-btn>
      </div>
      <div>
        <v-btn @click="copyUrlToClipboard" small fab icon><v-icon>fas fa-share-alt</v-icon></v-btn>
        <input type="text" value="https://spoonsite.com" ref="urlForClipboard" style="position: absolute; left: -1000px; top: -1000px">
      </div>
    </div>

    <div v-if="showOptions || showFilters" class="close-btn">
      <v-btn icon @click="showOptions = false; showFilters = false;"><v-icon>fas fa-times</v-icon></v-btn>
    </div>
    <!-- END CONTROLS -->

    <!-- SEARCH OPTIONS -->
    <div :class="`side-menu-content pt-0 ${showFilters || showOptions ? 'db' : 'dn'}`">
      <div v-if="showOptions">
        <h2>Search Options</h2>
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
        <v-btn block class="primary" @click="resetOptions()">Reset Options</v-btn>
      </div><!-- SEARCH OPTIONS -->

      <!-- SEARCH FILTERS -->
      <div v-if="showFilters">
        <h2>Search Filters</h2>
        <v-btn block class="" @click="clear()">Clear Filters</v-btn>
        <v-select
          v-model="filters.components"
          :items="componentsList"
          item-text="componentTypeDescription"
          item-value="componentType"
          label="Category"
          clearable
          multiple
          chips
          multi-line
        >
          <template slot="selection" slot-scope="data">
            <v-chip close small @input="removeComponent(data.item.componentType)" >
              <v-avatar class="grey lighten-1">{{ data.item.count }}</v-avatar>
              {{ data.item.componentTypeDescription}}
            </v-chip>
          </template>
          <template slot="item" slot-scope="data">
            <v-list-tile-content><v-list-tile-title>({{ data.item.count }}) {{ data.item.componentTypeDescription}}</v-list-tile-title></v-list-tile-content>
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
          class="pb-3"
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
        <!-- <v-radio-group label="Tag Search Condition: " v-model="filters.tagCondition">
          <v-radio label="And" value="AND"></v-radio>
          <v-radio label="Or" value="OR"></v-radio>
        </v-radio-group> -->
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
        <h3 class="pb-3">Attributes</h3>
        <div class="searchbar">
          <input
            type="text"
            label="Search Attributes"
            solo
            v-model="attributeQuery"
            placeholder="Search Attributes"
            ref="attributeBar"
          >
          <v-icon v-if="attributeQuery !== ''" class="search-icon" @click="attributeQuery=''">clear</v-icon>
        </div>
        <div>
          <v-chip
            close
            v-for="attr in filters.attributes"
            :key="attr"
            @input="removeAttributeFilter(attr)"
          >
            {{ printAttribute(attr) }}
          </v-chip>
        </div>
        <div v-if="Object.keys(searchResultsAttributes).length !== 0">Showing {{ attributeKeys.length }} of {{ Object.keys(searchResultsAttributes).length }} attributes</div>
        <div v-if="Object.keys(attributeKeys).length === 0">No Attributes</div>
        <v-expansion-panel class="mb-4" v-if="Object.keys(searchResultsAttributes).length !== 0">
          <!-- need the v-if with the v-for because the data sometimes gets out of sync -->
          <!-- eslint-disable vue/no-use-v-if-with-v-for -->
          <v-expansion-panel-content
            v-for="key in attributeKeys"
            :key="key"
            v-if="searchResultsAttributes[key]"
          >
          <!-- eslint-enable vue/no-use-v-if-with-v-for -->
            <div slot="header"
              v-html="searchResultsAttributes[key].attributeTypeLabel + (searchResultsAttributes[key].attributeUnit ? ' (' + searchResultsAttributes[key].attributeUnit + ') ' : '')"
            >
            </div>
            <v-card>
              <v-container class="pt-0" fluid>
                <!-- <attribute-range/> -->
                <v-checkbox
                  v-for="code in (searchResultsAttributes[key].codeMap)"
                  :key="code.codeLabel"
                  v-model="filters.attributes"
                  :value="JSON.stringify({ 'type': key, 'unit': searchResultsAttributes[key].attributeUnit ,'typelabel': searchResultsAttributes[key].attributeTypeLabel, 'code': code.codeLabel })"
                  hide-details
                >
                  <template slot="label">
                    <div>{{ code.codeLabel }}</div>
                  </template>
                </v-checkbox>
              </v-container>
            </v-card>
          </v-expansion-panel-content>
        </v-expansion-panel>
      </div><!-- SEARCH FILTERS -->
    </div><!-- SIDE MENU CONTENT -->
  </div><!--SIDE MENU -->

  <div :class="`search-block pt-4 ${showFilters || showOptions ? 'open' : 'closed'}`">
    <!-- Search Bar and menu  -->
    <div class="centeralign px-3 mb-5" style="max-width: 46em;">
      <SearchBar
        v-on:submitSearch="submitSearch()"
        v-on:clear="submitSearch()"
        :hideSuggestions="hideSearchSuggestions"
        v-model="searchQuery"
        :overlaySuggestions="true"
        :submittedEntryTypes="this.$route.query.comp.split(',')"
        @componentsChange="componentsChange"
      ></SearchBar>
      <!-- SEARCH FILTERS PILLS -->
      <v-chip
        v-for="component in filters.components"
        :key="component"
      >
        <v-avatar left>
          <v-icon small>fas fa-cubes</v-icon>
        </v-avatar>
        {{ getComponentName(component) }}
        <div class="v-chip__close"><v-icon right @click="removeComponent(component)">cancel</v-icon></div>
      </v-chip>
      <v-chip
        v-if="this.filters.children && !!this.filters.components && this.filters.components.length > 0"
      >
        <v-avatar left>
          <v-icon small>fas fa-check-square</v-icon>
        </v-avatar>
        Include Sub-Catagories
        <div class="v-chip__close"><v-icon right @click="filters.children = !filters.children">cancel</v-icon></div>
      </v-chip>
      <v-chip
        v-for="tag in filters.tags"
        :key="tag"
      >
        <v-avatar left>
          <v-icon small>fas fa-tag</v-icon>
        </v-avatar>
        {{ tag }}
        <div class="v-chip__close"><v-icon right @click="removeTag(tag)">cancel</v-icon></div>
      </v-chip>
      <v-chip
        v-if="filters.organization"
      >
        <v-avatar left>
          <v-icon small>fas fa-university</v-icon>
        </v-avatar>
        {{ filters.organization }}
        <div class="v-chip__close"><v-icon right @click="filters.organization = ''">cancel</v-icon></div>
      </v-chip>
      <v-chip
        close
        v-for="attr in filters.attributes"
        :key="attr"
        @input="removeAttributeFilter(attr)"
      >
        <v-avatar left>
          <v-icon small>fas fa-clipboard-list</v-icon>
        </v-avatar>
        {{ printAttribute(attr) }}
      </v-chip>
      <!-- SEARCH FILTERS PILLS -->
    </div><!-- Search Bar and menu  -->

    <!-- Search Results -->
    <div class="px-3">
      <h2 style="text-align: center" class="mb-2">Search Results</h2>

      <p v-if="searchResults.data && searchResults.data.totalNumber === 0">No Search Results</p>
      <p v-else-if="searchResults.data && !searchQueryIsDirty" class="pl-5 ma-0">
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
        v-else-if="!!searchResults.data"
        v-for="item in searchResults.data.data"
        :key="item.name"
        class="mt-4 item"
        style="clear: left; display: flex; flex-wrap: nowrap;"
      >
        <img
          v-if="computeHasImage(item)"
          :src="item.link"
          style="max-width: 40px; max-height: 40px; margin-right: 1em; float: left;"
        >
        <img
          v-else-if="item.includeIconInSearch && item.componentTypeIconUrl"
          :src="'/openstorefront/' + item.componentTypeIconUrl"
          style="max-width: 40px; max-height: 40px; margin-right: 1em; float: left;"
        >
        <div style="float: left;" class="mb-5">
          <h3 class='more-info' @click='moreInformation(item.componentId)'>{{ item.name }}</h3>
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
    </div><!-- Search Results -->
  </div>

  <!-- Pagination -->
  <v-footer
    fixed
    color="#FFF"
    style="border-top: 1px solid #DDD"
  >
    <v-layout justify-center>
      <v-pagination
        v-model="searchPage"
        :length="getNumPages()"
        total-visible="5"
      ></v-pagination>
    </v-layout>
  </v-footer>

</div>
</template>

<script>
import _ from 'lodash'
import SearchBar from '../components/SearchBar'
// import AttributeRange from '../components/AttributeRange'
import router from '../router.js'

export default {
  name: 'SearchPage',
  components: {
    SearchBar
    // AttributeRange
  },
  created () {
    this.$store.watch((state) => state.selectedComponentTypes, (newValue, oldValue) => {
      if (this.selectedEntryTypes !== newValue) {
        this.filters.components = newValue
      }
    })
  },
  mounted () {
    if (this.$route.query.q) {
      this.searchQuery = this.$route.query.q
    }
    if (this.$route.query.comp) {
      this.filters.components = this.$route.query.comp.split(',')
    }
    if (this.$route.query.children === 'false') {
      this.filters.children = false
    } else {
      this.filters.children = true
    }
    if (this.$route.query.tags) {
      this.filters.tags = this.$route.query.tags.split(',')
    }
    if (this.$route.query.orgs) {
      this.filters.organization = this.$route.query.orgs
    }
    if (this.$route.query.attributes) {
      this.filters.attributes = []
      this.$route.query.attributes.match(/({.*?})/gm).forEach(attribute => {
        this.filters.attributes.push(attribute)
      })
    }
    this.newSearch()
  },
  beforeRouteUpdate (to, from, next) {
    if (to.query.q) {
      this.searchQuery = to.query.q
    }
    if (to.query.comp) {
      this.filters.components = to.query.comp.split(',')
    }
    if (to.query.children) {
      this.filters.children = to.query.children
    }
    this.newSearch()
  },
  methods: {
    componentsChange (data) {
      this.filters.components = data
    },
    getComponentName (code) {
      // this.addHashToLocation(code)
      let name = ''
      this.$store.state.componentTypeList.forEach(comp => {
        if (comp.componentType === code) {
          name = comp.parentLabel
        }
      })
      return name
    },
    removeTag (tag) {
      this.filters.tags = this.filters.tags.filter(el => {
        return el !== tag
      })
    },
    removeComponent (component) {
      this.filters.components = this.filters.components.filter(el => {
        return el !== component
      })
    },
    naturalSort (data) {
      function compare (a, b) {
        var itemA
        var itemB
        if (isNaN(parseFloat(a.attributeCodeLabel))) {
          itemA = a.attributeCodeLabel.toUpperCase()
          itemB = b.attributeCodeLabel.toUpperCase()
        } else {
          itemA = parseFloat(a.attributeCodeLabel)
          itemB = parseFloat(b.attributeCodeLabel)
        }
        if (itemA < itemB) {
          return -1
        } else if (itemA > itemB) {
          return 1
        }
        return 0
      }
      return data.sort(compare)
    },
    clear () {
      this.filters = {
        attributes: [],
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
    deleteCompnent (component) {
      this.filters.components = _.remove(this.filters.components, n => n !== component)
    },
    addTag (tag) {
      if (this.filters.tags.indexOf(tag) === -1) {
        this.filters.tags.push(tag)
      }
    },
    loadAttributes (attributes) {
      this.searchResultsAttributes = this.$jsonparse(attributes)
      // initialize the attributes
      var keys = Object.keys(this.searchResultsAttributes)
      this.attributeKeys = keys.slice(0, 10)
    },
    filterAttributeKeys () {
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
      if (that.filters.components) {
        that.filters.components.forEach(function (entryType) {
          searchElements.push(
            {
              caseInsensitive: false,
              field: 'componentType',
              mergeCondition: 'AND',
              searchType: 'ENTRYTYPE',
              searchChildren: that.filters.children,
              stringOperation: 'EQUALS',
              value: entryType
            }
          )
        })
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
      if (that.filters.attributes) {
        that.filters.attributes.forEach(function (attribute) {
          let attr = that.$jsonparse(attribute)
          if (attr !== '') {
            searchElements.push(
              {
                keyField: attr.type,
                keyValue: attr.code,
                caseInsensitive: true,
                // mergeCondition: that.filters.attributeCondition,
                mergeCondition: 'AND',
                numberOperations: 'EQUALS',
                searchType: 'ATTRIBUTESET',
                stringOperation: 'EQUALS'
              }
            )
          }
        })
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
          // this may not return full list of all components
          that.componentsList = _.sortBy(response.data.meta.resultTypeStats, [function (o) { return o.componentTypeDescription }])
          that.searchQueryIsDirty = false
          this.loadAttributes(response.data.meta.resultAttributeStats)
        })
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
    },
    newSearch () {
      this.searchPage = 1
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
    },
    removeAttributeFilter (attribute) {
      this.filters.attributes.splice(this.filters.attributes.indexOf(attribute), 1)
      this.filters.attributes = [...this.filters.attributes]
    },
    printAttribute (attribute) {
      let attr = this.$jsonparse(attribute)
      if (attr === null) {
        attr.unit = ''
      }
      return `${attr.typelabel} : ${attr.code} ${attr.unit}`
    },
    copyUrlToClipboard () {
      var urlBeginning
      window.location.href.match(/(.*?)\?/m).forEach(element => {
        urlBeginning = element
      })
      var urlEnding = '?q=' + this.searchQuery +
          '&comp=' + this.filters.components.join(',') +
          '&children=' + this.filters.children.toString() +
          '&tags=' + this.filters.tags.join(',') +
          '&orgs=' + this.filters.organization +
          '&attributes=' + this.filters.attributes.join(',')

      var url = encodeURI(urlBeginning + urlEnding)
      var copyText = this.$refs.urlForClipboard
      copyText.value = url
      copyText.select()
      document.execCommand('copy')
      this.$toasted.show('Search url copied to clipboard', { position: 'top-left', duration: 3000 })
      // alert('Copied the text: ' + copyText.value)
    },
        moreInformation (componentId) {
      router.push({
        name: 'Entry Detail',
        params: {
          id: componentId
        }
      });
    },
    computeHasImage (item) {
      if (item.componentMedia) {
        for (var i = 0; i < item.componentMedia.length; i++) {
          if (item.componentMedia[i].mediaTypeCode === 'IMG') {
            return true;
          }
        }
      }
    },
  },
  watch: {
    filters: {
      handler: function () {
        this.newSearch()
      },
      deep: true
    },
    attributeQuery: _.debounce(function () {
      var keys = Object.keys(this.searchResultsAttributes)
      var regEx = RegExp(this.attributeQuery, 'gi')
      if (this.attributeQuery.trim() === '') {
        this.attributeKeys = keys.slice(0, 10)
      } else {
        this.attributeKeys = keys.filter((v) => regEx.test(v)).slice(0, 10)
      }
    }, 500),
    searchSortField () {
      this.newSearch()
    },
    searchSortOrder () {
      this.newSearch()
    },
    searchPageSize () {
      this.newSearch()
    },
    searchPage () {
      this.submitSearch()
    },
    componentTypeListComputed: function () {
      this.$store.commit('setSelectedComponentTypes', { data: this.componentTypeListComputed })
    }
  },
  computed: {
    offset () {
      return (this.searchPage - 1) * this.searchPageSize
    },
    hideSearchSuggestions () {
      return this.searchQueryIsDirty || this.searchQuery.length === 0
    },
    componentTypeListComputed () {
      return this.filters.components
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
      attributeQuery: '',
      attributeKeys: [],
      filters: {
        components: [],
        tags: [],
        attributes: [],
        organization: '',
        children: false,
        tagCondition: 'AND'
      },
      searchResults: {},
      searchResultsAttributes: {},
      searchQueryIsDirty: false,
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

<style lang="scss" scoped>
@import '../assets/scss/variables.scss';

$side-menu-width: 24em;
$side-menu-width-medium: 30em;
$side-menu-width-large: 34em;
$closed-width: 5em;
$footer-height: 42.4px;

.searchbar {
  border-radius: 2px;
  box-shadow: 0 3px 1px -2px rgba(0,0,0,.2),0 2px 2px 0 rgba(0,0,0,.14),0 1px 5px 0 rgba(0,0,0,.12);
  padding: 0.7em 0.7em 0.7em 1.2em;
  margin-bottom: 0.3em;
  margin-left: auto;
  margin-right: auto;
  font-size: 140%;
  transition: box-shadow 0.7s;
  background-color: #FFF;
}

.dn {
  display: none;
}
.db {
  display: block;
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
.side-menu {
  border-right: 1px solid #DDD;
  overflow-y: auto;
  position: fixed;
  left: 0;
  top: $header-height;
  bottom: $footer-height;
}
.close-btn {
  width: 100%;
  text-align: right;
}
.side-menu.open {
  width: $side-menu-width;
}
.side-menu.closed {
  width: $closed-width;
}
.side-menu-btns {
  position: fixed;
  top: $header-height;
  left: 0;
  margin: 0.5em;
}
.side-menu-content {
  max-width: $side-menu-width;
  padding-right: 2em;
  margin-left: $closed-width;
  overflow-y: auto;
}
.search-block {
  position: fixed;
  min-width: 24em;
  overflow-y: scroll;
  top: $header-height;
  bottom: $footer-height;
  right: 0;
  left: 0;
}
.search-block.open {
  margin-left: $side-menu-width;
}
.search-block.closed {
  margin-left: $closed-width;
}
.more-info {
  cursor: pointer;
}
.more-info:hover {
  transition-duration: 0.2s;
  text-decoration: underline;
}
.v-footer {
  height: $footer-height !important;
}
@media only screen and (min-width: 800px) {
  .search-block.open {
    margin-left: $side-menu-width-medium;
  }
  .side-menu-content {
    max-width: $side-menu-width-medium - $closed-width;
  }
  .side-menu.open {
    width: $side-menu-width-medium;
  }
}
@media only screen and (min-width: 1200px) {
  .search-block.open {
    margin-left: $side-menu-width-large;
  }
  .side-menu-content {
    max-width: $side-menu-width-large - $closed-width;
  }
  .side-menu.open {
    width: $side-menu-width-large;
  }
}
</style>

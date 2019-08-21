<template>
<div>

  <div :class="`side-menu ${showFilters || showOptions ? 'open' : 'closed'}`">
    <!-- CONTROLS -->
    <div class="side-menu-btns mt-4">
      <div>
        <v-btn @click="showFilters = !showFilters; showOptions = false;" small fab dark icon :color="`primary ${showFilters ? 'lighten-4' : ''}`"><v-icon dark>fas fa-filter</v-icon></v-btn>
      </div>
      <div>
        <v-btn @click="showOptions = !showOptions; showFilters = false;" small fab dark icon :color="`primary ${showOptions ? 'lighten-4' : ''}`"><v-icon dark>fas fa-cog</v-icon></v-btn>
      </div>
      <div>
        <v-btn @click="copyUrlToClipboard" small fab icon><v-icon>fas fa-share-alt</v-icon></v-btn>
        <input type="text" value="https://spoonsite.com" id="urlForClipboard" style="position: absolute; left: -1000px; top: -1000px">
      </div>
    </div>

    <div v-if="showOptions || showFilters" style="width: 100%; text-align: right;">
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
            <v-chip close small @input="removeComponent(data.item.key)" >
              <v-avatar class="grey lighten-1">{{ data.item.doc_count }}</v-avatar>
              {{ data.item.label}}
            </v-chip>
          </template>
          <template slot="item" slot-scope="data">
            <v-list-tile-content><v-list-tile-title>({{ data.item.doc_count }}) {{ data.item.label}}</v-list-tile-title></v-list-tile-content>
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
            <v-chip close small @input="deleteTag(data.item.key)" >
              <v-avatar class="grey lighten-1">{{ data.item.doc_count }}</v-avatar>
              {{ data.item.key}}
            </v-chip>
          </template>
          <template slot="item" slot-scope="data">
            <v-list-tile-content><v-list-tile-title>({{ data.item.doc_count }}) {{ data.item.key}}</v-list-tile-title></v-list-tile-content>
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
            ({{ data.item.doc_count }}) {{ data.item.key }}
          </template>
          <template slot="item" slot-scope="data">
            <v-list-tile-content><v-list-tile-title>({{ data.item.doc_count }}) {{ data.item.key }}</v-list-tile-title></v-list-tile-content>
          </template>
        </v-autocomplete>
        <h3 class="pb-3">Attributes</h3>

        <v-text-field
          label="Search Attributes"
          solo
          v-model="attributeQuery"
          placeholder="Search Attributes"
        ></v-text-field>
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
      ></SearchBar>
      <!-- SEARCH FILTERS PILLS -->
      <v-chip
        color="teal"
        text-color="white"
        v-for="component in filters.components"
        :key="component"
      >
        {{ getComponentName(component) }}
        <div class="v-chip__close"><v-icon right @click="removeComponent(component)">cancel</v-icon></div>
      </v-chip>
      <v-chip
        color="light-blue lighten-2"
        text-color="white"
        v-if="this.filters.children && !!this.filters.components && this.filters.components.length > 0"
      >
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
        color="indigo"
        text-color="white"
      >
        {{ filters.organization }}
        <div class="v-chip__close"><v-icon right @click="filters.organization = ''">cancel</v-icon></div>
      </v-chip>
      <v-chip
        close
        v-for="attr in filters.attributes"
        :key="attr"
        @input="removeAttributeFilter(attr)"
      >
        {{ printAttribute(attr) }}
      </v-chip>
      <!-- SEARCH FILTERS PILLS -->
    </div><!-- Search Bar and menu  -->

    <!-- Search Results -->
    <div class="px-3">
      <h2 style="text-align: center" class="mb-2">Search Results</h2>

      <p v-if="totalSearchResults === 0">No Search Results</p>
      <p v-else-if="searchResults && !searchQueryIsDirty" class="pl-5 ma-0">
        {{ offset + 1 }} -
        {{ totalSearchResults > offset + searchPageSize ? offset + searchPageSize : totalSearchResults }}
        of
        {{ totalSearchResults }} results
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
        v-else-if="!!searchResults"
        v-for="item in searchResults"
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
            v-if="!!item.tags && item.tags.length !== 0"
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
    height="auto"
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
  mounted () {
    if (this.$route.query.q) {
      this.searchQuery = this.$route.query.q
    }
    if (this.$route.query.comp) {
      this.filters.components = this.$route.query.comp.split(',')
    }
    if (this.$route.query.children) {
      this.filters.children = this.$route.query.children
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
      console.log(this.searchResultsAttributes)
      // initialize the attributes
      var keys = Object.keys(this.searchResultsAttributes)
      this.attributeKeys = keys.slice(0, 10)
      console.log(this.attributeKeys)
    },
    getCompTypeLabels(entryTypes){
      // This gets the labels for each of the entry types by using the codes return from request
      entryTypes.forEach(entryType => {
        entryType['label'] = this.$store.state.componentTypeList.find(element => {
          return entryType.key == element.componentType
        }).parentLabel
      })
      this.componentsList = entryTypes
    },
    filterAttributeKeys () {
    },
    submitSearch () {
      let that = this
      // a new search clears the data and can trigger a watcher
      // sometimes 2 POST requests get sent out together
      if (that.searchQueryIsDirty) return
      that.searchQueryIsDirty = true

      // build search request here
      var searchFilters = {
        "query": '',
        'page': 0,
        'pageSize': 10,
        'componentTypes': [],
        'includeChildren': true,
        'organization': '',
        'stringAttributes': [],
        'tags': [],
        'sortOrder': '',
        'sortField': ''
      }

      searchFilters.query = ( this.searchQuery ? this.searchQuery : searchFilters.query )
      searchFilters.page = ( this.searchPage ? this.searchPage : searchFilters.page )
      searchFilters.pageSize = ( this.searchPageSize ? this.searchPageSize : searchFilters.pageSize )
      searchFilters.componentTypes = ( this.filters.components ? this.filters.components : searchFilters.componentTypes )
      searchFilters.includeChildren = ( this.filters.includeChildren ? this.filters.includeChildren : searchFilters.includeChildren )
      searchFilters.organization = ( this.filters.organization ? this.filters.organization : searchFilters.organization )
      // searchFilters.stringAttributes = ( this.filters.attributes ? this.filters.attributes : searchFilters.attributes )
      searchFilters.tags = ( this.filters.tags ? this.filters.tags : searchFilters.tags )
      searchFilters.sortField = ( this.searchSortField ? this.searchSortField : searchFilters.sortField )
      searchFilters.sortOrder = ( this.searchSortOrder ? this.searchSortOrder : searchFilters.sortOrder )

      if (this.filters.attributes) {
        this.filters.attributes.forEach(attribute => {
          searchFilters.stringAttributes.push(JSON.parse(attribute))
        })
      }

      this.$http
        .post(
          '/openstorefront/api/v2/service/search',
            searchFilters
        ).then(response => {

          console.log(response)

          that.searchResults = response.data.hits.hits.map(e => e._source)
          that.totalSearchResults = response.data.hits.total.value
          that.organizationsList = response.data.aggregations['sterms#by_organization'].buckets
          that.tagsList = response.data.aggregations['sterms#by_tag'].buckets

          var entryTypes = response.data.aggregations['sterms#by_category'].buckets
          this.getCompTypeLabels(entryTypes)

          // Attributes List
          // that.organizationsList = response.data.aggregations['sterms#by_attribute'].buckets
          // console.log(response.data.aggregations['sterms#by_attribute_type'].buckets)
          // console.log(response.data.aggregations['sterms#by_attribute_label'].buckets)
          that.searchQueryIsDirty = false
        }).catch(err => console.log(err))

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
          // that.searchResults = response
          // that.totalSearchResults = response.data.totalNumber
          // that.organizationsList = _.sortBy(response.data.meta.resultOrganizationStats, [function (o) { return o.organization }])
          // that.tagsList = _.sortBy(response.data.meta.resultTagStats, [function (o) { return o.tagLabel }])
          // this may not return full list of all components
          // that.componentsList = _.sortBy(response.data.meta.resultTypeStats, [function (o) { return o.componentTypeDescription }])
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
      return `${attr.typelabel} : ${attr.code} ${attr.unit}`
    },
    copyUrlToClipboard () {
      var urlBeginning
      window.location.href.match(/(.*?)\?/m).forEach(element => {
        urlBeginning = element
      })
      var url = encodeURI(urlBeginning +
          '?q=' + this.searchQuery +
          '&comp=' + this.filters.components.join(',') +
          '&children=' + this.filters.children.toString() +
          '&tags=' + this.filters.tags.join(',') +
          '&orgs=' + this.filters.organization +
          '&attributes=' + this.filters.attributes.join(','))

      var copyText = document.getElementById('urlForClipboard')
      copyText.value = url
      copyText.select()
      document.execCommand('copy')
      this.$toasted.show('Search url copied to clip board', {position: 'top-left', duration : 3000})
      // alert('Copied the text: ' + copyText.value)
    }
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
$side-menu-width: 24em;
$side-menu-width-medium: 30em;
$side-menu-width-large: 34em;
$closed-width: 5em;
$footer-height: 10em;

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
  position: fixed;
  height: 100%;
  padding-bottom: $footer-height;
}
.side-menu.open {
  width: $side-menu-width;
}
.side-menu.closed {
  width: $closed-width;
}
.side-menu-btns {
  position: fixed;
  margin: 0.5em;
}
.side-menu-content {
  height: 100%;
  max-width: $side-menu-width;
  padding-right: 2em;
  margin-left: $closed-width;
  overflow: auto;
}
.search-block {
  min-width: 24em;
}
.search-block.open {
  margin-left: $side-menu-width;
}
.search-block.closed {
  margin-left: $closed-width;
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

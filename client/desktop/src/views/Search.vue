<template>
  <div>
    <div :class="`side-menu ${showFilters || showOptions ? 'open' : 'closed'}`">
      <!-- CONTROLS -->
      <div class="side-menu-btns">
        <v-tooltip right>
          <template v-slot:activator="{ on }">
            <v-btn
              class="db"
              @click="
                showFilters = !showFilters
                showOptions = false
              "
              small
              fab
              dark
              icon
              :color="`primary ${showFilters ? 'lighten-4' : ''}`"
              v-on="on"
            >
              <v-icon dark>fas fa-filter</v-icon>
            </v-btn>
          </template>
          <span>Search Filters</span>
        </v-tooltip>
        <v-tooltip right>
          <template v-slot:activator="{ on }">
            <v-btn
              class="db"
              @click="
                showOptions = !showOptions
                showFilters = false
              "
              small
              fab
              dark
              icon
              :color="`primary ${showOptions ? 'lighten-4' : ''}`"
              v-on="on"
            >
              <v-icon dark>fas fa-cog</v-icon>
            </v-btn>
          </template>
          <span>Search Options</span>
        </v-tooltip>
        <v-tooltip right>
          <template v-slot:activator="{ on }">
            <v-btn
              class="db"
              @click="
                sortComparisonData()
                showComparison = true
              "
              :disabled="!(comparisonList.length >= 2)"
              small
              fab
              icon
              v-on="on"
            >
              <v-icon>fas fa-columns</v-icon>
            </v-btn>
          </template>
          <span>Compare</span>
        </v-tooltip>
        <v-tooltip right>
          <template v-slot:activator="{ on }">
            <v-btn class="db" @click="copyUrlToClipboard" small fab icon v-on="on">
              <v-icon>fas fa-share-alt</v-icon>
            </v-btn>
          </template>
          <span>Share Search</span>
        </v-tooltip>
        <input
          type="text"
          value="https://spoonsite.com"
          ref="urlForClipboard"
          style="position: absolute; left: -1000px; top: -1000px"
        />
      </div>

      <div v-if="showOptions || showFilters" class="close-btn">
        <v-btn
          icon
          @click="
            showOptions = false
            showFilters = false
          "
        >
          <v-icon>fas fa-times</v-icon>
        </v-btn>
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
          <v-select v-model="searchSortField" :items="searchSortFields"></v-select>
          <h3>Page Size</h3>
          {{ searchPageSize }}
          <v-slider v-model="searchPageSize" step="4" min="4" thumb-label></v-slider>
          <h2>Display Options</h2>
          <v-checkbox
            v-model="displayOptions.organization"
            :label="'Organization'"
            class="checkbox-spacing"
          ></v-checkbox>
          <v-checkbox v-model="displayOptions.category" :label="'Category'" class="checkbox-spacing"></v-checkbox>
          <v-checkbox v-model="displayOptions.tags" :label="'Tags'" class="checkbox-spacing"></v-checkbox>
          <v-checkbox
            v-model="displayOptions.userRating"
            :label="'Average User Rating'"
            class="checkbox-spacing"
          ></v-checkbox>
          <v-checkbox v-model="displayOptions.description" :label="'Description'" class="checkbox-spacing"></v-checkbox>
          <v-checkbox
            v-model="displayOptions.lastUpdated"
            :label="'Last Updated'"
            class="checkbox-spacing"
          ></v-checkbox>
          <v-checkbox
            v-model="displayOptions.approvalDate"
            :label="'Approval Date'"
            class="checkbox-spacing"
          ></v-checkbox>
          <v-btn block class="primary" @click="resetOptions()">Reset Options</v-btn>
        </div>
        <!-- SEARCH OPTIONS -->

        <!-- SEARCH FILTERS -->
        <div v-if="showFilters">
          <h2>Search Filters</h2>
          <v-btn block class @click="clear()">Clear Filters</v-btn>
          <v-autocomplete
            v-model="filters.entryType"
            :items="componentsList"
            item-text="label"
            item-value="key"
            label="Category"
            clearable
          >
            <template slot="selection" slot-scope="data">({{ data.item.doc_count }}) {{ data.item.label }}</template>
            <template slot="item" slot-scope="data">
              <v-list-item-content>
                <v-list-item-title>({{ data.item.doc_count }}) {{ data.item.label }}</v-list-item-title>
              </v-list-item-content>
            </template>
          </v-autocomplete>
          <v-checkbox class="ma-0" color="black" label="Include Sub-Categories" v-model="filters.children"></v-checkbox>
          <v-autocomplete
            v-model="filters.tags"
            hide-details
            :items="tagsList"
            :disabled="!tagsList || tagsList.length === 0"
            item-text="key"
            item-value="key"
            :label="!tagsList || tagsList.length === 0 ? 'No Tags' : 'Tags'"
            multiple
            small-chips
            clearable
            class="pb-3"
          >
            <template slot="selection" slot-scope="data">
              <v-chip close small @click:close="removeTag(data.item.key)">
                <v-avatar left class="grey lighten-1 mr-2">{{ data.item.doc_count }}</v-avatar>
                {{ data.item.key }}
              </v-chip>
            </template>
            <template slot="item" slot-scope="data">
              <v-list-item-content>
                <v-list-item-title>({{ data.item.doc_count }}) {{ data.item.key }}</v-list-item-title>
              </v-list-item-content>
            </template>
          </v-autocomplete>
          <v-autocomplete
            v-model="filters.organization"
            :items="organizationsList"
            label="Organization"
            item-text="key"
            item-value="key"
            clearable
          >
            <template slot="selection" slot-scope="data"
              >({{ organizationsMap.get(data.item) }}) {{ data.item }}</template
            >
            <template slot="item" slot-scope="data">
              <v-list-item-content>
                <v-list-item-title>({{ organizationsMap.get(data.item) }}) {{ data.item }}</v-list-item-title>
              </v-list-item-content>
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
              style="width: 90%;"
            />
            <v-icon v-if="attributeQuery !== ''" class="search-icon" @click="attributeQuery = ''">clear</v-icon>
          </div>
          <div>
            <v-chip close v-for="attr in filters.attributes" :key="attr" @input="removeAttributeFilter(attr)">{{
              printAttribute(attr)
            }}</v-chip>
          </div>
          <div v-if="Object.keys(searchResultsAttributes).length !== 0">
            Showing {{ attributeKeys.length }} of {{ Object.keys(searchResultsAttributes).length }} attributes
          </div>
          <div v-if="Object.keys(attributeKeys).length === 0">No Attributes</div>
          <v-expansion-panels accordion>
            <v-expansion-panel v-for="key in attributeKeys.slice(0, 9)" :key="key">
              <v-expansion-panel-header>
                {{
                  searchResultsAttributes[key].label +
                    (searchResultsAttributes[key].attributeUnit
                      ? ' (' + searchResultsAttributes[key].attributeUnit + ') '
                      : '')
                }}
              </v-expansion-panel-header>
              <v-expansion-panel-content>
                <v-container class="pt-0" fluid>
                  <v-checkbox
                    v-for="code in searchResultsAttributes[key].codes"
                    :key="key + code.code"
                    color="black"
                    v-model="filters.attributes"
                    :value="
                      JSON.stringify({
                        type: key,
                        unit: searchResultsAttributes[key].attributeUnit,
                        typelabel: searchResultsAttributes[key].label,
                        code: code.code
                      })
                    "
                    hide-details
                  >
                    <template slot="label">
                      <div>{{ code.code | crushNumericString }} ({{ code.count }})</div>
                    </template>
                  </v-checkbox>
                </v-container>
              </v-expansion-panel-content>
            </v-expansion-panel>
          </v-expansion-panels>
        </div>
        <!-- SEARCH FILTERS -->
      </div>
      <!-- SIDE MENU CONTENT -->
    </div>
    <!--SIDE MENU -->

    <div :class="`search-block pt-4 ${showFilters || showOptions ? 'open' : 'closed'}`">
      <!-- Search Bar and menu  -->
      <div class="centeralign px-3 mb-5" style="max-width: 46em;">
        <SearchBar
          @submitSearch="submitSearch"
          v-on:clear="submitSearch()"
          :hideSuggestions="hideSearchSuggestions"
          v-model="searchQuery"
          :overlaySuggestions="true"
        ></SearchBar>
        <!-- SEARCH FILTERS PILLS -->
        <div style="padding-top:20px; ">
          <v-chip v-if="filters.entryType" close @click:close="filters.entryType = ''">
            <v-avatar left>
              <v-icon small>fas fa-layer-group</v-icon>
            </v-avatar>
            {{ getComponentName(filters.entryType) }}
          </v-chip>
          <v-chip
            text-color="black"
            v-if="this.filters.children && this.filters.entryType"
            close
            @click:close="filters.children = !filters.children"
          >
            <v-avatar left>
              <v-icon small>fas fa-check-square</v-icon> </v-avatar
            >Include Sub-Catagories
          </v-chip>
          <v-chip v-for="tag in filters.tags" :key="tag" close @click:close="removeTag(tag)">
            <v-avatar left>
              <v-icon small>fas fa-tag</v-icon>
            </v-avatar>
            {{ tag }}
          </v-chip>
          <v-chip v-if="filters.organization" close @click:close="filters.organization = ''">
            <v-avatar left>
              <v-icon small>fas fa-university</v-icon>
            </v-avatar>
            {{ filters.organization }}
          </v-chip>
          <v-chip close @click:close="removeAttributeFilter(attr)" v-for="attr in filters.attributes" :key="attr">
            <v-avatar left>
              <v-icon small>fas fa-clipboard-list</v-icon>
            </v-avatar>
            {{ printAttribute(attr) }}
          </v-chip>
        </div>
        <!-- SEARCH FILTERS PILLS -->
      </div>
      <!-- Search Bar and menu  -->

      <!-- Search Results -->
      <div class="px-3">
        <h2 v-if="searchQuery === ''" style="text-align: center" class="mb-2">Search Results for ALL</h2>
        <h2 v-else style="text-align: center" class="mb-2">Search Results for "{{ searchQuery }}"</h2>

        <p v-if="totalSearchResults === 0">No Search Results</p>
        <p v-else-if="searchResults && !searchQueryIsDirty" class="pl-5 ma-0">
          {{ offset + 1 }} -
          {{ totalSearchResults > offset + searchPageSize ? offset + searchPageSize : totalSearchResults }}
          of
          {{ totalSearchResults }} results
        </p>

        <!-- SEARCH RESULTS DATA -->
        <div class="search-results" style="display: flex; flex-wrap: wrap; align-items: stretch;">
          <v-layout row justify-center align-center v-if="searchQueryIsDirty">
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
          <v-flex
            v-else-if="!!searchResults"
            v-for="item in searchResults"
            :key="item.name"
            xs12
            sm6
            md4
            lg4
            xl3
            style="margin-bottom: 10px;"
          >
            <v-card class="item">
              <div class="item-header">
                <img
                  v-if="item.includeIconInSearch && item.componentTypeIconUrl"
                  :src="'/openstorefront/' + item.componentTypeIconUrl"
                  style="min-width: 40px; max-height: 40px; margin-right: 15px"
                />
                <h3 class="headline more-info">
                  <router-link :to="'/entry-detail/' + item.componentId">{{ item.name }}</router-link>
                </h3>
              </div>
              <v-divider></v-divider>
              <div class="item-body">
                <div class="item-properties">
                  <span v-if="displayOptions.organization">
                    <v-chip small class="organization-chip" @click="addOrganization(item.organization)">
                      <v-icon style="font-size: 16px; padding-right: 4px;">fas fa-university</v-icon>
                      <div class="tag-links">{{ item.organization }}</div>
                    </v-chip>
                  </span>
                  <div class="comp-type-wrapper" v-if="displayOptions.category">
                    <v-chip
                      v-if="item.componentTypeDescription.includes('>')"
                      style="padding: 5px 0px;"
                      large
                      @click="addComponentType(item.componentType)"
                    >
                      <div class="tag-links two-line-chips">
                        {{ getFirstCompType(item.componentTypeDescription) }}
                        <br />
                        {{ getSecondCompType(item.componentTypeDescription) }}
                      </div>
                    </v-chip>
                    <v-chip v-else @click="addComponentType(item.componentType)">
                      <div class="tag-links">{{ item.componentTypeDescription }}</div>
                    </v-chip>
                  </div>
                  <div class="tag-wrapper" v-if="!!item.tags && item.tags.length !== 0 && displayOptions.tags">
                    <span v-for="tag in item.tags" :key="tag.text" class="tag-links" @click="addTag(tag.text)">
                      <v-icon style="font-size: 14px; color: rgb(248, 197, 51);">fas fa-tag</v-icon>
                      {{ tag.text }}
                    </span>
                  </div>
                  <p v-if="displayOptions.userRating">
                    <star-rating
                      :rating="item.averageRating"
                      :read-only="true"
                      :show-rating="false"
                      :increment="0.01"
                      inline
                      :star-size="17"
                    ></star-rating>
                    ({{ item.numberOfRatings }})
                  </p>
                </div>
                <v-divider></v-divider>
                <div class="item-details">
                  <div class="description-wrapper" v-if="displayOptions.description">
                    {{ shortenDescription(item.description) }}
                  </div>
                  <div class="item-details-bottom">
                    <div>
                      <p v-if="displayOptions.lastUpdated">
                        <strong>Last Updated:</strong>
                        <!-- {{ Date(item.updateDts) | formatDate }} -->
                        {{ item.updateDts | formatDate }}
                      </p>
                      <p v-if="displayOptions.approvalDate">
                        <strong>Approved Date:</strong>
                        <!-- {{ Date(item.approvedDts) | formatDate }} -->
                        {{ item.approvedDts | formatDate }}
                      </p>
                    </div>
                    <div class="compare-box">
                      <input type="checkbox" v-model="comparisonList" :value="item" :id="item.componentId" />
                      <label :for="item.componentId">Add to Compare</label>
                    </div>
                  </div>
                </div>
              </div>
            </v-card>
          </v-flex>
        </div>
        <!-- Search Results -->
        <!-- Comparison Table Dialog -->
        <v-dialog
          v-model="showComparison"
          justify="center"
          max-width="85vw"
          :class="{ 'dialog-scroll': showComparison }"
        >
          <v-card class="dialog-scroll">
            <ModalTitle title="Compare" @close="showComparison = false" />
            <v-card-text>
              <div class="scrollable">
                <table>
                  <thead>
                    <tr>
                      <th
                        v-for="(component, position) in this.comparisonDataHeaders"
                        :class="changeTableClass(position)"
                        :key="component.text"
                      >
                        {{ component.text }}
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="attribute in this.comparisonDataDisplay" :key="attribute.name">
                      <td
                        v-for="(compAtt, position, col) in attribute"
                        :class="changeTableClass(position)"
                        :key="compAtt.name"
                      >
                        {{ compAtt }}
                        <span class="tooltip" v-if="attribute.name != 'Attributes'"
                          >{{ attribute.name }} of {{ comparisonDataHeaders[col].text }}</span
                        >
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </v-card-text>
          </v-card>
        </v-dialog>
        <!-- Comparison Table Dialog -->
      </div>

      <!-- Pagination -->
      <v-footer fixed color="#FFF" style="border-top: 1px solid #DDD; margin-bottom: 12px">
        <v-layout justify-center>
          <v-pagination v-model="searchPage" :length="getNumPages()" total-visible="5"></v-pagination>
        </v-layout>
      </v-footer>
    </div>
  </div>
</template>

<script>
import _ from 'lodash'
import StarRating from 'vue-star-rating'
import SearchBar from '@/components/SearchBar'
import ModalTitle from '@/components/ModalTitle'
import crush from '@/util/scientificToDecimal'

export default {
  name: 'SearchPage',
  components: {
    SearchBar,
    StarRating,
    ModalTitle
  },
  created() {
    this.$store.watch(
      state => state.selectedComponentTypes,
      (newValue, oldValue) => {
        this.newSearch()
      }
    )
    let sortOrder = JSON.parse(window.localStorage.getItem('searchSortOrder'))
    let sortField = JSON.parse(window.localStorage.getItem('searchSortField'))
    let pageSize = JSON.parse(window.localStorage.getItem('searchPageSize'))
    let displayOptions = JSON.parse(window.localStorage.getItem('displayOptions'))
    this.displayOptions = displayOptions || this.displayOptions
    this.searchSortOrder = sortOrder || this.searchSortOrder
    this.searchSortField = sortField || this.searchSortField
    this.searchPageSize = pageSize || this.searchPageSize
    // If the cached options is no longer available, use default
    let hasSearchSortField = this.searchSortFields.filter(e => e.value === this.searchSortField).length > 0
    if (!hasSearchSortField) {
      this.searchSortField = '_score'
    }
    window.addEventListener('resize', this.hideOrShowFilters)
  },
  mounted() {
    this.parseFiltersFromUrl(this.$route.query)
    this.newSearch()
    this.hideOrShowFilters()
  },
  beforeRouteUpdate(to, from, next) {
    this.parseFiltersFromUrl(to.query)
    this.newSearch()
    next()
  },
  methods: {
    parseFiltersFromUrl(params) {
      if (params.q) {
        this.searchQuery = params.q
      }
      if (params.comp) {
        this.filters.entryType = params.comp
      }
      if (params.children) {
        this.filters.children = params.children === 'true'
      }
      if (params.tags) {
        this.filters.tags = params.tags.split(',')
      }
      if (params.organization) {
        this.filters.organization = params.organization
      }
      if (params.attributes) {
        this.filters.attributes = []
        this.filters.attributes = JSON.parse(params.attributes)
      }
      if (params.searchoptions) {
        this.searchoptions = params.searchoptions.split(',')
      }
    },
    getComponentName(code) {
      let name = ''
      if (this.$store.state.componentTypeList === undefined) {
        this.$store.dispatch('getComponentTypeList')
      }
      this.$store.state.componentTypeList.forEach(comp => {
        if (comp.componentType === code) {
          name = comp.parentLabel
        }
      })
      return name
    },
    removeTag(tag) {
      this.filters.tags = this.filters.tags.filter(el => {
        return el !== tag
      })
    },
    addComponentType(compType) {
      this.filters.entryType = compType
    },
    addOrganization(org) {
      this.filters.organization = org
    },
    clear() {
      this.filters = {
        attributes: [],
        entryType: '',
        tags: [],
        organization: '',
        children: false
      }
    },
    resetOptions() {
      this.searchPageSize = 12
      this.searchSortField = 'searchScore'
      this.searchSortOrder = 'DESC'
      this.displayOptions.organization = true
      this.displayOptions.category = true
      this.displayOptions.tags = true
      this.displayOptions.userRating = false
      this.displayOptions.description = true
      this.displayOptions.lastUpdated = true
      this.displayOptions.approvalDate = true
    },
    addTag(tag) {
      if (this.filters.tags.indexOf(tag) === -1) {
        this.filters.tags.push(tag)
      }
    },
    parseAttributesFromSearchResponse(attributesAggregation) {
      let that = this
      that.attributeKeys = []
      that.searchResultsAttributes = {}

      if (that.$store.state.attributeMap === undefined) {
        that.$store.dispatch('getAttributeMap')
      }

      let source = {}
      let unit = ''

      // This is a map to increase speed of building the searchResultsAttributes dict
      let codesMap = {}

      // Iterate over each returned attribute and make an entry in the dict or add a code if the entry already exists
      attributesAggregation.forEach(el => {
        source = el._source
        // If the attribute does not exist in the table, create a new entry for it and add the attribute code to the new entry
        if (!that.searchResultsAttributes.hasOwnProperty(source.type)) {
          unit = that.$store.state.attributeMap[source.type]
            ? that.$store.state.attributeMap[source.type].attributeUnit
            : undefined
          that.searchResultsAttributes[source.type] = {
            codes: [],
            label: source.typeLabel,
            attributeUnit: unit,
            code: source.type
          }
          that.searchResultsAttributes[source.type].codes.push({
            code: source.label,
            count: 1
          })
          codesMap[source.type] = {}
          codesMap[source.type][source.label] = 0
        } else {
          // If the attribute is in the table but the attribute code is not, then append the attribute code
          if (!codesMap[source.type].hasOwnProperty(source.label)) {
            that.searchResultsAttributes[source.type].codes.push({
              code: source.label,
              count: 1
            })
            codesMap[source.type][source.label] = 0
          } else {
            // If the attribute code is in the table, increment the count
            codesMap[source.type][source.label] = codesMap[source.type][source.label] + 1
            let objIndex = that.searchResultsAttributes[source.type].codes.findIndex(obj => obj.code === source.label)
            let count = that.searchResultsAttributes[source.type].codes[objIndex].count
            that.searchResultsAttributes[source.type].codes[objIndex] = {
              code: source.label,
              count: count + 1
            }
          }
        }
      })

      // Get the first 10 attributes
      that.attributeKeys = Object.keys(that.searchResultsAttributes)
        .sort()
        .slice(0, 10)
    },
    getCompTypeLabels(entryTypes) {
      let that = this
      // This gets the labels for each of the entry types by using the codes return from request

      if (that.$store.state.componentTypeList === undefined) {
        that.$store.dispatch('getComponentTypeList')
      }

      entryTypes.forEach(entryType => {
        entryType['label'] = that.$store.state.componentTypeList.find(element => {
          return entryType.key === element.componentType
        }).parentLabel
      })
      this.componentsList = entryTypes

      if (this.componentsList.length === 0 && this.filters.entryType !== '') {
        let entryTypeLabel = that.$store.state.componentTypeList.find(element => {
          return this.filters.entryType === element.componentType
        }).parentLabel

        this.componentsList.push({
          doc_count: 0,
          key: this.filters.entryType,
          label: entryTypeLabel
        })
      }
    },
    submitSearch() {
      this.comparisonList = []
      let that = this
      // a new search clears the data and can trigger a watcher
      // sometimes 2 POST requests get sent out together
      if (that.searchQueryIsDirty) return
      that.searchQueryIsDirty = true

      let cachedOptions = window.localStorage.getItem('searchOptions')

      let searchFilterOptions = {
        canUseNameInSearch: cachedOptions.includes('Name'),
        canUseDescriptionInSearch: cachedOptions.includes('Description'),
        canUseOrganizationsInSearch: cachedOptions.includes('Organization'),
        canUseAttributesInSearch: cachedOptions.includes('Vitals'),
        canUseTagsInSearch: cachedOptions.includes('Tags')
      }

      // Default values
      let searchFilters = {
        query: '',
        page: 0,
        pageSize: 12,
        componentType: '',
        includeChildren: true,
        organization: '',
        attributes: null,
        tags: [],
        sortOrder: '',
        sortField: '',
        searchFilterOptions: searchFilterOptions
      }

      // Use values from ui if available
      searchFilters.query = this.searchQuery ? this.searchQuery : searchFilters.query
      searchFilters.page = this.searchPage ? this.searchPage : searchFilters.page
      searchFilters.pageSize = this.searchPageSize ? this.searchPageSize : searchFilters.pageSize
      searchFilters.componentType = this.filters.entryType ? this.filters.entryType : searchFilters.componentType
      searchFilters.organization = this.filters.organization ? this.filters.organization : searchFilters.organization
      searchFilters.includeChildren = this.filters.children

      let tags = []
      if (this.filters.tags != null) {
        this.filters.tags.forEach(tag => {
          tags.push(tag)
        })
      }

      searchFilters.tags = tags
      searchFilters.sortField = this.searchSortField ? this.searchSortField : searchFilters.sortField
      searchFilters.sortOrder = this.searchSortOrder ? this.searchSortOrder : searchFilters.sortOrder

      if (this.filters.attributes) {
        searchFilters.attributes = []
        this.filters.attributes.forEach(attribute => {
          searchFilters.attributes.push(JSON.parse(attribute))
        })
        searchFilters.attributes = JSON.stringify(searchFilters.attributes)
      }

      // need to check that the route is not already in the history
      // this occurs when search comes from the landing page
      let currentURL = window.location.href.toString().split('#')[1]
      if ('#' + currentURL !== this.searchUrl()) {
        window.history.pushState({}, '', this.searchUrl())
      }

      this.$http
        .post('/openstorefront/api/v2/service/search', searchFilters)
        .then(response => {
          that.searchResults = response.data.hits.hits.map(e => e._source)
          that.totalSearchResults = response.data.hits.total.value

          // Organizations
          that.organizationsList = []
          that.organizationsMap.clear()

          response.data.aggregations['sterms#by_organization'].buckets.forEach(el => {
            that.organizationsList.push(el.key)
            that.organizationsMap.set(el.key, el.doc_count)
          })

          that.tagsList = response.data.aggregations['sterms#by_tag'].buckets

          let entryTypes = response.data.aggregations['sterms#by_category'].buckets
          this.getCompTypeLabels(entryTypes)

          let attributesAggregation =
            response.data.aggregations['nested#by_attribute_type']['top_hits#attribute'].hits.hits
          this.parseAttributesFromSearchResponse(attributesAggregation)

          that.searchQueryIsDirty = false
        })
        .catch(err => console.error(err))
        .finally(() => {
          that.searchQueryIsDirty = false
        })
    },
    newSearch() {
      this.searchPage = 1
      this.submitSearch()
    },
    getNumPages() {
      // compute number of pages of data based on page size
      if (this.totalSearchResults % this.searchPageSize === 0) return this.totalSearchResults / this.searchPageSize - 1
      return Math.floor(this.totalSearchResults / this.searchPageSize) + 1
    },
    removeAttributeFilter(attribute) {
      this.filters.attributes.splice(this.filters.attributes.indexOf(attribute), 1)
      this.filters.attributes = [...this.filters.attributes]
    },
    printAttribute(attribute) {
      let attr = JSON.parse(attribute)
      let attributeType = this.$store.state.attributeMap[attr.type]
      if (attributeType === undefined) {
        this.$store.dispatch('getAttributeMap').then(() => {
          attributeType = this.$store.state.attributeMap[attr.type]
          if (attr === null || attributeType === undefined || attributeType.attributeUnit === undefined) {
            attributeType = {}
            attributeType.attributeUnit = ''
          }
          return `${attributeType.description} : ${crush.crushNumericString(attr.code)} ${attributeType.attributeUnit}`
        })
      } else {
        if (attr === null || attributeType.attributeUnit === undefined) {
          attributeType.attributeUnit = ''
        }
        return `${attributeType.description} : ${crush.crushNumericString(attr.code)} ${attributeType.attributeUnit}`
      }
    },
    copyUrlToClipboard() {
      var url = encodeURI(window.location.origin + window.location.pathname + this.searchUrl())
      var copyText = this.$refs.urlForClipboard
      copyText.value = url
      copyText.select()
      document.execCommand('copy')
      this.$toasted.show('Search url copied to clipboard', {
        position: 'top-left',
        duration: 3000
      })
    },
    getFirstCompType(componentType) {
      var index = componentType.indexOf('>')
      if (index !== -1) {
        return componentType.slice(0, index)
      }
    },
    getSecondCompType(componentType) {
      var index = componentType.indexOf('>')
      if (index !== -1) {
        return componentType.slice(index)
      }
    },
    shortenDescription(desc) {
      var descriptionLength = 200
      return desc.slice(0, descriptionLength) + '...'
    },
    sortComparisonData() {
      this.deleteAllTableData()
      this.comparisonDataHeaders.push({
        text: '',
        value: 'name',
        sortable: false
      })
      for (var component in this.comparisonList) {
        this.comparisonDataHeaders.push({
          text: this.comparisonList[component].name,
          value: 'component' + component,
          sortable: false
        })
      }

      var possibleAttributes = this.getListOfComparableAttributes()
      this.formatDataForDisplay(possibleAttributes)
      this.countNumberOfSimilarities()
      this.sortListByCommonalities()
      this.addDescriptionTableData()
    },
    formatDataForDisplay(possibleAttributes) {
      for (var attribute in possibleAttributes) {
        this.comparisonDataDisplay.push({ name: possibleAttributes[attribute] })
        for (var component in this.comparisonList) {
          for (var componentAttribute in this.comparisonList[component].attributes) {
            if (
              possibleAttributes[attribute] === this.comparisonList[component].attributes[componentAttribute].typeLabel
            ) {
              var unit = this.getAttributeUnit(possibleAttributes[attribute])
              this.comparisonDataDisplay[attribute]['name'] = possibleAttributes[attribute]
              this.setDecimalSizeLimit(component, componentAttribute)
              this.comparisonDataDisplay[attribute]['component' + component] =
                this.comparisonList[component].attributes[componentAttribute].label + unit
            }
          }
          if (!this.comparisonDataDisplay[attribute].hasOwnProperty('component' + component)) {
            this.comparisonDataDisplay[attribute]['component' + component] = '--'
          }
        }
      }
    },
    getAttributeUnit(attributeCompared) {
      for (var attribute in this.searchResultsAttributes) {
        if (
          this.searchResultsAttributes[attribute].label === attributeCompared &&
          this.searchResultsAttributes[attribute].attributeUnit != null
        ) {
          return ' ' + this.searchResultsAttributes[attribute].attributeUnit
        }
      }
      return ''
    },
    countNumberOfSimilarities() {
      for (var attribute in this.comparisonDataDisplay) {
        var counter = 0
        for (var componentAttribute in this.comparisonDataDisplay[attribute]) {
          if (this.comparisonDataDisplay[attribute][componentAttribute] !== '--' && componentAttribute !== '--') {
            counter++
          }
        }
        this.comparisonDataDisplay[attribute]['similarities'] = counter
      }
    },
    sortListByCommonalities() {
      this.comparisonDataDisplay.sort(function(similar1, similar2) {
        return similar2.similarities - similar1.similarities
      })
      for (var data in this.comparisonDataDisplay) {
        delete this.comparisonDataDisplay[data].similarities
      }
    },
    getListOfComparableAttributes() {
      var possibleAttributes = []
      for (var component in this.comparisonList) {
        for (var attribute in this.comparisonList[component].attributes) {
          if (!possibleAttributes.includes(this.comparisonList[component].attributes[attribute].typeLabel)) {
            possibleAttributes.push(this.comparisonList[component].attributes[attribute].typeLabel)
          }
        }
      }
      return possibleAttributes
    },
    addDescriptionTableData() {
      this.comparisonDataDisplay.unshift({ name: 'Organization' })
      for (var component in this.comparisonList) {
        this.comparisonDataDisplay[0]['component' + component] = this.comparisonList[component].organization
      }
      this.comparisonDataDisplay.unshift({ name: 'Description' })
      for (var item in this.comparisonList) {
        this.comparisonDataDisplay[0]['component' + item] = this.comparisonList[item].description
      }
      this.comparisonDataDisplay.unshift({ name: 'Entry Type' })
      for (var comp in this.comparisonList) {
        this.comparisonDataDisplay[0]['component' + comp] = this.comparisonList[comp].componentTypeDescription
      }
    },
    setDecimalSizeLimit(component, componentAttribute) {
      if (
        !isNaN(this.comparisonList[component].attributes[componentAttribute].label) &&
        this.comparisonList[component].attributes[componentAttribute].label.includes('.')
      ) {
        if (this.comparisonList[component].attributes[componentAttribute].label.split('.')[1].length > 4) {
          var numericAttribute = parseFloat(this.comparisonList[component].attributes[componentAttribute].label)
          this.comparisonList[component].attributes[componentAttribute].label = numericAttribute.toFixed(4).toString()
        }
      }
    },
    deleteAllTableData() {
      this.comparisonDataHeaders = []
      this.comparisonDataDisplay = []
    },
    changeTableClass(position) {
      return {
        'left-column': position === 'name',
        'top-corner': position === 0,
        'table-column': position !== 'name' && position !== 0
      }
    },
    searchUrl() {
      let searchOptions = window.localStorage.getItem('searchOptions')
      let url =
        '#/search?q=' +
        (this.searchQuery ? this.searchQuery : '') +
        (this.filters.entryType ? '&comp=' + this.filters.entryType : '') +
        (this.filters.children ? '&children=' + this.filters.children : '') +
        (this.filters.organization ? '&organization=' + this.filters.organization : '') +
        (this.filters.attributes.length > 0 ? '&attributes=' + JSON.stringify(this.filters.attributes) : '') +
        (this.filters.tags.length > 0 ? '&tags=' + this.filters.tags.join(',') : '') +
        '&searchoptions=' +
        JSON.parse(searchOptions).join(',')
      return url
    },
    hideOrShowFilters() {
      if (window.innerWidth < 700) {
        this.showFilters = false
      } else {
        this.showFilters = true
      }
    }
  },
  watch: {
    filters: {
      handler: function() {
        this.newSearch()
      },
      deep: true
    },
    attributeQuery: _.debounce(function() {
      var keys = Object.keys(this.searchResultsAttributes)
      var regEx = RegExp(this.attributeQuery, 'gi')
      if (this.attributeQuery.trim() === '') {
        this.attributeKeys = keys.slice(0, 10)
      } else {
        this.attributeKeys = keys.filter(v => regEx.test(this.searchResultsAttributes[v].label)).slice(0, 10)
      }
    }, 500),
    searchSortField() {
      window.localStorage.setItem('searchSortField', JSON.stringify(this.searchSortField))
      this.newSearch()
    },
    searchSortOrder() {
      window.localStorage.setItem('searchSortOrder', JSON.stringify(this.searchSortOrder))
      this.newSearch()
    },
    searchPageSize() {
      window.localStorage.setItem('searchPageSize', JSON.stringify(this.searchPageSize))
      this.newSearch()
    },
    displayOptions: {
      handler: function() {
        window.localStorage.setItem('displayOptions', JSON.stringify(this.displayOptions))
      },
      deep: true
    },
    searchPage() {
      this.submitSearch()
    }
  },
  computed: {
    offset() {
      return (this.searchPage - 1) * this.searchPageSize
    },
    hideSearchSuggestions() {
      return this.searchQueryIsDirty || this.searchQuery.length === 0
    }
  },
  data() {
    return {
      componentsList: [],
      tagsList: [],
      organizationsList: [],
      organizationsMap: new Map(),
      comparisonList: [],
      comparisonDataHeaders: [],
      comparisonDataDisplay: [],
      showFilters: true,
      showOptions: false,
      searchoptions: [],
      showHelp: false,
      showComparison: false,
      displayOptions: {
        organization: true,
        category: true,
        tags: true,
        userRating: false,
        description: true,
        lastUpdated: true,
        approvalDate: true
      },
      searchQuery: '',
      attributeQuery: '',
      attributeKeys: [],
      filters: {
        tags: [],
        attributes: [],
        organization: '',
        entryType: '',
        children: false
      },
      searchResults: {},
      searchResultsAttributes: {},
      searchQueryIsDirty: false,
      searchPage: 0,
      searchPageSize: 12,
      totalSearchResults: 0,
      searchSortOrder: 'DESC',
      searchSortField: '_score',
      searchSortFields: [
        { text: 'Name', value: 'name' },
        { text: 'User Rating', value: 'averageRating' },
        { text: 'Last Update', value: 'lastActivityDts' },
        { text: 'Approval Date', value: 'approvedDts' },
        { text: 'Relevance', value: '_score' }
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

.item {
  padding: 15px;
  height: 100%;
  margin: 5px;
  display: flex;
  flex-direction: column;
}
.item-header {
  display: flex;
  margin-bottom: 15px;
}
.item-body {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  padding: 2px;
  overflow: hidden;
}
.organization-chip {
  width: min-content;
  display: flex;
  align-items: center;
}
.comp-type-wrapper {
  padding: 5px 0px;
}
.tag-wrapper {
  padding: 5px 0px;
}
.item-details {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 100%;
}
.description-wrapper {
  text-overflow: ellipsis;
}
.item-details-bottom {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
}
.tag-links {
  margin-right: 0.8em;
  cursor: pointer;
}
.tag-links:hover {
  text-decoration: underline;
}
.compare-box {
  display: flex;
  align-items: center;
}
.compare-box label {
  padding-left: 4px;
}
p {
  margin: 0px;
}
.searchbar {
  border-radius: 2px;
  box-shadow: 0 3px 1px -2px rgba(0, 0, 0, 0.2), 0 2px 2px 0 rgba(0, 0, 0, 0.14), 0 1px 5px 0 rgba(0, 0, 0, 0.12);
  padding: 0.7em 0.7em 0.7em 1.2em;
  margin-bottom: 0.3em;
  margin-left: auto;
  margin-right: auto;
  font-size: 140%;
  transition: box-shadow 0.7s;
  background-color: #fff;
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
hr {
  color: #333;
  margin-bottom: 1em;
}
.side-menu {
  border-right: 1px solid #ddd;
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
  overflow-y: scroll;
  overflow-x: hidden;
  top: $header-height;
  bottom: $footer-height;
  min-width: 300px;
  right: 0;
  left: 0;
}
.search-block.open {
  margin-left: $side-menu-width;
}
.search-block.closed {
  margin-left: $closed-width;
}
.spinner {
  margin-top: 7em;
}
.more-info a {
  text-decoration: none;
}
.more-info:hover {
  transition-duration: 0.2s;
  text-decoration: underline;
}
table {
  border-collapse: separate;
  border-spacing: 0;
  height: 100%;
  width: 100%;
}
tr:nth-child(even) {
  background-color: rgba(0, 0, 0, 0.12);
}
tr:hover td {
  background-color: #b3d4fc;
}
td.table-column:hover .tooltip {
  visibility: visible;
}
.tooltip {
  visibility: hidden;
  background-color: black;
  color: #fff;
  text-align: center;
  border-radius: 6px;
  padding: 5px;
  width: 200px;
  z-index: 1;
  display: block;
  position: absolute;
}
.left-column {
  font-weight: bold;
  min-width: 220px;
  font-size: 17px;
  padding: 5px 0px 5px 20px;
  border-right: 1px solid rgba(0, 0, 0, 0.12);
}
.table-column {
  min-width: 400px;
  padding-left: 24px;
  position: relative;
}
th {
  border-bottom: 1px solid rgba(0, 0, 0, 0.12);
  text-align: left;
}
.top-corner {
  font-weight: bold;
  font-size: 20px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.12);
  border-right: 1px solid rgba(0, 0, 0, 0.12);
  padding: 20px;
}
.scrollable {
  overflow-x: scroll;
  overflow-y: scroll;
  position: absolute;
  top: 75px;
  left: 0;
  bottom: 0;
  right: 0;
}
.dialog-scroll {
  overflow-y: hidden !important;
  overflow-x: hidden !important;
  height: 80vh;
  width: 85vw;
  position: relative;
}
.checkbox-spacing {
  margin: 0;
  padding: 0;
}
.v-footer {
  height: $footer-height !important;
}
.two-line-chips {
  padding-left: 12px;
  font-size: 14px;
}
.v-chip {
  margin: 4px;
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

<template>
  <form v-on:submit.prevent="submitQuery()">
    <div class="searchbar-button">
      <v-icon @click="showOptions=!showOptions" class="search-icon search-options-icon">expand_more</v-icon>
    </div>
    <div class="searchbar">
      <input
        :value="value"
        @input="$emit('input', $event.target.value)"
        class="searchfield"
        type="text"
        placeholder="Search"
        @click="searchBarFocused"
        @blur="searchBarBlur"
      >
      <v-icon v-if="value == ''" class="search-icon" @click="submitQuery()">search</v-icon>
      <v-icon v-if="value !== ''" class="search-icon" @click="$emit('input', ''), $emit('clear')">clear</v-icon>
    </div>
    <v-card v-if="searchSuggestions.length > 0 && !hideSearchSuggestions" :height="overlaySuggestions ? 0 : 'auto'" style="z-index: 2">
      <v-list dense class="elevation-1">
        <v-list-tile v-for="i in searchSuggestions" :key="i.name" @click="submitQuery(i.name);" class="suggestion">
          <v-list-tile-content>
            {{ i.name }}
          </v-list-tile-content>
        </v-list-tile>
      </v-list>
    </v-card>
    <v-card v-if="hideSearchSuggestions && showOptions && canShowOptions" :height="overlaySuggestions ? 0 : 'auto'" style="position:relative; z-index:5">
      <v-list dense class="elevation-1">
        <h4 class="search-option-titles">Search Options</h4>
        <v-list-tile v-for="(e,index) in searchOptionsSource" v-bind:key="index" class="suggestion" height="100px">
          <v-list-tile-content>
            <v-checkbox
              :ripple="false"
              v-model="searchOptions"
              :value="e"
              :label="e"
            ></v-checkbox>
          </v-list-tile-content>
        </v-list-tile>
      </v-list>
      <v-list dense class="elevation-1">
        <h4 class="search-option-titles">Entry Types</h4>
        <v-list-tile v-for="(e,index) in entryTypes" v-bind:key="index" class="suggestion">
          <v-list-tile-content>
            <v-checkbox
              :ripple="false"
              :key="index"
              v-model="selectedEntryTypes"
              :value="e.componentType.componentType"
              :label="e.componentType.label"
            ></v-checkbox>
          </v-list-tile-content>
        </v-list-tile>
      </v-list>
    </v-card>
  </form>
</template>

<script>
import axios from 'axios'
import _ from 'lodash'

export default {
  name: 'SearchBar',
  props: ['value', 'hideSuggestions', 'overlaySuggestions'],
  mounted () {
  },
  data () {
    return {
      hideSearchSuggestions: true,
      entryTypes: {},
      searchSuggestions: [],
      selectedEntryTypes: [],
      showOptions: false,
      canShowOptions: true,
      searchOptionsSource: ['Name', 'Organization', 'Description', 'Vitals', 'Tags'],
      searchOptions: ['Name', 'Organization', 'Description', 'Vitals', 'Tags'],
      searchOptionsId: '',
      submittedEntries: ''
    }
  },
  methods: {
    searchBarFocused () {
      this.hideSearchSuggestions = false
      this.canShowOptions = false
      this.showOptions = false
    },
    searchBarBlur () {
      this.hideSearchSuggestions = true
      this.canShowOptions = true
    },
    submitQuery (query) {
      this.saveSearchOptions()
      if (query) {
        this.$emit('input', query)
      }
      this.searchSuggestions = []
      this.$emit('submitSearch', '&comp=' + this.selectedEntryTypes.toString() + '&children=true')
      // this.$router.push(`/search?q=${this.value}`)
    },
    getSearchSuggestions () {
      if (!this.hideSearchSuggestions) {
        axios
          .get(
            `/openstorefront/api/v1/service/search/suggestions?query=${this.value}&componentType=`
          )
          .then(response => {
            this.searchSuggestions = response.data
          })
          .catch(e => this.errors.push(e))
      }
    },
    saveSearchOptions () {
      this.$http.put('/openstorefront/api/v1/resource/searchoptions/user',
        {
          globalFlag: false,
          username: this.$store.state.currentUser.username,
          searchOptionsId: this.searchOptionsId,
          canUseNameInSearch: this.searchOptions.includes('Name'),
          canUseDescriptionInSearch: this.searchOptions.includes('Organization'),
          canUseOrganizationsInSearch: this.searchOptions.includes('Description'),
          canUseAttributesInSearch: this.searchOptions.includes('Vitals'),
          canUseTagsInSearch: this.searchOptions.includes('Tags')
        })
    },
    populateEntryTypes (entryTypes) {
      if (entryTypes != null && entryTypes !== '') {
        this.selectedEntryTypes = entryTypes.split(',')
      } else {
        this.selectedEntryTypes = []
      }
    }
  },
  computed: {

  },
  watch: {
    value: _.throttle(function () {
      if (this.value === '') {
        this.searchSuggestions = []
      } else if (!this.searchQueryIsDirty) {
        this.getSearchSuggestions()
      }
    }, 400),
    selectedEntryTypes: function () {
      if (this.$store.getters.getSelectedComponentTypes !== this.selectedEntryTypes) {
        this.$store.commit('setSelectedComponentTypes', { data: this.selectedEntryTypes })
      }
    }
  },
  created: function () {
    var components = this.$route.query.comp
    if (components !== undefined) {
      this.populateEntryTypes(components)
    }

    this.$store.commit('setSelectedComponentTypes', { data: this.selectedEntryTypes })

    this.$store.watch((state) => state.selectedComponentTypes, (newValue, oldValue) => {
      if (this.selectedEntryTypes !== newValue) {
        this.selectedEntryTypes = newValue
      }
    })

    this.$http
      .get('/openstorefront/api/v1/resource/searchoptions/user')
      .then(response => {
        this.searchOptionsId = response.data.searchOptionsId
        this.searchOptions = []
        if (response.data.canUseNameInSearch) {
          this.searchOptions.push('Name')
        }
        if (response.data.canUseDescriptionInSearch) {
          this.searchOptions.push('Description')
        }
        if (response.data.canUseOrganizationsInSearch) {
          this.searchOptions.push('Organization')
        }
        if (response.data.canUseAttributesInSearch) {
          this.searchOptions.push('Vitals')
        }
        if (response.data.canUseTagsInSearch) {
          this.searchOptions.push('Tags')
        }
      })
    this.$http
      .get(
        '/openstorefront/api/v1/resource/componenttypes/nested'
      )
      .then(response => {
        this.entryTypes = response.data.children
      })
  }
}
</script>

<style lang="scss" scoped>
@import "../assets/scss/colors.scss";
/* Search Bar */
.searchbar {
  border-radius: 2px;
  box-shadow: 0 3px 1px -2px rgba(0,0,0,.2),0 2px 2px 0 rgba(0,0,0,.14),0 1px 5px 0 rgba(0,0,0,.12);
  padding: 0.7em 0.7em 0.7em 0.7em;
  margin-bottom: 0.3em;
  margin-left: auto;
  margin-right: auto;
  font-size: 140%;
  transition: box-shadow 0.7s;
  background-color: $white;
}
.searchbar-button {
  border-radius: 2px;
  box-shadow: 0 3px 1px -2px rgba(0,0,0,.2),0 2px 2px 0 rgba(0,0,0,.14),0 1px 5px 0 rgba(0,0,0,.12);
  margin-bottom: 0.3em;
  font-size: 140%;
  transition: box-shadow 0.7s;
  background-color: $white;
  width: 3em;
}
.searchfield {
  display: inline-block;
  width: 80%;
  padding-left: .7em;
}
.search-icon {
  float: right;
  font-size: 34px !important;
  margin-bottom: 0.1em;
}
.search-options-icon {
  float: left !important;
  padding: 0em 0.3em;
  height: 1.66em;
  background-color: rgba(0,0,0,.12);
}
.search-option-titles {
  padding-left: 0.7em;
}
.search-icon:hover {
  cursor: pointer;
}
input {
    caret-color: $blue;
    padding-left: 0.3em;
}
input:focus {
  outline: none;
}
input:focus + .icon {
  color: $blue;
}
.myicon {
  font-size: 14px;
}
.fade-enter-active {
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

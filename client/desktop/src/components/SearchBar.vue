<template>
  <form v-on:submit.prevent="submitQuery()" v-click-outside="searchBarBlur">
    <div class="searchbar-button">
      <v-icon @click="searchOptionsClicked" class="drop-down-icon search-options-icon"
        >fas {{ hideSearchOptions ? 'fa-chevron-up' : 'fa-chevron-down' }} fa-xs</v-icon
      >
    </div>
    <div class="searchbar">
      <input
        :value="value"
        @input="$emit('input', $event.target.value)"
        class="searchfield"
        type="text"
        placeholder="Keyword Search"
        @click="searchBarFocused"
      />
      <v-icon v-if="value == ''" class="search-icon" @click="submitQuery()">mdi-magnify</v-icon>
      <v-icon v-if="value !== ''" class="search-icon" @click="$emit('input', ''), $emit('clear')">mdi-close</v-icon>
    </div>
    <!-- SEARCH SUGGESTIONS -->
    <v-card
      v-if="searchSuggestions.length > 0 && !hideSearchSuggestions"
      :height="overlaySuggestions ? 0 : 'auto'"
      style="z-index: 2; top: 6px;"
    >
      <v-list dense class="elevation-1">
        <v-list-item v-for="i in searchSuggestions" :key="i.name" @click="submitQuery(i.name)" role="button">
          <v-list-item-content>{{ i.name }}</v-list-item-content>
        </v-list-item>
      </v-list>
    </v-card>
    <!-- SEARCH OPTIONS -->
    <v-card
      v-if="hideSearchSuggestions && !hideSearchOptions"
      :height="overlaySuggestions ? 0 : 'auto'"
      style="position:relative; z-index:5; top: 6px;"
    >
      <div width="100%" style="background-color: red; text-align: center;" :hidden="searchOptions.length !== 0">
        All search options are off, this will cause a search to return nothing
      </div>
      <v-list dense :class="'elevation-1 ' + (searchOptions.length !== 0 ? '' : 'flat-top')">
        <v-list-item v-for="(e, index) in searchOptionsSource" :key="index" style="height: 50px">
          <v-list-item-content>
            <v-checkbox v-model="searchOptions" :value="e" :label="e" class="ml-2" />
          </v-list-item-content>
        </v-list-item>
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
  data() {
    return {
      hideSearchSuggestions: true,
      hideSearchOptions: true,
      searchSuggestions: [],
      searchOptionsSource: ['Name', 'Organization', 'Description', 'Vitals', 'Tags'],
      searchOptions: ['Name', 'Organization', 'Description', 'Vitals', 'Tags'],
      searchOptionsId: '',
      submittedEntries: ''
    }
  },
  methods: {
    searchBarFocused() {
      this.hideSearchSuggestions = false
      this.hideSearchOptions = true
    },
    searchBarBlur() {
      this.hideSearchSuggestions = true
      this.hideSearchOptions = true
    },
    searchOptionsClicked() {
      this.hideSearchOptions = !this.hideSearchOptions
      this.hideSearchSuggestions = true
    },
    submitQuery(query) {
      if (query) {
        this.$emit('input', query)
      }
      this.searchSuggestions = []
      this.$emit('submitSearch', '&children=true&searchoptions=' + this.searchOptions.join(','))
      this.hideSearchSuggestions = true
      this.hideSearchOptions = true
    },
    getSearchSuggestions() {
      if (!this.hideSearchSuggestions) {
        axios
          .get(`/openstorefront/api/v1/service/search/suggestions?query=${this.value}&componentType=`)
          .then(response => {
            this.searchSuggestions = response.data
          })
          .catch(e => this.errors.push(e))
      }
    },
    saveSearchOptions() {
      this.$http.put('/openstorefront/api/v1/resource/searchoptions/user', {
        globalFlag: false,
        username: this.$store.state.currentUser.username,
        searchOptionsId: this.searchOptionsId,
        canUseNameInSearch: this.searchOptions.includes('Name'),
        canUseDescriptionInSearch: this.searchOptions.includes('Organization'),
        canUseOrganizationsInSearch: this.searchOptions.includes('Description'),
        canUseAttributesInSearch: this.searchOptions.includes('Vitals'),
        canUseTagsInSearch: this.searchOptions.includes('Tags')
      })
    }
  },
  watch: {
    value: _.throttle(function() {
      if (this.value === '') {
        this.searchSuggestions = []
      } else if (!this.searchQueryIsDirty) {
        this.getSearchSuggestions()
      }
    }, 400),
    searchOptions: function(val) {
      window.localStorage.setItem('searchOptions', JSON.stringify(val))
      this.saveSearchOptions()
    }
  },
  created: function() {
    let searchOptions = window.localStorage.getItem('searchOptions')
    if (searchOptions === null) {
      this.$http.get('/openstorefront/api/v1/resource/searchoptions/user').then(response => {
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
        window.localStorage.setItem('searchOptions', JSON.stringify(this.searchOptions))
      })
    } else {
      this.searchOptions = JSON.parse(searchOptions)
    }
  },
  computed: {
    warningStyle() {
      return 'background-color: ' + this.$store.state.branding.vueErrorColor
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/scss/colors.scss';
/* Search Bar */
.searchbar {
  border-radius: 2px;
  box-shadow: 0 3px 1px -2px rgba(0, 0, 0, 0.2), 0 2px 2px 0 rgba(0, 0, 0, 0.14), 0 1px 5px 0 rgba(0, 0, 0, 0.12);
  padding: 0.7em 0.7em 0.7em 0.7em;
  margin-left: auto;
  margin-right: auto;
  font-size: 140%;
  transition: box-shadow 0.7s;
  background-color: $white;
  min-width: 100%;
}
.searchbar-button {
  border-radius: 2px;
  box-shadow: 0 3px 1px -2px rgba(0, 0, 0, 0.2), 0 2px 2px 0 rgba(0, 0, 0, 0.14), 0 1px 5px 0 rgba(0, 0, 0, 0.12);
  margin-bottom: 0.3em;
  font-size: 140%;
  transition: box-shadow 0.7s;
  background-color: $white;
  width: 3em;
}
.searchfield {
  display: inline-block;
  width: 80%;
  padding-left: 0.7em;
}
.search-icon {
  float: right;
  font-size: 34px !important;
  position: relative;
}
.drop-down-icon {
  float: right;
  font-size: 20px !important;
}
.search-options-icon {
  float: left !important;
  padding: 0em 0.3em;
  height: 65px;
  width: 45px;
  background-color: rgba(0, 0, 0, 0.12);
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
.soWarning {
  text-align: center;
  color: white;
}
.flat-top {
  border-top-right-radius: 0px;
  border-top-left-radius: 0px;
}
@media only screen and (max-width: 360px) {
  .searchfield {
    width: 200px;
  }
  .searchbar {
    width: 230px;
  }
}
@media only screen and (max-width: 415px) {
  .searchfield {
    width: 200px;
  }
}
@media only screen and (max-width: 380px) {
  .searchfield {
    width: 200px;
  }
  .searchbar {
    width: 220px;
  }
}
</style>

<template>
  <form v-on:submit.prevent="submitQuery()">
    <div class="searchbar">
      <input
        :value="value"
        @input="$emit('input', $event.target.value)"
        class="searchfield"
        type="text"
        placeholder="Search"
      >
      <v-icon v-if="value == ''" class="search-icon" @click="submitQuery()">search</v-icon>
      <v-icon v-if="value !== ''" class="search-icon" @click="$emit('input', ''), submitQuery()">clear</v-icon>
    </div>

    <v-card v-if="searchSuggestions.length > 0">
      <v-list dense>
        <v-list-tile v-for="i in searchSuggestions" :key="i" @click="submitQuery(i.name);" class="suggestion">
          <v-list-tile-content>
            {{ i.name }}
          </v-list-tile-content>
        </v-list-tile>
      </v-list>
    </v-card>

  </form>
</template>

<script>
import axios from 'axios';
import _ from 'lodash';

export default {
  name: 'SearchBar',
  props: ['value', 'hideSuggestions'],
  mounted () {
  },
  data () {
    return {
      searchSuggestions: []
    };
  },
  methods: {
    submitQuery (query) {
      if (query) {
        this.$emit('input', query);
      }
      this.searchSuggestions = [];
      this.$emit('submitSearch');
      // this.$router.push(`/search?q=${this.value}`)
    },
    getSearchSuggestions () {
      if (!this.hideSuggestions) {
        axios
          .get(
            `/openstorefront/api/v1/service/search/suggestions?query=${this.value}&componentType=`
          )
          .then(response => {
            this.searchSuggestions = response.data;
          })
          .catch(e => this.errors.push(e));
      }
    }
  },
  computed: {

  },
  watch: {
    value: _.throttle(function () {
      if (this.value === '') {
        this.searchSuggestions = [];
      } else if (!this.searchQueryIsDirty) {
        this.getSearchSuggestions();
      }
    }, 400)
  }
};
</script>

<style>
/* Search Bar */
.searchbar {
  border-radius: 2px;
  box-shadow: 0 3px 1px -2px rgba(0,0,0,.2),0 2px 2px 0 rgba(0,0,0,.14),0 1px 5px 0 rgba(0,0,0,.12);
  padding: 0.7em 1.2em;
  margin-bottom: 0.3em;
  margin-left: auto;
  margin-right: auto;
  font-size: 120%;
  transition: box-shadow 0.7s;
  max-width: 30em;
}
.searchfield {
  display: inline-block;
  width: 80%;
}
.search-icon {
  float: right;
}
.search-icon:hover {
  cursor: pointer;
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

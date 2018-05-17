<template>
  <form v-on:submit.prevent="submitQuery()">
    <div class="searchbar">
      <input v-model="searchQuery" class="searchfield" type="text" placeholder="Search">
      <v-icon class="search-icon" @click="submitQuery()">search</v-icon>
    </div>
  </form>
</template>

<script>
export default {
  name: 'SearchBar',
  props: [],
  mounted () {

  },
  data () {
    return {
      searchQuery: ''
    }
  },
  methods: {
    submitQuery () {
      this.$router.push(`/search?q=${this.searchQuery}`)
    }
  },
  computed: {

  }
}
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

<template lang="html">

  <section class="mx-3">
    <div style="width: 100%; background-color: #252931; text-align: center; margin-bottom: 1em;" >
      <img src="../assets/img/SPOONLogoWhiteBlock.svg" width="300" alt="SPOON Logo">
    </div>
    <SearchBar></SearchBar>
  </section>

</template>

<script lang="js">
import SearchBar from './subcomponents/SearchBar'

export default {
  name: 'landing-page',
  components: {
    SearchBar
  },
  props: [],
  mounted () {

  },
  data () {
    return {

    }
  },
  methods: {

  },
  computed: {

  }
}
</script>

<style scoped lang="scss">
  .landing-page {

  }
</style>

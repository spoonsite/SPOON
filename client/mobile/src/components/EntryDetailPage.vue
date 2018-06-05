<template lang="html">

  <section class="entry-detail-page">
    <h1>entry-detail-page Component</h1>
  </section>

</template>

<script lang="js">
import axios from 'axios'

export default {
  name: 'entry-detail-page',
  props: [],
  mounted () {
    if(this.$route.params.id) {
      this.id = this.$route.params.id;
    }
  },
  data () {
    return {

    };
  },
  methods: {
    getComponent(){
      let that = this;
      this.$http
        .get(
          '/openstorefront/api/v1/resource/components/' + this.id
        )
        .then(response => {
          that.tagsList = response.data;
        })
        .catch(e => this.errors.push(e));
    }
  },
  computed: {

  }
};
</script>

<style scoped lang="scss">
  .entry-detail-page {

  }
</style>

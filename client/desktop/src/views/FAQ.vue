<template>
  <v-layout my-5 mx-3>
    <v-flex xs12 md8 offset-md2>
      <h2 class="headline text-xs-center mb-4">Frequently Asked Questions</h2>
      <v-expansion-panels popout max-width="700px">
        <v-expansion-panel v-for="entry in questions" :key="entry.faqSortOrder" ripple>
          <v-expansion-panel-header>{{ entry.question }}</v-expansion-panel-header>
          <v-expansion-panel-content
            v-html="entry.answer"
            style="flex-direction: column; margin: 20px"
          ></v-expansion-panel-content>
        </v-expansion-panel>
      </v-expansion-panels>
    </v-flex>
  </v-layout>
</template>

<script>
export default {
  name: 'FAQ',
  data: () => ({
    questions: [],
    errors: []
  }),
  methods: {
    getQuestions: function() {
      let that = this

      this.$http
        .get('/openstorefront/api/v1/resource/faq')
        .then(response => {
          var filtered = response.data.filter(item => item.activeStatus === 'A')
          that.questions = filtered
        })
        .catch(e => this.errors.push(e))
    }
  },
  mounted() {
    this.getQuestions()
  }
}
</script>

<style>
</style>

<template>
  <div>
    <h2 class="text-center">Frequently Asked Questions</h2>
    <v-layout my-5 mx-3>
      <v-flex xs12 md8 offset-md2>
        <v-expansion-panels max-width="700px">
          <v-expansion-panel v-for="(entry, index) in questions" :key="entry.faqSortOrder + index" ripple>
            <v-expansion-panel-header class="title">{{ entry.question }}</v-expansion-panel-header>
            <v-expansion-panel-content
              v-html="entry.answer"
              style="flex-direction: column; margin: 20px"
            ></v-expansion-panel-content>
          </v-expansion-panel>
        </v-expansion-panels>
      </v-flex>
    </v-layout>
  </div>
</template>

<script>
export default {
  name: 'FAQPage',
  data: () => ({
    questions: [],
    errors: []
  }),
  methods: {
    getQuestions: function () {
      this.$http
        .get('/openstorefront/api/v1/resource/faq')
        .then(response => {
          this.questions = response.data.filter(item => item.activeStatus === 'A')
        })
        .catch(e => this.errors.push(e))
    }
  },
  mounted () {
    this.getQuestions()
  }
}
</script>

<style>
</style>

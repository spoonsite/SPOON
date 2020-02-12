<template>
  <div>
    <h2 class="text-center">Frequently Asked Questions</h2>
    <v-layout my-5 mx-3>
      <v-flex xs12 md8 offset-md2>
        <v-expansion-panels popout max-width="700px">
          <v-expansion-panel v-for="(entry, index) in questions" :key="index" ripple>
            <v-expansion-panel-header>{{ entry.question }}</v-expansion-panel-header>
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
  name: 'FAQ',
  data: () => ({
    questions: []
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
        .catch(error => {
          this.$toasted.error('An error occurred retrieving questions')
          console.error(error)
        })
    }
  },
  mounted() {
    this.getQuestions()
  }
}
</script>

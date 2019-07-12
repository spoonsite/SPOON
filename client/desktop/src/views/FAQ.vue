<template>
<v-layout my-5 mx-3>
  <v-flex xs12 md8 offset-md2>
    <h2 class="headline text-xs-center mb-4">Frequently Asked Questions</h2>

    <v-expansion-panel popout>
      <v-expansion-panel-content v-for="entry in questions" :key="entry.faqSortOrder" ripple>
      <div slot="header" class="title">{{entry.question}}</div>
      <v-card>
        <v-card-text class="grey lighten-5" v-html="entry.answer"></v-card-text>
      </v-card>
      </v-expansion-panel-content>
    </v-expansion-panel>
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
    getQuestions: function () {
      let that = this;

      this.$http
        .get('/openstorefront/api/v1/resource/faq')
        .then(response => {
          var filtered = response.data.filter(item => item.activeStatus === 'A');
          that.questions = filtered;
        })
        .catch(e => this.errors.push(e));
    }
  },
  mounted () {
    this.getQuestions();
  }
};
</script>

<style>
</style>

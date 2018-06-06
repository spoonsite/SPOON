<template>
<v-layout mt-3 mx-3>
  <v-flex xs12 sm4 offset-sm4>
    <h2 class="text-xs-center">Frequently Asked Questions</h2>

    <v-expansion-panel popout>
      <v-expansion-panel-content v-for="entry in questions" :key="entry.faqSortOrder" ripple>
      <div slot="header" class="title">{{entry.question}}</div>
      <v-card>
        <v-card-text class="grey lighten-3" v-html="entry.answer"></v-card-text>
      </v-card>
      </v-expansion-panel-content>
    </v-expansion-panel>
  </v-flex>
</v-layout>
</template>

<script>
import axios from 'axios';

export default {
  name: 'FAQPage',
  data: () => ({
    questions: [],
    errors: []
  }),
  methods: {
    getQuestions: function () {
      let that = this;

      axios
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

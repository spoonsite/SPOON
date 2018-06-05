<template>
<div class="wrapper">
  <h2 class="text-xs-center">Frequently Asked Questions</h2>

<v-layout mt-3>
    <v-flex xs12>
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
</div>
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

      // that.questions = this.$api.getFAQquestions()

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
.wrapper {
  max-width: 45em;
  padding: 1em;
  margin-right: auto;
  margin-left: auto;
}
</style>

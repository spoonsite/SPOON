<template>
<div>
  <h2 @click="getAnswers(question.questionId);">Q: <span v-html="question.question"></span> </h2>
  <hr>
  <v-btn @click="answerQuestion(question.questionId)">Answer Question</v-btn>
  <div
  v-for="answer in answers"
  :key="answer.response"
  class="white pa-2 elevation-2"
  >
    <p class="caption">Answered by {{ answer.createUser }} on {{ answer.createDts | formatDate }}</p>
    <p class="caption" v-if="answer.createDts !== answer.updateDts">Updated on {{ answer.updateDts | formatDate }}</p>
    <p class="ma-0" v-if="!edit"><span v-html="answer.response" ></span></p>
    <quill-editor
    v-else
    style="background-color: white;"
    v-model="answer.response"
    :options="quillToolbarOptions"
    ></quill-editor>
    <v-btn v-if="edit" small color="success" @click="edit = false"><v-icon small class="icon">save</v-icon> Save</v-btn>
    <v-btn v-else small @click="edit = true"><v-icon small class="icon">edit</v-icon> Edit</v-btn>
    <v-btn small @click="deleteAnswer(answer.responseId)"><v-icon small class="icon">delete</v-icon> Delete</v-btn>
  </div>
</div>
</template>

<script>
export default {
  name: 'Question',
  props: ['question'],
  mounted () {
  },
  data () {
    return {
      answers: {},
      edit: false,
      quillToolbarOptions: {
        modules: {
          toolbar: [[{'header': 1}, {'header': 2}], ['bold', 'italic'], [{'list': 'bullet'}, {'list': 'ordered'}], ['clean']]
        }
      },
      errors: []
    };
  },
  methods: {
    getAnswers (qid) {
      this.$http.get(`/openstorefront/api/v1/resource/components/${this.question.componentId}/questions/${qid}/responses`)
        .then(response => {
          this.answers = response.data;
        })
        .catch(e => this.errors.push(e));
    },
    answerQuestion (qid) {
      console.log(`Answer Question: ${qid}`);
      console.log(`Current User: ${this.$store.state.currentUser.username}`);
    },
    editAnswer (aid) {
      console.log(`Edit Answer: ${aid}`);
    },
    deleteAnswer (aid) {
      console.log(`Delete Answer: ${aid}`);
    }
  },
  computed: {
  },
  watch: {
  }
};
</script>

<style>
</style>

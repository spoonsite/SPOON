<template>
<div>
  <h2 @click="getAnswers(question.questionId);">Q: <span v-html="question.question"></span> </h2>
  <hr>
  <div v-if="$store.state.currentUser.username === question.createUser">
    <v-btn icon @click="editQuestion(question.questionId)"><v-icon>edit</v-icon></v-btn>
    <v-btn icon @click="deleteQuestionDialog = true"><v-icon>delete</v-icon></v-btn>
  </div>
  <v-btn icon @click="answerQuestionDialog = true"><v-icon>fas fa-reply</v-icon></v-btn>
  <Answer v-for="answer in answers" :answer="answer" :key="answer.responseId"></Answer>

  <v-dialog
    v-model="answerQuestionDialog"
    >
    <v-card>
      <v-card-title>
        <h2>Answer a Question</h2>
      </v-card-title>
      <v-card-text>
        <quill-editor
        style="background-color: white;"
        v-model="newAnswer"
        ></quill-editor>
      </v-card-text>
      <v-card-actions>
        <v-btn @click="submitAnswer(question.questionId)">Submit</v-btn>
        <v-btn @click="answerQuestionDialog = false; newAnswer = '';">Cancel</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <v-dialog
    v-model="deleteQuestionDialog"
    >
    <v-card>
      <v-card-text>
        <p>Are you sure you want to delete this question?</p>
      </v-card-text>
      <v-card-actions>
        <v-btn color="warning" @click="deleteQuestion(answer.responseId)"><v-icon>delete</v-icon> Delete</v-btn>
        <v-btn @click="deleteQuestionDialog = false">Cancel</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

</div>
</template>

<script>
import Answer from './Answer';

export default {
  name: 'Question',
  props: ['question'],
  components: {
    Answer
  },
  mounted () {
  },
  data () {
    return {
      answers: {},
      edit: false,
      answerQuestionDialog: false,
      deleteQuestionDialog: false,
      newAnswer: '',
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
    submitAnswer (qid) {
      console.log(`Answer question ${qid}`);
      let data = {
        dataSensitivity: '',
        organization: this.$store.state.currentUser.organization,
        questionId: qid,
        response: this.newAnswer,
        securityMarkingType: '',
        userTypeCode: this.$store.state.currentUser.userTypeCode
      };
      this.$http.post(`/openstorefront/api/v1/resource/components/${this.question.componentId}/questions/${qid}/responses`, data)
        .then(response => {
          // answer created
          if (response.status === 201) {
            console.log('response created');
          }
          this.getAnswers(qid);
          this.answerQuestionDialog = false;
        });
    },
    deleteQuestion (qid) {
      this.$http.delete(`http://localhost:8080/openstorefront/api/v1/resource/components/${this.question.componentId}/questions/${qid}`)
        .then(response => {
          console.log(response);
          this.deleteQuestionDialog = false;
          this.$emit('deleteQuestion');
        });
    },
    editQuestion (qid) {
      let data = {
        dataSensitivity: '',
        organization: this.$store.state.currentUser.organization,
        question: this.newQuestion,
        securityMarkingType: '',
        userTypeCode: this.$store.state.currentUser.userTypeCode
      };
      this.$http.put(`/openstorefront/api/v1/resource/components/${this.question.componentId}/questions/${qid}`, data)
        .then(response => {
          // question submitted
          console.log('Question asked, awaiting approval');
          this.newQuestion = '';
          this.askQuestionDialog = false;
        });
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

<template>
<div class="mt-4">

  <div class="white elevation-2" style="overflow: auto;">
    <div class="grey lighten-2 pa-2">
      <h3>Question
      <span style="float: right" v-if="$store.state.currentUser.username === question.createUser">
        <v-tooltip bottom>
          <v-btn small slot="activator" icon @click="openEditQuestionDialog()">   <v-icon class="icon">edit</v-icon></v-btn>
          <span>Edit the question</span>
        </v-tooltip>
        <v-tooltip bottom>
          <v-btn small slot="activator" icon @click="deleteQuestionDialog = true"><v-icon class="icon">delete</v-icon></v-btn>
          <span>Delete the question</span>
        </v-tooltip>
      </span>
      </h3>

    </div>
    <v-alert type="warning" :value="question.activeStatus === 'P'">This question is pending admin approval.</v-alert>
    <div class="px-2 pb-2">
      <p class="caption">Asked by <strong>{{ question.createUser }}</strong> on {{ question.createDts | formatDate }} <span v-if="question.createDts !== question.updateDts">(updated on {{ question.updateDts | formatDate }} by <strong>{{ question.updateUser }}</strong>)</span></p>
      <div class="pt-2" style="font-size: 16px;" v-html="question.question"></div>
      <v-btn small @click="answerQuestionDialog = true">Answer</v-btn>

      <v-btn :loading="loading" v-if="!showAnswers" small @click="getAnswers(question.questionId); showAnswers = true;">View Answers</v-btn>
      <v-btn :loading="loading" v-else-if="noAnswers" disabled small>No Answers</v-btn>
      <v-btn :loading="loading" v-else small @click="showAnswers = false">Hide Answers</v-btn>

      <transition name="slide">
      <div v-if="showAnswers">
        <Answer v-for="answer in answers" :answer="answer" @answerDeleted="deleteAnswer" :key="answer.responseId"></Answer>
      </div>
      </transition>
    </div>
  </div>

  <v-dialog
    v-model="answerQuestionDialog"
    >
    <v-card>
      <v-card-title>
        <h2 class="w-100">Answer a Question</h2>
        <v-alert class="w-100" type="warning" :value="true">Do not enter any ITAR restricted, FOUO, Proprietary or otherwise sensitive information.</v-alert>
        <v-alert class="w-100" type="info" :value="true">All answers need admin approval before being made public.</v-alert>
      </v-card-title>
      <v-card-text>
        <div v-html="question.question"></div>
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
    max-width="300px"
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

  <v-dialog
    v-model="editQuestionDialog"
    >
    <v-card>
      <v-card-title>
        <h2 class="w-100">Edit a Question</h2>
        <v-alert class="w-100" type="warning" :value="true">Do not enter any ITAR restricted, FOUO, Proprietary or otherwise sensitive information.</v-alert>
        <v-alert class="w-100" type="info" :value="true">All questions need admin approval before being made public.</v-alert>
      </v-card-title>
      <v-card-text>
        <quill-editor
        style="background-color: white;"
        v-model="newQuestion"
        ></quill-editor>
      </v-card-text>
      <v-card-actions>
        <v-btn color="success" @click="editQuestion(question.questionId)">Submit</v-btn>
        <v-btn @click="editQuestionDialog = false">Cancel</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

</div>
</template>

<script>
import Answer from './Answer';
import _ from 'lodash';

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
      answers: [],
      showAnswers: false,
      edit: false,
      answerQuestionDialog: false,
      deleteQuestionDialog: false,
      editQuestionDialog: false,
      newAnswer: '',
      noAnswers: false,
      newQuestion: '',
      errors: [],
      loading: false
    };
  },
  methods: {
    checkAnswers () {
      this.noAnswers = false;
      if (this.answers.length === 0) {
        this.noAnswers = true;
      } else {
        let hasAnswer = false;
        this.answers.forEach(function (el) {
          if (el.activeStatus !== 'I') {
            hasAnswer = true;
          }
        });
        if (!hasAnswer) {
          this.noAnswers = true;
        }
      }
    },
    getAnswers (qid) {
      if (_.isEmpty(this.answers)) {
        this.loading = true;
        this.$http.get(`/openstorefront/api/v1/resource/components/${this.question.componentId}/questions/${qid}/responses`)
          .then(response => {
            this.answers = response.data;
            this.checkAnswers();
            this.loading = false;
          })
          .catch(e => this.errors.push(e));
      }
    },
    submitAnswer (qid) {
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
          this.$toasted.show('Answer submitted.');
          this.answers.push(response.data);
          this.newAnswer = '';
          this.noAnswers = false;
          this.showAnswers = true;
          this.answerQuestionDialog = false;
        })
        .catch(e => this.$toasted.error('There was a problem submitting the answer.'));
    },
    deleteQuestion (qid) {
      this.$http.delete(`http://localhost:8080/openstorefront/api/v1/resource/components/${this.question.componentId}/questions/${qid}`)
        .then(response => {
          this.deleteQuestionDialog = false;
          this.$toasted.show('Question deleted.');
          this.$emit('questionDeleted', this.question);
        })
        .catch(e => this.$toasted.error('There was a problem deleting the answer.'));
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
          this.question.question = response.data.question;
          this.question.updateDts = new Date(); // the date is not sent back in the response
          this.question.activeStatus = response.data.activeStatus;
          this.newQuestion = '';
          this.$toasted.show('Edited question submitted.');
          this.editQuestionDialog = false;
        })
        .catch(e => this.$toasted.error('There was a problem submitting the edit.'));
    },
    deleteAnswer (answer) {
      console.log(answer);
      this.answers = this.answers.filter(function (el) {
        return el.responseId !== answer.responseId;
      });
      // check if no answers
      this.checkAnswers();
    },
    openEditQuestionDialog () {
      this.newQuestion = this.question.question;
      this.editQuestionDialog = true;
    }
  },
  computed: {
  },
  watch: {
  }
};
</script>

<style>
.icon {
  font-size: 18px;
}
.btn {
  margin: 0;
}
.slide-enter-active, .slide-leave-active {
  transition: 0.5s cubic-bezier(.25,.8,.5,1);
  max-height: 50em;
  overflow: hidden;
}
.slide-enter, .slide-leave-to {
  max-height: 0;
}
.w-100 {
  width: 100%;
}
</style>

<template>
  <div class="mt-4">
    <div class="white elevation-2" style="overflow: auto;">
      <div class="grey lighten-2 pa-2">
        <h3>
          Question
          <span style="float: right" v-if="$store.state.currentUser.username === question.createUser">
            <v-tooltip bottom>
              <template v-slot:activator="{ on }">
                <v-btn v-on="on" small icon @click="editQuestionDialog = true">
                  <v-icon class="icon">mdi-pencil</v-icon></v-btn
                >
              </template>
              <span>Edit the question</span>
            </v-tooltip>
          </span>
        </h3>
      </div>
      <v-alert type="warning" class="w-100" :value="question.activeStatus === 'P'"
        >This question is pending admin approval.</v-alert
      >
      <div class="px-2 pb-2">
        <p class="caption">
          Asked by <strong>{{ question.createUser }}</strong> on {{ question.createDts | formatDate }}
          <span v-if="question.createDts !== question.updateDts"
            >(updated on {{ question.updateDts | formatDate }} by <strong>{{ question.updateUser }}</strong
            >)</span
          >
        </p>
        <div class="pt-2" style="font-size: 16px;" v-html="question.question"></div>
        <div class="d-flex justify-end">
          <v-btn small @click="answerQuestionDialog = true" class="mx-3">Answer</v-btn>

          <v-btn
            :loading="loading"
            v-if="!showAnswers"
            small
            class="mx-3"
            @click="
              getAnswers(question.questionId)
              showAnswers = true
            "
            >View Answers</v-btn
          >
          <v-btn :loading="loading" v-else-if="noAnswers" disabled small class="mx-3">No Answers</v-btn>
          <v-btn :loading="loading" v-else small @click="showAnswers = false" class="mx-3">Hide Answers</v-btn>
        </div>

        <transition name="slide">
          <div v-if="showAnswers">
            <Answer
              v-for="answer in answers"
              :answer="answer"
              @answerDeleted="deleteAnswer"
              @getAnswers="getAnswers($event.answer)"
              :key="answer.responseId"
            ></Answer>
          </div>
        </transition>
      </div>
    </div>

    <QuestionModal
      v-model="editQuestionDialog"
      title="Edit a Question"
      :editQuestion="question.question"
      @close="editQuestion($event)"
    />

    <AnswerModal
      v-model="answerQuestionDialog"
      @close="submitAnswer($event)"
      title="Answer a Question"
      :question="question.question"
      :answerProp="question"
    />

    <v-dialog v-model="editQuestionDialog" max-width="75em">
      <v-card>
        <ModalTitle title="Edit a Question" @close="editQuestionDialog = false" />
        <v-card-text>
          <v-alert class="w-100" type="warning" :value="true"
            ><span v-html="$store.state.branding.userInputWarning"></span
          ></v-alert>
          <v-alert class="w-100" type="info" :value="true"
            >All questions need admin approval before being made public.</v-alert
          >
          <quill-editor style="background-color: white;" v-model="newQuestion"></quill-editor>
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn color="success" @click="editQuestion(question.questionId)">Submit</v-btn>
          <v-btn @click="editQuestionDialog = false">Cancel</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import Answer from '@/components/Answer'
import ModalTitle from '@/components/ModalTitle'
import QuestionModal from '@/components/QuestionModal'
import AnswerModal from '@/components/AnswerModal'

export default {
  name: 'Question',
  props: ['question'],
  components: {
    Answer,
    ModalTitle,
    QuestionModal,
    AnswerModal
  },
  data() {
    return {
      answers: [],
      showAnswers: false,
      edit: false,
      answerQuestionDialog: false,
      deleteQuestionDialog: false,
      editQuestionDialog: false,
      newAnswer: '',
      noAnswers: false,
      errors: [],
      loading: false
    }
  },
  methods: {
    checkAnswers() {
      this.noAnswers = false
      if (this.answers.length === 0) {
        this.noAnswers = true
      } else {
        let hasAnswer = false
        this.answers.forEach(function(el) {
          if (el.activeStatus !== 'I') {
            hasAnswer = true
          }
        })
        if (!hasAnswer) {
          this.noAnswers = true
        }
      }
    },
    getAnswers(qid) {
      this.loading = true
      this.$http
        .get(`/openstorefront/api/v1/resource/components/${this.question.componentId}/questions/${qid}/responses`)
        .then(response => {
          this.answers = response.data
          this.checkAnswers()
          this.loading = false
        })
        .catch(e => this.errors.push(e))
    },
    submitAnswer(question) {
      if (question) {
        let data = {
          dataSensitivity: '',
          organization: this.$store.state.currentUser.organization,
          questionId: question.questionId,
          response: question.answer,
          securityMarkingType: '',
          userTypeCode: this.$store.state.currentUser.userTypeCode
        }
        this.$http
          .post(
            `/openstorefront/api/v1/resource/components/${question.componentId}/questions/${question.questionId}/responses`,
            data
          )
          .then(response => {
            this.$toasted.success('Answer submitted')
            this.getAnswers(question.questionId)
            this.showAnswers = true
            this.answerQuestionDialog = false
          })
          .catch(e => this.$toasted.error('There was a problem submitting the answer.'))
      } else {
        this.answerQuestionDialog = false
      }
    },
    deleteQuestion(qid) {
      this.$http
        .delete(
          `http://localhost:8080/openstorefront/api/v1/resource/components/${this.question.componentId}/questions/${qid}`
        )
        .then(response => {
          this.deleteQuestionDialog = false
          this.$toasted.success('Question deleted.')
          this.$emit('questionDeleted')
        })
        .catch(e => this.$toasted.error('There was a problem deleting the question.'))
    },
    editQuestion(question) {
      if (question) {
        let data = {
          dataSensitivity: '',
          organization: this.$store.state.currentUser.organization,
          question: question,
          securityMarkingType: '',
          userTypeCode: this.$store.state.currentUser.userTypeCode
        }
        this.$http
          .put(
            `/openstorefront/api/v1/resource/components/${this.question.componentId}/questions/${this.question.questionId}`,
            data
          )
          .then(response => {
            this.question.question = response.data.question
            this.question.updateDts = new Date() // the date is not sent back in the response
            this.question.activeStatus = response.data.activeStatus
            this.$toasted.success('Edited question submitted.')
            this.editQuestionDialog = false
          })
          .catch(e => this.$toasted.error('There was a problem submitting the edit.'))
      } else {
        this.editQuestionDialog = false
      }
    },
    deleteAnswer(answer) {
      this.answers = this.answers.filter(function(el) {
        return el.responseId !== answer.responseId
      })
      // check if no answers
      this.checkAnswers()
    }
  }
}
</script>

<style>
.icon {
  font-size: 18px;
}
.btn {
  margin: 0;
}
.slide-enter-active,
.slide-leave-active {
  transition: 0.5s cubic-bezier(0.25, 0.8, 0.5, 1);
  max-height: 50em;
  overflow: hidden;
}
.slide-enter,
.slide-leave-to {
  max-height: 0;
}
.w-100 {
  width: 100%;
}
</style>

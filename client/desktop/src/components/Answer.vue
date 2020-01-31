<template>
  <div class="white elevation-3 ma-3" :class="{ dn: answer.activeStatus === 'I' }">
    <v-alert type="warning" :value="answer.activeStatus === 'P'">This answer is pending admin approval.</v-alert>
    <v-alert type="error" :value="answer.activeStatus === 'I'">This answer is inactive.</v-alert>
    <div class="pt-2 px-2">
      <p class="caption">
        Answered by <strong>{{ answer.createUser }}</strong> on {{ answer.createDts | formatDate }}
        <span v-if="answer.createDts !== answer.updateDts"
          >(updated on {{ answer.updateDts | formatDate }} by <strong>{{ answer.updateUser }})</strong></span
        >
      </p>
      <div class="ma-0" v-html="answer.response"></div>
      <div v-if="$store.state.currentUser.username === answer.createUser" class="d-flex justify-end pb-4">
        <v-btn @click="edit = true" class="mx-3" small><v-icon small class="icon">mdi-pencil</v-icon>Edit</v-btn>
        <v-btn @click="deleteDialog = true" class="mx-3" color="warning" small
          ><v-icon small class="icon">mdi-delete</v-icon>Delete</v-btn
        >
      </div>
    </div>

    <AnswerModal
      v-model="edit"
      title="Edit Answer"
      @close="editAnswer($event)"
      :answerProp="answer"
      :answerText="answer.response"
    />

    <v-dialog v-model="deleteDialog" max-width="25em">
      <v-card>
        <ModalTitle title="Delete?" @close="deleteDialog = false" />
        <v-card-text>
          <p>
            Are you sure you want to delete this answer?
          </p>
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn color="warning" @click="deleteAnswer()"><v-icon>mdi-delete</v-icon> Delete</v-btn>
          <v-btn @click="deleteDialog = false">Cancel</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import ModalTitle from '@/components/ModalTitle'
import AnswerModal from '@/components/AnswerModal'

export default {
  name: 'Answer',
  components: {
    ModalTitle,
    AnswerModal
  },
  props: ['answer'],
  data() {
    return {
      edit: false,
      deleteDialog: false
    }
  },
  methods: {
    editAnswer(answer) {
      if (answer) {
        let data = {
          dataSensitivity: '',
          organization: this.$store.state.currentUser.organization,
          questionId: answer.questionId,
          response: answer.answer,
          userTypeCode: this.$store.state.currentUser.userTypeCode
        }
        this.$http
          .put(
            `/openstorefront/api/v1/resource/components/${answer.componentId}/questions/${answer.questionId}/responses/${answer.responseId}`,
            data
          )
          .then(response => {
            this.$toasted.show('Edit submitted.')
            this.$emit('getAnswers', { answer: answer.questionId })
            this.edit = false
          })
          .catch(e => this.$toasted.error('There was a problem submitting the edit.'))
      }
    },
    deleteAnswer() {
      this.$http
        .delete(
          `/openstorefront/api/v1/resource/components/${this.answer.componentId}/questions/${this.answer.questionId}/responses/${this.answer.responseId}`
        )
        .then(response => {
          this.$toasted.show('Answer deleted.')
          this.$emit('answerDeleted', this.answer)
          this.deleteDialog = false
        })
        .catch(e => this.$toasted.error('There was a problem deleting the answer.'))
    }
  }
}
</script>

<style>
.btn {
  margin: 0;
}
.w-100 {
  width: 100%;
}
.dn {
  display: none;
}
</style>

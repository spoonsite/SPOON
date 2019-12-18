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
        <v-btn @click="edit = true" class="mx-3"><v-icon small class="icon">mdi-pencil</v-icon>Edit</v-btn>
        <v-btn @click="deleteDialog = true" class="mx-3" color="warning"
          ><v-icon small class="icon">mdi-delete</v-icon>Delete</v-btn
        >
      </div>
    </div>

    <v-dialog v-model="edit" max-width="75em">
      <v-card>
        <ModalTitle title="Edit answer" @close="edit = false" />
        <v-card-text>
          <v-alert class="w-100" type="warning" :value="true"
            ><span v-html="$store.state.branding.userInputWarning"></span
          ></v-alert>
          <v-alert class="w-100" type="info" :value="true"
            >All answers need admin approval before being made public.</v-alert
          >
          <quill-editor style="background-color: white;" v-model="answer.response" />
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn color="success" @click="editAnswer()">Save</v-btn>
          <v-btn @click="edit = false">Cancel</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

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

export default {
  name: 'Answer',
  components: {
    ModalTitle
  },
  props: ['answer'],
  mounted() {},
  data() {
    return {
      edit: false,
      deleteDialog: false
    }
  },
  methods: {
    editAnswer() {
      let data = {
        dataSensitivity: '',
        organization: this.$store.state.currentUser.organization,
        questionId: this.answer.questionId,
        response: this.answer.response,
        securityMarkingType: '',
        userTypeCode: this.$store.state.currentUser.userTypeCode
      }
      this.$http
        .put(
          `/openstorefront/api/v1/resource/components/${this.answer.componentId}/questions/${this.answer.questionId}/responses/${this.answer.responseId}`,
          data
        )
        .then(response => {
          this.$toasted.show('Edit submitted.')
          this.answer.response = response.data.response
          this.answer.activeStatus = response.data.activeStatus
          this.answer.updateDts = new Date() // the date is not sent back in the response
          this.edit = false
        })
        .catch(e => this.$toasted.error('There was a problem submitting the edit.'))
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
  },
  computed: {},
  watch: {}
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

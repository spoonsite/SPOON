<template>
<div class="white elevation-3 ma-3">
  <v-alert type="warning" :value="answer.activeStatus === 'P'">This answer is pending admin approval.</v-alert>
  <div class="pt-2 px-2">
    <p class="caption">Answered by <strong>{{ answer.createUser }}</strong> on {{ answer.createDts | formatDate }}</p>
    <p class="caption" v-if="answer.createDts !== answer.updateDts">Updated on {{ answer.updateDts | formatDate }}</p>
    <div class="ma-0" v-html="answer.response"></div>
    <div v-if="$store.state.currentUser.username === answer.createUser">
      <v-btn icon @click="edit = true"><v-icon small class="icon">edit</v-icon></v-btn>
      <v-btn icon @click="deleteDialog = true"><v-icon small class="icon">delete</v-icon></v-btn>
    </div>
  </div>

  <v-dialog
    v-model="edit"
  >
    <v-card>
      <v-card-title>
        <h2>Edit question</h2>
        <v-alert type="warning" :value="true">Do not enter any ITAR restricted, FOUO, Proprietary or otherwise sensitive information.</v-alert>
        <v-alert type="info" :value="true">All answers need admin approval before being made public.</v-alert>
      </v-card-title>
      <v-card-text>
        <quill-editor
        style="background-color: white;"
        v-model="answer.response"
        ></quill-editor>
      </v-card-text>
      <v-card-actions>
        <v-btn @click="editAnswer(answer.questionId, answer.responseId, answer.response)">Save</v-btn>
        <v-btn @click="edit = false">Cancel</v-btn>
      </v-card-actions>
    </v-card>

  </v-dialog>

  <v-dialog
    v-model="deleteDialog"
    >
    <v-card>
      <v-card-text>
        <p>Are you sure you want to delete this answer?</p>
      </v-card-text>
      <v-card-actions>
        <v-btn color="warning" @click="deleteAnswer(answer.questionId, answer.responseId)"><v-icon>delete</v-icon> Delete</v-btn>
        <v-btn @click="deleteDialog = false">Cancel</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

</div>
</template>

<script>
export default {
  name: 'Answer',
  props: ['answer'],
  mounted () {
  },
  data () {
    return {
      edit: false,
      deleteDialog: false
    };
  },
  methods: {
    editAnswer (qid, aid, newAnswer) {
      let data = {
        dataSensitivity: '',
        organization: this.$store.state.currentUser.organization,
        questionId: qid,
        response: newAnswer,
        securityMarkingType: '',
        userTypeCode: this.$store.state.currentUser.userTypeCode
      };
      this.$http.put(`/openstorefront/api/v1/resource/components/${this.answer.componentId}/questions/${qid}/responses/${aid}`, data)
        .then(response => {
          this.$toasted.show('Edit submitted.');
          this.answer = response.data;
          this.edit = false;
        })
        .catch(e => this.$toasted.error('There was a problem submitting the edit.'));
    },
    deleteAnswer (qid, aid) {
      this.$http.delete(`/openstorefront/api/v1/resource/components/${this.answer.componentId}/questions/${qid}/responses/${aid}`)
        .then(response => {
          this.$toasted.show('Answer deleted.');
          this.deleteDialog = false;
        })
        .catch(e => this.$toasted.error('There was a problem deleting the answer.'));
    }
  },
  computed: {
  },
  watch: {
  }
};
</script>

<style>
.btn {
  margin: 0;
}
</style>

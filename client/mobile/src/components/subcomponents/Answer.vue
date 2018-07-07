<template>
<div class="white pa-2 elevation-2 mt-2">
  <p class="caption">Answered by <strong>{{ answer.createUser }}</strong> on {{ answer.createDts | formatDate }}</p>
  <p class="caption" v-if="answer.createDts !== answer.updateDts">Updated on {{ answer.updateDts | formatDate }}</p>
  <p class="ma-0" v-if="!edit"><span v-html="answer.response" ></span></p>
  <quill-editor
  v-else
  style="background-color: white;"
  v-model="answer.response"
  ></quill-editor>
  <div v-if="$store.state.currentUser.username === answer.createUser">
    <v-btn icon color="success" v-if="edit" @click="editAnswer(answer.questionId, answer.responseId, answer.response)"><v-icon small class="icon">save</v-icon></v-btn>
    <v-btn icon v-else @click="edit = true"><v-icon small class="icon">edit</v-icon></v-btn>
    <v-btn icon @click="deleteDialog = true"><v-icon small class="icon">delete</v-icon></v-btn>
  </div>

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
          // answer updated
          console.log('Edit successful');
          this.edit = false;
        });
    },
    deleteAnswer (qid, aid) {
      console.log(`Delete Answer: ${aid}`);
      // if warning dialog === true
      this.$http.delete(`/openstorefront/api/v1/resource/components/${this.answer.componentId}/questions/${qid}/responses/${aid}`)
        .then(response => {
          // answer deleted
          console.log('Delete successful');
          this.deleteDialog = false;
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

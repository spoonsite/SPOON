<template>
  <v-dialog :value="value" @input="close" max-width="75em">
    <v-card>
      <ModalTitle :title="title" @close="close" />
      <v-card-text>
        <v-alert class="w-100" type="warning" :value="true"
          ><span v-html="$store.state.branding.userInputWarning"></span
        ></v-alert>
        <v-alert class="w-100" type="info" :value="!autoApprove"
          ><span v-html="$store.state.branding.submissionFormWarning"></span
        ></v-alert>
        <div v-if="this.question">
          Question: <br />
          <div v-html="this.question" class="py-4" />
        </div>
        <quill-editor style="background-color: white;" v-model="answer"></quill-editor>
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn color="success" :disabled="!answer || answer === ''" @click="submit">Submit</v-btn>
        <v-btn @click="close">Cancel</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import ModalTitle from '@/components/ModalTitle'

export default {
  name: 'Answer-Modal',
  props: ['value', 'title', 'answerProp', 'question', 'answerText'],
  components: {
    ModalTitle
  },
  mounted() {
    this.$http
      .get(`/openstorefront/api/v1/service/application/configproperties/userreview.autoapprove`)
      .then(response => (this.autoApprove = response.data.description === 'true'))
  },
  data() {
    return {
      questionText: '',
      answer: this.answerText,
      autoApprove: false
    }
  },
  methods: {
    close() {
      this.$emit('close')
    },
    submit() {
      this.$emit('close', {
        answer: this.answer,
        componentId: this.answerProp.componentId,
        questionId: this.answerProp.questionId,
        responseId: this.answerProp.responseId
      })
    }
  },
  watch: {
    value(val) {
      if (val === true) {
        this.$http
          .get(
            `/openstorefront/api/v1/resource/components/${this.answerProp.componentId}/questions/${this.answerProp.questionId}`
          )
          .then(response => {
            this.questionText = response.data.question
          })
      }
      this.answer = this.answerProp.response
    }
  }
}
</script>

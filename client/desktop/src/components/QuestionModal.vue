<template>
  <v-dialog :value="value" @input="close" max-width="75em">
    <v-card>
      <ModalTitle title="Ask a Question" @close="close" />
      <v-card-text>
        <v-alert class="w-100" type="warning" :value="true"
          ><span v-html="$store.state.branding.userInputWarning"></span
        ></v-alert>
        <v-alert class="w-100" type="info" :value="!autoApprove"
          ><span v-html="$store.state.branding.submissionFormWarning"></span
        ></v-alert>
        <quill-editor style="background-color: white;" v-model="question"></quill-editor>
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn color="success" :disabled="question === ''" @click="submit">Submit</v-btn>
        <v-btn @click="close">Cancel</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import ModalTitle from '@/components/ModalTitle'

export default {
  name: 'Question-Modal',
  props: ['value', 'questionProp'],
  components: {
    ModalTitle
  },
  mounted() {
    this.$http
      .get(`/openstorefront/api/v1/service/application/configproperties/userreview.autoapprove`)
      .then(response => {
        this.autoApprove = response.data.description === 'true'
      })
  },
  data() {
    return {
      question: '',
      autoApprove: false
    }
  },
  methods: {
    close() {
      this.$emit('close')
    },
    submit() {
      this.$emit('close', this.question)
    }
  },
  watch: {
    value(val) {
      this.question = this.questionProp
    }
  }
}
</script>

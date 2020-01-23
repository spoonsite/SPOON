<template>
  <div>
    <v-tabs icons-and-text color="black" background-color="grey lighten-3" slider-size="3" centered>
      <v-tab>
        <v-icon left>fa fa-question</v-icon>
        Questions
      </v-tab>
      <v-tab>
        <v-icon left>fa fa-comments</v-icon>
        Answers
      </v-tab>

      <v-tab-item>
        <div class="pa-5">
          <h2 class="text-center">Questions</h2>
          <v-btn color="grey lighten-2" @click="refreshQuestions"><v-icon left>fas fa-sync-alt</v-icon>Refresh</v-btn>
        </div>
        <v-data-table
          :headers="question.headers"
          :items="question.questions"
          :items-per-page="1000"
          class="elevation-1"
          hide-default-footer
          :expanded.sync="expanded"
          item-key="questionId"
          show-expand
        >
          <template v-slot:item.activeStatus="{ item }">
            <div v-if="item.activeStatus === 'A'">Active</div>
            <div v-else-if="item.activeStatus === 'P'">Pending</div>
            <div v-else>{{ item.activeStatus }}</div>
          </template>
          <template v-slot:item.question="{ item }">
            <div v-html="item.question" />
          </template>
          <template v-slot:item.createDts="{ item }">
            {{ item.createDts | formatDate }}
          </template>
          <template v-slot:item.questionUpdateDts="{ item }">
            {{ item.questionUpdateDts | formatDate }}
          </template>
          <template v-slot:item.actions="{ item }">
            <v-tooltip bottom>
              <template v-slot:activator="{ on }">
                <v-btn icon @click="viewComponent(item.componentId)" v-on="on">
                  <v-icon>fas fa-eye</v-icon>
                </v-btn>
              </template>
              <span>View Entry</span>
            </v-tooltip>
            <v-tooltip bottom>
              <template v-slot:activator="{ on }">
                <v-btn icon @click="editQuestion(item)" v-on="on">
                  <v-icon>fas fa-pencil-alt</v-icon>
                </v-btn>
              </template>
              <span>Edit</span>
            </v-tooltip>
          </template>
          <template v-slot:expanded-item="{ headers, item }">
            <td colspan="7" class="py-4">
              <h3>Answers:</h3>
              <div v-for="response in item.responses" :key="response.responseId" class="py-4">
                <v-card>
                  <v-card-text style="margin-bottom: -12px;">
                    <p class="text--primary" v-html="response.response" />
                  </v-card-text>
                </v-card>
              </div>
            </td>
          </template>
          <template v-slot:no-data>
            No questions have been asked...
          </template>
        </v-data-table>
      </v-tab-item>
      <v-tab-item>
        <div class="pa-5">
          <h2 class="text-center">Answers</h2>
          <v-btn color="grey lighten-2" @click="refreshAnswers"><v-icon left>fas fa-sync-alt</v-icon>Refresh</v-btn>
        </div>
        <v-data-table
          :headers="answer.headers"
          :items="answer.answers"
          :items-per-page="1000"
          class="elevation-1"
          hide-default-footer
        >
          <template v-slot:item.activeStatus="{ item }">
            <div v-if="item.activeStatus === 'A'">Active</div>
            <div v-else-if="item.activeStatus === 'P'">Pending</div>
            <div v-else>{{ item.activeStatus }}</div>
          </template>
          <template v-slot:item.response="{ item }">
            <div v-html="item.response" />
          </template>
          <template v-slot:item.answeredDate="{ item }">
            {{ item.answeredDate | formatDate }}
          </template>
          <template v-slot:item.updateDts="{ item }">
            {{ item.updateDts | formatDate }}
          </template>
          <template v-slot:item.actions="{ item }">
            <v-tooltip bottom>
              <template v-slot:activator="{ on }">
                <v-btn icon @click="viewComponent(item.componentId)" v-on="on">
                  <v-icon>fas fa-eye</v-icon>
                </v-btn>
              </template>
              <span>View Entry</span>
            </v-tooltip>
            <v-tooltip bottom>
              <template v-slot:activator="{ on }">
                <v-btn icon @click="editAnswer(item)" v-on="on">
                  <v-icon>fas fa-pencil-alt</v-icon>
                </v-btn>
              </template>
              <span>Edit</span>
            </v-tooltip>
            <v-tooltip bottom>
              <template v-slot:activator="{ on }">
                <v-btn
                  icon
                  @click="
                    answer.checkDeleteModal = true
                    answer.currentDelete = item
                  "
                  v-on="on"
                >
                  <v-icon>fas fa-trash</v-icon>
                </v-btn>
              </template>
              <span>Delete</span>
            </v-tooltip>
          </template>
          <template v-slot:no-data>
            No answers have been posted...
          </template>
        </v-data-table>
      </v-tab-item>
    </v-tabs>

    <!-- Modals -->
    <QuestionModal
      v-model="question.questionModal"
      :questionProp="question.currentQuestion.question"
      @close="submitQuestionEdit($event)"
    />
    <AnswerModal v-model="answer.answerModal" :answerProp="answer.currentAnswer" @close="submitAnswerEdit($event)" />
    <v-dialog v-model="question.checkDeleteModal" max-width="30em">
      <v-card>
        <ModalTitle title="Are you sure?" @close="question.checkDeleteModal = false" />
        <v-card-text>
          Are you sure you want to delete this question?
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn color="warning" @click="deleteQuestion(question.currentDelete)"
            ><v-icon left>mdi-delete</v-icon>Delete</v-btn
          >
          <v-btn @click="question.checkDeleteModal = false">Cancel</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-dialog v-model="answer.checkDeleteModal" max-width="30em">
      <v-card>
        <ModalTitle title="Are you sure?" @close="answer.checkDeleteModal = false" />
        <v-card-text>
          Are you sure you want to delete this answer?
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn color="warning" @click="deleteAnswer(answer.currentDelete)"
            ><v-icon left>mdi-delete</v-icon>Delete</v-btn
          >
          <v-btn @click="answer.checkDeleteModal = false">Cancel</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import ModalTitle from '@/components/ModalTitle'
import QuestionModal from '@/components/QuestionModal'
import AnswerModal from '@/components/AnswerModal'

export default {
  name: 'Questions',
  components: { QuestionModal, AnswerModal, ModalTitle },
  mounted() {
    this.refreshQuestions()
    this.refreshAnswers()
  },
  data() {
    return {
      expanded: [],
      question: {
        isLoading: false,
        checkDeleteModal: false,
        currentDelete: {},
        questionModal: false,
        currentQuestion: '',
        headers: [
          { text: 'Entry', value: 'componentName' },
          { text: 'Status', value: 'activeStatus' },
          { text: 'Question', value: 'question', sortable: false },
          { text: 'Update Date', value: 'questionUpdateDts' },
          { text: 'Post Date', value: 'createDts' },
          { text: 'Actions', value: 'actions', sortable: false },
          { text: '', value: 'data-table-expand', sortable: false }
        ],
        questions: []
      },
      answer: {
        isLoading: false,
        checkDeleteModal: false,
        currentDelete: {},
        answerModal: false,
        currentAnswer: '',
        headers: [
          { text: 'Entry', value: 'componentName' },
          { text: 'Status', value: 'activeStatus' },
          { text: 'Answer', value: 'response', sortable: false },
          // { text: 'Update Date', value: 'updateDts' },
          { text: 'Answer Date', value: 'answeredDate' },
          { text: 'Actions', value: 'actions', sortable: false }
        ],
        answers: []
      }
    }
  },
  methods: {
    editQuestion(question) {
      this.question.currentQuestion = question
      this.question.questionModal = true
    },
    submitQuestionEdit(event) {
      this.question.questionModal = false
      if (event === undefined) {
        return
      }
      this.$http
        .put(
          `/openstorefront/api/v1/resource/components/${this.question.currentQuestion.componentId}/questions/${this.question.currentQuestion.questionId}`,
          {
            question: event,
            userTypeCode: this.$store.state.currentUser.userTypeCode,
            organization: this.$store.state.currentUser.organization
          }
        )
        .then(response => {
          this.refreshQuestions()
        })
        .catch(error => {
          console.error(error)
        })
    },
    viewComponent(componentId) {
      this.$router.push({ name: 'Entry Detail', params: { id: componentId } })
    },
    refreshQuestions() {
      this.question.isLoading = true
      this.$http
        .get('/openstorefront/api/v1/resource/componentquestions/admin?status=A')
        .then(response => {
          this.question.isLoading = false
          this.question.questions = response.data
          this.$http
            .get('/openstorefront/api/v1/resource/componentquestions/admin?status=P')
            .then(response => {
              this.question.isLoading = false
              this.question.questions.push(...response.data)
              this.getAnswersToQuestions()
            })
            .catch(error => {
              console.error(error)
              this.question.isLoading = false
            })
        })
        .catch(error => {
          console.error(error)
          this.question.isLoading = false
        })
    },
    getAnswersToQuestions() {
      this.question.questions.forEach(question => {
        this.$http
          .get(`/openstorefront/api/v1/resource/componentquestions/${question.questionId}/responses`)
          .then(response => {
            question.responses = response.data
          })
      })
    },
    editAnswer(answer) {
      this.answer.currentAnswer = answer
      this.answer.answerModal = true
    },
    submitAnswerEdit(event) {
      this.answer.answerModal = false
      if (event === undefined) {
        return
      }
      this.$http
        .put(
          `/openstorefront/api/v1/resource/components/${event.componentId}/questions/${event.questionId}/responses/${event.responseId}`,
          {
            questionId: event.questionId,
            response: event.answer,
            userTypeCode: this.$store.state.currentUser.userTypeCode,
            organization: this.$store.state.currentUser.organization
          }
        )
        .then(response => {
          this.refreshAnswers()
        })
        .catch(error => {
          console.error(error)
        })
    },
    deleteAnswer(response) {
      this.answer.checkDeleteModal = false
      let componentId = response.componentId
      let questionId = response.questionId
      let responseId = response.responseId
      this.answer.isLoading = true
      this.$http
        .delete(
          `/openstorefront/api/v1/resource/components/${componentId}/questions/${questionId}/responses/${responseId}`
        )
        .then(response => {
          this.answer.isLoading = false
          this.refreshAnswers()
        })
        .catch(error => {
          console.error(error)
          this.answer.isLoading = false
        })
    },
    refreshAnswers() {
      this.answer.isLoading = true
      this.$http
        .get('/openstorefront/api/v1/resource/componentquestions/responses/admin?status=A')
        .then(response => {
          this.answer.answers = response.data
          this.$http
            .get('/openstorefront/api/v1/resource/componentquestions/responses/admin?status=P')
            .then(response => {
              this.answer.isLoading = false
              this.answer.answers.push(...response.data)
            })
            .catch(error => {
              console.error(error)
              this.answer.isLoading = false
            })
        })
        .catch(error => {
          console.error(error)
          this.answer.isLoading = false
        })
    }
  }
}
</script>

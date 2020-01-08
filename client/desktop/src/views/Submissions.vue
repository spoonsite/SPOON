<template lang="html">
  <div>
    <v-form style="padding: 1em; padding-top: 2em;">
      <v-flex class="d-flex" xs5>
        <v-btn class="top-buttons" @click="getUserParts()"><v-icon left>fas fa-sync-alt</v-icon>Refresh</v-btn>
        <v-btn class="top-buttons" @click="showData()"><v-icon left>fas fa-plus</v-icon>Add New</v-btn>
        <v-btn class="top-buttons" @click="bulkUploadDialog = true"
          ><v-icon left>fas fa-upload</v-icon>Bulk Upload</v-btn
        >
        <v-text-field
          v-model="search"
          append-icon="fas fa-search"
          label="Search"
          single-line
          hide-details
          style="padding-left: 1em; margin-bottom: 10px;"
        ></v-text-field>
      </v-flex>
      <div class="d-flex">
        <v-data-table
          :headers="tableHeaders"
          :items="componentData"
          :items-per-page="10"
          :loading="isLoading"
          class="tableLayout"
          :search="search"
        >
          <template v-slot:item.name="{ item }">
            {{ item.name }}
            <div v-if="item.submissionOriginalComponentId" class="red">Incomplete Change Request</div>
            <div v-else-if="item.submissionId" class="red">Incomplete Submission</div>
            <div v-else-if="item.evaluationsAttached" class="red">Evaluations Are In Progress</div>
          </template>
          <template v-slot:item.status="{ item }">
            <div v-if="item.status === 'A'">Active</div>
            <div v-else-if="item.status === 'P'">Pending</div>
            <div v-else-if="item.status === 'N'">Not Submitted</div>
            <div v-else>{{ item.status }}</div>
          </template>
          <template v-slot:item.submitDate="{ item }">
            <div v-if="item.submitDate">{{ item.submitDate | formatDate }}</div>
            <div v-else-if="item.status === 'P'">{{ item.lastUpdate | formatDate }}</div>
          </template>
          <template v-slot:item.pendingChange="{ item }">
            <div v-if="item.hasChangeRequest">Pending</div>
          </template>
          <template v-slot:item.lastUpdate="{ item }">
            {{ item.lastUpdate | formatDate }}
          </template>
          <template v-slot:item.approvalWorkflow="{ item }">
            <svg width="200" height="50">
              <g v-for="(step, i) in item.steps" :key="step.name" :id="step.name">
                <circle :cx="20 + i * 50" cy="25" r="15" stroke="black" :fill="'#' + step.color" />
                <line
                  v-if="i !== item.steps.length - 1"
                  :x1="35 + i * 50"
                  y1="25"
                  :x2="55 + i * 50"
                  y2="25"
                  style="stroke:black; stroke-width:2"
                ></line>
              </g>
            </svg>
          </template>
          <template v-slot:item.actions="{ item }">
            <div style="display: flex; flex-direction: row;">
              <v-tooltip bottom v-if="item.componentId">
                <template v-slot:activator="{ on }">
                  <v-btn icon v-on="on" :to="{ name: 'Entry Detail', params: { id: item.componentId } }">
                    <v-icon>fas fa-eye</v-icon>
                  </v-btn>
                </template>
                <span>View Entry</span>
              </v-tooltip>
              <v-tooltip bottom>
                <template v-slot:activator="{ on }">
                  <v-btn icon v-on="on">
                    <v-icon>fas fa-pencil-alt</v-icon>
                  </v-btn>
                </template>
                <span>Edit</span>
              </v-tooltip>
              <v-tooltip bottom>
                <template v-slot:activator="{ on }">
                  <v-btn icon>
                    <v-icon>far fa-comment</v-icon>
                  </v-btn>
                </template>
                <span>Comment</span>
              </v-tooltip>
              <v-tooltip bottom v-if="item.status !== 'P'">
                <template v-slot:activator="{ on }">
                  <v-btn icon v-on="on" @click="determineDeleteForm(item)">
                    <v-icon>fas fa-trash</v-icon>
                  </v-btn>
                </template>
                <span>Delete</span>
              </v-tooltip>
            </div>
          </template>
        </v-data-table>
      </div>
    </v-form>

    <v-dialog v-model="bulkUploadDialog" width="35em">
      <v-card>
        <ModalTitle title="Bulk Uploads" @close="bulkUploadDialog = false" />
        <v-card-text>
          <p>
            This bulk upload tool is designed to help you submit a part or parts into our database. You can upload a zip
            file containing PDFs, excel spreadsheets, or other human readable files. The SPOON support team will then do
            all the data entry for you.
          </p>
          <p>
            Once SPOON support is done entering your information, you will then need to review and submit the
            information for Subject Matter Expert (SME) review. Once the SME has approved your information your part
            will become searchable on the site.
          </p>
          <p style="color: red;">
            The information submitted to this site will be made publicly available. Please do not submit any sensitive
            information such as proprietary or ITAR restricted information.
          </p>
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-file-input label="File input"></v-file-input>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="commentsDialog" width="35em">
      <v-card>
        <ModalTitle title="Comments" @close="commentsDialog = false" />
        <v-card-text>
          <p>{{ comments }}</p>
        </v-card-text>
        <v-card-actions>
          <v-spacer />
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="deleteDialog" width="35em">
      <v-card>
        <ModalTitle title="Delete?" @close="deleteDialog = false" />
        <v-card-text>
          <v-btn
            v-if="currentComponent.hasChangeRequest"
            @click="
              requestRemoval = true
              deleteChange = false
            "
          >
            Request Removal
          </v-btn>
          <v-btn
            v-if="currentComponent.hasChangeRequest"
            @click="
              deleteChange = true
              requestRemoval = false
            "
          >
            Delete Change
          </v-btn>
          <p v-if="deleteChange || !currentComponent.hasChangeRequest" style="padding-top: 1em;">
            Are you sure you want to delete: {{ currentComponent.name }}?
          </p>
          <v-form v-if="requestRemoval">
            <v-container>
              <p>Reason:*</p>
              <v-textarea style="background-color: white;" v-model="removalForm.message" outline></v-textarea>
              <p>Contact Information:</p>
              <v-text-field :rules="formNameRules" single-line label="Name*" v-model="removalForm.name"> </v-text-field>
              <v-text-field :rules="formEmailRules" single-line label="Email*" v-model="removalForm.email">
              </v-text-field>
              <v-text-field single-line label="Phone" v-model="removalForm.phone"> </v-text-field>
              <v-text-field single-line label="Organization" v-model="removalForm.organization"> </v-text-field>
            </v-container>
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn color="warning" v-if="requestRemoval" @click="submitRemoval()">
            Submit
          </v-btn>
          <p v-else-if="currentComponent.hasChangeRequest && !requestRemoval && !deleteChange"></p>
          <v-btn color="warning" v-else @click="submitDeletion()">
            Submit
          </v-btn>
          <v-btn @click="deleteDialog = false">Cancel</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script lang="js">
import ModalTitle from '@/components/ModalTitle'

export default {
  name: 'submissions-page',
  components: {
    ModalTitle
  },
  mounted() {
    this.getUserParts()
  },
  data() {
    return {
      tableHeaders: [
        { text: 'Name', value: 'name' },
        { text: 'Status', value: 'status' },
        { text: 'Type', value: 'type' },
        { text: 'Submit/Approved Date', value: 'submitDate' },
        { text: 'Pending Change', value: 'pendingChange' },
        { text: 'Last Update', value: 'lastUpdate' },
        { text: 'Approval Workflow', value: 'approvalWorkflow', sortable: false },
        { text: 'Actions', value: 'actions', sortable: false }
      ],
      component: {
        name: '',
        status: '',
        type: '',
        submitDate: '',
        pendingChange: false,
        lastUpdate: '',
        steps: [
          {
            name: 'step',
            color: '#333'
          }
        ]
      },
      componentDisplay: [],
      componentData: [],
      comments: [],
      isLoading: true,
      counter: 0,
      search: '',
      bulkUploadDialog: false,
      commentsDialog: false,
      deleteDialog: false,
      requestRemoval: false,
      deleteChange: false,
      currentComponent: {},
      removalForm: {
        message: '',
        name: '',
        email: '',
        phone: '',
        organization: ''
      },
      formNameRules: [
        v => !!v || 'Name is required'
      ],
      formEmailRules: [
        v => !!v || 'Email is required'
      ],
      formReasonRules: [
        v => !!v || 'A reason is required'
      ],
      formMessageRules: [
        v => !!v || 'A message is required'
      ]
    }
  },
  methods: {
    getUserParts() {
      this.componentData = []
      this.counter = 0
      this.isLoading = true
      this.$http.get('/openstorefront/api/v1/resource/componentsubmissions/user')
        .then(response => {
          this.isLoading = false
          this.componentData = this.combineComponentsAndWorkPlans(response.data.componentSubmissionView, response.data.workPlans)
        }).catch(error => {
          this.isLoading = false
          this.errors.push(error)
        })
    },
    getComments(component) {
      if (component.componentId) {
        this.$http.get(`/openstorefront/api/v1/resource/components/${component.componentId}/comments`)
          .then(response => {
            this.comments = response.data
            this.commentsDialog = true
          })
          .catch(e => this.errors.push(e))
      } else {
        this.$http.get(`/openstorefront/api/v1/resource/usersubmissions/${component.submissionId}/comments`)
          .then(response => {
            this.comments = response.data
            this.commentsDialog = true
          })
          .catch(e => this.errors.push(e))
      }
    },
    viewComponent(componentId) {
      this.$router.push({ name: 'Entry Detail', params: { id: componentId } })
    },
    combineComponentsAndWorkPlans(allComponents, workPlans) {
      let components = allComponents.filter(e => e.componentId !== undefined)
      let submissions = allComponents.filter(e => e.userSubmissionId !== undefined)
      let updatedComponents = []

      components.forEach(component => {
        let myWorkPlan = null
        workPlans.forEach(workPlan => {
          if (component.workPlanID === workPlan.workPlanId) {
            myWorkPlan = workPlan
          }
        })
        if (myWorkPlan !== null) {
          updatedComponents.push(this.generateComponent(component, myWorkPlan))
        }
      })

      submissions.forEach(submission => {
        updatedComponents.push(this.generateSubmission(submission))
      })

      return updatedComponents
    },
    generateComponent(component, workPlan) {
      let seenCurrStep = false
      let steps = []

      workPlan.steps.forEach((step, index) => {
        if (!seenCurrStep) {
          if (component.stepId === step.workPlanStepId) {
            if (index === workPlan.steps.length - 1) {
              steps.push({
                name: step.name,
                color: workPlan.completeColor
              })
            } else {
              steps.push({
                name: step.name,
                color: workPlan.inProgressColor
              })
            }
            seenCurrStep = true
          } else {
            steps.push({
              name: step.name,
              color: workPlan.completeColor
            })
          }
        } else {
          steps.push({
            name: step.name,
            color: workPlan.pendingColor
          })
        }
      })

      // TODO: deal with the chance of the component being a submission

      let updatedComponent = {}

      updatedComponent = {
        name: component.name,
        lastUpdate: component.lastActivityDts,
        type: component.componentTypeLabel,
        componentId: component.componentId,
        status: component.approvalState,
        submitDate: component.approvedDts,
        steps: steps,
        submissionOriginalComponentId: component.submissionOriginalComponentId,
        evaluationsAttached: component.evaluationsAttached,
        hasChangeRequest: component.statusOfPendingChange != null,
        pendingChangeComponentId: component.pendingChangeComponentId
      }

      return updatedComponent
    },
    generateSubmission(submission) {
      return {
        name: submission.name,
        submissionId: submission.userSubmissionId,
        type: submission.componentTypeLabel,
        status: submission.approvalState,
        lastUpdate: submission.updateDts,
        steps: null,
        submissionOriginalComponentId: submission.submissionOriginalComponentId,
        evaluationsAttached: submission.evaluationsAttached
      }
    },
    determineDeleteForm(item) {
      this.currentComponent = item
      this.requestRemoval = false
      this.deleteChange = false
      if (this.currentComponent.status === 'A' && this.currentComponent.hasChangeRequest) {
        this.requestRemoval = false
      } else if (this.currentComponent.status === 'A') {
        this.requestRemoval = true
      }
      this.deleteDialog = true
    },
    submitRemoval() {
      let data = {
        securityMarkingType: '',
        dataSensitivity: '',
        description: 'Entry Name: ' + this.currentComponent.name + '\n\n' + this.removalForm.message,
        fullname: this.removalForm.name,
        email: this.removalForm.email,
        organization: this.removalForm.organization,
        phone: this.removalForm.phone,
        summary: this.currentComponent.name,
        ticketType: 'Request entry to be Unapproved'
      }
      this.$http.post(`/openstorefront/api/v1/resource/feedbacktickets`, data)
        .then(response => {
          this.deleteDialog = false
          this.removalForm.message = ''
          this.$toasted.show('Sent Sucessfully.')
        })
        .catch(e => this.$toasted.error('There was a problem submitting the correction.'))
    },
    submitDeletion() {
      this.isLoading = true
      if (this.currentComponent.submissionId) {
        this.$http.delete(`/openstorefront/api/v1/resource/usersubmissions/${this.currentComponent.submissionId}`)
          .then(response => {
            this.$toasted.show('Submission Deleted')
            this.getUserParts()
            this.deleteDialog = false
          })
          .catch(error => {
            this.$toasted.error('Submission could not be deleted.')
            this.errors.push(error)
            this.isLoading = false
          })
      } else {
        this.$http.delete(`/openstorefront/api/v1/resource/components/${this.currentComponent.pendingChangeComponentId}/cascade`)
          .then(response => {
            this.$toasted.show('Submission Deleted')
            this.getUserParts()
            this.deleteDialog = false
          })
          .catch(error => {
            this.$toasted.error('Submission could not be deleted.')
            this.errors.push(error)
            this.isLoading = false
          })
      }
    }
  }
}
</script>

<style scoped lang="scss">
.top-buttons {
  text-transform: none;
  background-color: #e0e0e0 !important;
}
.tableLayout {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}
</style>

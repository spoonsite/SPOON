<template lang="html">
  <div>
    <v-form class="pa-2 pt-4">
      <v-flex class="d-flex flex-wrap">
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
          style="padding-top: 0; margin-bottom: 1em; max-width: 20em;"
        ></v-text-field>
      </v-flex>
      <div class="d-flex">
        <v-data-table
          :headers="tableHeaders"
          :items="componentData"
          :items-per-page="10"
          :loading="isLoading"
          :footer-props="{
            'items-per-page-options': [10, 20, 30, 40, 50]
          }"
          :sort-by="['name']"
          class="tableLayout"
          :search="search"
        >
          <template v-slot:item.name="{ item }">
            {{ item.name }}
            <div v-if="item.submissionOriginalComponentId" style="color: red;">Incomplete Change Request</div>
            <div v-else-if="item.submissionId" style="color: red;">Incomplete Submission</div>
            <div v-else-if="item.evaluationsAttached" style="color: red;">Evaluations Are In Progress</div>
          </template>
          <template v-slot:item.submitDate="{ item }">
            <div v-if="item.submitDate">{{ item.submitDate | formatDate }}</div>
            <div v-else-if="item.status === 'Pending'">{{ item.lastUpdate | formatDate }}</div>
          </template>
          <template v-slot:item.lastUpdate="{ item }">
            {{ item.lastUpdate | formatDate }}
          </template>
          <template v-slot:item.approvalWorkflow="{ item }">
            <svg v-if="item && item.steps && item.steps.length > 0" :width="item.steps.length * 50" height="65">
              <g v-for="(step, i) in item.steps" :key="step.name" :id="step.name"  class="step">
                <circle class="circle" :cx="20 + i * 50" cy="25" r="15" stroke="black" :fill="'#' + step.color" />
                <line
                  v-if="i !== item.steps.length - 1"
                  :x1="35 + i * 50"
                  y1="25"
                  :x2="55 + i * 50"
                  y2="25"
                  style="stroke:black; stroke-width:2"
                ></line>
                <text v-if="item.currentStep" :x="i*50" y="60">{{ step.name }}</text>
              </g>
            </svg>
          </template>
          <template v-slot:item.actions="{ item }">
            <div style="display: flex;">
              <v-tooltip bottom v-if="item.componentId">
                <template v-slot:activator="{ on }">
                  <v-btn
                    icon
                    v-on="on"
                    :to="{ name: 'Entry Detail', params: { id: item.componentId } }"
                    style="order: 1"
                  >
                    <v-icon>fas fa-eye</v-icon>
                  </v-btn>
                </template>
                <span>View Entry</span>
              </v-tooltip>
              <v-tooltip bottom>
                <template v-slot:activator="{ on }">
                  <v-btn
                    v-if="item.status === 'Active'"
                    :href="'mailto:support@spoonsite.com?subject=Change%20Request%20for%20' + item.name"
                    icon
                    v-on="on"
                    style="order: 2"
                  >
                    <v-icon>fas fa-pencil-alt</v-icon>
                  </v-btn>
                  <v-btn v-else icon v-on="on" style="order: 2">
                    <v-icon>fas fa-pencil-alt</v-icon>
                  </v-btn>
                </template>
                <span>Edit</span>
              </v-tooltip>
              <v-tooltip bottom>
                <template v-slot:activator="{ on }">
                  <v-btn
                    icon
                    v-on="on"
                    @click="
                      $refs.comment.getComments(item)
                      currentComponent = item
                      commentsDialog = true
                    "
                    style="order: 3"
                  >
                    <v-icon>far fa-comment</v-icon>
                  </v-btn>
                </template>
                <span>Comment</span>
              </v-tooltip>
              <v-tooltip bottom v-if="item.status !== 'P'">
                <template v-slot:activator="{ on }">
                  <v-btn icon v-on="on" @click="determineDeleteForm(item)" style="order: 4">
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
    <!-- Display for new Bulk Upload Dialog -->
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
          <p class="red--text">
            The information submitted to this site will be made publicly available. Please do not submit any sensitive
            information such as proprietary or ITAR restricted information.
          </p>
          <v-file-input v-model="uploadFile" ref="bulkUploadFile"></v-file-input>
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn @click="openBulkUpload()">Upload</v-btn>
          <v-btn @click="bulkUploadDialog = false">Close</v-btn>
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
          <p v-if="currentComponent.hasChangeRequest && deleteChange" class="pt-2">
            Are you sure you want to delete the change request for: {{ currentComponent.name }}?
          </p>
          <p v-else-if="!currentComponent.hasChangeRequest && !requestRemoval" class="pt-2">
            Are you sure you want to delete: {{ currentComponent.name }}?
          </p>
          <v-form v-if="requestRemoval" v-model="isFormValid">
            <v-container>
              <p>Reason:*</p>
              <v-textarea
                style="background-color: white;"
                v-model="removalForm.message"
                :rules="formMessageRules"
                outlined
              ></v-textarea>
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
          <v-btn v-if="requestRemoval" color="warning" @click="submitRemoval()" :disabled="!isFormValid">
            Submit
          </v-btn>
          <p v-else-if="currentComponent.hasChangeRequest && !requestRemoval && !deleteChange"></p>
          <v-btn color="warning" v-else @click="submitDeletion()">
            Delete
          </v-btn>
          <v-btn
            @click="
              deleteDialog = false
              removalForm.message = ''
            "
            >Cancel</v-btn
          >
        </v-card-actions>
      </v-card>
    </v-dialog>

    <CommentModal
      v-model="commentsDialog"
      ref="comment"
      :component="currentComponent"
      @close="commentsDialog = false"
    ></CommentModal>
  </div>
</template>

<script lang="js">
import ModalTitle from '@/components/ModalTitle'
import CommentModal from '@/components/CommentModal'

export default {
  name: 'submissions-page',
  components: {
    ModalTitle,
    CommentModal
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
        isFormValid: false,
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
      isLoading: true,
      search: '',
      uploadFile: null,
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
        v => !!v || 'E-mail is required',
        v => /.+@.+/.test(v) || 'E-mail must be valid'
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
      this.isLoading = true
      this.$http.get('/openstorefront/api/v1/resource/componentsubmissions/user')
        .then(response => {
          this.isLoading = false
          this.componentData = this.combineComponentsAndWorkPlans(response.data.componentSubmissionView, response.data.workPlans)
        }).catch(error => {
          this.isLoading = false
          console.error(error)
        })
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
        } else {
          updatedComponents.push(this.generateComponent(component, null))
        }
      })

      submissions.forEach(submission => {
        updatedComponents.push(this.generateSubmission(submission))
      })

      return updatedComponents
    },
    generateComponent(component, workPlan) {
      let seenCurrStep = false
      let currentStep = ''
      let steps = []

      if (workPlan !== null) {
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
                currentStep = step.name
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
        if (currentStep === '') {
          currentStep = 'Approved'
        }
      }

      let updatedComponent = {}

      updatedComponent = {
        name: component.name,
        lastUpdate: component.lastActivityDts,
        type: component.componentTypeLabel,
        componentId: component.componentId,
        status: this.determineApprovalStatus(component),
        submitDate: component.approvedDts,
        pendingChange: this.determineChangeRequest(component),
        steps: steps,
        currentStep: currentStep,
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
        status: this.determineApprovalStatus(submission),
        lastUpdate: submission.updateDts,
        steps: null,
        submissionOriginalComponentId: submission.submissionOriginalComponentId,
        evaluationsAttached: submission.evaluationsAttached
      }
    },
    determineApprovalStatus(component) {
      if (component.approvalState === 'A') {
        return 'Active'
      } else if (component.approvalState === 'P') {
        return 'Pending'
      } else {
        return 'Not Submitted'
      }
    },
    determineChangeRequest(component) {
      if (component.statusOfPendingChange != null) {
        return 'Pending'
      } else {
        return ''
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
            console.error(error)
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
            console.error(error)
            this.isLoading = false
          })
      }
    },
    openBulkUpload() {
      window.open('openstorefront/bulkUpload.jsp', 'uploadWin', 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, width=500, height=440')
    }
  }
}
</script>

<style scoped lang="scss">
.top-buttons {
  text-transform: none;
  background-color: #e0e0e0 !important;
  margin-right: 1em;
  margin-top: 0.5em;
}
.tableLayout {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}
svg text {
  display: none;
}
svg g:hover text{
  display: block;
}
</style>

<template lang="html">
  <div>
    <v-form style="padding: 1em; padding-top: 2em;">
      <div>
        <v-btn class="top-buttons" @click="getUserParts()"
          ><v-icon left>fas fa-sync-alt</v-icon>Refresh</v-btn
        >
        <v-btn class="top-buttons" @click="showData()"
          ><v-icon left>fas fa-plus</v-icon>Add New</v-btn
        >
        <v-btn class="top-buttons" @click="bulkUploadDialog = true"
          ><v-icon left>fas fa-upload</v-icon>Bulk Upload</v-btn
        >
      </div>
      <div class="d-flex">
        <v-data-table
          :headers="tableHeaders"
          :items="componentData"
          :loading="isLoading"
          class="tableLayout"
          hide-actions
        >
          <template slot="items" slot-scope="props">
            <td>
              <div>{{ props.item.name }}</div>
              <div v-if="props.item.submissionOriginalComponentId" class="red">Incomplete Change Request</div>
              <div v-else-if="props.item.userSubmissionId" class="red">Incomplete Submission</div>
              <div v-else-if="props.item.evaluationsAttached" class="red">Evaluations Are In Progress</div>
            </td>
            <td>
              <div v-if="props.item.status === 'A'">Approved</div>
              <div v-if="props.item.status === 'P'">Pending</div>
              <div v-if="props.item.status === 'N'">Not Submitted</div>
            </td>

            <td>{{ props.item.type }}</td>
            <td>
              <div v-if="props.item.submitDate">{{ props.item.submitDate | formatDate }}</div>
              <div v-else-if="props.item.status === 'P'">{{ props.item.lastUpdate | formatDate }}</div>
              <!-- <div v-else="props.item.approvedDate">{{ props.item.approvedDate | formatDate }}</div> -->
            </td>
            <td>
              <div v-if="props.item.lastUpdate">{{ props.item.lastUpdate | formatDate }}</div>
            </td>
            <td>
              <div>
                <svg width="200" height="50">
                  <g v-for="(step, i) in props.item.steps"
                    :key="step.name"
                    :id="step.name"
                  >
                    <circle
                      :cx="20 + i * 50"
                      cy="25"
                      r="15"
                      stroke="black"
                      :fill="'#' + step.color"
                    />
                    <line
                      v-if="i !== props.item.steps.length - 1"
                      :x1="35 + i * 50"
                      y1="25"
                      :x2="55 + i * 50"
                      y2="25"
                      style="stroke:black; stroke-width:2"
                    ></line>
                  </g>
                </svg>
              </div>
            </td>
            <td>
              <div style="display: flex; flex-direction: row;">
                <!-- TODO: Add tool tips:
                  Component:
                    - Request Change
                    - Comments
                    - Request Removal
                    - View
                  Submission:
                    - Edit
                    - Delete
                    - Comments -->
                <v-btn small fab icon class="grey lighten-2" v-if="props.item.componentId" @click="viewComponent(props.item.componentId)">
                  <v-icon>far fa-eye</v-icon>
                </v-btn>
                <v-btn small fab icon class="grey lighten-2">
                  <v-icon>fas fa-pencil-alt</v-icon>
                </v-btn>
                <v-btn small fab icon class="grey lighten-2" @click="getComments(props.item)">
                  <v-icon>far fa-comment</v-icon>
                </v-btn>
                <v-btn small fab icon class="red lighten-3" v-if="props.item.status !== 'P'" @click="deleteDialog = true; currentComponent = props.item">
                  <v-icon>fas fa-trash</v-icon>
                </v-btn>
              </div>
            </td>
          </template>
        </v-data-table>
      </div>
    </v-form>
    <v-dialog v-model="bulkUploadDialog" width="35em">
      <v-card>
        <ModalTitle title="Bulk Uploads" @close="bulkUploadDialog = false" />
        <v-card-text>
          <p>
            This bulk upload tool is designed to help you submit a part or parts
            into our database. You can upload a zip file containing PDFs, excel
            spreadsheets, or other human readable files. The SPOON support team
            will then do all the data entry for you.
          </p>
          <p>
            Once SPOON support is done entering your information, you will then
            need to review and submit the information for Subject Matter Expert
            (SME) review. Once the SME has approved your information your part
            will become searchable on the site.
          </p>
          <p style="color: red;">
            The information submitted to this site will be made publicly
            available. Please do not submit any sensitive information such as
            proprietary or ITAR restricted information.
          </p>
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <!-- <v-file-input label="File input"></v-file-input> -->
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-dialog v-model="commentsDialog" width="35em">
      <v-card>
        <ModalTitle title="Comments" @close="commentsDialog = false" />
        <v-card-text>
          <!-- <p>Tag to be removed: <strong style="color: red;">{{ tagName }}</strong></p> -->
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
          <p v-if="currentComponent.status === 'N'">Are you sure you want to delete: {{ currentComponent.name }}?</p>
          <p>{{currentComponent}}</p>
          <v-form>
            <v-container>
              <p>Reason:*</p>
              <v-textarea
                style="background-color: white;"
                v-model="removalForm.message"
                outline
              ></v-textarea>
              <p>Contact Information:</p>
              <v-text-field
                :rules="formNameRules"
                single-line
                label="Name*"
                v-model="removalForm.name"
              >
              </v-text-field>
              <v-text-field
                :rules="formEmailRules"
                single-line
                label="Email*"
                v-model="removalForm.email"
              >
              </v-text-field>
              <v-text-field
                single-line
                label="Phone"
                v-model="removalForm.phone"
              >
              </v-text-field>
              <v-text-field
                single-line
                label="Organization"
                v-model="removalForm.organization"
              >
              </v-text-field>
            </v-container>
          </v-form>
          <!-- <p>Tag to be removed: <strong style="color: red;">{{ tagName }}</strong></p> -->
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn v-if="currentComponent.isChangeRequest" @click="requestRemoval = true; deleteChange = false;">Request Removal</v-btn>
          <v-btn v-if="currentComponent.isChangeRequest" @click="deleteChange = true; requestRemoval = false;">Delete Change</v-btn>
          <v-btn color=warning>Yes</v-btn>
          <v-btn>No</v-btn>
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
  mounted () {
    this.getUserParts()
  },
  data () {
    return {
      tableHeaders: [
        { text: 'Name', value: 'name' },
        { text: 'Status', value: 'status' },
        { text: 'Type', value: 'type' },
        { text: 'Submit/Approved Date', value: 'submitDate' },
        // { text: 'Pending Change', value: 'pendingChange' },
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
      bulkUploadDialog: false,
      commentsDialog: false,
      deleteDialog: false,
      requestRemoval: false,
      deleteChange: false,
      currentComponent: {},
      removalForm: {
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
      ],
    }
  },
  methods: {
    getUserParts () {
      this.counter = 0
      this.isLoading = true
      this.$http.get('/openstorefront/api/v1/resource/componentsubmissions/user')
        .then(response => {
          this.isLoading = false
          // console.log(response)
          this.componentData = this.combineComponentsAndWorkPlans(response.data.componentSubmissionView, response.data.workPlans)
          // console.log(this.componentData)
        }).catch(error => {
          this.isLoading = false
          console.error(error)
        })
    },
    getComments (component) {
      if (component.componentId) {
        this.$http.get(`/openstorefront/api/v1/resource/components/${component.componentId}/comments`)
          .then(response => {
            this.comments = response.data
            this.commentsDialog = true
          })
          .catch(e => this.errors.push(e))
      }
      else {
        this.$http.get(`/openstorefront/api/v1/resource/usersubmissions/${component.submissionId}/comments`)
          .then(response => {
            this.comments = response.data
            this.commentsDialog = true
          })
          .catch(e => this.errors.push(e))
      }
    },
    viewComponent (componentId) {
      this.$router.push({ name: 'Entry Detail', params: { id: componentId } })
    },
    combineComponentsAndWorkPlans (allComponents, workPlans) {
      for(var i =0; i<allComponents.length; i++) {
        if(allComponents[i].componentId === undefined) {
          console.log(allComponents[i])
        }
      }
      let components = allComponents.filter(e => e.componentId !== undefined)
      let submissions = allComponents.filter(e => e.userSubmissionId !== undefined)
      let updatedComponents = []

// console.log(components)
// console.log(submissions)
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
    generateComponent (component, workPlan) {
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
        steps: steps
      }

      return updatedComponent
    },
    generateSubmission (submission) {
      return {
        name: submission.name,
        submissionId: submission.submissionId,
        type: submission.componentTypeLabel,
        status: submission.approvalState,
        isChangeRequest: submission.isChangeRequest,
        lastUpdate: submission.updateDts,
        steps: null
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

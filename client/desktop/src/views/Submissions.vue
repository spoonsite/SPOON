<template lang="html">
  <!-- <v-layout
    row
    justify-center
    align-center
    v-if="isLoading"
    >
    <v-flex xs1>
      <v-progress-circular
        color="primary"
        :size="60"
        :width="6"
        indeterminate
      ></v-progress-circular>
    </v-flex>
  </v-layout> -->
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
        <v-btn class="top-buttons pr-2" @click="commentsDialog = true"
          ><v-icon left>far fa-comment</v-icon>Comments</v-btn
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
              <div v-if="props.item.isChangeRequest" class="red">Incomplete Change Request</div>
              <div v-else-if="props.item.submissionId" class="red">Incomplete Submission</div>
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
            </td>

            <!-- <td>N/A</td> -->
            <td>
              <div v-if="props.item.lastUpdate">{{ props.item.lastUpdate | formatDate }}</div>
            </td>
            <td>
              <div v-if="props.item.componentId">
                <svg width="200" height="50">
                  <!-- <circle v-for="(step, i) in props.item.approvalWorkflow.steps" :cx="cx" cy="25" r="15" stroke="black" fill="blue" /> -->
                  <line
                    x1="35"
                    y1="25"
                    x2="55"
                    y2="25"
                    style="stroke:black; stroke-width:2"
                  ></line>
                  <!-- <circle cx="70" cy="25" r="15" stroke="black" fill="grey" /> -->
                  <line
                    x1="85"
                    y1="25"
                    x2="105"
                    y2="25"
                    style="stroke:black; stroke-width:2"
                  ></line>
                  <!-- <circle cx="120" cy="25" r="15" stroke="black" fill="grey" /> -->
                  <line
                    x1="135"
                    y1="25"
                    x2="155"
                    y2="25"
                    style="stroke:black; stroke-width:2"
                  ></line>
                  <!-- <circle cx="170" cy="25" r="15" stroke="black" fill="grey" /> -->
                </svg>
              </div>
            </td>
            <td>
              <div>
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
                <v-btn icon class="pa-4 grey lighten-2" v-if="props.item.componentId" @click="viewComponent(props.item.componentID)">
                  <v-icon>far fa-eye</v-icon>
                </v-btn>
                <v-btn icon class="pa-4 grey lighten-2">
                  <v-icon>fas fa-pencil-alt</v-icon>
                </v-btn>
                <v-btn icon class="pa-4 red lighten-3">
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
        </v-card-text>
        <v-card-actions>
          <v-spacer />
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
      isLoading: true,
      counter: 0,
      bulkUploadDialog: false,
      commentsDialog: false,
      cx: 20
    }
  },
  methods: {
    getUserParts () {
      this.counter = 0
      this.isLoading = true
      this.$http.get('/openstorefront/api/v1/resource/componentsubmissions/user')
        .then(response => {
          this.isLoading = false
          console.log(response)
          this.componentData = this.combineComponentsAndWorkPlans(response.data.componentSubmissionView, response.data.workPlans)
        }).catch(error => {
          this.isLoading = false
          console.error(error)
        })
    },
    viewComponent (componentId) {
      this.$router.push({ name: 'Entry Detail', params: { id: componentId } })
    },
    combineComponentsAndWorkPlans (allComponents, workPlans) {
      let components = allComponents.filter(e => e.componentId !== undefined)
      let submissions = allComponents.filter(e => e.submissionId !== undefined)
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
        type: component.type,
        componentId: component.componentId,
        status: component.status,
        submitDate: component.approveDts,
        steps: steps
      }

      return updatedComponent
    },
    generateSubmission (submission) {
      return {
        name: submission.name,
        submissionId: submission.submissionId,
        type: submission.type,
        status: submission.status,
        isChangeRequest: submission.isChangeRequest,
        lastUpdate: submission.lastActivityDts,
        steps: null
      }
    },
    openBulkUpload () {
      // window.open('openstorefront/bulkUpload.jsp','uploadWin', 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, width=500, height=440')
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

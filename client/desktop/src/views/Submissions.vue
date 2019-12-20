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
        <v-btn class="top-buttons" @click="getUserParts()">Refresh</v-btn>
        <v-btn class="top-buttons" @click="showData()"
          ><v-icon class="fa-xs pr-2">fas fa-plus</v-icon>Add New</v-btn
        >
        <v-btn class="top-buttons pr-2" @click="bulkUploadDialog = true"
          ><v-icon>fas fa-upload</v-icon>Bulk Upload</v-btn
        >
        <v-btn class="top-buttons pr-2" @click="commentsDialog = true"
          ><v-icon>far fa-comment</v-icon>Comments</v-btn
        >
      </div>
      <div class="d-flex">
        <v-data-table
          :headers="tableHeaders"
          :items="componentDisplay"
          class="tableLayout"
        >
          <template slot="items" slot-scope="props">
            <td>{{ props.item.name }}</td>
            <td>{{ props.item.status }}</td>

            <td>{{ props.item.type }}</td>
            <td>{{ props.item.submitDate }}</td>

            <td>N/A</td>
            <td>{{ props.item.lastUpdate }}</td>
            <td style="display: flex; flex-direction: row;">
              <v-btn class="grey lighten-2" small fab>
                <v-icon>far fa-eye</v-icon>
              </v-btn>
              <v-btn small fab class="grey lighten-2" small f
                ><v-icon>fas fa-pencil-alt</v-icon></v-btn
              >
              <v-btn small fab class="table-buttons red lighten-3"
                ><v-icon>fas fa-trash</v-icon></v-btn
              >
            </td>
            <td>
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
        { text: 'Submit Date', value: 'submitDate' },
        { text: 'Pending Change', value: 'pendingChange' },
        { text: 'Last Update', value: 'lastUpdate' },
        { text: 'Actions', value: 'actions' },
        { text: 'Approval Workflow', value: 'approvalWorkflow' }
      ],
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
      this.$http.get(`/openstorefront/api/v1/resource/componentsubmissions/user`)
        .then(response => {
          this.isLoading = true
          this.componentData = []
          this.componentDisplay = []
          this.componentData = response.data
          this.formatData()
          console.log(this.componentData)
          // this.getComponentWorkplan()
        })
    },
    getComponentWorkplan () {
      for (var component in this.componentDisplay) {
        if (this.componentData[component].componentId) {
          this.$http.get(`/openstorefront/api/v1/resource/components/${this.componentData[component].componentId}/worklink`)
          .then(response => {
            this.componentDisplay[component][approvalWorkflow] = response.data

          })
          .finally(() => {
            this.counter++
            if (this.counter >= this.componentData.length) {
              this.isLoading = false
            }
          })
        }
        else {
          this.$http.get(`/openstorefront/api/v1/resource/usersubmissions/${this.componentData[component].userSubmissionId}/worklink`)
          .then(response => {
            this.componentDisplay[component][approvalWorkflow] = response.data
            console.log(this.componentDisplay[component].approvalWorkflow)

          })
          .finally(() => {
            this.counter++
            if (this.counter >= this.componentData.length) {
              this.isLoading = false
            }
          })
        }
      }

    },
    formatData () {
      for (var component in this.componentData) {
        this.removeDescriptionHtml(component)
        this.changeDateFormat(component)
      }
      this.setUpTableArray()
    },
    removeDescriptionHtml (component) {
      var description = this.componentData[component].description
      var tmp = document.createElement('div')
      tmp.innerHTML = description
      description = tmp.innerText
      var descriptionLength = 200
      description = description.slice(0, descriptionLength) + '...'
      this.componentData[component].description = description
    },
    changeDateFormat (component) {
      var updateDate = new Date(this.componentData[component].updateDts)
      this.componentData[component].updateDts = updateDate.toDateString()
      var submittedDate = new Date(this.componentData[component].submittedDts)
      this.componentData[component].submittedDts = submittedDate.toDateString()
      var approvedDate = new Date(this.componentData[component].approvedDts)
      this.componentData[component].approvedDts = approvedDate.toDateString()
    },
    getSubmitOrApprovalDate (component) {
      if (this.componentData[component].approvalState === 'A') {
				return this.componentData[component].approvedDts;
			}
      else if (this.componentData[component].approvalState === 'N') {
        return ''
      }
      else {
				return this.componentData[component].submittedDts;
			}
    },
    setUpTableArray () {
      for (var component in this.componentData) {
        this.componentDisplay.push({
          name: this.componentData[component].name,
          status: this.componentData[component].approvalStateLabel,
          type: this.componentData[component].componentTypeLabel,
          submitDate: this.getSubmitOrApprovalDate(component),
          pendingChange: this.componentData[component].adminModified,
          lastUpdate: this.componentData[component].updateDts
        })
      }
    },
    openBulkUpload () {
      // window.open('openstorefront/bulkUpload.jsp','uploadWin', 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, width=500, height=440')
    },
    showData () {
      console.log(this.componentDisplay)
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

<template lang="html">
  <div>
    <v-form style="padding: 1em; padding-top: 2em;">
      <div>
        <v-btn class="top-buttons">Refresh</v-btn>
        <v-btn class="top-buttons"><v-icon class="fa-xs">fas fa-plus</v-icon>&nbsp; Add New</v-btn>
        <v-btn class="top-buttons"><v-icon>fas fa-upload</v-icon>&nbsp; Bulk Upload</v-btn>
      </div>
      <div style="display: flex; ">
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
              <v-btn
                class="grey lighten-2"
                small fab
              >
                <v-icon>far fa-eye</v-icon>
              </v-btn>
              <v-btn small fab class="grey lighten-2" small f><v-icon>fas fa-pencil-alt</v-icon></v-btn>
              <v-btn small fab class="table-buttons red lighten-3"><v-icon>fas fa-trash</v-icon></v-btn>
            </td>
            <td>
              <svg width="300" height="50">
                <circle cx="20" cy="25" r="20" fill="blue" />
              </svg>
            </td>
          </template>
        </v-data-table>
      </div>
    </v-form>
  </div>
</template>

<script lang="js">

export default {
  name: 'submissions-page',
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
      componentData: []
    }
  },
  methods: {
    getUserParts () {
      this.$http.get(`/openstorefront/api/v1/resource/componentsubmissions`)
        .then(response => {
          this.componentData = response.data
          this.formatData()
        })
        .catch(e => this.errors.push(e))
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
      var createDate = new Date(this.componentData[component].createDts)
      this.componentData[component].createDts = createDate.toDateString()
      var updateDate = new Date(this.componentData[component].updateDts)
      this.componentData[component].updateDts = updateDate.toDateString()
    },
    setUpTableArray () {
      for (var component in this.componentData) {
        this.componentDisplay.push({
          name: this.componentData[component].name,
          status: this.componentData[component].approvalStateLabel,
          type: this.componentData[component].componentType,
          submitDate: this.componentData[component].updateDts,
          pendingChange: this.componentData[component].adminModified,
          lastUpdate: this.componentData[component].adminModified
        })
      }
      console.log(this.componentData)
    }
  }
}
</script>

<style scoped lang="scss">
  .top-buttons {
    text-transform: none;
    background-color: #E0E0E0 !important;
  }
  .tableLayout {
    display: flex;
    flex-direction: column;
    flex-grow: 1;
  }
</style>

<template lang="html">
  <div>
    <v-form style="padding: 1em; padding-top: 2em;">
      <div>
        <v-btn class="top-buttons">Refresh</v-btn>
        <v-btn class="top-buttons"><v-icon class="fa-xs">fas fa-plus</v-icon>&nbsp Add New</v-btn>
        <v-btn class="top-buttons"><v-icon>fas fa-upload</v-icon>&nbsp Bulk Upload</v-btn>
      </div>

      <table>
        <tr>
          <th v-for="header in tableHeaders">
            {{ header }}
          </th>
        </tr>
        <tr v-for="component in componentSubmissions">
          <td style="max-width: 10vw;">{{ component.name }}</td>
          <td>{{ component.approvalStateLabel }}</td>
          <td style="max-width: 25vw;">{{ component.description }}</td>
          <td>{{ component.componentType }}</td>
          <td>{{ component.createDts }}</td>
          <td>{{ component.ownerUser }}</td>
          <td>N/A</td>
          <td>{{ component.updateDts }}</td>
          <td style="display: flex; flex-direction: row;"> 
            <v-btn
              class="table-buttons grey lighten-2"
              v-if="component.approvalStateLabel == 'Approved'"
            >
              <v-icon>far fa-eye</v-icon>
            </v-btn>
            <v-btn class="table-buttons grey lighten-2"><v-icon>fas fa-pencil-alt</v-icon></v-btn>
            <v-btn class="table-buttons red lighten-3"><v-icon>fas fa-trash</v-icon></v-btn>
          </td>
          <td>
            <svg width="300" height="50">
              <circle cx="20" cy="25" r="20" fill="blue" />
            </svg>
          </td>
        </tr>
      </table>
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
        'Name', 'Status', 'Description', 'Type', 'Submit Date', 'Email', 'Pending Change', 'Last Update', 'Actions', 'Approval Workflow'
      ],
      componentSubmissions: []
    }
  },
methods: {
    getUserParts () {
      this.$http.get(`/openstorefront/api/v1/resource/componentsubmissions`)
        .then(response => {
          this.componentSubmissions = response.data
          console.log(this.componentSubmissions)
          this.formatData()
        })
        .catch(e => this.errors.push(e))
    },
    formatData () {
      for (var component in this.componentSubmissions) {
        this.removeDescriptionHtml(component)
        this.changeDateFormat(component)
      }
    },
    removeDescriptionHtml (component) {
      var description = this.componentSubmissions[component].description
        var tmp = document.createElement("DIV");
        tmp.innerHTML = description;
        description = tmp.innerText;
        var descriptionLength = 200
        description = description.slice(0, descriptionLength) + '...'
        this.componentSubmissions[component].description = description
    },
    changeDateFormat (component) {
      var createDate = new Date(this.componentSubmissions[component].createDts)
      this.componentSubmissions[component].createDts = createDate.toDateString()
      var updateDate = new Date(this.componentSubmissions[component].updateDts)
      this.componentSubmissions[component].updateDts = updateDate.toDateString()
    }
  }
}
</script>

<style scoped lang="scss">
  .top-buttons {
    text-transform: none;
    background-color: #E0E0E0 !important;
  }
  .table-buttons {
    min-width: 3em;
  }
  td {
    padding: 1em;
  }

</style>
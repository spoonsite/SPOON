<template lang="html">
  <div>
    <v-form style="padding: 1em; padding-top: 2em;">
      <div>
        <v-btn class="buttons">Refresh</v-btn>
        <v-btn class="buttons"><v-icon class="fa-xs">fas fa-plus</v-icon>&nbsp Add New</v-btn>
        <v-btn class="buttons"><v-icon>fas fa-upload</v-icon>&nbsp Bulk Upload</v-btn>
      </div>
      <table>
        <tr>
          <th v-for="header in tableHeaders">
            {{ header }}
          </th>
        </tr>
      </table>
    </v-form>
  </div>
</template>

<script lang="js">

export default {
  name: 'submissions-page',
  data () {
    return {
      tableHeaders: [
        'Name', 'Status', 'Description', 'Type', 'Submit Date', 'Email', 'Pending Change', 'Last Update', 'Actions', 'Approval Workflow'
      ],
      newPassword: '',
      confirmPassword: '',
      errorMessages: [],
      existingPasswordError: [],
      showExisting: false,
      showNew: false,
      showConfirmation: false
    }
  },
methods: {

  }
}
</script>

<style scoped lang="scss">
  .buttons {
    text-transform: none;
    background-color: #E0E0E0 !important;
  }
</style>
<template>
  <div>
    <v-menu
      offset-y
      left
      :close-on-content-click="false"
      min-width="35em"
    >
      <v-btn icon slot="activator">
        <v-badge left overlap light color="info">
          <span v-if="notifications && notifications.length > 0" slot="badge">{{ newNotifications }}</span>
          <v-icon>fas fa-envelope</v-icon>
        </v-badge>
      </v-btn>
      <v-list three-line v-if="notifications && notifications.length > 0">
        <v-subheader>Notifications</v-subheader>
        <template v-for="(item, index) in notifications">
          <v-list-tile :key="item.eventId">
            <v-list-tile-content :class="`${item.readMessage ? '' : 'font-weight-bold' }`">
              <v-list-tile-title>{{ item.message }}</v-list-tile-title>
              <v-list-tile-sub-title>{{ item.updateDts | formatDate }}</v-list-tile-sub-title>
              <v-list-tile-sub-title>Event Type: {{ item.eventTypeDescription }}</v-list-tile-sub-title>
            </v-list-tile-content>
            <v-list-tile-action>
              <v-btn icon @click="deleteNotification(item.eventId)"><v-icon>fas fa-trash</v-icon></v-btn>
            </v-list-tile-action>
          </v-list-tile>
          <v-divider v-if="index + 1 < notifications.length" :key="index"></v-divider>
        </template>
      </v-list>
      <v-card>
        <v-card-text v-if="!notifications || notifications.length === 0">No Notifications</v-card-text>
        <v-card-actions>
        <v-btn
          v-if="notifications && notifications.length > 0"
          color="primary"
          @click="deleteAllDialog = !deleteAllDialog"
        >
          Delete All
        </v-btn>
        <v-btn :loading="fetchingNotifications" @click="getNotifications()">Refresh</v-btn>
        </v-card-actions>
      </v-card>
    </v-menu>

    <v-dialog
      v-model="deleteAllDialog"
      max-width="300px"
      >
      <v-card>
        <v-card-text>
          <p>Are you sure you want to delete all notifications?</p>
        </v-card-text>
        <v-card-actions>
          <v-btn :loading="deletingAllNotifications" color="warning" @click="deleteAllNotifications()"><v-icon>delete</v-icon> Delete</v-btn>
          <v-btn @click="deleteAllDialog = false">Cancel</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

  </div>
</template>

<script>

export default {
  name: 'Notifications',
  data () {
    return {
      messagesDialog: false,
      notifications: [],
      deleteAllDialog: false,
      deletingAllNotifications: false,
      fetchingNotifications: false
    }
  },
  mounted () {
    this.getNotifications()
  },
  computed: {
    newNotifications () {
      var cnt = 0

      if (this.notifications) {
        this.notifications.forEach((el) => {
          if (!el.readMessage) cnt += 1
        })
      }
      return cnt
    }
  },
  methods: {
    getNotifications () {
      this.fetchingNotifications = true
      this.$http
        .get('/openstorefront/api/v1/resource/notificationevent')
        .then(response => {
          this.notifications = response.data.data
        })
        .finally(() => {
          this.fetchingNotifications = false
        })
    },
    deleteNotification (id) {
      this.$http
        .delete(`/openstorefront/api/v1/resource/notificationevent/${id}`)
        .then(response => {
          this.$toasted.success('Notification deleted')
          this.getNotifications()
        })
    },
    deleteAllNotifications () {
      this.deletingAllNotifications = true
      this.$http
        .delete(`/openstorefront/api/v1/resource/notificationevent/currentuser`)
        .then(response => {
          this.$toasted.success('All notifications deleted')
          this.getNotifications()
        })
        .finally(() => {
          this.deletingAllNotifications = false
          this.deleteAllDialog = false
        })
    }
  }
}

</script>

<style>

</style>

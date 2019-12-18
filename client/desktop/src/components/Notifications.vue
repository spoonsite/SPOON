<template>
  <div>
    <v-menu offset-y left :close-on-content-click="false">
      <template v-slot:activator="{ on }">
        <v-btn icon v-on="on">
          <v-badge left overlap light color="info">
            <span v-if="notifications && notifications.length > 0" slot="badge">{{ newNotifications }}</span>
            <v-tooltip bottom>
              <template v-slot:activator="{ on }">
                <v-icon v-on="on">fas fa-envelope</v-icon>
              </template>
              <span>Notifications</span>
            </v-tooltip>
          </v-badge>
        </v-btn>
      </template>
      <v-card>
        <v-list three-line v-if="notifications && notifications.length > 0">
          <v-list-item v-for="(item, index) in notifications" :key="index">
            <v-list-item-content :class="`${item.readMessage ? '' : 'font-weight-bold'}`">
              <v-list-item-title>{{ item.message }}</v-list-item-title>
              <v-list-item-subtitle>{{ item.updateDts | formatDate('yyyy/mm/dd - HH:mm:ss') }}</v-list-item-subtitle>
              <v-list-item-subtitle>Event Type: {{ item.eventTypeDescription }}</v-list-item-subtitle>
            </v-list-item-content>
            <v-list-item-action>
              <v-btn icon @click="deleteNotification(item.eventId)">
                <v-icon>fas fa-trash</v-icon>
              </v-btn>
            </v-list-item-action>
          </v-list-item>
        </v-list>
        <v-card-text v-if="!notifications || notifications.length === 0" class="font-weight-bold"
          >No Notifications</v-card-text
        >
        <v-card-actions>
          <v-btn
            v-if="notifications && notifications.length > 0"
            color="primary"
            @click="deleteAllDialog = !deleteAllDialog"
            >Delete All</v-btn
          >
          <v-btn :loading="fetchingNotifications" @click="getNotifications()">Refresh</v-btn>
        </v-card-actions>
      </v-card>
    </v-menu>

    <v-dialog v-model="deleteAllDialog" max-width="25em">
      <v-card>
        <ModalTitle title="Delete?" @close="deleteAllDialog = false" />
        <v-card-text>
          <p>Are you sure you want to delete all notifications?</p>
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn :loading="deletingAllNotifications" color="warning" @click="deleteAllNotifications()">
            <v-icon>delete</v-icon>Delete
          </v-btn>
          <v-btn @click="deleteAllDialog = false">Cancel</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import ModalTitle from '@/components/ModalTitle'

export default {
  name: 'Notifications',
  components: {
    ModalTitle
  },
  data() {
    return {
      messagesDialog: false,
      notifications: [],
      deleteAllDialog: false,
      deletingAllNotifications: false,
      fetchingNotifications: false
    }
  },
  mounted() {
    this.getNotifications()
  },
  computed: {
    newNotifications() {
      var cnt = 0

      if (this.notifications) {
        this.notifications.forEach(el => {
          if (!el.readMessage) cnt += 1
        })
      }
      return cnt
    }
  },
  methods: {
    sortedNotifications() {
      this.notifications.sort((a, b) => {
        return new Date(b.updateDts) - new Date(a.updateDts)
      })
      return this.notifications
    },
    getNotifications() {
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
    deleteNotification(id) {
      this.$http.delete(`/openstorefront/api/v1/resource/notificationevent/${id}`).then(response => {
        this.$toasted.success('Notification deleted')
        this.getNotifications()
      })
    },
    deleteAllNotifications() {
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

<style></style>

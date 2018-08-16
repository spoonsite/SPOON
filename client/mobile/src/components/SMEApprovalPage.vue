<template lang="html">

  <section class="sme-approval-page">
    <v-layout align-center row wrap>
      <v-flex pa-3 xs12 md6 offset-md3>
        <h1>Assigned to Me</h1>
      </v-flex>

      <v-flex xs12 md6 offset-md3>
        <v-expansion-panel popout>
          <v-expansion-panel-content
            v-for="entry in submissions.data"
            :key="entry.componentId"
            v-if="entry.hasOwnProperty('currentUserAssigned') && entry.currentUserAssigned === $store.state.currentUser.username">
            <div slot="header">{{entry.linkName}}</div>
            <v-card class="grey lighten-3">
              <v-card-text>
                <strong>Component Type: </strong>
                <p>
                    {{ entry.componentTypeDescription }}
                </p>
              </v-card-text>
              <v-layout column>
                <v-btn @click="viewEntry(entry.componentId)" class="primary">View Entry</v-btn>
                <v-btn @click="workView(entry)" class="primary">Work View</v-btn>
                <v-btn @click="unassign(entry)" class="primary">Unassign</v-btn>
              </v-layout>
            </v-card>
          </v-expansion-panel-content>
        </v-expansion-panel>
      </v-flex>
    <!-- </v-layout> -->

    <v-divider class="my-2"></v-divider>

    <!-- <v-layout column> -->
      <v-flex pa-3 xs12 md6 offset-md3>
        <h1>Unassigned</h1>
      </v-flex>

      <v-flex xs12 md6 offset-md3>
        <v-expansion-panel popout>
          <v-expansion-panel-content
            v-for="entry in submissions.data"
            :key="entry.componentId"
            v-if="!entry.hasOwnProperty('currentUserAssigned')">
            <div slot="header">{{entry.linkName}}</div>
            <v-card class="grey lighten-3">
              <v-card-text>
                <strong>Component Type: </strong>
                <p>
                  {{ entry.componentTypeDescription }}
                </p>
              </v-card-text>
              <v-layout column>
                <v-btn @click="viewEntry(entry.componentId)" class="primary">View Entry</v-btn>
                <v-btn @click="assignToMe(entry)" class="primary">Assign to Me</v-btn>
              </v-layout>
            </v-card>
          </v-expansion-panel-content>
        </v-expansion-panel>
      </v-flex>

    </v-layout>

    <LoadingOverlay v-model="isLoading"></LoadingOverlay>
  </section>

</template>

<script lang="js">
import LoadingOverlay from './subcomponents/LoadingOverlay';
import router from '../router/index';

export default {
  name: 'sme-approval-page',
  props: [],
  components: {
    LoadingOverlay,
    router
  },
  mounted () {
    this.getWorkPlan();
  },
  data () {
    return {
      submissions: {},
      isLoading: false
    };
  },
  methods: {
    assignToMe (entry) {
      this.$http.put(`/openstorefront/api/v1/resource/workplans/${entry.workPlanId}/worklinks/${entry.workPlanLinkId}/assigntome`)
        .then(res => {
          this.assignedToMe = res.data;
          this.unassigned = res.data;
        })
        .finally(() => {
          this.getWorkPlan();
        });
    },
    getWorkPlan () {
      this.$http.get(`/openstorefront/api/v1/resource/workplans/worklinks`)
        .then(res => {
          this.submissions = res.data;
        });
    },
    unassign (entry) {
      this.$http.put(`/openstorefront/api/v1/resource/workplans/${entry.workPlanId}/worklinks/${entry.workPlanLinkId}/unassign`)
        .finally(() => {
          this.getWorkPlan();
        });
    },
    viewEntry (componentId) {
      router.push({
        name: 'Entry Detail',
        params: {
          id: componentId
        }
      });
    },
    workView (entry) {
      router.push({
        name: 'Work Plan Process',
        params: {
          id: entry.workPlanLinkId
        }
      });
    }

  }
};
</script>

<style scoped lang="scss">
  .sme-approval-page {

  }
</style>

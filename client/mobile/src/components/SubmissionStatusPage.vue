<template lang="html">
  <div class="submission-status-page">

    <v-expansion-panel v-if="data && data.length > 0">
      <v-expansion-panel-content v-for="item in data" :key="item.name">
        <div slot="header">
          <h3>
            <v-icon v-if="item.approvalState === 'N'" color="red darken-2">warning</v-icon>
            <v-icon v-if="item.approvalState === 'P'" color="orange darken-2">hourglass_empty</v-icon>
              {{ item.name }}
            </h3>
          </div>
        <v-card>
          <v-card-text>
            <p>
              <strong>Submission Status: </strong>{{ item.approvalStateLabel }}
            </p>
            <p v-if="item.updateDts">
              <strong>Last Update Date: </strong>{{ item.updateDts | formatDate }}
            </p>
            <p v-if="item.approvedDts">
              <strong>Approved Date: </strong>{{ item.approvedDts | formatDate }}
            </p>
          </v-card-text>
          <v-card-actions v-if="item.approvalState !== 'N'">
            <v-btn color="accent" @click="moreInformation(item.componentId)">More Information</v-btn>
          </v-card-actions>
        </v-card>
      </v-expansion-panel-content>
    </v-expansion-panel>

    <v-container v-else-if="!loading" text-xs-center>
      <h2>You don't have any submissions.</h2>
      <v-spacer style="height: 1.5em"></v-spacer>
      <v-btn class="primary" v-on:click="$router.push('/')">Return to Search</v-btn>
    </v-container>

    <LoadingOverlay v-model="loading"></LoadingOverlay>

  </div>
</template>

<script lang="js">
import LoadingOverlay from './subcomponents/LoadingOverlay';

export default {
  name: 'submission-status-page',
  components: {
    LoadingOverlay
  },
  mounted () {
    this.getComponentInfo();
  },
  data () {
    return {
      data: [],
      loading: true
    };
  },
  methods: {
    getComponentInfo () {
      this.$http.get('/openstorefront/api/v1/resource/componentsubmissions')
        .then(response => {
          this.data = response.data;

          var normal = [];
          var pending = [];
          var ret = [];

          if (response.data) {
            for (var i = 0; i < response.data.length; i++) {
              if (response.data[i].approvalState === 'N') {
                ret.push(response.data[i]);
              } else if (response.data[i].approvalState === 'P') {
                pending.push(response.data[i]);
              } else {
                normal.push(response.data[i]);
              }
            }

            normal = normal.reverse();
            pending = pending.reverse();
            ret = ret.reverse();

            ret = ret.concat(pending).concat(normal);

            this.data = ret;
          }
        })
        .finally(() => { this.loading = false; });
    },
    moreInformation (componentId) {
      this.$router.push({
        name: 'Entry Detail',
        params: {
          id: componentId
        }
      });
    }
  },
  computed: {
  }
};
</script>

<style scoped lang="scss">

</style>

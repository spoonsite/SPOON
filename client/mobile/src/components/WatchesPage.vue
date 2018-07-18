<template lang="html">

  <div class="watches-page">
    <div  v-if="watches.length > 0">
      <v-expansion-panel popout>
        <v-expansion-panel-content v-for="item in watches" :key="item.componentName">
          <div slot="header" v-if="item.lastUpdateDts > item.lastViewDts" class="light-green accent-1">
            <strong>{{ item.componentName }}</strong>
          </div>
          <div slot="header" v-else>
            {{ item.componentName }}
          </div>
          <v-card>
            <v-card-text>
              <p>
                <strong>Last update to entry: </strong>  {{ item.lastUpdateDts | formatDate('YYYY/MM/DD hh:mm') }}
              </p>
              <p>
                <strong>Last time viewed: </strong> {{ item.lastViewDts | formatDate}}
              </p>
            </v-card-text>
            <v-card-actions>
            <v-btn color="info" @click="moreInformation(item.componentId)">More Information</v-btn>
            </v-card-actions>
          </v-card>
        </v-expansion-panel-content>
      </v-expansion-panel>
    </div>

    <div v-else>
      <v-card>
        <v-card-text>
          You aren't watching any entries.
        </v-card-text>
        <v-card-actions>
          <v-btn color="info" @click="$router.push('/')">Return to Search</v-btn>
        </v-card-actions>
      </v-card>
    </div>
  </div>

</template>

<script lang="js">
import router from '../router/index';

export default {
  name: 'watches-page',
  props: [],
  mounted () {
    this.getWatches();
  },
  data () {
    return {
      watches: []
    };
  },
  methods: {
    getWatches () {
      this.$http.get('/openstorefront/api/v1/resource/userprofiles/' + this.$store.state.currentUser.username + '/watches')
        .then(response => {
          if (response) {
            if (response.data) {
              if (response.data.length > 0) {
                this.watches = response.data;
              }
            }
          }
        })
        .catch(e => this.errors.push(e));
    },
    moreInformation (componentId) {
      router.push({
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
  .watches-page {

  }
</style>

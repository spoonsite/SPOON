<template lang="html">

  <div class="watches-page">
    <h2 class="text-center">Watches</h2>

    <div v-if="loading" class="text-xs-center overlay">
      <v-progress-circular
        color="primary"
        :size="60"
        :width="6"
        indeterminate
        class="spinner"
      ></v-progress-circular>
    </div>

    <v-layout v-if="watches.length > 0" mt-3 mx-2>
    <v-flex xs12 md6 offset-md3>
      <v-expansion-panels popout>
        <v-expansion-panel v-for="item in watches" :key="item.componentName" :class="updateClasses(item)">
          <v-expansion-panel-header>
            {{ item.componentName}}
          </v-expansion-panel-header>
          <v-expansion-panel-content>
              <p v-if="item.lastSubmitDts" class="date"><strong>Last Vendor Update Provided:</strong> {{ item.lastSubmitDts | formatDate }}</p>
              <p v-else class="date"><strong>Last Vendor Update Provided:</strong> {{ item.approvedDts | formatDate }}</p>
              <p class="date"><strong>Last System Update:</strong> {{ item.lastUpdateDts | formatDate }}</p>
              <v-btn color="accent" :to="`entry-detail/${item.componentId}`">More Information</v-btn>
              <!-- <v-btn color="accent" @click="moreInformation(item.componentId)">More Information</v-btn> -->
          </v-expansion-panel-content>
        </v-expansion-panel>
      </v-expansion-panels>
    </v-flex>
    </v-layout>

    <v-container v-else-if="!loading" text-xs-center>
      <h2>You aren't watching any entries.</h2>
      <v-spacer style="height: 1.5em"></v-spacer>
      <v-btn class="primary" v-on:click="$router.push('/')">Return to Search</v-btn>
    </v-container>
  </div>

</template>

<script lang="js">
import router from '@/router/index'

export default {
  name: 'watches-page',
  props: [],
  mounted() {
    // need to check if we have the current user
    if (this.$store.state.currentUser.username) {
      this.getWatches()
    } else {
      // trigger an update once the user has been fetched
      this.$store.watch(
        (state, getters) => state.currentUser,
        (newValue, oldValue) => {
          this.getWatches()
        }
      )
    }
  },
  data() {
    return {
      watches: [],
      loading: true
    }
  },
  methods: {
    nav(url) {
      router.push(url)
    },
    getWatches() {
      this.loading = true
      this.$http.get('/openstorefront/api/v1/resource/userprofiles/' + this.$store.state.currentUser.username + '/watches')
        .then(response => {
          if (response.data && response.data.length > 0) {
            this.watches = response.data
          }
          this.loading = false
        })
    },
    moreInformation(componentId) {
      router.push({
        name: 'Entry Detail',
        params: {
          id: componentId
        }
      })
    },
    updateClasses(item) {
      return item.lastUpdateDts > item.lastViewDts ? 'light-green accent-1' : ''
    }
  }
}
</script>

<style scoped lang="scss">
.centeralign {
  margin-right: auto;
  margin-left: auto;
}

.overlay {
  width: 100%;
  height: 100%;
  pointer-events: all;
  margin-top: 10%;
}
</style>

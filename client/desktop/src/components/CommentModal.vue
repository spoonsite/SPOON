<template>
  <v-dialog :value="value" @input="close" width="40em">
      <v-card>
        <ModalTitle title="Workflow Comments" @close="close" />
        <v-card-text>
          <p>Click a comment you are an author of to edit/delete it.</p>
          <v-tabs
            v-model="tab"
          >
            <v-tabs-slider></v-tabs-slider>
            <v-tab href="#tab-1">Public</v-tab>
            <v-tab href="#tab-2" v-if="permission">Private</v-tab>
          </v-tabs>
          <v-tabs-items v-model="tab">
            <v-tab-item
              v-for="i in 2"
              :key="i"
              :value="'tab-' + i"
            >
              <v-card flat>
                <div style="background-color: #F5F5F5; height: 500px;">
                  <div
                    v-for="comment in comments"
                      :key="comment.comment"
                      style="overflow: hidden; margin: 1em;"
                    >
                      <v-flex xs6
                        v-if="comment.updateUser === $store.state.currentUser.username"
                        style="float:right; margin-top: 1em;"
                      >
                        <p style="margin: 0; text-align: center;">{{ comment.createUser }} {{ comment.createDts | formatDate }}</p>
                        <div style="background-color: #FFF9C4; border-radius: 15px;  padding: 0.75em;">
                          {{ comment.comment }}
                        </div>
                      </v-flex>
                      <v-flex xs6
                        v-else
                      >
                        <p style="margin: 0; text-align: center;">{{ comment.createUser }} {{ comment.createDts | formatDate }}</p>
                        <div style="background-color: #B3E5FC; border-radius: 15px;  padding: 0.75em;">
                          {{ comment.comment }}
                        </div>
                      </v-flex>
                  </div>
                </div>
                <quill-editor
                  style="background-color: white;"
                ></quill-editor>
                <v-btn>Post Comment</v-btn>
              </v-card>
            </v-tab-item>
          </v-tabs-items>
        </v-card-text>
        <v-card-actions>
          <v-spacer />
        </v-card-actions>
      </v-card>

    </v-dialog>
</template>

<script>
import ModalTitle from '@/components/ModalTitle'
// import permissions from '@/util/permissions'

export default {
  name: 'CommentModal',
  props: {
    value: {
      type: Boolean,
      default: false
    },
    component: Object
  },
  components: {
    ModalTitle
  },
  data() {
    return {
      comments: [],
      permission: false,
      // tabs: [
      //   { name: 'Private' },
      //   { name: 'Public' }
      // ],
      tabs: 3,
      tab: null,
      errors: []
    }
  },
  methods: {
    close() {
      this.$emit('close')
    },
    getComments(component) {
      if (component.componentId) {
        this.$http.get(`/openstorefront/api/v1/resource/components/${component.componentId}/comments`)
          .then(response => {
            this.comments = response.data
          })
          .catch(e => this.errors.push(e))
      } else {
        this.$http.get(`/openstorefront/api/v1/resource/usersubmissions/${component.submissionId}/comments`)
          .then(response => {
            this.comments = response.data
          })
          .catch(e => this.errors.push(e))
      }
      var perm = ['WORKFLOW-ADMIN-SUBMISSION-COMMENTS']
      this.permission = this.checkPermissions(perm)
    },
    checkPermissions(has) {
      let ret = false
      if (has.length === 0) {
        ret = true
      } else {
        has.forEach(perm => {
          if (this.$store.getters.hasPermission(perm)) {
            ret = true
          }
        })
      }
      return ret
    }
  }
}
</script>

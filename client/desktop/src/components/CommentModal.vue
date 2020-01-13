<template>
  <v-dialog :value="value" @input="close" width="40em">
      <v-card>
        <ModalTitle title="Workflow Comments" @close="close" />
        <v-card-text>
          <p>{{ component.name }}</p>
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
              <v-layout row justify-center align-center v-if="isLoading" style="height:100%;">
                <v-flex xs1>
                  <v-progress-circular color="primary" :size="60" :width="6" indeterminate class="spinner"></v-progress-circular>
                </v-flex>
              </v-layout>
              <v-card flat v-else>
                <div class="background">
                  <div
                    v-for="comment in comments"
                      :key="comment.comment"
                      style="overflow: hidden; margin: 1em;"
                  >
                    <div v-if="!comment.privateComment && i === 1">
                      <v-flex xs6
                        v-if="comment.updateUser === $store.state.currentUser.username"
                        class="user-text-location"
                      >
                        <p class="center-text">{{ comment.createUser }}</p>
                        <p class="center-text">{{ comment.createDts | formatDate("Pp") }}</p>
                        <div class="user-comments" v-html="comment.comment"/>
                      </v-flex>
                      <v-flex xs6
                        v-else
                      >
                        <p class="center-text">{{ comment.createUser }}</p>
                        <p class="center-text">{{ comment.createDts | formatDate("Pp") }}</p>
                        <div class="contact-comments" v-html="comment.comment"/>
                      </v-flex>
                    </div>

                    <div v-else-if="comment.privateComment && i === 2">
                      <v-flex xs6
                        v-if="comment.updateUser === $store.state.currentUser.username"
                        class="user-text-location"
                      >
                        <p class="center-text">{{ comment.createUser }}</p>
                        <p class="center-text">{{ comment.createDts | formatDate("Pp") }}</p>
                        <div class="user-comments" v-html="comment.comment"/>
                      </v-flex>
                      <v-flex xs6
                        v-else
                      >
                        <p class="center-text">{{ comment.createUser }}</p>
                        <p class="center-text">{{ comment.createDts | formatDate("Pp") }}</p>
                        <div class="contact-comments" v-html="comment.comment"/>
                      </v-flex>
                    </div>
                  </div>
                </div>
                <quill-editor
                  style="background-color: white;"
                  v-model="newComment"
                ></quill-editor>
                <v-btn v-if="i === 1" @click="submitPublicComment()">Post Comment</v-btn>
                <v-btn v-else @click="submitPrivateComment()">Post Comment</v-btn>
              </v-card>
            </v-tab-item>
          </v-tabs-items>
        </v-card-text>
      </v-card>

    </v-dialog>
</template>

<script>
import ModalTitle from '@/components/ModalTitle'

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
      newComment: '',
      permission: false,
      isLoading: false,
      tabs: 2,
      tab: null,
      errors: []
    }
  },
  methods: {
    close() {
      this.$emit('close')
    },
    getComments(component) {
      if (component.pendingChangeComponentId) {
        this.$http.get(`/openstorefront/api/v1/resource/components/${component.pendingChangeComponentId}/comments`)
          .then(response => {
            console.log(response.data)
            this.comments = response.data
            this.isLoading = false
          })
          .catch(e => this.errors.push(e))
      } else if (component.componentId) {
        this.$http.get(`/openstorefront/api/v1/resource/components/${component.componentId}/comments`)
          .then(response => {
            console.log(response.data)
            this.comments = response.data
            this.isLoading = false
          })
          .catch(e => this.errors.push(e))
      } else {
        this.$http.get(`/openstorefront/api/v1/resource/usersubmissions/${component.submissionId}/comments`)
          .then(response => {
            console.log(response.data)
            this.comments = response.data
            this.isLoading = false
          })
          .catch(e => this.errors.push(e))
      }
      var perm = ['WORKFLOW-ADMIN-SUBMISSION-COMMENTS']
      this.permission = this.checkPermissions(perm)
    },
    submitPrivateComment() {
      console.log(this.component)
      this.isLoading = true
      let data = {
        comment: this.newComment,
        commentType: 'SUBMISSION',
        privateComment: true,
        willSendEmail: false
      }
      if (this.component.componentId) {
        this.submitComponentComment(data)
      } else {
        this.submitSubmissionComment(data)
      }
    },
    submitPublicComment() {
      console.log(this.component)
      this.isLoading = true
      let data = {
        comment: this.newComment,
        commentType: 'SUBMISSION',
        privateComment: false,
        willSendEmail: false
      }
      if (this.component.componentId) {
        this.submitComponentComment(data)
      } else {
        this.submitSubmissionComment(data)
      }
    },
    submitComponentComment(data) {
      this.$http.post(`/openstorefront/api/v1/resource/components/${this.component.componentId}/comments`, data)
        .then(response => {
          this.getComments(this.component)
          this.newComment = ''
        })
        .catch(e => this.$toasted.error('There was a problem submitting your comment.'))
    },
    submitSubmissionComment(data) {
      this.$http.post(`/openstorefront/api/v1/resource/usersubmissions/${this.component.submissionId}/comments`, data)
        .then(response => {
          this.getComments(this.component)
          this.newComment = ''
        })
        .catch(e => this.$toasted.error('There was a problem submitting your comment.'))
    },
    submitChangeRequestComment(data) {
      this.$http.post(`/openstorefront/api/v1/resource/components/${this.component.pendingChangeComponentId}/comments`, data)
        .then(response => {
          this.getComments(this.component)
          this.newComment = ''
        })
        .catch(e => this.$toasted.error('There was a problem submitting your comment.'))
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
<style>
.user-comments {
  background-color: #FFF9C4;
  border-radius: 15px;
  padding: 0.75em;
  margin-bottom: 0;
}
.contact-comments {
  background-color: #B3E5FC;
  border-radius: 15px;
  padding: 0.75em;
}
.user-text-location {
  float: right;
  margin-top: 1em;
}
.center-text {
  margin: 0 !important;
  text-align: center;
}
.background {
  background-color: #F5F5F5;
  height: 30em;
  overflow: auto;
}
</style>

<template>
  <div>
    <v-dialog :value="value" @input="close" width="40em">
      <v-card>
        <ModalTitle title="Workflow Comments" @close="close" />
        <v-card-text>
          <p class="">{{ component.name }}</p>
          <v-tabs grow>
            <v-tab>
              Public
            </v-tab>
            <v-tab v-if="permission">
              Private
            </v-tab>
            <v-tab-item>
              <v-card flat>
                <div class="background">
                  <v-layout row justify-center align-center v-if="isLoading" style="height:100%;">
                    <v-flex xs1>
                      <v-progress-circular color="primary" :size="60" :width="6" indeterminate class="spinner"></v-progress-circular>
                    </v-flex>
                  </v-layout>
                  <div
                    v-else
                    v-for="comment in comments"
                      :key="comment.comment"
                      class="mx-1"
                      style="overflow: hidden;"
                  >
                    <div v-if="!comment.privateComment">
                      <v-flex xs8
                        v-if="comment.updateUser === $store.state.currentUser.username"
                        class="user-text-location"
                      >
                        <p class="right-text">{{ comment.createUser }}</p>
                        <p class="right-text">{{ comment.createDts | formatDate("Pp") }}</p>
                        <div class="user-comments" v-html="comment.comment"/>
                        <div class="d-flex flex-row">
                          <v-menu offset-y auto>
                            <template v-slot:activator="{ on }">
                              <v-spacer></v-spacer>
                              <v-btn
                                icon
                                v-on="on"
                              >
                                <v-icon>fas fa-ellipsis-h</v-icon>
                              </v-btn>
                            </template>
                            <v-list>
                              <v-list-item @click="editing = true; publicComment = comment.comment; currentComment = comment">
                                <v-list-item-title>Edit</v-list-item-title>
                              </v-list-item>
                              <v-list-item @click="currentComment=comment; deleteDialog = true;">
                                <v-list-item-title>Delete</v-list-item-title>
                              </v-list-item>
                            </v-list>
                          </v-menu>
                        </div>
                      </v-flex>
                      <v-flex
                        xs6
                        v-else
                      >
                        <p class="left-text">{{ comment.createUser }}</p>
                        <p class="left-text">{{ comment.createDts | formatDate("Pp") }}</p>
                        <div class="contact-comments" v-html="comment.comment"/>
                      </v-flex>
                    </div>
                  </div>
                </div>
                <quill-editor
                  style="background-color: white;"
                  v-model="publicComment"
                ></quill-editor>
                <p class="ma-0 red--text">This comment will be sent to the vendor.</p>
                <v-btn v-if="!editing" @click="submitPublicComment()">Post Comment</v-btn>
                <v-btn v-else-if="editing" @click="editPublicComment()">Update Comment</v-btn>
              </v-card>
            </v-tab-item>
            <v-tab-item>
              <v-card flat>
                <div class="background">
                  <v-layout row justify-center align-center v-if="isLoading" style="height:100%;">
                    <v-flex xs1>
                      <v-progress-circular color="primary" :size="60" :width="6" indeterminate class="spinner"></v-progress-circular>
                    </v-flex>
                  </v-layout>
                  <div
                    v-else
                    v-for="comment in comments"
                      :key="comment.comment"
                      class="mx-1"
                      style="overflow: hidden;"
                  >
                    <div v-if="comment.privateComment">
                      <v-flex xs8
                        v-if="comment.updateUser === $store.state.currentUser.username"
                        class="user-text-location"
                      >
                        <p class="right-text">{{ comment.createUser }}</p>
                        <p class="right-text">{{ comment.createDts | formatDate("Pp") }}</p>
                        <div class="user-comments" v-html="comment.comment"/>
                        <div class="d-flex flex-row">
                          <v-spacer/>
                          <v-menu offset-y auto>

                            <template v-slot:activator="{ on }">
                              <v-btn
                                icon
                                v-on="on"
                              >
                                <v-icon>fas fa-ellipsis-h</v-icon>
                              </v-btn>
                            </template>
                            <v-list>
                              <v-list-item @click="editing = true; privateComment=comment.comment; currentComment = comment">
                                <v-list-item-title>Edit</v-list-item-title>
                              </v-list-item>
                              <v-list-item @click="currentComment=comment; deleteDialog = true;">
                                <v-list-item-title>Delete</v-list-item-title>
                              </v-list-item>
                            </v-list>
                          </v-menu>
                        </div>
                      </v-flex>
                      <v-flex xs6
                        v-else
                      >
                        <p class="left-text">{{ comment.createUser }}</p>
                        <p class="left-text">{{ comment.createDts | formatDate("Pp") }}</p>
                        <div class="contact-comments" v-html="comment.comment"/>
                      </v-flex>
                    </div>
                  </div>
                </div>
                <quill-editor
                  style="background-color: white;"
                  v-model="privateComment"
                ></quill-editor>
                <v-btn v-if="!editing" @click="submitPrivateComment()">Post Comment</v-btn>
                <v-btn v-else-if="editing" @click="editPrivateComment()">Update Comment</v-btn>
              </v-card>
            </v-tab-item>
          </v-tabs>
        </v-card-text>
      </v-card>
    </v-dialog>

    <v-dialog v-model="deleteDialog" width="30em">
      <v-card>
        <ModalTitle title="Delete Comment?" @close="deleteDialog = false" />
        <v-card-text>
          <p> Are you sure you want to delete the comment:</p>
          <p class="red--text" v-html="currentComment.comment"></p>

        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn @click="deleteComment()">Delete</v-btn>
          <v-btn @click="deleteDialog = false">Close</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
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
      publicComment: '',
      privateComment: '',
      currentComment: '',
      deleteDialog: false,
      permission: false,
      isLoading: false,
      editing: false,
      tabs: 2,
      tab: null
    }
  },
  methods: {
    close() {
      this.$emit('close')
    },
    getComments(component) {
      this.editing = false
      var url = ''
      if (component.pendingChangeComponentId) {
        url = `/openstorefront/api/v1/resource/components/${component.pendingChangeComponentId}/comments`
      } else if (component.componentId) {
        url = `/openstorefront/api/v1/resource/components/${component.componentId}/comments`
      } else {
        url = `/openstorefront/api/v1/resource/usersubmissions/${component.submissionId}/comments`
      }
      this.$http.get(url)
        .then(response => {
          this.comments = response.data
          this.isLoading = false
        })
        .catch(error => {
          this.$toasted.error('An error occurred retrieving comments')
          console.error(error)
        })
      var perm = ['WORKFLOW-ADMIN-SUBMISSION-COMMENTS']
      this.permission = this.checkPermissions(perm)
    },
    submitPrivateComment() {
      this.isLoading = true
      let data = {
        comment: this.privateComment,
        commentType: 'SUBMISSION',
        privateComment: true,
        willSendEmail: false
      }
      this.submitComment(data)
    },
    submitPublicComment() {
      this.isLoading = true
      let data = {
        comment: this.publicComment,
        commentType: 'SUBMISSION',
        privateComment: false,
        willSendEmail: false
      }
      this.submitComment(data)
    },
    submitComment(data) {
      var submitUrl = ''
      if (this.component.pendingChangeComponentId) {
        submitUrl = `/openstorefront/api/v1/resource/components/${this.component.pendingChangeComponentId}/comments`
      } else if (this.component.componentId) {
        submitUrl = `/openstorefront/api/v1/resource/components/${this.component.componentId}/comments`
      } else {
        submitUrl = `/openstorefront/api/v1/resource/usersubmissions/${this.component.submissionId}/comments`
      }
      this.$http.post(submitUrl, data)
        .then(response => {
          this.getComments(this.component)
          this.publicComment = ''
          this.privateComment = ''
        })
        .catch(error => {
          this.$toasted.error('There was a problem submitting your comment')
          console.error(error)
        })
    },
    deleteComment() {
      var deleteUrl = ''
      if (this.component.pendingChangeComponentId) {
        deleteUrl = `/openstorefront/api/v1/resource/components/${this.component.pendingChangeComponentId}/comments/${this.currentComment.commentId}`
      } else if (this.component.componentId) {
        deleteUrl = `/openstorefront/api/v1/resource/components/${this.component.componentId}/comments/${this.currentComment.commentId}`
      } else {
        deleteUrl = `/openstorefront/api/v1/resource/usersubmissions/${this.component.submissionId}/comments/${this.currentComment.commentId}`
      }
      this.$http.delete(deleteUrl)
        .then(response => {
          this.isLoading = true
          this.$toasted.show('Comment Deleted')
          this.getComments(this.component)
          this.deleteDialog = false
        })
        .catch(error => {
          this.$toasted.error('Submission could not be deleted.')
          console.error(error)
          this.isLoading = false
        })
    },
    editPublicComment() {
      this.isLoading = true
      let data = {
        comment: this.publicComment,
        commentType: 'SUBMISSION',
        privateComment: false,
        willSendEmail: false
      }
      this.editComment(data)
    },
    editPrivateComment() {
      this.isLoading = true
      let data = {
        comment: this.privateComment,
        commentType: 'SUBMISSION',
        privateComment: true,
        willSendEmail: false
      }
      this.editComment(data)
    },
    editComment(data) {
      var editUrl = ''
      if (this.component.pendingChangeComponentId) {
        editUrl = `/openstorefront/api/v1/resource/components/${this.component.pendingChangeComponentId}/comments/${this.currentComment.commentId}`
      } else if (this.component.componentId) {
        editUrl = `/openstorefront/api/v1/resource/components/${this.component.componentId}/comments/${this.currentComment.commentId}`
      } else {
        editUrl = `/openstorefront/api/v1/resource/usersubmissions/${this.component.submissionId}/comments/${this.currentComment.commentId}`
      }
      this.$http.put(editUrl, data)
        .then(response => {
          this.getComments(this.component)
          this.currentComment = ''
          this.publicComment = ''
          this.privateComment = ''
          this.editing = false
        })
        .catch(error => {
          this.$toasted.error('There was a problem editing your comment')
          console.error(error)
        })
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
  cursor: default;
}
.contact-comments {
  background-color: #B3E5FC;
  border-radius: 15px;
  padding: 0.5em;
  cursor: default;
}
.user-text-location {
  float: right;
  margin-top: 1em;
}
.right-text {
  margin: 0 !important;
  padding-right: 0.5em;
  text-align: right !important;
}
.left-text {
  margin: 0 !important;
  padding-left: 0.5em;
  text-align: left;
}
.background {
  background-color: #F5F5F5;
  height: 40vh;
  overflow-x: hidden;
  overflow-y: auto;
}
</style>

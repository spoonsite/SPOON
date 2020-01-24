<template>
  <div>
    <v-dialog :value="value" @input="close" width="40em">
      <v-card>
        <ModalTitle title="Workflow Comments" @close="close" />
        <v-card-text>
          <p class="">{{ component.name }}</p>
          <v-tabs
            v-model="tab"
            grow
          >
            <v-tabs-slider></v-tabs-slider>
            <v-tab href="#tab-1" >Public</v-tab>
            <v-tab href="#tab-2" v-if="permission">Private</v-tab>
          </v-tabs>
          <v-tabs-items v-model="tab">
            <v-tab-item
              v-for="i in 2"
              :key="i"
              :value="'tab-' + i"
            >
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
                    <div v-if="!comment.privateComment && i === 1">
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
                              <v-list-item @click="editing = true; newComment = comment.comment; currentComment = comment">
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

                    <div v-else-if="comment.privateComment && i === 2">
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
                              <v-list-item @click="editing = true; newComment=comment.comment; currentComment = comment">
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
                  v-model="newComment"
                ></quill-editor>
                <p class="ma-0 red--text" v-if="i===1">This comment will be sent to the vendor.</p>
                <v-btn v-if="i === 1 && !editing" @click="submitPublicComment()">Post Comment</v-btn>
                <v-btn v-else-if="i ===2 && !editing" @click="submitPrivateComment()">Post Comment</v-btn>
                <v-btn v-else-if="i===1 && editing" @click="editPublicComment()">Update Comment</v-btn>
                <v-btn v-else-if="i===2 && editing" @click="editPrivateComment()">Update Comment</v-btn>
              </v-card>
            </v-tab-item>
          </v-tabs-items>
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
      newComment: '',
      currentComment: '',
      deleteDialog: false,
      permission: false,
      isLoading: false,
      editing: false,
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
        .catch(e => this.errors.push(e))
      var perm = ['WORKFLOW-ADMIN-SUBMISSION-COMMENTS']
      this.permission = this.checkPermissions(perm)
    },
    submitPrivateComment() {
      this.isLoading = true
      let data = {
        comment: this.newComment,
        commentType: 'SUBMISSION',
        privateComment: true,
        willSendEmail: false
      }
      this.submitComment(data)
    },
    submitPublicComment() {
      this.isLoading = true
      let data = {
        comment: this.newComment,
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
          this.newComment = ''
        })
        .catch(e => this.$toasted.error('There was a problem submitting your comment.'))
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
          this.errors.push(error)
          this.isLoading = false
        })
    },
    editPublicComment() {
      this.isLoading = true
      let data = {
        comment: this.newComment,
        commentType: 'SUBMISSION',
        privateComment: false,
        willSendEmail: false
      }
      this.editComment(data)
    },
    editPrivateComment() {
      this.isLoading = true
      let data = {
        comment: this.newComment,
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
          this.newComment = ''
          this.editing = false
        })
        .catch(e => this.$toasted.error('There was a problem editing your comment.'))
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

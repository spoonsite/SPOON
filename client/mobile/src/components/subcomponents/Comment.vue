<template lang="html">

  <section class="comment-box">
    <div>
      <v-container pa-3>
        <div class="white elevation-4" style="overflow: auto;">
          <div v-bind:class="isOwner(comment.createUser)">
            <h3 style="height: 2em; vertical-align: middle; line-height: 2em;">{{ comment.createUser }}
              <span style="float: right;" v-if="$store.state.currentUser.username === comment.createUser">
                <v-btn small slot="activator" icon @click="deleteCommentDialog = true" dark><v-icon class="icon">delete</v-icon></v-btn>
                <v-btn small slot="activator" flat icon @click="openEditCommentDialog()" dark><v-icon class="icon">edit</v-icon></v-btn>
              </span>
            </h3>
          </div>

          <div class="pa-2">
            <strong>Comment date: </strong>
            <span>{{ comment.createDts | formatDate }}</span>

            <div class="pa-2" v-html="comment.comment">
            </div>
          </div>
        </div>
      </v-container>
    </div>

      <v-dialog
        v-model="deleteCommentDialog"
        max-width="300px"
        >
        <v-card>
          <v-card-text>
            <p>Are you sure you want to delete this comment?</p>
          </v-card-text>
          <v-card-actions>
            <v-btn color="warning" @click="deleteComment()"><v-icon>delete</v-icon> Delete</v-btn>
            <v-btn @click="deleteCommentDialog = false">Cancel</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

    <v-dialog
      v-model="editCommentDialog"
      >
      <v-card>
        <v-card-title>
          <h2 class="w-100">Edit a Comment</h2>
          <v-alert class="w-100" type="warning" :value="true"><span v-html="$store.state.branding.userInputWarning"></span></v-alert>
        </v-card-title>
        <v-card-text>
          <quill-editor
          style="background-color: white;"
          v-model="newComment"
          ></quill-editor>
        </v-card-text>
        <v-card-actions>
          <v-btn color="success" @click="editComment()">Submit</v-btn>
          <v-btn @click="editCommentDialog = false">Cancel</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <LoadingOverlay v-model="isLoading"></LoadingOverlay>
  </section>

</template>

<script lang="js">
import LoadingOverlay from './LoadingOverlay';

export default {
  name: 'Comment',
  props: ['comment'],
  components: {
    LoadingOverlay
  },
  mounted () {
  },
  data () {
    return {
      deleteCommentDialog: false,
      editCommentDialog: false,
      isLoading: false,
      newComment: ''
    };
  },
  methods: {
    deleteComment () {
      this.isLoading = true;
      this.$http.delete(`/openstorefront/api/v1/resource/components/${this.comment.componentId}/comments/${this.comment.commentId}`)
        .finally(() => {
          this.isLoading = false;
          this.$emit('dataChange');
          this.deleteCommentDialog = false;
        });
    },
    editComment () {
      this.isLoading = true;
      let commentEdit = {
        securityMarkingType: null,
        dataSensitivity: null,
        commentType: 'SUBMISSION', // ask about this.
        comment: this.newComment,
        parentCommentId: null,
        privateComment: null,
        adminComment: null
      };
      this.$http.put(`/openstorefront/api/v1/resource/components/${this.comment.componentId}/comments/${this.comment.commentId}`, commentEdit)
        .finally(() => {
          this.isLoading = false;
          this.$emit('dataChange');
          this.editCommentDialog = false;
        });
    },
    isOwner (createUser) {
      if (this.$store.state.currentUser.username === createUser) {
        return 'blue-grey darken-2 pa-2 white--text';
      }
      return 'grey lighten-2 pa-2';
    },
    openEditCommentDialog () {
      this.newComment = this.comment.comment;
      this.editCommentDialog = true;
    }
  },
  computed: {

  }
};
</script>

<style scoped lang="scss">
  .comments {

  }
</style>

<template lang="html">

  <section class="submission-comments-page">
    <v-card class="grey darken-3 white--text text-xs-center">
      <v-card-text>
        <h1 class="title">{{ componentName }}</h1>
      </v-card-text>
    </v-card>

    <v-container pa-2>
      <v-layout justify-end>
        <v-btn round class="primary" v-on:click="submitCommentDialog = true"><v-icon>fa-comments</v-icon>&nbsp; Add Comment</v-btn>
      </v-layout>
    </v-container>

    <v-divider></v-divider>

    <Comment
      v-if="comments.length > 0"
      v-for="comment in comments"
      :key="comment.commentId"
      :comment="comment"
      @dataChange="getComments"></Comment>

    <v-container v-if="comments.length === 0" text-xs-center py-2>
      <v-card-text>
        <h1>
          There are no comments on this submission.
        </h1>
      </v-card-text>
    </v-container>

    <v-dialog
      v-model="submitCommentDialog"
      >
      <v-card>
        <v-card-title>
          <h2 class="w-100">Create a Comment</h2>
          <v-alert class="w-100" type="warning" :value="true"><span v-html="$store.state.branding.userInputWarning"></span></v-alert>
        </v-card-title>
        <v-card-text>
          <quill-editor
          style="background-color: white;"
          v-model="newComment"
          ></quill-editor>
        </v-card-text>
        <v-card-actions>
          <v-btn color="success" @click="submitComment()">Submit</v-btn>
          <v-btn @click="submitCommentDialog = false">Cancel</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <LoadingOverlay v-model="isLoading"></LoadingOverlay>
  </section>

</template>

<script lang="js">
import router from '../router/index';
import Comment from './subcomponents/Comment';
import LoadingOverlay from './subcomponents/LoadingOverlay';

export default {
  name: 'submission-comments-page',
  components: {
    Comment,
    router,
    LoadingOverlay
  },
  props: [],
  mounted () {
    this.isLoading = true;
    if (this.$route.params.id) {
      this.id = this.$route.params.id;
    }

    this.getComments();

    this.$http.get(`/openstorefront/api/v1/resource/components/${this.id}`)
      .then(response => {
        if (response.data.name) {
          this.componentName = response.data.name;
        }
      })
      .finally(() => {
        this.isLoading = false;
      });
  },
  data () {
    return {
      id: '',
      isLoading: false,
      componentName: '',
      comments: {},
      deleteCommentDialog: false,
      submitCommentDialog: false,
      newComment: ''
    };
  },
  methods: {
    getComments () {
      this.$http.get(`/openstorefront/api/v1/resource/components/${this.id}/comments?submissionOnly=true`)
        .then(response => {
          if (response.data) {
            this.comments = response.data;
          }
        });
    },
    isOwner (createUser) {
      if (this.$store.state.currentUser.username === createUser) {
        return 'cyan lighten-2 pa-2';
      }
      return 'grey lighten-2 pa-2';
    },
    returnToEntry () {
      router.push({
        name: 'Entry Detail',
        params: {
          id: this.id
        }
      });
    },
    submitComment () {
      this.isLoading = true;
      let commentSubmission = {
        securityMarkingType: null,
        dataSensitivity: null,
        commentType: 'SUBMISSION', // ask about this.
        comment: this.newComment,
        parentCommentId: null,
        privateComment: null,
        adminComment: null
      };

      this.$http.post(`/openstorefront/api/v1/resource/components/${this.id}/comments`, commentSubmission)
        .finally(() => {
          this.submitCommentDialog = false;
          this.getComments();
          this.isLoading = false;
          this.newComment = '';
        });
    }
  },
  computed: {

  }
};
</script>

<style scoped lang="scss">
  .submission-comments-page {

  }
</style>

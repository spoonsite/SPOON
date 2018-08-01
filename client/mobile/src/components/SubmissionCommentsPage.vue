<template lang="html">

  <section class="submission-comments-page">
    <v-container text-xs-center>
      <v-btn class="primary" v-on:click="returnToEntry()">Return To Entry</v-btn>
      <v-btn class="primary" v-on:click="editCommentDialog = true">Add Comment</v-btn>
    </v-container>

    <v-divider></v-divider>

    <Comment
      v-for="comment in comments"
      :key="comment.commentId"
      :comment="comment"></Comment>

    <v-dialog
      v-model="submitCommentDialog"
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
          <v-btn color="success" @click="submitComment()">Submit</v-btn>
          <v-btn @click="submitCommentDialog = false">Cancel</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

  </section>

</template>

<script lang="js">
import router from '../router/index';
import Comment from './subcomponents/Comment';

export default {
  name: 'submission-comments-page',
  components: {
    Comment,
    router
  },
  props: [],
  mounted () {
    if (this.$route.params.id) {
      this.id = this.$route.params.id;
    }

    this.$http.get('http://localhost:3005/submission')
      .then(response => {
        if (response.data) {
          this.comments = response.data;
        }
      });
  },
  data () {
    return {
      id: '',
      comments: {},
      deleteCommentDialog: false,
      submitCommentDialog: false,
      newComment: ''
    };
  },
  methods: {
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

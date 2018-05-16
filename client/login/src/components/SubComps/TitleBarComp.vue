<template>
  <div>
    <v-toolbar dense flat dark color="primary">
      <v-spacer></v-spacer>
      <v-toolbar-title >{{CTitle}}</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn icon id="menubutton">
        <v-icon>menu</v-icon>
      </v-btn>
    </v-toolbar>
    <hr>
    <v-toolbar dense dark color="secondary">
      <v-spacer></v-spacer>
      <v-toolbar-title id="subtitle">{{CSubtitle}}</v-toolbar-title>
      <v-spacer></v-spacer>
    </v-toolbar>
  </div>
</template>

<script>
export default {
  name: "TitleBarComp",
  props: ["CTitle", "CSubtitle"]
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
hr {
  background: goldenrod;
  height: 7px;
}
#subtitle {
  font-size: 1em;
  font-style: italic;
}
#menubutton {
  color: white;
}
</style>

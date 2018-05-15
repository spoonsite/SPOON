<template>
  <div>
    <v-toolbar dense flat id="topbar">
      <v-spacer></v-spacer>
      <v-toolbar-title>{{CTitle}}</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn icon id="menubutton">
        <v-icon>menu</v-icon>
      </v-btn>
    </v-toolbar>
    <hr>
    <v-toolbar dense id="subbar">
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
#topbar {
  background-color: #252931;
  color: white;
}
#subbar {
  background-color: #303540;
  color: white;
}
#subtitle {
  font-size: 1em;
  font-style: italic;
}
#menubutton {
  color: white;
}
</style>

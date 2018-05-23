<template>
  <div>
    <div class="topbar">
      <v-toolbar dense flat dark color="primary">
        <v-spacer></v-spacer>
        <v-toolbar-title >{{CTitle}}</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-toolbar-side-icon></v-toolbar-side-icon>
      </v-toolbar>
    </div>
    <div class="subtitle dark secondary">
      {{ CSubtitle }}
    </div>
  </div>
</template>

<script>
export default {
  name: 'TitleBarComp',
  props: ['CTitle', 'CSubtitle']
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.topbar {
  border-bottom: 4px solid gold;
}
.subtitle {
  text-align: center;
  color: white;
  padding: 0.8em 0;
  font-size: 1em;
  font-style: italic;
}
</style>

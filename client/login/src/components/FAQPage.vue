<template>
<div class="wrapper">
  <h2>Frequently Asked Questions</h2>
  <v-expansion-panel>
    <v-expansion-panel-content v-for="(item,i) in 5" :key="i">
    <div slot="header">Item</div>
    <v-card>
      <v-card-text>Lorem ipsum</v-card-text>
    </v-card>
    </v-expansion-panel-content>
  </v-expansion-panel>
</div>
</template>

<script>
</script>

<style>
.wrapper {
  max-width: 45em;
  padding: 1em;
  margin-right: auto;
  margin-left: auto;
}
</style>

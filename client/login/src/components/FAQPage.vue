<template>
<div class="wrapper">
  <h2>Frequently Asked Questions</h2>

<v-layout mt-3>
    <v-flex xs12>
      <v-expansion-panel focusable>
        <v-expansion-panel-content v-for="(item,i) in 5" :key="i">
        <div slot="header" class="title">Item</div>
        <v-card>
          <v-card-text class="grey lighten-3">Lorem ipsum</v-card-text>
        </v-card>
        </v-expansion-panel-content>
      </v-expansion-panel>
    </v-flex>
  </v-layout>
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

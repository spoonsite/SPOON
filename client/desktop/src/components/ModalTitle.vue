<template>
  <v-card-title>
    <div class="space-between">
      <h2>{{ title }}</h2>
      <v-btn icon @click="$emit('close')" class="close-x">
        <v-icon small>
          fas fa-times
        </v-icon>
      </v-btn>
    </div>
  </v-card-title>
</template>

<script>
export default {
  name: 'ModalTitle',
  props: ['title']
}
</script>

<style scoped>
.space-between {
  word-wrap: normal;
  width: 100%;
  display: inline-flex;
  justify-content: space-between;
}
.close-x {
  margin: -10px -10px 0 0;
}
</style>

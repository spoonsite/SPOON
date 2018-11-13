<template>
  <div v-if="value" class="overlay">
    <v-progress-circular
      color="primary"
      :size="60"
      :width="6"
      indeterminate
      class="center"
    ></v-progress-circular>
  </div>
</template>

<script>
export default {
  name: 'LoadingOverlay',
  props: ['value'],
  mounted () {
  },
  data () {
    return {
    }
  },
  methods: {
  },
  computed: {
  },
  watch: {
  }
}
</script>

<style>
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 15;
  background-color: rgba(255,255,255, 0.7);
  pointer-events: all;
}
.center {
  z-index: 991;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
</style>

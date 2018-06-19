<template>
<div>
    <!-- MEDIA carousel -->
    <div
      class="mediaWrapper  grey lighten-2"
      >
      <div class="mediaItem">
        <img
          v-for="(item, index) in lightboxList"
          :key="item.link"
          :src="baseURL+item.link"
          class="mediaImage elevation-4"
          @click="lightboxOn(index)"
        >
      </div>
    </div>

    <!-- LIGHTBOX Popup -->
    <div v-if="lightbox" class="lightbox">
      <div class="lightboxImageWrapper">
        <img :src="lightboxCurrentImage" class="lightboxImage elevation-3">
      </div>

      <div class="lightboxControl">
        <v-btn v-if="lightboxList.length > 1" flat icon small color="yellow" @click="lightboxPrev()">
          <v-icon dark>navigate_before</v-icon>
        </v-btn>

        <v-btn flat icon small color="yellow" style="margin-left: 2em; margin-right: 2em;" @click="lightbox = false;">
          <v-icon dark>clear</v-icon>
        </v-btn>

        <v-btn v-if="lightboxList.length > 1" flat icon small color="yellow" @click="lightboxNext()">
          <v-icon dark>navigate_next</v-icon>
        </v-btn>
      </div>
    </div>
</div>
</template>

<script>
export default {
  name: 'Lightbox',
  props: ['lightboxList'],
  mounted () {
  },
  data () {
    return {
      baseURL: '/openstorefront/',
      lightbox: false,
      lightboxCurrentImage: '',
      lightboxCurrentIndex: 0
    };
  },
  methods: {
    lightboxOn (index) {
      this.lightbox = true;
      this.lightboxCurrentIndex = index;
      this.lightboxCurrentImage = this.baseURL + this.lightboxList[index].link;
      this.lightboxSetImage();
    },
    lightboxSetImage () {
      this.lightboxCurrentImage = this.baseURL + this.lightboxList[this.lightboxCurrentIndex].link;
    },
    lightboxNext () {
      this.lightboxCurrentIndex = this.lightboxList.length % (this.lightboxCurrentIndex + 1);
      this.lightboxSetImage();
    },
    lightboxPrev () {
      this.lightboxCurrentIndex = this.lightboxList.length > 0 ? this.lightboxCurrentIndex - 1 : this.lightboxList.length;
      this.lightboxSetImage();
    }
  },
  computed: {
  },
  watch: {
  }
};
</script>

<style>
  .lightbox {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 990;
    background-color: rgba(71, 71, 71, 0.7);
    pointer-events: all;
  }
  .lightboxControl {
    bottom: 0%;
    position: fixed;
    width: 100%;
    z-index: 991;
    text-align: center;
    background-color: rgba(39, 39, 39, 0.7);
  }
  .lightboxImage {
    background: #fff;
    max-width: 98%;
    max-height: 90%;
  }
  .lightboxImageWrapper {
    margin-top: 1em;
    width: 100%;
    max-height: 90%;
    position: absolute;
    top: 45%;
    left: 50%;
    transform: translate(-50%, -50%);
    text-align: center;
  }
  .mediaWrapper {
    overflow-x: scroll;
    overflow-y: hidden;
    white-space: nowrap;
    margin-bottom: 1em;
  }
  .mediaItem{
    float: left;
    display: block;
  }
  .mediaImage {
    background: #fff;
    margin: 1em;
    max-height: 8em;
  }
</style>

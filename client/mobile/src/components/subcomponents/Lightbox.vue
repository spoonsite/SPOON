<template>
<div>
    <!-- MEDIA carousel -->
    <div
      class="mediaWrapper"
      >
      <div class="mediaItem">
        <img
          v-if="item.mediaTypeCode === 'IMG'"
          v-for="(item, index) in list"
          :key="item.link"
          :src="item.link"
          class="mediaImage elevation-4"
          @click="lightboxOn(index)"
        >
        <video
          v-else-if="item.mediaTypeCode === 'VID'"
          :src="item.link"
          class="mediaImage elevation-4"
          @click="lightboxOn(index)"
          style="display: inline-block;"
        ></video>
      </div>
    </div>

    <!-- LIGHTBOX Popup -->
    <transition name="fade">
    <div
      v-if="lightbox"
      class="lightbox"
        v-touch="{
          left: () => lightboxNext(),
          right: () => lightboxPrev()
        }"
      >

      <div class="lightboxImageWrapper">
        <transition name="swipe" keep-alive mode="out-in">
          <div :key="currentItem.link" style="margin-bottom: 3em;">
            <p style="color: white;">{{ currentItem.caption}}</p>
            <img v-if="currentItem.mediaTypeCode === 'IMG'" :src="currentItem.link" class="lightboxImage elevation-6">
            <video controls v-else-if="currentItem.mediaTypeCode === 'VID'" :src="currentItem.link" class="lightboxImage elevation-6"></video>
            <p style="color: white;">
              Image {{ currentIndex + 1 }} of {{ list.length }}
              <v-btn dark small flat icon :href="currentItem.link"><v-icon class="download-icon">fas fa-download</v-icon></v-btn>
            </p>
          </div>
        </transition>
      </div>

      <div class="lightboxControl">
        <v-btn v-if="list.length > 1" flat icon dark @click="lightboxPrev()">
          <v-icon dark>navigate_before</v-icon>
        </v-btn>
        <v-btn icon dark style="margin-left: 2em; margin-right: 2em;" @click="lightbox = false;">
          <v-icon dark>clear</v-icon>
        </v-btn>
        <v-btn v-if="list.length > 1" flat icon dark @click="lightboxNext()">
          <v-icon dark>navigate_next</v-icon>
        </v-btn>
      </div>

    </div>
    </transition>
</div>
</template>

<script>
export default {
  name: 'Lightbox',
  props: ['list'],
  mounted () {
  },
  data () {
    return {
      baseURL: '/openstorefront/',
      lightbox: false,
      currentItem: null,
      currentIndex: 0
    };
  },
  methods: {
    lightboxOn (index) {
      this.lightbox = true;
      this.currentIndex = index;
      this.currentItem = this.list[index];
      this.lightboxSetImage();
    },
    lightboxSetImage () {
      this.currentItem = this.list[this.currentIndex];
    },
    lightboxNext () {
      this.currentIndex = (this.currentIndex + 1) % this.list.length;
      this.lightboxSetImage();
    },
    lightboxPrev () {
      this.currentIndex = this.currentIndex - 1;
      if (this.currentIndex < 0) {
        this.currentIndex = this.list.length - 1;
      }
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
    overflow-y: scroll;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 990;
    background-color: rgba(71, 71, 71, 0.8);
    pointer-events: all;
  }
  .lightboxControl {
    bottom: 0%;
    position: fixed;
    width: 100%;
    z-index: 999;
    text-align: center;
    background-color: rgba(39, 39, 39, 0.7);
  }
  .lightboxImage {
    max-width: 90%;
    max-height: 90%;
    background-color: white;
  }
  .lightboxImageWrapper {
    margin-top: 1em;
    width: 100%;
    max-height: 90%;
    position: absolute;
    top: 55%;
    left: 50%;
    transform: translate(-50%, -50%);
    text-align: center;
  }
  .mediaWrapper {
    overflow-x: auto;
    white-space: nowrap;
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
  /* transition animations */
  .fade-enter-active, .fade-leave-active {
  transition: opacity .5s;
  }
  .fade-enter, .fade-leave-to{
    opacity: 0;
  }
  .swipe-enter-active, .swipe-leave-active {
    transition: opacity .1s;
  }
  .swipe-enter {
    opacity: 0;
  }
  .swipe-leave-to {
    opacity: 0;
  }
  .download-icon {
    font-size: 14px;
  }
</style>

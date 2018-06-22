<template>
<div>
    <!-- MEDIA carousel -->
    <div
      class="mediaWrapper"
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
    <transition name="fade">
    <div
      v-if="lightbox"
      class="lightbox"
        v-touch="{
          left: () => lightboxNext(),
          right: () => lightboxPrev()
        }"
      >

      <v-btn icon dark large class="close-btn" @click="lightbox = false;"><v-icon dark large>clear</v-icon></v-btn>

      <v-btn class="left-btn" v-if="lightboxList.length > 1" flat icon dark @click="lightboxPrev()">
        <v-icon large dark>navigate_before</v-icon>
      </v-btn>

      <v-btn class="right-btn" v-if="lightboxList.length > 1" flat icon dark @click="lightboxNext()">
        <v-icon large dark>navigate_next</v-icon>
      </v-btn>

      <div class="lightboxImageWrapper">
        <transition name="swipe" keep-alive mode="out-in">
          <div :key="lightboxCurrentImage">
            <img :src="lightboxCurrentImage" class="lightboxImage elevation-6">
            <p style="color: white;">Image {{ lightboxCurrentIndex + 1 }} of {{ lightboxList.length }} <v-btn dark small flat icon><v-icon class="download-icon">fas fa-download</v-icon></v-btn></p>
          </div>
        </transition>
      </div>

      <!-- <div class="lightboxControl">
        <v-btn v-if="lightboxList.length > 1" flat icon small dark @click="lightboxPrev()">
          <v-icon dark>navigate_before</v-icon>
        </v-btn>

        <v-btn icon small dark style="margin-left: 2em; margin-right: 2em;" @click="lightbox = false;">
          <v-icon dark>clear</v-icon>
        </v-btn>

        <v-btn v-if="lightboxList.length > 1" flat icon small dark @click="lightboxNext()">
          <v-icon dark>navigate_next</v-icon>
        </v-btn>
      </div> -->

    </div>
    </transition>
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
      this.lightboxCurrentIndex = (this.lightboxCurrentIndex + 1) % this.lightboxList.length;
      this.lightboxSetImage();
    },
    lightboxPrev () {
      this.lightboxCurrentIndex = this.lightboxCurrentIndex - 1;
      if (this.lightboxCurrentIndex < 0) {
        this.lightboxCurrentIndex = this.lightboxList.length - 1;
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
    z-index: 991;
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
  .left-btn {
    position: absolute;
    top: 50%;
    left: 0;
    z-index: 991;
  }
  .right-btn {
    position: absolute;
    top: 50%;
    right: 0;
    z-index: 991;
  }
  .close-btn {
    position: absolute;
    top: 0;
    right: 0;
    z-index: 991;

  }
</style>

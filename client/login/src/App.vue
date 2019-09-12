<template>
  <div id="app">
    <v-app>
      <title-bar-comp :CTitle="title" :CSubtitle="subTitle"/>
      <router-view :style="'padding-bottom: ' + footer + 'px;'"/>
      <div id="particle-js"></div>
      <v-footer absolute color="primary" dark height="auto" id="footer">
        <v-card color="primary" dark flat class="footer-wrapper">
          <div v-html="$store.state.branding.loginFooter"></div>
        </v-card>
      </v-footer>
    </v-app>
  </div>
</template>

<script>
import TitleBarComp from './components/SubComps/TitleBarComp';

export default {
  name: 'App',
  components: {
    TitleBarComp
  },
  data() {
    return {
      footerHeight: undefined,
    }
  },
  mounted () {
    /* eslint-disable no-undef */
    // particleJS loaded from CDN, npm loading fails
    let particles = true;
    if (particles) {
      particlesJS('particle-js', {
        particles: {
          number: { value: 100, density: { enable: true, value_area: 1500 } },
          color: { value: '#000000' },
          shape: {
            type: 'circle',
            stroke: { width: 0, color: '#000000' },
            polygon: { nb_sides: 5 },
            image: { src: 'img/github.svg', width: 100, height: 100 }
          },
          opacity: {
            value: 0.5,
            random: false,
            anim: { enable: false, speed: 1, opacity_min: 0.1, sync: false }
          },
          size: {
            value: 3,
            random: true,
            anim: { enable: false, speed: 40, size_min: 0.1, sync: false }
          },
          line_linked: {
            enable: true,
            distance: 150,
            color: '#000000',
            opacity: 0.10259081259815171,
            width: 1
          },
          move: {
            enable: true,
            speed: 2,
            direction: 'none',
            random: false,
            straight: false,
            out_mode: 'out',
            bounce: false,
            attract: { enable: true, rotateX: 600, rotateY: 1200 }
          }
        },
        interactivity: {
          detect_on: 'canvas',
          events: {
            onhover: { enable: true, mode: 'repulse' },
            onclick: { enable: true, mode: 'push' },
            resize: true
          },
          modes: {
            grab: { distance: 400, line_linked: { opacity: 1 } },
            bubble: { distance: 400, size: 40, duration: 2, opacity: 8, speed: 3 },
            repulse: { distance: 200, duration: 0.4 },
            push: { particles_nb: 4 },
            remove: { particles_nb: 2 }
          }
        },
        retina_detect: true
      });
    }
    this.footerHeight = document.getElementById("footer").clientHeight;
  },
  computed: {
    title () {
      return this.$store.state.branding.applicationName;
    },
    subTitle () {
      return this.$store.state.branding.landingPageBanner;
    },
    footer () {
      return this.footerHeight +175;
    }
  }
};
</script>

<style>
#app {
  font-family: "Roboto", "Franklin Gothic Medium", "Arial Narrow", Arial,
    sans-serif;
}
#app, .application--wrap, .wrapper {
  background-color: rgba(255,255,255,0);
}
.footer-wrapper {
  width: 100%;
  margin-left: auto;
  margin-right: auto;
}
.logo-img {
  margin: 0.5em;
  padding: 0.5em;
}
canvas {
  display: block;
}
/* ---- particles.js container ---- */
#particle-js {
  z-index: -1;
  position: absolute;
  top: 0; /* IE11 */
  width: 100%;
  height: 100%;
  background-color: #fafafa;
  background-image: url("");
  background-repeat: no-repeat;
  background-size: cover;
  background-position: 50% 50%;
}
</style>

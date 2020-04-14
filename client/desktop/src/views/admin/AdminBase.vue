<template>
  <div>
    <v-navigation-drawer
      v-model="drawer"
      :mini-variant.sync="mini"
      permanent
      app
      clipped
      v-click-outside="onClickOutside"
    >
      <v-list-item class="px-2">
        <v-btn icon @click.stop="mini = !mini">
          <v-icon>mdi-chevron-{{ mini ? 'right' : 'left' }}</v-icon>
        </v-btn>
      </v-list-item>

      <v-divider></v-divider>

      <v-list dense>
        <v-list-item>
          <v-list-item-icon>
            <v-icon>mdi-home-city</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>Home</v-list-item-title>
          </v-list-item-content>
        </v-list-item>

        <v-list-item link>
          <v-list-item-icon>
            <v-icon>mdi-account</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>My Account</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
    <router-view class="ml-5" />
  </div>
</template>

<script lang="js">
export default {
  name: 'AdminBase',
  mounted() {},
  data() {
    return {
      drawer: false,
      items: [
        { title: 'Home', icon: 'mdi-home-city' },
        { title: 'My Account', icon: 'mdi-account' },
        { title: 'Users', icon: 'mdi-account-group-outline' }
      ],
      mini: true
    }
  },
  methods: {
    onClickOutside() {
      if (!this.mini) {
        this.mini = true
      }
    }
  }
}
</script>

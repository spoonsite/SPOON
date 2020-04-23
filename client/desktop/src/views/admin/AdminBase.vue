<template>
  <div>
    <v-navigation-drawer
      v-model="drawer"
      :mini-variant.sync="mini"
      permanent
      app
      clipped
      width="300"
      v-click-outside="outsideNavBar"
    >
      <v-list-item class="px-2">
        <v-btn icon @click.stop="toggleNavBar">
          <v-icon>mdi-chevron-{{ mini ? 'right' : 'left' }}</v-icon>
        </v-btn>
      </v-list-item>

      <v-divider></v-divider>

      <v-list dense>
        <v-list-group
          v-for="item in navBarLinks"
          :key="item.topLevel"
          :prepend-icon="item.icon"
          v-model="item.showChildren"
        >
          <template v-slot:activator>
            <v-list-item-title>{{ item.topLevel }}</v-list-item-title>
          </template>
          <v-list-item v-for="child in item.children" :key="child.title" link :to="child.link" class="pl-8">
            <v-list-item-icon>
              <v-icon>{{ child.icon }}</v-icon>
            </v-list-item-icon>

            <v-list-item-content>
              <v-list-item-title>{{ child.title }}</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list-group>
      </v-list>
    </v-navigation-drawer>
    <router-view class="ml-5" />
  </div>
</template>

<script lang="js">
export default {
  name: 'AdminBase',
  mounted() {
    this.navBarLinks.forEach(link => {
      link.children.filter(child => this.$store.state.permissionMap.includes(child.permission))
    })
    this.navBarLinks.filter(link => link.children.length > 0)
  },
  data() {
    return {
      drawer: false,
      navBarLinks: [
        {
          topLevel: 'Data Managment',
          icon: 'mdi-database',
          showChildren: false,
          children: [
            { title: 'Attributes', link: { name: 'AdminAttributes' }, permission: 'ADMIN-ATTRIBUTE-PAGE' },
            { title: 'Entries', link: { name: 'AdminEntries' }, permission: 'ADMIN-ENTRIES-PAGE' },
            { title: 'Entry Types', link: { name: 'AdminEntryTypes' }, permission: 'ADMIN-ENTRYTYPE-PAGE' },
            { title: 'Tags', link: { name: 'AdminTags' }, permission: 'ADMIN-TAGS-PAGE' },
            { title: 'Lookups', link: { name: 'AdminLookups' }, permission: 'ADMIN-LOOKUPS-PAGE' }
          ]
        },
        {
          topLevel: 'Site Branding',
          icon: 'mdi-tag',
          showChildren: false,
          children: [
            { title: 'Branding', link: { name: 'AdminDashboard' }, permission: 'ADMIN-BRANDING-PAGE' },
            { title: 'Media', link: { name: 'AdminDashboard' }, permission: 'ADMIN-SUPPORTMEDIA-PAGE' },
            { title: 'Highlights', link: { name: 'AdminDashboard' }, permission: 'ADMIN-HIGHLIGHTS-PAGE' },
            { title: 'FAQ', link: { name: 'AdminDashboard' }, permission: 'ADMIN-FAQ-PAGE' }
          ]
        },
        {
          topLevel: 'User Data',
          icon: 'mdi-account',
          showChildren: false,
          children: [
            { title: 'Questions and Answers', link: { name: 'AdminDashboard' }, permission: 'ADMIN-QUESTIONS-PAGE' },
            { title: 'Reviews', link: { name: 'AdminDashboard' }, permission: 'ADMIN-REVIEWS-PAGE' },
            { title: 'Watches', link: { name: 'AdminDashboard' }, permission: 'ADMIN-WATCHES-PAGE' },
            { title: 'Permissions/Roles', link: { name: 'AdminDashboard' }, permission: 'ADMIN-SECURITY-PAGE' },
            { title: 'Organizations', link: { name: 'AdminDashboard' }, permission: 'ADMIN-ORGANIZATION-PAGE' },
            { title: 'Tracking', link: { name: 'AdminDashboard' }, permission: 'ADMIN-TRACKING-PAGE' },
            { title: 'Contacts', link: { name: 'AdminDashboard' }, permission: 'ADMIN-CONTACTS-PAGE' },
            { title: 'User Profiles', link: { name: 'AdminDashboard' }, permission: 'ADMIN-USERPROFILES-PAGE' },
            { title: 'User Management', link: { name: 'AdminDashboard' }, permission: 'ADMIN-USER-MANAGEMENT-PAGE' }
          ]
        },
        {
          topLevel: 'System',
          icon: 'mdi-console',
          showChildren: false,
          children: [
            { title: 'System Status', link: { name: 'AdminDashboard' }, permission: 'ADMIN-SYSTEM-MANAGEMENT-STATUS' },
            { title: 'Application State Properties', link: { name: 'AdminDashboard' }, permission: 'ADMIN-SYSTEM-MANAGEMENT-APP-PROP' },
            { title: 'System Configuration', link: { name: 'AdminDashboard' }, permission: 'ADMIN-SYSTEM-MANAGEMENT-CONFIG-PROP-UPDATE' },
            { title: 'Logs', link: { name: 'AdminDashboard' }, permission: 'ADMIN-SYSTEM-MANAGEMENT-LOGGING' },
            { title: 'Plugins', link: { name: 'AdminDashboard' }, permission: 'ADMIN-SYSTEM-MANAGEMENT-PLUGIN' },
            { title: 'Managers', link: { name: 'AdminDashboard' }, permission: 'ADMIN-SYSTEM-MANAGEMENT-MANAGERS' },
            { title: 'Cache', link: { name: 'AdminDashboard' }, permission: 'ADMIN-SYSTEM-MANAGEMENT-CACHE' },
            { title: 'Search Management', link: { name: 'AdminDashboard' }, permission: 'ADMIN-SYSTEM-MANAGMENT-SEARCH-CONTROL' },
            { title: 'Recent Changes Email', link: { name: 'AdminDashboard' }, permission: 'ADMIN-SYSTEM-MANAGEMENT-RECENT-CHANGES' },
            { title: 'Jobs', link: { name: 'AdminDashboard' }, permission: 'ADMIN-JOBS-PAGE' },
            { title: 'Error Tickets', link: { name: 'AdminDashboard' }, permission: 'ADMIN-SYSTEM-MANAGEMENT-ERROR-TICKET' }
          ]
        },
        {
          topLevel: 'App Management',
          icon: 'mdi-cogs',
          showChildren: false,
          children: [
            { title: 'Alerts', link: { name: 'AdminDashboard' }, permission: 'ADMIN-ALERTS-PAGE' },
            { title: 'Feedback', link: { name: 'AdminDashboard' }, permission: 'ADMIN-FEEDBACK-PAGE' },
            { title: 'Messages', link: { name: 'AdminDashboard' }, permission: 'ADMIN-MESSAGES-PAGE' },
            { title: 'Reports', link: { name: 'AdminDashboard' }, permission: 'REPORTS-PAGE' }
          ]
        },
        {
          topLevel: 'Workplan Management',
          icon: 'mdi-folder',
          showChildren: false,
          children: [
            { title: 'Workplan Progress', link: { name: 'AdminDashboard' }, permission: 'WORKPLAN-PROGRESS-MANAGEMENT-PAGE' },
            { title: 'Workplan Management', link: { name: 'AdminDashboard' }, permission: 'ADMIN-WORKPLAN-PAGE' }
          ]
        }
      ],
      mini: true
    }
  },
  methods: {
    toggleNavBar() {
      this.mini = !this.mini
      if (this.mini) {
        this.navBarLinks.forEach(e => {
          e.showChildren = false
        })
      }
    },
    outsideNavBar() {
      if (!this.mini) {
        this.mini = true
        this.navBarLinks.forEach(e => {
          e.showChildren = false
        })
      }
    }
  }
}
</script>

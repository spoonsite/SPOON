<template>
  <div>
    <h3 class="heading">Select Attribute Range</h3>
    <v-text-field
      label="Low"
      style="margin-right: 0.5em; width: 6em; display: inline-block;"
      v-model="min"
      :rules="[validNumber]"
    ></v-text-field>
    <span style="margin-right: 0.5em;">
      to
    </span>
    <v-text-field
      label="High"
      v-model="max"
      style="margin-right: 0.5em; width: 6em; display: inline-block;"
      :rules="[validNumber]"
    ></v-text-field>
    <!-- <v-combobox
      :items="['kg', 'mg', 'μg', 'g', 'lbs', 'oz']"
      label="Unit"
      value='kg'
      style="width: 4em; display: inline-block;"
    >
    </v-combobox> -->
  </div>
</template>

<script>
export default {
  name: 'AttributeRange',
  methods: {
    validNumber(v) {
      return !isNaN(v) || 'Must be a valid number'
    }
  },
  data() {
    return {
      min: null,
      max: null
    }
  }
}
</script>

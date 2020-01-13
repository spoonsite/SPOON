<template>
  <div>
    <h1 class="text-center">Submission Form</h1>
    <div class="text-center" style="color: red;">
      <h2>Caution!</h2>
      <p>{{ $store.state.branding.userInputWarning }}</p>
    </div>
    <v-form v-model="isFormValid" ref="submissionForm" style="width: 80%;" class="mx-auto">
      <fieldset>
        <legend class="title">Entry Details*</legend>
        <v-text-field
          label="Entry Title*"
          v-model="entryTitle"
          required
          :rules="[rules.required]"
          class="mx-4"
          autofocus
        />
        <v-autocomplete
          label="Entry Type*"
          v-model="entryType"
          :items="this.$store.state.componentTypeList"
          item-text="parentLabel"
          required
          :rules="[rules.required]"
          class="mx-4"
        />
        <v-autocomplete
          label="Organization*"
          v-model="organization"
          :items="organizationList"
          item-text="name"
          item-value="name"
          required
          :rules="[rules.required]"
          class="mx-4"
        />
        <v-select
          label="Security Marking*"
          v-model="securityMarking"
          :items="securityMarkingList"
          item-text="description"
          item-value="code"
          required
          :rules="[rules.required]"
          class="mx-4"
        />
      </fieldset>
      <fieldset>
        <legend class="title">Primary Point of Contact*</legend>
        <v-text-field label="First Name*" v-model="firstName" required :rules="[rules.required]" class="mx-4" />
        <v-text-field label="Last Name*" v-model="lastName" required :rules="[rules.required]" class="mx-4" />
        <v-text-field label="Email*" v-model="email" required :rules="[rules.required]" class="mx-4" />
        <v-text-field label="Phone*" v-model="phone" required :rules="[rules.required]" class="mx-4" />
      </fieldset>
      <fieldset>
        <legend class="title">Image Upload</legend>
        <v-row v-for="(item, index) in images" :key="index" class="mx-4">
          <v-col cols="12" md="2">
            <v-img :src="item.img" alt="No preview available" max-height="90px" max-width="120px" contain />
          </v-col>
          <v-col cols="12" md="4">
            <v-file-input
              v-model="item.file"
              label="Upload Image*"
              required
              :rules="[rules.required]"
              @change="imageChange(index)"
            />
          </v-col>
          <v-col cols="12" md="5">
            <v-text-field v-model="item.caption" label="Image Caption*" required :rules="[rules.required]" />
          </v-col>
          <v-col cols="12" md="1">
            <v-btn class="mt-3" icon @click="removeImage(index)"><v-icon>mdi-delete</v-icon></v-btn>
          </v-col>
        </v-row>
        <v-btn color="grey lighten-2" class="ma-4" @click="addImage"><v-icon left>mdi-plus</v-icon>Add image</v-btn>
      </fieldset>
      <fieldset style="height: 500px">
        <legend class="title">Description*</legend>
        <quill-editor style="height:400px;" class="ma-2" v-model="description" />
      </fieldset>
      <fieldset>
        <legend class="title">Attributes</legend>
        <div class="mx-4 mt-4">
          <div v-if="attributes.required.length === 0">
            No required attributes available, please select an entry type
          </div>
          <div v-else>
            <h2>Required Attributes:</h2>
            <div v-for="attribute in attributes.required" :key="attribute.attributeType" class="mx-2">
              <p style="word-wrap: wrap" class="pt-5 pr-4">{{ attribute.description }}</p>
              <v-text-field
                v-if="attribute.allowUserGeneratedCodes && attribute.attributeValueType === 'TEXT'"
                v-model="attribute.selectedCodes"
                label="Value"
              />
              <v-text-field
                v-else-if="attribute.allowUserGeneratedCodes && attribute.attributeValueType === 'NUMBER'"
                v-model="attribute.selectedCodes"
                label="Value"
                :rules="[rules.numberOnly]"
              />
              <v-select v-else label="Value" clearable :items="attribute.codes" item-text="label" item-code="code" />
              <v-select
                label="Unit"
                v-if="attribute.attributeValueType === 'NUMBER' && attribute.attributeUnit !== ''"
                :value="attribute.attributeUnit"
                :items="attribute.attributeUnitList"
                item-text="unit"
                item-value="unit"
              />
            </div>
            <br />
          </div>
          <div v-if="attributes.suggested.length === 0">
            No required attributes available, please select an entry type
          </div>
          <div v-else>
            <h2>Suggested Attributes:</h2>
            <div v-for="attribute in attributes.suggested" :key="attribute.attributeType" class="mx-2">
              <p style="word-wrap: wrap" class="pt-5 pr-4">{{ attribute.description }}</p>
              <v-text-field
                v-if="attribute.allowUserGeneratedCodes && attribute.attributeValueType === 'TEXT'"
                v-model="attribute.selectedCodes"
                label="Value"
              />
              <v-text-field
                v-else-if="attribute.allowUserGeneratedCodes && attribute.attributeValueType === 'NUMBER'"
                v-model="attribute.selectedCodes"
                label="Value"
                :rules="[rules.numberOnly]"
              />
              <v-select v-else label="Value" clearable :items="attribute.codes" item-text="label" item-code="code" />
              <v-select
                label="Unit"
                v-if="attribute.attributeValueType === 'NUMBER' && attribute.attributeUnit !== ''"
                :value="attribute.attributeUnit"
                :items="attribute.attributeUnitList"
                item-text="unit"
                item-value="unit"
              />
            </div>
            <br />
          </div>
        </div>
        <div class="mx-4 mt-4">
          <strong>Request New Attribute</strong>
          <p class="mb-0">Please describe the attribute you would like to have added.</p>
          <p class="mb-0">
            Include the value for your entry, a brief description, and how your part is defined by the attribute.
          </p>
          <v-textarea label="Request New Attribute (opt.)" v-model="attributes.missingAttribute" />
        </div>
      </fieldset>
      <fieldset>
        <legend class="title">Resources</legend>
        <div class="pa-4">
          <p>Local Files</p>
          <div v-for="(file, index) in resources.localFiles" :key="index">
            <v-select
              label="Resource Type"
              v-model="file.resourceType"
              :items="resourceType"
              item-text="description"
              item-value="code"
            />
            <v-file-input label="Add File" v-model="file.file" />
            <v-text-field label="Description" v-model="file.description" />
            <v-select
              label="Security Marking"
              v-model="file.securityMarking"
              :items="securityMarkingList"
              item-text="description"
              item-value="code"
            />
            <v-btn icon @click="removeLocalFile"><v-icon>mdi-delete</v-icon></v-btn>
          </div>
          <v-btn @click="addLocalFile" color="primary" block class="mb-2">Add Local File</v-btn>
          <p>Links</p>
          <div v-for="(link, index) in resources.links" :key="index">
            <v-select
              label="Resource Type"
              v-model="link.resourceType"
              :items="resourceType"
              item-text="description"
              item-value="code"
            />
            <v-text-field label="Link" v-model="link.link" />
            <v-text-field label="Description" v-model="link.description" />
            <v-select
              label="Security Marking"
              v-model="link.securityMarking"
              :items="securityMarkingList"
              item-text="description"
              item-value="code"
            />
          </div>
          <v-btn @click="addLink" block color="primary">Add Link</v-btn>
        </div>
      </fieldset>
      <fieldset>
        <legend class="title">Tags</legend>
        <h3>Tags here</h3>
      </fieldset>
      <fieldset>
        <legend class="title">Contacts</legend>
        <h3>Contacts here here</h3>
      </fieldset>
      <v-btn color="primary" @click="submit">
        Save and close
      </v-btn>
      <v-btn :disabled="!isFormValid" color="success" class="mr-4" @click="submit">
        Submit
      </v-btn>
    </v-form>
  </div>
</template>

<script>
export default {
  name: 'SubmissionForm',
  mounted() {
    if (this.$store.state.currentUser.username) {
      this.setName()
    } else {
      // trigger an update once the user has been fetched
      this.$store.watch(
        (state, getters) => state.currentUser,
        (newValue, oldValue) => {
          this.setName()
        }
      )
    }
    this.$http.get('openstorefront/api/v1/resource/organizations').then(response => {
      this.organizationList = response.data.data
    })
    this.$http.get('openstorefront/api/v1/resource/lookuptypes/SecurityMarkingType').then(response => {
      this.securityMarkingList = response.data
    })
    this.$http.get('openstorefront/api/v1/resource/attributes/required?componentType=CNDHE').then(response => {
      this.attributes.required = response.data
      console.log(response.data)
    })
    this.$http.get('openstorefront/api/v1/resource/attributes/optional?componentType=CNDHE').then(response => {
      this.attributes.suggested = response.data.filter(e => e.attributeType !== 'MISSINGATTRIBUTE')
    })
    this.$http.get('/openstorefront/api/v1/resource/lookuptypes/ResourceType').then(response => {
      this.resourceType = response.data
    })
  },
  data: () => ({
    isFormValid: false,
    // entryDetails:
    entryTitle: '',
    entryType: '',
    organization: '',
    organizationList: [],
    securityMarking: '',
    securityMarkingList: [],
    // primaryPOC:
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    images: [{ file: null, caption: '', img: '' }],
    description: '',
    attributes: {
      required: [],
      suggested: [],
      missingAttribute: ''
    },
    resources: {
      localFiles: [{ resourceType: '', file: null, description: '', securityMarking: '' }],
      links: [{ resourceType: '', link: '', description: '', securityMarking: '' }]
    },
    resourceType: [],

    rules: {
      required: value => !!value || 'Required',
      numberOnly: value => !Number.isNaN(Number.parseFloat(value)) || 'This field only allows numbers'
    }
  }),
  methods: {
    setName() {
      this.firstName = this.$store.state.currentUser.firstName
      this.lastName = this.$store.state.currentUser.lastName
      this.phone = this.$store.state.currentUser.phone
      this.email = this.$store.state.currentUser.email
    },
    submit(data) {
      if (this.$refs.submissionForm.validate()) {
        console.log('Form is valid')
      } else {
        console.log('Form is invalid')
      }
    },
    addImage() {
      this.images.push({ file: null, caption: '', img: '' })
    },
    removeImage(index) {
      this.images.splice(index, 1)
    },
    imageChange(index) {
      let e = this.images[index]
      let reader = new FileReader()
      reader.onloadend = function() {
        e.img = reader.result
        console.log(e.img)
      }
      if (e.file) {
        reader.readAsDataURL(e.file)
      } else {
        e.img = ''
      }
    },
    addLocalFile() {
      this.resources.localFiles.push({ resourceType: '', file: null, description: '', securityMarking: '' })
    },
    removeLocalFile(index) {
      this.resources.localFiles.splice(index, 1)
    },
    addLink() {
      this.resources.links.push({ resourceType: '', link: '', description: '', securityMarking: '' })
    },
    removeLink(index) {
      this.resources.links.splice(index, 1)
    }
  },
  watch: {
    entryType: function(oldVal, newVal) {
      console.log(oldVal)
      console.log(newVal)
    }
  }
}
</script>

<style>
fieldset {
  border: 1px solid rgba(0, 0, 0, 0.2);
  background-color: white;
  border-radius: 10px;
  margin: 2em 0;
  padding: 6px;
}
legend {
  margin-left: 1em;
}
</style>

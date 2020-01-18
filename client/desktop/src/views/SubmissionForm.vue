<template>
  <div>
    <h1 class="text-center">Submission Form</h1>
    <div class="text-center px-2" style="color: red;">
      <h2>Caution!</h2>
      <p>{{ $store.state.branding.userInputWarning }}</p>
    </div>
    <v-form v-model="isFormValid" id="submissionForm" style="width: 80%;" class="mx-auto">
      <fieldset class="fieldset flex-wrap">
        <legend class="legend title">Entry Details*</legend>
        <v-text-field
          label="Entry Title*"
          v-model="entryTitle"
          required
          :rules="[rules.required]"
          class="mx-4 mw-18"
          autofocus
        />
        <v-autocomplete
          label="Entry Type*"
          v-model="entryType"
          :items="this.$store.state.componentTypeList"
          item-text="parentLabel"
          item-value="componentType"
          required
          :rules="[rules.required]"
          class="mx-4 mw-18"
        />
        <v-autocomplete
          label="Organization*"
          v-model="organization"
          :items="organizationList"
          item-text="name"
          item-value="name"
          required
          :rules="[rules.required]"
          class="mx-4 mw-18"
        />
        <!-- <v-select
          label="Security Marking*"
          v-model="securityMarking"
          :items="securityMarkingList"
          item-text="description"
          item-value="code"
          required
          :rules="[rules.required]"
          class="mx-4 mw-14"
        />-->
      </fieldset>
      <fieldset class="fieldset">
        <legend class="title legend">Primary Point of Contact*</legend>
        <div class="flex-wrap">
          <div class="flex-wrap w-100">
            <v-text-field
              label="First Name*"
              v-model="firstName"
              required
              :rules="[rules.required]"
              class="mx-4 mw-14"
            />
            <v-text-field label="Last Name*" v-model="lastName" required :rules="[rules.required]" class="mx-4 mw-14" />
            <v-text-field label="Email*" v-model="email" required :rules="[rules.required]" class="mx-4 mw-14" />
            <v-text-field label="Phone*" v-model="phone" required :rules="[rules.required]" class="mx-4 mw-14" />
          </div>
        </div>
      </fieldset>
      <fieldset class="fieldset">
        <legend class="title legend">Image Upload</legend>
        <v-btn color="grey lighten-2" class="mt-0 ma-4" @click="addImage"> <v-icon left>mdi-plus</v-icon>Add image </v-btn>
        <div class="image-row" v-for="(item, index) in images" :key="index">
          <div class="flex-wrap">
            <div class="bg-light-gray mb-4">
              <v-img
                class="mw-14 ma-0"
                :src="item.img"
                alt="Image preview"
                max-height="80px"
                max-width="120px"
                contain
                style="height: 80px;"
              />
            </div>
            <v-file-input
              v-model="item.file"
              label="Upload Image*"
              required
              :rules="[rules.image, rules.required]"
              @change="imageChange(index)"
              :accept="allowedImageTypesString"
              class="mx-4 mw-14"
            />
            <v-text-field
              v-model="item.caption"
              label="Image Caption*"
              required
              :rules="[rules.required]"
              class="mx-4 mw-14"
            />
          </div>
          <div>
            <v-btn icon title="delete" @click="removeImage(index)">
              <v-icon>mdi-delete</v-icon>
            </v-btn>
          </div>
        </div>
      </fieldset>
      <fieldset class="fieldset">
        <legend class="title legend">Description*</legend>
        <quill-editor class="ma-2" v-model="description" />
      </fieldset>
      <fieldset class="fieldset">
        <legend class="title legend">Attributes</legend>
        <!-- TODO: Fix the issue with multiple select -->
          <fieldset class="fieldset mt-0 attribute-grid">
            <legend class="title legend">Required Attributes</legend>
            <p v-if="attributes.required.length === 0">
              No required attributes, please select an entry type.
            </p>
            <div class="attribute" v-for="attribute in attributes.required" :key="attribute.attributeType">
                <v-text-field
                  v-if="attribute.allowUserGeneratedCodes && attribute.attributeValueType === 'TEXT'"
                  v-model="attribute.selectedCodes"
                  :label="attribute.description"
                  class="mr-3"
                  required
                  :rules="[rules.required]"
                />
                <v-text-field
                  v-else-if="attribute.allowUserGeneratedCodes && attribute.attributeValueType === 'NUMBER'"
                  v-model="attribute.selectedCodes"
                  :label="attribute.description"
                  :rules="[rules.numberOnly, rules.required]"
                  class="mr-3"
                  required
                />
                <v-select
                  v-else
                  :label="attribute.description"
                  clearable
                  :items="attribute.codes"
                  item-text="label"
                  item-code="code"
                  required
                  :rules="[rules.required]"
                  class="mr-3"
                />
                <v-select
                  label="Unit"
                  v-if="attribute.attributeValueType === 'NUMBER' && attribute.attributeUnit !== ''"
                  :value="attribute.attributeUnit"
                  :items="attribute.attributeUnitList"
                  item-text="unit"
                  item-value="unit"
                  class="mr-3 unit"
                  required
                  :rules="[rules.required]"
                />
            </div>
          </fieldset>
          <fieldset class="fieldset attribute-grid">
            <legend class="title legend">Suggested Attributes</legend>
            <p v-if="attributes.suggested.length === 0">
              No suggested attributes, please select an entry type.
            </p>
            <div class="attribute" v-for="attribute in attributes.suggested" :key="attribute.attributeType">
              <v-text-field
                v-if="attribute.allowUserGeneratedCodes && attribute.attributeValueType === 'TEXT'"
                v-model="attribute.selectedCodes"
                :label="attribute.description"
                class="mr-3"
              />
              <v-text-field
                v-else-if="attribute.allowUserGeneratedCodes && attribute.attributeValueType === 'NUMBER'"
                v-model="attribute.selectedCodes"
                :label="attribute.description"
                :rules="[rules.numberOnly]"
                class="mr-3"
              />
              <v-select
                v-else
                :label="attribute.description"
                clearable
                :items="attribute.codes"
                item-text="label"
                item-code="code"
                class="mr-3"
              />
              <v-select
                label="Unit"
                v-if="attribute.attributeValueType === 'NUMBER' && attribute.attributeUnit !== ''"
                :value="attribute.attributeUnit"
                :items="attribute.attributeUnitList"
                item-text="unit"
                item-value="unit"
                class="mr-3 unit"
              />
            </div>
          </fieldset>
        <div class="mx-4 mt-4">
          <p class="mb-0">Please describe the attribute you would like to have added.</p>
          <p class="mb-3">
            Include the value for your entry, a brief description, and how your part is defined by the attribute.
          </p>
          <label class="title" for="request-new-attribute">Request New Attribute (opt.)</label>
          <v-textarea outlined placeholder="Request new attribute" class="" v-model="attributes.missingAttribute" />
        </div>
      </fieldset>
      <fieldset class="fieldset">
        <legend class="title legend">Resources</legend>
        <fieldset class="fieldset mt-0">
          <legend class="title legend">Local Files</legend>
          <v-btn color="grey lighten-2" @click="addLocalFile">Add Local File</v-btn>
          <div class="image-row" v-for="(file, index) in resources.localFiles" :key="index">
            <div class="file-grid">
              <v-select
                label="Resource Type"
                v-model="file.resourceType"
                :items="resourceType"
                item-text="description"
                item-value="code"
              />
              <v-file-input label="Add File" v-model="file.file" />
              <v-text-field label="Description" v-model="file.description" />
              <!-- <v-select
                label="Security Marking"
                v-model="file.securityMarking"
                :items="securityMarkingList"
                item-text="description"
                item-value="code"
              />-->
            </div>
            <div>
              <v-btn title="delete" icon @click="removeLocalFile(index)">
                <v-icon>mdi-delete</v-icon>
              </v-btn>
            </div>
          </div>
        </fieldset>
        <fieldset class="fieldset mb-1">
          <legend class="title legend">External Resource</legend>
          <v-btn @click="addLink" color="grey lighten-2">Add URL</v-btn>
          <div class="image-row" v-for="(link, index) in resources.links" :key="index">
            <div class="file-grid">
              <v-select
                label="Resource Type"
                v-model="link.resourceType"
                :items="resourceType"
                item-text="description"
                item-value="code"
              />
              <v-text-field label="URL" v-model="link.link" />
              <v-text-field label="Description" v-model="link.description" />
            </div>
            <div>
              <v-btn title="delete" icon @click="removeLink(index)">
                <v-icon>mdi-delete</v-icon>
              </v-btn>
            </div>
          </div>
        </fieldset>
      </fieldset>
      <fieldset class="fieldset">
        <legend class="title legend">Tags</legend>
        <v-autocomplete
          label="Add tags"
          v-model="tags"
          multiple
          :items="tagsList"
          chips
          deletable-chips
          @keypress.enter="addTag"
          :search-input.sync="tagSearchText"
          class="mx-4"
        />
      </fieldset>
      <fieldset class="fieldset">
        <legend class="title legend">Contacts</legend>
        <v-btn color="grey lighten-2" @click="addContact">Add Contact</v-btn>
        <div class="image-row mx-4" v-for="(contact, index) in contacts" :key="index">
          <div class="contact-grid">
            <!-- get fields from backend -->
            <v-select
              :items="contactTypeList"
              v-model="contact.type"
              label="Contact Type"
              item-text="description"
              item-value="code"
            />
            <v-text-field v-model="contact.firstName" label="First Name" />
            <v-text-field v-model="contact.lastName" label="Last Name" />
            <v-text-field v-model="contact.email" label="Email" />
            <v-text-field v-model="contact.phone" label="Phone" />
          </div>
          <div>
            <v-btn icon @click="removeContact(index)">
              <v-icon>mdi-delete</v-icon>
            </v-btn>
          </div>
        </div>
      </fieldset>
      <!-- <div class="mb-8">
        <h2 class="mb-2 title">Form validation errors</h2>
        <ul class="form-errors">
          <li>entry description</li>
          <li>contacts</li>
          <li>required attributes</li>
        </ul>
      </div> -->
      <div class="mb-5">
        <p>If you save and close the entry you will need to come back and finish to submit the entry.</p>
        <v-btn class="mr-4 mb-3" color="primary" @click="submit">Save and close</v-btn>
        <v-btn :disabled="!isFormValid" color="success" class="mr-4 mb-3" @click="submit">Submit</v-btn>
      </div>
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
    this.$http.get('/openstorefront/api/v1/resource/lookuptypes/ResourceType').then(response => {
      this.resourceType = response.data
    })
    this.$http.get(`/openstorefront/api/v1/resource/components/tags`).then(response => {
      this.tagsList = response.data
    })
    this.$http.get('/openstorefront/api/v1/resource/lookuptypes/UserTypeCode').then(response => {
      this.contactTypeList = response.data
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
    // Images
    images: [],
    allowedImageTypes: ['image/png', 'image/jpeg', 'image/jpg'],
    // Description
    description: '',
    // Attributes
    attributes: {
      required: [],
      suggested: [],
      missingAttribute: ''
    },
    // Resources
    resources: {
      localFiles: [],
      links: []
    },
    resourceType: [],
    // Tags
    tagSearchText: '',
    tags: [],
    tagsList: [],
    // Contacts
    contactTypeList: [],
    contacts: [{ firstName: '', lastName: '', type: '', organization: '', email: '', phone: '' }],

    rules: {
      required: value => !!value || 'Required',
      // TODO: Fix issue with null values
      numberOnly: value => {
        // If the value is null, we don't care about validation, in this case
        if (value === null) {
          return true
        }
        // if the value is an array, iterate over it and check each selection
        if (Array.isArray(value)) {
          let valid = true
          value.forEach(e => {
            if (/\d+(\.\d+)?/.exec(e)[0] === e) {
              valid = false
            }
          })
          // if we found one invalid piece, invalidate the whole field
          if (valid === false) {
            return 'Invalid Number'
          } else {
            return true
          }
        }
        // else it is just a string, so check that
        return /\d+(\.\d+)?/.exec(value)[0] === value || 'Invalid Number'
      },
      image: value => {
        if (value === null) {
          return true
        }
        let allowedImageTypes = ['image/png', 'image/jpeg', 'image/jpg']
        return allowedImageTypes.includes(value['type']) || 'Not a valid image'
      }
    }
  }),
  computed: {
    allowedImageTypesString() {
      return this.allowedImageTypes.join(',')
    }
  },
  methods: {
    setAttributes() {
      if (this.entryType === '') {
        return
      }
      this.$http.get(`openstorefront/api/v1/resource/attributes/required?componentType=${this.entryType}`).then(response => {
        this.attributes.required = response.data
        this.attributes.required.forEach(e => {
          if (e.allowMultipleFlg) {
            e.selectedCodes = []
          } else {
            e.selectedCodes = ''
          }
          if (e.attributeUnitList) {
            e.attributeUnitList = e.attributeUnitList.filter(e => e.unit !== undefined)
            if (e.attributeUnitList.length === 0) {
              e.attributeUnit = ''
            }
          }
        })
      })
      this.$http.get(`openstorefront/api/v1/resource/attributes/optional?componentType=${this.entryType}`).then(response => {
        this.attributes.suggested = response.data.filter(e => e.attributeType !== 'MISSINGATTRIBUTE')
        this.attributes.suggested.forEach(e => {
          if (e.allowMultipleFlg) {
            e.selectedCodes = []
          } else {
            e.selectedCodes = ''
          }
          if (e.attributeUnitList) {
            e.attributeUnitList = e.attributeUnitList.filter(e => e.unit !== undefined)
            if (e.attributeUnitList.length === 0) {
              e.attributeUnit = ''
            }
          }
        })
      })
    },
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
      e.img = ''
      // test that the file is an image
      let validImageTypes = ['image/png', 'image/svg', 'image/jpeg', 'image/jpg']
      if (e.file && validImageTypes.includes(e.file['type'])) {
        let reader = new FileReader()
        reader.onloadend = function() {
          e.img = reader.result
        }
        if (e.file) {
          reader.readAsDataURL(e.file)
        } else {
          e.img = ''
        }
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
    },
    addTag() {
      this.tagsList.push(this.tagSearchText)
      this.tags.push(this.tagSearchText)
      this.tagSearchText = ''
    },
    addContact() {
      this.contacts.push({ firstName: '', lastName: '', type: '', organization: '', email: '', phone: '' })
    },
    removeContact(index) {
      this.contacts.splice(index, 1)
    }
  },
  watch: {
    entryType: function(oldVal, newVal) {
      // TODO: Deal with entryType state change
      this.setAttributes()
      // console.log(oldVal)
      // console.log(newVal)
    }
  }
}
</script>

<style lang="css" scoped>
.fieldset {
  border: 1px solid rgba(0, 0, 0, 0.2);
  background-color: white;
  border-radius: 10px;
  margin: 2em 0;
  padding: 1em;
  padding-bottom: 12px;
}
.legend {
  margin-left: 1em;
  padding: 0 0.5em;
}
.bg-light-gray {
  background: rgba(190, 190, 190, 0.2);
}
.tc {
  text-align: center;
}
.bold {
  font-weight: 700;
}
.text-dark-gray {
  color: rgba(0, 0, 0, 0.5);
}
.flex-wrap {
  display: flex;
  flex-wrap: wrap;
}
.mw-14 {
  min-width: 14em;
}
.mw-18 {
  min-width: 18em;
}
.attribute-row {
  display: grid;
  grid-gap: 1em;
  grid-template-columns: 3fr 1fr;
  align-items: center;
  padding: 0 1em;
}
.image-row {
  display: grid;
  grid-gap: 1em;
  grid-template-columns: 14fr 1fr;
  align-items: center;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  margin-bottom: 12px;
}
.image-row:last-child {
  border-bottom: none;
  margin-bottom: 0px;
}
.contact-row {
  display: grid;
  grid-gap: 1em;
  grid-template-columns: 1fr 14fr;
  align-items: center;
  padding: 0 1em;
  margin-bottom: 2em;
}
.form-errors {
  border-radius: 10px;
  color: #610000;
  background-color: #ffa0a081;
  display: inline-block;
  padding: 1em;
  padding-left: 2em;
}
.attribute-grid {
  display: grid;
  grid-column-gap: 3em;
  grid-row-gap: 1em;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  align-items: center;
}
.contact-grid {
  display: grid;
  grid-gap: 1em;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  align-items: center;
}
.file-grid {
  display: grid;
  grid-gap: 1em;
  grid-row-gap: 0px;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  align-items: center;
}
.attribute {
  display: flex;
}
.unit {
  max-width: 10em;
}
</style>

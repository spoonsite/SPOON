<template>
  <div>
    <h1 class="text-center mt-4">New Entry Submission Form</h1>
    <div class="text-center px-2 error--text">
      <h2>Caution!</h2>
      <p v-html="$store.state.branding.userInputWarning"></p>
    </div>
    <v-form v-model="isFormValid" ref="submissionForm" style="width: 80%;" class="mx-auto">
      <fieldset class="fieldset flex-wrap">
        <legend class="legend title">Entry Details*</legend>
        <v-text-field
          label="Entry Title*"
          v-model="entryTitle"
          name="name"
          required
          :rules="[rules.required, rules.len255]"
          class="mx-4 mw-18"
          counter="255"
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
          placeholder="Search Organizations"
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
              v-model="primaryPOC.firstName"
              required
              :rules="[rules.required]"
              class="mx-4 mw-14"
            />
            <v-text-field
              label="Last Name*"
              v-model="primaryPOC.lastName"
              required
              :rules="[rules.required]"
              class="mx-4 mw-14"
            />
            <v-text-field
              label="Email*"
              v-model="primaryPOC.email"
              required
              :rules="[rules.required]"
              class="mx-4 mw-14"
            />
            <v-text-field
              label="Phone*"
              v-model="primaryPOC.phone"
              required
              :rules="[rules.required]"
              class="mx-4 mw-14"
            />
            <v-autocomplete
              label="Organization*"
              v-model="primaryPOC.organization"
              id="organization"
              :items="organizationList"
              item-text="name"
              item-value="name"
              :rules="[rules.required]"
              class="mx-4 mw-18"
            />
          </div>
        </div>
      </fieldset>
      <fieldset class="fieldset">
        <legend class="title legend">Image Upload</legend>
        <v-btn color="grey lighten-2" class="mt-0 ma-4" @click="addImage" :disabled="images.length > 10">
          <v-icon left>mdi-plus</v-icon>Add image
        </v-btn>
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
        <quill-editor class="ma-2" v-model="description" maxLength="20" />
        <v-alert color="red" :value="false" transition="fade-transition">
          test
        </v-alert>
        <v-slide-y-transition>
          <div v-if="description.length === 0" class="mx-2 error--text caption">
            Description is required
          </div>
          <div v-if="description.length > 65536" class="mx-2 error--text caption">
            Description has a character limit of 64k
          </div>
        </v-slide-y-transition>
      </fieldset>
      <fieldset class="fieldset">
        <legend class="title legend">Attributes</legend>
        <!-- TODO: Fix the issue with multiple select -->
        <!-- TODO: Check into these more in regard to the flags on the attributes -->
        <fieldset class="fieldset mt-0 attribute-grid">
          <legend class="title legend">Required Attributes*</legend>
          <p v-if="attributes.required.length === 0">
            No required attributes, please select an entry type.
          </p>
          <div class="attribute" v-for="attribute in attributes.required" :key="attribute.attributeType">
            <v-autocomplete
              v-if="attribute.allowMultipleFlg && attribute.allowUserGeneratedCodes"
              v-model="attribute.selectedCodes"
              :label="`${attribute.description}*`"
              multiple
              chips
              deletable-chips
              :items="attribute.codes"
              item-text="label"
              item-value="code"
              :search-input.sync="attribute.searchText"
              @keypress.enter="
                attribute.codes.push(attribute.searchText)
                attribute.selectedCodes.push(attribute.searchText)
                attribute.searchText = ''
              "
              class="mr-3"
              :rules="
                attribute.attributeValueType === 'NUMBER'
                  ? [rules.requiredArray, rules.numberOnly]
                  : [rules.requiredArray]
              "
              required
            />
            <v-autocomplete
              v-if="attribute.allowMultipleFlg && !attribute.allowUserGeneratedCodes"
              v-model="attribute.selectedCodes"
              :label="`${attribute.description}*`"
              multiple
              chips
              deletable-chips
              :items="attribute.codes"
              item-text="label"
              item-value="code"
              class="mr-3"
              :rules="
                attribute.attributeValueType === 'NUMBER'
                  ? [rules.requiredArray, rules.numberOnly]
                  : [rules.requiredArray]
              "
              required
            />
            <v-text-field
              v-if="!attribute.allowMultipleFlg && attribute.allowUserGeneratedCodes"
              v-model="attribute.selectedCodes"
              :label="`${attribute.description}*`"
              class="mr-3"
              :rules="attribute.attributeValueType === 'NUMBER' ? [rules.required, rules.numberOnly] : [rules.required]"
              required
            />
            <v-autocomplete
              v-if="!attribute.allowMultipleFlg && !attribute.allowUserGeneratedCodes"
              v-model="attribute.selectedCodes"
              :label="`${attribute.description}*`"
              :items="attribute.codes"
              item-text="label"
              item-value="code"
              class="mr-3"
              :rules="attribute.attributeValueType === 'NUMBER' ? [rules.required, rules.numberOnly] : [rules.required]"
              required
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
        <fieldset class="fieldset attribute-grid">
          <legend class="title legend">Suggested Attributes (opt.)</legend>
          <p v-if="attributes.suggested.length === 0">
            No suggested attributes, please select an entry type.
          </p>
          <div class="attribute" v-for="attribute in attributes.suggested" :key="attribute.attributeType">
            <v-autocomplete
              v-if="attribute.allowMultipleFlg && attribute.allowUserGeneratedCodes"
              v-model="attribute.selectedCodes"
              :label="`${attribute.description}`"
              multiple
              chips
              deletable-chips
              :items="attribute.codes"
              item-text="label"
              item-value="code"
              :search-input.sync="attribute.searchText"
              @keypress.enter="
                attribute.codes.push(attribute.searchText)
                attribute.selectedCodes.push(attribute.searchText)
                attribute.searchText = ''
              "
              class="mr-3"
            />
            <v-autocomplete
              v-if="attribute.allowMultipleFlg && !attribute.allowUserGeneratedCodes"
              v-model="attribute.selectedCodes"
              :label="`${attribute.description}`"
              multiple
              chips
              deletable-chips
              :items="attribute.codes"
              item-text="label"
              item-value="code"
              class="mr-3"
            />
            <v-text-field
              v-if="!attribute.allowMultipleFlg && attribute.allowUserGeneratedCodes"
              v-model="attribute.selectedCodes"
              :label="`${attribute.description}`"
              class="mr-3"
            />
            <v-text-field
              v-if="!attribute.allowMultipleFlg && !attribute.allowUserGeneratedCodes"
              v-model="attribute.selectedCodes"
              :label="`${attribute.description}`"
              :items="attribute.codes"
              item-text="label"
              item-value="code"
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
          <v-textarea
            outlined
            id="request-new-attribute"
            placeholder="Request new attribute"
            class=""
            v-model="attributes.missingAttribute"
          />
        </div>
      </fieldset>
      <fieldset class="fieldset">
        <legend class="title legend">Resources</legend>
        <fieldset class="fieldset mt-0">
          <legend class="title legend">Local Files</legend>
          <v-btn color="grey lighten-2" @click="addLocalFile" :disabled="resources.localFiles.length > 10"
            >Add Local File</v-btn
          >
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
          <v-btn @click="addLink" color="grey lighten-2" :disabled="resources.links.length > 10">Add URL</v-btn>
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
          return-object
          :items="tagsList"
          chips
          deletable-chips
          @keypress.enter="addTag"
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
              v-model="contact.contactType"
              label="Contact Type"
              item-text="description"
              item-value="code"
            />
            <v-text-field v-model="contact.firstName" label="First Name" />
            <v-text-field v-model="contact.lastName" label="Last Name" />
            <v-text-field v-model="contact.email" label="Email" />
            <v-text-field v-model="contact.phone" label="Phone" />
            <v-autocomplete
              label="Organization"
              v-model="contact.organization"
              :items="organizationList"
              item-text="name"
              item-value="name"
            />
          </div>
          <div>
            <v-btn icon @click="removeContact(index)">
              <v-icon>mdi-delete</v-icon>
            </v-btn>
          </div>
        </div>
      </fieldset>
      <v-alert type="error" v-if="!isFormValid" prominent outlined>
        Form validation errors. Please check the form.
      </v-alert>
      <v-alert type="error" v-if="errors.length > 0" colored-border border="left" elevation="2">
        <ul>
          <li v-for="error in errors" :key="error.key">
            <span class="bold">{{ error.key }}:</span> {{ error.value }}
          </li>
        </ul>
      </v-alert>
      <div class="mb-5">
        <p>If you save and close the entry you will need to come back and finish to submit the entry.</p>
        <v-btn class="mr-4 mb-3" color="primary" @click="submit">Save and close</v-btn>
        <!-- :disabled="!isFormValid" -->
        <v-btn color="success" class="mr-4 mb-3" @click="submit">
          Submit
        </v-btn>
      </div>
    </v-form>
    <v-dialog :value="showEntryTypeWarning" @input="showEntryTypeWarning = false" max-width="50em">
      <v-card>
        <ModalTitle title="Are you sure?" @close="showEntryTypeWarning = false" />
        <v-card-text>
          Changing the entry type will change the associated attributes. If you change your entry type the form may
          delete some of the entered attributes.
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn color="warning" @click="changeEntryType">Change Entry Type</v-btn>
          <v-btn @click="cancelChangeEntryType">Close</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import ModalTitle from '@/components/ModalTitle'
import _ from 'lodash'

export default {
  name: 'SubmissionForm',
  components: { ModalTitle },
  beforeRouteLeave(to, from, next) {
    const answer = window.confirm('Do you really want to leave?')
    if (answer) {
      next()
    } else {
      next(false)
    }
  },
  mounted() {
    // load the data from an existing submission
    if (this.$route.params.id) {
      if (this.$route.params.id !== 'new') {
        // load in the data
        this.id = this.$route.params.id
        this.loadData(this.id)
      } else {
        // auto fill out the user info
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
      }
    }
    this.$http.get('/openstorefront/api/v1/resource/organizations').then(response => {
      this.organizationList = response.data.data
    })
    this.$http.get('/openstorefront/api/v1/resource/lookuptypes/SecurityMarkingType').then(response => {
      this.securityMarkingList = response.data
    })
    this.$http.get('/openstorefront/api/v1/resource/lookuptypes/ResourceType').then(response => {
      this.resourceType = response.data
    })
    this.$http.get(`/openstorefront/api/v1/resource/components/tags`).then(response => {
      this.tagsList = response.data
    })
    this.$http.get('/openstorefront/api/v1/resource/lookuptypes/ContactType').then(response => {
      this.contactTypeList = response.data
    })
  },
  data: () => ({
    // server validation errors
    errors: [],
    isFormValid: false,
    // entryDetails:
    id: null,
    entryTitle: '',
    entryType: '',
    lastEntryType: '',
    showEntryTypeWarning: false,
    isEntryTypeDirty: false,
    organization: '',
    organizationList: [],
    securityMarking: '',
    securityMarkingList: [],
    // primaryPOC:
    primaryPOC: {
      firstName: '',
      lastName: '',
      email: '',
      phone: '',
      organization: '',
      contactType: 'SUB'
    },
    // 'SUB' -> the submitter
    // Images
    images: [],
    allowedImageTypes: ['image/png', 'image/jpeg', 'image/jpg'],
    // Description
    description: '',
    // Attributes
    savedAttributes: [],
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
    contacts: [],

    rules: {
      required: value => !!value || 'Required',
      requiredArray: value => value.length !== 0 || 'Required',
      len255: value => value.length < 255 || 'Must have less than 255 characters',
      len64k: value => value.length < 65536 || 'Must have less than 64k characters',
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
    /**
     * attributes cannot be loaded until the entry type is loaded
     */
    loadData(id) {
      this.$http
        .get(`/openstorefront/api/v1/resource/componentsubmissions/${id}`)
        .then(response => {
          // console.log(response.data)
          let component = response.data.component
          let contacts = response.data.contacts

          this.entryTitle = component.name
          this.entryType = component.componentType
          this.description = component.description
          this.organization = component.organization
          // load tags
          this.savedAttributes = response.data.attributes

          this.primaryPOC = contacts[0] // unsafe
          this.contacts = _.tail(contacts)
        })
        .catch(e => {
          console.error(e)
        })
    },
    getFormData() {
      // console.log(this.images)
      // TODO: make a Browser form object
      // let formData = new FormData()

      // formData.append('name', this.entryTitle)
      // formData.append('componentType', this.entryType)
      // formData.append('organization', this.organization)
      // formData.append('images', this.images)
      // formData.append('description', this.description)
      // // TODO: Fix attributes
      // formData.append('requiredAttributes', this.attributes.required)
      // formData.append('suggestedAttributes', this.attributes.suggested)
      // formData.append('localFiles', this.resources.localFiles)
      // formData.append('urls', this.resources.links)
      // formData.append('tags', this.tags)
      // formData.append('contacts', [this.primaryPOC].concat(this.contacts))
      // for (var i = 0; i < this.images.length; i++) {
      //   this.images[0].file.description = this.images[0].description
      //   formData.append('file', this.images[0].file)
      // }

      // return formData

      let allAttributes = this.attributes.required.concat(this.attributes.suggested)
      let newAttributes = []
      allAttributes.forEach(el => {
        if (Array.isArray(el.selectedCodes) && el.selectedCodes.length > 0) {
          el.selectedCodes.forEach(code => {
            newAttributes.push({
              componentAttributePk: {
                attributeType: el.attributeType,
                attributeCode: code
              }
            })
          })
        } else if (typeof el.selectedCodes === 'string' && el.selectedCodes !== '') {
          newAttributes.push({
            componentAttributePk: {
              attributeType: el.attributeType,
              attributeCode: el.selectedCodes
            }
          })
        }
      })

      return {
        component: {
          name: this.entryTitle,
          description: this.description,
          componentType: this.entryType,
          organization: this.organization
        },
        attributes: newAttributes,
        // TODO: fix media and resources
        // media: this.images,
        // resources: this.resources.localFiles.concat(this.resources.links),
        tags: this.tags,
        contacts: [this.primaryPOC].concat(this.contacts)
      }
    },
    setAttributes() {
      if (this.entryType === '') {
        return
      }
      this.$http
        .get(`/openstorefront/api/v1/resource/attributes/required?componentType=${this.entryType}`)
        .then(response => {
          // TODO: Add check for hideOnSubmission
          this.attributes.required = response.data
          this.attributes.required.forEach(e => {
            // Set up values for required codes
            if (e.allowMultipleFlg && e.allowUserGeneratedCodes) {
              e.selectedCodes = []
              e.searchText = ''
            } else if (e.allowMultipleFlg && !e.allowUserGeneratedCodes) {
              e.selectedCodes = []
            } else if (!e.allowMultipleFlg && e.allowUserGeneratedCodes) {
              e.selectedCodes = ''
            } else if (!e.allowMultipleFlg && !e.allowUserGeneratedCodes) {
              e.selectedCodes = ''
            }

            // Set up unit stuff
            if (e.attributeUnitList) {
              e.attributeUnitList = e.attributeUnitList.filter(e => e.unit !== undefined)
              if (e.attributeUnitList.length === 0) {
                e.attributeUnit = ''
              }
            }

            // load saved attributes
            this.savedAttributes.forEach(attribute => {
              if (attribute.componentAttributePk.attributeType === e.attributeType) {
                if (Array.isArray(e.selectedCodes)) {
                  e.selectedCodes.push(attribute.componentAttributePk.attributeCode)
                } else {
                  e.selectedCodes = attribute.componentAttributePk.attributeCode
                }
              }
            })
          })
        })
      this.$http
        .get(`/openstorefront/api/v1/resource/attributes/optional?componentType=${this.entryType}`)
        .then(response => {
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
            // load saved attributes
            this.savedAttributes.forEach(attribute => {
              if (attribute.componentAttributePk.attributeType === e.attributeType) {
                if (Array.isArray(e.selectedCodes)) {
                  e.selectedCodes.push(attribute.componentAttributePk.attributeCode)
                } else {
                  e.selectedCodes = attribute.componentAttributePk.attributeCode
                }
              }
            })
          })
        })
    },
    changeEntryType() {
      this.showEntryTypeWarning = false
      this.setAttributes()
    },
    cancelChangeEntryType() {
      this.showEntryTypeWarning = false
      this.isEntryTypeDirty = true
      this.entryType = this.lastEntryType
      this.lastEntryType = ''
    },
    setName() {
      this.primaryPOC.firstName = this.$store.state.currentUser.firstName
      this.primaryPOC.lastName = this.$store.state.currentUser.lastName
      this.primaryPOC.phone = this.$store.state.currentUser.phone
      this.primaryPOC.email = this.$store.state.currentUser.email
      this.primaryPOC.organization = this.$store.state.currentUser.organization
    },
    submit(data) {
      let formData = this.getFormData()

      // let userCreatedAttributes = formData.attributes.filter(el => el.userCreated)
      // let createdCodeList = []
      // userCreatedAttributes.forEach(el => {
      //   createdCodeList.push({
      //     attributeCodeLabel: el.attributeCode,
      //     attributeType: el.attributeType
      //   })
      // })

      // this.$http
      //   .post('/openstorefront/api/v1/resource/attributes/attributetypes/usercodes', createdCodeList)
      //   .then(response => {
      //     console.log(response)
      //   })
      //   .catch(e => {
      //     console.error(e)
      //   })

      if (this.id && this.id !== 'new') {
        this.$http
          .put(`/openstorefront/api/v1/resource/componentsubmissions/${this.id}`, formData)
          .then(response => {
            if (response.data && response.data.success === false) {
              this.errors = response.data.errors.entry
            }
            if (response.data && response.data.component) {
              this.errors = []
              this.$toasted.success('Submission Saved')
            }
          })
          .catch(e => {
            console.error(e)
          })
      } else {
        this.$http
          .post('/openstorefront/api/v1/resource/componentsubmissions', formData)
          .then(response => {
            if (response.data && response.data.success === false) {
              this.errors = response.data.errors.entry
            }
            if (response.data && response.data.component) {
              this.errors = []
              this.componentId = response.data.component.componentId
              this.$router.replace(`${this.componentId}`)
              this.$toasted.success('Submission Saved')
            }
          })
          .catch(e => {
            console.error(e)
          })
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
      this.contacts.push({ firstName: '', lastName: '', contactType: '', organization: '', email: '', phone: '' })
    },
    removeContact(index) {
      this.contacts.splice(index, 1)
    }
  },
  watch: {
    entryType: function(newVal, oldVal) {
      if (oldVal === '') {
        this.setAttributes()
      } else {
        if (this.isEntryTypeDirty) {
          this.isEntryTypeDirty = false
          return
        }
        this.lastEntryType = oldVal
        this.showEntryTypeWarning = true
      }
    }
  }
}
</script>

<style lang="css" scoped>
.fieldset {
  border: 1px solid rgba(0, 0, 0, 0.2);
  background-color: white;
  border-radius: 4px;
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
  grid-template-columns: repeat(auto-fill, 430px);
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
.attribute * {
  align-items: end;
}
.unit {
  max-width: 10em;
}
</style>

<template>
  <div>
    <h1 class="text-center mt-4">New Entry Submission Form</h1>
    <div class="text-center px-2 error--text">
      <h2>Caution!</h2>
      <p v-html="$store.state.branding.userInputWarning"></p>
    </div>
    <v-form v-model="formValidation" ref="submissionForm" style="width: 80%;" class="mx-auto" autocomplete="off">
      <fieldset class="fieldset flex-wrap">
        <legend class="legend title">Entry Details*</legend>
        <v-text-field
          label="Entry Title*"
          v-model="entryTitle"
          name="name"
          required
          :rules="[rules.required, rules.len255]"
          class="mx-4 mw-18"
          counter="225"
          autofocus
        />
        <v-autocomplete
          label="Entry Type*"
          v-model="entryType"
          :items="this.entryTypeList"
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
        <legend class="title legend">Description*</legend>
        <quill-editor class="ma-2" v-model="description" maxLength="20" />
        <v-slide-y-transition>
          <div v-if="description.length === 0" class="mx-2 error--text caption">
            Description is required
          </div>
          <div v-if="description.length > MAX_DESCRIPTION_LENGTH" class="mx-2 error--text caption">
            Description has a character limit of 64k
          </div>
        </v-slide-y-transition>
      </fieldset>
      <!-- TODO: Fix the issue with multiple select -->
      <!-- TODO: Check into these more in regard to the flags on the attributes -->
      <fieldset class="fieldset mt-0 attribute-grid">
        <legend class="title legend">Required Attributes*</legend>
        <p v-if="attributes.required.length === 0">
          No required attributes, please select an entry type.
        </p>
        <div class="attribute" v-for="attribute in attributes.required" :key="attribute.attributeType">
          <v-combobox
            v-if="attribute.allowMultipleFlg && attribute.allowUserGeneratedCodes"
            v-model="attribute.selectedCodes"
            :label="`${attribute.description}*`"
            multiple
            append-icon
            chips
            deletable-chips
            :items="attribute.codes"
            item-text="label"
            item-value="code"
            :search-input.sync="attribute.searchText"
            @keypress.enter="
              attribute.codes.push({ label: attribute.searchText, code: attribute.searchText, userCreated: true })
              attribute.selectedCodes.push({
                label: attribute.searchText,
                code: attribute.searchText,
                userCreated: true
              })
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
            v-model="attribute.selectedUnit"
          />
        </div>
      </fieldset>
      <fieldset class="fieldset attribute-grid">
        <legend class="title legend">Suggested Attributes (opt.)</legend>
        <p v-if="attributes.suggested.length === 0">
          No suggested attributes, please select an entry type.
        </p>
        <div class="attribute" v-for="attribute in attributes.suggested" :key="attribute.attributeType">
          <v-combobox
            v-if="attribute.allowMultipleFlg && attribute.allowUserGeneratedCodes"
            v-model="attribute.selectedCodes"
            :label="`${attribute.description}`"
            append-icon
            multiple
            chips
            deletable-chips
            :items="attribute.codes"
            item-text="label"
            item-value="code"
            :search-input.sync="attribute.searchText"
            @keypress.enter="
              attribute.codes.push({ label: attribute.searchText, code: attribute.searchText, userCreated: true })
              attribute.selectedCodes.push({
                label: attribute.searchText,
                code: attribute.searchText,
                userCreated: true
              })
              attribute.searchText = ''
            "
            class="mr-3"
            :rules="
              attribute.attributeValueType === 'NUMBER'
                ? [rules.requiredArray, rules.numberOnly]
                : [rules.requiredArray]
            "
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
          <v-autocomplete
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
            v-model="attribute.selectedUnit"
          />
        </div>
      </fieldset>
      <fieldset class="fieldset">
        <legend class="title legend">Request New Attribute (opt.)</legend>
        <div class="mx-4 mt-4">
          <p class="mb-0">Please describe the attribute you would like to have added.</p>
          <p class="mb-3">
            Include the value for your entry, a brief description, and how your part is defined by the attribute.
          </p>
          <v-textarea
            outlined
            id="request-new-attribute"
            placeholder="New attribute details"
            class=""
            v-model="attributes.missingAttribute"
          />
        </div>
      </fieldset>
      <fieldset class="fieldset">
        <legend class="title legend">Resources</legend>
        <fieldset class="fieldset mt-0">
          <legend class="title legend">Image Upload</legend>
          <p v-if="!id" class="error--text">You must first save the submission to attach images to it.</p>
          <p>Choose an image to add to this part, add a descriptive caption, then upload the image.</p>
          <div class="image-row">
            <div class="flex-wrap">
              <div class="bg-light-gray mb-4">
                <v-img
                  class="mw-14 ma-0"
                  :src="currentImage.img"
                  alt="Image preview"
                  max-height="80px"
                  max-width="120px"
                  contain
                  style="height: 80px;"
                />
              </div>
              <v-file-input
                v-model="currentImage.file"
                label="Select an Image*"
                :rules="[rules.image]"
                @change="imageChange()"
                :accept="allowedImageTypesString"
                class="mx-4 mw-14"
                :disabled="!id"
              />
              <v-text-field v-model="currentImage.caption" label="Image Caption*" class="mx-4 mw-14" :disabled="!id" />
            </div>
            <div>
              <v-btn
                title="attach"
                elevation="0"
                :loading="uploadMedia"
                :disabled="currentImage.caption === '' || currentImage.file === null || uploadMedia"
                class="mr-2"
                @click="attachMedia()"
              >
                Upload
              </v-btn>
            </div>
          </div>
          <h2 class="mb-4" v-if="media && media.length > 0">Attached Media</h2>
          <div class="image-row" v-for="image in media" :key="image.componentMediaId">
            <div class="flex-wrap">
              <v-img
                class="mw-14 ma-0 mb-4"
                :src="`/openstorefront/Media.action?LoadMedia&mediaId=${image.file.mediaFileId}`"
                :alt="image.file.orignalName"
                max-height="80px"
                max-width="120px"
                contain
                style="height: 80px;"
              />
              <div class="mx-4">
                <p><span class="bold">File Name:</span> {{ image.file.originalName }}</p>
                <p><span class="bold">Caption:</span> {{ image.caption }}</p>
              </div>
            </div>
            <div>
              <v-btn
                small
                fab
                elevation="0"
                class="mb-2"
                :loading="image.loading"
                :disabled="image.loading"
                title="remove"
                @click="removeImage(image)"
              >
                <v-icon>mdi-delete</v-icon>
              </v-btn>
              <!-- <v-btn fab small elevation="0" title="edit" @click="editImage(image)">
              <v-icon>mdi-pencil</v-icon>
            </v-btn> -->
            </div>
          </div>
        </fieldset>
        <fieldset class="fieldset mt-0">
          <legend class="title legend">Local Files</legend>
          <p v-if="!id" class="error--text">You must first save the submission to attach local files to it.</p>
          <p>
            Choose a file to add to this part, add a descriptive caption, select a category for the file, then upload
            the file.
          </p>
          <div class="image-row">
            <div class="file-grid">
              <v-select
                label="Resource Type*"
                v-model="resourceFile.resourceType"
                :items="resourceType"
                item-text="description"
                item-value="code"
                :disabled="!id"
              />
              <v-file-input label="Add File*" :disabled="!id" v-model="resourceFile.file" />
              <v-text-field label="Description*" :disabled="!id" v-model="resourceFile.description" />
              <!-- <v-select
                label="Security Marking"
                v-model="file.securityMarking"
                :items="securityMarkingList"
                item-text="description"
                item-value="code"
              />-->
            </div>
            <div>
              <v-btn
                elevation="0"
                title="attach resource"
                @click="attachResource()"
                :loading="uploadingResource"
                :disabled="
                  !resourceFile.file || !resourceFile.description || !resourceFile.resourceType || uploadingResource
                "
              >
                Upload
              </v-btn>
            </div>
          </div>
          <h2 class="mb-4" v-if="resources.localFiles && resources.localFiles.length > 0">Attached Resources</h2>
          <div class="image-row" v-for="resource in resources.localFiles" :key="resource.componentMediaId">
            <div class="flex-wrap">
              <v-icon large>mdi-file-document-outline</v-icon>
              <div class="mx-4">
                <p><span class="bold">Resource Type:</span> {{ lookupType(resource.resourceType) }}</p>
                <p><span class="bold">File Name:</span> {{ resource.file.originalName }}</p>
                <p><span class="bold">Description:</span> {{ resource.description }}</p>
              </div>
            </div>
            <div>
              <v-btn
                small
                fab
                elevation="0"
                class="mb-2"
                :loading="resource.loading"
                :disabled="resource.loading"
                :title="`remove ${resource.file.originalName}`"
                @click="removeResource(resource)"
              >
                <v-icon>mdi-delete</v-icon>
              </v-btn>
            </div>
          </div>
        </fieldset>
      </fieldset>
      <fieldset class="fieldset">
        <legend class="title legend">Tags</legend>
        <p v-if="!id" class="error--text">You must first save the submission to add tags to it.</p>
        <v-autocomplete
          label="Add tags"
          v-model="tags"
          multiple
          return-object
          :items="tagsList"
          chips
          :disabled="!id"
          deletable-chips
          @keypress.enter="addTag"
          :search-input.sync="tagSearchText"
          class="mx-4"
        >
          <template v-slot:prepend-item>
            <v-list-item>
              <v-list-item-content>
                <v-list-item-title>Create a new tag by typing some text and then pressing enter</v-list-item-title>
              </v-list-item-content>
            </v-list-item>
            <v-divider class="mt-2"></v-divider>
          </template>
        </v-autocomplete>
      </fieldset>

      <fieldset class="fieldset mb-1">
        <legend class="title legend">External Links</legend>
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
      <v-alert type="error" outlined v-if="resourceFile.file || currentImage.file">
        You have selected files to upload but have not uploaded them.
      </v-alert>
      <v-alert type="error" v-if="errors.length > 0" colored-border border="left" elevation="2">
        <ul>
          <li v-for="error in errors" :key="error.key">
            <span class="bold">{{ error.key }}:</span> {{ error.value }}
          </li>
        </ul>
      </v-alert>
      <div class="mb-5">
        <p class="mb-2" v-if="timeLastSaved">Last saved at: {{ timeLastSaved | formatDate('Pp') }}</p>
        <p>If you close the entry without submitting you will need to come back and finish to submit the entry.</p>
        <v-btn
          class="mr-4 mb-3"
          :loading="savingAndClose"
          :disabled="savingAndClose"
          color="primary"
          @click="saveAndClose()"
        >
          Close
          <template v-slot:loader>
            <span>Saving...</span>
          </template>
        </v-btn>
        <v-btn :loading="saving" :disabled="saving || !isFormValid" color="success" class="mr-4 mb-3" @click="save()">
          Save
        </v-btn>
        <v-btn
          :loading="submitting"
          :disabled="submitting || !isFormValid"
          color="success"
          class="mr-4 mb-3"
          @click="submitConfirmDialog = true"
        >
          Submit
        </v-btn>
      </div>
    </v-form>

    <v-dialog :value="submitConfirmDialog" @input="submitConfirmDialog = false" max-width="50em">
      <v-card>
        <ModalTitle title="Are you sure?" @close="submitConfirmDialog = false" />
        <v-card-text>
          Are you sure you want to submit your entry for review?
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn color="warning" @click="submit">Submit</v-btn>
          <v-btn @click="submitConfirmDialog = false">Close</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog :value="showEntryTypeWarning" @input="showEntryTypeWarning = false" max-width="50em">
      <v-card>
        <ModalTitle title="Are you sure?" @close="showEntryTypeWarning = false" />
        <v-card-text>
          Changing the entry type will change the associated attributes. If you change your entry type the form will
          delete all of the entered attributes.
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

// defined in MediaType.java
const MEDIA_TYPE_CODE = {
  IMAGE: 'IMG',
  VIDEO: 'VID',
  TEXT: 'TEX',
  AUDIO: 'AUD',
  ARCHIVE: 'ARC',
  OTHER: 'OTH'
}

// from MediaFileType.java
// const MEDIA_FILE_TYPE = {
//   GENERAL: 'GENERAL',
//   SUPPORT: 'SUPPORT',
//   RESOURCE: 'RESOURCE',
//   MEDIA: 'MEDIA'
// }

export default {
  name: 'SubmissionForm',
  components: { ModalTitle },
  beforeRouteLeave(to, from, next) {
    if (!this.bypassLeaveConfirmation) {
      const answer = window.confirm('Do you really want to leave?')
      if (answer) {
        next()
      }
    } else {
      next()
    }
  },
  mounted() {
    this.bypassLeaveConfirmation = false
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
      this.organizationList.sort((a, b) => a.name > b.name ? 1 : -1)
    })
    this.$http.get('/openstorefront/api/v1/resource/lookuptypes/SecurityMarkingType').then(response => {
      this.securityMarkingList = response.data
    })
    this.$http.get('/openstorefront/api/v1/resource/lookuptypes/ResourceType').then(response => {
      this.resourceType = response.data
    })
    this.$http.get(`/openstorefront/api/v1/resource/components/tags`).then(response => {
      this.tagsList = response.data.sort((e1, e2) => e1.text > e2.text)
    })
    this.$http.get('/openstorefront/api/v1/resource/lookuptypes/ContactType').then(response => {
      this.contactTypeList = response.data
    })
    this.setupAutoSave()
  },
  data: () => ({
    // NOTE: Server supports more but sometimes prettifies the html which means this needs to be a smaller value
    MAX_DESCRIPTION_LENGTH: 64000,
    saving: false,
    timeLastSaved: null,
    saveTimer: null,
    submitText: 'Submit',
    isChangeRequest: false,
    submitting: false,
    submitConfirmDialog: false,
    savingAndClose: false,
    bypassLeaveConfirmation: false,
    // server validation errors
    errors: [],
    formValidation: false,
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
    media: [],
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
    currentImage: {
      img: '',
      file: null,
      caption: ''
    },
    resourceFile: {
      img: '',
      file: null,
      description: '',
      resourceType: ''
    },
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
    uploadMedia: false,
    uploadingResource: false,
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
      numberOnly: value => {
        // If the value is null, we don't care about validation, in this case
        if (value === null) {
          return true
        }
        // if the value is an array, iterate over it and check each selection
        if (Array.isArray(value)) {
          let valid = true
          value.forEach(e => {
            if (/\d+(\.\d+)?/.exec(e) === null || /\d+(\.\d+)?/.exec(e)[0] !== e) {
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
    },
    isFormValid() {
      return this.description !== '' && this.description.length <= this.MAX_DESCRIPTION_LENGTH && this.formValidation
    },
    entryTypeList() {
      let list = this.$store.state.componentTypeList
      return list.sort((a, b) => a.parentLabel > b.parentLabel)
    }
  },
  methods: {
    /**
     * Fetch and load submission data into the form
     * Note that attributes cannot be loaded until the entry type is loaded.
     *
     * @param {string} id - the submission component id
     */
    loadData(id) {
      this.$http
        .get(`/openstorefront/api/v1/resource/componentsubmissions/${id}`)
        .then(response => {
          let component = response.data.component
          let contacts = response.data.contacts
          let media = response.data.media
          let [resourceFiles, resourceLinks] = _.partition(response.data.resources, el => !!el.file)
          media.forEach(el => {
            el.loading = false
          })

          this.entryTitle = component.name
          this.entryType = component.componentType
          this.description = component.description
          this.organization = component.organization
          this.media = media
          this.resources.localFiles = resourceFiles
          this.resources.links = resourceLinks
          this.tags = response.data.tags
          this.savedAttributes = response.data.attributes

          if (_.head(contacts)) {
            this.primaryPOC = _.head(contacts)
          }
          this.contacts = _.tail(contacts)
        })
        .catch(e => {
          this.$toasted.error('There was a problem fetching data for this submission')
          console.error(e)
        })
    },
    /**
     * Applies the unit conversion factor to a number
     *
     * @param {string} value - the number to be converted
     * @param {number} conversionFactor - the factor to be divided by
     *
     * @example
     *
     *      let tenMeters = 10
     *      let convertedToCentimeters = convertNumber(tenMeters, 1/100)
     */
    convertNumber(value, conversionFactor) {
      let numValue = Number(value)
      if (!isNaN(numValue)) {
        numValue /= conversionFactor
        return String(numValue)
      } else {
        return value
      }
    },
    /**
     * Aggregate all the form data to submit to the server
     */
    getFormData() {
      let allAttributes = this.attributes.required.concat(this.attributes.suggested)
      let newAttributes = []
      allAttributes.forEach(el => {
        // get conversion factor
        let conversionFactor = 1
        el.attributeUnitList.forEach(unit => {
          if (unit.unit === el.selectedUnit) {
            conversionFactor = unit.conversionFactor
          }
        })

        // deal with multiple select and single select attributes
        if (Array.isArray(el.selectedCodes) && el.selectedCodes.length > 0) {
          el.selectedCodes.forEach(code => {
            let codeValue = code.code ? code.code : code
            newAttributes.push({
              componentAttributePk: {
                userCreated: code.userCreated,
                attributeType: el.attributeType,
                attributeCode: this.convertNumber(codeValue, conversionFactor)
              },
              preferredUnit: el.selectedUnit
            })
          })
        } else if (typeof el.selectedCodes === 'string' && el.selectedCodes !== '') {
          newAttributes.push({
            componentAttributePk: {
              userCreated: el.userCreated,
              attributeType: el.attributeType,
              attributeCode: this.convertNumber(el.selectedCodes, conversionFactor)
            }
          })
        } else if (typeof el.selectedCodes === 'object' && el.selectedCodes && !Array.isArray(el.selectedCodes)) {
          newAttributes.push({
            componentAttributePk: {
              userCreated: el.userCreated,
              attributeType: el.attributeType,
              attributeCode: this.convertNumber(el.selectedCodes.code, conversionFactor)
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
        resources: this.resources.links,
        tags: this.tags,
        contacts: [this.primaryPOC].concat(this.contacts)
      }
    },
    loadSavedAttributes(topAttribute) {
      // load saved attributes
      this.savedAttributes.forEach(attribute => {
        if (attribute.componentAttributePk.attributeType === topAttribute.attributeType) {
          // get the attribute label
          // set the default label to the code
          let label = attribute.componentAttributePk.attributeCode
          topAttribute.codes.forEach(el => {
            if (el.code === attribute.componentAttributePk.attributeCode) {
              label = el.label
            }
          })
          if (Array.isArray(topAttribute.selectedCodes)) {
            topAttribute.selectedCodes.push({
              code: attribute.componentAttributePk.attributeCode,
              label: label
            })
          } else {
            topAttribute.selectedCodes = {
              code: attribute.componentAttributePk.attributeCode,
              label: label
            }
          }
        }
      })
    },
    /**
     * Fetch and load the suggested and required attributes for a given entry type
     */
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
            // set default selected unit value
            e.selectedUnit = e.attributeUnit
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

            this.loadSavedAttributes(e)

            // Set up unit stuff
            if (e.attributeUnitList) {
              e.attributeUnitList = e.attributeUnitList.filter(e => e.unit !== undefined)
              if (e.attributeUnitList.length === 0) {
                e.attributeUnit = ''
              }
            }
          })
        })
      this.$http
        .get(`/openstorefront/api/v1/resource/attributes/optional?componentType=${this.entryType}`)
        .then(response => {
          this.attributes.suggested = response.data.filter(e => e.attributeType !== 'MISSINGATTRIBUTE')
          this.attributes.suggested.forEach(e => {
            // set default selected unit value
            e.selectedUnit = e.attributeUnit
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

            this.loadSavedAttributes(e)
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
      this.bypassLeaveConfirmation = true
      this.submitConfirmDialog = false
      this.submitting = true
      this.save(() => {
        this.$http
          .put(`/openstorefront/api/v1/resource/componentsubmissions/${this.id}/submit`, this.getFormData())
          .then(response => {
            if (response.data && response.data.success === false) {
              this.errors = response.data.errors.entry
              this.$toasted.error('There was an error when submitting! Changes have not been submitted.')
            }
            if (response.data && response.data.component) {
              this.errors = []
            }
          })
          .catch(e => {
            this.$toasted.error('There was a problem submitting this entry')
            console.error(e)
          })
          .finally(() => {
            this.submitting = false
            this.$router.push({ name: 'Submissions' })
          })
      }, 'Submission Submitted')
    },
    saveAndClose() {
      this.savingAndClose = true
      if (this.isFormValid) {
        this.bypassLeaveConfirmation = true
        this.save(() => {
          this.savingAndClose = false
          this.$router.push({ name: 'Submissions' })
        })
      } else {
        this.savingAndClose = false
        this.$router.push({ name: 'Submissions' })
      }
    },
    save(callback, toastMessage, showToast) {
      this.saving = true
      let formData = this.getFormData()

      // get all user created codes
      let userCreatedAttributes = formData.attributes.filter(el => !!el.componentAttributePk.userCreated)
      let createdCodeList = []
      userCreatedAttributes.forEach(el => {
        createdCodeList.push({
          attributeCodeLabel: el.componentAttributePk.attributeCode,
          attributeType: el.componentAttributePk.attributeType
        })
      })

      this.$http
        .post('/openstorefront/api/v1/resource/attributes/attributetypes/usercodes', {
          userAttributes: createdCodeList
        })
        .then(response => {
          if (response.data.error) {
            console.error('Server returned error when getting attribute usercodes', response.data.error)
          }
          if (response.data && !response.data.error) {
            // update the form with newly created attributes to attach to submission
            if (Array.isArray(response.Data)) {
              formData.attributes.concat(response.data)
            }
          }
        })
        .catch(e => {
          this.$toasted.error('There was a problem saving attributes')
          console.error(e)
        })
        .finally(() => {
          if (this.id && this.id !== 'new') {
            this.$http
              .put(`/openstorefront/api/v1/resource/componentsubmissions/${this.id}`, formData)
              .then(response => {
                if (response.data && response.data.success === false) {
                  this.savingAndClose = false
                  this.submitting = false
                  this.errors = response.data.errors.entry
                  this.$toasted.error('There was an error when saving! Changes have not been saved.')
                } else if (response.data && response.data.component) {
                  this.errors = []
                  this.timeLastSaved = new Date()
                  if (showToast) this.$toasted.success(toastMessage || 'Submission Saved')

                  if (callback) {
                    callback()
                  }
                } else {
                  console.error(
                    `Recieved unexpected response from server on put call to componentsubmissions/${this.id}`,
                    response.data
                  )
                }
              })
              .catch(e => {
                this.$toasted.error('There was a problem saving the submission')
                console.error(e)
              })
              .finally(() => {
                this.saving = false
              })
          } else {
            this.$http
              .post('/openstorefront/api/v1/resource/componentsubmissions', formData)
              .then(response => {
                if (response.data && response.data.success === false) {
                  this.savingAndClose = false
                  this.submitting = false
                  this.errors = response.data.errors.entry
                  this.$toasted.error('There was an error when saving! Changes have not been saved.')
                } else if (response.data && response.data.component) {
                  this.errors = []
                  this.id = response.data.component.componentId
                  this.$router.replace(`${this.id}`)
                  this.timeLastSaved = new Date()
                  this.$toasted.success('Submission Saved')

                  if (callback) {
                    callback()
                  }
                } else {
                  console.error(
                    `Recieved unexpected response from server on post call to componentsubmissions`,
                    response.data
                  )
                }
              })
              .catch(e => {
                this.$toasted.error('There was a problem saving the submission')
                console.error(e)
              })
              .finally(() => {
                this.saving = false
              })
          }
        })
    },
    attachResource() {
      this.uploadingResource = true
      let formData = new FormData()
      formData.append('description', this.resourceFile.description)
      formData.append('file', this.resourceFile.file)
      formData.append('resourceType', this.resourceFile.resourceType)
      formData.append('mimeType', this.resourceFile.file.type)

      if (this.id) {
        this.$http
          .post(`/openstorefront/api/v1/resource/componentsubmissions/${this.id}/attachresource`, formData, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          })
          .then(response => {
            if (response.data && response.data.success === false) {
              this.errors = response.data.errors.entry
            }
            if (response.data) {
              this.resources.localFiles.push(response.data)
              // reset the image form
              this.resourceFile = {
                file: null,
                description: '',
                resourceType: ''
              }
            }
          })
          .catch(e => {
            this.$toasted.error('There was a problem attaching a resource to the submission')
            console.error(e)
          })
          .finally(() => {
            this.uploadingResource = false
          })
      }
    },
    removeResource(resource) {
      // TODO: detach image from submission after confirmation
      let resourceId = resource.resourceId
      resource.loading = true
      this.$http
        .delete(`/openstorefront/api/v1/resource/componentsubmissions/${this.id}/resources/${resourceId}/force`)
        .then(response => {
          this.$toasted.show('Deleted attached resource from submission')
          this.resources.localFiles = this.resources.localFiles.filter(el => el.resourceId !== resourceId)
          resource.loading = false
        })
        .catch(e => {
          resource.loading = false
          console.error('Problem with deleting attached image from submission', e)
          this.$toasted.error('Problem with deleting attached image from submission')
        })
    },
    lookupType(resourceTypeCode) {
      let result = ''
      this.resourceType.forEach(el => {
        if (el.code === resourceTypeCode) {
          result = el.description
        }
      })
      return result
    },
    attachMedia() {
      this.uploadMedia = true
      let formData = new FormData()
      formData.append('caption', this.currentImage.caption)
      formData.append('file', this.currentImage.file)
      formData.append('mediaTypeCode', MEDIA_TYPE_CODE.IMAGE)
      formData.append('mimeType', this.currentImage.file.type)

      if (this.id) {
        this.$http
          .post(`/openstorefront/api/v1/resource/componentsubmissions/${this.id}/attachmedia`, formData, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          })
          .then(response => {
            if (response.data && response.data.success === false) {
              this.errors = response.data.errors.entry
            }
            if (response.data) {
              this.media.push(response.data)
              // reset the image form
              this.currentImage = {
                img: '',
                file: null,
                caption: ''
              }
            }
          })
          .catch(e => {
            this.$toasted.error('There was a problem attaching media to the submission')
            console.error(e)
          })
          .finally(() => {
            this.uploadMedia = false
          })
      }
    },
    removeImage(media) {
      // TODO: detach image from submission after confirmation
      let mediaId = media.componentMediaId
      media.loading = true
      this.$http
        .delete(`/openstorefront/api/v1/resource/componentsubmissions/${this.id}/media/${mediaId}/force`)
        .then(response => {
          this.$toasted.show('Deleted attached image from submission')
          this.media = this.media.filter(el => el.componentMediaId !== mediaId)
          media.loading = false
        })
        .catch(e => {
          media.loading = false
          console.error('Problem with deleting attached image from submission', e)
          this.$toasted.error('Problem with deleting attached image from submission')
        })
    },
    imageChange(index) {
      let e = this.currentImage
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
    addLink() {
      this.resources.links.push({ resourceType: '', link: '', description: '', securityMarking: '' })
    },
    removeLink(index) {
      this.resources.links.splice(index, 1)
    },
    addTag() {
      this.tagsList.push({ text: this.tagSearchText })
      this.tags.push({ text: this.tagSearchText })
      this.addNewTag(this.tagSearchText)
      this.tagSearchText = ''
    },
    addNewTag(text) {
      this.$http
        .post(`/openstorefront/api/v1/resource/components/${this.id}/tags`, {
          text: text
        })
        .then(res => {
          // fetch all tags for submission
          // inefficient but it guarantees that the tagIds are valid
          this.getTags()
        })
        .catch(e => {
          this.$toasted.error('There was a problem adding a tag to the submission')
          console.error('Problem adding tag: ', text)
        })
    },
    addContact() {
      this.contacts.push({ firstName: '', lastName: '', contactType: '', organization: '', email: '', phone: '' })
    },
    removeContact(index) {
      this.contacts.splice(index, 1)
    },
    setupAutoSave() {
      this.saveTimer = setInterval(() => {
        if (this.isFormValid) {
          this.save()
        }
      }, 30000)
    },
    getTags() {
      this.$http
        .get(`/openstorefront/api/v1/resource/components/${this.id}/tagsview`)
        .then(res => {
          // update the tagIds for all attached tags on the submission
          this.tags.forEach(el => {
            if (Array.isArray(res.data)) {
              res.data.forEach(el2 => {
                if (el.text === el2.text) {
                  el.tagId = el2.tagId
                }
              })
            }
          })
        })
        .catch(e => {
          this.$toasted.error('There was a problem fetching tags for the submission')
          console.error('problem fetching tags for the submission')
        })
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
    },
    tags: function(newVal, oldVal) {
      let newTag = _.differenceBy(newVal, oldVal, 'text')
      let removedTag = _.differenceBy(oldVal, newVal, 'text')
      if (newTag && newTag.length > 0) {
        // add new tag
        this.addNewTag(newTag[0].text)
      }
      if (removedTag && removedTag.length > 0) {
        // add new tag
        this.$http
          .delete(`/openstorefront/api/v1/resource/components/${this.id}/tags/${removedTag[0].tagId}`)
          .then(res => {})
          .catch(e => {
            this.$toasted.error('There was a problem deleteing a tag from the submission')
            console.error('Problem deleting tag: ', removedTag)
          })
      }
    },
    isFormValid: function(newVal, oldVal) {
      if (newVal && this.id === null) {
        this.save()
      }
    }
  },
  beforeDestroy() {
    clearInterval(this.saveTimer)
  }
}
</script>

<style lang="scss" scoped>
$red: #c62828;

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
.image-warning {
  border: 1px solid $red;
  border-radius: 4px;
  position: relative;
}
.image-warning:last-child {
  border: 1px solid $red;
}
.image-warning::before {
  content: 'Image Not Attached';
  background: rgba(255, 255, 255, 0.75);
  color: $red;
  border-radius: 4px;
  position: absolute;
  padding: 4px 8px;
  top: 0;
  left: 0;
  z-index: 2;
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

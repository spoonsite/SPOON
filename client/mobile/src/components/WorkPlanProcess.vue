
<template lang="html">

  <section class="sme-approval-page">
     <v-card class="grey darken-3 white--text text-xs-center">
      <v-card-text>
        <h1 class="title">{{ componentData.name }}</h1>
      </v-card-text>
    </v-card>
    <v-alert type="warning" :value="invalidEntry"><strong>Unable to process workflow. The component has missing required attributes or has not been submitted. Please contact the owner of this entry or the admin.</strong></v-alert>
    <div v-if="steps.length > 0">
      <v-stepper v-model="currentStep" vertical>
        <template
          v-for="(step, index) in steps"
          >

          <v-stepper-step :key="step.step" :complete="currentStep > step.step" :step="index + 1">
            {{ step.name }}
            <small>{{ step.summary }}</small>
          </v-stepper-step>

          <v-stepper-content :key="step.name" :step="index + 1">
            <div class="grey lighten-3 mb-4 pa-2"><strong>{{ step.description }}</strong></div>
            <v-btn block color="primary" @click="viewEntry()">View Entry</v-btn>
            <v-layout justify-space-between>
              <v-flex xs-6>
                <v-btn
                  v-if="showNext()"
                  color="primary"
                  @click="nextStep"
                  :loading="continueLoading"
                  :disabled="continueLoading || invalidEntry"><v-icon class="mr-2" small>fa-arrow-circle-down</v-icon>Next</v-btn>
              </v-flex>
              <v-flex xs-6>
                <v-btn
                  style="float: right;"
                  v-if="showBack()"
                  color="primary"
                  @click="prevStep"
                  :loading="prevLoading"
                  :disabled="prevLoading || invalidEntry"><v-icon class="mr-2" small>fa-arrow-circle-up</v-icon>Back</v-btn>
              </v-flex>
            </v-layout>
          </v-stepper-content>

        </template>

      </v-stepper>
    </div>
    <LoadingOverlay v-model="isLoading"></LoadingOverlay>
  </section>

</template>

<script lang="js">
import LoadingOverlay from './subcomponents/LoadingOverlay';
import router from '../router/index';

export default {
  name: 'work-plan-process',
  props: [],
  components: {
    LoadingOverlay,
    router
  },
  mounted () {
    this.isLoading = true;
    if (this.$route.params.id) {
      this.id = this.$route.params.id;
    }

    this.getWorkLink();
  },
  data () {
    return {
      componentData: {},
      continueLoading: false,
      currentStep: 0,
      id: '',
      isLoading: false,
      prevLoading: false,
      steps: [],
      invalidEntry: false,
      workLink: {}
    };
  },
  methods: {
    validateEntry () {
      this.isLoading = true;
      this.$http.get(`/openstorefront/api/v1/resource/components/${this.componentData.componentId}/validate`)
        .then(res => {
          if (res.data.errors) {
            this.invalidEntry = false;
          }
        })
        .finally(() => {
          this.isLoading = false;
        });
    },
    getWorkLink () {
      this.$http.get(`/openstorefront/api/v1/resource/workplans/worklinks/${this.id}`)
        .then(res => {
          this.workLink = res.data;
        })
        .finally(() => {
          this.getComponentData();
          this.setSteps();
        });
    },
    getComponentData () {
      this.$http.get(`/openstorefront/api/v1/resource/components/${this.workLink.componentId}`)
        .then(response => {
          this.componentData = response.data;
        })
        .finally(() => {
          this.validateEntry();
          this.isLoading = false;
        });
    },
    nextStep () {
      this.continueLoading = true;
      this.$http.put(`/openstorefront/api/v1/resource/workplans/${this.workLink.workPlanId}/worklinks/${this.workLink.workPlanLinkId}/nextstep`)
        .then(res => {
          this.workLink = res.data;
        })
        .finally(() => {
          this.setSteps();
          this.continueLoading = false;
        });
    },
    prevStep () {
      this.prevLoading = true;
      this.$http.put(`/openstorefront/api/v1/resource/workplans/${this.workLink.workPlanId}/worklinks/${this.workLink.workPlanLinkId}/previousstep`)
        .then(res => {
          this.workLink = res.data;
        })
        .finally(() => {
          this.setSteps();
          this.prevLoading = false;
        });
    },
    setSteps () {
      this.steps = this.workLink.steps;
      for (let i = 0; i < this.steps.length; i++) {
        if (this.workLink.currentStep.workPlanStepId === this.steps[i].workPlanStepId) {
          this.currentStep = i + 1;
          break;
        }
      }
    },
    showBack () {
      return (this.currentStep > 1);
    },
    showNext () {
      return (this.currentStep < this.steps.length);
    },
    viewEntry () {
      router.push({
        name: 'Entry Detail',
        params: {
          id: this.workLink.componentId
        }
      });
    }

  }
};
</script>

<style scoped lang="scss">
  .work-plan-process {

  }
</style>

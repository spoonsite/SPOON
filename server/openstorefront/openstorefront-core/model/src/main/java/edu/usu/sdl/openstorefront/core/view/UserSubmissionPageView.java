/*
 * Copyright 2019 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.usu.sdl.openstorefront.core.view;

import java.util.List;

import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.entity.WorkPlan;

/**
 *
 * @author gfowler
 */
public class UserSubmissionPageView
{
    @ConsumeField
    private List<ComponentWorkPlanId> componentWorkPlanIds;

    @ConsumeField
    private List<WorkPlan> workPlans;

    public UserSubmissionPageView(){}

    public UserSubmissionPageView(List<ComponentWorkPlanId> componentWorkPlanIds, List<WorkPlan> workPlans){
        this.componentWorkPlanIds = componentWorkPlanIds;
        this.workPlans = workPlans;
    }

    public List<ComponentWorkPlanId> getComponentWorkPlanIds() {
        return componentWorkPlanIds;
    }

    public void setComponentWorkPlanIds(List<ComponentWorkPlanId> componentWorkPlanIds) {
        this.componentWorkPlanIds = componentWorkPlanIds;
    }

    public List<WorkPlan> getWorkPlans() {
        return workPlans;
    }

    public void setWorkPlans(List<WorkPlan> workPlans) {
        this.workPlans = workPlans;
    }
}
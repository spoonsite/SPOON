/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service;

import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.service.api.UserService;
import edu.usu.sdl.openstorefront.service.query.QueryByExample;
import edu.usu.sdl.openstorefront.storage.model.Component;
import edu.usu.sdl.openstorefront.storage.model.TestEntity;
import edu.usu.sdl.openstorefront.storage.model.UserProfile;
import edu.usu.sdl.openstorefront.storage.model.UserWatch;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Handles all user business logic
 *
 * @author dshurtleff
 */
public class UserServiceImpl
        extends ServiceProxy
        implements UserService
{

    private static final Logger log = Logger.getLogger(UserServiceImpl.class.getName());

    @Override
    public List<UserWatch> getWatches(String userId)
    {
        UserWatch temp;
        temp = new UserWatch();
        temp.setUsername("Username"/*TODO: set this to the real username*/);
        temp.setActiveStatus(TestEntity.ACTIVE_STATUS);
        return persistenceService.queryByExample(new QueryByExample(temp));
    }

    /**
     *
     * @param watch
     * @return
     */
    @Override
    public UserWatch addWatch(UserWatch watch)
    {
        watch.setCreateDts(new Date());
        watch.setCreateUser(watch.getUsername());
        watch.setUpdateDts(new Date());
        watch.setUpdateUser(watch.getUsername());
        watch.setUserWatchId(persistenceService.generateId());
        watch.setLastViewDts(new Date());
        return persistenceService.persist(watch);
    }

    /**
     *
     * @param watch
     * @return
     */
    @Override
    public UserWatch updateWatch(UserWatch watch)
    {
        UserWatch temp = persistenceService.findById(UserWatch.class, watch.getUserWatchId());
        if (!watch.getNotifyFlg().equals(temp.getNotifyFlg()))
        {
            temp.setNotifyFlg(watch.getNotifyFlg());
        }
        if (!watch.getActiveStatus().equals(temp.getActiveStatus()))
        {
            temp.setActiveStatus(watch.getActiveStatus());
        }
        if (!watch.getUpdateUser().equals(temp.getUpdateUser()))
        {
            temp.setUpdateUser(watch.getUpdateUser());
        }
        if (!watch.getLastViewDts().equals(temp.getLastViewDts()))
        {
            temp.setLastViewDts(watch.getLastViewDts());
        }
        if (!watch.getUsername().equals(temp.getUsername()))
        {
            temp.setUsername(watch.getUsername());
        }
        temp.setUpdateDts(new Date());
        temp.setUpdateUser(watch.getUsername());
        return persistenceService.persist(temp);
    }

    @Override
    public Boolean deleteWatch(String watchId)
    {
        UserWatch temp = persistenceService.findById(UserWatch.class, watchId);
        persistenceService.delete(temp);
        return Boolean.TRUE;
    }

    @Override
    public UserProfile getUserProfile(String userId)
    {
        try
        {
            UserProfile profile = persistenceService.findById(UserProfile.class, userId);
            if (profile == null)
            {
                /*TODO: Here we need to create a new profile if it doesn't exist...*/
                profile = new UserProfile();
                profile.setActiveStatus(TestEntity.ACTIVE_STATUS);
                profile.setAdmin(Boolean.FALSE);
                profile.setCreateDts(new Date());
                profile.setCreateUser(userId);
                profile.setEmail("email@email.com");
                profile.setFirstName("First Name");
                profile.setLastName("Last Name");
                profile.setOrganization("Organization");
                profile.setUpdateDts(new Date());
                profile.setUpdateUser(userId);
                profile.setUserTypeCode("USER");
                profile.setUsername(userId);
                return persistenceService.persist(profile);
            }
            return profile;
        }
        catch (OpenStorefrontRuntimeException ex)
        {
            throw new OpenStorefrontRuntimeException("There was an error getting the user profile");
        }
    }

    @Override
    public UserProfile saveUserProfile(UserProfile user)
    {
        UserProfile temp = persistenceService.findById(UserProfile.class, user.getUsername());
        if (!user.getActiveStatus().equals(temp.getActiveStatus()))
        {
            temp.setActiveStatus(user.getActiveStatus());
        }

        if (!user.getEmail().equals(temp.getEmail()))
        {
            temp.setEmail(user.getEmail());
        }

        if (!user.getFirstName().equals(temp.getFirstName()))
        {
            temp.setFirstName(user.getFirstName());
        }

        if (!user.getLastName().equals(temp.getLastName()))
        {
            temp.setLastName(user.getLastName());
        }

        if (!user.getOrganization().equals(temp.getOrganization()))
        {
            temp.setOrganization(user.getOrganization());
        }

        if (!user.getUserTypeCode().equals(temp.getUserTypeCode()))
        {
            temp.setUserTypeCode(user.getUserTypeCode());
        }

        if (!user.getUsername().equals(temp.getUsername()))
        {
            temp.setUsername(user.getUsername());
        }
        return persistenceService.persist(temp);
    }

    @Override
    public List<Component> getRecentlyViewed(String userId)
    {
        
        //CONTINUE HERE... left off after work on friday.
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.util.EntityUtil;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.validation.BlankSantizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 * @param <T>
 */
@APIDescription("Root entity for all top-level entities")
public abstract class StandardEntity<T>
		extends BaseEntity<T>
{

	private static final long serialVersionUID = 1L;

	public static final Logger LOG = Logger.getLogger(StandardEntity.class.getName());

	public static final String ACTIVE_STATUS = "A";
	public static final String INACTIVE_STATUS = "I";
	public static final String PENDING_STATUS = "P";

	public static final String FIELD_ACTIVE_STATUS = "activeStatus";
	public static final String FIELD_CREATE_DTS = "createDts";
	public static final String FIELD_UPDATE_DTS = "updateDts";

	@Sanitize({TextSanitizer.class, BlankSantizer.class})
	@ConsumeField
	@ValidValueType(value = {}, lookupClass = SecurityMarkingType.class)
	@APIDescription("Security Classification")
	@FK(SecurityMarkingType.class)
	private String securityMarkingType;

	@Sanitize({TextSanitizer.class, BlankSantizer.class})
	@ConsumeField
	@ValidValueType(value = {}, lookupClass = DataSensitivity.class)
	@APIDescription("Data Sensitivity")
	@FK(DataSensitivity.class)
	private String dataSensitivity;

	@NotNull
	@ValidValueType({"A", "I", "P"})
	private String activeStatus;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_USERNAME)
	private String createUser;

	@NotNull
	private Date createDts;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_USERNAME)
	private String updateUser;

	@NotNull
	private Date updateDts;

	private Boolean adminModified;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public StandardEntity()
	{
	}

	/**
	 * Override to do specific handling
	 *
	 * @return
	 */
	public String entityOwner()
	{
		return getCreateUser();
	}

	protected Set<String> excludedChangeFields()
	{
		Set<String> excludedFields = new HashSet<>();

		excludedFields.add("activeStatus");
		excludedFields.add("createUser");
		excludedFields.add("createDts");
		excludedFields.add("updateUser");
		excludedFields.add("updateDts");
		excludedFields.add("adminModified");
		excludedFields.add("handler");
		excludedFields.add("storageVersion");

		return excludedFields;
	}

	@Override
	public int compareTo(T o)
	{
		if (o != null) {
			if (o instanceof StandardEntity) {
				return ReflectionUtil.compareObjects(getActiveStatus(), ((StandardEntity) o).getActiveStatus());
			} else {
				return -1;
			}
		} else {
			return -1;
		}
	}

	public <T extends StandardEntity> void updateFields(T entity)
	{
		this.setSecurityMarkingType(entity.getSecurityMarkingType());
		this.setDataSensitivity(entity.getDataSensitivity());
		if (entity.getActiveStatus() != null) {
			this.setActiveStatus(entity.getActiveStatus());
		}

		this.populateBaseUpdateFields();

		if (StringUtils.isNotBlank(entity.getUpdateUser())) {
			setUpdateUser(entity.getUpdateUser());
		}

	}

	public void populateBaseUpdateFields()
	{
		if (StringUtils.isBlank(getActiveStatus())) {
			setActiveStatus(ACTIVE_STATUS);
		}
		setUpdateDts(TimeUtil.currentDate());
		setUpdateUser(SecurityUtil.getCurrentUserName());

		if (getAdminModified() == null) {
			setAdminModified(SecurityUtil.isEntryAdminUser());
		}
	}

	public void populateBaseCreateFields()
	{
		if (StringUtils.isBlank(getActiveStatus())) {
			setActiveStatus(ACTIVE_STATUS);
		}
		setCreateDts(TimeUtil.currentDate());
		setUpdateDts(TimeUtil.currentDate());

		if (StringUtils.isBlank(getCreateUser())) {
			setCreateUser(SecurityUtil.getCurrentUserName());
		}
		if (StringUtils.isBlank(getUpdateUser())) {
			setUpdateUser(SecurityUtil.getCurrentUserName());
		}
		if (getAdminModified() == null) {
			setAdminModified(SecurityUtil.isEntryAdminUser());
		}
	}

	/**
	 * This only works for simple saves (non-transactional) And non-composite
	 * keys;
	 *
	 * @return Newly Saved or Updated entity
	 */
	public T save()
	{
		Service service = ServiceProxyFactory.getServiceProxy();

		T existing = service.getPersistenceService().findById((Class<T>) this.getClass(), EntityUtil.getPKFieldValue(this));
		if (existing != null) {
			((StandardEntity) existing).updateFields(this);
			existing = (T) service.getPersistenceService().persist(((StandardEntity) existing));
		} else {
			Field pkField = EntityUtil.getPKField(this);
			PK pk = pkField.getAnnotation(PK.class);
			if (pk != null && pk.generated()) {
				//perserve id if already set
				String idValue = EntityUtil.getPKFieldValue(this);
				if (StringUtils.isBlank(idValue)) {
					EntityUtil.updatePKFieldValue(this, service.getPersistenceService().generateId());
				}
			}
			this.populateBaseCreateFields();
			existing = (T) service.getPersistenceService().persist(this);
		}
		return existing;
	}

	private T getDBEntity()
	{
		Service service = ServiceProxyFactory.getServiceProxy();
		T existing = service.getPersistenceService().findById((Class<T>) this.getClass(), EntityUtil.getPKFieldValue(this));
		return existing;
	}

	/**
	 * This will inactive the current entity and the backing record in the DB If
	 * there is no backing record it just changes the entity. This is only works
	 * for simple cases (non-transactional) and non-composite Keys.
	 *
	 * @return true if backing record was updated.
	 */
	public boolean inactivate()
	{
		boolean updatedRecord = false;
		Service service = ServiceProxyFactory.getServiceProxy();
		setActiveStatus(INACTIVE_STATUS);
		StandardEntity existing = (StandardEntity) getDBEntity();
		if (existing != null && !INACTIVE_STATUS.equals(existing.getActiveStatus())) {
			existing.setActiveStatus(INACTIVE_STATUS);
			existing.populateBaseUpdateFields();
			service.getPersistenceService().persist(existing);
			updatedRecord = true;
		} else if (LOG.isLoggable(Level.FINER)) {
			String id = EntityUtil.getPKFieldValue(this);
			LOG.log(Level.FINER, MessageFormat.format("Unable to find a db record for {0} entity.  Id: {1}", new Object[]{this.getClass().getSimpleName(), id}));
		}
		return updatedRecord;
	}

	/**
	 * This is hard delete of the backing record if found. This is only works
	 * for simple cases (non-transactional) and non-composite Keys.
	 *
	 * @return true if backing record was deleted
	 */
	public boolean delete()
	{
		boolean deletedRecord = false;

		Service service = ServiceProxyFactory.getServiceProxy();
		StandardEntity existing = (StandardEntity) getDBEntity();
		if (existing != null) {
			service.getPersistenceService().delete(existing);
			deletedRecord = true;
		}

		return deletedRecord;
	}

	public String getActiveStatus()
	{
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus)
	{
		this.activeStatus = activeStatus;
	}

	public String getCreateUser()
	{
		return createUser;
	}

	public void setCreateUser(String createUser)
	{
		this.createUser = createUser;
	}

	public Date getCreateDts()
	{
		return createDts;
	}

	public void setCreateDts(Date createDts)
	{
		this.createDts = createDts;
	}

	public String getUpdateUser()
	{
		return updateUser;
	}

	public void setUpdateUser(String updateUser)
	{
		this.updateUser = updateUser;
	}

	public Date getUpdateDts()
	{
		return updateDts;
	}

	public void setUpdateDts(Date updateDts)
	{
		this.updateDts = updateDts;
	}

	public Boolean getAdminModified()
	{
		return adminModified;
	}

	public void setAdminModified(Boolean adminModified)
	{
		this.adminModified = adminModified;
	}

	public String getSecurityMarkingType()
	{
		return securityMarkingType;
	}

	public void setSecurityMarkingType(String securityMarkingType)
	{
		this.securityMarkingType = securityMarkingType;
	}

	public String getDataSensitivity()
	{
		return dataSensitivity;
	}

	public void setDataSensitivity(String dataSensitivity)
	{
		this.dataSensitivity = dataSensitivity;
	}

}

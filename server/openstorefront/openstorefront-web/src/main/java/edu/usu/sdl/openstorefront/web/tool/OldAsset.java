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

package edu.usu.sdl.openstorefront.web.tool;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OldAsset
	implements Serializable
{
	private long id;
	private Date editedDate;
	private String description;
	private String approvalStatus;
	private List<String> techPocs = new ArrayList<>();
	private Date createDate;
	private String versionName;
	private OldAssetUser createdBy;
	private String uuid;
	private List<OldAssetUser> owners = new ArrayList<>();
	private List<OldAssetScreenshot> screenshots = new ArrayList<>();
	private String imageLargeUrl;
	private String imageSmallUrl;
	private OldAssetState state;
	private int totalComments;
	private String organization;
	private String title;
	private String requirements;
	private OldAssetActivity lastActivity;
	private String releaseDate;
	private OldAssetUser editedBy;
	private OldAssetType types;
	private String dependencies;
	private List<OldAssetDocs> docUrls = new ArrayList<>();
	private Date approvalDate;
	private List<OldAssetMetaData> customFields = new ArrayList<>();
	private List<OldAssetCategory> categories = new ArrayList<>();	
	private String installUrl;
	
	
	public OldAsset()
	{
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public Date getEditedDate()
	{
		return editedDate;
	}

	public void setEditedDate(Date editedDate)
	{
		this.editedDate = editedDate;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getApprovalStatus()
	{
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus)
	{
		this.approvalStatus = approvalStatus;
	}

	public List<String> getTechPocs()
	{
		return techPocs;
	}

	public void setTechPocs(List<String> techPocs)
	{
		this.techPocs = techPocs;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}

	public String getVersionName()
	{
		return versionName;
	}

	public void setVersionName(String versionName)
	{
		this.versionName = versionName;
	}

	public OldAssetUser getCreatedBy()
	{
		return createdBy;
	}

	public void setCreatedBy(OldAssetUser createdBy)
	{
		this.createdBy = createdBy;
	}

	public String getUuid()
	{
		return uuid;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	public List<OldAssetUser> getOwners()
	{
		return owners;
	}

	public void setOwners(List<OldAssetUser> owners)
	{
		this.owners = owners;
	}

	public String getImageLargeUrl()
	{
		return imageLargeUrl;
	}

	public void setImageLargeUrl(String imageLargeUrl)
	{
		this.imageLargeUrl = imageLargeUrl;
	}

	public String getImageSmallUrl()
	{
		return imageSmallUrl;
	}

	public void setImageSmallUrl(String imageSmallUrl)
	{
		this.imageSmallUrl = imageSmallUrl;
	}

	public OldAssetState getState()
	{
		return state;
	}

	public void setState(OldAssetState state)
	{
		this.state = state;
	}

	public int getTotalComments()
	{
		return totalComments;
	}

	public void setTotalComments(int totalComments)
	{
		this.totalComments = totalComments;
	}

	public String getOrganization()
	{
		return organization;
	}

	public void setOrganization(String organization)
	{
		this.organization = organization;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getRequirements()
	{
		return requirements;
	}

	public void setRequirements(String requirements)
	{
		this.requirements = requirements;
	}

	public OldAssetActivity getLastActivity()
	{
		return lastActivity;
	}

	public void setLastActivity(OldAssetActivity lastActivity)
	{
		this.lastActivity = lastActivity;
	}

	public String getReleaseDate()
	{
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate)
	{
		this.releaseDate = releaseDate;
	}

	public OldAssetUser getEditedBy()
	{
		return editedBy;
	}

	public void setEditedBy(OldAssetUser editedBy)
	{
		this.editedBy = editedBy;
	}

	public OldAssetType getTypes()
	{
		return types;
	}

	public void setTypes(OldAssetType types)
	{
		this.types = types;
	}

	public String getDependencies()
	{
		return dependencies;
	}

	public void setDependencies(String dependencies)
	{
		this.dependencies = dependencies;
	}

	public List<OldAssetDocs> getDocUrls()
	{
		return docUrls;
	}

	public void setDocUrls(List<OldAssetDocs> docUrls)
	{
		this.docUrls = docUrls;
	}

	public Date getApprovalDate()
	{
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate)
	{
		this.approvalDate = approvalDate;
	}

	public List<OldAssetMetaData> getCustomFields()
	{
		return customFields;
	}

	public void setCustomFields(List<OldAssetMetaData> customFields)
	{
		this.customFields = customFields;
	}

	public List<OldAssetCategory> getCategories()
	{
		return categories;
	}

	public void setCategories(List<OldAssetCategory> categories)
	{
		this.categories = categories;
	}

	public List<OldAssetScreenshot> getScreenshots()
	{
		return screenshots;
	}

	public void setScreenshots(List<OldAssetScreenshot> screenshots)
	{
		this.screenshots = screenshots;
	}

	public String getInstallUrl()
	{
		return installUrl;
	}

	public void setInstallUrl(String installUrl)
	{
		this.installUrl = installUrl;
	}
	
}

package edu.usu.sdl.openstorefront.web.test;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.core.entity.UserTypeCode;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 * Base Test Case for all container tests
 *
 * @author dshurtleff
 */
public abstract class BaseTestCase
{

	private static final Logger LOG = Logger.getLogger(BaseTestCase.class.getName());

	protected static final String TEST_USER = "TEST-USER";
	protected static final String TEST_ORGANIZATION = "TEST-ORGANIZATION";
	private static final String TEST_COMPONENT_NAME = "AUTO-TEST-Component";

	protected boolean success;
	protected StringBuffer failureReason = new StringBuffer();
	protected StringBuffer results = new StringBuffer();
	protected ServiceProxy service = new ServiceProxy();
	protected List<CleanupTestData> cleanTestDataList = new ArrayList<>();

	public BaseTestCase()
	{
	}

	public abstract String getDescription();

	protected void initializeTest()
	{
	}

	public void runTest()
	{
		try {
			initializeTest();
			runInternalTest();
			if (failureReason.length() == 0) {
				success = true;
			}
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Test: " + getDescription() + " Fail Trace: ", e);
			failureReason.append(e);
		} finally {
			try {
				cleanupTest();
			} catch (Exception e) {
				LOG.log(Level.SEVERE, "Failed Cleanup Test: " + getDescription() + " Fail Trace: ", e);
				failureReason.append(e);
			}
		}
	}

	protected void cleanupTest()
	{
		cleanTestDataList.sort((a, b) -> {
			return Integer.compare(a.getOrder(), b.getOrder());
		});
		for (CleanupTestData cleanupTestData : cleanTestDataList) {
			try {
				cleanupTestData.cleanup();
			} catch (Exception e) {
				failureReason.append("Unable to cleanup data. <br> Message: ").append(e.getMessage());
				LOG.log(Level.SEVERE, "Test " + getDescription() + " Failed during clean Trace: <br>", e);
			}
		}

	}

	protected abstract void runInternalTest();

	public boolean isSuccess()
	{
		return success;
	}

	protected void addResultsLines(String... lines)
	{
		for (String line : lines) {
			results.append(line).append("<br>");
		}
	}

	protected void addFailLines(String... lines)
	{
		for (String line : lines) {
			failureReason.append(line).append("<br>");
		}
	}

	protected ComponentAll getTestComponent()
	{
		Component componentExample = new Component();
		componentExample.setName(TEST_COMPONENT_NAME);
		Component found = componentExample.find();

		ComponentAll componentAll;
		if (found != null) {
			componentAll = service.getComponentService().getFullComponent(found.getComponentId());
		} else {
			componentAll = new ComponentAll();
			Component component = new Component();
			component.setName(TEST_COMPONENT_NAME);
			component.setDescription("Test Description");
			component.setOrganization(TEST_ORGANIZATION);
			component.setApprovalState(ApprovalStatus.APPROVED);
			component.setGuid("5555555");
			component.setLastActivityDts(TimeUtil.currentDate());
			component.setActiveStatus(Component.ACTIVE_STATUS);

			ComponentType componentTypeExample = new ComponentType();
			List<ComponentType> componentTypes = componentTypeExample.findByExample();
			if (componentTypes.isEmpty()) {
				//TODO: should create a test one and then remove it.
				throw new OpenStorefrontRuntimeException("No Component type exist...add one");
			} else {
				component.setComponentType(componentTypes.get(0).getComponentType());
			}
			componentAll.setComponent(component);

			List<AttributeType> attributeTypes = service.getAttributeService().findRequiredAttributes(component.getComponentType(), false);
			for (AttributeType type : attributeTypes) {
				ComponentAttribute componentAttribute = new ComponentAttribute();
				componentAttribute.setCreateUser(TEST_USER);
				componentAttribute.setUpdateUser(TEST_USER);
				componentAttribute.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
				ComponentAttributePk componentAttributePk = new ComponentAttributePk();
				componentAttributePk.setAttributeType(type.getAttributeType());
				List<AttributeCode> attributeCodes = service.getAttributeService().findCodesForType(type.getAttributeType());
				componentAttributePk.setAttributeCode(attributeCodes.get(0).getAttributeCodePk().getAttributeCode());
				componentAttribute.setComponentAttributePk(componentAttributePk);
				componentAll.getAttributes().add(componentAttribute);
			}

			componentAll = service.getComponentService().saveFullComponent(componentAll);
		}

		final String componentIdToDelete = componentAll.getComponent().getComponentId();
		CleanupTestData cleanupTestData = new CleanupTestData()
		{
			@Override
			public void cleanup()
			{
				service.getComponentService().cascadeDeleteOfComponent(componentIdToDelete);
			}
		};
		cleanupTestData.setOrder(1);
		cleanTestDataList.add(cleanupTestData);

		return componentAll;
	}

	public UserProfile getTestUserProfile()
	{
		UserProfile userProfile = service.getUserService().getUserProfile(TEST_USER);
		if (userProfile == null) {
			results.append("Creating a new user profile").append("<br>");
			userProfile = new UserProfile();
			userProfile.setUsername(TEST_USER);
			userProfile.setFirstName("TEST FIRSTNAME");
			userProfile.setLastName("LASTNAME");
			userProfile.setOrganization(TEST_ORGANIZATION);
			userProfile.setEmail(getSystemEmail());
			userProfile.setUserTypeCode(UserTypeCode.END_USER);
			userProfile.setExternalGuid("5555-5555");
			userProfile.setCreateUser(TEST_USER);
			userProfile.setUpdateUser(TEST_USER);
			service.getUserService().saveUserProfile(userProfile, false);
		}

		CleanupTestData cleanupTestData = new CleanupTestData()
		{
			@Override
			public void cleanup()
			{
				results.append("Removing profile").append("<br>");
				service.getUserService().deleteProfile(TEST_USER);
			}
		};
		cleanTestDataList.add(cleanupTestData);

		return userProfile;
	}

	protected String getSystemEmail()
	{
		String systemEmail = PropertiesManager.getInstance().getValue(PropertiesManager.KEY_TEST_EMAIL);
		if (StringUtils.isBlank(systemEmail)) {
			throw new OpenStorefrontRuntimeException("Unable to find test email.", "Add/Update system property: " + PropertiesManager.KEY_TEST_EMAIL + " set a email for test.");
		}

		return systemEmail;
	}

	public void setSuccess(boolean success)
	{
		this.success = success;
	}

	public StringBuffer getFailureReason()
	{
		return failureReason;
	}

	public void setFailureReason(StringBuffer failureReason)
	{
		this.failureReason = failureReason;
	}

	public StringBuffer getResults()
	{
		return results;
	}

	public void setResults(StringBuffer results)
	{
		this.results = results;
	}

}

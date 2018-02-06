package edu.usu.sdl.openstorefront.dataupdater;

import au.com.bytecode.opencsv.CSVWriter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.apiclient.APIResponse;
import edu.usu.sdl.apiclient.ClientAPI;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.view.ComponentAdminView;
import edu.usu.sdl.openstorefront.core.view.ComponentAdminWrapper;
import edu.usu.sdl.openstorefront.core.view.ComponentContactView;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class EntrySubmissionReportUseCase
{

	@Test
	public void submissionReport()
	{
		ObjectMapper objectMapper = StringProcessor.defaultObjectMapper();

		System.out.println("Connecting to API...");
		ClientAPI clientAPI = new ClientAPI(objectMapper);
		clientAPI.connect("", "", "http://spoonsite.usurf.usu.edu/openstorefront"); //"https://spoonsite.com/openstorefront"  //Cert issue
		System.out.println("Connected");

		System.out.println("Pulling all components..");
		Map<String, String> params = new HashMap<>();
		params.put("approvalState", "ALL");

		APIResponse response = clientAPI.httpGet("/api/v1/resource/components/filterable", params);
		ComponentAdminWrapper componentWrapper = response.getList(new TypeReference<ComponentAdminWrapper>()
		{
		});
		System.out.println("Found: " + componentWrapper.getTotalNumber());

		System.out.println("Writing Report...");
		try (Writer writer = new FileWriter("/test/spoon/entrysubmission.csv")) {
			CSVWriter csvWriter = new CSVWriter(writer);

			csvWriter.writeNext(new String[]{
				"Report Period: 08/01/2017 - 01/31/2018"
			});

			csvWriter.writeNext(new String[]{
				"Entry Name",
				"Entry Type",
				"Entry Organization",
				"Approval Status",
				"Submission Date (Only exists if a non-admin)",
				"Submitter Name",
				"Submitter Email",
				"Submitter Organization",
				"Create Date",
				"Create User (Current Owner)",
				"Update Date",
				"Update User"
			});

			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

			Date startDate = sdf.parse("08/01/2017 00:00:00", new ParsePosition(0));
			Date endDate = sdf.parse("02/01/2018 00:00:00", new ParsePosition(0));

			for (ComponentAdminView view : componentWrapper.getComponents()) {

				if (view.getComponent().getCreateDts().after(startDate)
						&& view.getComponent().getCreateDts().before(endDate)) {

					System.out.println("Pulling contacts for: " + view.getComponent().getName() + "...");
					response = clientAPI.httpGet("/api/v1/resource/components/" + view.getComponent().getComponentId() + "/contacts/view", null);
					String submitterName = "";
					String submitterEmail = "";
					String submitterOrg = "";

					List<ComponentContactView> contacts = response.getList(new TypeReference<List<ComponentContactView>>()
					{
					});
					for (ComponentContactView contactView : contacts) {
						if ("SUB".equals(contactView.getContactType())) {
							submitterName = contactView.getName();
							submitterEmail = contactView.getEmail();
							submitterOrg = contactView.getOrganization();
						}
					}

					csvWriter.writeNext(new String[]{
						view.getComponent().getName(),
						view.getComponent().getComponentTypeLabel(),
						view.getComponent().getOrganization(),
						view.getComponent().getApprovalStateLabel(),
						view.getComponent().getSubmittedDts() != null ? sdf.format(view.getComponent().getSubmittedDts()) : "",
						submitterName,
						submitterEmail,
						submitterOrg,
						view.getComponent().getCreateDts() != null ? sdf.format(view.getComponent().getCreateDts()) : "",
						view.getComponent().getCreateUser(),
						view.getComponent().getUpdateDts() != null ? sdf.format(view.getComponent().getUpdateDts()) : "",
						view.getComponent().getUpdateUser()
					});

				}
			}
			System.out.println("Done");

			csvWriter.flush();
			csvWriter.close();
		} catch (IOException ex) {
			Logger.getLogger(POCReportUseCase.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

}

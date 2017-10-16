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
package edu.usu.sdl.openstorefront.report;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentResource;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import edu.usu.sdl.openstorefront.core.entity.ReportTransmissionType;
import edu.usu.sdl.openstorefront.core.entity.ResourceType;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import edu.usu.sdl.openstorefront.report.generator.BaseGenerator;
import edu.usu.sdl.openstorefront.report.generator.CSVGenerator;
import edu.usu.sdl.openstorefront.report.model.LinkCheckModel;
import edu.usu.sdl.openstorefront.report.model.LinkValidationReportModel;
import edu.usu.sdl.openstorefront.report.output.ReportWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLHandshakeException;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author dshurtleff
 */
public class ExternalLinkValidationReport
		extends BaseReport
{

	private static final Logger LOG = Logger.getLogger(ExternalLinkValidationReport.class.getName());

	private static final String SIPR_LINIK = "smil.mil";
	private static final String JWICS_LINIK = "ic.gov";
	private static final String NETWORK_SIPR = "SIPR";
	private static final String NETWORK_JWICS = "JWICS";

	private static final int MAX_CHECKPOOL_SIZE = 20;
	private static final int MAX_CONNECTION_TIME_MILLIS = 5000;

	private List<LinkCheckModel> links = new ArrayList<>();

	public ExternalLinkValidationReport(Report report)
	{
		super(report);
	}

	@Override
	protected LinkValidationReportModel gatherData()
	{
		ComponentResource componentResourceExample = new ComponentResource();
		componentResourceExample.setActiveStatus(ComponentResource.ACTIVE_STATUS);
		List<ComponentResource> componentResources = service.getPersistenceService().queryByExample(componentResourceExample);
		Map<String, List<ComponentResource>> resourceMap = new HashMap<>();
		componentResources.forEach(resource -> {
			if (resourceMap.containsKey(resource.getComponentId())) {
				resourceMap.get(resource.getComponentId()).add(resource);
			} else {
				List<ComponentResource> resources = new ArrayList<>();
				resources.add(resource);
				resourceMap.put(resource.getComponentId(), resources);
			}
		});

		Component componentExample = new Component();
		componentExample.setActiveStatus(Component.ACTIVE_STATUS);
		componentExample.setApprovalState(ApprovalStatus.APPROVED);
		List<Component> components = service.getPersistenceService().queryByExample(componentExample);

		Map<String, Component> componentMap = new HashMap<>();
		components.forEach(component -> {
			componentMap.put(component.getComponentId(), component);
		});

		//exact all links
		long linkCountId = 1;
		for (Component component : componentMap.values()) {

			Document doc = Jsoup.parseBodyFragment(component.getDescription());
			Elements elements = doc.select("a");

			for (Element element : elements) {
				String link = element.attr("href");
				LinkCheckModel linkCheckModel = new LinkCheckModel();
				linkCheckModel.setId(component.getComponentId() + "-" + (linkCountId++));
				linkCheckModel.setComponentName(component.getName());
				linkCheckModel.setLink(link);
				linkCheckModel.setNetworkOfLink(getNetworkOfLink(link));
				linkCheckModel.setResourceType("Description Link");
				linkCheckModel.setSecurityMarking(component.getSecurityMarkingType());
				links.add(linkCheckModel);
			}

			List<ComponentResource> resources = resourceMap.get(component.getComponentId());
			if (resources != null) {
				for (ComponentResource resource : resources) {
					String link = resource.getLink();

					//Blank means it's an internal resource
					if (StringUtils.isNotBlank(link)) {
						if (link.toLowerCase().contains("<a")) {
							doc = Jsoup.parseBodyFragment(link);
							elements = doc.select("a");
							for (Element element : elements) {
								link = element.attr("href");
								break;
							}
						}

						LinkCheckModel linkCheckModel = new LinkCheckModel();
						linkCheckModel.setId(component.getComponentId() + "-" + resource.getResourceId());
						linkCheckModel.setComponentName(component.getName());
						linkCheckModel.setLink(link);
						linkCheckModel.setNetworkOfLink(getNetworkOfLink(resource.getLink()));
						linkCheckModel.setResourceType(TranslateUtil.translate(ResourceType.class, resource.getResourceType()));
						linkCheckModel.setSecurityMarking(resource.getSecurityMarkingType());
						links.add(linkCheckModel);
					}
				}
			}

		}
		checkLinks();

		LinkValidationReportModel linkValidationReportModel = new LinkValidationReportModel();
		linkValidationReportModel.setTitle("External Link Validation Report");
		linkValidationReportModel.setData(links);

		return linkValidationReportModel;
	}

	@Override
	protected Map<String, ReportWriter> getWriterMap()
	{
		Map<String, ReportWriter> writerMap = new HashMap<>();

		String viewCSV = outputKey(ReportTransmissionType.VIEW, ReportFormat.CSV);
		writerMap.put(viewCSV, (generator, reportModel) -> {
			writeCSV(generator, (LinkValidationReportModel) reportModel);
		});

		String emailCSV = outputKey(ReportTransmissionType.EMAIL, ReportFormat.CSV);
		writerMap.put(emailCSV, (generator, reportModel) -> {
			writeCSV(generator, (LinkValidationReportModel) reportModel);
		});

		return writerMap;
	}

	@Override
	public List<ReportTransmissionType> getSupportedOutputs()
	{
		List<ReportTransmissionType> transmissionTypes = new ArrayList<>();

		ReportTransmissionType view = service.getLookupService().getLookupEnity(ReportTransmissionType.class, ReportTransmissionType.VIEW);
		ReportTransmissionType email = service.getLookupService().getLookupEnity(ReportTransmissionType.class, ReportTransmissionType.EMAIL);
		transmissionTypes.add(view);
		transmissionTypes.add(email);

		return transmissionTypes;
	}

	@Override
	public List<ReportFormat> getSupportedFormat(String reportTransmissionType)
	{
		List<ReportFormat> formats = new ArrayList<>();

		switch (reportTransmissionType) {
			case ReportTransmissionType.VIEW:
				ReportFormat format = service.getLookupService().getLookupEnity(ReportFormat.class, ReportFormat.CSV);
				formats.add(format);
				break;

			case ReportTransmissionType.EMAIL:
				format = service.getLookupService().getLookupEnity(ReportFormat.class, ReportFormat.CSV);
				formats.add(format);
				break;
		}

		return formats;
	}

	private void writeCSV(BaseGenerator generator, LinkValidationReportModel reportModel)
	{
		CSVGenerator cvsGenerator = (CSVGenerator) generator;
		cvsGenerator.addLine(reportModel.getTitle(), sdf.format(reportModel.getCreateTime()));
		cvsGenerator.addLine(
				"Component Name",
				"Resource Type",
				"Network Of Link",
				"Link",
				"Status",
				"Http Status",
				"Check Results"
		);

		for (LinkCheckModel lineModel : reportModel.getData()) {
			cvsGenerator.addLine(
					lineModel.getComponentName(),
					lineModel.getResourceType(),
					lineModel.getNetworkOfLink(),
					lineModel.getLink(),
					lineModel.getStatus(),
					lineModel.getHttpStatus(),
					lineModel.getCheckResults()
			);
		}
	}

	private String getNetworkOfLink(String url)
	{
		String network = null;
		if (StringUtils.isNotBlank(url)) {
			if (url.toLowerCase().contains(SIPR_LINIK)) {
				network = NETWORK_SIPR;
			} else if (url.toLowerCase().contains(JWICS_LINIK)) {
				network = NETWORK_JWICS;
			}

		}
		return network;
	}

	private void checkLinks()
	{
		int timeOutTime = MAX_CONNECTION_TIME_MILLIS;
		if (report.getReportOption() != null) {
			if (report.getReportOption().getMaxWaitSeconds() != null) {
				timeOutTime = report.getReportOption().getMaxWaitSeconds() * 1000;
			}
		}

		ForkJoinPool forkJoinPool = new ForkJoinPool(MAX_CHECKPOOL_SIZE);

		Map<String, LinkCheckModel> linkMap = new HashMap();
		List<ForkJoinTask<LinkCheckModel>> tasks = new ArrayList<>();
		for (LinkCheckModel link : links) {
			linkMap.put(link.getId(), link);
			tasks.add(forkJoinPool.submit(new CheckLinkTask(link, timeOutTime)));
		}

		int completedCount = 0;
		for (ForkJoinTask<LinkCheckModel> task : tasks) {
			try {
				LinkCheckModel processed;
				try {
					processed = task.get(timeOutTime, TimeUnit.MILLISECONDS);
					if (processed != null) {
						LinkCheckModel reportModel = linkMap.get(processed.getId());
						reportModel.setStatus(processed.getStatus());
						reportModel.setCheckResults(processed.getCheckResults());
						reportModel.setHttpStatus(processed.getHttpStatus());
					} else {
						//This shouldn't occur, however if it does at least show a message.
						LOG.log(Level.WARNING, MessageFormat.format("A link check task failed to return results.  Status at Completed Abnormally? {0}", task.isCompletedAbnormally()));
					}
				} catch (TimeoutException e) {
					task.cancel(true);
				}

				completedCount++;
			} catch (InterruptedException | ExecutionException ex) {
				LOG.log(Level.WARNING, "Check task  was interrupted.  Report results may be not complete.", ex);
			}
			LOG.log(Level.FINE, MessageFormat.format("Complete Checking Link Count: {0} out of {1}", new Object[]{completedCount, links.size()}));
		}

		for (LinkCheckModel checkModel : links) {
			if (StringUtils.isBlank(checkModel.getStatus())) {
				checkModel.setStatus("Unable to verify.  Timed out while waiting.");
			}
		}

		forkJoinPool.shutdownNow();
		try {
			forkJoinPool.awaitTermination(1000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException ex) {
			LOG.log(Level.WARNING, "Check task shutdown was interrupted.  The application will recover and continue.", ex);
		}
	}

	private class CheckLinkTask
			implements Callable<LinkCheckModel>
	{

		private final LinkCheckModel modelToCheck;
		private int timeOutTime;

		public CheckLinkTask(LinkCheckModel modelToCheck, int timeOutTime)
		{
			this.modelToCheck = modelToCheck;
			this.timeOutTime = timeOutTime;
		}

		@Override
		public LinkCheckModel call() throws Exception
		{
			LinkCheckModel linkCheckModel = new LinkCheckModel();
			linkCheckModel.setId(modelToCheck.getId());

			long startTime = System.currentTimeMillis();
			LOG.log(Level.FINEST, MessageFormat.format("Checking link: {0}", modelToCheck.getLink()));

			if (StringUtils.isNotBlank(modelToCheck.getNetworkOfLink())) {
				linkCheckModel.setCheckResults("Not checked");
				linkCheckModel.setStatus(OpenStorefrontConstant.NOT_AVAILABLE);
			} else {
				try {
					URL url = new URL(modelToCheck.getLink());
					URLConnection connection = url.openConnection();
					connection.setConnectTimeout(timeOutTime);
					connection.setReadTimeout(timeOutTime);
					connection.setUseCaches(false);

					HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
					httpConnection.setInstanceFollowRedirects(true);

					try {
						connection.connect();
						linkCheckModel.setHttpStatus(Integer.toString(httpConnection.getResponseCode()));
						linkCheckModel.setStatus(httpConnection.getResponseMessage());
						if (StringUtils.isNotBlank(linkCheckModel.getStatus())
								&& "OK".equalsIgnoreCase(linkCheckModel.getStatus().trim()) == false) {
							linkCheckModel.setCheckResults("Bad Link or it is restricted. (See HTTP Status Code)");
						}
					} catch (SSLHandshakeException e) {
						linkCheckModel.setStatus("Certificate Request/Error");
						linkCheckModel.setCheckResults("Client Certificate Requested (CAC) or Server Certificate Error.  Actual error Message: " + e.getMessage());
					} catch (Exception e) {
						LOG.log(Level.FINER, "Actual connection error: ", e);
						linkCheckModel.setStatus("Timeout/Error Connecting");
						linkCheckModel.setCheckResults("Error occur when trying to connect.  This may be a temporary case or the link may be bad. Actual error Message: " + e.getMessage());
					}
				} catch (Exception e) {
					linkCheckModel.setStatus("URL is bad");
					linkCheckModel.setCheckResults("Check link to make sure it's properly formatted");
				}
			}
			LOG.log(Level.FINEST, MessageFormat.format("Finish checking link: {0} Check Time: {1} ms", modelToCheck.getLink(), System.currentTimeMillis() - startTime));

			return linkCheckModel;
		}

	}

}

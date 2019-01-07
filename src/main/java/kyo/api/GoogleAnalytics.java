package kyo.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.AnalyticsReportingScopes;
import com.google.api.services.analyticsreporting.v4.model.DateRange;
import com.google.api.services.analyticsreporting.v4.model.Dimension;
import com.google.api.services.analyticsreporting.v4.model.GetReportsRequest;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.google.api.services.analyticsreporting.v4.model.Metric;
import com.google.api.services.analyticsreporting.v4.model.Report;
import com.google.api.services.analyticsreporting.v4.model.ReportRequest;
import com.google.api.services.analyticsreporting.v4.model.ReportRow;

public class GoogleAnalytics {
	private static final String APPLICATION_NAME = "Google Analytics Report!";
	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	private static final String KEY_FILE_LOCATION = "./ga_client_secret.json";
	private static final String VIEW_ID = "146036006";

	public static void main(String[] args) {
		try {
			AnalyticsReporting service = initializeAnalyticsReporting();

			GetReportsResponse response = getReport(service);
			printResponse(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initializes an Analytics Reporting API V4 service object.
	 *
	 * @return An authorized Analytics Reporting API V4 service object.
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	private static AnalyticsReporting initializeAnalyticsReporting() throws GeneralSecurityException, IOException {

		HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(KEY_FILE_LOCATION))
				.createScoped(AnalyticsReportingScopes.all());

		// Construct the Analytics Reporting service object.
		return new AnalyticsReporting.Builder(httpTransport, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build();
	}

	/**
	 * Queries the Analytics Reporting API V4.
	 *
	 * @param service An authorized Analytics Reporting API V4 service object.
	 * @return GetReportResponse The Analytics Reporting API V4 response.
	 * @throws IOException
	 */
	private static GetReportsResponse getReport(AnalyticsReporting service) throws IOException {
		
		LocalDate currentDate = LocalDate.now();// "yyyy-MM-dd"
		LocalDate firstDate = currentDate.withDayOfMonth(1);
		LocalDate lastDate = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
		// Create the DateRange object.
		DateRange dateRange = new DateRange();
		dateRange.setStartDate(firstDate.toString());
		dateRange.setEndDate(lastDate.toString());

		// Create the Metrics object.
		Metric sessions = new Metric().setExpression("ga:sessions");
		Metric sessions2 = new Metric().setExpression("ga:goal6Completions");
		Metric sessions3 = new Metric().setExpression("ga:goal6ConversionRate");

		Dimension browser = new Dimension().setName("ga:date");
		Dimension browser2 = new Dimension().setName("ga:dimension2");
		Dimension browser3 = new Dimension().setName("ga:channelGrouping");
		Dimension browser4 = new Dimension().setName("ga:adwordsCampaignID");

		// Create the ReportRequest object.
		ReportRequest request = new ReportRequest().setViewId(VIEW_ID).setDateRanges(Arrays.asList(dateRange))
				.setMetrics(Arrays.asList(sessions, sessions2, sessions3))
				.setDimensions(Arrays.asList(browser, browser2, browser3, browser4));

		ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
		requests.add(request);

		// Create the GetReportsRequest object.
		GetReportsRequest getReport = new GetReportsRequest().setReportRequests(requests);

		// Call the batchGet method.
		GetReportsResponse response = service.reports().batchGet(getReport).execute();

		// Return the response.
		return response;
	}

	/**
	 * Parses and prints the Analytics Reporting API V4 response.
	 *
	 * @param response An Analytics Reporting API V4 response.
	 * @throws UnsupportedEncodingException
	 */
	private static void printResponse(GetReportsResponse response) throws UnsupportedEncodingException {

		StringBuilder data = new StringBuilder();
		data.append("Date,ga:dimension2,Default Channel Grouping,Sessions,ga:goal6Completions,ga:goal6ConversionRate");
		data.append("\n");
		for (Report report : response.getReports()) {
			List<ReportRow> rows = report.getData().getRows();
			report.getData().get("sessions");
			if (rows == null) {
				System.out.println("No data found for " + VIEW_ID);
				return;
			}
			for (ReportRow row : rows) {
				List<String> dimensions = row.getDimensions();
				List<String> metrics = row.getMetrics().get(0).getValues();
				if (dimensions.size() > 0) {
					data = addData(data, dimensions.get(0));
					data = addData(data, dimensions.get(1));
					data = addData(data, dimensions.get(2));
				}
				if (metrics.size() > 0) {
					data = addData(data, metrics.get(0));
					data = addData(data, metrics.get(1));
					data = addData(data, metrics.get(2));
				}
				data.append('\n');
			}
			try {
				String PATH = "api_output/google_analytic/";
				File dir = new File(PATH);
				if(!dir.exists()) {
					dir.mkdirs();
				}
							
				File file = new File(PATH + "GA.csv");
				PrintWriter pw = new PrintWriter(file);
				pw.write(data.toString());
				pw.close();
				System.out.println("done!");
				System.out.println(file.getAbsolutePath());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static StringBuilder addData(StringBuilder data, String str) {

		data.append(str);
		data.append(',');
		return data;
	}
}

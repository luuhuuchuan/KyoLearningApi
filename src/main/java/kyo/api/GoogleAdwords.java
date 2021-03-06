package kyo.api;

import static com.google.api.ads.common.lib.utils.Builder.DEFAULT_CONFIGURATION_FILENAME;

import com.google.api.ads.adwords.axis.factory.AdWordsServices;
import com.google.api.ads.adwords.axis.utils.v201809.ServiceQuery;
import com.google.api.ads.adwords.axis.v201809.cm.ApiError;
import com.google.api.ads.adwords.axis.v201809.cm.ApiException;
import com.google.api.ads.adwords.axis.v201809.cm.Campaign;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignPage;
import com.google.api.ads.adwords.axis.v201809.cm.CampaignServiceInterface;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import com.google.api.ads.adwords.lib.selectorfields.v201809.cm.CampaignField;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.client.auth.oauth2.Credential;
import java.rmi.RemoteException;

/**
 * This example gets all campaigns with AWQL. To add a campaign, run
 * AddCampaign.java.
 *
 * <p>
 * Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info.
 */
public class GoogleAdwords {

	private static final int PAGE_SIZE = 100;

	public static void main(String[] args) {
		AdWordsSession session;
		try {
			// Generate a refreshable OAuth2 credential.
			Credential oAuth2Credential = new OfflineCredentials.Builder().forApi(Api.ADWORDS).fromFile().build()
					.generateCredential();

			// Construct an AdWordsSession.
			session = new AdWordsSession.Builder().fromFile().withOAuth2Credential(oAuth2Credential).build();
		} catch (ConfigurationLoadException cle) {
			System.err.printf("Failed to load configuration from the %s file. Exception: %s%n",
					DEFAULT_CONFIGURATION_FILENAME, cle);
			return;
		} catch (ValidationException ve) {
			System.err.printf("Invalid configuration in the %s file. Exception: %s%n", DEFAULT_CONFIGURATION_FILENAME,
					ve);
			return;
		} catch (OAuthException oe) {
			System.err.printf(
					"Failed to create OAuth credentials. Check OAuth settings in the %s file. " + "Exception: %s%n",
					DEFAULT_CONFIGURATION_FILENAME, oe);
			return;
		}

		AdWordsServicesInterface adWordsServices = AdWordsServices.getInstance();

		try {
			runExample(adWordsServices, session);
		} catch (ApiException apiException) {
			// ApiException is the base class for most exceptions thrown by an API request.
			// Instances
			// of this exception have a message and a collection of ApiErrors that indicate
			// the
			// type and underlying cause of the exception. Every exception object in the
			// adwords.axis
			// packages will return a meaningful value from toString
			//
			// ApiException extends RemoteException, so this catch block must appear before
			// the
			// catch block for RemoteException.
			System.err.println("Request failed due to ApiException. Underlying ApiErrors:");
			if (apiException.getErrors() != null) {
				int i = 0;
				for (ApiError apiError : apiException.getErrors()) {
					System.err.printf("  Error %d: %s%n", i++, apiError);
				}
			}
		} catch (RemoteException re) {
			System.err.printf("Request failed unexpectedly due to RemoteException: %s%n", re);
		}
	}

	/**
	 * Runs the example.
	 *
	 * @param adWordsServices the services factory.
	 * @param session         the session.
	 * @throws ApiException    if the API request failed with one or more service
	 *                         errors.
	 * @throws RemoteException if the API request failed due to other errors.
	 */
	public static void runExample(AdWordsServicesInterface adWordsServices, AdWordsSession session)
			throws RemoteException {
		// Get the CampaignService.
		CampaignServiceInterface campaignService = adWordsServices.get(session, CampaignServiceInterface.class);

		ServiceQuery serviceQuery = new ServiceQuery.Builder()
				.fields(CampaignField.Id, CampaignField.Name, CampaignField.Status).orderBy(CampaignField.Name)
				.limit(0, PAGE_SIZE).build();

		CampaignPage page = null;
		do {
			serviceQuery.nextPage(page);
			// Get all campaigns.
			page = campaignService.query(serviceQuery.toString());

			// Display campaigns.
			if (page.getEntries() != null) {
				for (Campaign campaign : page.getEntries()) {
					System.out.printf("キャンペーン名(chair_id) = " + campaign.getName() + " and 日(yyyy_mm_dd) = "
							+ campaign.getStartDate() + " and 費用 (ad_amount) = " + campaign.getBudget().getAmount()
							+ " was found.%n");
					campaign.getAdServingOptimizationStatus();
					campaign.getAdvertisingChannelSubType();
					campaign.getAdvertisingChannelType();
					campaign.getBaseCampaignId();
					campaign.getBiddingStrategyConfiguration();
					campaign.getBudget();
					campaign.getCampaignGroupId();
					campaign.getCampaignTrialType();
					campaign.getConversionOptimizerEligibility();
					campaign.getEndDate();
					campaign.getFinalUrlSuffix();
					campaign.getForwardCompatibilityMap();
					campaign.getFrequencyCap();
					campaign.getId();
					campaign.getLabels();
					campaign.getName();
					campaign.getNetworkSetting();
					campaign.getSelectiveOptimization();
					campaign.getServingStatus();
					campaign.getSettings();
					campaign.getStartDate();
					campaign.getStatus();
					campaign.getTrackingUrlTemplate();
					campaign.getUniversalAppCampaignInfo();
					campaign.getUrlCustomParameters();
					campaign.getVanityPharma();
				}
			} else {
				System.out.println("No campaigns were found.");
			}
		} while (serviceQuery.hasNext(page));
	}
}
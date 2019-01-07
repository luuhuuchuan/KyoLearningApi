package kyo.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import twitter4jads.BaseAdsListResponse;
import twitter4jads.BaseAdsListResponseIterable;
import twitter4jads.TwitterAds;
import twitter4jads.TwitterAdsFactory;
import twitter4jads.api.TwitterAdsCampaignApi;
import twitter4jads.conf.ConfigurationBuilder;
import twitter4jads.internal.models4j.TwitterException;
import twitter4jads.models.ads.Campaign;

@SuppressWarnings("unused")
public class TwitterApi {
	private static final List<String> CSV_HEADERS = Arrays.asList("期間", "プレースメント", "キャンペーン名", "目的", "リンクの編集", "ステータス",
			"インプレッション", "ご利用金額", "新規登録", "登録あたりのコスト", "登録 - ポストエンゲージメント", "登録 - アシスト", "登録 - ポストビュー", "リンクのクリック数",
			"リンククリックあたりのコスト", "キャンペーン開始", "キャンペーン終了", "総予算", "日別予算", "キャンペーン予算残高", "キャンペーンの1日のペーシング",
			"キャンペーン期間全体のペーシング", "結果", "結果タイプ", "結果レート", "結果レートタイプ", "結果あたりのコスト", "結果タイプあたりのコスト", "登録 - 売上金額", "いいね",
			"リツイート");

	public static void main(String[] args) {
		TwitterAds twitterAdsInstance = getTwitterAdsInstance();
		// ===============Get
		// Campaign===================================================
		TwitterAdsCampaignApi campaignApi = twitterAdsInstance.getCampaignApi();
		List<Campaign> campaignList = new ArrayList<Campaign>();
		try {
			// BaseAdsListResponseIterable<Campaign> allCampaigns =
			// campaignApi.getAllCampaigns("18ce53uo3nm", null, null, false, null, null,
			// null);
			BaseAdsListResponseIterable<Campaign> allCampaigns = campaignApi.getAllCampaigns("18ce53xeak4", null, null,
					false, null, null, null);
			for (BaseAdsListResponse<Campaign> allCampaign : allCampaigns) {
				campaignList.addAll(allCampaign.getData());
			}
			// "Period", "Placement", "Campaign Name", "Purpose", "Edit Link", "Status",
			// "Impression", "Amount Used", "New Registration", "Cost Per Registration"
			// "Registration - post engagement", "registration - assist", "registration -
			// post view", "click number of links", "cost per link click", "campaign start",
			// "campaign end", "total budget" "Daily budget", "Campaign budget outstanding",
			// "Campaign's daily pacing", "Campaign overall pacing", "Results", "Result
			// type", "Result rate", "Result rate type", " Cost per result "," cost per
			// result type "," registration - sales amount "," nice "," retweet "
			// "期間", "プレースメント", "キャンペーン名","目的", "リンクの編集", "ステータス","インプレッション", "ご利用金額",
			// "新規登録", "登録あたりのコスト", "登録 - ポストエンゲージメント", "登録 - アシスト", "登録 - ポストビュー",
			// "リンクのクリック数", "リンククリックあたりのコスト", "キャンペーン開始", "キャンペーン終了", "総予算", "日別予算",
			// "キャンペーン予算残高", "キャンペーンの1日のペーシング", "キャンペーン期間全体のペーシング", "結果", "結果タイプ", "結果レート",
			// "結果レートタイプ", "結果あたりのコスト", "結果タイプあたりのコスト", "登録 - 売上金額", "いいね", "リツイート"
			List<Object> dataCsv = new ArrayList<Object>();
			for (Campaign campaign : campaignList) {
				List<Object> row = new ArrayList<Object>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/mm/dd");
				row.add(campaign.getEndTime() == null ? "" : sdf.format(campaign.getEndTime()));// Period
				row.add("すべて");// プレースメント
				row.add(campaign.getName());

				row.add("ウェブサイトへの誘導数またはコンバージョン");// 目的
				row.add(campaign.getAccountId());
				System.out.println("AccountId:" + campaign.getAccountId());
				System.out.println("CreateTime:" + campaign.getCreateTime());
				System.out.println("Currency:" + campaign.getCurrency());
				System.out.println("DailyBudgetInMicro:" + campaign.getDailyBudgetInMicro());
				System.out.println("Deleted:" + campaign.getDeleted());
				System.out.println("DurationInDays:" + campaign.getDurationInDays());
				System.out.println("EndTime:" + campaign.getEndTime());
				System.out.println("EntityStatus:" + campaign.getEntityStatus());
				System.out.println("FrequencyCap:" + campaign.getFrequencyCap());
				System.out.println("FundingInstrumentId:" + campaign.getFundingInstrumentId());
				System.out.println("Id:" + campaign.getId());

				System.out.println("Name:" + campaign.getName());
				System.out.println("ReasonsNotServable:" + campaign.getReasonsNotServable().get(0));
				System.out.println("Servable:" + campaign.getServable());
				System.out.println("StandardDelivery:" + campaign.getStandardDelivery());
				System.out.println("StartTime:" + campaign.getStartTime());
				System.out.println("TotalBudgetInMicro:" + campaign.getTotalBudgetInMicro());
				System.out.println("UpdateTime:" + campaign.getUpdateTime());
				System.out.println("--------------------------");
			}
			System.out.println(campaignList.size());
		} catch (TwitterException e) {
			System.err.println(e.getErrorMessage());
		}

	}

	public static TwitterAds getTwitterAdsInstance() {
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setOAuthConsumerSecret("VjCWEi9ItSMtNCocY0k16UtDLuyUAe0BKVoTPp322XiZ5DegHz")
				.setOAuthConsumerKey("IcwQasEKb1SJzwSGvVBOZKcUN")
				.setOAuthAccessToken("108772228-odtwCUd1w8tDQAO59Yl1yiSnxFvcpQY7HIL21UzW")
				.setOAuthAccessTokenSecret("2tzl29RCu5pTCAR5ECTnHQRSK7PJFkUL5s5gD2z8PxdUX").setHttpRetryCount(0)
				.setHttpConnectionTimeout(5000);
		return new TwitterAdsFactory(configurationBuilder.build()).getAdsInstance();
	}
}

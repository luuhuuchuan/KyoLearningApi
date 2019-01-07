package kyo.api;

import java.io.File;
import java.io.PrintWriter;

import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.APINodeList;
import com.facebook.ads.sdk.AdAccount;
import com.facebook.ads.sdk.Campaign;

public class FacebookApi {
	public static final APIContext context = new APIContext(
			// token
			"EAAVfA2OAyS4BAAwoELY4jlqjWARu0XANoSw8JJLMPF0ZCgGmMdMBEznRinZAbujNiFMShwXL2wixEI31JI1PK0z3nbdZBuzGeNuf9141G7alOWCxzj1W7KyQQcyE73YQwc4S5R6vKyWFygAUTyQ6vsCyxeGE5V8uMsvQrxLwwZDZD",
			// app secret key
			"0db980a1a24e8d0575e4aa94d7be620a");

	public static void main(String[] args) {
		// Sandbox Ad Account
		AdAccount account = new AdAccount("1511843042478382", context);
		try {
			APINodeList<Campaign> campaigns = account.getCampaigns().requestAllFields().execute();
			for (Campaign campaign : campaigns) {
				System.out.println(campaign.getFieldName());
			}

			String endpointBase = account.getContext().getEndpointBase();
			String version = account.getContext().getVersion();
			String appSecret = account.getContext().getAppSecret();
			PrintWriter pw = new PrintWriter(new File("test.csv"));
			StringBuilder sb = new StringBuilder();
			sb.append("EndpointBase");
			sb.append(',');
			sb.append("Version");
			sb.append(',');
			sb.append("AppSecret");
			sb.append('\n');

			sb.append(endpointBase + "コードの構造");
			sb.append(',');
			sb.append(version);
			sb.append(',');
			sb.append(appSecret);
			sb.append('\n');

			pw.write(sb.toString());
			pw.close();
			System.out.println("done!");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

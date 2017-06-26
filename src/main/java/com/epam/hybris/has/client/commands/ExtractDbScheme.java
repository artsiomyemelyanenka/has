package com.epam.hybris.has.client.commands;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 */
public class ExtractDbScheme implements Cmd
{

	@Override
	public void execute(CmdContext context)
	{
		String url = context.getString(Const.DB_URL);
		Pattern pattern = Pattern.compile("jdbc:mysql:\\/\\/(.+)\\/(.+)\\?.*");
		Matcher m = pattern.matcher(url);
		if (m.find()) {
			String host = m.group(1);
			if (!Const.LOCALHOST.equals(host)) {
				context.addErrorMessage("The db should be local");
				return;
			}
			String dbScheme = m.group(2);
			context.set(Const.DB_SCHEME, dbScheme);
		} else {
			System.err.println("Can't parse jdbc url. Example of the url below:");
			System.err.println("jdbc:mysql://localhost/amway?useConfigs=maxPerformance&rewriteBatchedStatements=true&useUnicode=true&characterEncoding=utf8&useSSL=false");
			context.addErrorMessage("Can't parse DB scheme.");
			return;
		}
	}
}

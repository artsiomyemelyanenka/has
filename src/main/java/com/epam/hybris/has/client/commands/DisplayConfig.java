package com.epam.hybris.has.client.commands;

/**
 * Created by vb on 18.6.17.
 */
public class DisplayConfig implements Cmd
{

	@Override
	public void execute(CmdContext context)
	{
		displayKey(context, Const.HYBRIS_VERSION);
		displayKey(context, Const.DB_URL);
		displayKey(context, Const.DB_USERNAME);
		displayKey(context, Const.DB_SCHEME);
		displayKey(context, Const.GIT_BRANCH);
	}

	private void displayKey(CmdContext context, String key)
	{
		System.out.println(key + "\t" + context.get(key));
	}
}

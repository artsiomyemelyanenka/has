package com.epam.hybris.has.client.commands.store;

import org.apache.commons.lang3.StringUtils;

import com.epam.hybris.has.client.commands.Cmd;
import com.epam.hybris.has.client.commands.CmdContext;
import com.epam.hybris.has.client.commands.Const;


/**
 */
public class BeforeStoreCheckContext implements Cmd
{

	@Override
	public void execute(CmdContext context)
	{
		if (StringUtils.isEmpty(context.getString(Const.PROJECT_NAME))) {
			context.addErrorMessage("Please, specify 'project.name' in has.properties");
			return;
		}
		if (StringUtils.isEmpty(context.getString(Const.GIT_BRANCH))) {
			context.addErrorMessage("Git branch not recognized. Init git or specify branch directly.");
			return;
		}
		if (StringUtils.isEmpty(context.getString(Const.HYBRIS_VERSION))) {
			context.addErrorMessage("Hybris version is not recognized.");
			return;
		}
		if (StringUtils.isEmpty(context.getString(Const.DB_SCHEME))) {
			context.addErrorMessage("DB scheme is not recognized.");
			return;
		}
		if (StringUtils.isEmpty(context.getString(Const.DB_USERNAME))) {
			context.addErrorMessage("DB user name is not recognized.");
			return;
		}
		if (StringUtils.isEmpty(context.getString(Const.DB_PASSWORD))) {
			context.addErrorMessage("DB user password is not recognized.");
			return;
		}

	}
}

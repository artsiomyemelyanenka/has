package com.epam.hybris.has.client.commands.store;

import java.io.File;
import java.io.IOException;

import com.epam.hybris.has.client.commands.Cmd;
import com.epam.hybris.has.client.commands.CmdContext;
import com.epam.hybris.has.client.commands.Const;
import com.epam.hybris.has.client.model.Image;
import com.epam.hybris.has.client.model.ImageSpec;


/**
 */
public class StoreImage implements Cmd
{
	@Override
	public void execute(CmdContext context)
	{
		context.process("Store image");
		File dumpFile = (File) context.get(Const.SQL_DUMP_FILE);
		File dataZip = (File) context.get(Const.DATA_ZIP_FILE);
		ImageSpec spec = new ImageSpec();
		spec.setBranchName(context.getString(Const.GIT_BRANCH));
		spec.setHybrisVersion(context.getString(Const.HYBRIS_VERSION));
		spec.setProjectName(context.getString(Const.PROJECT_NAME));
		Image image = new Image(spec, dumpFile, dataZip, null);
		try
		{
			context.getRepository().saveImage(image);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			context.addErrorMessage("Can't save image");
		}
	}
}

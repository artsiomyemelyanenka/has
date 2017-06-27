package com.epam.hybris.has.client.commands.restore;

import java.io.File;
import java.io.IOException;

import org.apache.ant.compress.taskdefs.Unzip;
import org.apache.commons.io.FileUtils;

import com.epam.hybris.has.client.commands.Cmd;
import com.epam.hybris.has.client.commands.CmdContext;
import com.epam.hybris.has.client.commands.Const;
import com.epam.hybris.has.client.model.Image;


/**
 */
public class RestoreData implements Cmd
{
	@Override
	public void execute(CmdContext context)
	{
		File dataFolder = new File(context.getBaseFolder(), "../../data");
		if (! dataFolder.exists()) {
			context.message("Data folder not found. But I'll proceed. " + dataFolder.getAbsolutePath());
		} else {
			backupData(context, dataFolder);
		}
		try {
			dataFolder = dataFolder.getCanonicalFile();
		} catch (IOException ex) {
			ex.printStackTrace();
			context.addErrorMessage("Can't get datta folder. " + dataFolder.getAbsolutePath());
		}
		if (! dataFolder.mkdirs()) {
			context.addErrorMessage("Can't create new data folder. " + dataFolder.getAbsolutePath());
		}
		Image image = (Image) context.get(Const.IMAGE);
		File mediaZip = image.getData();
		Unzip u = new Unzip();
		u.setSrc(mediaZip);
		u.setDest(dataFolder);
		context.process("Unzip media");
		u.execute();
	}

	private void backupData(CmdContext context, File dataFolder)
	{
		File bak = new File(dataFolder.getParent(), "data_bak");
		if (bak.exists()) {
			try
			{
				FileUtils.deleteDirectory(bak);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				context.addErrorMessage("Can't delete old bak data folder.");
				return;
			}
		}
		if ((! dataFolder.renameTo(bak)) || (dataFolder.exists())) {
			context.addErrorMessage("Can't rename data folder into data_bak. Check your system is not running.");
			return;
		}
	}
}

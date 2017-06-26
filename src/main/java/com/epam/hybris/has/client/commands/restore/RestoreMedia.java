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
 * @deprecated
 */
public class RestoreMedia implements Cmd
{
	@Override
	public void execute(CmdContext context)
	{
		File mediaFolder = new File(context.getBaseFolder(), "../../data/media");
		if (! mediaFolder.exists()) {
			context.message("Media folder not found. But I'll proceed. " + mediaFolder.getAbsolutePath());
		} else {
			backupMedia(context, mediaFolder);
		}
		if (! mediaFolder.mkdirs()) {
			context.addErrorMessage("Can't create new media folder. " + mediaFolder.getAbsolutePath());
		}
		Image image = (Image) context.get(Const.IMAGE);
		File mediaZip = image.getMedia();
		Unzip u = new Unzip();
		u.setSrc(mediaZip);
		u.setDest(mediaFolder.getParentFile());
		context.process("Unzip media");
		u.execute();
	}

	private void backupMedia(CmdContext context, File mediaFolder)
	{
		File bak = new File(mediaFolder.getParent(), "media_bak");
		if (bak.exists()) {
			try
			{
				FileUtils.deleteDirectory(bak);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				context.addErrorMessage("Can't delete old bak media folder.");
				return;
			}
		}
		if ((! mediaFolder.renameTo(bak)) || (mediaFolder.exists())) {
			context.addErrorMessage("Can't rename media folder into media_bak. Check your system is not running.");
			return;
		}
	}
}

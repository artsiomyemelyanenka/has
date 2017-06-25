package com.epam.hybris.has.client.commands.repo.simple;

import java.io.File;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;

import com.epam.hybris.has.client.commands.Cmd;
import com.epam.hybris.has.client.commands.CmdContext;
import com.epam.hybris.has.client.commands.Const;
import com.epam.hybris.has.client.model.Image;
import com.epam.hybris.has.client.model.ImageSpec;


/**
 */
public class DownloadImage implements Cmd
{

	@Override
	public void execute(CmdContext context)
	{
		if (context.get(Const.IMAGE) != null) {
			context.message("Skip image download.");
			return;
		}
		try
		{
			context.process("Download image. DB dump is loading.");

			File dbDump = File.createTempFile("dbdump", "sql");
			java.nio.file.Files.copy(
					(InputStream) context.getResource(Const.REMOTE_DUMP_STREAM),
					dbDump.toPath(),
					StandardCopyOption.REPLACE_EXISTING);
			context.process("Download image. Media is loading.");
			File media = File.createTempFile("media", "zip");
			java.nio.file.Files.copy(
					(InputStream) context.getResource(Const.REMOTE_MEDIA_STREAM),
					media.toPath(),
					StandardCopyOption.REPLACE_EXISTING);
			CacheManager cm = new CacheManager(context);
			Image image = cm.put((ImageSpec) context.get(Const.REMOTE_IMAGE_SPEC), dbDump, media);
			context.set(Const.IMAGE, image);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

package com.epam.hybris.has.client.commands.repo.simple;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.IIOException;

import org.apache.commons.io.FileUtils;

import com.epam.hybris.has.client.commands.CmdContext;
import com.epam.hybris.has.client.model.Image;
import com.epam.hybris.has.client.model.ImageSpec;


/**
 */
public class CacheManager
{
	private final CmdContext context;

	private final File cacheFolder;

	public CacheManager(CmdContext context) throws IOException
	{
		this.context = context;
		File cacheFolder = context.getCacheFolder();
		this.cacheFolder = new File(cacheFolder, "simplerepo");
		if ((! this.cacheFolder.exists()) && (! this.cacheFolder.mkdirs())) {
			throw new IIOException("Can't create cache folder " + this.cacheFolder.getAbsolutePath());
		}
	}

	public Image extract(ImageSpec spec) throws IOException
	{
		String branchName = spec.getBranchName();
		File branchFolder = new File(cacheFolder, branchName);
		if (! branchFolder.exists()) {
			return null;
		}
		File dbDump = new File(branchFolder, "dump.sql");
		File media = new File(branchFolder, "media.zip");
		if (dbDump.exists() && media.exists()) {
			return new Image(spec, dbDump, media);
		} else {
			return null;
		}
	}

	public Image put(ImageSpec spec, File dbDump, File media) throws IOException
	{
		String branchName = spec.getBranchName();
		File branchFolder = new File(cacheFolder, branchName);
		if (! branchFolder.exists()) {
			if (! branchFolder.mkdirs()) {
				throw new IOException("Can't create branch folder" + branchFolder.getAbsolutePath());
			}
		}
		File cachedDbDump = new File(branchFolder, "dump.sql");
		File cachedMedia = new File(branchFolder, "media.zip");
		FileUtils.moveFile(dbDump, cachedDbDump);
		FileUtils.moveFile(media, cachedMedia);
		return new Image(spec, cachedDbDump, cachedMedia);
	}
}

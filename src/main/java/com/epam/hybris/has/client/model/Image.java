package com.epam.hybris.has.client.model;

import java.io.File;
import java.util.Map;


/**
 */
public class Image
{
	private final ImageSpec spec;

	/**
	 * File with SQL instructions to restore DB.
	 */
	private File dbDumpSql;

	private File binDump;

	/**
	 * Media folder in zip archive.
	 */
	private File media;

	/**
	 * Entire hybris data folder in zip archive.
	 */
	private File data;

	Map<String, String> meta;

	public Image(ImageSpec spec, File dbDump, File media)
	{
		this.spec = spec;
		this.dbDumpSql = dbDump;
		this.media = media;
	}

	public Image(ImageSpec spec, File dbDumpSql, File data, Map<String, String> meta)
	{
		this.spec = spec;
		this.dbDumpSql = dbDumpSql;
		this.data = data;
		this.meta = meta;
	}

	public ImageSpec getSpec()
	{
		return spec;
	}

	public File getDbDumpSql()
	{
		return dbDumpSql;
	}

	public File getMedia()
	{
		return media;
	}

	public File getBinDump()
	{
		return binDump;
	}

	public File getData()
	{
		return data;
	}

	public Map<String, String> getMeta()
	{
		return meta;
	}
}

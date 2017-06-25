package com.epam.hybris.has.client.model;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


/**
 */
public class Image
{
	private final ImageSpec spec;

	private final File dbDump;

	private final File media;

	public Image(ImageSpec spec, File dbDump, File media)
	{
		this.spec = spec;
		this.dbDump = dbDump;
		this.media = media;
	}

	public ImageSpec getSpec()
	{
		return spec;
	}

	public File getDbDump()
	{
		return dbDump;
	}

	public File getMedia()
	{
		return media;
	}
}

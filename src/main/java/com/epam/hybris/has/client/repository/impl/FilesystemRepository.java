package com.epam.hybris.has.client.repository.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;


/**
 */
public class FilesystemRepository extends AbstractHierarchicalRepository
{
	Path root;

	public FilesystemRepository(Path root)
	{
		this.root = root;
	}

	@Override
	protected void storeBinary(Path specPath, String resourceName, Path file) throws IOException
	{
		Path toPath = root.resolve(specPath).resolve(resourceName);
		if (! Files.exists(toPath)) {
			Files.createDirectories(toPath);
		}
		Files.move(file, toPath, StandardCopyOption.REPLACE_EXISTING);
	}

	@Override
	protected Path getResource(Path resourcePath)
	{
		return root.resolve(resourcePath);
	}
}

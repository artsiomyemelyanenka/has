package com.epam.hybris.has.client.repository.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.epam.hybris.has.client.model.Image;
import com.epam.hybris.has.client.model.ImageSpec;
import com.epam.hybris.has.client.repository.Repository;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 */
public abstract class AbstractHierarchicalRepository implements Repository
{

	public static final String DUMP_SQL = "dump.sql";
	public static final String DATA_ZIP = "data.zip";
	public static final String SPEC_JSON = "spec.json";

	@Override
	public void saveImage(Image image) throws IOException
	{
		Path rootPath = getPath(image.getSpec());
		storeBinary(rootPath, DUMP_SQL, image.getDbDumpSql().toPath());
		storeBinary(rootPath, DATA_ZIP, image.getData().toPath());
		if (image.getSpec() != null) {
			Path specPath = Files.createTempFile("spec", "json");
			ObjectMapper objMapper = new ObjectMapper();
			File specFile = specPath.toFile();
			objMapper.writeValue(specFile, image.getSpec());
			storeBinary(rootPath, SPEC_JSON, specPath);
		}
	}

	protected abstract void storeBinary(Path specPath, String resourceName, Path file) throws IOException;

	private Path getPath(ImageSpec spec)
	{
		return Paths.get(spec.getProjectName(), spec.getHybrisVersion(), spec.getBranchName());
	}

	@Override
	public Image loadImage(ImageSpec spec) throws IOException
	{
		Path imagePath = getPath(spec);
		if (! Files.exists(getResource(imagePath))) {
			return null;
		}
		Path sqlDbDump = getResource(imagePath.resolve(DUMP_SQL));
		Path dataZip = getResource(imagePath.resolve(DATA_ZIP));
		Path specFile = getResource(imagePath.resolve(SPEC_JSON));

		if (! Files.exists(sqlDbDump)) {
			throw new FileNotFoundException(sqlDbDump.toString());
		}
		if (! Files.exists(dataZip)) {
			throw new FileNotFoundException(dataZip.toString());
		}
		if (! Files.exists(specFile)) {
			throw new FileNotFoundException(specFile.toString());
		}
		ObjectMapper objMapper = new ObjectMapper();
		ImageSpec imageSpec = objMapper.readValue(specFile.toFile(), ImageSpec.class);
		Image image = new Image(imageSpec, sqlDbDump.toFile(), dataZip.toFile(), null);
		return image;
	}

	protected abstract Path getResource(Path imagePath);
}

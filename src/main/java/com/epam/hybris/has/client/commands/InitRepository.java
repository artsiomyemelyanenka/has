package com.epam.hybris.has.client.commands;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.epam.hybris.has.client.repository.impl.FilesystemRepository;


/**
 */
public class InitRepository implements Cmd
{
	@Override
	public void execute(CmdContext context)
	{
		Path root = Paths.get(context.getString("local.repo.root"));
		try
		{
			if (! Files.exists(root)) {
				Files.createDirectories(root);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			context.addErrorMessage("Can't create folder for local repo.");
			return;
		}
		context.setRepository(new FilesystemRepository(root.toAbsolutePath()));
	}
}

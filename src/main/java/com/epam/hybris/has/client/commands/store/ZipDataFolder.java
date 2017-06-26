package com.epam.hybris.has.client.commands.store;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;

import com.epam.hybris.has.client.commands.Cmd;
import com.epam.hybris.has.client.commands.CmdContext;
import com.epam.hybris.has.client.commands.Const;


/**
 */
public class ZipDataFolder implements Cmd
{
	@Override
	public void execute(CmdContext context)
	{
		context.process("Create data archive");
		Project p = new Project();
		p.init();
		Zip zipTask = new Zip();
		zipTask.setProject(p);
		File zipFile = new File("data" + System.currentTimeMillis() + ".zip");
		zipTask.setDestFile(zipFile);
		zipTask.setBasedir(new File(context.getBaseFolder(), "../../data/"));
		zipTask.perform();
		context.set(Const.DATA_ZIP_FILE, zipFile);
	}
}

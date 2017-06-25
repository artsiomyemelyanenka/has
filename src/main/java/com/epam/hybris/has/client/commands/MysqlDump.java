package com.epam.hybris.has.client.commands;

import static com.epam.hybris.has.client.commands.MysqlEnvExamination.MYSQLDUMP;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.WriterStreamConsumer;


/**
 */
public class MysqlDump implements Cmd
{
	public static final String DUMP_FILE = "dump.sql";
	public static final String DUMP_FOLDER = "dump";

	@Override
	public void execute(CmdContext context)
	{
		//mysqldump --user=hybris --password=hybris amway>./dump.sql
		Commandline commandline = new Commandline();
		commandline.setExecutable(MYSQLDUMP);
		commandline.addArguments(new String[] {//
				"--user=" + context.get(Const.DB_USERNAME), //
				"--password=" + context.get(Const.DB_PASSWORD), //
				context.getString(Const.DB_SCHEME)});
		File dumpFile = new File(DUMP_FOLDER, DUMP_FILE);
		if (dumpFile.exists()) {
			if (! dumpFile.delete()) {
				context.addErrorMessage("Can't delete " + dumpFile.getAbsolutePath());
				return;
			}
		}
		File workingDir = dumpFile.getParentFile();
		if (! workingDir.exists()) {
			if (! workingDir.mkdirs()) {
				context.addErrorMessage("Can't create" + workingDir.getAbsolutePath());
				return;
			}
		}

		commandline.setWorkingDirectory(workingDir);

		CommandLineUtils.StringStreamConsumer err = new CommandLineUtils.StringStreamConsumer();
		try (FileWriter fw = new FileWriter(dumpFile))
		{
			WriterStreamConsumer out = new WriterStreamConsumer(fw);
			context.process("Creating dump...");
			int exitCode = CommandLineUtils.executeCommandLine(commandline, out, err);
			if (exitCode != 0)
			{
				context.addErrorMessage(err.getOutput());
				return;
			}
		}
		catch (CommandLineException e)
		{
			context.addErrorMessage(e);
			return;
		}
		catch (IOException e)
		{
			context.addErrorMessage(e);
			return;
		}
	}
}

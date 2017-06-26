package com.epam.hybris.has.client.commands.store;

import static com.epam.hybris.has.client.commands.store.MysqlEnvExamination.MYSQLDUMP;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.WriterStreamConsumer;

import com.epam.hybris.has.client.commands.Cmd;
import com.epam.hybris.has.client.commands.CmdContext;
import com.epam.hybris.has.client.commands.Const;


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

		CommandLineUtils.StringStreamConsumer err = new CommandLineUtils.StringStreamConsumer();
		final File dumpFile;
		try
		{
			dumpFile = File.createTempFile("dump", "sql");
		}
		catch (IOException e)
		{
			e.printStackTrace();
			context.addErrorMessage("Can't create temp file");
			return;
		}

		try (FileWriter fw = new FileWriter(dumpFile))
		{
			WriterStreamConsumer out = new WriterStreamConsumer(fw);
			context.process("Creating dump...");
			int exitCode = CommandLineUtils.executeCommandLine(commandline, out, err);
			if (exitCode != 0)
			{
				context.addErrorMessage(err.getOutput());
				return;
			} else {
				context.set(Const.SQL_DUMP_FILE, dumpFile);
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

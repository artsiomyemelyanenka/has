package com.epam.hybris.has.client.commands.store;

import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

import com.epam.hybris.has.client.commands.Cmd;
import com.epam.hybris.has.client.commands.CmdContext;
import com.epam.hybris.has.client.commands.Const;


/**
 * Analyzes curretn mysql environment.
 */
public class MysqlEnvExamination implements Cmd
{

	public static final String MYSQLDUMP = "mysqldump";
	public static final String MYSQLDUMP_VERSION = "--version";

	@Override
	public void execute(CmdContext context)
	{
		Commandline commandline = new Commandline();
		commandline.setExecutable(MYSQLDUMP);
		commandline.addArguments(new String[] { MYSQLDUMP_VERSION });
		CommandLineUtils.StringStreamConsumer err = new CommandLineUtils.StringStreamConsumer();
		CommandLineUtils.StringStreamConsumer out = new CommandLineUtils.StringStreamConsumer();
		try
		{
			int exitCode = CommandLineUtils.executeCommandLine(commandline, out, err);
			if (exitCode != 0)
			{
				context.addErrorMessage(err.getOutput());
				return;
			}
			context.set(Const.MYSQL_VERSION, out.getOutput());
		}
		catch (CommandLineException e)
		{
			context.addErrorMessage(e);
			return;
		}
	}
}

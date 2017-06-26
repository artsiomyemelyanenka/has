package com.epam.hybris.has.client.commands.restore;

import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

import com.epam.hybris.has.client.commands.Cmd;
import com.epam.hybris.has.client.commands.CmdContext;
import com.epam.hybris.has.client.commands.Const;
import com.epam.hybris.has.client.model.Image;


/**
 */
public class RestoreDb implements Cmd
{
	@Override
	public void execute(CmdContext context)
	{
		context.process("Restore DB");
		Image image = (Image) context.get(Const.IMAGE);
		//mysql -u hybris -phybris amway <dump.sql
		Commandline commandline = new Commandline();
		commandline.setExecutable("mysql");

		commandline.addArguments(new String[] {//
				"--user=" + context.getString(Const.DB_USERNAME),//
				"--password=" + context.get(Const.DB_PASSWORD),//
				context.getString(Const.DB_SCHEME),//
				"-e",//
				" source " + image.getDbDumpSql().getAbsolutePath()});
		context.message(commandline.toString());
		CommandLineUtils.StringStreamConsumer err = new CommandLineUtils.StringStreamConsumer();
		CommandLineUtils.StringStreamConsumer out = new CommandLineUtils.StringStreamConsumer();
		try
		{
			int exitCode = CommandLineUtils.executeCommandLine(commandline, out, err);
			if (exitCode != 0)
			{
				context.addErrorMessage("Error restoring db dump");
			} else {
				context.message("DB restored.");
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}

package com.epam.hybris.has.client;

//import org.springframework.shell.Bootstrap;

import java.util.Arrays;
import java.util.List;

import com.epam.hybris.has.client.commands.Cmd;
import com.epam.hybris.has.client.commands.Context;
import com.epam.hybris.has.client.commands.DisplayConfig;
import com.epam.hybris.has.client.commands.ExtractDbScheme;
import com.epam.hybris.has.client.commands.GetGitCurrentBranch;
import com.epam.hybris.has.client.commands.LoadDbInfo;
import com.epam.hybris.has.client.commands.LoadHybrisInfo;
import com.epam.hybris.has.client.commands.LoadSettingsFromProperties;
import com.epam.hybris.has.client.commands.MysqlDump;
import com.epam.hybris.has.client.commands.MysqlEnvExamination;
import com.epam.hybris.has.client.commands.image.RestoreDb;
import com.epam.hybris.has.client.commands.repo.simple.DownloadImage;
import com.epam.hybris.has.client.commands.repo.simple.FindImage;


/**
 * POC code quality :)
 */
public class Main
{
	public static void main(String[] args)
	{
		Context ctx = new Context();
		//doDump(ctx);
		Cmd[] restoreDump = new Cmd[] {new LoadHybrisInfo(), new LoadDbInfo(), new ExtractDbScheme(), new GetGitCurrentBranch(),
				new MysqlEnvExamination(),
				new LoadSettingsFromProperties(),
				new FindImage(),
				new DownloadImage(),
				new RestoreDb()};
		execute(ctx, Arrays.asList(restoreDump));
	}

	private static void doDump(Context ctx)
	{
		Cmd[] createDump = new Cmd[] {new LoadHybrisInfo(), new LoadDbInfo(), new ExtractDbScheme(), new GetGitCurrentBranch(),
			new MysqlEnvExamination(),
			new MysqlDump(),
			new DisplayConfig()};

		List<Cmd> cmds = Arrays.asList(createDump);
		execute(ctx, cmds);
	}

	private static void execute(Context ctx, List<Cmd> cmds)
	{
		for (Cmd c: cmds) {
			c.execute(ctx);
			if (ctx.hasErrors()) {
				System.err.println(ctx.getLastError());
			}
		}
		ctx.close();
	}
}

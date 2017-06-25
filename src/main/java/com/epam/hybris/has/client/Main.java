package com.epam.hybris.has.client;

import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

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
import com.epam.hybris.has.client.commands.image.RestoreMedia;
import com.epam.hybris.has.client.commands.repo.simple.DownloadImage;
import com.epam.hybris.has.client.commands.repo.simple.FindImage;


/**
 * POC code quality :)
 */
public class Main
{
	public static void main(String[] args)
	{
		Runnable checkVersionTask = new CheckVersion();
		new Thread(checkVersionTask).start();

		Context ctx = new Context();
		Cmd[] restoreDump = new Cmd[] {new LoadHybrisInfo(), new LoadDbInfo(), new ExtractDbScheme(), new GetGitCurrentBranch(),
				new MysqlEnvExamination(),
				new LoadSettingsFromProperties(),
				new FindImage(),
				new DownloadImage(),
				new RestoreDb(),
				new RestoreMedia()};
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

	static class CheckVersion implements Runnable {
		final static String URL_VERSION = "https://raw.githubusercontent.com/artsiomyemelyanenka/has/master/src/main/resources/version.properties";
		final static String VERSION = "version";
		final static String VERSION_PROPERTIES = "version.properties";
		@Override
		public void run()
		{
			InputStream remoteStream = null;
			InputStream localStream = null;
			try
			{
				URL file = new URL(URL_VERSION);
				Properties p = new Properties();
				remoteStream = file.openStream();
				p.load(remoteStream);
				String remoteVersion = p.getProperty(VERSION);
				localStream = Main.class.getClassLoader().getResourceAsStream(VERSION_PROPERTIES);
				p.load(localStream);
				String localVersion = p.getProperty(VERSION);
				if (! StringUtils.equals(remoteVersion, localVersion)) {
					System.out.println("Local version is " + localVersion + ". Version " + remoteVersion + " is available.");
				}
			}
			catch (Exception e)
			{
				IOUtils.closeQuietly(remoteStream);
				IOUtils.closeQuietly(localStream);
			}
		}
	}
}

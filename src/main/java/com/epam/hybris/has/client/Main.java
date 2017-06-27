package com.epam.hybris.has.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.epam.hybris.has.client.commands.Cmd;
import com.epam.hybris.has.client.commands.Const;
import com.epam.hybris.has.client.commands.Context;
import com.epam.hybris.has.client.commands.DisplayConfig;
import com.epam.hybris.has.client.commands.ExtractDbScheme;
import com.epam.hybris.has.client.commands.GetGitCurrentBranch;
import com.epam.hybris.has.client.commands.InitRepository;
import com.epam.hybris.has.client.commands.LoadDbInfo;
import com.epam.hybris.has.client.commands.LoadHybrisInfo;
import com.epam.hybris.has.client.commands.LoadSettingsFromProperties;
import com.epam.hybris.has.client.commands.restore.BeforeRestoreCheckContext;
import com.epam.hybris.has.client.commands.restore.FindImageInRepository;
import com.epam.hybris.has.client.commands.restore.RestoreData;
import com.epam.hybris.has.client.commands.restore.RestoreDb;
import com.epam.hybris.has.client.commands.restore.RestoreMedia;
import com.epam.hybris.has.client.commands.store.MysqlDump;
import com.epam.hybris.has.client.commands.store.MysqlEnvExamination;
import com.epam.hybris.has.client.commands.repo.simple.DownloadImage;
import com.epam.hybris.has.client.commands.repo.simple.FindImage;
import com.epam.hybris.has.client.commands.store.BeforeStoreCheckContext;
import com.epam.hybris.has.client.commands.store.StoreImage;
import com.epam.hybris.has.client.commands.store.ZipDataFolder;


/**
 * POC code quality :)
 */
public class Main
{
	public static void main(String[] args)
	{
		Runnable checkVersionTask = new CheckVersion();
		new Thread(checkVersionTask).start();

		if (args.length < 1) {
			showInstruction();
			return;
		}
		String mode = args[0];
		String branchName = args.length > 1 ? args[1] : null;
		if ("restoreSimple".equals(mode)) {
			doRestoreSimple(branchName);
		} else if ("store".equals(mode)) {
			doStore(branchName);
		} else if ("restore".equals(mode)) {
			doRestore(branchName);
		} else {
			System.err.println("Wrong mode " + mode);
		}
	}

	private static void doRestore(String branchName)
	{
		Context ctx = new Context();
		if (branchName != null) {
			ctx.set(Const.GIT_BRANCH, branchName);
		}
		Cmd[] restoreDump = new Cmd[] {
				new LoadHybrisInfo(),
				new GetGitCurrentBranch(),
				new LoadSettingsFromProperties(),
				new MysqlEnvExamination(),
				new InitRepository(),
				new LoadDbInfo(),
				new ExtractDbScheme(),
				new BeforeRestoreCheckContext(),
				new FindImageInRepository(),
				new RestoreDb(),
				new RestoreData()};
		execute(ctx, Arrays.asList(restoreDump));
	}

	private static void doStore(String branchName)
	{
		Context ctx = new Context();
		if (branchName != null) {
			ctx.set(Const.GIT_BRANCH, branchName);
		}
		Cmd[] restoreDump = new Cmd[] {
				new LoadHybrisInfo(),
				new LoadSettingsFromProperties(),
				new MysqlEnvExamination(),
				new InitRepository(),
				new GetGitCurrentBranch(),
				new LoadDbInfo(),
				new ExtractDbScheme(),
				new BeforeStoreCheckContext(),
				new DisplayConfig(),
				new MysqlDump(),
				new ZipDataFolder(),
				new StoreImage()};
		execute(ctx, Arrays.asList(restoreDump));
	}

	private static void showInstruction()
	{
		try (InputStream re = Main.class.getClassLoader().getResourceAsStream("instruction.txt"))
		{
			IOUtils.copy(re, System.out);
		}
		catch (IOException e)
		{
			//
		}
	}

	private static void doRestoreSimple(String branchName)
	{
		Context ctx = new Context();
		if (branchName != null) {
			ctx.set(Const.GIT_BRANCH, branchName);
		}
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
				break;
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

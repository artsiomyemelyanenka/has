package com.epam.hybris.has.client.commands;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.stereotype.Component;


/**
 */
@Component("loadDbInfo")
public class LoadDbInfo implements Cmd
{

	public void execute(CmdContext context)
	{
		File configFile = new File(context.getBaseFolder(), "../../config/local.properties");
		if (!configFile.exists()) {
			System.err.println("Can't find config folder at " + configFile.getAbsolutePath());
			return;
		}
		PropertiesConfiguration p = new PropertiesConfiguration();
		try
		{
			p.load(configFile);
			String dbDriver = p.getString(Const.DB_DRIVER);
			if (!"com.mysql.jdbc.Driver".equals(dbDriver)) {
				System.err.println("I can only work with com.mysql.jdbc.Driver driver so far. Sorry.");
				return;
			}
			String userName = p.getString(Const.DB_USERNAME);
			String password = p.getString(Const.DB_PASSWORD);
			String dbUri = p.getString(Const.DB_URL);
			context.set(Const.DB_USERNAME, userName);
			context.set(Const.DB_PASSWORD, password);
			context.set(Const.DB_URL, dbUri);
		}
		catch (ConfigurationException e)
		{
			e.printStackTrace();
			System.err.println("Error reading hybris config.");
		}

	}

}

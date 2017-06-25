package com.epam.hybris.has.client.commands;

import static com.epam.hybris.has.client.commands.Const.HYBRIS_VERSION;

import java.io.File;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.stereotype.Component;


/**
 */
@Component("loadDbInfo")
public class LoadHybrisInfo implements Cmd
{
	public void execute(CmdContext context)
	{
		File buildProps = new File(context.getBaseFolder(), "build.number");
		PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration();
		try
		{
			propertiesConfiguration.load(buildProps);
			String hybrisVersion = propertiesConfiguration.getString("version");
			context.set(HYBRIS_VERSION, hybrisVersion);
		}
		catch (ConfigurationException e)
		{
			System.err.println("Can't find buils.properties. Please, run at hybris/bin/platform folder.");
		}
	}

}

package com.epam.hybris.has.client.commands;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.epam.hybris.has.client.Main;


/**
 */
public class LoadSettingsFromProperties implements Cmd
{

	public static final String HAS_PROPERTIES = "has.properties";

	@Override
	public void execute(CmdContext context)
	{
		File propertiesFile = new File(context.getBaseFolder(), HAS_PROPERTIES);
		PropertiesConfiguration props = new PropertiesConfiguration();
		if (!propertiesFile.exists())
		{
			context.message("Can't find file " + HAS_PROPERTIES + " in the working folder. Will load it from internal resources.");
			try (InputStream re = Main.class.getClassLoader().getResourceAsStream("has.properties"))
			{
				props.load(re);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				context.addErrorMessage(e);
				return;
			}
		}
		else
		{
			try
			{
				props.load(propertiesFile);
			}
			catch (ConfigurationException e)
			{
				e.printStackTrace();
				context.addErrorMessage(e);
				return;
			}
		}
		Iterator keys = props.getKeys();
		while (keys.hasNext())
		{
			String key = keys.next().toString();
			context.set(key, props.getString(key));
		}
	}

}

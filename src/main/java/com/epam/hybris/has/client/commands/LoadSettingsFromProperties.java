package com.epam.hybris.has.client.commands;

import java.io.File;
import java.util.Iterator;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;


/**
 */
public class LoadSettingsFromProperties implements Cmd
{

	public static final String HAS_PROPERTIES = "has.properties";

	@Override
	public void execute(CmdContext context)
	{
		File propertiesFile = new File(context.getBaseFolder(), HAS_PROPERTIES);
		if (! propertiesFile.exists()) {
			context.addErrorMessage("Can't find file " + HAS_PROPERTIES + " in the working folder.");
			return;
		}
		PropertiesConfiguration props = new PropertiesConfiguration();
		try
		{
			props.load(propertiesFile);
			Iterator keys = props.getKeys();
			while(keys.hasNext()) {
				String key = keys.next().toString();
				context.set(key, props.getString(key));
			}
		}
		catch (ConfigurationException e)
		{
			e.printStackTrace();
		}

	}

}

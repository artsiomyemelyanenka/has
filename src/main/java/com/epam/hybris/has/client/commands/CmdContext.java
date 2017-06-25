package com.epam.hybris.has.client.commands;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.util.cli.CommandLineException;
import org.springframework.stereotype.Component;

@Component
public interface CmdContext
{
	Object get(String key);

	String getString(String key);

	void set(String key, Object value);

	File getBaseFolder();

	File getCacheFolder() throws IOException;

	void addErrorMessage(String output);

	void addErrorMessage(Exception e);

	boolean hasErrors();

	void process(String s);

	void message(String s);

	void addResource(String key, Closeable res);

	Closeable getResource(String key);
}

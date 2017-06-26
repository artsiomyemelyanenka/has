package com.epam.hybris.has.client.commands;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.epam.hybris.has.client.repository.Repository;


/**
 */
@Component("simpleContext")
public class Context implements CmdContext, Closeable
{

	private final File userDir;
	private Map<String, Object> context;
	private Map<String, Closeable> resources;
	private String error;
	private Repository repository;

	public Context()
	{
		context = new HashMap<>();
		resources = new HashMap<>();
		String userDir = System.getProperty("user.dir");
		this.userDir = new File(userDir);
	}

	public Object get(String key)
	{
		return context.get(key);
	}

	public void set(String key, Object value)
	{
		context.put(key, value);
	}

	public File getBaseFolder()
	{
		return userDir;
	}

	@Override
	public String getString(String key)
	{
		Object value = get(key);
		if (value == null) {
			return null;
		}
		return String.valueOf(value);
	}

	@Override
	public void addErrorMessage(String output)
	{
		this.error = output;
	}

	@Override
	public void addErrorMessage(Exception e)
	{
		e.printStackTrace();
		addErrorMessage(e.getMessage());
	}

	public String getLastError()
	{
		return error;
	}

	@Override
	public boolean hasErrors()
	{
		return error != null;
	}

	@Override
	public void process(String s)
	{
		System.out.println(s);
	}

	@Override
	public void message(String s)
	{
		System.out.println(s);
	}

	@Override
	public void addResource(String key, Closeable res) {
		resources.put(key, res);
	}

	@Override
	public Closeable getResource(String key) {
		return resources.get(key);
	}

	public void close() {
		resources.entrySet().forEach(e ->
		{
			try
			{
				e.getValue().close();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		});
	}

	@Override
	public File getCacheFolder() throws IOException
	{
		File cacheFolder = new File(getBaseFolder(), ".hascache");
		if (! cacheFolder.exists()) {
			if (! cacheFolder.mkdirs()) {
				throw new IOException("Can't create cache folder");
			}
		}
		return cacheFolder;
	}

	@Override
	public Repository getRepository()
	{
		return repository;
	}

	@Override
	public void setRepository(Repository repository)
	{
		this.repository = repository;
	}
}


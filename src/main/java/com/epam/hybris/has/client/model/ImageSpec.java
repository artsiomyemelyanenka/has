package com.epam.hybris.has.client.model;

/**
 */
public class ImageSpec
{
	private String projectName;

	private String branchName;

	private String hybrisVersion;

	public ImageSpec()
	{
	}

	public String getBranchName()
	{
		return branchName;
	}

	public void setBranchName(String branchName)
	{
		this.branchName = branchName;
	}

	public String getProjectName()
	{
		return projectName;
	}

	public void setProjectName(String projectName)
	{
		this.projectName = projectName;
	}

	public String getHybrisVersion()
	{
		return hybrisVersion;
	}

	public void setHybrisVersion(String hybrisVersion)
	{
		this.hybrisVersion = hybrisVersion;
	}
}

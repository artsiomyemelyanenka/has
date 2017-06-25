package com.epam.hybris.has.client.commands;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;


/**
 */
public class GetGitCurrentBranch implements Cmd
{

	@Override
	public void execute(CmdContext context)
	{
		File gitWorkDir = new File(context.getBaseFolder(), "../custom");
		try
		{
			Git git = Git.open(gitWorkDir);
			Repository repository = git.getRepository();
			context.set(Const.GIT_BRANCH, repository.getBranch());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}

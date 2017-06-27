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
		if (context.get(Const.GIT_BRANCH) != null) {
			context.message("Skip branch detection. Use " + context.get(Const.GIT_BRANCH));
			return;
		}
		File gitWorkDir = new File(context.getBaseFolder(), "../custom");
		try
		{
			Git git = Git.open(gitWorkDir);
			if (git == null) {
				noGitCase(context);
				return;
			}
			Repository repository = git.getRepository();
			if (repository == null) {
				noGitCase(context);
				return;
			}
			context.set(Const.GIT_BRANCH, repository.getBranch());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void noGitCase(CmdContext context)
	{
		context.message("No git repository found. Using technical branch 'nogit'");
		context.set(Const.GIT_BRANCH, "nogit");
	}
}

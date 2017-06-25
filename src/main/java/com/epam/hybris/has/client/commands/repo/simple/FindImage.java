package com.epam.hybris.has.client.commands.repo.simple;

import static com.epam.hybris.has.client.commands.Const.IMAGE;

import java.net.URL;

import com.epam.hybris.has.client.commands.Cmd;
import com.epam.hybris.has.client.commands.CmdContext;
import com.epam.hybris.has.client.commands.Const;
import com.epam.hybris.has.client.model.Image;
import com.epam.hybris.has.client.model.ImageSpec;


/**
 */
public class FindImage implements Cmd
{

	public static final String DEVELOP = "develop";
	public static final String LINK_DB_DEVELOP = "link.db.develop";
	public static final String LINK_MEDIA_DEVELOP = "link.media.develop";
	public static final String LINK_DB_RELEASE = "link.db.release";
	public static final String LINK_MEDIA_RELEASE = "link.media.release";

	@Override
	public void execute(CmdContext context)
	{
		String gitBranch = context.getString(Const.GIT_BRANCH);

		String linkDb, linkMedia;
		boolean isDevelop = DEVELOP.equals(gitBranch) || gitBranch.startsWith("fix/");
		String branchNameSpec;
		if (isDevelop) {
			linkDb = context.getString(LINK_DB_DEVELOP);
			linkMedia = context.getString(LINK_MEDIA_DEVELOP);
			branchNameSpec = "develop";
		} else  {
			linkDb = context.getString(LINK_DB_RELEASE);
			linkMedia = context.getString(LINK_MEDIA_RELEASE);
			branchNameSpec = "release";
		}
		try
		{
			CacheManager cm = new CacheManager(context);
			ImageSpec spec = new ImageSpec();
			spec.setBranchName(branchNameSpec);
			Image image = cm.extract(spec);
			if (image == null) {
				URL db = new URL(linkDb);
				URL media = new URL(linkMedia);
				context.message("DB: " + linkDb);
				context.message("Media: " + linkDb);
				context.process("Opening resources. If hangs, check your connection and VPN.");
				context.addResource(Const.REMOTE_DUMP_STREAM, db.openStream());
				context.addResource(Const.REMOTE_MEDIA_STREAM, media.openStream());
				context.message("Links are ok. Repository is reachable.");
				context.set(Const.REMOTE_IMAGE_SPEC, spec);
			} else {
				context.message("Image found in cache.");
				context.set(IMAGE, image);
			}
		}
		catch (Exception e)
		{
			context.addErrorMessage("Can't download image.");
			e.printStackTrace();
		}

	}


}

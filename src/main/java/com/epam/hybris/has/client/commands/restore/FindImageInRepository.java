package com.epam.hybris.has.client.commands.restore;

import java.io.IOException;

import com.epam.hybris.has.client.commands.Cmd;
import com.epam.hybris.has.client.commands.CmdContext;
import com.epam.hybris.has.client.commands.Const;
import com.epam.hybris.has.client.model.Image;
import com.epam.hybris.has.client.model.ImageSpec;


/**
 */
public class FindImageInRepository implements Cmd
{
	@Override
	public void execute(CmdContext context)
	{
		ImageSpec spec = new ImageSpec();
		spec.setBranchName(context.getString(Const.GIT_BRANCH));
		spec.setHybrisVersion(context.getString(Const.HYBRIS_VERSION));
		spec.setProjectName(context.getString(Const.PROJECT_NAME));
		try
		{
			Image image = context.getRepository().loadImage(spec);
			if (image == null) {
				context.addErrorMessage("Can't find appropriate image.");
				return;
			}
			context.set(Const.IMAGE, image);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			context.addErrorMessage("Can'r find image in the repository");
		}
	}
}

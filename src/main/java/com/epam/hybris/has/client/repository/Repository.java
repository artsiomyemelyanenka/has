package com.epam.hybris.has.client.repository;

import java.io.IOException;

import com.epam.hybris.has.client.model.Image;
import com.epam.hybris.has.client.model.ImageSpec;


/**
 */
public interface Repository
{
	void saveImage(Image image) throws IOException;

	Image loadImage(ImageSpec spec) throws IOException;

}

package ca.bc.gov.webade.developer.xml;

import java.io.File;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.webade.developer.WebADEDeveloperException;

/**
 * Caches the XML file binding based on last modified time of the file.
 * 
 * @author Vivid Solutions Inc
 */
public class XMLFileBindingCache implements Serializable {

	private static final long serialVersionUID = -9106330895545881314L;
	
	private static final Logger logger = LoggerFactory.getLogger(XMLFileBindingCache.class);

	private XMLFileBinding binding;
	private File bindingFile;
	private long timeLastLoaded;

	public XMLFileBindingCache(File file) throws WebADEDeveloperException {
		bindingFile = file;
		timeLastLoaded = 0;
		if (logger.isDebugEnabled()) {
			logger.debug("bindingFile = " + bindingFile);
			logger.debug("timeLastLoaded = " + timeLastLoaded);
		}
		loadBinding();
	}

	public XMLFileBinding loadBinding() throws WebADEDeveloperException {
		if (bindingFile != null && bindingFile.exists() && bindingFile.canRead()) {
			if (logger.isDebugEnabled()) {
				logger.debug("timeLastLoaded = " + timeLastLoaded);
				logger.debug("bindingFile.lastModified() = " + bindingFile.lastModified());
			}
			if (bindingFile.lastModified() > timeLastLoaded) {
				binding = new XMLFileBinding(bindingFile);
				if (logger.isDebugEnabled()) {
					logger.debug("binding = " + binding);
				}
				timeLastLoaded = bindingFile.lastModified();
			}
		} else {
			throw new WebADEDeveloperException("File " + bindingFile + " does not exist or cannot be read.");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("binding = " + binding);
		}
		return binding;
	}
}

/*******************************************************************************
 * Copyright (c) 2012  Egon Willighagen <egon.willighagen@gmail.com>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.sbml.business;

import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.managers.business.IBioclipseManager;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;

public class SbmlManager implements IBioclipseManager {

    private static final Logger logger = Logger.getLogger(SbmlManager.class);

    /**
     * Gives a short one word name of the manager used as variable name when
     * scripting.
     */
    public String getManagerName() {
        return "sbml";
    }

    public SBMLDocument importFile(IFile target, IProgressMonitor monitor) throws BioclipseException {
    	logger.debug("Reading this SMBL file: " + target);
    	SBMLReader reader = new SBMLReader();
    	try {
			SBMLDocument doc = reader.readSBMLFromStream(target.getContents());
			return doc;
		} catch (Exception exception) {
			throw new BioclipseException("Error while reading the SBML file: " + exception.getMessage(), exception);
		}
    }
}

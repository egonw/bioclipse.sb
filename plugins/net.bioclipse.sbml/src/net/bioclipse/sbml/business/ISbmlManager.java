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

import net.bioclipse.core.PublishedClass;
import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.Recorded;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.managers.business.IBioclipseManager;

import org.sbml.jsbml.SBMLDocument;

@PublishedClass(
    value="Manager that uses JSBML to provide support for the SBML format.",
    doi="10.1093/bioinformatics/btr361"
)
public interface ISbmlManager extends IBioclipseManager {

    @Recorded
    @PublishedMethod(
        params = "String file",
        methodSummary = "Loads an SMBL file"
    )
    public SBMLDocument importFile(String target) throws BioclipseException;

}

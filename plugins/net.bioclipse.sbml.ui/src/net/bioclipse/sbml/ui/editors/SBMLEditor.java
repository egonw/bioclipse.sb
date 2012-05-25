/*
 * Copyright (c) 2012  Egon Willighagen <egon.willighagen@gmail.com>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.sbml.ui.editors;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

import net.bioclipse.sbml.business.SbmlManager;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.sbml.jsbml.AbstractSBase;
import org.sbml.jsbml.AbstractTreeNode;
import org.sbml.jsbml.KineticLaw;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.Species;

public class SBMLEditor extends EditorPart implements ISelectionProvider {

	public SBMLEditor() {
		super();
	}

	public void dispose() {
		super.dispose();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(IEditorSite site, IEditorInput editorInput)
			throws PartInitException {
		setPartName(editorInput.getName());
        setSite(site);
        setInput(editorInput);
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
        //Set the layout for parent
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.verticalSpacing = 2;
        layout.marginWidth = 0;
        layout.marginHeight = 2;
        parent.setLayout(layout);

        GridData layoutData = new GridData();
        layoutData.grabExcessHorizontalSpace = true;
        layoutData.grabExcessVerticalSpace = true;
        parent.setLayoutData(layoutData);

        //Add the Jmol composite to the top
        Composite composite = new Composite( parent, SWT.NO_BACKGROUND 
                                                   | SWT.EMBEDDED
                                                   | SWT.DOUBLE_BUFFERED );
        layout = new GridLayout();
        composite.setLayout(layout);
        layoutData = new GridData(GridData.FILL_BOTH);
        composite.setLayoutData(layoutData);

        java.awt.Frame awtFrame = SWT_AWT.new_Frame(composite);
        java.awt.Panel awtPanel 
            = new java.awt.Panel(new java.awt.BorderLayout());
        awtFrame.add(awtPanel);

		IEditorInput input = getEditorInput();

		SbmlManager sbml = new SbmlManager();
		try {
			if ((input instanceof IFileEditorInput) && 
					((IFileEditorInput)input).getFile().exists()) {
				SBMLDocument doc = sbml.importFile(((IFileEditorInput)input).getFile(), null);
				final JTree tree = new JTree(doc);
				tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

				//Listen for when the selection changes.
				tree.addTreeSelectionListener(new TreeSelectionListener() {
					@Override
					public void valueChanged(TreeSelectionEvent treeElement) {
						// a new tree node has been selected
						AbstractTreeNode node = (AbstractTreeNode)
								tree.getLastSelectedPathComponent();
						if (node == null) return;
						setSelection(new StructuredSelection(new SBMLElement(node)));
					}
				});
		        JScrollPane scrollPane = new JScrollPane(tree);
		        awtPanel.add(scrollPane);    
			} else {
				System.out.println("POOP");
			}
		} catch (Exception exception) {
			System.out.println(exception);
		}

		// set the selection provider
		getSite().setSelectionProvider(this);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
	
	private ListenerList listeners = new ListenerList(); 

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		listeners.add(listener);
	}

	private ISelection selection;

	@Override
	public ISelection getSelection() {
		return selection;
	}

	@Override
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void setSelection(ISelection selection) {
		this.selection = selection;
		Object[] list = listeners.getListeners();  
		for (int i = 0; i < list.length; i++) {
			final ISelectionChangedListener listener = (ISelectionChangedListener) list[i];
	        final SelectionChangedEvent event = new SelectionChangedEvent(this, this.selection);
	        //Does SWT stuff so this has to be called on SWT's thread
	        PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
	            public void run() {
	                listener.selectionChanged(event);
	            }
            });
		}
	}

	class SBMLElement implements IPropertySource {

		private static final String ELEMENT_TYPE = "net.bioclipse.smbl.elementType";
		private static final String LABEL = "net.bioclipse.smbl.label";
		private static final String VALUE = "net.bioclipse.smbl.value";
		private static final String ANNOTATION = "net.bioclipse.smbl.annotation";

		private AbstractTreeNode foo;
		
		SBMLElement(AbstractTreeNode foo) {
			this.foo = foo;
		}
		
		@Override
		public Object getEditableValue() {
			return null;
		}

		@Override
		public IPropertyDescriptor[] getPropertyDescriptors() {
			IPropertyDescriptor[] descriptor = new IPropertyDescriptor[4];
			descriptor[0] = new PropertyDescriptor(ELEMENT_TYPE, "Element Type");
			descriptor[1] = new PropertyDescriptor(LABEL, "Label");
			descriptor[2] = new PropertyDescriptor(VALUE, "Value");
			descriptor[3] = new PropertyDescriptor(ANNOTATION, "Annotation");
			return descriptor;
		}

		@Override
		public Object getPropertyValue(Object id) {
			if (ELEMENT_TYPE.equals(id)) return this.foo.getClass().getName();
			if (LABEL.equals(id)) return this.foo.toString();
			if (ANNOTATION.equals(id)) return getAnnotation(this.foo);
			if (VALUE.equals(id)) return getValue(this.foo);
			return this.foo;
		}

		private Object getAnnotation(AbstractTreeNode foo2) {
			if (foo2 instanceof AbstractSBase) {
				AbstractSBase base = (AbstractSBase)foo2;
				if (base.getCVTerms().size() > 0) return base.getCVTerm(0); // pick the first
			}
			return "";
		}

		private Object getValue(AbstractTreeNode foo2) {
			if (foo2 instanceof KineticLaw) {
				return ((KineticLaw)foo2).getFormula();
			}
			if (foo2 instanceof Species) {
				Species species = (Species)foo2;
				return species.getInitialConcentration() + species.getUnits();
			}
			return "";
		}

		@Override
		public boolean isPropertySet(Object id) {
			return true;
		}

		@Override
		public void resetPropertyValue(Object id) {
			// ignore
		}

		@Override
		public void setPropertyValue(Object id, Object value) {
			// ignore
		}
		
	}

}

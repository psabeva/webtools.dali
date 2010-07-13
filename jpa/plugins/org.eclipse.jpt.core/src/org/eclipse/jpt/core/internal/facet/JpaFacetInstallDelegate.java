/*******************************************************************************
 * Copyright (c) 2006, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.facet;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jpt.core.JptCorePlugin;
import org.eclipse.jpt.core.internal.operations.JpaFileCreationDataModelProperties;
import org.eclipse.jpt.core.internal.operations.OrmFileCreationDataModelProvider;
import org.eclipse.jpt.core.internal.operations.PersistenceFileCreationDataModelProvider;
import org.eclipse.jpt.db.JptDbPlugin;
import org.eclipse.jpt.utility.internal.ArrayTools;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;

/**
 * We don't really "install" the JPA facet here. We simply store all the various
 * data model properties in the appropriate preferences. These settings will
 * used in the POST_INSTALL event listener to build the JPA project.
 */
public class JpaFacetInstallDelegate 
	extends JpaFacetActionDelegate
	implements JpaFacetInstallDataModelProperties
{
	@Override
	protected void execute_(
			IProject project, IProjectFacetVersion fv, 
			Object config, IProgressMonitor monitor) throws CoreException {
		
		// NB: WTP Natures (including the JavaEMFNature)
		// should already be added, as this facet should 
		// always coexist with a module facet.
		SubMonitor sm = SubMonitor.convert(monitor, 10);
		
		super.execute_(project, fv, config, sm.newChild(1));
		IJavaProject javaProject = JavaCore.create(project);
		IDataModel dataModel = (IDataModel) config;
		
		// project settings
		this.addDbDriverLibraryToClasspath(javaProject, dataModel, sm.newChild(1));

		// create project XML files
		this.createProjectXml(project, dataModel.getBooleanProperty(CREATE_ORM_XML), sm.newChild(8));
	}
	
	protected void addDbDriverLibraryToClasspath(
			IJavaProject javaProject, IDataModel dataModel, 
			IProgressMonitor monitor) throws CoreException {
		
		if( ! dataModel.getBooleanProperty(USER_WANTS_TO_ADD_DB_DRIVER_JARS_TO_CLASSPATH)) {
			return;
		}
		String driverName = dataModel.getStringProperty(DB_DRIVER_NAME);
		
		IClasspathContainer container = JptDbPlugin.instance().buildDriverClasspathContainerFor(driverName);
		IClasspathEntry entry = JavaCore.newContainerEntry(container.getPath());
		this.addClasspathEntryToProject(entry, javaProject, monitor);
	}
	
	private void addClasspathEntryToProject(
			IClasspathEntry classpathEntry, IJavaProject javaProject, IProgressMonitor monitor) 
			throws CoreException {
		
		// if the classpathEntry is already there, do nothing
		IClasspathEntry[] classpath = javaProject.getRawClasspath();
		if (ArrayTools.contains(classpath, classpathEntry)) {
			return;
		}
		
		// add the given classpathEntry to the project classpath
		int len = classpath.length;
		IClasspathEntry[] newClasspath = new IClasspathEntry[len + 1];
		System.arraycopy(classpath, 0, newClasspath, 0, len);
		newClasspath[len] = classpathEntry;
		javaProject.setRawClasspath(newClasspath, monitor);
	}
	
	private void createProjectXml(IProject project, boolean buildOrmXml, IProgressMonitor monitor) {
		int tasks = 1 + (buildOrmXml ? 1 : 0);
		SubMonitor sm = SubMonitor.convert(monitor, tasks);

		this.createPersistenceXml(project, sm.newChild(1));
		if (buildOrmXml) {
			this.createOrmXml(project, sm.newChild(1));
		}
	}

	private void createPersistenceXml(IProject project, IProgressMonitor monitor) {
		SubMonitor sm = SubMonitor.convert(monitor, 5);

		IDataModel config = DataModelFactory.createDataModel(new PersistenceFileCreationDataModelProvider());
		config.setProperty(JpaFileCreationDataModelProperties.PROJECT_NAME, project.getName());
		sm.worked(1);
		// default values for all other properties should suffice
		try {
			config.getDefaultOperation().execute(sm.newChild(4), null);
		} catch (ExecutionException ex) {
			JptCorePlugin.log(ex);
		}
	}

	private void createOrmXml(IProject project, IProgressMonitor monitor) {
		SubMonitor sm = SubMonitor.convert(monitor, 5);

		IDataModel config = DataModelFactory.createDataModel(new OrmFileCreationDataModelProvider());
		config.setProperty(JpaFileCreationDataModelProperties.PROJECT_NAME, project.getName());
		sm.worked(1);
		// default values for all other properties should suffice
		try {
			config.getDefaultOperation().execute(sm.newChild(4), null);
		} catch (ExecutionException ex) {
			JptCorePlugin.log(ex);
		}
	}
}

/*******************************************************************************
 * Copyright (c) 2018 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.swtbot.testing.api;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.swtbot.testing.lowlevel.XtextSWTBotView;
import org.eclipse.xtext.swtbot.testing.lowlevel.XtextSWTWorkbenchBot;
import org.eclipse.xtext.ui.testing.util.IResourcesSetupUtil;

/**
 * Starter class for all SWTBot tests. Provides static methods to access all parts of the IDE.
 * 
 * @author Arne Deutsch - Initial contribution and API
 */
public class EclipseAPI {

	private static Logger log = Logger.getLogger(EclipseAPI.class);
	private static XtextSWTWorkbenchBot bot = new XtextSWTWorkbenchBot();

	private EclipseAPI() {
	}

	public static MainMenuAPI mainMenu() {
		return new MainMenuAPI(bot);
	}

	public static PackageExplorerAPI packageExplorer() {
		return new PackageExplorerAPI(activateViewById("org.eclipse.jdt.ui.PackageExplorer"));
	}

	public static ProblemsViewAPI problemsView() {
		return new ProblemsViewAPI(activateViewById("org.eclipse.ui.views.ProblemView"));
	}

	public static JUnitViewAPI junitView() {
		return new JUnitViewAPI(activateViewById("org.eclipse.jdt.junit.ResultView"));
	}

	public static ConsoleViewAPI consoleView() {
		return new ConsoleViewAPI(activateViewById("org.eclipse.ui.console.ConsoleView"));
	}

	public static OutlineViewAPI outlineView() {
		return new OutlineViewAPI(activateViewById("org.eclipse.ui.views.ContentOutline"));
	}

	public static void closeAllShells() {
		bot.closeAllShells();
	}

	public static void closeAllEditors() {
		Display.getDefault().syncExec(() -> PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(false));
	}

	public static void deleteAllProjects() {
		packageExplorer().deleteAllProjects();
	}

	public static void touchFile(String path) {
		try {
			ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(path)).touch(null);
		} catch (CoreException e) {
			log.error("Can't touch '" + path + "'.", e);
		}
	}

	public static void waitForBuild() {
		IResourcesSetupUtil.waitForBuild();
	}

	public static void cleanBuild() throws Exception {
		IResourcesSetupUtil.cleanBuild();
	}

	public static void sleep(long millis) {
		bot.sleep(millis);
	}

	public static long calculateFolderSize(String relativeFolder) {
		return getFolderSize(ResourcesPlugin.getWorkspace().getRoot().getFolder(new Path(relativeFolder)).getRawLocation().toFile());
	}

	public static void runJunitTests(String... path) {
		junitView().clearHistory();
		packageExplorer().runJUnitTests(path);
		junitView().waitForTestrunToFinish();
	}

	public static void runJunitPluginTests(String... path) {
		junitView().clearHistory();
		packageExplorer().runJUnitPluginTests(path);
		junitView().waitForTestrunToFinish();
	}

	public static void screenshot() {
		StackTraceElement[] stackTrace = new Exception().getStackTrace();
		StackTraceElement relevantTrace = stackTrace[1];
		SWTUtils.captureScreenshot("screenshots/INFO-" + relevantTrace.getClassName() + "-" + relevantTrace.getMethodName() + "-line-"
				+ relevantTrace.getLineNumber() + ".jpeg");
	}

	private static long getFolderSize(File folder) {
		long result = 0;
		File[] children = folder.listFiles();
		if (children == null) {
			return result;
		}
		for (int n = 0; n < children.length; n++) {
			if (children[n].isFile()) {
				result += children[n].length();
			} else {
				result += getFolderSize(children[n]);
			}
		}
		return result;
	}

	private static XtextSWTBotView activateViewById(String viewId) {
		XtextSWTBotView view = bot.viewById(viewId);
		view.setFocus();
		return view;
	}

}

/********************************************************************************
 * Copyright (c) 2017-2018 TypeFox and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 ********************************************************************************/
package org.eclipse.sprotty;

/**
 * A layout engine is able to compute layout information for a model. Invoked by {@link DefaultDiagramServer}.
 * The client decides in the {@link RequestModelAction} whether a layout is performed on the server.
 */
public interface ILayoutEngine {
	
	/**
	 * Compute a layout for the given model and modify the model accordingly.
	 */
	public void layout(SModelRoot root, Action cause);
	
	/**
	 * An implementation that does nothing.
	 */
	public static class NullImpl implements ILayoutEngine {
		@Override
		public void layout(SModelRoot root, Action cause) {
		}
	}
}

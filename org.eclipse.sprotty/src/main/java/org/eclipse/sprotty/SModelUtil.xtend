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
package org.eclipse.sprotty

import java.util.ArrayList
import java.util.function.Consumer
import org.eclipse.sprotty.util.IdCache

/**
 * Convenient functions for generating sprotty diagrams.
 * 
 * @deprecated Use the constructors with the lambda parameter for convenient and 
 * readable construction and the {@link IdCache} to guarantee unique id's.
 */
@Deprecated
final class SModelUtil {
	
	private new() {}
	
	private static def String getDefaultType(Class<? extends SModelElement> elementClazz) {
		if (SGraph.isAssignableFrom(elementClazz))
			return 'graph'
		else if (SNode.isAssignableFrom(elementClazz))
			return 'node'
		else if (SPort.isAssignableFrom(elementClazz))
			return 'port'
		else if (SEdge.isAssignableFrom(elementClazz))
			return 'edge'
		else if (SLabel.isAssignableFrom(elementClazz))
			return 'label'
		else if (SCompartment.isAssignableFrom(elementClazz))
			return 'comp'
		else if (SButton.isAssignableFrom(elementClazz))
			return 'button'
	}
	
	static def <T extends SModelElement> T create(Class<T> clazz, String id) {
		create(clazz, id, clazz.defaultType)
	}
	
	static def <T extends SModelElement> T create(Class<T> clazz, String id, String type) {
		val result = clazz.newInstance
		result.id = id
		result.type = type
		return result
	}
	
	static def <T extends SModelElement> T create(Class<T> clazz, String id, Consumer<T> initializer) {
		create(clazz, id, clazz.defaultType, initializer)
	}
	
	static def <T extends SModelElement> T create(Class<T> clazz, String id, String type, Consumer<T> initializer) {
		val result = clazz.newInstance
		result.id = id
		result.type = type
		initializer.accept(result)
		return result
	}
	
	static def <T extends SModelElement> T addChild(T parent, SModelElement newChild) {
		if (parent.children === null)
			parent.children = new ArrayList
		parent.children.add(newChild)
		return parent
	}
	
	static def <T extends SModelElement> T addChild(T parent, Class<? extends SModelElement> clazz) {
		val type = clazz.defaultType
		val index = if (parent.children === null) 0 else parent.children.size
		val id = parent.id + '/' + type + index
		addChild(parent, create(clazz, id, type))
	}
	
	static def <T extends SModelElement> T addChild(T parent, Class<? extends SModelElement> clazz, String id) {
		addChild(parent, create(clazz, id))
	}
	
	static def <T extends SModelElement> T addChild(T parent, Class<? extends SModelElement> clazz, String id, String type) {
		addChild(parent, create(clazz, id, type))
	}
	
	static def <T1 extends SModelElement, T2 extends SModelElement> T1 addChild(T1 parent, Class<T2> clazz, Consumer<T2> initializer) {
		val type = clazz.defaultType
		val index = if (parent.children === null) 0 else parent.children.size
		val id = parent.id + '/' + type + index
		addChild(parent, create(clazz, id, type, initializer))
	}
	
	static def <T1 extends SModelElement, T2 extends SModelElement> T1 addChild(T1 parent, Class<T2> clazz, String id,
			Consumer<T2> initializer) {
		addChild(parent, create(clazz, id, initializer))
	}
	
	static def <T1 extends SModelElement, T2 extends SModelElement> T1 addChild(T1 parent, Class<T2> clazz, String id, String type,
			Consumer<T2> initializer) {
		addChild(parent, create(clazz, id, type, initializer))
	}
	
}
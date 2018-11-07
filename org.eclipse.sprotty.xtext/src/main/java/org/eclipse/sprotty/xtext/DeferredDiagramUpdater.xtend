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

package org.eclipse.sprotty.xtext

import java.util.Collection
import java.util.Set
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.LinkedBlockingQueue
import org.eclipse.emf.common.util.URI

class DeferredDiagramUpdater {
	
	Timer currentTimer
	
	val uris = new LinkedBlockingQueue<URI>
	
	val lock = new Object

	val (Set<? extends URI>)=>void updateFunction 
	
	new((Set<? extends URI>)=>void updateFunction) {
		this.updateFunction = updateFunction
	}
	
	def updateLater(Collection<? extends URI> newUris) {
		uris.addAll(newUris)
		schedule(200)
	}
	
	protected def schedule(long delay) {
		synchronized(lock) {
			if(currentTimer !== null)
				currentTimer.cancel
			currentTimer = new Timer('Diagram updater', true)	
			currentTimer.schedule(createTimerTask, delay)
		}
	}
	
	protected def TimerTask createTimerTask() {
		[ this.update() ]
	}
	
	protected def update() {
		val processUris = <URI>newHashSet
		uris.drainTo(processUris)
		updateFunction.apply(processUris)
	}
}


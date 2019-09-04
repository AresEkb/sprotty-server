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

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * A diagram server can be connected to the action stream of a sprotty client.
 * This is the Java representation of the backend accessed via the TypeScript class {@code DiagramServer}
 * in the client. An instance of this interface is always bound to one sprotty client, which is
 * identified with its {@code clientId} string. In most cases a diagram server is stateful, since it
 * remembers the current model, and possibly further information required for building that model.
 */
public interface IDiagramServer extends Consumer<ActionMessage> {
	
	/**
	 * A string used to uniquely identify the sprotty view and its corresponding server instance.
	 */
	String getClientId();
	
	/**
	 * Set the client identifier. This should be done only once for initializing this server instance.
	 */
	void setClientId(String clientId);
	
	/**
	 * The endpoint to which messages are sent by this server.
	 */
	Consumer<ActionMessage> getRemoteEndpoint();
	
	/**
	 * Set the remote endpoint. This should be done for initializing this server instance, or when
	 * the communication channel to the client has changed.
	 */
	void setRemoteEndpoint(Consumer<ActionMessage> remoteEndpoint);
	
	/**
	 * Dispatch the given action to the client. This can only be done if a remote endpoint has been
	 * set with {@link #setRemoteEndpoint(Consumer)}.
	 */
	void dispatch(Action action);
	
    /**
     * Dispatch a request to the client. The returned future is resolved when a response with
     * matching identifier is received. That response is _not_ handled by the diagram server.
     * Instead, it is the responsibility of the caller of this method to handle the response
     * properly.
     */
	<Res extends ResponseAction> CompletableFuture<Res> request(RequestAction<Res> action);
	
	/**
	 * The current model, represented by its root element.
	 */
	SModelRoot getModel();
	
	/**
	 * Set the current model and send it to the client, if a remote endpoint has been configured.
	 * The root must not be {@code null}.
	 */
	CompletableFuture<Void> setModel(SModelRoot root);
	
	/**
	 * Set the current model and send an update to the client, if a remote endpoint has been configured.
	 * The main difference to {@link #setModel(SModelRoot)} is that with this method the change will be
	 * animated in the client.
	 * 
	 * Since 0.7 the new <code>root</code> should no longer be null. as in-place modifications of the 
	 * model should be avoid due to concurrency issues.
	 */
	CompletableFuture<Void> updateModel(SModelRoot root);
	
	/**
	 * Set the current status popup model and send an update to the client, if a remote endpoint has 
	 * been configured.
	 * 
	 * <p>The root parameter can be {@code null}, in which case the current popup model is cleared.</p> 
	 */
	void setStatus(ServerStatus status);
	
	/**
	 * The options received from the client with the last {@link RequestModelAction}. These options
	 * can be used to control diagram creation. If no such action has been received yet, or the action did
	 * not contain any options, an empty map is returned.
	 */
	Map<String, String> getOptions();

	/**
	 * Current state of the diagram. 
	 */
	IDiagramState getDiagramState();
	
	/**
	 * A diagram server provider creates a diagram server for a given {@code clientId} or returns
	 * an already existing one.
	 */
	public interface Provider {
		/**
		 * Returns a diagram server, or {@code null} if no server is available for the given {@code clientId}.
		 */
		IDiagramServer getDiagramServer(String clientId);
	}
	
}

/*******************************************************************************
 * Copyright (c) 2016 Sierra Wireless and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v20.html
 * and the Eclipse Distribution License is available at
 *    http://www.eclipse.org/org/documents/edl-v10.html.
 *
 * Contributors:
 *     Sierra Wireless - initial API and implementation
 *******************************************************************************/
package org.eclipse.leshan.bsserver;

import java.util.Map;

import org.eclipse.leshan.bsserver.security.BootstrapAuthorizer;
import org.eclipse.leshan.core.endpoint.EndpointUri;
import org.eclipse.leshan.core.model.LwM2mModel;
import org.eclipse.leshan.core.peer.LwM2mPeer;
import org.eclipse.leshan.core.request.BootstrapRequest;
import org.eclipse.leshan.core.request.ContentFormat;

/**
 * Represent a single Bootstrapping session.
 *
 * Should be created by {@link BootstrapSessionManager} implementations in
 * {@link BootstrapSessionManager#begin(String, BootstrapRequest, LwM2mPeer, EndpointUri)}.
 */
public interface BootstrapSession {

    /**
     * @return the identifier for this session
     */
    String getId();

    /**
     * @return the endpoint of the LwM2M client.
     */
    String getEndpoint();

    /**
     * @return the bootstrap request which initiate the session.
     */
    BootstrapRequest getBootstrapRequest();

    /**
     * @return the transport information about the LwM2M client.
     */
    LwM2mPeer getClientTransportData();

    /**
     * @return the URI of the endpoint used for this session
     */
    EndpointUri getEndpointUsed();

    /**
     * @return <code>true</code> if the LwM2M client is authorized to start a bootstrap session.
     */
    boolean isAuthorized();

    /**
     * @return the content format to use on write request during this bootstrap session.
     */
    ContentFormat getContentFormat();

    /**
     * @return some application data that could be attached by {@link BootstrapAuthorizer}.
     */
    Map<String, String> getApplicationData();

    /**
     * @return the create time in milliseconds
     * @see System#currentTimeMillis()
     */
    long getCreationTime();

    /**
     * Cancel the current session
     */
    void cancel();

    /**
     * @return True if this session was cancellled
     */
    boolean isCancelled();

    /**
     * @return objects model supported by the client for this session.
     */
    LwM2mModel getModel();
}

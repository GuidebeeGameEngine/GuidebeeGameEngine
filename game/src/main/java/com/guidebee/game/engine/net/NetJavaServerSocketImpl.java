/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
//--------------------------------- PACKAGE ------------------------------------
package com.guidebee.game.engine.net;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.game.GameEngineRuntimeException;
import com.guidebee.game.Net;
import com.guidebee.game.net.ServerSocket;
import com.guidebee.game.net.ServerSocketHints;
import com.guidebee.game.net.Socket;
import com.guidebee.game.net.SocketHints;

import java.net.InetSocketAddress;

//[------------------------------ MAIN CLASS ----------------------------------]
/**
 * Server socket implementation using java.net.ServerSocket.
 *
 * @author noblemaster
 */
public class NetJavaServerSocketImpl implements ServerSocket {

    private Net.Protocol protocol;

    /**
     * Our server or null for disposed, aka closed.
     */
    private java.net.ServerSocket server;

    public NetJavaServerSocketImpl(Net.Protocol protocol, int port,
                                   ServerSocketHints hints) {
        this.protocol = protocol;

        // create the server socket
        try {
            // initialize
            server = new java.net.ServerSocket();
            if (hints != null) {
                server.setPerformancePreferences(hints.performancePrefConnectionTime,
                        hints.performancePrefLatency,
                        hints.performancePrefBandwidth);
                server.setReuseAddress(hints.reuseAddress);
                server.setSoTimeout(hints.acceptTimeout);
                server.setReceiveBufferSize(hints.receiveBufferSize);
            }

            // and bind the server...
            InetSocketAddress address = new InetSocketAddress(port);
            if (hints != null) {
                server.bind(address, hints.backlog);
            } else {
                server.bind(address);
            }
        } catch (Exception e) {
            throw new GameEngineRuntimeException("Cannot create a server socket at port "
                    + port + ".", e);
        }
    }

    @Override
    public Net.Protocol getProtocol() {
        return protocol;
    }

    @Override
    public Socket accept(SocketHints hints) {
        try {
            return new NetJavaSocketImpl(server.accept(), hints);
        } catch (Exception e) {
            throw new GameEngineRuntimeException("Error accepting socket.", e);
        }
    }

    @Override
    public void dispose() {
        if (server != null) {
            try {
                server.close();
                server = null;
            } catch (Exception e) {
                throw new GameEngineRuntimeException("Error closing server.", e);
            }
        }
    }
}

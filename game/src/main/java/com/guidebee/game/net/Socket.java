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
package com.guidebee.game.net;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.utils.Disposable;

import java.io.InputStream;
import java.io.OutputStream;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * A client socket that talks to a server socket via some
 * {@link com.guidebee.game.Net.Protocol}. See
 * {@link com.guidebee.game.Net#newClientSocket(com.guidebee.game.Net.Protocol,
 * String, int, SocketHints)} and
 * {@link com.guidebee.game.Net#newServerSocket(com.guidebee.game.Net.Protocol,
 * int, ServerSocketHints)}.</p>
 * <p>
 * A socket has an {@link InputStream} used to send data to the other end of the connection,
 * and an {@link OutputStream} to
 * receive data from the other end of the connection.</p>
 * <p/>
 * A socket needs to be disposed if it is no longer used. Disposing also closes the connection.
 *
 * @author mzechner
 */
public interface Socket extends Disposable {
    /**
     * @return whether the socket is connected
     */
    public boolean isConnected();

    /**
     * @return the {@link InputStream} used to read data from the other end of the connection.
     */
    public InputStream getInputStream();

    /**
     * @return the {@link OutputStream} used to write data to the other end of the connection.
     */
    public OutputStream getOutputStream();

    /**
     * @return the RemoteAddress of the Socket as String
     */
    public String getRemoteAddress();
}

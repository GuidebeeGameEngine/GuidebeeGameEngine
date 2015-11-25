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
package com.guidebee.game.engine.platform;

//--------------------------------- IMPORTS ------------------------------------

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.guidebee.game.activity.BaseActivity;
import com.guidebee.game.engine.net.NetJavaImpl;
import com.guidebee.game.engine.net.NetJavaServerSocketImpl;
import com.guidebee.game.engine.net.NetJavaSocketImpl;
import com.guidebee.game.net.ServerSocket;
import com.guidebee.game.net.ServerSocketHints;
import com.guidebee.game.net.Socket;
import com.guidebee.game.net.SocketHints;

//[------------------------------ MAIN CLASS ----------------------------------]
/**
 * Android implementation of the {@link com.guidebee.game.Net} API.
 *
 * @author acoppes
 */
public class Net implements com.guidebee.game.Net {

    // IMPORTANT: The GameEngine.net classes are a currently duplicated for
    // JGLFW/LWJGL + Android!
    // If you make changes here, make changes in the other backend as well.
    final BaseActivity app;
    NetJavaImpl netJavaImpl;

    public Net(BaseActivity app) {
        this.app = app;
        netJavaImpl = new NetJavaImpl();
    }

    @Override
    public void sendHttpRequest(HttpRequest httpRequest,
                                final HttpResponseListener httpResponseListener) {
        netJavaImpl.sendHttpRequest(httpRequest, httpResponseListener);
    }

    @Override
    public void cancelHttpRequest(HttpRequest httpRequest) {

        netJavaImpl.cancelHttpRequest(httpRequest);
    }

    @Override
    public ServerSocket newServerSocket(Protocol protocol, int port, ServerSocketHints hints) {
        return new NetJavaServerSocketImpl(protocol, port, hints);
    }

    @Override
    public Socket newClientSocket(Protocol protocol, String host, int port, SocketHints hints) {
        return new NetJavaSocketImpl(protocol, host, port, hints);
    }

    @Override
    public void openURI(String URI) {
        final Uri uri = Uri.parse(URI);
        app.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                // LiveWallpaper and Daydream applications need this flag
                if (!(app.getContext() instanceof Activity))
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                app.startActivity(intent);
            }
        });
    }

}

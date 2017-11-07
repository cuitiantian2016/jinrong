/*
 * (C) Copyright 2015-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     ohun@live.cn (夜色)
 */

package com.xulu.mpush.handler;


import com.xulu.mpush.api.Client;
import com.xulu.mpush.api.Logger;
import com.xulu.mpush.api.connection.Connection;
import com.xulu.mpush.api.protocol.Packet;
import com.xulu.mpush.client.ClientConfig;
import com.xulu.mpush.client.MPushClient;
import com.xulu.mpush.message.RequestMarketMessage;

/**
 * 接收到行情数据的处理类
 *
 * @author ohun@live.cn (夜色)
 */
public final class HandMarketMessageHandler extends BaseMessageHandler<RequestMarketMessage> {
    private static final String TAG = "HandMarketMessageHandler";

    private final Logger logger = ClientConfig.I.getLogger();
    Connection connection;
    @Override
    public RequestMarketMessage decode(Packet packet, Connection connection) {
        return new RequestMarketMessage(packet, connection);
    }

    @Override
    public void handle(RequestMarketMessage message) {
        logger.d(TAG, "handle: message-->"+message);
        Client client = message.getConnection().getClient();
        MPushClient mPushClient = (MPushClient) client;
        mPushClient.config.getClientListener().onReceiverMarket(message);
    }

}

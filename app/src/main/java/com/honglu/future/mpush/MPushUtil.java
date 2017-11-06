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


package com.honglu.future.mpush;

/**
 * push发送的控制类
 * <p/>
 * Created by hefei on 2016/2/13.
 *
 * @author ohun@live.cn
 */
public final class MPushUtil {

    /**
     * 发送请求行情的数据
     *
     * @param code
     * @return
     */
    public static void requestMarket(String code) {
       MPush.I().requestMarket(code);
    }

    /**
     * 停止行情请求
     */
    public static void pauseRequset(){
        MPush.I().requestMarket("");
    }
}

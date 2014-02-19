/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.deltaspike.data.impl.util;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ProxyUtils
{
    private ProxyUtils()
    {
    }

    public static List<Class<?>> extractFromProxy(Class<?> proxyClass)
    {
        List<Class<?>> result = new LinkedList<Class<?>>();
        result.add(proxyClass);
        if (isInterfaceProxy(proxyClass))
        {
            result.addAll(Arrays.asList(proxyClass.getInterfaces()));
        }
        else
        {
            result.add(proxyClass.getSuperclass());
        }
        return result;
    }

    public static boolean isInterfaceProxy(Class<?> proxyClass)
    {
        Class<?>[] interfaces = proxyClass.getInterfaces();
        return Proxy.class.equals(proxyClass.getSuperclass()) &&
                interfaces != null && interfaces.length > 0;
    }
}

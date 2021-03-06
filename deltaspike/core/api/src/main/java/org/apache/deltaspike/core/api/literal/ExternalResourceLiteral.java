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
package org.apache.deltaspike.core.api.literal;

import org.apache.deltaspike.core.api.resourceloader.ExternalResource;
import org.apache.deltaspike.core.api.resourceloader.ExternalResourceStorage;

import javax.enterprise.util.AnnotationLiteral;

/**
 *
 */
public class ExternalResourceLiteral extends AnnotationLiteral<ExternalResource> implements ExternalResource
{
    private static final long serialVersionUID = 1705986508118055892L;

    private Class<? extends ExternalResourceStorage> storage;
    private String location;
    public ExternalResourceLiteral(final Class<? extends ExternalResourceStorage> storage, final String location)
    {
        this.storage = storage;
        this.location = location;
    }
    @Override
    public String location()
    {
        return this.location;
    }

    @Override
    public Class<? extends ExternalResourceStorage> storage()
    {
        return this.storage;
    }
}

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
package org.apache.deltaspike.data.impl.tx;

import static org.apache.deltaspike.data.impl.util.ClassUtils.contains;
import static org.apache.deltaspike.data.impl.util.ClassUtils.extract;

import java.lang.reflect.Method;

import javax.inject.Inject;

import org.apache.deltaspike.data.impl.builder.QueryBuilder;
import org.apache.deltaspike.data.impl.handler.CdiQueryInvocationContext;
import org.apache.deltaspike.data.impl.handler.EntityRepositoryHandler;
import org.apache.deltaspike.data.impl.handler.QueryRunner;
import org.apache.deltaspike.data.impl.meta.RequiresTransaction;
import org.apache.deltaspike.jpa.impl.entitymanager.EntityManagerHolder;
import org.apache.deltaspike.jpa.spi.transaction.TransactionStrategy;

public class TransactionalQueryRunner implements QueryRunner
{

    @Inject
    private TransactionStrategy strategy;

    @Inject
    private EntityManagerHolder entityManagerHolder;

    @Override
    public Object executeQuery(final QueryBuilder builder, final CdiQueryInvocationContext context)
        throws Throwable
    {
        if (needsTransaction(context))
        {
            try
            {
                entityManagerHolder.set(context.getEntityManager());
                return executeTransactional(builder, context);
            }
            finally
            {
                entityManagerHolder.dispose();
            }
        }
        return executeNonTransactional(builder, context);
    }

    protected Object executeNonTransactional(final QueryBuilder builder, final CdiQueryInvocationContext context)
    {
        return builder.executeQuery(context);
    }

    protected Object executeTransactional(final QueryBuilder builder, final CdiQueryInvocationContext context)
        throws Exception
    {
        return strategy.execute(new InvocationContextWrapper(context)
        {
            @Override
            public Object proceed() throws Exception
            {
                return builder.executeQuery(context);
            }
        });
    }

    private boolean needsTransaction(CdiQueryInvocationContext context)
    {
        boolean requiresTx = false;
        Method method = context.getMethod();
        if (contains(EntityRepositoryHandler.class, method))
        {
            Method executed = extract(EntityRepositoryHandler.class, method);
            requiresTx = executed.isAnnotationPresent(RequiresTransaction.class);
        }
        return requiresTx || context.getRepositoryMethod().requiresTransaction();
    }

}

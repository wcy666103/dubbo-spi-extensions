/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.registry.kubernetes;

import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;

public class MeshConstant {

    /**
     * 自定义资源上下文？
     * 该方法只是方便 vsappWatch那边创建watch
     * @return
     */
    public static CustomResourceDefinitionContext getVsDefinition() {
        // TODO cache
        return new CustomResourceDefinitionContext.Builder()
                .withGroup("service.dubbo.apache.org")
                .withVersion("v1alpha1")
                .withScope("Namespaced")
                .withName("virtualservices.service.dubbo.apache.org")
                .withPlural("virtualservices")
                .withKind("VirtualService")
                .build();
    }

    public static CustomResourceDefinitionContext getDrDefinition() {
        // TODO cache
        return new CustomResourceDefinitionContext.Builder()
                .withGroup("service.dubbo.apache.org")
                .withVersion("v1alpha1")
                .withScope("Namespaced")
                .withName("destinationrules.service.dubbo.apache.org")
                .withPlural("destinationrules")
                .withKind("DestinationRule")
                .build();
    }
}

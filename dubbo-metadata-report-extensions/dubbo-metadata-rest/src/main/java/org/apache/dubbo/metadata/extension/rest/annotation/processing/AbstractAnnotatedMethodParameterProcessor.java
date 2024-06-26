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
package org.apache.dubbo.metadata.extension.rest.annotation.processing;


import org.apache.dubbo.metadata.extension.rest.api.RestMethodMetadata;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

import static org.apache.dubbo.metadata.annotation.processing.util.AnnotationUtils.getValue;
import static org.apache.dubbo.metadata.extension.rest.annotation.processing.AnnotatedMethodParameterProcessor.buildDefaultValue;

/**
 * The abstract {@link AnnotatedMethodParameterProcessor} implementation
 *
 * @since 2.7.6
 */
public abstract class AbstractAnnotatedMethodParameterProcessor implements AnnotatedMethodParameterProcessor {

    @Override
    public final void process(
            AnnotationMirror annotation,
            VariableElement parameter,
            int parameterIndex,
            ExecutableElement method,
            RestMethodMetadata restMethodMetadata) {
        String annotationValue = getAnnotationValue(annotation, parameter, parameterIndex);
        String defaultValue = getDefaultValue(annotation, parameter, parameterIndex);
        process(annotationValue, defaultValue, annotation, parameter, parameterIndex, method, restMethodMetadata);
    }

    protected abstract void process(
            String annotationValue,
            String defaultValue,
            AnnotationMirror annotation,
            VariableElement parameter,
            int parameterIndex,
            ExecutableElement method,
            RestMethodMetadata restMethodMetadata);

    protected String getAnnotationValue(AnnotationMirror annotation, VariableElement parameter, int parameterIndex) {
        return getValue(annotation);
    }

    protected String getDefaultValue(AnnotationMirror annotation, VariableElement parameter, int parameterIndex) {
        return buildDefaultValue(parameterIndex);
    }
}

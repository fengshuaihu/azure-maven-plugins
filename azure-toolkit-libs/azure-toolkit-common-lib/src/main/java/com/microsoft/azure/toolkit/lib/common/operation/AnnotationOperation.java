/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azure.toolkit.lib.common.operation;

import com.microsoft.azure.toolkit.lib.common.Executable;
import com.microsoft.azure.toolkit.lib.common.bundle.AzureString;
import com.microsoft.azure.toolkit.lib.common.utils.aspect.ExpressionUtils;
import com.microsoft.azure.toolkit.lib.common.utils.aspect.MethodInvocation;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.annotation.Nonnull;
import java.util.Arrays;

@SuperBuilder
public class AnnotationOperation extends MethodInvocation implements IAzureOperation<Object> {

    @Getter
    @Setter
    private IAzureOperation<?> parent;

    @Override
    public String toString() {
        final AzureOperation annotation = this.getAnnotation(AzureOperation.class);
        return String.format("{name:'%s', method:%s}", annotation.name(), method.getName());
    }

    @Nonnull
    public String getName() {
        final AzureOperation annotation = this.getAnnotation(AzureOperation.class);
        return annotation.name();
    }

    @Override
    public Executable<Object> getBody() {
        return () -> this.getMethod().invoke(this.getInstance(), this.getParamValues());
    }

    @Nonnull
    public String getType() {
        final AzureOperation annotation = this.getAnnotation(AzureOperation.class);
        return annotation.type().name();
    }

    public AzureString getTitle() {
        final AzureOperation annotation = this.getAnnotation(AzureOperation.class);
        final String name = annotation.name();
        final String[] params = Arrays.stream(annotation.params()).map(e -> ExpressionUtils.interpret(e, this)).toArray(String[]::new);
        return AzureOperationBundle.title(name, (Object[]) params);
    }
}

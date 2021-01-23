/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.artifacts.transform;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that even though dependencies are potentially injected into a
 * transformer their hash should not be considered when creating an identity used
 * to identify the current state of the transformer.
 *
 * Applying this annotation makes the identity of the transformer only rely on the
 * input artifacts, as well as the parameters given, but not anymore on the dependencies
 * past potentially into the transformer, even if said dependencies would change!
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface IgnoreDependenciesForIdentity {
}

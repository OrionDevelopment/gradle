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

import org.gradle.api.file.FileCollection;

/**
 * Filter used to modify the list of files that are handed to a transformer
 * when it is about to be executed.
 *
 * This filter is invoked before fingerprinting of the dependencies of the artifact
 * that is being transformed.
 */
@FunctionalInterface
public interface TransformerDependenciesModifier {

    /**
     * Method invoked when the transformation pipeline has determined the upstream
     * dependencies of an artifact that is about to be transformed.
     *
     * Allows for the modification of the artifacts that are considered the input artifacts
     * dependencies and as such are considered during fingerprinting (and cache id determination)
     * as well as during injection.
     *
     * @param dependencies The upstream dependencies.
     * @return An iterable that lists all dependencies that should be passed to the transformer.
     */
    FileCollection filter(FileCollection dependencies);

    /**
     * The no-operations filter.
     * This returns the collection unmodified.
     */
    TransformerDependenciesModifier NO_OP = dependencies -> dependencies;
}

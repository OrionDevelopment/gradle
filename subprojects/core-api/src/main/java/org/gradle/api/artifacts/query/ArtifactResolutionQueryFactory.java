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
package org.gradle.api.artifacts.query;

/**
 * Defines a factory to be used by external and internal tooling to create
 * new instances of {@link ArtifactResolutionQuery}.
 */
@FunctionalInterface
public interface ArtifactResolutionQueryFactory {

    /**
     * Invoked when a new query is requested.
     * This method should always return a new instance.
     *
     * @return A new {@link ArtifactResolutionQuery} to be used.
     */
    ArtifactResolutionQuery createArtifactResolutionQuery();
}

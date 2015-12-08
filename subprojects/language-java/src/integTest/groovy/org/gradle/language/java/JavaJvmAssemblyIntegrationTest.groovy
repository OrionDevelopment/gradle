/*
 * Copyright 2015 the original author or authors.
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

package org.gradle.language.java

import org.gradle.integtests.fixtures.AbstractIntegrationSpec

import java.util.zip.ZipFile

import static org.gradle.language.java.JavaIntegrationTesting.applyJavaPlugin

class JavaJvmAssemblyIntegrationTest extends AbstractIntegrationSpec {

    def setup() {
        applyJavaPlugin buildFile
        buildFile << '''
            model {
                components {
                    main(JvmLibrarySpec)
                }
                tasks.processMainJarMainResources {
                    // remove lines that start with #
                    filter {
                        it.startsWith('#') ? null : it
                    }
                }
            }
        '''
        file('src/main/java/myorg/Main.java')       << 'package myorg; class Main {}'
        file('src/main/resources/myorg/answer.txt') << '# yadda\n42'
    }

    def "can create task that depends on assembly and jar is *not* built"() {
        given:
        buildFile << '''
            model {
                tasks { tasks ->
                    $.components.main.binaries { binaries ->
                        def binary = binaries.values().first()
                        tasks.create('taskThatDependsOnAssembly') {
                            dependsOn binary.assembly
                            doFirst {
                                def hasOrNot = binary.jarFile.exists() ? 'has' : 'has not'
                                println "Jar $hasOrNot been built."

                                def relativize = { root, file ->
                                    root.toURI().relativize(file.toURI()).toString()
                                }

                                def classes = []
                                binary.assembly.classDirectories.each { dir ->
                                    dir.eachFileRecurse(groovy.io.FileType.FILES) {
                                        classes << relativize(dir, it)
                                    }
                                }
                                println "Classes were generated: $classes"

                                def resources = []
                                binary.assembly.resourceDirectories.each { dir ->
                                    dir.eachFileRecurse(groovy.io.FileType.FILES) {
                                        resources << "${relativize(dir, it)} => $it.text"
                                    }
                                }
                                println "Resources were processed: $resources"
                            }
                        }
                    }
                }
            }
        '''

        expect:
        succeeds 'taskThatDependsOnAssembly'

        and:
        outputContains 'Jar has not been built.'
        outputContains 'Classes were generated: [myorg/Main.class]'
        outputContains 'Resources were processed: [myorg/answer.txt => 42]'
    }

    def "can specify additional resource directory with preprocessed resources and it will end up in the jar"() {
        given:
        buildFile << '''
            model {
                components.main.binaries.jar {
                    assembly.resourceDirectories << file('preprocessed/resources')
                }
            }
        '''
        file('preprocessed/resources/myorg/question.txt') << '# the ultimate question'

        expect:
        succeeds 'assemble'

        and:
        def jar = new ZipFile(file('build/jars/main/jar/main.jar'))
        def textForEntry = { String path -> jar.getInputStream(jar.getEntry(path)).text }
        textForEntry('myorg/question.txt') == '# the ultimate question'
        textForEntry('myorg/answer.txt')   == '42'
    }
}

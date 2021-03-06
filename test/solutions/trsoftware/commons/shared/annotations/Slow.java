/*
 * Copyright 2018 TR Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package solutions.trsoftware.commons.shared.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Test methods that take more than 2 seconds to execute should be marked with this annotation, so that they can be
 * excluded from a suite of fast unit tests.
 *
 * @author Alex
 */
@Retention(RetentionPolicy.RUNTIME)  // test suite builders must be able to get this annotation at runtime
@Target({ElementType.METHOD, ElementType.TYPE})  // methods and classes may be annotated with this
public @interface Slow {
}

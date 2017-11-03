/*
 *  Copyright 2017 TR Software Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy of
 *  the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package solutions.trsoftware.commons.server.io.csv;

import solutions.trsoftware.commons.client.util.MapUtils;
import solutions.trsoftware.commons.client.util.callables.Function1;

/**
 * Mar 15, 2010
 *
 * @author Alex
 */
public class CSVObjectBinderTest extends CSVObjectBinderBaseTest {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    myClassBinder = new CSVObjectBinder<>(MyClass.class, new String[]{"foo", "bar"},
        MapUtils.hashMap(
            "foo",
            (Function1<String, Object>)Integer::new,
            "bar",
            (Function1<String, Object>)Float::parseFloat),
        MapUtils.hashMap(
            "foo",
            (Function1<Object, String>)String::valueOf,
            "bar",
            (Function1<Object, String>)String::valueOf
        ));
  }


}
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

package solutions.trsoftware.commons.server.util;

import com.google.common.collect.ImmutableSet;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerCollectionUtilsTest extends TestCase {

  public void testPowerset() throws Exception {
    // example from Wikipedia: http://en.wikipedia.org/wiki/Powerset
    int x = 5;
    int y = 6;
    int z = 7;
    Set<Set<Integer>> powerset = ServerCollectionUtils.powerset(ImmutableSet.of(x, y, z));
    assertEquals(8, powerset.size());
    assertTrue(powerset.contains(ImmutableSet.<Integer>of()));
    assertTrue(powerset.contains(ImmutableSet.of(x)));
    assertTrue(powerset.contains(ImmutableSet.of(y)));
    assertTrue(powerset.contains(ImmutableSet.of(z)));
    assertTrue(powerset.contains(ImmutableSet.of(x, y)));
    assertTrue(powerset.contains(ImmutableSet.of(x, z)));
    assertTrue(powerset.contains(ImmutableSet.of(y, z)));
    assertTrue(powerset.contains(ImmutableSet.of(x, y, z)));
  }

  public void testRandomElement() throws Exception {
    // check that all elements of the collection are returned with approximately equal probability
    Set<String> collection = ImmutableSet.of("a", "b", "c", "d", "e");
    int n = 1000000;

    Map<String, AtomicInteger> counts = new HashMap<String, AtomicInteger>();
    for (String elt : collection)
      counts.put(elt, new AtomicInteger());
    for (int i = 0; i < n; i++)
      counts.get(ServerCollectionUtils.randomElement(collection)).incrementAndGet();

    // print the counts and assert that each one is approx equal to 20% of n, with a 1% margin of error
    System.out.println("Element occurrences");
    for (String elt : collection) {
      int count = counts.get(elt).get();
      System.out.printf("%s: %d%n", elt, count);
      assertEquals(.2, (double)count/n, .01);
    }
  }
}
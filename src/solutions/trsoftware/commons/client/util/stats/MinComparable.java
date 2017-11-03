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

package solutions.trsoftware.commons.client.util.stats;

import java.io.Serializable;

/**
 * Keeps track of the minimum in a sequence of Comparable objects.
 *
 * @author Alex
 */
public class MinComparable<T extends Comparable<T>> extends MinMaxComparableBase<T> implements Serializable {

  public MinComparable() {
    super(-1);
  }

  public MinComparable(Iterable<T> candidates) {
    super(-1, candidates);
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MinComparable min = (MinComparable)o;

    if (get() != null ? !get().equals(min.get()) : min.get() != null) return false;

    return true;
  }

  public int hashCode() {
    return (get() != null ? get().hashCode() : 0);
  }

  /**
   * @return The min of the given comparable objects.
   */
  public static <T extends Comparable<T>> T eval(T... candidates) {
    return new MinComparable<T>().updateAll(candidates);
  }
}
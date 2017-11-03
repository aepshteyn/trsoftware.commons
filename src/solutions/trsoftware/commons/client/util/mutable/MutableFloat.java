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

package solutions.trsoftware.commons.client.util.mutable;

/**
 * A GWT-compatible imitation of {@link org.apache.commons.lang3.mutable.MutableFloat}.
 *
 * @author Alex
 */
public class MutableFloat extends MutableNumber {

  private volatile float n;

  public MutableFloat() {
    n = 0;
  }

  public MutableFloat(float n) {
    this.n = n;
  }

  public float get() {
    return n;
  }

  public float incrementAndGet() {
    return ++n;
  }

  public float getAndIncrement() {
    return n++;
  }

  public float decrementAndGet() {
    return --n;
  }

  public float getAndDecrement() {
    return n--;
  }

  public float addAndGet(float delta) {
    return n += delta;
  }

  public float getAndAdd(float delta) {
    float old = n;
    n += delta;
    return old;
  }

  public float setAndGet(float newValue) {
    n = newValue;
    return n;
  }

  public float getAndSet(float newValue) {
    float old = n;
    n = newValue;
    return old;
  }

  public int intValue() {
    return (int)get();
  }

  public long longValue() {
    return (long)get();
  }

  public float floatValue() {
    return get();
  }

  public double doubleValue() {
    return get();
  }

  public Number toPrimitive() {
    return n;
  }

  @Override
  public void merge(MutableNumber other) {
    n += other.floatValue();
  }
}
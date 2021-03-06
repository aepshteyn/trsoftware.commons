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

package solutions.trsoftware.commons.server;

import solutions.trsoftware.commons.server.util.CanStopClock;
import solutions.trsoftware.commons.server.util.Clock;

/**
 * All unit tests that use the Clock must extend this class so that
 * the clock is cleaned up after the test.
 * 
 * Jun 30, 2009
 *
 * @author Alex
 */
public abstract class TestCaseCanStopClock extends SuperTestCase implements CanStopClock {

  @Override
  protected void tearDown() throws Exception {
    Clock.resetToNormal(); // make sure to leave the clock in an un-tampered state
    super.tearDown();
  }
}

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

package solutions.trsoftware.commons.client.util.time;

import com.google.gwt.core.client.Duration;
import solutions.trsoftware.commons.shared.util.time.Time;

/**
 * @author Alex, 3/24/2015
 */
public class ClientTime extends Time {

  public static final ClientTime INSTANCE = new ClientTime();

  @Override
  public double currentTimeMillis() {
    return Duration.currentTimeMillis();
  }
}

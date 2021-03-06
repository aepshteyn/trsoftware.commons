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

package solutions.trsoftware.commons.server.servlet.testutil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simply counts the number of times {@link #doFilter(ServletRequest, ServletResponse)} was invoked.
 *
 * @author Alex, 10/31/2017
 */
public class DummyFilterChain implements FilterChain {
  private AtomicInteger invocationCount = new AtomicInteger();

  @Override
  public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
    invocationCount.incrementAndGet();
  }

  public int getInvocationCount() {
    return invocationCount.get();
  }

  public boolean wasInvoked() {
      return invocationCount.get() > 0;
    }
}

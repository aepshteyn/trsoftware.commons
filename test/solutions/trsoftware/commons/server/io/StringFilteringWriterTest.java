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

package solutions.trsoftware.commons.server.io;

import junit.framework.TestCase;
import solutions.trsoftware.commons.server.util.Duration;
import solutions.trsoftware.commons.shared.util.StringUtils;
import solutions.trsoftware.commons.shared.util.TimeUnit;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Nov 4, 2009
 *
 * @author Alex
 */
public class StringFilteringWriterTest extends TestCase {

  public void testRemovalOfUnwantedStrings() throws Exception {
    assertEquals("abcd", filter("a b c d", " "));
    assertEquals("abcd", filter("a b c d ", " "));
    assertEquals("abcd", filter(" a b c d ", " "));
    assertEquals("abcd\nefg", filter(" a b c d \n e f g", " "));
    assertEquals("ad\nefg", filter("afood\nefg", "foo"));
    assertEquals("ad\nefg", filter("afood\nfooefg", "foo"));
    assertEquals("ad\nefg", filter("afood\nfooefgfoo", "foo"));

    // test chaining the filter to filter out mutliple strings
    assertEquals("abcdefg", chainedFilter(" a b c d \n e f g", " ", "\n"));
    assertEquals("abcefg", chainedFilter(" a b cfoo d \n e f g", " ", "\n", "d", "foo"));
  }

  public void testFilteringSpeed() throws Exception {
    final int iterations = 100;
    final int charsPerIteration = 10000;
    int totalChars = iterations * charsPerIteration;
    Duration duration = new Duration(String.format("Writing %,d chars with StringFilteringWriter", totalChars));
    StringWriter stringWriter = new StringWriter(totalChars);
    StringFilteringWriter filter = new StringFilteringWriter(stringWriter, "foo");
    // text written to this PrintWriter will be filtered and can be examined in the delegate StringWriter
    final PrintWriter pw = new PrintWriter(filter);
    for (int i = 0; i < iterations; i++) {
      pw.print(StringUtils.randString(charsPerIteration));
    }
    assertFalse(duration.toString(), duration.exceeds(1, TimeUnit.SECONDS));
    System.out.println(duration);
  }


  /** Passes the given string through the writer and returns the result */
  private String filter(String str, String unwantedString) throws Exception {
    StringWriter stringWriter = new StringWriter();
    StringFilteringWriter filter = new StringFilteringWriter(stringWriter, unwantedString);
    // text written to this PrintWriter will be filtered and can be examined in the delegate StringWriter
    PrintWriter pw = new PrintWriter(filter);
    pw.print(str);
    pw.flush();
    return stringWriter.toString();
  }

  /** Passes the given string through a sequences of writers, one for each unwanted string */
  private String chainedFilter(String str, String... unwantedStrings) throws Exception {
    StringWriter stringWriter = new StringWriter();
    Writer filter = stringWriter; // init chain with the underlying writer
    for (String unwantedString : unwantedStrings) {
      filter = new StringFilteringWriter(filter, unwantedString);  // add to chain by wrapping the filter with another
    }
    // text written to this PrintWriter will be filtered and can be examined in the delegate StringWriter
    PrintWriter pw = new PrintWriter(filter);
    pw.print(str);
    pw.flush();
    return stringWriter.toString();
  }

}
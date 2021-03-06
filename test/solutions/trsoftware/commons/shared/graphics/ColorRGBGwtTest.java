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

package solutions.trsoftware.commons.shared.graphics;

import solutions.trsoftware.commons.client.CommonsGwtTestCase;

/**
 * A simplified version of {@link ColorRGBTest} to be run in a GWT environment
 *
 * @author Alex
 */
public class ColorRGBGwtTest extends CommonsGwtTestCase {

  public void testColor() throws Exception {
    ColorRGB c = new ColorRGB(115, 255, 45);
    System.out.println(c.toString());
    assertEquals("#73ff2d", c.toString());
    ColorRGB parsedColor = ColorRGB.valueOf("#73ff2d");
    assertEquals(115, parsedColor.getRed());
    assertEquals(255, parsedColor.getGreen());
    assertEquals(45, parsedColor.getBlue());

    // equals & hash code methods should be properly implemented
    assertEquals(c.hashCode(), parsedColor.hashCode());
    assertEquals(c, parsedColor);
    assertNotSame(c, parsedColor);

    ColorRGB parsedColor2 = ColorRGB.valueOf("73ff2d");  // without the # prefix
    assertEquals(115, parsedColor2.getRed());
    assertEquals(255, parsedColor2.getGreen());
    assertEquals(45, parsedColor2.getBlue());

    assertEquals(parsedColor2.hashCode(), parsedColor.hashCode());
    assertEquals(parsedColor2, parsedColor);
    assertNotSame(parsedColor2, parsedColor);

    // make sure that all components will be automatically coerced to the range 0..255
    assertEquals("#0000ff", new ColorRGB(-25, 0, 256).toString());
  }
}
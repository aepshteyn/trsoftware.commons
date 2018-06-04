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

package solutions.trsoftware.commons.shared.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import junit.framework.TestCase;
import solutions.trsoftware.commons.shared.annotations.Slow;

import java.util.LinkedHashMap;
import java.util.Map;

import static solutions.trsoftware.commons.shared.testutil.AssertUtils.assertEqualsAndHashCode;
import static solutions.trsoftware.commons.shared.testutil.AssertUtils.assertNotEqualsAndHashCode;
import static solutions.trsoftware.commons.shared.util.ColorRGB.MAX_COMPONENT_VALUE;
import static solutions.trsoftware.commons.shared.util.ColorRGB.valueOf;

/**
 * @author Alex
 * @since 12/24/2017
 */
public class ColorRGBTest extends TestCase {

  // test data obtained from http://hslpicker.com/
  static final BiMap<ColorRGB, ColorHSL> EQUIVALENT_COLORS = ImmutableBiMap.of(
      new ColorRGB(255, 228, 196), new ColorHSL(32.54237288135594, 1, 0.884313725490196)
  );

  private static final Map<String, ColorRGB> STRING_VALUES = new MapDecorator<String, ColorRGB>(new LinkedHashMap<String, ColorRGB>())
      .put("#12afb1", new ColorRGB(0x12, 0xaf, 0xb1))
      .getMap();

  public void testToHSL() throws Exception {
    for (ColorRGB rgb : EQUIVALENT_COLORS.keySet()) {
      assertEquals(EQUIVALENT_COLORS.get(rgb), rgb.toHSL());
    }
  }

  public void testToString() throws Exception {
    for (Map.Entry<String, ColorRGB> entry : STRING_VALUES.entrySet()) {
      assertEquals(entry.getKey(), entry.getValue().toString());
    }
  }

  public void testValueOf() throws Exception {
    for (Map.Entry<String, ColorRGB> entry : STRING_VALUES.entrySet()) {
      assertEquals(entry.getValue(), valueOf(entry.getKey()));
    }
  }

  @Slow
  public void testEqualsAndHashCode() throws Exception {
    for (int r = 0; r <= MAX_COMPONENT_VALUE; r++) {
      for (int g = 0; g <= MAX_COMPONENT_VALUE; g++) {
        for (int b = 0; b <= MAX_COMPONENT_VALUE; b++) {
          ColorRGB c1 = new ColorRGB(r, g, b);
          ColorRGB c2 = new ColorRGB(r, g, b);
          assertEqualsAndHashCode(c1, c2);
          if (r > 0)
            assertNotEqualsAndHashCode(c1, new ColorRGB(r - 1, g, b));
          else if (g > 1)
            assertNotEqualsAndHashCode(c1, new ColorRGB(r, g - 1, b));
        }
      }
    }
  }

  public void testConstructor() throws Exception {
    // verify that constructor restricts args to the range [0,255]
    ColorRGB color = new ColorRGB(-1, 123, MAX_COMPONENT_VALUE+10);
    assertEquals(new ColorRGB(0, 123, MAX_COMPONENT_VALUE), color);
  }
}
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

package solutions.trsoftware.commons.client.widgets.input;

import com.google.gwt.junit.DoNotRunWith;
import com.google.gwt.junit.Platform;
import solutions.trsoftware.commons.client.CommonsGwtTestCase;

/**
 * Jan 18, 2010
 *
 * @author Alex
 */
@DoNotRunWith(Platform.Devel)  // this test fails in dev mode but works in prod mode
public class DirtyComboBoxTest extends CommonsGwtTestCase {

  public void testDirtyComboBox() throws Exception {
    DirtyComboBox box = new DirtyComboBox(new String[][]{
        {"foo", "x"},
        {"bar", "y"},
        {"baz", "z"}}, "y");

    assertEquals("y", box.getText());  // should start with "y" selected
    assertEquals(1, box.getSelectedIndex());
    assertFalse(box.isDirty());

    // should be able to use setText to select an item by value
    box.setText("z");
    assertEquals("z", box.getText());
    assertEquals(2, box.getSelectedIndex());
    assertTrue(box.isDirty());

    box.setText("y");
    assertEquals("y", box.getText());  // back to "y" selected
    assertEquals(1, box.getSelectedIndex());
    assertFalse(box.isDirty());
  }

}
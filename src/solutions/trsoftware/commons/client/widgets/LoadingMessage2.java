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

package solutions.trsoftware.commons.client.widgets;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LabelBase;

/**
 * A lighter, experimental version of {@link LoadingMessage}, that does not use tables.  Instead the spinner image is
 * configured via the CSS background property.
 *
 * TODO: try to replace all the usages LoadingMessage with this widget (however, that will be challenging, since there will certainly be layout problems in some cases)
 *
 * @author Alex
 */
public class LoadingMessage2 extends Composite {

  public enum SpinnerPosition { LEFT, RIGHT }

  public LoadingMessage2(LabelBase label, boolean startVisible, SpinnerPosition spinnerPosition) {
    initWidget(label);
    setStyleName("loadingMessage2");
    Style style = getStyleElement().getStyle();
    if (spinnerPosition == SpinnerPosition.RIGHT) {
      style.setPaddingRight(20, Style.Unit.PX);  /* 16px for spinner image width, and another 4px for spacing */
      style.setProperty("backgroundPosition", "right");
    }
    else
      style.setPaddingLeft(20, Style.Unit.PX);  /* 16px for spinner image width, and another 4px for spacing */
    if (!startVisible)
      setVisible(false);
  }

  public LoadingMessage2(String message, boolean startVisible, SpinnerPosition spinnerPosition) {
    this(new Label(message), startVisible, spinnerPosition);
  }

  public LoadingMessage2(String message, boolean startVisible) {
    this(message, startVisible, SpinnerPosition.LEFT);
  }

  public LoadingMessage2(boolean startVisible) {
    this("Loading...", startVisible);
  }

  public LoadingMessage2() {
    this(true);
  }

  public LoadingMessage2(String message) {
    this(message, true);
  }

  public LoadingMessage2(String message, SpinnerPosition spinnerPosition) {
    this(message, true, spinnerPosition);
  }

}
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

package solutions.trsoftware.commons.server.util;

import java.util.UUID;

/**
 * A simpler version of java.util.UUID, which generates a "universally unique id"
 * by delegating to java.util.UUID and encoding the result using a url-safe
 * encoding in base64.  The base 64 algorithm is customized for encoding
 * a 128-bit number, and hence avoids trailing padding characters normally
 * present when using the standard base64 encoding on character data.
 *
 * Because this class encodes the uuid as base 64, the string returned by this
 * class will be approximately 24 chars instead of the 36 by UUID.toString().
 *
 * @see NumberRadixEncoder
 * @author Alex
 */
public abstract class SimpleUUID {

  /** A url-safe b64 encoded version of UUID.random number */
  public static String randomUUID() {
    UUID uuid = UUID.randomUUID();
//    byte[] uuidBytes = int128ToByteArray(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
//    return ServerStringUtils.bytesToStringUtf8(UrlSafeBase64.toStringBase64(uuidBytes));
    return NumberRadixEncoder.toStringBase64(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
  }

  // Utility methods

}
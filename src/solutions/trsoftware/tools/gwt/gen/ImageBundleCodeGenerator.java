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

package solutions.trsoftware.tools.gwt.gen;

import solutions.trsoftware.commons.server.io.file.FileUtils;
import solutions.trsoftware.commons.server.util.ServerStringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;

/**
 * Generates Java source code for a class implementing {@link com.google.gwt.user.client.ui.ImageBundle}
 * with all the image files in a particular directory.
 * 
 * Nov 6, 2009
 *
 * @author Alex
 */
public class ImageBundleCodeGenerator {
  public static void main(String[] args) {
    // TODO: impl invocation via command line
  }

  /** Writes the bundle code to the given output device */
  public static void generateBundleClass(File packageDir, String packageName, String bundleName) {
    FileWriter fileWriter = null;
    try {
      fileWriter = new FileWriter(new File(packageDir, bundleName + ".java"));
      generateBundleSourceCode(packageDir, packageName, bundleName, new PrintWriter(fileWriter));
    }
    catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    finally {
      if (fileWriter != null)
        try {
          fileWriter.close();
        }
        catch (IOException e) {
          e.printStackTrace();
        }
    }
  }

  /** Writes the bundle code to the given output device */
  public static void generateBundleSourceCode(File inputDir, String packageName, String bundleName, PrintWriter out) {
    assert inputDir.isDirectory();

    File[] imageFiles = inputDir.listFiles(new FileFilter() {
      public boolean accept(File pathname) {
        // accept all the files that can be read as images
        try {
          BufferedImage image = ImageIO.read(pathname);
          return image != null && image.getHeight() > 0 && image.getWidth() > 0;
        }
        catch (IOException e) {
          return false;
        }
      }
    });

    out.println("package " + packageName + ";");
    out.println();
    out.println("import com.google.gwt.user.client.ui.AbstractImagePrototype;");
    out.println("import com.google.gwt.user.client.ui.ImageBundle;");
    out.println("import com.google.gwt.core.shared.GWT;");
    out.println();
    out.println("/** Generated by " + ImageBundleCodeGenerator.class.getName() + " on " + new Date() + " */");
    out.println("public interface " + bundleName + " extends ImageBundle {");
    out.println();
    // create a static instance of the bundle
    out.println("  public static final " + bundleName + " instance = GWT.create(" + bundleName + ".class);");
    out.println();
    String[] filenamePrefixes = new String[imageFiles.length];
    String[] methodNames = new String[imageFiles.length];
    for (int i = 0; i < imageFiles.length; i++) {
      File imageFile = imageFiles[i];
      String filename = imageFile.getName();
      String filenamePrefix = FileUtils.filenamePrefix(filename);
      filenamePrefixes[i] = filenamePrefix;
      String validName = ServerStringUtils.toJavaNameIdentifier(filenamePrefix);
      methodNames[i] = validName;
      // we need a valid method name to generate
      if (!filenamePrefix.equals(validName)) {
        // the image filename is not a valid java identifier, therefore can't use it as a method name
        // instead, use the valid name for the method and specify the filename as a resource
        out.printf("  @ImageBundle.Resource(\"%s\")%n", filename);
      }
      out.printf("  AbstractImagePrototype %s();%n", validName);
    }
    out.println();
    // now generate an inner class which can be used to look up the images by the filename prefix (i.e. filename without extension)
    out.println("  public static final class Lookup {");
    out.println("    public static AbstractImagePrototype getByName(String name) {");
    for (int i = 0; i < filenamePrefixes.length; i++) {
      String filenamePrefix = filenamePrefixes[i];
      if (i == 0)
        out.print("      if");
      else
        out.print("      else if");
      out.println(" (\"" + filenamePrefix + "\".equals(name)) return instance." + methodNames[i] + "();");
    }
    out.println("      return null;");  // match not found
    out.println("    }");
    out.println("  }");
    out.println("}");
  }
}

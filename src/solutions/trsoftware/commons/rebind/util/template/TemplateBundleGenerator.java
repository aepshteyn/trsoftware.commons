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

package solutions.trsoftware.commons.rebind.util.template;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.dev.generator.GenUtil;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import solutions.trsoftware.commons.client.util.template.*;
import solutions.trsoftware.commons.server.util.FileTemplateParser;

import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Just like an ImageBundle, except each resource file defines a string template.
 * (Lots of this code is borrowed from ImageBundleGenerator), so lots
 * of it could be extracted into a superclass if this code ever makes it
 * into GWT core.
 *
 * Generates an implementation of a user-defined interface <code>T</code> that
 * extends {@link TemplateBundle}.
 *
 * Each method in <code>T</code> must be declared to return
 * {@link com.google.gwt.user.client.ui.AbstractImagePrototype}, take no
 * parameters, and optionally specify the metadata tag <code>gwt.resource</code>
 * as the name of an image that can be found in the classpath. In the absence of
 * the metatadata tag, the method name with an extension of
 * <code>.png, .jpg, or .gif</code> defines the name of the image, and the
 * image file must be located in the same package as <code>T</code>.
 */
public class TemplateBundleGenerator extends Generator {

  /**
   * Simple wrapper around JMethod that allows for unit test mocking.
   */
  /* private */interface JMethodOracle {

    TemplateBundle.Resource getAnnotation(Class<TemplateBundle.Resource> clazz);

    String[][] getMetaData(String metadataTag);

    String getName();

    String getPackageName();
  }

  /**
   * Indirection around the act of looking up a resource that allows for unit
   * test mocking.
   */
  /* private */interface ResourceLocator {
    /**
     *
     * @param resName the resource name in a format that could be passed to
     *          <code>ClassLoader.getResource()</code>
     * @return <code>true</code> if the resource is present
     */
    boolean isResourcePresent(String resName);
  }

  private class JMethodOracleImpl implements JMethodOracle {
    private final JMethod delegate;

    public JMethodOracleImpl(JMethod delegate) {
      this.delegate = delegate;
    }

    public TemplateBundle.Resource getAnnotation(Class<TemplateBundle.Resource> clazz) {
      return delegate.getAnnotation(clazz);
    }

    public String[][] getMetaData(String metadataTag) {
      return delegate.getMetaData(metadataTag);
    }

    public String getName() {
      return delegate.getName();
    }

    public String getPackageName() {
      return delegate.getEnclosingType().getPackage().getName();
    }
  }

  /* private */static final String MSG_JAVADOC_FORM_DEPRECATED = "Use of @gwt.resource in javadoc is deprecated; use the annotation TemplateBundle.@Resource instead";

  /* private */static final String MSG_MULTIPLE_ANNOTATIONS = "You are using both the @Resource annotation and the deprecated @gwt.resource in javadoc; @Resource will be used, and @gwt.resource will be ignored";

  /* private */static final String MSG_NO_FILE_BASED_ON_METHOD_NAME = "No matching image resource was found; any of the following filenames would have matched had they been present:";

  private static final String TEMPLATE_QNAME = "solutions.trsoftware.commons.client.util.template.Template";
  private static final String TEMPLATEPART_QNAME = "solutions.trsoftware.commons.client.util.template.TemplatePart";
  private static final String STRINGPART_QNAME = "solutions.trsoftware.commons.client.util.template.StringPart";
  private static final String VARIABLEPART_QNAME = "solutions.trsoftware.commons.client.util.template.VariablePart";

  private static final String GWT_QNAME = "com.google.gwt.core.client.GWT";

  private static final String[] TEMPLATE_FILE_EXTENSIONS = {"html", "txt"};

  private static final String TEMPLATEBUNDLE_QNAME = "solutions.trsoftware.commons.client.util.template.TemplateBundle";

  private static final String METADATA_TAG = "gwt.resource";

  /* private */static String msgCannotFindImageFromMetaData(String imgResName) {
    return "Unable to find image resource '" + imgResName + "'";
  }

  private final ResourceLocator resLocator;

  /**
   * Default constructor for image bundle. Locates resources using this class's
   * own class loader.
   */
  public TemplateBundleGenerator() {
    this(new ResourceLocator() {
      public boolean isResourcePresent(String resName) {
        URL url = this.getClass().getClassLoader().getResource(resName);
        return url != null;
      }
    });
  }

  /**
   * Default access so that it can be accessed by unit tests.
   */
  /* private */TemplateBundleGenerator(ResourceLocator resourceLocator) {
    assert (resourceLocator != null);
    this.resLocator = resourceLocator;
  }

  @Override
  public String generate(TreeLogger logger, GeneratorContext context,
      String typeName) throws UnableToCompleteException {

    TypeOracle typeOracle = context.getTypeOracle();

    // Get metadata describing the user's class.
    JClassType userType = getValidUserType(logger, typeName, typeOracle);

    // Write the new class.
    JMethod[] imgMethods = userType.getOverridableMethods();
    String resultName = generateImplClass(logger, context, userType, imgMethods);

    // Return the complete name of the generated class.
    return resultName;
  }

  /**
   * Gets the resource name of the image associated with the specified image
   * bundle method in a form that can be passed to
   * <code>ClassLoader.getResource()</code>.
   *
   * @param logger the main logger
   * @param method the image bundle method whose image name is being sought
   * @return a resource name that is suitable to be passed into
   *         <code>ClassLoader.getResource()</code>; never returns
   *         <code>null</code>
   * @throws UnableToCompleteException thrown if a resource was specified but
   *           could not be found on the classpath
   */
  /* private */String getImageResourceName(TreeLogger logger,
      JMethodOracle method) throws UnableToCompleteException {
    String imgName = tryGetImageNameFromMetaData(logger, method);
    if (imgName != null) {
      return imgName;
    } else {
      return getImageNameFromMethodName(logger, method);
    }
  }

  private String computeSubclassName(JClassType userType) {
    String baseName = userType.getName().replace('.', '_');
    return baseName + "_generatedBundle";
  }

  private void generateTemplateMethod(SourceWriter sw, JMethod method, String resourceName) {
    // build the template object right now (at compile time) then "serialize" it
    Template template = FileTemplateParser.getInstance().getTemplate(resourceName);
    Iterator<TemplatePart> partIterator = template.getParts().iterator();
    if (!partIterator.hasNext())
      return; // this template doesn't have any parts

    // Create a singleton that this method can return. There is no need to
    // create a new instance every time this method is called, since
    // Template is immutable; we just laziliy instantiate the field to amortize the cost
    String name = method.getName();
    String fieldName = name + "_field";
    sw.println();
    sw.println("private static Template " + fieldName + " = null;");
    sw.println();
    String decl = method.getReadableDeclaration(false, true, true, true, true);
    {
      sw.print(decl);
      sw.println(" {");
      {
        sw.indent();
        sw.println("if (" + fieldName + " == null) {");
        {
          sw.indent();
          sw.println(fieldName + " = new Template(java.util.Arrays.<TemplatePart>asList(");
          {
            sw.indent();
            while (partIterator.hasNext()) {
              TemplatePart part = partIterator.next();
              sw.print("new ");
              if (part instanceof VariablePart)
                sw.print("VariablePart(\"" + ((VariablePart)part).getVarName() + "\")");
              else if (part instanceof StringPart)
                sw.print("StringPart(\"" + escape(part.toString()) + "\")");
              if (partIterator.hasNext())
                sw.println(", ");
            }
            sw.println("));");  // close Arrays.asList
            sw.outdent();
          }
          sw.println("}");  // close Arrays.asList
          sw.outdent();
        }
        sw.println("return " + fieldName + ";");
        sw.outdent();
      }
      sw.println("}");
    }
  }

  /**
   * Generates the image bundle implementation class, checking each method for
   * validity as it is encountered.
   */
  private String generateImplClass(TreeLogger logger, GeneratorContext context,
      JClassType userType, JMethod[] imageMethods)
      throws UnableToCompleteException {
    // Lookup the type info for AbstractImagePrototype so that we can check for
    // the proper return type
    // on image bundle methods.
    final JClassType abstractImagePrototype;
    try {
      abstractImagePrototype = userType.getOracle().getType(
          TEMPLATE_QNAME);
    } catch (NotFoundException e) {
      logger.log(TreeLogger.ERROR, TEMPLATE_QNAME
          + " class is not available", e);
      throw new UnableToCompleteException();
    }

    // Compute the package and class names of the generated class.
    String pkgName = userType.getPackage().getName();
    String subName = computeSubclassName(userType);

    // Begin writing the generated source.
    ClassSourceFileComposerFactory f = new ClassSourceFileComposerFactory(
        pkgName, subName);
    f.addImport(TEMPLATE_QNAME);
    f.addImport(TEMPLATEPART_QNAME);
    f.addImport(STRINGPART_QNAME);
    f.addImport(VARIABLEPART_QNAME);
    f.addImport(GWT_QNAME);
    f.addImplementedInterface(userType.getQualifiedSourceName());

    PrintWriter pw = context.tryCreate(logger, pkgName, subName);
    if (pw != null) {
      SourceWriter sw = f.createSourceWriter(context, pw);

      // Store the computed image names so that we don't have to lookup them up
      // again.
      List<String> imageResNames = new ArrayList<String>();

      for (JMethod method : imageMethods) {
        String branchMsg = "Analyzing method '" + method.getName()
            + "' in type " + userType.getQualifiedSourceName();
        TreeLogger branch = logger.branch(TreeLogger.DEBUG, branchMsg, null);

        // Verify that this method is valid on an image bundle.
        if (method.getReturnType() != abstractImagePrototype) {
          branch.log(TreeLogger.ERROR, "Return type must be "
              + TEMPLATE_QNAME, null);
          throw new UnableToCompleteException();
        }

        if (method.getParameters().length > 0) {
          branch.log(TreeLogger.ERROR, "Method must have zero parameters", null);
          throw new UnableToCompleteException();
        }

        // Find the associated imaged resource.
        String imageResName = getImageResourceName(branch,
            new JMethodOracleImpl(method));
        assert (imageResName != null);
        imageResNames.add(imageResName);
      }

      // create a cache for all the instances of Template (these are immutable,
      // and can be lazily instantiated on demand).  The keys for the map
      // will be the names of the template resources.
      sw.println();

      // Generate an implementation of each method.
      int imageResNameIndex = 0;
      for (JMethod method : imageMethods) {
        generateTemplateMethod(sw, method, imageResNames.get(imageResNameIndex++));
      }

      // Finish.
      sw.commit(logger);
    }

    return f.getCreatedClassName();
  }

  /**
   * Attempts to get the image name from the name of the method itself by
   * speculatively appending various image-like file extensions in a prioritized
   * order. The first image found, if any, is used.
   *
   * @param logger if no matching image resource is found, an explanatory
   *          message will be logged
   * @param method the method whose name is being examined for matching image
   *          resources
   * @return a resource name that is suitable to be passed into
   *         <code>ClassLoader.getResource()</code>; never returns
   *         <code>null</code>
   * @throws UnableToCompleteException thrown when no image can be found based
   *           on the method name
   */
  private String getImageNameFromMethodName(TreeLogger logger,
      JMethodOracle method) throws UnableToCompleteException {
    String pkgName = method.getPackageName();
    String pkgPrefix = pkgName.replace('.', '/');
    if (pkgPrefix.length() > 0) {
      pkgPrefix += "/";
    }
    String methodName = method.getName();
    String pkgAndMethodName = pkgPrefix + methodName;
    List<String> testImgNames = new ArrayList<String>();
    for (int i = 0; i < TEMPLATE_FILE_EXTENSIONS.length; i++) {
      String testImgName = pkgAndMethodName + '.' + TEMPLATE_FILE_EXTENSIONS[i];
      if (resLocator.isResourcePresent(testImgName)) {
        return testImgName;
      }
      testImgNames.add(testImgName);
    }

    TreeLogger branch = logger.branch(TreeLogger.ERROR,
        MSG_NO_FILE_BASED_ON_METHOD_NAME, null);
    for (String testImgName : testImgNames) {
      branch.log(TreeLogger.ERROR, testImgName, null);
    }

    throw new UnableToCompleteException();
  }

  private JClassType getValidUserType(TreeLogger logger, String typeName,
      TypeOracle typeOracle) throws UnableToCompleteException {
    try {
      // Get the type that the user is introducing.
      JClassType userType = typeOracle.getType(typeName);

      // Get the type this generator is designed to support.
      JClassType magicType = typeOracle.findType(TEMPLATEBUNDLE_QNAME);

      // Ensure it's an interface.
      if (userType.isInterface() == null) {
        logger.log(TreeLogger.ERROR, userType.getQualifiedSourceName()
            + " must be an interface", null);
        throw new UnableToCompleteException();
      }

      // Ensure proper derivation.
      if (!userType.isAssignableTo(magicType)) {
        logger.log(TreeLogger.ERROR, userType.getQualifiedSourceName()
            + " must be assignable to " + magicType.getQualifiedSourceName(),
            null);
        throw new UnableToCompleteException();
      }

      return userType;

    } catch (NotFoundException e) {
      logger.log(TreeLogger.ERROR, "Unable to find required type(s)", e);
      throw new UnableToCompleteException();
    }
  }

  /**
   * Attempts to get the image name (verbatim) from an annotation.
   *
   * @return the string specified in in the {@link TemplateBundle.Resource}
   *         annotation, or <code>null</code>
   */
  private String tryGetImageNameFromAnnotation(JMethodOracle method) {
    TemplateBundle.Resource imgResAnn = method.getAnnotation(TemplateBundle.Resource.class);
    String imgName = null;
    if (imgResAnn != null) {
      imgName = imgResAnn.value();
    }
    return imgName;
  }

  /**
   * Attempts to get the image name (verbatim) from old-school javadoc metadata.
   *
   * @return the string specified in "resource" javadoc metdata tag; never
   *         returns <code>null</code>
   */
  private String tryGetImageNameFromJavaDoc(JMethodOracle method) {
    String imgName = null;
    String[][] md = method.getMetaData(METADATA_TAG);
    if (md.length == 1) {
      int lastTagIndex = md.length - 1;
      int lastValueIndex = md[lastTagIndex].length - 1;
      imgName = md[lastTagIndex][lastValueIndex];
    }
    return imgName;
  }

  /**
   * Attempts to get the image name from either an annotation or from pre-1.5
   * javadoc metadata, logging warnings to educate the user about deprecation
   * and behaviors in the face of conflicting metadata (for example, if both
   * forms of metadata are present).
   *
   * @param logger if metadata is found but the specified resource isn't
   *          available, a warning is logged
   * @param method the image bundle method whose associated image resource is
   *          being sought
   * @return a resource name that is suitable to be passed into
   *         <code>ClassLoader.getResource()</code>, or <code>null</code>
   *         if metadata wasn't provided
   * @throws UnableToCompleteException thrown when metadata is provided but the
   *           resource cannot be found
   */
  private String tryGetImageNameFromMetaData(TreeLogger logger,
      JMethodOracle method) throws UnableToCompleteException {
    String imgFileName = null;
    String imgNameAnn = tryGetImageNameFromAnnotation(method);
    String imgNameJavaDoc = tryGetImageNameFromJavaDoc(method);
    if (imgNameJavaDoc != null) {
      if (imgNameAnn == null) {
        // There is JavaDoc metadata but no annotation.
        imgFileName = imgNameJavaDoc;
        if (GenUtil.warnAboutMetadata()) {
          logger.log(TreeLogger.WARN, MSG_JAVADOC_FORM_DEPRECATED, null);
        }
      } else {
        // There is both JavaDoc metadata and an annotation.
        logger.log(TreeLogger.WARN, MSG_MULTIPLE_ANNOTATIONS, null);
        imgFileName = imgNameAnn;
      }
    } else if (imgNameAnn != null) {
      // There is only an annotation.
      imgFileName = imgNameAnn;
    }
    assert (imgFileName == null || (imgNameAnn != null || imgNameJavaDoc != null));

    if (imgFileName == null) {
      // Exit early because neither an annotation nor javadoc was found.
      return null;
    }

    // If the name has no slashes (that is, it isn't a fully-qualified resource
    // name), then prepend the enclosing package name automatically, being
    // careful about the default package.
    if (imgFileName.indexOf("/") == -1) {
      String pkgName = method.getPackageName();
      if (!"".equals(pkgName)) {
        imgFileName = pkgName.replace('.', '/') + "/" + imgFileName;
      }
    }

    if (!resLocator.isResourcePresent(imgFileName)) {
      // Not found.
      logger.log(TreeLogger.ERROR, msgCannotFindImageFromMetaData(imgFileName),
          null);
      throw new UnableToCompleteException();
    }

    // Success.
    return imgFileName;
  }

}
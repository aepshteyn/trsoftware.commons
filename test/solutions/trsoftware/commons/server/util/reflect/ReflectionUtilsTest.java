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

package solutions.trsoftware.commons.server.util.reflect;

import junit.framework.TestCase;
import solutions.trsoftware.commons.server.io.ResourceLocator;
import solutions.trsoftware.commons.server.io.StringPrintStream;
import solutions.trsoftware.commons.server.io.file.FileSet;
import solutions.trsoftware.commons.shared.util.SetUtils;
import solutions.trsoftware.commons.shared.util.StringUtils;
import solutions.trsoftware.commons.shared.util.reflect.ClassNameParser;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

import static solutions.trsoftware.commons.server.util.reflect.ReflectionUtils.*;

/**
 * @author Alex, 1/9/14
 */
public class ReflectionUtilsTest extends TestCase {

  public interface IFoo<A,B> {}
  public interface IBar {}
  public static class Foo implements IFoo {}
  public static class FooIntString implements IFoo<Integer, String> {}

  public interface IFooSubA extends IFoo {}
  public interface IFooSubB extends IFoo {}
  public static class FooSubA implements IFooSubA {}
  public static class FooSubB implements IFooSubB {}
  public static class FooSubAB implements IFooSubA, IFooSubB {}
  public static class FooSubABSub extends FooSubAB implements IBar {}


  public void testGetActualTypeArguments() throws Exception {
    assertEquals(Arrays.<Type>asList(Integer.class, String.class),
        Arrays.asList(getGenericTypeArgumentsForInterface(FooIntString.class, IFoo.class)));
    assertNull(getGenericTypeArgumentsForInterface(Foo.class, IFoo.class));
    assertNull(getGenericTypeArgumentsForInterface(FooIntString.class, Object.class));
  }


  public void testGetAllTypesAssignableFrom() throws Exception {
    assertAllAssignableFrom(null,
        expectedSet());

    assertAllAssignableFrom(Object.class,
        expectedSet(Object.class));

    assertAllAssignableFrom(int.class,
        expectedSet(int.class));  // primitives aren't assignable to Object

    assertAllAssignableFrom(int[].class,
        expectedSet(int[].class, Object.class, Cloneable.class, Serializable.class)); // all array classes implement Cloneable and Serializable

    assertAllAssignableFrom(Foo.class,
        expectedSet(Foo.class, IFoo.class, Object.class));

    assertAllAssignableFrom(IFoo.class,
        expectedSet(IFoo.class, Object.class));

    assertAllAssignableFrom(IFooSubA.class,
        expectedSet(IFooSubA.class, IFoo.class, Object.class));

    assertAllAssignableFrom(FooSubAB.class,
        expectedSet(FooSubAB.class, IFooSubA.class, IFoo.class, IFooSubB.class, Object.class));

    assertAllAssignableFrom(FooSubABSub.class,
        expectedSet(FooSubABSub.class, IBar.class, FooSubAB.class, IFooSubA.class, IFoo.class, IFooSubB.class, Object.class));
  }

  public void testGetAllTypesAssignableFromAll() throws Exception {
    assertAllAssignableFromAll(
        new Class[]{FooSubAB.class},
        expectedSet(FooSubAB.class, IFooSubA.class, IFoo.class, IFooSubB.class, Object.class));

    assertAllAssignableFromAll(
        new Class[]{FooSubAB.class, FooSubABSub.class},
        expectedSet(FooSubAB.class, IFooSubA.class, IFoo.class, IFooSubB.class, Object.class));

    assertAllAssignableFromAll(
        new Class[]{FooSubAB.class, FooSubABSub.class, IFooSubA.class},
        expectedSet(IFooSubA.class, IFoo.class, Object.class));

    assertAllAssignableFromAll(
         new Class[]{FooSubAB.class, FooSubABSub.class, IFooSubA.class, Object.class},
        expectedSet(Object.class));

    assertAllAssignableFromAll(
         new Class[]{FooSubAB.class, FooSubABSub.class, IFooSubA.class, int.class},
        expectedSet());  // no classes have anything in common with the primitive type int

    assertAllAssignableFromAll(
        new Class[]{LinkedList.class, ArrayList.class},
        expectedSet(List.class, Collection.class, Iterable.class, Cloneable.class, Serializable.class, AbstractList.class, AbstractCollection.class, Object.class));

    assertAllAssignableFromAll(
        new Class[]{LinkedList.class, ArrayList.class, int[].class, Integer[].class},
        expectedSet(Cloneable.class, Serializable.class, Object.class));

    assertAllAssignableFromAll(
        new Class[]{LinkedList.class, ArrayList.class, int[].class, Integer[].class, null},
        expectedSet());  // no classes have anything in common with a null arg
  }

  public void testGetCompilerOutputDir() throws Exception {
    Class<? extends ReflectionUtilsTest> thisClass = getClass();
    File result = getCompilerOutputDir(thisClass);
    System.out.println("result = " + result);
    assertTrue(result.exists());
    assertTrue(result.isDirectory());
    // confirm that the result indeed contains this class
    FileSet allClassFiles = new FileSet(result);
    assertTrue(allClassFiles.contains(ReflectionUtils.getClassFile(thisClass).toFile()));
  }

  private static void assertAllAssignableFrom(Class<?> arg, Set<Class<?>> expected) {
    Set<Class<?>> actual = getAllTypesAssignableFrom(arg);
    System.out.println(StringUtils.methodCallToStringWithResult("getAllTypesAssignableFrom", actual, arg));
    assertEquals(expected, actual);
    for (Class<?> x : actual) {
      assertTrue(x.isAssignableFrom(arg));
    }
  }

  private static void assertAllAssignableFromAll(Class<?>[] args, Set<Class<?>> expected) {
    Set<Class<?>> actual = getAllTypesAssignableFromAll(args);
    System.out.println(StringUtils.methodCallToStringWithResult("getAllTypesAssignableFromAll", actual, args));
    assertEquals(expected, actual);
    for (Class<?> x : actual) {
      for (Class arg : args) {
        assertTrue(x.isAssignableFrom(arg));
      }
    }
  }

  private static Set<Class<?>> expectedSet(Class<?>... args) {
    return SetUtils.newSet(args);
  }

  public void testIsJavaKeyword() throws Exception {
    // list obtained from https://docs.oracle.com/javase/specs/jls/se9/html/jls-3.html#jls-3.9
    String[] keywordsFromJLS = {"abstract", "continue", "for", "new", "switch", "assert", "default", "if", "package", "synchronized", "boolean", "do", "goto", "private", "this", "break", "double", "implements", "protected", "throw", "byte", "else", "import", "public", "throws", "case", "enum", "instanceof", "return", "transient", "catch", "extends", "int", "short", "try", "char", "final", "interface", "static", "void", "class", "finally", "long", "strictfp", "volatile", "const", "float", "native", "super", "while", "_"};
    for (String k : keywordsFromJLS) {
      assertTrue(k, isJavaKeyword(k));
      assertFalse(k, isJavaKeyword(k + "_"));
    }
  }

  public void testGetClassFile() throws Exception {
    class LocalClass {}
    assertTrue(getClassFile(getClass()).exists());
    assertTrue(getClassFile(ReflectionUtils.class).exists());
    assertTrue(getClassFile(NestedClass.class).exists());
    assertTrue(getClassFile(InnerClass.class).exists());
    assertTrue(getClassFile(LocalClass.class).exists());
    // now try some anonymous classes
    assertTrue(getClassFile(new NestedClass(){}.getClass()).exists());
    assertTrue(getClassFile(new NestedClass(){}.getClass()).exists());
    // now try some array classes
    assertTrue(getClassFile(ReflectionUtils[].class).exists());
    assertTrue(getClassFile(ReflectionUtils[][].class).exists());
    assertTrue(getClassFile(NestedClass[].class).exists());
    assertTrue(getClassFile(InnerClass[].class).exists());
    assertTrue(getClassFile(LocalClass[].class).exists());
    // now try some classes from the Java runtime
    assertNotNull(getClassFile(Map.class));
    assertNotNull(getClassFile(Map.Entry.class));
    // now try some primitives
    assertNull(getClassFile(int.class));
    assertNull(getClassFile(void.class));
    assertNull(getClassFile(byte[].class));
  }

  private File getClassFile(Class cls) {
    StringPrintStream msg = new StringPrintStream();
    msg.printf("getClassFile(%s)", cls);
    ResourceLocator classFileResource = ReflectionUtils.getClassFile(cls);
    File file = null;
    if (classFileResource == null) {
      msg.printf("%n -> null");
    }
    else {
      msg.printf("%n -> (resource) %s", classFileResource);
      file = classFileResource.toFile();
      if (file != null) {
        msg.printf("%n -> (file) %s", file);
        // check that the filename corresponds to the binary name of the class
        ClassNameParser classNameParser = new ClassNameParser(getRootComponentTypeOfArray(cls));
        assertEquals(classNameParser.getComplexName() + ".class", file.getName());
      }
    }
    System.out.println(msg);
    return file;
  }

  private static class NestedClass {}
  private class InnerClass {}

  public void testGetRootComponentTypeOfArray() throws Exception {
    assertEquals(Foo.class, getRootComponentTypeOfArray(Foo.class));
    assertEquals(Foo.class, getRootComponentTypeOfArray(Foo[].class));
    assertEquals(Foo.class, getRootComponentTypeOfArray(Foo[][].class));
    assertEquals(Foo.class, getRootComponentTypeOfArray(Foo[][][].class));
  }

  public void testListPublicGetters() throws Exception {
    Class<SimpleBean> cls = SimpleBean.class;
    List<Method> expected = Arrays.asList(
        cls.getMethod("getX"),
        cls.getMethod("getY"),
        cls.getMethod("getName"),
        cls.getMethod("isFoo"),
        cls.getMethod("getClass")
    );
    assertEquals(new HashSet<>(expected), new HashSet<>(listPublicGetters(cls)));
  }


  private static class SimpleBean {
    private int x, y;
    private String name;
    private boolean foo;

    public SimpleBean() {
    }

    public SimpleBean(int x, int y, String name, boolean foo) {
      this.x = x;
      this.y = y;
      this.name = name;
      this.foo = foo;
    }

    public int getX() {
      return x;
    }

    public void setX(int x) {
      this.x = x;
    }

    public int getY() {
      return y;
    }

    public void setY(int y) {
      this.y = y;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public boolean isFoo() {
      return foo;
    }

    public void setFoo(boolean foo) {
      this.foo = foo;
    }
  }
}

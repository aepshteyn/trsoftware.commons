package solutions.trsoftware.commons.shared.util;

import junit.framework.TestCase;
import solutions.trsoftware.commons.shared.util.mutable.MutableInteger;

import static solutions.trsoftware.commons.client.testutil.AssertUtils.assertThrows;
import static solutions.trsoftware.commons.shared.util.NumberUtils.*;

/**
 * @author Alex
 * @since 12/30/2017
 */
public class NumberUtilsTest extends TestCase {

  public void testMaxValue() throws Exception {
    assertEquals(Double.MAX_VALUE, maxValue(Double.class));
    assertEquals(Float.MAX_VALUE, maxValue(Float.class));
    assertEquals(Integer.MAX_VALUE, maxValue(Integer.class));
    assertEquals(Long.MAX_VALUE, maxValue(Long.class));
    assertEquals(Short.MAX_VALUE, maxValue(Short.class));
    assertEquals(Byte.MAX_VALUE, maxValue(Byte.class));
    assertThrows(IllegalArgumentException.class, new Runnable() {
      @Override
      public void run() {
        maxValue(MutableInteger.class);
      }
    });
  }

  public void testMinValue() throws Exception {
    assertEquals(Double.MIN_VALUE, minValue(Double.class));
    assertEquals(Float.MIN_VALUE, minValue(Float.class));
    assertEquals(Integer.MIN_VALUE, minValue(Integer.class));
    assertEquals(Long.MIN_VALUE, minValue(Long.class));
    assertEquals(Short.MIN_VALUE, minValue(Short.class));
    assertEquals(Byte.MIN_VALUE, minValue(Byte.class));
    assertThrows(IllegalArgumentException.class, new Runnable() {
      @Override
      public void run() {
        minValue(MutableInteger.class);
      }
    });
  }

  public void testFromDouble() throws Exception {
    assertEquals(1.5, fromDouble(Double.class, 1.5));
    assertEquals(1.5f, fromDouble(Float.class, 1.5));
    assertEquals(1, fromDouble(Integer.class, 1.5));
    assertEquals(1L, fromDouble(Long.class, 1.5));
    assertEquals((short)1, fromDouble(Short.class, 1.5));
    assertEquals((byte)1, fromDouble(Byte.class, 1.5));
    assertThrows(IllegalArgumentException.class, new Runnable() {
      @Override
      public void run() {
        fromDouble(MutableInteger.class, 1.5);
      }
    });
    fail("TODO"); // TODO: test narrowing conversions
  }

  public void testAllPrimitiveWrapperTypes() throws Exception {
    fail("TODO"); // TODO
  }

}
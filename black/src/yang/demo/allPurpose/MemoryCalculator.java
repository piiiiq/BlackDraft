package yang.demo.allPurpose;

import java.io.PrintStream;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Stack;

public final class MemoryCalculator
{
  public static void premain(String agentArgs, Instrumentation inst)
  {
    instrumentation = inst;
    System.out.println("agent start done.");
  }
  
  public static long shallowSizeOf(Object obj)
  {
    if (instrumentation == null) {
      throw new IllegalStateException("Instrumentation initialize failed");
    }
    if (isSharedObj(obj)) {
      return 0L;
    }
    return instrumentation.getObjectSize(obj);
  }
  
  public static long deepSizeOf(Object obj)
  {
    Map calculated = new IdentityHashMap();
    Stack unCalculated = new Stack();
    unCalculated.push(obj);
    long result = 0L;
    do
    {
      result += doSizeOf(unCalculated, calculated);
    } while (!unCalculated.isEmpty());
    return result;
  }
  
  private static boolean isSharedObj(Object obj)
  {
    if ((obj instanceof Comparable))
    {
      if ((obj instanceof Enum)) {
        return true;
      }
      if ((obj instanceof String)) {
        return obj == ((String)obj).intern();
      }
      if ((obj instanceof Boolean)) {
        return (obj == Boolean.TRUE) || (obj == Boolean.FALSE);
      }
      if ((obj instanceof Integer)) {
        return obj == Integer.valueOf(((Integer)obj).intValue());
      }
      if ((obj instanceof Short)) {
        return obj == Short.valueOf(((Short)obj).shortValue());
      }
      if ((obj instanceof Byte)) {
        return obj == Byte.valueOf(((Byte)obj).byteValue());
      }
      if ((obj instanceof Long)) {
        return obj == Long.valueOf(((Long)obj).longValue());
      }
      if ((obj instanceof Character)) {
        return obj == Character.valueOf(((Character)obj).charValue());
      }
    }
    return false;
  }
  
  private static boolean isEscaped(Object obj, Map calculated)
  {
    return (obj == null) || (calculated.containsKey(obj)) || 
      (isSharedObj(obj));
  }
  
  private static long doSizeOf(Stack unCalculated, Map calculated)
  {
    Object obj = unCalculated.pop();
    if (isEscaped(obj, calculated)) {
      return 0L;
    }
    Class clazz = obj.getClass();
    if (clazz.isArray()) {
      doArraySizeOf(clazz, obj, unCalculated);
    } else {
      while (clazz != null)
      {
        Field[] fields = clazz.getDeclaredFields();
        Field[] arrayOfField1;
        int j = (arrayOfField1 = fields).length;
        for (int i = 0; i < j; i++)
        {
          Field field = arrayOfField1[i];
          if ((!Modifier.isStatic(field.getModifiers())) && 
            (!field.getType().isPrimitive()))
          {
            field.setAccessible(true);
            try
            {
              unCalculated.add(field.get(obj));
            }
            catch (IllegalAccessException ex)
            {
              throw new RuntimeException(ex);
            }
          }
        }
        clazz = clazz.getSuperclass();
      }
    }
    calculated.put(obj, null);
    return shallowSizeOf(obj);
  }
  
  private static void doArraySizeOf(Class arrayClazz, Object array, Stack unCalculated)
  {
    if (!arrayClazz.getComponentType().isPrimitive())
    {
      int length = Array.getLength(array);
      for (int i = 0; i < length; i++) {
        unCalculated.add(Array.get(array, i));
      }
    }
  }
  
  private static Instrumentation instrumentation = null;
}

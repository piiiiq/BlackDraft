package yang.demo.allPurpose;

public class debug
{
  public static String getNameOfClassAndMethodOfCaller()
  {
    StackTraceElement[] temp = Thread.currentThread().getStackTrace();
    StackTraceElement a = temp[3];
    return "from：" + a.getClassName() + "." + a.getMethodName();
  }
}

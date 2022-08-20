package yang.app.qt.black;

abstract class blackCommand
{
  String command;
  String info;
  String[] args;
  boolean noArgs;
  
  public blackCommand(String command, String info)
  {
    this.command = command;
    this.info = info;
  }
  
  public blackCommand(String command, String info, boolean noArgs)
  {
    this.command = command;
    this.info = info;
    this.noArgs = noArgs;
  }
  
  public abstract void action(String[] args);
}

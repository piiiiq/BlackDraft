package yang.app.qt.black;

class dataForOpenedFile
{
  String filename;
  String content;
  blacktext editor;
  boolean isSaved;
  
  public dataForOpenedFile(String filename, String content, blacktext editor, boolean isSaved)
  {
    this.filename = filename;
    this.content = content;
    this.isSaved = isSaved;
  }
}

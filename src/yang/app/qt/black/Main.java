package yang.app.qt.black;

import java.io.File;
import java.io.IOException;


public class Main implements RealtimeProcessInterface{
 
	/**
	 * @param args
	 */
	private RealtimeProcess mRealtimeProcess = null;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Main().test();
	}
	public void test(){
		mRealtimeProcess = new RealtimeProcess(this);
		//mRealtimeProcess.setDirectory("");
//		File file = new File("./Tools/wget.exe");
//		String cmd = file.getAbsolutePath().toString()+" https://github.com/piiiiq/BlackDraft/releases/download/0.665/0.88.zip";
//		System.out.println(cmd);
		mRealtimeProcess.setCommand(new String[] {"ping"});
		
		try {
			try {
				mRealtimeProcess.start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(mRealtimeProcess.getAllResult());
	}
	public void onNewStdoutListener(String newStdout) {
		// TODO Auto-generated method stub
		System.out.println("==>STDOUT  >  " + newStdout);
		
	}
	public void onNewStderrListener(String newStderr) {
		// TODO Auto-generated method stub
		System.out.println("==>STDERR  >  " + newStderr);
	}
	public void onProcessFinish(int resultCode) {
		// TODO Auto-generated method stub
		System.out.println("==>RESULT_CODE  >  " + resultCode);
	}
}
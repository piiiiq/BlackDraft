package yang.app.qt.black;

import io.qt.widgets.QApplication;
import java.util.HashMap;

public abstract class bRunnable {
	private volatile boolean stop;
	private boolean onlyOnce;
	private HashMap<String, Object> datas = new HashMap();
	int ms;
	Thread t;
	int count = -1;
	int currentCount;

	public bRunnable(int ms) {
		this.ms = ms;
		doingInUIThreadNow();
		start();
	}

	public bRunnable(int ms, boolean start) {
		this.ms = ms;
		doingInUIThreadNow();
		if (start) {
			start();
		}
	}

	public bRunnable(int ms, boolean start, boolean isRunNow, boolean inUIThread, boolean onlyOnce) {
		this.ms = ms;
		if (isRunNow) {
			if (inUIThread) {
				doingInUIThreadNow();
			} else {
				doingNow();
			}
		} else if (inUIThread) {
			doingInUIThread();
		} else {
			doing();
		}
		this.onlyOnce = onlyOnce;
		if (start) {
			start();
		}
	}

	public bRunnable(int ms, boolean start, boolean isRunNow, boolean inUIThread, boolean onlyOnce, int taskRunCount) {
		this.ms = ms;
		this.count = taskRunCount;
		if (isRunNow) {
			if (inUIThread) {
				doingInUIThreadNow();
			} else {
				doingNow();
			}
		} else if (inUIThread) {
			doingInUIThread();
		} else {
			doing();
		}
		this.onlyOnce = onlyOnce;
		if (start) {
			start();
		}
	}

	public void execInUIThread(Runnable run) {
		black.b.uiRun(black.b,run);
	}

	public void doing() {
		this.t = new Thread(new Runnable() {
			public void run() {
				while (!bRunnable.this.stop) {
					try {
						Thread.sleep(bRunnable.this.ms);
					} catch (InterruptedException e) {
						break;
					}
					if (bRunnable.this.stop) {
						break;
					}
					bRunnable.this.run();
					bRunnable.this.currentCount += 1;
					if ((bRunnable.this.onlyOnce) || ((bRunnable.this.count != -1)
							&& (bRunnable.this.currentCount >= bRunnable.this.count))) {
						break;
					}
				}
			}
		});
	}

	public void doingNow() {
		this.t = new Thread(new Runnable() {
			public void run() {
				while (!bRunnable.this.stop) {
					bRunnable.this.run();
					bRunnable.this.currentCount += 1;
					if ((bRunnable.this.onlyOnce)
							|| ((bRunnable.this.count != -1) && (bRunnable.this.currentCount >= bRunnable.this.count))
							|| (bRunnable.this.stop)) {
						break;
					}
					try {
						Thread.sleep(bRunnable.this.ms);
					} catch (InterruptedException e) {
						break;
					}
				}
			}
		});
	}

	public void doingInUIThreadNow() {
		this.t = new Thread(new Runnable() {
			public void run() {
				while (!bRunnable.this.stop) {
					black.b.uiRun(black.b,new Runnable() {
						public void run() {
							bRunnable.this.run();
						}
					});
					bRunnable.this.currentCount += 1;
					if ((bRunnable.this.onlyOnce)
							|| ((bRunnable.this.count != -1) && (bRunnable.this.currentCount >= bRunnable.this.count))
							|| (bRunnable.this.stop)) {
						break;
					}
					try {
						Thread.sleep(bRunnable.this.ms);
					} catch (InterruptedException e) {
						break;
					}
				}
			}
		});
	}

	public void doingInUIThread() {
		this.t = new Thread(new Runnable() {
			public void run() {
				while (!bRunnable.this.stop) {
					try {
						Thread.sleep(bRunnable.this.ms);
					} catch (InterruptedException e) {
						break;
					}
					if (bRunnable.this.stop) {
						break;
					}
					
					black.b.uiRun(black.b,new Runnable() {
							public void run() {
								bRunnable.this.run();
							}
						});
					
					bRunnable.this.currentCount += 1;
					if ((bRunnable.this.onlyOnce) || ((bRunnable.this.count != -1)
							&& (bRunnable.this.currentCount >= bRunnable.this.count))) {
						break;
					}
				}
			}
		});
	}

	public void start() {
		this.t.start();
	}

	public void stop() {
		if (this.t.isAlive()) {
			this.stop = true;
			try {
				this.t.interrupt();
			} catch (Exception e) {
			}
			;
		}
	}

	public void setTime(int time) {
		this.ms = time;
	}
	public void setData(String key, Object data) {
		
		this.datas.put(key, data);
	}

	public Object Data(String key) {
		return this.datas.get(key);
	}

	public void clearData() {
		this.datas = null;
		this.datas = new HashMap();
	}

	public abstract void run();
}

package yang.app.qt.black;

import io.qt.core.QByteArray;
import io.qt.core.QPropertyAnimation;
import io.qt.widgets.QWidget;
/**
 * 用以改善动画框架的可靠性，防止其在动画过程中停止
 * @author Administrator
 *
 */
public abstract class animation extends QPropertyAnimation {
	private boolean animationStart;
	public animation(final QWidget widget, final String key, Object startValue, final Object endValue,
			int time) {
		setPropertyName(new QByteArray(key));
		setTargetObject(widget);
		setDuration(time);
		setStartValue(startValue);
		setEndValue(endValue);
		finished.connect(this, "action_setAnimation()");
		animationStart = true;
		start();
//		new bRunnable(time,true,false,true,true) {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				if(animationStart) {
//					widget.setProperty(key, endValue);
//				}
//				action_endAnimation();
//			}
//		};
	}
	public abstract void action_endAnimation();
	void action_setAnimation() {
		animationStart = false;
		action_endAnimation();
	}
}

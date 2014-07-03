package icontroller.checkin;


public class VideoRecorderThread extends Thread{
	int time;
	private VideoControlWrapper control;
	boolean run;
	IUpdateUi li;
	Runnable exit;
	public VideoRecorderThread(int time, VideoControlWrapper control,IUpdateUi li,Runnable exit){
		this.time=time;
		this.control=control;
		this.li=li;
		run=true;
		this.exit=exit;
	}
	public void stop(){
		run=false;
	}
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("CNC.Debug: start record");
		for(int i=1;i<=time;i++){
			if(run){
				try {
					if(li!=null)
						li.onUpdate(new Integer(i));
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.out.println("CNC.Debug: Exception 2:"+e.getMessage());
					e.printStackTrace();
				}
				if(i%10==0&&i!=time){
					control.takeSnapshot();
				}
			}
		}
		exit.run();
	}
}

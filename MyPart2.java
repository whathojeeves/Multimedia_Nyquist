import java.awt.BorderLayout;
import java.util.Date;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class MyPart2 {
	
	public static void main(String[] args)
	{
		if(args.length != 3)
		{
			System.out.println("Please enter the correct number of input arguments");
			return;
		}
		MyPart1.formRotImageBuffer(Integer.parseInt(args[0]));
		renderVideo(512, 512, Integer.parseInt(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
	}
	
	public static void renderVideo(int frameW, int frameH, int numLines, double omega, double fps)
	{
		final JLabel vLabelF = new JLabel();
		MyPart1.vFrame.getContentPane().add(MyPart1.vLabel, BorderLayout.WEST);
		MyPart1.vFrame.getContentPane().add(vLabelF, BorderLayout.EAST);
		MyPart1.vFrame.setVisible(true);
		int numRev = 0;
		int totalNumRevs = 500;
		 
		
		int rTimeMSPerIteration = (int) Math.round((1000/omega)/12);
		long rTimeFMS = (long)Math.round(1000/fps);
		
		new java.util.Timer().scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				vLabelF.setVisible(true);
				try {
				vLabelF.setIcon(new ImageIcon(MyPart1.rImg[MyPart1.numFrame]));
				} catch(Exception e)
				{
					System.out.println("Program ended");
				}
				MyPart1.vFrame.pack();
			}
		},new Date(), (long) rTimeFMS);
			
		for(numRev=0; numRev<totalNumRevs; numRev++)
		{
			for(MyPart1.numFrame=0; MyPart1.numFrame<12; MyPart1.numFrame++)
			{
				MyPart1.vLabel.setVisible(true);
				MyPart1.vLabel.setIcon(new ImageIcon(MyPart1.rImg[MyPart1.numFrame]));
				MyPart1.vFrame.pack();
				try{
					Thread.currentThread();
					Thread.sleep(rTimeMSPerIteration);
				}
				catch(InterruptedException ie){
				}
			}
		}
	}

}

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.lang.Math;
import java.util.Date;
import java.util.TimerTask;

public class MyPart1 {

	public static int width = 512; //Integer.parseInt(args[1]);
	public static int height = 512; //Integer.parseInt(args[2]);
	public static int wPix;
	public static int bPix;
	public static boolean isLineDrawingInProgress = false;
	public static BufferedImage rImg[] = new BufferedImage[120];
	public static int numFrame = 0;
	public static JFrame vFrame = new JFrame();
	public static JLabel vLabel = new JLabel();

	public static void main(String[] args) {
		
		if(args.length != 3)
		{
			System.out.println("Please enter the correct number of input arguments");
			return;
		}
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		//byte a = 0;
		byte r = (byte) 255; //bytes[ind];
		byte g = (byte) 255; //bytes[ind+height*width];
		byte b = (byte) 255; //bytes[ind+height*width*2]; 
		//int pix = ((a << 24) + (r << 16) + (g << 8) + b);
		wPix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);

		r = (byte)0;
		g = (byte)0;
		b = (byte)0;
		bPix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
		
		/* Drawing the lines */
		
		int numLines = Integer.parseInt(args[0]);
	
		img = drawLine(numLines, 0.0);
		JFrame mFrame = new JFrame();
		JLabel mlabel = new JLabel(new ImageIcon(img));
		mFrame.getContentPane().add(mlabel, BorderLayout.CENTER);
		mFrame.pack();
		mFrame.setVisible(true);

		/* Filtering  */

		double scale = Double.parseDouble(args[1]);
		int isFilter = Integer.parseInt(args[2]);
		BufferedImage fImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		if(isFilter == 1)
		{
			fImg=filterImage(img, scale);
		}

		/* Scaling */
		BufferedImage srcImage = (isFilter == 1)?fImg:img;
		BufferedImage scaledImg = scaleImage(srcImage, scale);
		
		JLabel scaledLabel = new JLabel(new ImageIcon(scaledImg));
		mFrame.getContentPane().add(scaledLabel, BorderLayout.EAST);
		mFrame.pack();
		mFrame.setVisible(true);

		/* Video in the videoRenderer */
	}
	
	public static void formRotImageBuffer(int numLines)
	{
		for(int i=0; i<12; i++)
		{
			rImg[i] = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			clearImage(rImg[i], width, height);
			rImg[i] = drawLine(numLines, 30*i);
		}
	}
	
	public static BufferedImage scaleImage(BufferedImage imgToScale, double scale)
	{
		double newWidth = width/scale;
		double newHeight = height/scale;

		int wintPart = (int) (width/newWidth);
		int wfractPart = (int) (width % newWidth);

		int hintPart = (int) (height/newHeight);
		int hfractPart = (int) (height % newHeight);

		BufferedImage scaledImg = new BufferedImage((int) newWidth, (int) newHeight, BufferedImage.TYPE_INT_RGB);

		clearImage(scaledImg, (int)newWidth, (int)newHeight);
		int hpixCount = (int) newHeight;

		int srcX=0,srcY=0;
		int destX=0,destY=0;

		int dw=0, dh=0;

		while(hpixCount-- > 0)
		{
			int wpixCount = ((int) newWidth);
			destX = 0;
			srcX = 0;
			while(wpixCount-- > 0)
			{
				if((destX < (int)newWidth -1 && destY < (int)newHeight - 1) && 
						(srcX < width && srcY <height))
					scaledImg.setRGB(destX,destY,imgToScale.getRGB(srcX, srcY));

				destX++;

				srcX += wintPart;
				dw += wfractPart;

				if(dw >= (int)(newWidth))
				{
					dw -= newWidth;
					srcX++;
				}
			}
			destY++;
			srcY += hintPart;
			dh += hfractPart;
			if(dh >= newHeight)
			{
				dh -= newHeight;
				srcY++;
			}
		}
		return scaledImg;
	}
	
	public static BufferedImage filterImage(BufferedImage imgToFilter, double scale)
	{
		BufferedImage fImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Integer[] pixs = new Integer[(int)((Math.round(scale) * Math.round(scale)))];

		int xval = 0;
		int ct = 0;
		int x=0,y=0;

		for(y=0; y<511-Math.round(scale);y++)
		{
			fImg.setRGB(x, y, imgToFilter.getRGB(x, y));
			for(x=0;x<511-Math.round(scale);x++)
			{
				ct = 0;
				for(int xs=0; xs< Math.round(scale); xs++)
				{
					for(int ys=0; ys<Math.round(scale); ys++)
					{
						pixs[ct++] = imgToFilter.getRGB(x+xs,y+ys);
					}
				}

				xval = pixAverage1(pixs, pixs.length);

				fImg.setRGB(x, y, xval);
			}
		}

		return fImg;

	}
	
	public static BufferedImage drawLine(int numLines, double startAngle)
	{
		int w = width/2;
		int h = height/2;

		int xo = w;
		int yo = h;

		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		//byte a = 0;
		byte r = (byte) 255; //bytes[ind];
		byte g = (byte) 255; //bytes[ind+height*width];
		byte b = (byte) 255; //bytes[ind+height*width*2]; 
		//int pix = ((a << 24) + (r << 16) + (g << 8) + b);
		wPix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);

		r = (byte)0;
		g = (byte)0;
		b = (byte)0;
		bPix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);

		clearImage(img, width, height);

		/* Draw box first */

		for(int x=0;x<511;x++)
		{
			int ys=0,ye=511;

			img.setRGB(x,ys,bPix);
			img.setRGB(x,ye,bPix);
		}

		for(int y=0;y<511;y++)
		{
			int xs=0,xe=511;

			img.setRGB(xs,y,bPix);
			img.setRGB(xe,y,bPix);
		}


		/* Drawing the lines */

		int lineCount = 0;
		double angle = 0.0;
		double step = 360/numLines;
		boolean xZero = false;
		boolean isInfinity = false;
		boolean xneg = true, yneg = true;
		int ylim=0, xlim=0;
		int x=0,y=0;
		int tangent = 0;

		for(angle=0.0+startAngle, lineCount =0; angle<=360+startAngle && lineCount < numLines; angle+=step, lineCount++)
		{
			isInfinity = false;
			if(angle % 90 == 0)
			{
				isInfinity = true;
			}
			
			if(angle >= 360)
			{
				angle = angle % 360;
			}

			if(angle>=0 && angle<90)
			{
				xZero = true;
			}

			if(angle>=90 && angle<180)
			{
				xZero = false;
			}

			if(angle>=180 && angle <270)
			{
				xZero = true;
			}

			if(angle>=270 && angle <360)
			{
				xZero = false;
			}

			if(angle>=0 && angle<=180)
				xneg = false;
			else
				xneg = true;

			if(angle>=90 && angle<=270)
				yneg = false;
			else
				yneg = true;

			xlim = (xneg)?0:width;
			ylim = (yneg)?0:height;

			x=xo;
			y=yo;

			while(x!=xlim && y!=ylim)
			{
				if(!isInfinity)
				{
					tangent = (int) Math.round(((yo-y)*Math.tan(Math.toRadians(angle))));
					x = xo+tangent;
					if(x<=0 || x>=512)
						break;
				}

				img.setRGB(x,y,bPix);

				if(isInfinity)
				{
					if(xZero)
					{
						y = (yneg)?y-1:y+1;
					}
					else
					{
						x = (xneg)?x-1:x+1;
					}	
				}
				else
					y = (yneg)?y-1:y+1;

			}
		}

		return img;
	}

	public static int pixAverage1(Integer[] pixs, int len)
	{
		byte r=0,g=0,b=0;
		
		for(int i=0; i<len; i++)
		{
			r += (byte)((pixs[i] >> 16) & 0xff);
			g += (byte)((pixs[i] >> 8) & 0xff);
			b += (byte)((pixs[i]) & 0xff);
		}
		
		byte avgR = (byte) (r/len);
		byte avgG = (byte) (g/len);
		byte avgB = (byte) (b/len);
		
		return 0xff000000 | ((avgR & 0xff) << 16) | ((avgG & 0xff) << 8) | (avgB & 0xff);
	}	
	
	public static void clearImage(BufferedImage image, int b, int h)
	{
		int x=0,y=0;

		for(y = 0; y < h; y++)
		{
			for(x = 0; x < b; x++)
			{
				image.setRGB(x,y,wPix);
			}
		}
	}

	public static void renderVideo(int frameW, int frameH, double fps, double omega)
	{
		final JLabel vLabelF = new JLabel();
		vFrame.getContentPane().add(vLabel, BorderLayout.WEST);
		vFrame.getContentPane().add(vLabelF, BorderLayout.EAST);
		vFrame.setVisible(true);
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
				vLabelF.setIcon(new ImageIcon(rImg[numFrame]));
				} catch(Exception e)
				{
					System.out.println("Program ended");
				}
				vFrame.pack();
			}
		},new Date(), (long) rTimeFMS);
			
		for(numRev=0; numRev<totalNumRevs; numRev++)
		{
			for(numFrame=0; numFrame<12; numFrame++)
			{
				vLabel.setVisible(true);
				vLabel.setIcon(new ImageIcon(rImg[numFrame]));
				vFrame.pack();
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

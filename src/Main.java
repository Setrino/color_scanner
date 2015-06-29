import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {

	public static void main(String[] args) {

		final JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		frame.addFocusListener(new FocusAdapter() {

			int noOfClick = 0;

			public void focusLost(FocusEvent event) {

				if (noOfClick > 0) {

					int xPos = MouseInfo.getPointerInfo().getLocation().x;
					int yPos = MouseInfo.getPointerInfo().getLocation().y;

					Robot robot = null;
					try {
						robot = new Robot();
					} catch (AWTException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					BufferedImage image = robot
							.createScreenCapture(new Rectangle(new Point(xPos,
									yPos), new Dimension(338, 338)));
					frame.add(new MyPanel(image));
					frame.pack();
					frame.setVisible(true);
					File imageFile = new File(System.getProperty("user.home")
							+ File.separator + "Documents" + File.separator
							+ "tests" + File.separator + "screenshot.png");
					try {
						ImageIO.write(image, "png", imageFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					/* BufferedImage image = null;
							try {
								image = ImageIO.read(new File(System.getProperty("user.home")
										+ File.separator + "Documents" + File.separator
										+ "tests" + File.separator + "screenshot.png"));
							} catch (IOException e) {
							}*/

					int width = image.getWidth();
					int height = image.getHeight();
					String previousX = "";
					int xValue = 0;
					int yValue = 0;
					List<List<String>> listOfLists = new ArrayList<List<String>>();
					List<List<String>> listOfLists2 = new ArrayList<List<String>>();		
					List<String> currentList = null;

					for (int y = 0; y < height; y++) {
						String currentY = "";
						currentList = new ArrayList<String>();
						for (int x = 0; x < width; x++) {
							int color = image.getRGB(x, y);
							String red = Integer.toHexString(
									(color & 0x00ff0000) >> 16).toUpperCase();
							String green = Integer.toHexString(
									(color & 0x0000ff00) >> 8).toUpperCase();
							String blue = Integer.toHexString(
									color & 0x000000ff).toUpperCase();
							String currentPixel = red + green + blue;
							if (!previousX.equals(currentPixel)) {
								currentY += currentPixel + " | ";
								previousX = currentPixel;
								if (!currentPixel.equals("FFFFFF")) {
									currentList.add(currentPixel);
								}
							}
						}
						if (!currentY.contains("FEFEFE")) {
							if(!currentList.isEmpty()){
								listOfLists.add(currentList);
							}
						}
					}
					
					int minSize = listOfLists.get(0).size();
					
					for(int i = 1; i < listOfLists.size(); i++){
						if(minSize > listOfLists.get(i).size()){
							minSize = listOfLists.get(i).size();
						}
					}
										
					for(int i = 1; i < listOfLists.size(); i++){
						if(listOfLists.get(i).size() > minSize){
							listOfLists.remove(i);
						}
					}
					
					double step = Math.ceil(listOfLists.size() / minSize) + 1;
					int listSize = listOfLists.size();
					System.out.println("Size is " + minSize + " out of " + listSize);
					if(listOfLists.get(0).size() > 10){
					}
					for(int i = 1; i < listSize; i+=step){
						listOfLists2.add(listOfLists.get(i));
					}
										
					String tempX = "";
					int tempX_v = 0;
					String tempX2 = "";
					int tempX2_v = 0;
					int tempY = 0;
					for (List<String> listStrings : listOfLists2) {
						System.out.println(listStrings);
						tempX = listStrings.get(0);
						tempX_v = 1;
						for (int j = 1; j < listStrings.size(); j++) {
							if (!tempX2.equals("") && !tempX2.equals(tempX)) {
								// X2 value is the odd one out
								if (!tempX2.equals(listStrings.get(j))) {
									xValue = tempX2_v;
									yValue = tempY;
									// X value is the odd one out
								} else if(!tempX.equals(listStrings.get(j))){
									xValue = tempX_v;
									yValue = tempY + 1;
									break;
								}
							}

							if (!tempX.equals(listStrings.get(j))) {
								if(!tempX2.equals(tempX) && !tempX2.equals("")){
									xValue = tempX_v;	
									yValue = tempY;
									//System.out.println("NA");
								}else if(tempX2.equals(tempX) && !tempX2.equals("")){
									xValue = j + 1;
									yValue = tempY + 1;
								}else if(tempX2.equals("")){
									tempX2 = tempX;
									tempX = listStrings.get(j);
									tempX2_v = tempX_v;
									tempX_v = j + 1;
								}
							}
						}
						tempX2 = tempX;
						tempX2_v = tempX_v;
						tempY++;
					}
					
					System.out.println(xValue + " " + yValue);
					System.out
							.println("--------------------------------------");
				} else {
					noOfClick = 1; 
				}
			};
		});

	}

	private static class MyPanel extends JPanel {
		private static final long serialVersionUID = 3404137174712770214L;
		BufferedImage image = null;

		MyPanel(BufferedImage image) {
			this.image = image;
		};

		@Override
		public void paint(Graphics g) {
			g.drawImage(image, 0, 0, null);
		}

		// so our panel is the correct size when pack() is called on Jframe
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(image.getWidth(), image.getHeight());
		}
	};
}

package UI;
import javax.swing.*;

import Common.Data;
import Common.Info;
import Common.imageInfo;

import java.awt.*;

/*
 * Main Bedroom (10, 175) ---- (300, 30)	40
 * Sub Bedroom (110, 380) ---- (300, 220)	40
 * Sitting Room (350, 30) ---- (580, 260)	40
 * Study (630, 30) ---- (760, 170)	40
 * Balcony (810, 30) ---- (850, 250)	40
 * Bathroom (350, 380) ---- (470, 300)	40
 * Dining room (610, 220) ---- (770, 300)	40
 * Kitchen (730, 380) ---- (850, 300)	40
 */

@SuppressWarnings("serial")
public class FurniturePanel extends JPanel {
	public FurniturePanel() {
		for (int i = 0; i < Data.x_start.length; i++) {
			Data.x[i] = Data.x_start[i];
			Data.y[i] = Data.y_start[i];
		}
	}

	public void paintComponent(Graphics g) {
		Image house = new ImageIcon("data/images/background/House2.png").getImage();
		g.drawImage(house, 0, 0, 900, 486, this);
		
		for(int i = 0; i < Data.imageList.size(); i++) {
			imageInfo tempII = Data.imageList.get(i);
			String path = "data/images/furnitures/" + tempII.orignalName + "/" + tempII.state + ".png";
			Image image = new ImageIcon(path).getImage();
			g.drawImage(image, tempII.location_x, tempII.location_y, 80, 80, this);
			g.setFont(new Font("Courier", Font.BOLD, 13));
			g.setColor(Color.BLUE);
			g.drawString(tempII.furName, tempII.location_x + 5, tempII.location_y + 85);
		}
	}
		
	public void refresh() {
		Data.imageList.clear();
		
//		for(Rule rule: Info.R_Array) {
//			System.out.println("fp:\nsrc name:" + Info.getFurName(rule.srcSN));
//			System.out.println("sig:" + rule.sigTrig);
//			System.out.println("newsig:" + rule.newSigTrig);
//			System.out.println("tri:" + rule.trigger);
//			System.out.println("newtri:" + rule.newTrigger);
//		}
		
		for (int i = 0; i < Data.x_start.length; i++) {
			Data.x[i] = Data.x_start[i];
			Data.y[i] = Data.y_start[i];
		}

		for(int i = 0; i < Info.F_Array.size(); i++) {
			imageInfo imageinfo = new imageInfo();
			imageinfo.orignalName = Info.F_Array.get(i).furname;
			imageinfo.SN = Info.F_Array.get(i).SN;
			imageinfo.furName = Data.getShorterName(imageinfo.orignalName) + "_" + imageinfo.SN;

			int location_index = Info.F_Array.get(i).furnitureInfo.loc_index;
			imageinfo.location_index = location_index;
			imageinfo.location_x = Data.x[location_index];
			imageinfo.location_y = Data.y[location_index];
			imageinfo.initState = Info.F_Array.get(i).initState;
			imageinfo.state = imageinfo.initState;

			Data.imageList.add(imageinfo);

			// 更新坐标
			Data.x[location_index] += Data.interval;
			if (Data.x[location_index] >= Data.x_end[location_index]) {
				Data.x[location_index] = Data.x_start[location_index];
				Data.y[location_index] += Data.interval;
			}
		}
		repaint();
	}
}

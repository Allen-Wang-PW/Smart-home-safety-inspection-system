package UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import Common.Data;
import Common.Info;
import Common.Var;
import Common.animationStateSet;
import Common.animationStateSetList;
import Common.imageInfo;

@SuppressWarnings("serial")
public class DemoFurPanel extends JPanel {
	static int flag = 0;
 	public int annimationCnt = -1; 

	public DemoFurPanel() {
		for (int i = 0; i <Data.x_start.length; i++) {
			Data.x[i] = Data.x_start[i];
			Data.y[i] = Data.y_start[i];
		}
	}

	public void paintComponent(Graphics g) {
		Image house = new ImageIcon("data/images/background/House2.png").getImage();
		g.drawImage(house, 0, 0, this);

		for(int i = 0; i < Data.imageList.size(); i++) {
			imageInfo tempII = Data.imageList.get(i);
			String path = "data/images/furnitures/" + tempII.orignalName + "/" + tempII.state + ".png";
			Image image = new ImageIcon(path).getImage();
			g.drawImage(image, tempII.location_x, tempII.location_y, 80, 80, this);

			g.setFont(new Font("Consolas", Font.BOLD, 14));
			g.setColor(Color.GREEN);
			g.drawString(tempII.furName, tempII.location_x + 2, tempII.location_y + 85);
		}
	}

	public void animationRefresh() {
		System.out.println("size:"+animationStateSetList.actualListSize);
		if(annimationCnt >= animationStateSetList.actualListSize - 1) {
			Data.timer.stop();
			return;
		}
		annimationCnt++;

		animationStateSet info = animationStateSetList.info[annimationCnt];
		
		for(int i = 0; i < Data.imageList.size(); i++) {
			imageInfo tempII = Data.imageList.get(i);
			for(int j = 0; j < animationStateSetList.ElementSize; j++) {
				if(info.element[j].SN == tempII.SN) {
					tempII.state = info.element[j].state;
					//System.out.println(tempII.state);
					break;
				}
			}
		}
		
		for(int i = 0; i < info.element.length; i++) {
			if(info.element[i].state != null) {	// output states information
				info.element[i].name = Info.getFurName(info.element[i].SN);
				Data.demo_text.append(info.element[i].name +": ");
				Data.demo_text.append(info.element[i].state + Data.lineSeparator);
			}
			for(Var var: info.element[i].variable) { // output variables information
				Data.demo_text.append("    " + var.getName() + ": ");
				Data.demo_text.append(var.getValue() + Data.lineSeparator);
				System.out.println(var.getName() + ":" + var.getValue());
			}
		}
		Data.demo_text.append(Data.lineSeparator);
		
		repaint();
	}

	public void reset() {
		annimationCnt = -1;
		for(int i = 0; i < Data.imageList.size(); i++) {
			imageInfo tempII = Data.imageList.get(i);
			tempII.state = Data.imageList.get(i).initState;
		}
		Data.demo_text.setText("");
		repaint();
	}
	
	public void addDeviceRefresh() {
		Data.imageList.clear();

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

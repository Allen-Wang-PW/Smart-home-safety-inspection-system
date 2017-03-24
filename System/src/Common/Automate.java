package Common;

import java.io.PrintWriter;
import java.util.ArrayList;

public class Automate {
	public String name;
	public boolean signal = false;
	public boolean hasSensor = false;
	public String variable;
	
	public ArrayList<Location> locArr = new ArrayList<Location>();
	public ArrayList<Convert> conArr = new ArrayList<Convert>();
	
	// print current automation
	public void print(PrintWriter pw) {
		pw.println("automaton " + this.name);
		pw.print("var:");
		if(this.hasSensor && this.signal)
			pw.println(this.variable + ",signal_t;");
		else if(this.hasSensor)
			pw.println(this.variable + ";");
		else
			pw.println("signal_t;");
		
		pw.print("synclabs:");
		int conSize = this.conArr.size();
		for(int i = 0; i < conSize; ++i) {
			if(i < conSize - 1)
				pw.print(conArr.get(i).label + ",");
			else
				pw.print(conArr.get(i).label + ";");
		}
		pw.println();
		
		for(Location loc : this.locArr) {
			pw.println("loc " + loc.name + ":while " + loc.invariant);
			pw.println("\twait {" + loc.dynamic + "}");
			for(Convert con : this.conArr) {
				if(con.from.equals(loc.name)) {
					pw.print("\twhen " + con.condition + " sync " + con.label + " ");
					if(con.asg != null)
						pw.print("do {" + con.asg + "} ");
					pw.println("goto " + con.to + ";");
				}
			}
		}
		pw.println("initially: S0;");
		pw.println("end");
		pw.println();
	}
}
package model;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Common.FsmSpec;
import Common.FurListObj;
import Common.Furniture;
import Common.Info;
import Common.LhaSpec;
import Common.Relation;
import Common.Rule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Io {
	static boolean Debug = false;

	static void readFromFile(String filename) {
		try  {
			String fname = "data/records/" + filename;
			File inputFile = new File(fname);
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        Document doc = dBuilder.parse(inputFile);
	        doc.getDocumentElement().normalize();
	        if(Debug)
	        	System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
	        
	        // initial furnitures from file
	        NodeList nList = doc.getElementsByTagName("furniture");
	        for (int i = 0; i < nList.getLength(); ++i) {
	            Node nNode = nList.item(i);
	            Element eElement = (Element) nNode;
	            String path = eElement.getElementsByTagName("jsonPath").item(0).getTextContent();
	            int SN = Integer.parseInt(eElement.getElementsByTagName("SN").item(0).getTextContent());
	            int loc_x = Integer.parseInt(eElement.getElementsByTagName("loc_x").item(0).getTextContent());
	            int loc_y = Integer.parseInt(eElement.getElementsByTagName("loc_y").item(0).getTextContent());
	            int room = Integer.parseInt(eElement.getElementsByTagName("room").item(0).getTextContent());
	            Furniture fur = jsonParser.JsonParserObject(path, loc_x, loc_y);
	            fur.SN = SN;
	            fur.furnitureInfo.loc_index = room;
	            
	            if(Debug) {
	            	System.out.println("Furname: " + eElement.getElementsByTagName("name").item(0).getTextContent());
	            	System.out.println("Furname: " + SN);
	            	System.out.println("Furname: " + path);
	            }
	            ChangeInfo.setSN(SN);
	        } 
	        
	        // initial Fsm specifications from file
	        NodeList sList = doc.getElementsByTagName("fsmSpec");
	        for(int i = 0; i < sList.getLength(); ++i) {
	        	Node sNode = sList.item(i);
	        	Element eElement = (Element) sNode;
	        	
	        	String str = eElement.getElementsByTagName("str").item(0).getTextContent();
	        	String spe = eElement.getElementsByTagName("spe").item(0).getTextContent();
	        	ChangeInfo.addSpecFsm(str, spe);
	        	if(Debug)
	        		System.out.println("Fsm: " + str);
	        }
	        
	        // initial Lha specifications from file
	        NodeList hList = doc.getElementsByTagName("lhaSpec");
	        for(int i = 0; i < hList.getLength(); ++i) {
	        	Node hNode = hList.item(i);
	        	Element eElement = (Element) hNode;
	        	
	        	String name = eElement.getElementsByTagName("name").item(0).getTextContent();
	        	String state = eElement.getElementsByTagName("state").item(0).getTextContent();
	        	String varCondition = eElement.getElementsByTagName("varCondition").item(0).getTextContent();
	        	if(varCondition == "")
	        		varCondition = null;
	        	ChangeInfo.addSpecLha(name, state, varCondition);
	        	if(Debug)
	        		System.out.println("Lha: " + name + " " + state + " " + varCondition);
	        }
	        
	        // initial rules from file
	        NodeList rList = doc.getElementsByTagName("rule");
	        for(int i = 0; i < rList.getLength(); ++i) {
	        	Node rNode = rList.item(i);
	        	Element eElement = (Element) rNode;
	        	
	        	String srcSN = eElement.getElementsByTagName("triggerDevice").item(0).getTextContent();
	        	String dstSN = eElement.getElementsByTagName("targetDevice").item(0).getTextContent();
	        	String action = eElement.getElementsByTagName("targetAPI").item(0).getTextContent();
	        	
	        	String srcTri = eElement.getElementsByTagName("triggerAPI").item(0).getTextContent();
	        	if(srcTri.length() > 0) {
	        		ChangeInfo.addRule(Integer.parseInt(srcSN), srcTri, Integer.parseInt(dstSN), action);
	        		if(Debug)
	        			System.out.println(srcSN + " " + srcTri + " " + dstSN + " " + action);
	        	}
	        	else {
	        		String var = eElement.getElementsByTagName("sensorName").item(0).getTextContent();
	        		String rel = eElement.getElementsByTagName("relation").item(0).getTextContent();
	        		String value = eElement.getElementsByTagName("result").item(0).getTextContent();
	        		ChangeInfo.addRule(Integer.parseInt(srcSN), var, Relation.getRel(rel), Integer.parseInt(value), Integer.parseInt(dstSN), action);
	        		if(Debug)
	        			System.out.println(srcSN + " " + var + " " + rel + " " + value + " " + dstSN + " " + action);
	        	}
	        }
	        
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}
	
	static void createFile(String filename) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			
			// root element
			Element rootElement = doc.createElement("TestCase");
			doc.appendChild(rootElement);
			
			// device element
	        Element devices = doc.createElement("Devices");
	        rootElement.appendChild(devices);

	        for(Furniture tmp : Info.F_Array) {
	        	Element fur = doc.createElement("furniture");
	        	devices.appendChild(fur);
	        	
	        	Element name = doc.createElement("name");
	            name.appendChild(doc.createTextNode(tmp.furname));
	            fur.appendChild(name);
	            
	            Element SN = doc.createElement("SN");
	            SN.appendChild(doc.createTextNode(String.valueOf(tmp.SN)));
	            fur.appendChild(SN);
	            
	            Element jsonPath = doc.createElement("jsonPath");
	            jsonPath.appendChild(doc.createTextNode(tmp.furnitureInfo.path));
	            fur.appendChild(jsonPath);
	            
	            Element loc_x = doc.createElement("loc_x");
	            loc_x.appendChild(doc.createTextNode(String.valueOf(tmp.furnitureInfo.loc_x)));
	            fur.appendChild(loc_x);
	            
	            Element loc_y = doc.createElement("loc_y");
	            loc_y.appendChild(doc.createTextNode(String.valueOf(tmp.furnitureInfo.loc_y)));
	            fur.appendChild(loc_y);
	            
	            Element room = doc.createElement("room");
	            room.appendChild(doc.createTextNode(String.valueOf(tmp.furnitureInfo.loc_index)));
	            fur.appendChild(room);
	        }
	        
	        // rule element
	        Element rules = doc.createElement("Rules");
	        rootElement.appendChild(rules);
	        
	        for(Rule tmp : Info.R_Array) {
	        	Element rule = doc.createElement("rule");
	        	rules.appendChild(rule);
	        	
	        	Element triggerDevice = doc.createElement("triggerDevice");
	        	triggerDevice.appendChild(doc.createTextNode(String.valueOf(tmp.srcSN)));
	        	rule.appendChild(triggerDevice);
	        	
	        	if(tmp.sigTrig != null) {
	        		Element triggerAPI = doc.createElement("triggerAPI");
		        	triggerAPI.appendChild(doc.createTextNode(tmp.sigTrig));
		        	rule.appendChild(triggerAPI);
		        	
		        	Element sensorName = doc.createElement("sensorName");
		        	sensorName.appendChild(doc.createTextNode(""));
		        	rule.appendChild(sensorName);
		        	
		        	Element relation = doc.createElement("relation");
		        	relation.appendChild(doc.createTextNode(""));
		        	rule.appendChild(relation);
		        	
		        	Element result = doc.createElement("result");
		        	result.appendChild(doc.createTextNode(""));
		        	rule.appendChild(result);
	        	}
	        	else {
	        		Element triggerAPI = doc.createElement("triggerAPI");
		        	triggerAPI.appendChild(doc.createTextNode(""));
		        	rule.appendChild(triggerAPI);
		        	
		        	Element sensorName = doc.createElement("sensorName");
		        	sensorName.appendChild(doc.createTextNode(tmp.trigger.var));
		        	rule.appendChild(sensorName);
		        	
		        	Element relation = doc.createElement("relation");
		        	relation.appendChild(doc.createTextNode(tmp.trigger.rel.getStr_f()));
		        	rule.appendChild(relation);
		        	
		        	Element result = doc.createElement("result");
		        	result.appendChild(doc.createTextNode(tmp.trigger.value));
		        	rule.appendChild(result);
	        	}
	        	
	        	Element targetDevice = doc.createElement("targetDevice");
	        	targetDevice.appendChild(doc.createTextNode(String.valueOf(tmp.dstSN)));
	        	rule.appendChild(targetDevice);
	        	
	        	Element targetAPI = doc.createElement("targetAPI");
	        	targetAPI.appendChild(doc.createTextNode(tmp.dstAct));
	        	rule.appendChild(targetAPI);
	        }
	        
	        // Fsm spec element
	        Element fsmSpecs = doc.createElement("FSMSpecs");
	        rootElement.appendChild(fsmSpecs);
	        
	        for(FsmSpec tmp : Info.FsmArray) {
	        	Element spec = doc.createElement("fsmSpec");
	        	fsmSpecs.appendChild(spec);
	        	
	        	Element str = doc.createElement("str");
	        	str.appendChild(doc.createTextNode(tmp.str));
	        	spec.appendChild(str);
	        	
	        	Element spe = doc.createElement("spe");
	        	spe.appendChild(doc.createTextNode(tmp.spe));
	        	spec.appendChild(spe);
	        }
	        
	        // Lha spec element
	        Element lhaSpecs = doc.createElement("LHASpecs");
	        rootElement.appendChild(lhaSpecs);
	        
	        for(LhaSpec tmp : Info.LhaArray) {
	        	if(tmp.getName().contains("Trigger")) {
	        		System.out.println(tmp.getName());
	        		continue;
	        	}
	        		
	        	Element spec = doc.createElement("lhaSpec");
	        	lhaSpecs.appendChild(spec);
	        	
	        	Element name = doc.createElement("name");
	        	name.appendChild(doc.createTextNode(tmp.getName()));
	        	spec.appendChild(name);
	        	
	        	Element state = doc.createElement("state");
	        	state.appendChild(doc.createTextNode(tmp.getState()));
	        	spec.appendChild(state);
	        	
	        	if(tmp.getVarCondition() != null)
	        	{
		        	Element varCondition = doc.createElement("varCondition");
		        	varCondition.appendChild(doc.createTextNode(tmp.getVarCondition()));
		        	spec.appendChild(varCondition);
	        	}
	        	else
	        	{
	        		Element varCondition = doc.createElement("varCondition");
	        		varCondition.appendChild(doc.createTextNode(""));
	        		spec.appendChild(varCondition);
	        	}
	        }
	        
	        // write the content into xml file
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        DOMSource source = new DOMSource(doc);
	        String name = "data/records/" + filename + ".xml";
	        StreamResult result = new StreamResult(new File(name));
	        transformer.transform(source, result);	        
		} catch (ParserConfigurationException | TransformerException e) {
			e.printStackTrace();
		}
	}
	
	public static void initDevice() {
		try {
			File inputFile = new File("data/list.xml");
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			
	        Document doc = dBuilder.parse(inputFile);
	        doc.getDocumentElement().normalize();
	         
	        // initial furnitures-list from file
	        NodeList nList = doc.getElementsByTagName("furniture");
	        for (int i = 0; i < nList.getLength(); ++i) {
	            Node nNode = nList.item(i);
	            Element eElement = (Element) nNode;
	            String name = eElement.getElementsByTagName("name").item(0).getTextContent();
	            String path = eElement.getElementsByTagName("jsonPath").item(0).getTextContent();
	            FurListObj fur = new FurListObj();
	            fur.name = name;
	            fur.jsonpath = path;
	            Info.FurList.add(fur);

	            if(Debug)
		        	System.out.println(fur.name + " " + fur.jsonpath);
	        }
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void initFromFile(String filename) {
		readFromFile(filename);
	}
	
	public static void saveIntoFile(String filename) {
		createFile(filename);
	}
}
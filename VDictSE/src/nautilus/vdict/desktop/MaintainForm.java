package nautilus.vdict.desktop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import nautilus.vdict.data.Adjective;
import nautilus.vdict.data.Adverb;
import nautilus.vdict.data.Idiom;
import nautilus.vdict.data.Noun;
import nautilus.vdict.data.PartOfSpeech;
import nautilus.vdict.data.VDictionary;
import nautilus.vdict.data.Verb;
import nautilus.vdict.data.WordData;
import nautilus.vdict.data.WordIndex;
import nautilus.vdict.data.WordMean;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MaintainForm {
	private Shell shell = null;
	private Button btnImport, btnBrowse;
	private Text txtFile;
	
	private VDictionary dictionary;
	
	public MaintainForm(Shell parent, VDictionary dict)	{
		dictionary = dict;
		shell = new Shell(parent);
		shell.setSize(350, 200);
		initComponents();
		shell.open();
	}
	
	private void initComponents() {
		FormLayout layout = new FormLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		shell.setText("Vdict - Desktop verion 1.0");
		shell.setLayout(layout);
		
		FormData fdEdit = new FormData();
		Label lbFile = new Label(shell, SWT.NONE);
		fdEdit.left = new FormAttachment(0, 5);
		fdEdit.top = new FormAttachment(0, 10);
		lbFile.setText("Data file: ");
		lbFile.setLayoutData(fdEdit);
		
		fdEdit = new FormData();
		txtFile = new Text(shell, SWT.BORDER);
		fdEdit.left = new FormAttachment(lbFile, 10, SWT.RIGHT);
		fdEdit.top = new FormAttachment(0, 10);
		fdEdit.width = 170;
		txtFile.setLayoutData(fdEdit);
		//
		txtFile.setText("D:\\projects\\nautilus-dictionary\\vdict\\data\\words.xml");
		
		//btnBrowse
		btnBrowse = new Button(shell, SWT.PUSH);
		fdEdit = new FormData();
		fdEdit.top = new FormAttachment(0, 10);
		fdEdit.left = new FormAttachment(txtFile, 10, SWT.RIGHT);
		btnBrowse.setText("...");
		btnBrowse.setLayoutData(fdEdit);
		
		//btnImport
		btnImport = new Button(shell, SWT.PUSH);
		fdEdit = new FormData();
		fdEdit.top = new FormAttachment(txtFile, 10, SWT.BOTTOM);
		fdEdit.left = new FormAttachment(txtFile, 0, SWT.LEFT);
		btnImport.setText("Import");
		btnImport.setLayoutData(fdEdit);
		
		btnImport.addListener(SWT.Selection, new Listener(){

			@Override
			public void handleEvent(Event arg0) {
				// TODO Auto-generated method stub
				String filepath = txtFile.getText();
				readXMLFile(filepath);
			}
			
		});

	}
	
	/**
	 * This method read words from xml file using DOM
	 * */
	private void readXMLFile(String filepath) {
		int i;
		WordData word;
		PartOfSpeech part = null;
		try {
			File fXmlFile = new File(filepath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			//Get root element
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("word");
			
			for(i=0; i < nList.getLength(); i++) {
				Element w = (Element)nList.item(i);
				word = new WordData();
				
				//I don't want to check null here, 
				//text is a mandatory field of a word
				String wordText = getFirsChild("text", w).getTextContent();
				word.setWordString(wordText);
				NodeList parts = w.getElementsByTagName("partOfSpeed");
				for(int j=0; j<parts.getLength(); j++) {
					Element partEl = (Element)parts.item(j);
					byte pCode = Byte.parseByte(partEl.getAttribute("code"));
					String pronounce = getFirsChild("pronounce", partEl).getTextContent();
					
					switch(pCode) {
						case 0:
							part = new Noun();
							Node pluralEl = getFirsChild("plural", partEl);
							if(pluralEl != null) {
								String plural = pluralEl.getTextContent();
								((Noun)part).setPluralForm(plural);
							}
							break;
							
						case 1:
						case 2:
							part = new Verb();
							break;
							
						case 3:
							part = new Adverb();
							break;
							
						case 4:
							part = new Adjective();
							break;
					}

					part.setPronunciation(pronounce);
					
					//Get mean
					NodeList means = partEl.getElementsByTagName("mean");
					for(int k=0; k<means.getLength(); k++) {
						Element meanEl = (Element)means.item(k);
						String meanText = getFirsChild("text", meanEl).getTextContent();
						Node exampleNode = getFirsChild("example", meanEl);
						String example = null;
						String meanExample = null;
						if(exampleNode != null) {
							example = exampleNode.getTextContent();
						}
						
						Node exampleMeanNode = getFirsChild("exampleMean", meanEl);
						if(exampleMeanNode != null) {
							meanExample = exampleMeanNode.getTextContent();
						}
						
						WordMean mean = new WordMean(meanText, example, null, (byte)0);
						part.addMean(mean);
					}
					
					word.addPart(part);
				}
				
				WordIndex index = dictionary.findIndex(wordText);
				if(index == null) {
					index = new WordIndex();
					index.setWord(wordText);
				} else {
					WordData word1;
					if(index.getAddress() >= 0){
						word1 = dictionary.readWord(index.getAddress());
						if(word1.getLength() < word.getLength()) {
							dictionary.markAsDeleted(word1);
							index.setAddress(-1);
						}
					}
				}
				
				word.setIndex(index);
				dictionary.addWord2HashTable(word);
				
				dictionary.writeWord(word);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private List<Node> getChilds(String name, Element el){
		List<Node> result = new ArrayList<Node>();
		Node first = el.getFirstChild();
		
		while( first != null) {
			if(first.getNodeName().equals(name)) {
				result.add(first);
			}
			first = first.getNextSibling();
		}
		
		return result;
	}
	
	private Node getFirsChild(String name, Element el){
		Node first = el.getFirstChild();
		
		while( (first != null) && (!first.getNodeName().equals(name)) ) {
			first = first.getNextSibling();
		}
		
		if( (first != null) && (!first.getNodeName().equals(name)) )
			return null;
		
		return first;
	}
}

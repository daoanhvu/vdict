package nautilus.vdict.desktop;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLDecoder;

import java.util.Properties;
import java.util.ArrayList;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import nautilus.vdict.data.PartOfSpeech;
import nautilus.vdict.data.VDictionary;
import nautilus.vdict.data.WordData;
import nautilus.vdict.data.WordIndex;
import nautilus.vdict.data.WordMean;

public class MainFormSWT {
	private static final int WIDTH_PERCENT = 40;
	private static final String TEMPLATE_PATH = "vdict.vm";
	private static String executionPath;
	
	private Display display = new Display();
	private Shell shell = new Shell(display);
	
	private Group groupList;
	private Browser browser;
	private Text txtSearch;
	private List lstWords;
	private Menu menuBar, fileMenu, helpMenu;
	private MenuItem fileMenuHeader, fileSaveIndex, helpMenuHeader;
	private MenuItem fileImport, mniTestData, overwriteIndex;
	private MenuItem fileExitItem, showEditItem, helpGetHelpItem;
	private ModifyListener modifyListener = null;
	
	//Template engine, in this case, I use Velocity in separate instances mode
	private VelocityEngine velocityEngine = null;
	private Template velocitytemplate;
//	private InputStreamReader templateReader = null;
	
	private VDictionary dictionary;
	
	//Event handler for menuitems
	class ShowEditFormItemListener implements SelectionListener	{
        public void widgetSelected(SelectionEvent event) {
//        	display.//
        	EditWordForm editForm = new EditWordForm(dictionary, shell);
        }
        
        public void widgetDefaultSelected(SelectionEvent event) {
        
        }
    }//end class ShowEditFormItemListener
	
	class SaveIndexItemListener implements SelectionListener {
		public void widgetSelected(SelectionEvent event) {
        	dictionary.overwriteIndex();
        }
        
        public void widgetDefaultSelected(SelectionEvent event) {
        
        }
    }//end class ShowEditFormItemListener
	/////////////////////////////
	public MainFormSWT() {
		//char c = 'ɳ';
		initComponent();

        loadData();
		
		shell.setMaximized(true);
		shell.open();
		while(!shell.isDisposed()){
			if(!display.readAndDispatch())
				display.sleep();
		}
		
		display.dispose();
	}
	
	private void initComponent() {
		FormLayout layout = new FormLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		shell.setText("Vdict - Desktop verion 1.0");
		shell.setLayout(layout);
		
		preInitListPane();
		
		browser = new Browser(shell,  SWT.BORDER);
		FormData formData = new FormData();
		formData.top = new FormAttachment(groupList, 0, SWT.TOP);
		formData.left = new FormAttachment(groupList, 5, SWT.FILL);
		formData.right = new FormAttachment(100, 0);
		formData.bottom = new FormAttachment(100, 0);
		browser.setLayoutData(formData);
		
		//Menu Bar
		menuBar = new Menu(shell, SWT.BAR);
	    fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
	    fileMenuHeader.setText("&File");
	    
	    fileMenu = new Menu(shell, SWT.DROP_DOWN);
	    fileMenuHeader.setMenu(fileMenu);
	    
	    showEditItem = new MenuItem(fileMenu, SWT.PUSH);
	    showEditItem.setText("&Edit Words");
	    
	    fileSaveIndex = new MenuItem(fileMenu, SWT.PUSH);
	    fileSaveIndex.setText("&Save Index");

        mniTestData = new MenuItem(fileMenu, SWT.PUSH);
        mniTestData.setText("&Test Data");
        mniTestData.addSelectionListener(new SelectionListener(){

            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                TestDataForm editForm = new TestDataForm(shell);
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent selectionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
	    
	    fileImport = new MenuItem(fileMenu, SWT.PUSH);
	    fileImport.setText("&Import...");
	    
	    showEditItem.addSelectionListener(new ShowEditFormItemListener());
	    fileSaveIndex.addSelectionListener(new SaveIndexItemListener());
	    fileImport.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				MaintainForm editForm = new MaintainForm(shell, dictionary);
			}
	    	
	    });
	    
	    overwriteIndex = new MenuItem(fileMenu, SWT.PUSH);
	    overwriteIndex.setText("&Save indices");
	    overwriteIndex.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				dictionary.overwriteIndex();
			}
	    	
	    });

	    
	    shell.setMenuBar(menuBar);
	    
	    addActionListener();
	}
	
	private void preInitListPane()	{
		FormData fdList = new FormData();
		fdList.width = 200;
		fdList.left = new FormAttachment(0,5);
		fdList.top = new FormAttachment(0, 5);
		fdList.bottom = new FormAttachment(100, 0);
		groupList = new Group(shell, SWT.NONE);
		groupList.setLayout(new FormLayout());
		groupList.setText("List of words");
		groupList.setLayoutData(fdList);
		
		txtSearch = new Text(groupList,  SWT.BORDER);
		fdList = new FormData();
		fdList.top = new FormAttachment(0, 5);
		fdList.left = new FormAttachment(0, 5);
		fdList.right = new FormAttachment(100, -5);
		txtSearch.setLayoutData(fdList);
		
		lstWords = new List(groupList,  SWT.BORDER);
		fdList = new FormData();
		fdList.top = new FormAttachment(txtSearch, 5, SWT.BOTTOM);
		fdList.left = new FormAttachment(0, 5);
		fdList.right = new FormAttachment(100, -5);
		fdList.bottom = new FormAttachment(100, -5);
		lstWords.setLayoutData(fdList);
	}

    private void loadData() {
        velocityEngine = new VelocityEngine();
        velocityEngine.addProperty("resource.loader", "file");
        velocityEngine.addProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocityEngine.addProperty("file.resource.loader.path", "D:/projects/workspace/vdictse/src/");
        velocityEngine.addProperty("file.resource.loader.cache", "false");
        velocityEngine.addProperty("file.resource.loader.modificationCheckInterval", "0");
        velocityEngine.init();
        velocitytemplate = velocityEngine.getTemplate(TEMPLATE_PATH);
        dictionary = new VDictionary("English - Vietnamese", "D:/projects/nautilus-dictionary/vdict/data/vdict.data", 
        		"D:/projects/nautilus-dictionary/vdict/data/vdict.idx");
        try {
            dictionary.loadIndex();
            
            //For testing purpose
            dictionary.testByteOrder();
            
        } catch (IOException e) {
        	System.out.println("There is no data at the specified location.");
            //e.printStackTrace();
        }
    }
	
	private void addActionListener() {
		this.modifyListener = new ModifyListener(){
			public void modifyText(ModifyEvent e) {
				String s = txtSearch.getText();
				lstWords.removeAll();
				
				if( s.trim().length() > 0 )	{
					ArrayList<WordIndex> lst = dictionary.initListWord(s);
					for(int i=0;i<lst.size();i++)
						lstWords.add(lst.get(i).getWord());
				}				
			}
		};
		
		FocusListener focusListener = new FocusListener(){
			public void focusGained(FocusEvent e) {
				txtSearch.addModifyListener(modifyListener);
			}
			
			public void focusLost(FocusEvent e)	{
				txtSearch.removeModifyListener(modifyListener);
			}
		};
		txtSearch.addFocusListener(focusListener);
		
		txtSearch.addListener(SWT.Traverse, new Listener(){
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				String strHtml = "";
				if(event.detail == SWT.TRAVERSE_RETURN)	{
					String word = txtSearch.getText().trim();
					showWord(word);
				}
			}
			 
		});
		
		txtSearch.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.ARROW_DOWN)	{
					//System.out.println("[MAINFORMSWT] txtsearch arrow down ");
					lstWords.setSelection(0);
					lstWords.setFocus();
				} else {
					String text = txtSearch.getText().trim();
					if(!"".equals(text)) {
						java.util.List<WordIndex> results = dictionary.initListWord(text);
						for(WordIndex index: results) {
							lstWords.add(index.getWord());
						}
					}
				}
			}
		});
		
		lstWords.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent e){
				List lst = (List)e.getSource();
				int idx = lst.getSelectionIndex();
				txtSearch.setText(lst.getItem(idx));
			}
			
			public void widgetDefaultSelected(SelectionEvent e)	{
				List lst = (List)e.getSource();
				int idx = lst.getSelectionIndex();
				txtSearch.setText(lst.getItem(idx));
			}
		});
		
		lstWords.addListener(SWT.Traverse, new Listener(){
			public void handleEvent(Event e) {
				if(e.detail == SWT.TRAVERSE_RETURN)	{
					showWord(lstWords.getItem(lstWords.getSelectionIndex()));
				}
			}
		});
		
		
		shell.addListener(SWT.Close, new Listener() {
		      public void handleEvent(Event event) {
		        //dictionary.overwriteIndex();
		    	  
//		    	  if(templateReader != null) {
//					try {
//						templateReader.close();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//		    	  }
		      }
		    });
		
		//System.out.println(this.getClass().getClassLoader().getResource("speaker.jpeg"));
	}
	
	private void showWord(String sWord) {
		if(sWord.length() <= 0)
			return;
		
		String strHtml;
		WordIndex index = dictionary.findIndex(sWord);
		if(index != null) {
            try{
                WordData wd = dictionary.readWord(index.getAddress());
                //System.out.println(String.format("[MAINFORMSWT] Address Of %s: %d -> word: %s",sWord, index.getAddress(), wd));
                if(wd != null) {
                    wd.setIndex(index);
                    strHtml = formatHtml(wd);
                    browser.setText(strHtml);
                }
            } catch(IOException ie) {
                ie.printStackTrace();
            }
		}
	}
	
	private String formatHtml(WordData word) {
		VelocityContext context = new VelocityContext();
		String html = "";
		try {
			//String tempFilename = File.createTempFile("vdict", ".html").getAbsolutePath();
			StringWriter writer = new StringWriter();
			//velocityEngine.evaluate(context, writer, TEMPLATE_PATH, templateReader);
			context.put("speakerImagePath", this.getClass().getClassLoader().getResource("speaker.jpeg"));
			context.put("word", word.getIndex().getWord());
			context.put("partsOfSpeeds", word.getParts());
			context.put("relativeWords", word.getRelativeWord());
			velocitytemplate.merge(context, writer);
			//writer.flush();
			html = writer.toString();
			writer.close();
			return html;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "<html><h1>ERROR</h1></html>";
		}
	}
	
	public static String getExecutionPath()	{
		if(executionPath==null || executionPath.trim().equals("")) {
			String path = MainFormSWT.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			File f = new File(path);
			executionPath = f.getParentFile().getPath();
		}
		return executionPath;
	}
	
	public static void main(String[] args) {
		MainFormSWT app = new MainFormSWT();
	}
}

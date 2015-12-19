package nautilus.vdict.desktop;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Event;

import nautilus.vdict.data.Adjective;
import nautilus.vdict.data.Adverb;
import nautilus.vdict.data.Article;
import nautilus.vdict.data.Noun;
import nautilus.vdict.data.PartOfSpeech;
import nautilus.vdict.data.Preposition;
import nautilus.vdict.data.VDictionary;
import nautilus.vdict.data.Verb;
import nautilus.vdict.data.WordData;
import nautilus.vdict.data.WordIndex;

import java.io.IOException;

public class EditWordForm
{
	Shell shell = null; //new Shell(display);
	
	private VDictionary dictionary;
	private Group groupEdit;
	private Button btnSave, btnAddPart, btnRemovePart, btnEditPart;
	private Text txtWord, txtPronunciation, txtRelativeWord;
	private Text txtDescription;
	private Table tbListParts;
	private Combo cboPartOfSpeed;
	
	private Listener dynamicAddMeanButtonListener;
	private Listener dynamicAddIdiomButtonListener;
	
	private WordData word = null;
	private PartOfSpeech updatingPart = null;
	
	public EditWordForm(VDictionary dict, Shell parent)
	{
		dictionary = dict;
		shell = new Shell(parent);
		shell.setMaximized(true);
		
		initComponents();
		shell.open();
	}
	
	public EditWordForm(VDictionary dict, Shell parent, WordIndex wordIndex)
	{
		dictionary = dict;
		shell = new Shell(parent);
		initComponents();
		shell.open();
	}
	
	private void initComponents()
	{
		FormLayout layout = new FormLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		shell.setSize(1000, 600);
		shell.setText("Vdict - Desktop verion 1.0");
		shell.setLayout(layout);
		
		groupEdit = new Group(shell, SWT.NONE);
		groupEdit.setLayout(new FormLayout());
		
		//Declare controls
		FormData fdEdit = new FormData();
		fdEdit.left = new FormAttachment(0, 5);
		fdEdit.top = new FormAttachment(0, 5);
		fdEdit.right = new FormAttachment(100, 5);
		fdEdit.bottom = new FormAttachment(100, -45);
		groupEdit.setText("Edit word");
		groupEdit.setLayoutData(fdEdit);
		
		Label lbWord = new Label(groupEdit, SWT.NONE);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(0, 5);
		fdEdit.top = new FormAttachment(0, 10);
		lbWord.setText("Word");
		lbWord.setLayoutData(fdEdit);
		
		txtWord = new Text(groupEdit, SWT.BORDER);
		fdEdit = new FormData();
		fdEdit.width = 200;
		fdEdit.left = new FormAttachment(lbWord, 5, SWT.RIGHT);
		fdEdit.bottom = new FormAttachment(lbWord, 0, SWT.BOTTOM);
		txtWord.setLayoutData(fdEdit);
		
		Label lbRelativeWords = new Label(groupEdit, SWT.NONE);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(txtWord, 5, SWT.RIGHT);
		fdEdit.top = new FormAttachment(lbWord, 0, SWT.TOP); 
		lbRelativeWords.setText("Relative Words ");
		lbRelativeWords.setLayoutData(fdEdit);
		
		txtRelativeWord = new Text(groupEdit, SWT.BORDER);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(lbRelativeWords, 5, SWT.RIGHT);
		fdEdit.top = new FormAttachment(txtWord, 0, SWT.TOP);
		fdEdit.right = new FormAttachment(100, -5);
		//fdEdit.bottom = new FormAttachment(100, -5);
		txtRelativeWord.setLayoutData(fdEdit);
		
		//cboPartOfSpeed
		fdEdit = new FormData();
		Label lbPart = new Label(groupEdit, SWT.NONE);
		lbPart.setText("Part of speed");
		fdEdit.left = new FormAttachment(lbWord, 0, SWT.LEFT);
		fdEdit.top = new FormAttachment(txtRelativeWord, 10, SWT.BOTTOM);
		lbPart.setLayoutData(fdEdit);
		//System.out.println("[EDITWORDFORM] Chay den day");
		//cboPartOfSpeed
		cboPartOfSpeed = new Combo(groupEdit, SWT.DROP_DOWN | SWT.READ_ONLY);
		String[] parts = PartOfSpeech.PartName.values().toArray(new String[0]);
		cboPartOfSpeed.setItems(parts);
		
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(lbPart, 5, SWT.RIGHT);
		fdEdit.top = new FormAttachment(txtRelativeWord, 10, SWT.BOTTOM);
		fdEdit.width = 100;
		//fdEdit.right = new FormAttachment(txtWord, 0, SWT.RIGHT);
		//fdEdit.bottom = new FormAttachment(lbPart, 5, SWT.BOTTOM);
		cboPartOfSpeed.setLayoutData(fdEdit);
		
		//Label pronunciation
		fdEdit = new FormData();
		Label lbPronunciation = new Label(groupEdit, SWT.NONE);
		lbPronunciation.setText("Pronunciation ");
		fdEdit.left = new FormAttachment(cboPartOfSpeed, 5, SWT.RIGHT);
		fdEdit.top = new FormAttachment(lbPart, 0, SWT.TOP);
		lbPronunciation.setLayoutData(fdEdit);
						
		//txtPronunciation
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(lbPronunciation, 5, SWT.RIGHT);
		fdEdit.top = new FormAttachment(cboPartOfSpeed, 0, SWT.TOP);
		fdEdit.right = new FormAttachment(100, -5);
		//fdEdit.bottom = new FormAttachment(lbPart, 5, SWT.BOTTOM);
		txtPronunciation = new Text(groupEdit, SWT.BORDER);
		txtPronunciation.setLayoutData(fdEdit);
		
		//Label description
		fdEdit = new FormData();
		Label lbDescription = new Label(groupEdit, SWT.NONE);
		lbDescription.setText("Description ");
		fdEdit.left = new FormAttachment(lbPart, 0, SWT.LEFT);
		fdEdit.top = new FormAttachment(txtPronunciation, 10, SWT.BOTTOM);
		lbDescription.setLayoutData(fdEdit);
		
		//txtDescription
		txtDescription = new Text(groupEdit, SWT.BORDER);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(lbDescription, 5, SWT.RIGHT);
		fdEdit.top = new FormAttachment(txtPronunciation, 10, SWT.BOTTOM);
		fdEdit.right = new FormAttachment(100, -5);
		//fdEdit.bottom = new FormAttachment(lbPart, 5, SWT.BOTTOM); 
		txtDescription.setLayoutData(fdEdit);
		
		//btnRemove
		btnRemovePart = new Button(groupEdit, SWT.PUSH);
		fdEdit = new FormData();
		fdEdit.top = new FormAttachment(txtDescription, 10, SWT.BOTTOM);
		fdEdit.right = new FormAttachment(txtDescription, 0, SWT.RIGHT);
		btnRemovePart.setText("Remove Part");
		btnRemovePart.setLayoutData(fdEdit);
		
		//btnRemove
		btnEditPart = new Button(groupEdit, SWT.PUSH);
		fdEdit = new FormData();
		fdEdit.top = new FormAttachment(txtDescription, 10, SWT.BOTTOM);
		fdEdit.right = new FormAttachment(btnRemovePart, -5, SWT.LEFT);
		btnEditPart.setText("Edit Part");
		btnEditPart.setLayoutData(fdEdit);
		
		//btnAddPart
		fdEdit = new FormData();
		fdEdit.top = new FormAttachment(txtDescription, 10, SWT.BOTTOM);
		fdEdit.right = new FormAttachment(btnEditPart, -5, SWT.LEFT);
		btnAddPart = new Button(groupEdit, SWT.PUSH);
		btnAddPart.setText("Add Part");
		btnAddPart.setLayoutData(fdEdit);
		
		//tbListParts
		tbListParts = new Table(groupEdit, SWT.NONE);
		tbListParts.setRedraw(false);
		tbListParts.setHeaderVisible(true);
		tbListParts.setLinesVisible(true);
				
		TableColumn[] columns = new TableColumn[6];
		columns[0] = new TableColumn(tbListParts, SWT.NONE);
		columns[0].setText("Part code");
		columns[0].pack();
				
		columns[1] = new TableColumn(tbListParts, SWT.NONE);
		columns[1].setText("Part name");
		columns[1].setWidth(200);
		columns[1].pack();
				
		columns[2] = new TableColumn(tbListParts, SWT.NONE);
		columns[2].setText("Pronunciation ");
		columns[2].setWidth(200);
		//columns[1].pack();
				
		columns[3] = new TableColumn(tbListParts, SWT.NONE);
		columns[3].setText("Description ");
		columns[3].setWidth(300);
		//columns[2].pack();
		
		columns[4] = new TableColumn(tbListParts, SWT.NONE);
		columns[4].setText("[                            ]");
		columns[4].setWidth(75);
		//columns[1].pack();
				
		columns[5] = new TableColumn(tbListParts, SWT.NONE);
		columns[5].setText("          ");
		columns[5].setWidth(70);
		
		tbListParts.setRedraw(true);
				
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(0, 5);
		fdEdit.right = new FormAttachment(100, -5);
		fdEdit.top = new FormAttachment(btnAddPart, 10, SWT.BOTTOM);
		fdEdit.bottom = new FormAttachment(100, -5);
		tbListParts.setLayoutData(fdEdit);
		
		//btnSave
		btnSave = new Button(shell, SWT.PUSH);
		fdEdit = new FormData();
		fdEdit.top = new FormAttachment(groupEdit, 10, SWT.BOTTOM);
		fdEdit.right = new FormAttachment(groupEdit, 0, SWT.RIGHT);
		btnSave.setText("Save word");
		btnSave.setLayoutData(fdEdit);
		
		addActionListener();
	}
	
	private void addActionListener()
	{		
		dynamicAddMeanButtonListener = new Listener(){
			public void handleEvent(Event e)
			{
				Button btn = (Button)e.widget;
				TableDataItem tbItem = (TableDataItem)btn.getData();
				EditMean frm = new EditMean(shell, tbItem.part);
			}
		};
		
		dynamicAddIdiomButtonListener = new Listener(){
			public void handleEvent(Event e)
			{
				Button btn = (Button)e.widget;
				TableDataItem tbItem = (TableDataItem)btn.getData();
				EditIdiom form = new EditIdiom(shell, tbItem.part);
				
			}
		};
		
		FocusListener focusListener = new FocusListener(){
			public void focusGained(FocusEvent e)
			{
				
			}
			
			public void focusLost(FocusEvent e)
			{
				if( (txtWord.getText()==null) || (txtWord.getText().trim().equals("")) )
					return;
				
				String strWord = txtWord.getText().trim();
				WordIndex index = dictionary.findIndex(strWord);
				//int itemCount;
				tbListParts.removeAll();
				
				if(index != null)
				{
                    try {
                        word = dictionary.readWord(index.getAddress());
                        for(PartOfSpeech p:word.getParts())
                        {
                            addItem2Table(p);
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
					return;
				}
				
				if(word == null)
					word = new WordData();
				word.setWordString(strWord);
			}
		};
		
		txtWord.addFocusListener(focusListener);
		
		btnSave.addListener(SWT.Selection, new Listener(){
			public void handleEvent(Event e)
			{
				if(txtWord.getText().trim().equals(""))
					return;
				
				String strWord = txtWord.getText().trim();
				WordIndex index = dictionary.findIndex(strWord);
				
				WordData storedWord = null;
				word.setWordString(strWord);
				word.setRelativeWord(txtRelativeWord.getText().trim());
				
				if(index == null)
				{
					index = new WordIndex(strWord);
				}
				else
				{
                    try{
                        storedWord = dictionary.readWord(index.getAddress());
                        if(word.getLength() > storedWord.getLength())
                        {
                            //Xoa word cu trong data file
                            dictionary.markAsDeleted(storedWord);
                            index.setAddress(-1);
                        }
                    }catch(IOException ie)
                    {
                        ie.printStackTrace();
                    }
				}
				word.setIndex(index);

                try {
                    if(!dictionary.addWord2HashTable(word))
                    {
                        MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
                        messageBox.setText("Ndict 1.0 - Cannot save word!");
                        messageBox.setMessage("This word has not been saved successfully. Please check input data and save it again.");
                        messageBox.open();
                        return;
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

                txtWord.setText("");
				txtDescription.setText("");
				txtPronunciation.setText("");
			}
		});
		
		btnAddPart.addListener(SWT.Selection, new Listener(){
			public void handleEvent(Event e)
			{
				byte partCode = (byte)cboPartOfSpeed.getSelectionIndex();
				
				if(word == null)
				{
					word = new WordData();
					word.setWordString(txtWord.getText());
				}
				
				if(partCode<0)
					return;
				
				int itemCount = tbListParts.getItemCount();
				for(int i=0; i<itemCount; i++)
				{
					TableItem tbitem = tbListParts.getItem(i);
					byte pc = Byte.parseByte(tbitem.getText(0));
					if(partCode == pc)
					{
						MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
						messageBox.setText("Ndict 1.0 - Part already existed");
				        messageBox.setMessage("This part of speed is already existed. Please choose in grid and make change as your wish.");
				        messageBox.open();
				        
				        return;
					}
				}
				
				PartOfSpeech part = null;
				
				if(updatingPart != null)
				{
					if(partCode != updatingPart.getPartCode())
					{
						word.removePart(updatingPart);
						updatingPart = WordData.createPartByCode(partCode);
						word.addPart(updatingPart);
						//updatingPart.setWord(word);
					}
					
					part = updatingPart;
					updatingPart = null;
				}
				else
				{
					switch(partCode)
					{
						case 0:
							part = new Noun(word);
							//part.setPronunciation(txtPronunciation.getText());
							word.addPart(part);
							break;
							
						case 1:
						case 2:
							part = new Verb(partCode, word);
							//part.setPronunciation(txtPronunciation.getText());
							word.addPart(part);
							break;
							
						case 3:
							part = new Adverb(word);
							//part.setPronunciation(txtPronunciation.getText());
							word.addPart(part);
							break;
							
						case 4:
							part = new Adjective(word);
							//part.setPronunciation(txtPronunciation.getText());
							word.addPart(part);
							break;
							
						case 5:
							part = new Article(word);
							//part.setPronunciation(txtPronunciation.getText());
							word.addPart(part);
							break;
							
						case 6:
							part = new Preposition(word);
							//part.setPronunciation(txtPronunciation.getText());
							word.addPart(part);
							break;
					}//end switch
				}
				
				part.setDescription(txtDescription.getText().trim());
				part.setPronunciation(txtPronunciation.getText().trim());
				
				addItem2Table(part);
				
			}
		});
		
		
		this.btnEditPart.addListener(SWT.Selection, new Listener(){
			public void handleEvent(Event e)
			{
				TableItem[] items = tbListParts.getSelection();
				
				if(items == null || items.length <= 0)
					return;
				
				TableDataItem tdi = (TableDataItem)items[0].getData();
				
				updatingPart = tdi.part;
				
				tdi.btnEditIdiom.dispose();
				tdi.btnEditMean.dispose();
				
				tbListParts.remove(tbListParts.getSelectionIndex());
				items[0].dispose();
				
				tbListParts.getColumn(4).pack();
				tbListParts.getColumn(5).pack();
			}
		});
		
		this.btnRemovePart.addListener(SWT.Selection, new Listener(){
			public void handleEvent(Event e)
			{
				TableItem[] items = tbListParts.getSelection();
				
				if(items == null || items.length <= 0)
					return;
				
				TableDataItem tdi = (TableDataItem)items[0].getData();
				tdi.btnEditIdiom.dispose();
				tdi.btnEditMean.dispose();
				
				tbListParts.remove(tbListParts.getSelectionIndex());
				items[0].dispose();
				
				tbListParts.getColumn(4).pack();
				tbListParts.getColumn(5).pack();
			}
		});
		
		shell.addListener(SWT.Close, new Listener() {
		      public void handleEvent(Event event) {
		        dictionary.overwriteIndex();
		    	shell.dispose();
		      }
		    });
	}
	
	private void addItem2Table(PartOfSpeech part)
	{
		int itemCount = tbListParts.getItemCount();
		
		tbListParts.setRedraw(false);
		TableItem item = new TableItem(tbListParts, SWT.NONE);
		item.setText(0, String.valueOf(part.getPartCode()));
		item.setText(1, PartOfSpeech.PartName.get(part.getPartCode()) + "(" + part.getPartCode() + ")");
		item.setText(2, part.getPronunciation());
		item.setText(3, part.getDescription());
		
		//Add buttons
		Button btnAddMean = new Button(tbListParts, SWT.PUSH);
		Button btnAddIdm = new Button(tbListParts, SWT.PUSH);
		
		//Create a TableEditor
		TableEditor editor = new TableEditor(tbListParts);
		editor.grabHorizontal = true;
		btnAddMean.setText("Add mean");
		btnAddMean.pack();
		TableDataItem tbitem = new TableDataItem(item, itemCount, part, btnAddIdm, btnAddMean); 
		btnAddMean.setData(tbitem);
		btnAddMean.addListener(SWT.Selection, dynamicAddMeanButtonListener);
		editor.setEditor(btnAddMean, item, 4);
		
		editor = new TableEditor(tbListParts);
		editor.grabHorizontal = true;
		btnAddIdm.setText("Add idiom");
		btnAddIdm.pack();
		btnAddIdm.setData(tbitem);
		btnAddIdm.addListener(SWT.Selection, dynamicAddIdiomButtonListener);
		editor.setEditor(btnAddIdm, item, 5);
		
		item.setData(tbitem);
		
		tbListParts.update();
		tbListParts.setRedraw(true);
	}
	
	//inner class for part data item
	class TableDataItem
	{
		public TableItem item;
		public int index;
		public PartOfSpeech part;
		public Button btnEditIdiom, btnEditMean;
		
		public TableDataItem()
		{
			item = null;
			index = -1;
			part = null;
		}
		
		public TableDataItem(TableItem itm, int idx, PartOfSpeech p, Button btnIdm, Button btnMean)
		{
			item = itm;
			index = -1;
			part = p;
			btnEditIdiom = btnIdm;
			btnEditMean = btnMean;
		}
	}
}

package nautilus.vdict.desktop;



import nautilus.vdict.data.PartOfSpeech;
import nautilus.vdict.data.WordMean;


import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

//import org.eclipse.swt.events.//


public class EditMean 
{
	private Shell shell = null;
	private PartOfSpeech part;
	
	private Group groupEdit;
	private Group groupMean;
	private Button rdNonCount, rdCount, rdBothCount, btnAddMean, btnSavePart;
	private Table tbListMeans;
	private Text txtMean, txtExample, txtUsage;
	private Combo cboDomain;
	
	private Listener dynamicEditButtonListener;
	private Listener dynamicDeleteButtonListener;
	private WordMean updatingMean = null;
	
	//temporary use of Swing
	//private EventListenerList listenerList = new EventListenerList();
	
	public EditMean()
	{
	}
	
	public EditMean(Shell parent, PartOfSpeech p)
	{
		shell = new Shell(parent);
		shell.setMaximized(true);
		part = p;
		initComponents();
		shell.open();
	}
	
	public void addEventListener()
	{
		
	}
	
	private void initComponents()
	{
		FormLayout layout = new FormLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		shell.setSize(1000, 600);
		shell.setText("Ndict 1.0 - Add/Edit mean");
		shell.setLayout(layout);
		
		//initialize for word data
		Label lb4Word = new Label(shell, SWT.NONE);
		lb4Word.setText("Word: ");
		FormData fdEdit = new FormData();
		fdEdit.left = new FormAttachment(0, 10);
		fdEdit.top = new FormAttachment(0, 10);
		lb4Word.setLayoutData(fdEdit);
		
		Label lbWord = new Label(shell, SWT.NONE);
		lbWord.setText(part.getWord().getWordString());
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(lb4Word, 5, SWT.RIGHT);
		fdEdit.top = new FormAttachment(0, 10);
		lbWord.setLayoutData(fdEdit);
		
		Label lb4Part = new Label(shell, SWT.NONE);
		lb4Part.setText("Part of speed: ");
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(lbWord, 15, SWT.RIGHT);
		fdEdit.top = new FormAttachment(0, 10);
		lb4Part.setLayoutData(fdEdit);
		
		Label lbPart = new Label(shell, SWT.NONE);
		lbPart.setText(part.getPartText());
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(lb4Part, 5, SWT.RIGHT);
		fdEdit.top = new FormAttachment(0, 10);
		lbPart.setLayoutData(fdEdit);
		
		
		switch(part.getPartCode())
		{
			case 0: //NOUN
				initComponent4Noun();
				break;
				
			case 1: //Transitive verb
			case 2: //intransitive verb
				initComponent4Verb();
				break;
				
			case 3: //Adverb
				initComponent4Adverb();
				break;
				
			case 4: //Adjective
				initComponent4Adjective();
				break;
				
			case 5: //Article
				initComponent4Article();
				break;
				
			case 6: //Preposition
				initComponent4Preposition();
				break;
				
			default:
				break;
		}
		
		
		groupMean = new Group(shell, SWT.NONE);
		groupMean.setLayout(new FormLayout());
		
		if(groupEdit != null)
		{
			fdEdit = new FormData();
			fdEdit.left = new FormAttachment(groupEdit, 0, SWT.LEFT);
			fdEdit.top = new FormAttachment(groupEdit, 5, SWT.BOTTOM);
			fdEdit.right = new FormAttachment(groupEdit, 0, SWT.RIGHT);
			fdEdit.bottom = new FormAttachment(100, -45);
		}
		else
		{
			fdEdit = new FormData();
			fdEdit.left = new FormAttachment(0, 5);
			fdEdit.top = new FormAttachment(0, 35);
			fdEdit.right = new FormAttachment(100, -5);
			fdEdit.bottom = new FormAttachment(100, -45);
		}
		
		groupMean.setLayoutData(fdEdit);
		
		Label lbUsage = new Label(groupMean, SWT.NONE);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(0, 10);
		fdEdit.top = new FormAttachment(0, 10);
		lbUsage.setText("Usage: ");
		lbUsage.setLayoutData(fdEdit);
		
		txtUsage = new Text(groupMean, SWT.BORDER);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(lbUsage, 5, SWT.RIGHT);
		fdEdit.top = new FormAttachment(lbUsage, 0, SWT.TOP);
		fdEdit.right = new FormAttachment(100, -5);
		txtUsage.setLayoutData(fdEdit);
		
		Label lbMean = new Label(groupMean, SWT.NONE);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(0, 10);
		fdEdit.top = new FormAttachment(txtUsage, 10, SWT.BOTTOM);
		lbMean.setText("Mean: ");
		lbMean.setLayoutData(fdEdit);
		
		txtMean = new Text(groupMean, SWT.BORDER);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(lbMean, 5, SWT.RIGHT);
		fdEdit.top = new FormAttachment(lbMean, 0, SWT.TOP);
		fdEdit.right = new FormAttachment(100, -5);
		txtMean.setLayoutData(fdEdit);
		
		Label lbExample = new Label(groupMean, SWT.NONE);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(0, 10);
		fdEdit.top = new FormAttachment(txtMean, 10, SWT.BOTTOM);
		lbExample.setText("Example: ");
		lbExample.setLayoutData(fdEdit);
		
		txtExample = new Text(groupMean, SWT.BORDER);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(lbExample, 5, SWT.RIGHT);
		fdEdit.top = new FormAttachment(lbExample, 0, SWT.TOP);
		fdEdit.right = new FormAttachment(100, -5);
		txtExample.setLayoutData(fdEdit);
		
		Label lbDomain = new Label(groupMean, SWT.NONE);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(0, 10);
		fdEdit.top = new FormAttachment(txtExample, 10, SWT.BOTTOM);
		lbDomain.setText("Domain: ");
		lbDomain.setLayoutData(fdEdit);
		
		cboDomain = new Combo(groupMean, SWT.DROP_DOWN | SWT.READ_ONLY);
		String[] strDomains = WordMean.DomainMap.values().toArray(new String[0]);
		cboDomain.setItems(strDomains);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(lbDomain, 5, SWT.RIGHT);
		fdEdit.top = new FormAttachment(lbDomain, 0, SWT.TOP);
		fdEdit.right = new FormAttachment(100, -5);
		cboDomain.setLayoutData(fdEdit);
		
		btnAddMean = new Button(groupMean, SWT.PUSH);
		btnAddMean.setText("Add mean");
		fdEdit = new FormData();
		fdEdit.top = new FormAttachment(cboDomain, 5, SWT.BOTTOM);
		fdEdit.right = new FormAttachment(cboDomain, 0, SWT.RIGHT);
		btnAddMean.setLayoutData(fdEdit);
		
		//tbListParts
		tbListMeans = new Table(groupMean, SWT.NONE);
		tbListMeans.setRedraw(false);
		tbListMeans.setHeaderVisible(true);
		tbListMeans.setLinesVisible(true);
						
		TableColumn[] columns = new TableColumn[6];
		columns[0] = new TableColumn(tbListMeans, SWT.NONE);
		columns[0].setText("Usage");
		columns[0].pack();
						
		columns[1] = new TableColumn(tbListMeans, SWT.NONE);
		columns[1].setText("Mean");
		columns[1].setWidth(200);
		columns[1].pack();
						
		columns[2] = new TableColumn(tbListMeans, SWT.NONE);
		columns[2].setText("Example ");
		columns[2].setWidth(200);
		//columns[1].pack();
						
		columns[3] = new TableColumn(tbListMeans, SWT.NONE);
		columns[3].setText("Domain");
		columns[3].setWidth(300);
		//columns[2].pack();
		
		columns[4] = new TableColumn(tbListMeans, SWT.NONE);
		columns[4].setText(" ");
		columns[4].setWidth(200);
						
		columns[5] = new TableColumn(tbListMeans, SWT.NONE);
		columns[5].setText("");
		columns[5].setWidth(300);
				
		tbListMeans.setRedraw(true);
						
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(0, 5);
		fdEdit.right = new FormAttachment(100, -5);
		fdEdit.top = new FormAttachment(btnAddMean, 10, SWT.BOTTOM);
		fdEdit.bottom = new FormAttachment(100, -5);
		tbListMeans.setLayoutData(fdEdit);
		
		//button btnSavePart
		btnSavePart = new Button(shell, SWT.PUSH);
		btnSavePart.setText("Save");
		fdEdit = new FormData();
		fdEdit.top = new FormAttachment(groupMean, 5, SWT.BOTTOM);
		fdEdit.right = new FormAttachment(groupMean, 0, SWT.RIGHT);
		btnSavePart.setLayoutData(fdEdit);
		
		addListener();
		
	}//end initComponents()
	
	private void initComponent4Noun()
	{
		groupEdit = new Group(shell, SWT.NONE);
		groupEdit.setLayout(new FormLayout());
		
		FormData fdEdit = new FormData();
		fdEdit.left = new FormAttachment(0, 5);
		fdEdit.top = new FormAttachment(0, 35);
		fdEdit.right = new FormAttachment(100, -5);
		fdEdit.bottom = new FormAttachment(25, 0);
		groupEdit.setLayoutData(fdEdit);
		
		Label lbCountability = new Label(groupEdit, SWT.NONE);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(0, 5);
		fdEdit.top = new FormAttachment(0, 10);
		lbCountability.setText("Countability: ");
		lbCountability.setLayoutData(fdEdit);
		
		Composite radioGroup = new Composite(groupEdit, SWT.NULL);
		radioGroup.setLayout(new RowLayout());
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(lbCountability, 5, SWT.RIGHT);
		fdEdit.top = new FormAttachment(lbCountability,0, SWT.TOP);
		radioGroup.setLayoutData(fdEdit);
		
		rdNonCount = new Button(radioGroup, SWT.RADIO);
		rdNonCount.setText("Non-count Noun");
		rdCount = new Button(radioGroup, SWT.RADIO);
		rdCount.setText("Count Noun");
		rdBothCount = new Button(radioGroup, SWT.RADIO);
		rdBothCount.setText("Both");
		
		//Plural form
		Label lbPluralForm = new Label(groupEdit, SWT.NONE);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(lbCountability, 0, SWT.LEFT);
		fdEdit.top = new FormAttachment(radioGroup, 10, SWT.BOTTOM);
		lbPluralForm.setText("Plural form: ");
		lbPluralForm.setLayoutData(fdEdit);
		
		Text txtPluralForm = new Text(groupEdit, SWT.BORDER);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(lbPluralForm, 5, SWT.RIGHT);
		fdEdit.top = new FormAttachment(radioGroup, 10, SWT.BOTTOM);
		fdEdit.right = new FormAttachment(100, -5);
		txtPluralForm.setLayoutData(fdEdit);
	}
	
	private void initComponent4Verb()
	{
		groupEdit = new Group(shell, SWT.NONE);
		groupEdit.setLayout(new FormLayout());
		
		FormData fdEdit = new FormData();
		fdEdit.left = new FormAttachment(0, 5);
		fdEdit.top = new FormAttachment(0, 35);
		fdEdit.right = new FormAttachment(100, -5);
		fdEdit.bottom = new FormAttachment(25, 0);
		groupEdit.setLayoutData(fdEdit);
		
		Label lbPast = new Label(groupEdit, SWT.NONE);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(0, 5);
		fdEdit.top = new FormAttachment(0, 10);
		lbPast.setText("Past: ");
		lbPast.setLayoutData(fdEdit);
		
		Text txtPast = new Text(groupEdit, SWT.NULL);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(lbPast, 5, SWT.RIGHT);
		fdEdit.top = new FormAttachment(lbPast,0, SWT.TOP);
		fdEdit.width = 300;
		txtPast.setLayoutData(fdEdit);
		
		//past perfect form
		Label lbPastPerfect = new Label(groupEdit, SWT.NONE);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(txtPast, 15, SWT.RIGHT);
		fdEdit.top = new FormAttachment(lbPast,0, SWT.TOP);
		lbPastPerfect.setText("Past perfect: ");
		lbPastPerfect.setLayoutData(fdEdit);
		
		Text txtPastPerfect = new Text(groupEdit, SWT.BORDER);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(lbPastPerfect, 5, SWT.RIGHT);
		fdEdit.top = new FormAttachment(lbPast,0, SWT.TOP);
		fdEdit.right = new FormAttachment(100, -5);
		txtPastPerfect.setLayoutData(fdEdit);
		
		//gerund form
		Label lbGerundForm = new Label(groupEdit, SWT.NONE);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(lbPast, 0, SWT.LEFT);
		fdEdit.top = new FormAttachment(txtPastPerfect, 10, SWT.BOTTOM);
		lbGerundForm.setText("Gerund form: ");
		lbGerundForm.setLayoutData(fdEdit);
		
		Text txtGerundForm = new Text(groupEdit, SWT.BORDER);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(lbGerundForm, 5, SWT.RIGHT);
		fdEdit.top = new FormAttachment(txtPastPerfect, 10, SWT.BOTTOM);
		fdEdit.right = new FormAttachment(100, -5);
		txtGerundForm.setLayoutData(fdEdit);
	}
	
	private void initComponent4Adverb()
	{
		groupEdit = new Group(shell, SWT.NONE);
		groupEdit.setLayout(new FormLayout());
		
		FormData fdEdit = new FormData();
		fdEdit.left = new FormAttachment(0, 5);
		fdEdit.top = new FormAttachment(0, 35);
		fdEdit.right = new FormAttachment(100, -5);
		fdEdit.bottom = new FormAttachment(25, 0);
		groupEdit.setLayoutData(fdEdit);
		
		Label lbComparative = new Label(groupEdit, SWT.NONE);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(0, 5);
		fdEdit.top = new FormAttachment(0, 10);
		lbComparative.setText("Comparative: ");
		lbComparative.setLayoutData(fdEdit);
		
		Text txtComparative = new Text(groupEdit, SWT.BORDER);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(lbComparative, 5, SWT.RIGHT);
		fdEdit.top = new FormAttachment(lbComparative,0, SWT.TOP);
		fdEdit.width = 500;
		txtComparative.setLayoutData(fdEdit);
		
		//gerund form
		Label lbSuperlative = new Label(groupEdit, SWT.NONE);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(lbComparative, 0, SWT.LEFT);
		fdEdit.top = new FormAttachment(txtComparative, 10, SWT.BOTTOM);
		lbSuperlative.setText("Superlative: ");
		lbSuperlative.setLayoutData(fdEdit);
		
		Text txtSuperlative = new Text(groupEdit, SWT.BORDER);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(lbSuperlative, 5, SWT.RIGHT);
		fdEdit.top = new FormAttachment(txtComparative, 10, SWT.BOTTOM);
		fdEdit.right = new FormAttachment(100, -5);
		txtSuperlative.setLayoutData(fdEdit);
	}
	
	private void initComponent4Adjective()
	{
		groupEdit = new Group(shell, SWT.NONE);
		groupEdit.setLayout(new FormLayout());
		
		FormData fdEdit = new FormData();
		fdEdit.left = new FormAttachment(0, 5);
		fdEdit.top = new FormAttachment(0, 35);
		fdEdit.right = new FormAttachment(100, -5);
		fdEdit.bottom = new FormAttachment(25, 0);
		groupEdit.setLayoutData(fdEdit);
		
		Label lbComparative = new Label(groupEdit, SWT.NONE);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(0, 5);
		fdEdit.top = new FormAttachment(0, 10);
		lbComparative.setText("Comparative: ");
		lbComparative.setLayoutData(fdEdit);
		
		Text txtComparative = new Text(groupEdit, SWT.BORDER);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(lbComparative, 5, SWT.RIGHT);
		fdEdit.top = new FormAttachment(lbComparative,0, SWT.TOP);
		fdEdit.width = 200;
		txtComparative.setLayoutData(fdEdit);
		
		//Superlative
		Label lbSuperlative = new Label(groupEdit, SWT.NONE);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(txtComparative, 15, SWT.RIGHT);
		fdEdit.top = new FormAttachment(txtComparative, 0, SWT.TOP);
		lbSuperlative.setText("Superlative: ");
		lbSuperlative.setLayoutData(fdEdit);
		
		Text txtSuperlative = new Text(groupEdit, SWT.BORDER);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(lbSuperlative, 5, SWT.RIGHT);
		fdEdit.top = new FormAttachment(lbComparative,0, SWT.TOP);
		fdEdit.right = new FormAttachment(100, -5);
		txtSuperlative.setLayoutData(fdEdit);
		
		//Adverb form
		Label lbAdverbForm = new Label(groupEdit, SWT.NONE);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(lbComparative, 0, SWT.LEFT);
		fdEdit.top = new FormAttachment(txtSuperlative, 10, SWT.BOTTOM);
		lbAdverbForm.setText("Superlative: ");
		lbAdverbForm.setLayoutData(fdEdit);
				
		Text txtAdverbForm = new Text(groupEdit, SWT.BORDER);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(lbAdverbForm, 5, SWT.RIGHT);
		fdEdit.top = new FormAttachment(txtSuperlative, 10, SWT.BOTTOM);
		fdEdit.right = new FormAttachment(100, -5);
		txtAdverbForm.setLayoutData(fdEdit);
	}
	
	private void initComponent4Article()
	{
	}
	
	private void initComponent4Preposition()
	{
	}
	
	private void initGlobalListiner()
	{
		dynamicEditButtonListener = new Listener()
		{
			public void handleEvent(Event e)
			{
				Button btn = (Button)e.widget;
				MeanDataItem mdi = (MeanDataItem)btn.getData();
				
				for(int i =0;i<tbListMeans.getItemCount();i++)
					if(tbListMeans.getItems()[i] == mdi.tableItem)
					{
						tbListMeans.setRedraw(false);
						tbListMeans.remove(i);
						
						
						mdi.asideBtn.dispose();
						btn.dispose();
						tbListMeans.update();
						tbListMeans.setRedraw(true);
						
						updatingMean = mdi.mean;
						
						break;
					}
				
			}
		};
		
		dynamicDeleteButtonListener = new Listener()
		{
			public void handleEvent(Event e)
			{
				Button btn = (Button)e.widget;
				MeanDataItem mdi = (MeanDataItem)btn.getData();
				
				for(int i =0;i<tbListMeans.getItemCount();i++)
					if(tbListMeans.getItems()[i] == mdi.tableItem)
					{
						tbListMeans.setRedraw(false);
						tbListMeans.remove(i);
						
						
						mdi.asideBtn.dispose();
						btn.dispose();
						
						tbListMeans.update();
						tbListMeans.setRedraw(true);
						
						
						part.removeMean(mdi.mean);
						break;
					}
				
			}
		};
	}
	
	private void addListener()
	{
		initGlobalListiner();
		
		btnAddMean.addListener(SWT.Selection, new Listener(){
			public void handleEvent(Event e)
			{
				byte domain = (byte)cboDomain.getSelectionIndex();
				WordMean wmean;
				
				if(txtMean.getText().trim().equals(""))
				{
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
					messageBox.setText("Ndict 1.0 - Mean cannot be blank!");
			        messageBox.setMessage("This mean is empty. Please type in data for mean field.");
			        messageBox.open();
			        return;
				}
				
				if(updatingMean != null)
				{
					updatingMean.setMean(txtMean.getText().trim());
					updatingMean.setExample(txtExample.getText().trim());
					updatingMean.setDomain(domain);
					updatingMean.setUsage(txtUsage.getText().trim());
					
					wmean = updatingMean;
					
					updatingMean = null;
				}
				else
				{
					wmean = new WordMean(txtMean.getText().trim(), txtExample.getText().trim(), txtUsage.getText().trim(), domain);
					part.addMean(wmean);
				}
				
				//update mean table
				tbListMeans.setRedraw(false);
				TableItem item = new TableItem(tbListMeans, SWT.NONE);
				item.setText(0, wmean.getUsage());
				item.setText(1, wmean.getMean());
				item.setText(2, wmean.getExample());
				item.setText(3, WordMean.DomainMap.get(wmean.getDomain()) + "(" + wmean.getDomain() + ")");
				
				//Create a TableEditor
				TableEditor editor = new TableEditor(tbListMeans);
				editor.grabHorizontal = true;
				
				Button btnEdit = new Button(tbListMeans, SWT.PUSH);
				Button btnDel = new Button(tbListMeans, SWT.PUSH);
				
				btnEdit.setText("Edit");
				btnEdit.pack();
				btnEdit.setData(new MeanDataItem(wmean, item, btnDel));
				btnEdit.addListener(SWT.Selection, dynamicEditButtonListener);
				editor.setEditor(btnEdit, item, 4);
				
				//Add button DELETE
				editor = new TableEditor(tbListMeans);
				editor.grabHorizontal = true;
				
				btnDel.setText("Delete");
				btnDel.pack();
				//btn.setData(new TableDataItem(item, itemCount, p));
				btnDel.setData(new MeanDataItem(wmean, item, btnEdit));
				btnDel.addListener(SWT.Selection, dynamicDeleteButtonListener);
				editor.setEditor(btnDel, item, 5);
				
				tbListMeans.setRedraw(true);
			}
		});
		
		btnSavePart.addListener(SWT.Selection, new Listener(){
			public void handleEvent(Event e)
			{
				shell.dispose();
			}
		});
	}
	
	//Inner class for manipulate data in table
	class MeanDataItem
	{
		public WordMean mean;
		public TableItem tableItem;
		public Button asideBtn;
		
		public MeanDataItem()
		{
		}
		
		public MeanDataItem(WordMean m, TableItem itm, Button btn)
		{
			mean = m;
			tableItem = itm;
			asideBtn = btn;
		}
	}
}

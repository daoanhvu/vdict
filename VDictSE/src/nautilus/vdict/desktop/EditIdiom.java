package nautilus.vdict.desktop;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nautilus.vdict.data.Idiom;
import nautilus.vdict.data.PartOfSpeech;
import nautilus.vdict.data.WordMean;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
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

public class EditIdiom 
{
	private Shell shell = null;
	private PartOfSpeech part;
	
	private Group groupMean;
	private Button btnAddIdiom, btnSavePart, btnRemoveIdm;
	private Table tbListIdioms;
	private Text txtMean, txtExample, txtUsage, txtIdiom;
	private Combo cboDomain;
	
	public EditIdiom()
	{
		
	}
	
	public EditIdiom(Shell parent, PartOfSpeech p)
	{
		shell = new Shell(parent);
		shell.setMaximized(true);
		part = p;
		initComponents();
		shell.open();
	}
	
	private void initComponents()
	{
		FormLayout layout = new FormLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		shell.setSize(1000, 600);
		shell.setText("Ndict 1.0 - Add/Edit idiom");
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
		
		groupMean = new Group(shell, SWT.NONE);
		groupMean.setLayout(new FormLayout());
		
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(0, 5);
		fdEdit.top = new FormAttachment(0, 35);
		fdEdit.right = new FormAttachment(100, -5);
		fdEdit.bottom = new FormAttachment(100, -45);
		groupMean.setLayoutData(fdEdit);
		
		Label lbIdiom = new Label(groupMean, SWT.NONE);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(0, 10);
		fdEdit.top = new FormAttachment(0, 10);
		lbIdiom.setText("Idiom: ");
		lbIdiom.setLayoutData(fdEdit);
		
		txtIdiom = new Text(groupMean, SWT.BORDER);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(lbIdiom, 5, SWT.RIGHT);
		fdEdit.top = new FormAttachment(lbIdiom, 0, SWT.TOP);
		fdEdit.right = new FormAttachment(100, -5);
		txtIdiom.setLayoutData(fdEdit);
		
		Label lbUsage = new Label(groupMean, SWT.NONE);
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(0, 10);
		fdEdit.top = new FormAttachment(txtIdiom, 10, SWT.BOTTOM);
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
		
		//btnRemoveIdm
		btnRemoveIdm = new Button(groupMean, SWT.PUSH);
		btnRemoveIdm.setText("Remove");
		fdEdit = new FormData();
		fdEdit.top = new FormAttachment(cboDomain, 5, SWT.BOTTOM);
		fdEdit.right = new FormAttachment(cboDomain, 0, SWT.RIGHT);
		btnRemoveIdm.setLayoutData(fdEdit);
		
		//btnAddIdiom
		btnAddIdiom = new Button(groupMean, SWT.PUSH);
		btnAddIdiom.setText("Add idiom");
		fdEdit = new FormData();
		fdEdit.top = new FormAttachment(cboDomain, 5, SWT.BOTTOM);
		fdEdit.right = new FormAttachment(btnRemoveIdm, -5, SWT.LEFT);
		btnAddIdiom.setLayoutData(fdEdit);
		
		//tbListParts
		tbListIdioms = new Table(groupMean, SWT.NONE);
		tbListIdioms.setRedraw(false);
		tbListIdioms.setHeaderVisible(true);
		tbListIdioms.setLinesVisible(true);
						
		TableColumn[] columns = new TableColumn[5];
		columns[0] = new TableColumn(tbListIdioms, SWT.NONE);
		columns[0].setText("Idiom");
		columns[0].pack();
		
		columns[1] = new TableColumn(tbListIdioms, SWT.NONE);
		columns[1].setText("Domain");
		columns[1].setWidth(300);
						
		columns[2] = new TableColumn(tbListIdioms, SWT.NONE);
		columns[2].setText("Mean");
		columns[2].setWidth(200);
		columns[2].pack();
		
		columns[3] = new TableColumn(tbListIdioms, SWT.NONE);
		columns[3].setText("Usage");
		columns[3].setWidth(200);
						
		columns[4] = new TableColumn(tbListIdioms, SWT.NONE);
		columns[4].setText("Example ");
		columns[4].setWidth(200);
		
		tbListIdioms.setRedraw(true);
						
		fdEdit = new FormData();
		fdEdit.left = new FormAttachment(0, 5);
		fdEdit.right = new FormAttachment(100, -5);
		fdEdit.top = new FormAttachment(btnAddIdiom, 10, SWT.BOTTOM);
		fdEdit.bottom = new FormAttachment(100, -5);
		tbListIdioms.setLayoutData(fdEdit);
		
		//button btnSavePart
		btnSavePart = new Button(shell, SWT.PUSH);
		btnSavePart.setText("Save");
		fdEdit = new FormData();
		fdEdit.top = new FormAttachment(groupMean, 5, SWT.BOTTOM);
		fdEdit.right = new FormAttachment(groupMean, 0, SWT.RIGHT);
		btnSavePart.setLayoutData(fdEdit);
		
		addListener();
	}//end initComponents()
	
	private void addListener()
	{
		tbListIdioms.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent e)
			{
				TableItem itm = tbListIdioms.getItem(new Point(e.x,e.y));
				
				if(itm != null)
				{
					txtIdiom.setText(itm.getText(0));
					//cboDomain.setText(itm.getText(1));
					String s1 = itm.getText(1);
					
					txtMean.setText(itm.getText(2));
					txtUsage.setText(itm.getText(3));
					txtExample.setText(itm.getText(4));
					
					for(int i=0;i<tbListIdioms.getItemCount();i++)
						if(itm == tbListIdioms.getItem(i))
						{
							tbListIdioms.remove(i);
							break;
						}
					
					String s = findString("[0-9]", s1);
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
					messageBox.setText("Ndict 1.0 - Idiom existed!");
			        messageBox.setMessage(s1 + "\n" + (s==null?"":s));
			        messageBox.open();
				}
			}
			
			public void mouseDown(MouseEvent e)
			{
				
			}
			
			public void mouseUp(MouseEvent e)
			{
				
			}
		});
		
		btnAddIdiom.addListener(SWT.Selection, new Listener(){
			public void handleEvent(Event e)
			{
				String strIdm = txtIdiom.getText().trim();
				
				for(int i=0;i<tbListIdioms.getItemCount();i++)
				{
					if(strIdm.equals(tbListIdioms.getItem(i).getText(0)))
					{
						MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
						messageBox.setText("Ndict 1.0 - Idiom existed!");
				        messageBox.setMessage("This idiom is existed already. Double click it in table to edit.");
				        messageBox.open();
						return;
					}
				}
				
				byte domain = (byte)cboDomain.getSelectionIndex();
				
				tbListIdioms.setRedraw(false);
				TableItem item = new TableItem(tbListIdioms, SWT.NONE);
				item.setText(0, strIdm);
				item.setText(1, WordMean.DomainMap.get(domain) + "(" + domain + ")");
				item.setText(2, txtMean.getText());
				item.setText(3, txtUsage.getText());
				item.setText(4, txtExample.getText());
				tbListIdioms.setRedraw(true);
			}
		});
		
		btnRemoveIdm.addListener(SWT.Selection, new Listener(){
			public void handleEvent(Event e)
			{
				tbListIdioms.remove(tbListIdioms.getSelectionIndex());
			}
		});
		
		btnSavePart.addListener(SWT.Selection, new Listener(){
			public void handleEvent(Event e)
			{
				Idiom idm = null;
				for(TableItem itm: tbListIdioms.getItems())
				{
					idm = new Idiom();
					idm.setIdm(itm.getText(0));
					//idm.setMean(new WordMean(txtMean.getText(), txtExample.getText(), txtUsage.getText(), domain));
					
					part.addIdiom(idm);
				}
				
				shell.dispose();
			}
		});
	}
	
	private static String findString(String pattern, String inputString)
	{
		try{
			Pattern pt = Pattern.compile(pattern);
			Matcher m = pt.matcher(inputString);
			return m.group();
		}catch(IllegalStateException ex)
		{
			return null;
		}
	}
}

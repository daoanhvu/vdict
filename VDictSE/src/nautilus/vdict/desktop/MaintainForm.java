package nautilus.vdict.desktop;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import nautilus.vdict.data.Idiom;
import nautilus.vdict.data.PartOfSpeech;
import nautilus.vdict.data.VDictionary;
import nautilus.vdict.data.WordData;
import nautilus.vdict.data.WordIndex;
import nautilus.vdict.data.WordMean;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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

public class MaintainForm 
{
	private Shell shell = null;
	private Button btnImport, btnBrowse;
	private Text txtFile;
	
	private VDictionary dictionary;
	
	public MaintainForm(Shell parent, VDictionary dict)
	{
		dictionary = dict;
		shell = new Shell(parent);
		shell.setSize(350, 200);
		initComponents();
		shell.open();
	}
	
	private void initComponents()
	{
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
		txtFile.setText("./data.xls");
		
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
				readExcelFile();
			}
			
		});

	}
	
	private void readExcelFile()
	{
		int i;
		FileInputStream fis;
		try {
			String datafile = txtFile.getText();
			//fis = new FileInputStream(datafile);
			fis = new FileInputStream("/home/nautilus/bins/vdict/data.xls");
			Workbook wb = new HSSFWorkbook(fis);
			Sheet sheet1 = wb.getSheetAt(0);
			WordIndex windex = null;
			WordData worddata = null;
			WordMean mean = null;
			Idiom idm = null;
			PartOfSpeech part = null;
			boolean shouldWrite = false;
			
			System.out.println("[MaintainForm] Number of record in data file: " + sheet1.getLastRowNum());
			
			for(i=1;i<=sheet1.getLastRowNum();i++)
			{
				Row row = sheet1.getRow(i);
				
				Cell cell0 = row.getCell(0);
				Cell cell1 = row.getCell(1);
				Cell cell2 = row.getCell(2);
				Cell cell3 = row.getCell(3);
				Cell cell4 = row.getCell(4);
				Cell cell5 = row.getCell(5);
				Cell cell6 = row.getCell(6);
				Cell cell7 = row.getCell(7);
				Cell cell8 = row.getCell(8);
				Cell cell9 = row.getCell(9);
				Cell cell10 = row.getCell(10);
				Cell cell11 = row.getCell(11);
				Cell cell12 = row.getCell(12);
				
				//System.out.println("" + cell5);
				
				String strWord = cell0==null?"":cell0.getRichStringCellValue().getString();
				String relatetiveWord = cell1==null?"":cell1.getRichStringCellValue().getString();
				byte partCode = (byte)cell2.getNumericCellValue();
				String pronunciation = cell3.getRichStringCellValue().getString();
				String strMean = cell4.getRichStringCellValue().getString();
				String meanUsage = cell5==null?"":cell5.getRichStringCellValue().getString();
				String strExmp = cell6==null?"":cell6.getRichStringCellValue().getString();
				byte domain = (byte) cell7.getNumericCellValue();
				String idiom = cell8==null?"":cell8.getRichStringCellValue().getString();
				String idiomUsage = cell9==null?"":cell9.getRichStringCellValue().getString();
				String idiomMean = cell10==null?"":cell10.getRichStringCellValue().getString();
				String idiomExmp = cell11==null?"":cell11.getRichStringCellValue().getString();
				String idiomDomain = cell12==null?"":cell12.getRichStringCellValue().getString();
				
				if(strWord.trim().length() > 0)
				{
					if(shouldWrite)
					{
						//write the previous world
						//dictionary.writeWord(worddata);
						dictionary.addWord2HashTable(worddata);
						System.out.println("[MaintainForm] write word to file.");
						shouldWrite = false;
					}
					else
						shouldWrite = true;
					
					//form a new new word
					windex = dictionary.findIndex(strWord);
					if(windex != null)
					{
						worddata = dictionary.readWord(windex.getAddress());
					}
					else
					{
						windex = new WordIndex(strWord);
						worddata = new WordData();
						worddata.setIndex(windex);
						worddata.setRelativeWord(relatetiveWord);
					}
				}
				part = WordData.createPartByCode(partCode);
				part.setPronunciation(pronunciation);
				mean = new WordMean(strMean, strExmp, meanUsage, domain);
				
				if(idiom.trim().length() > 0)
				{
					idm = new Idiom();
					idm.setIdm(idiom);
					idm.setMean(new WordMean(idiomMean, idiomExmp, idiomUsage, Byte.parseByte(idiomDomain)));
					part.addIdiom(idm);
				}
				
				part.addMean(mean);
				worddata.addPart(part);
			}//end for
			

			if(shouldWrite)
			{
				//write the previous world
				//dictionary.writeWord(worddata);
				dictionary.addWord2HashTable(worddata);
				System.out.println("[MaintainForm] write word to file.");
			}
			
			fis.close();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}

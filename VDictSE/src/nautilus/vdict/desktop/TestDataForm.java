package nautilus.vdict.desktop;

import nautilus.vdict.VDictUtility;
import nautilus.vdict.data.PartOfSpeech;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.*;

//import java.nio.file.

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created with IntelliJ IDEA.
 * User: davu
 * Date: 11/7/12
 * Time: 12:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestDataForm
{
    private Shell shell = null;
    private Text txtDataFilePath, txtOffset, txtLength, txtData;
    private Button btnBrowse, btnTest;
    private Button rdByte, rdShort, rdInt, rdLong, rdString;

    public TestDataForm(Shell shParent)
    {
        shell = new Shell(shParent);
        initComponents();

        shell.open();
    }

    private void initComponents()
    {
        FormLayout layout = new FormLayout();
        layout.marginHeight = 5;
        layout.marginWidth = 5;
        shell.setSize(600, 200);
        shell.setText("Vdict - Desktop verion 1.0");
        shell.setLayout(layout);

        //Declare controls
        FormData fdEdit = new FormData();
        Label lbFilePath = new Label(shell, SWT.NONE);
        lbFilePath.setText("Data File");
        fdEdit.left = new FormAttachment(0, 5);
        fdEdit.top = new FormAttachment(0, 10);
        lbFilePath.setLayoutData(fdEdit);

        txtDataFilePath = new Text(shell, SWT.BORDER);
        txtDataFilePath.setEnabled(false);
        fdEdit = new FormData();
        fdEdit.left = new FormAttachment(lbFilePath, 5, SWT.RIGHT);
        fdEdit.bottom = new FormAttachment(lbFilePath, 0, SWT.BOTTOM);
        fdEdit.right = new FormAttachment(100, -70);
        txtDataFilePath.setLayoutData(fdEdit);

        btnBrowse = new Button(shell, SWT.PUSH);
        btnBrowse.setText("Browse");
        fdEdit = new FormData();
        fdEdit.left = new FormAttachment(txtDataFilePath, 5, SWT.RIGHT);
        fdEdit.bottom = new FormAttachment(lbFilePath, 0, SWT.BOTTOM);
        fdEdit.right = new FormAttachment(100, -5);
        btnBrowse.setLayoutData(fdEdit);

        fdEdit = new FormData();
        Label lbOffset = new Label(shell, SWT.NONE);
        fdEdit = new FormData();
        fdEdit.left = new FormAttachment(0, 5);
        fdEdit.top = new FormAttachment(lbFilePath, 10, SWT.BOTTOM);
        lbOffset.setText("Offset");
        lbOffset.setLayoutData(fdEdit);

        txtOffset = new Text(shell, SWT.BORDER);
        fdEdit = new FormData();
        fdEdit.width = 50;
        fdEdit.left = new FormAttachment(lbOffset, 5, SWT.RIGHT);
        fdEdit.bottom = new FormAttachment(lbOffset, 0, SWT.BOTTOM);
        txtOffset.setLayoutData(fdEdit);

        Label lbLength = new Label(shell, SWT.NONE);
        fdEdit = new FormData();
        fdEdit.left = new FormAttachment(txtOffset, 5, SWT.RIGHT);
        fdEdit.top = new FormAttachment(lbOffset, 0, SWT.TOP);
        lbLength.setText("Length");
        lbLength.setLayoutData(fdEdit);

        txtLength = new Text(shell, SWT.BORDER);
        fdEdit = new FormData();
        fdEdit.left = new FormAttachment(lbLength, 5, SWT.RIGHT);
        fdEdit.top = new FormAttachment(txtOffset, 0, SWT.TOP);
        fdEdit.right = new FormAttachment(100, -5);
        //fdEdit.bottom = new FormAttachment(100, -5);
        txtLength.setLayoutData(fdEdit);

        //Radio Buttons
        rdByte = new Button(shell, SWT.RADIO);
        rdByte.setText("Byte");
        fdEdit = new FormData();
        fdEdit.left = new FormAttachment(0, 5);
        fdEdit.top = new FormAttachment(lbOffset, 10, SWT.BOTTOM);
        rdByte.setLayoutData(fdEdit);

        rdShort = new Button(shell, SWT.RADIO);
        rdShort.setText("Short");
        fdEdit = new FormData();
        fdEdit.left = new FormAttachment(rdByte, 5, SWT.RIGHT);
        fdEdit.top = new FormAttachment(lbOffset, 10, SWT.BOTTOM);
        rdShort.setLayoutData(fdEdit);

        rdInt = new Button(shell, SWT.RADIO);
        rdInt.setText("Int");
        fdEdit = new FormData();
        fdEdit.left = new FormAttachment(rdShort, 5, SWT.RIGHT);
        fdEdit.top = new FormAttachment(lbOffset, 10, SWT.BOTTOM);
        rdInt.setLayoutData(fdEdit);

        rdLong = new Button(shell, SWT.RADIO);
        rdLong.setText("Long");
        fdEdit = new FormData();
        fdEdit.left = new FormAttachment(rdInt, 5, SWT.RIGHT);
        fdEdit.top = new FormAttachment(lbOffset, 10, SWT.BOTTOM);
        rdLong.setLayoutData(fdEdit);

        rdString = new Button(shell, SWT.RADIO);
        rdString.setText("String");
        fdEdit = new FormData();
        fdEdit.left = new FormAttachment(rdLong, 5, SWT.RIGHT);
        fdEdit.top = new FormAttachment(lbOffset, 10, SWT.BOTTOM);
        rdString.setLayoutData(fdEdit);

        btnTest = new Button(shell, SWT.PUSH);
        btnTest.setText("Test");
        fdEdit = new FormData();
        fdEdit.right = new FormAttachment(100, -5);
        fdEdit.top = new FormAttachment(rdString, 10, SWT.BOTTOM);
        btnTest.setLayoutData(fdEdit);

        Label lbData = new Label(shell, SWT.NONE);
        lbData.setText("Data");
        fdEdit = new FormData();
        fdEdit.left = new FormAttachment(0, 5);
        fdEdit.top = new FormAttachment(btnTest, 10, SWT.BOTTOM);
        lbData.setLayoutData(fdEdit);

        txtData = new Text(shell, SWT.BORDER);
        //txtData.setEditable(false);
        txtData.setEnabled(false);
        fdEdit = new FormData();
        fdEdit.left = new FormAttachment(lbData, 5, SWT.RIGHT);
        fdEdit.right = new FormAttachment(100, -5);
        fdEdit.top = new FormAttachment(btnTest, 10, SWT.BOTTOM);
        txtData.setLayoutData(fdEdit);

        rdByte.setSelection(true);

        addActionListener();
    }

    private void addActionListener()
    {
        btnBrowse.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                FileDialog fileDlg = new FileDialog(shell, SWT.OPEN);
                fileDlg.setText("Open Vdict Data");
                fileDlg.setFilterExtensions(new String[]{"*.data", "*.idx"});

                txtDataFilePath.setText(fileDlg.open());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent selectionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        btnTest.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                try {
                    doTest();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent selectionEvent) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }

    private void doTest() throws IOException
    {
        RandomAccessFile raf = null;
        try{
            if(!txtDataFilePath.getText().trim().equals(""))
            {
                File file = new File(txtDataFilePath.getText().trim());
                raf = new RandomAccessFile(file, "r");
                long pos = Long.parseLong(txtOffset.getText().trim());
                int length = 0;//Integer.parseInt(txtLength.getText().trim());

                raf.seek(pos);
                if(rdByte.getSelection())
                {
                    byte value = raf.readByte();
                    txtData.setText(value + "");
                }else if(rdShort.getSelection())
                {
                    short value = raf.readShort();
                    txtData.setText(value + "");
                }
                else if(rdInt.getSelection())
                {
                    int value = raf.readInt();
                    txtData.setText(value + "");
                }
                else if(rdLong.getSelection())
                {
                    long value;
                    value = raf.readLong();
                    //byte[] b = new byte[8];
                    //raf.read(b);
                    //value = VDictUtility.bytes2LongLittleEndian(b);
                    txtData.setText(value + "");
                }
                else if(rdString.getSelection())
                {
                    length = Integer.parseInt(txtLength.getText().trim());
                    byte[] buf = new byte[length];
                    int byteRead = raf.read(buf, 0, length);
                    String value = new String(buf, 0, byteRead, "utf-8");
                    txtData.setText(value + "");
                }
            }
        }
        finally
        {
            if(raf != null)
                raf.close();
        }
    }
}

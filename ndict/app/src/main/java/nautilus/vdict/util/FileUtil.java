package nautilus.vdict.util;

import android.content.Context;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by davu on 12/26/2015.
 */
public final class FileUtil {
    public static final String DATA_FILE = "vdict.data";
    public static final String INDEX_FILE = "vdict.idx";

    public static boolean checkDataExisted(Context context) {
        File dataFile = new File(context.getFilesDir(), DATA_FILE);
        File indexFile = new File(context.getFilesDir(), INDEX_FILE);

        return (dataFile.exists() && indexFile.exists());
    }

    public static boolean initDataFiles(Context context) {
        File dataFile = new File(context.getFilesDir(), DATA_FILE);
        File indexFile = new File(context.getFilesDir(), INDEX_FILE);
        byte[] buffer = new byte[512];
        InputStream assetReader = null;
        FileOutputStream outFile = null;
        int numRead;
        boolean result = true;
        try{
            //Data File
            assetReader = context.getAssets().open(DATA_FILE);
            outFile = new FileOutputStream(dataFile);
            while(assetReader.available() > 0) {
                numRead = assetReader.read(buffer);
                outFile.write(buffer, 0, numRead);
            }
            assetReader.close();
            outFile.flush();
            outFile.close();
//            assetReader = null;
//            outFile = null;

            //Index File
            assetReader = context.getAssets().open(INDEX_FILE);
            outFile = new FileOutputStream(indexFile);
            while(assetReader.available() > 0) {
                numRead = assetReader.read(buffer);
                outFile.write(buffer, 0, numRead);
            }
            assetReader.close();
            outFile.flush();
            outFile.close();

        }catch(IOException ex) {
            result = false;
        } finally {
            try {
                if(outFile != null)
                    outFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * this is for testing purpose
     */
    public static void readTestData(Context context) {
        InputStream assetReader = null;
        DataInputStream dos;
        try{
            //Data File
            assetReader = context.getAssets().open("test.data");
            dos = new DataInputStream(assetReader);
            short value1 = dos.readShort();
            short value2 = dos.readShort();
            android.util.Log.i("FileUtil", "The first value: " + value1);
            android.util.Log.i("FileUtil", "The next value: " + value2);
        }catch(IOException ex) {
            android.util.Log.e("FileUtil", ex.getMessage());
        } finally {
            try {
                if(assetReader != null)
                    assetReader.close();
            } catch (IOException e) {
            }
        }
    }
}

package no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.filemanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by espenjon on 24/01/2019.
 */

public class MetaDataFile {
    private File submissionFileName;
    private String metaDataFileName;


    public MetaDataFile(File submissionFile) {
        this.submissionFileName = submissionFileName;
        this.metaDataFileName = getMetaDataFileName(submissionFileName);
    }

    private String getMetaDataFileName(File submissionFileName) {
        StringBuilder text = new StringBuilder();
        String filename = submissionFileName.getAbsolutePath();
        String[] tokens = filename.split("\\.(?=[^\\.]+$)");
        return tokens[0] + ".metadata";
    }

    public boolean metaDataExcists () {
        return new File(metaDataFileName).exists();
    }

    public String readMetaData() throws IOException {
        StringBuilder metaData = new StringBuilder();
        if (metaDataExcists()) {
            BufferedReader br = new BufferedReader(new FileReader(metaDataFileName));
            String line;
            while ((line = br.readLine()) != null) {
                metaData.append(line);
                metaData.append('\n');
            }
            return metaData.toString();
        }
        return  null;
    }
}

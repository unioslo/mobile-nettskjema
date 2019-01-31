package no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.filemanagement;

import android.util.JsonReader;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

/**
 * Created by espenjon on 24/01/2019.
 */

public class MetaDataFile {
    private static final String extension = "queueTemp";
    private static final int daysToDelete = 90;

    private File submissionFileName;
    private File metaDataFile;
    private String metaDataFileName;


    public MetaDataFile(File submissionFile) {
        this.submissionFileName = submissionFile;
    }

    public MetaDataFile(File metadataFile, boolean b) {
        this.metaDataFile = metadataFile;
    }

    public String getMetaDataFileName() {
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
        if (metaDataFile != null) {
            BufferedReader br = new BufferedReader(new FileReader(this.metaDataFile));
            String line;
            while ((line = br.readLine()) != null) {
                metaData.append(line);
                metaData.append('\n');
            }
            return metaData.toString();
        }
        Log.d("Metadata manager", "File does not excist!");
        return  null;
    }

    public void readMetaDataToJson() throws IOException, JSONException {
        String m = readMetaData();
        JSONObject r = new JSONObject(m);


        System.out.println("---------->" + r.toString(2));
        System.out.println("------> READ: " + r.get("dateCreated"));
        String dateString = String.valueOf(r.get("dateCreated"));

        System.out.println(dateString);
        Long milli = Long.parseLong(dateString);
        System.out.println("--------> Date object " + new Date(milli).toString());

    }

    public String getSubmissionId() throws IOException, JSONException {
        String m = readMetaData();
        JSONObject r = new JSONObject(m);
        return String.valueOf(r.get("id"));
    }

    public boolean shouldDeleteFile() throws IOException, JSONException {
        String m = readMetaData();
        JSONObject r = new JSONObject(m);

        Long createdInMilliseconds = Long.parseLong(String.valueOf(r.get("dateCreated")));
        Date now = new Date();

        long diffInMilliseconds = Math.abs(createdInMilliseconds - now.getTime());
        long differenceInDays = diffInMilliseconds / (24 * 60 * 60 * 1000);
        return differenceInDays > daysToDelete;
    }
}

package angkush;

import java.io.File;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.transfer.Transfer;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import java.util.Arrays;
import java.util.List;

public class Multipart {
  public static TransferManager getTransferMgr(AmazonS3 s3) {
    long min_size = 52428800;
    return TransferManagerBuilder
                  .standard()
                  .withS3Client(s3)
                  .withMultipartUploadThreshold(min_size)
                  .withMinimumUploadPartSize(min_size)
                  .build();
  }
  
  public static void upload(String file_path, String bucket_name, String key_name, AmazonS3 s3) {
        try {
          TransferManager transferMgr = getTransferMgr(s3);
          
          // file_path and key_name need to be provided properly
          Transfer upload = transferMgr.upload(bucket_name, key_name, new File(file_path));
          
          System.out.println("File upload started");
          
          Progress.showTransferProgress(upload);
          Progress.waitForCompletion(upload);
          
          System.out.println("File upload completed");
          transferMgr.shutdownNow();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
  
  public static void copy(String from_bucket, String from_key_name, String to_bucket, String to_key_name, AmazonS3 s3) {
        try {
          TransferManager transferMgr = getTransferMgr(s3);
          
          // file_path and key_name need to be provided properly
          Transfer copy = transferMgr.copy(from_bucket, from_key_name, to_bucket, to_key_name);
          
          System.out.println("File copy started");
          
          Progress.showTransferProgress(copy);
          Progress.waitForCompletion(copy);
          
          System.out.println("File copy completed");
          transferMgr.shutdownNow();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
  
  public static void download(String bucket_name, String key_name, AmazonS3 s3) {
        try {
          TransferManager transferMgr = getTransferMgr(s3);
          
          // file_path and key_name need to be provided properly
          File newFile = new File("D:\\java\\Angkush\\download_from_s3.txt");
          Transfer download = transferMgr.download(bucket_name, key_name, newFile);
          
          System.out.println("File download started");
          
          Progress.showTransferProgress(download);
          Progress.waitForCompletion(download);
          
          System.out.println("File download completed");
          transferMgr.shutdownNow();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
  
  public static void uploadDirectoryFiles(String bucket_name, AmazonS3 s3) {
    try {
      TransferManager transferMgr = getTransferMgr(s3);

      // file_path and key_name need to be provided properly
      File directory = new File("D:\\java\\Angkush\\angkush-directory");
      List<File> fileList = Arrays.asList(directory.listFiles());
      Transfer uploadedDirectoryFiles = transferMgr.uploadFileList(bucket_name, "angkush-directory", directory, fileList);

      System.out.println("Directory upload started");
          
      Progress.showTransferProgress(uploadedDirectoryFiles);
      Progress.waitForCompletion(uploadedDirectoryFiles);
          
      System.out.println("Directory upload completed");
      transferMgr.shutdownNow();
    }
    catch (Exception e) {
        System.err.println(e.getMessage());
        System.exit(1);
    }
  }
  
  public static void downloadDirectoryFiles(String bucket_name, AmazonS3 s3) {
    try {
      TransferManager transferMgr = getTransferMgr(s3);

      // file_path and key_name need to be provided properly
      File directory = new File("D:\\java\\Angkush\\downloads-from-angkush-directory");
      Transfer downloadedDirectoryFiles = transferMgr.downloadDirectory(bucket_name, "angkush-directory", directory);

      System.out.println("Directory download started");
          
      Progress.showTransferProgress(downloadedDirectoryFiles);
      Progress.waitForCompletion(downloadedDirectoryFiles);
          
      System.out.println("Directory download completed");
      transferMgr.shutdownNow();
    }
    catch (Exception e) {
        System.err.println(e.getMessage());
        System.exit(1);
    }
  }
}

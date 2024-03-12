package angkush;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Basics {
  static AmazonS3 getS3Client() { 
        AmazonS3 s3; 
        ClientConfiguration clientConfig = new ClientConfiguration();
        BasicAWSCredentials credentials;
        credentials = new BasicAWSCredentials(key, secret);
        
        clientConfig.setMaxErrorRetry(4);
        s3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                                "https://127.0.0.1/", region))
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
               // .withRegion(region).withClientConfiguration(clientConfig)
               // .withEndpointConfiguration(null)
               .build();
        
        return s3;
  }
  
  static void uploadFile(String file_path, String bucket_name, String key_name, AmazonS3 s3) {
    System.out.format("Uploading %s to S3 bucket %s...\n", file_path, bucket_name);
    try {
        s3.putObject(bucket_name, key_name, new File(file_path));
        System.out.println("Uploaded successfully");
    } catch (AmazonServiceException e) {
        System.err.println(e.getErrorMessage());
        System.exit(1);
    }
  }
  
  static void listFiles(String file_path, String bucket_name, String key_name, AmazonS3 s3) {
    System.out.format("Objects in S3 bucket %s:\n", bucket_name);
    ListObjectsV2Result result = s3.listObjectsV2(bucket_name);
    List<S3ObjectSummary> objects = result.getObjectSummaries();
    for (S3ObjectSummary os : objects) {
        System.out.println("* " + os.getKey());
    }
  }
  
  static void deleteFile(String file_path, String bucket_name, String key_name, AmazonS3 s3) {
    System.out.format("Deleting an object from current bucket %s:\n", bucket_name);
    String object_key = "angkush.png";
    try {
        s3.deleteObject(bucket_name, object_key);
        System.out.println("Deleted successfully");
    } catch (AmazonServiceException e) {
        System.err.println(e.getErrorMessage());
        System.exit(1);
    }
  }
  
  static void downloadFile(String file_path, String bucket_name, String key_name, AmazonS3 s3) {
    System.out.format("Downloading %s from current bucket %s:\n", key_name, bucket_name);
    try {
      S3Object o = s3.getObject(bucket_name, key_name);
      S3ObjectInputStream s3is = o.getObjectContent();
      FileOutputStream fos = new FileOutputStream(new File("download_from_s3.txt"));
//      FileOutputStream fos = new FileOutputStream(new File(key_name));
      byte[] read_buf = new byte[1024];
      int read_len = 0;
      
      while ((read_len = s3is.read(read_buf)) > 0) {
          fos.write(read_buf, 0, read_len);
      }
      
       s3is.close();
       fos.close();
    } catch (AmazonServiceException e) {
        System.err.println(e.getErrorMessage());
        System.exit(1);
    } catch (FileNotFoundException e) {
        System.err.println(e.getMessage());
        System.exit(1);
    } catch (IOException e) {
        System.err.println(e.getMessage());
        System.exit(1);
    }
  }
  
  public static void deleteMultipleFiles(String file_path, String bucket_name, String[] key_names, AmazonS3 s3) {
        try {
            DeleteObjectsRequest dor = new DeleteObjectsRequest(bucket_name).withKeys(key_names);
            s3.deleteObjects(dor);
        }
        catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
    }
}

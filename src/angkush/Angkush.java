package angkush;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.SDKGlobalConfiguration;

import java.security.cert.X509Certificate;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import static angkush.Basics.listFiles;
import static angkush.Basics.uploadFile;
import static angkush.Basics.deleteFile;
import static angkush.Basics.downloadFile;

// start server: java -jar Mediator_Web.jar

public class Angkush {
  
  private static final class NullX509TrustManager implements X509TrustManager {
  
     @Override        
     public X509Certificate[] getAcceptedIssuers() {
        return null;
     }

     @Override
     public void checkClientTrusted(X509Certificate[] certs, String authType) {}

     @Override
     public void checkServerTrusted(X509Certificate[] certs, String authType) {}
  }
  
  static {
      System.setProperty(
              SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY, "true"
      );
      disableSslVerification();
  }
  
  static void disableSslVerification() {
    try {
      // Create a trust manager that does not validate certificate chains
      TrustManager[] trustAllCerts = new TrustManager[] {
          new NullX509TrustManager()
      };

      // Install the all-trusting trust manager
      SSLContext sc = SSLContext.getInstance("SSL");
      sc.init(null, trustAllCerts, new java.security.SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(
              sc.getSocketFactory());

      // Create all-trusting host name verifier
      HostnameVerifier allHostsValid = new HostnameVerifier() {
          @Override
          public boolean verify(String hostname, SSLSession session) {
              return true;
          }
      };

      // Install the all-trusting host verifier
      HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
      } catch (KeyManagementException | NoSuchAlgorithmException e) {
          throw new RuntimeException(e);
      }
  }

  public static void main(String[] args) {
    String file_path = "D:\\java\\Angkush\\fifty_mb.txt"; 
    String bucket_name = "saurabhi-interns";
    String key_name = "fifty_mb.txt"; // name of the file
       
    final AmazonS3 s3 = Basics.getS3Client();
    /* basic uploads and copies -- start */
    
    // uploadFile(file_path, bucket_name, key_name, s3);
    // listFiles(file_path, bucket_name, key_name, s3);
    // downloadFile(file_path, bucket_name, key_name, s3);
    
    // -- Not required --
    // deleteFile(file_path, bucket_name, key_name, s3);
    // deleteMultipleFiles(file_path, bucket_name, key_name, s3);
    // -- Not required --
    
    /* basic uploads and copies -- end */
    
    /* multipart uploads and copies -- start */
    // Multipart.upload(file_path, bucket_name, key_name, s3);
    
    // String to_bucket = "saurabhi-misc";
    // String to_key_name = "angkush-large-file-copy.txt";
    // Multipart.copy(bucket_name, key_name, to_bucket, to_key_name, s3);
    
    // Multipart.download(bucket_name, key_name, s3);
    // Multipart.uploadDirectoryFiles(bucket_name, s3);
    // Multipart.downloadDirectoryFiles(bucket_name, s3);
    /* multipart uploads and copies -- end */
  }
  
}

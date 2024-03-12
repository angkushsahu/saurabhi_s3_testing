package angkush;

import com.amazonaws.services.s3.transfer.Transfer;
import com.amazonaws.services.s3.transfer.Transfer.TransferState;
import com.amazonaws.services.s3.transfer.TransferProgress;

public class Progress {  
  public static void waitForCompletion(Transfer transer) {
    try {
      transer.waitForCompletion();
    } catch(Exception e) {
      System.out.println("Amazon service error " + e.getMessage());
      System.exit(1);
    }
  }
  
  public static void eraseProgressBar() {
    final String erase_bar = "\b\b\b\b\b\b\b\b\b\b";
    System.out.format(erase_bar);
  }
  
  public static void printProgressBar(double pct) {
    final int bar_size = 10;
    final String empty_bar = "          ";
    final String filled_bar = "##########";
    int amt_full = (int)(bar_size * (pct / 100.0));
    System.out.format("[%s%s]",
            filled_bar.substring(0, amt_full),
            empty_bar.substring(0, bar_size - amt_full));
  }
  
  public static void showTransferProgress(Transfer transfer) {
    System.out.println(transfer.getDescription());
    printProgressBar(0.0);
    
    do {
      try {
        Thread.sleep(100);
      } catch(InterruptedException e) {
        return;
      }
      
      TransferProgress progress = transfer.getProgress();
      long so_far = progress.getBytesTransferred();
      long total = progress.getTotalBytesToTransfer();
      double pct = progress.getPercentTransferred();
      
      eraseProgressBar();
      printProgressBar(pct);
    } while (!transfer.isDone());
    
    TransferState transfer_state = transfer.getState();
    System.out.println(": " + transfer_state);
  }
}

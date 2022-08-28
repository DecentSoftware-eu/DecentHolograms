package eu.decentsoftware.holograms.plugin.convertors;

public class ConvertorResult {
    
    private int successCount;
    private int skippedCount;
    private int failedCount;

    public ConvertorResult() {
        this(0, 0, 0);
    }
    
    public ConvertorResult(int successCount, int skippedCount, int failedCount) {
        this.successCount = successCount;
        this.skippedCount = skippedCount;
        this.failedCount = failedCount;
    }
    
    public static ConvertorResult createFailed() {
        return new ConvertorResult();
    }

    public boolean isSuccessful() {
        return successCount > 0 || getTotalCount() == 0;
    }

    public int getTotalCount() {
        return successCount + skippedCount + failedCount;
    }
    
    public int getSuccessCount() {
        return successCount;
    }
    
    public int getSkippedCount() {
        return skippedCount;
    }
    
    public int getFailedCount() {
        return failedCount;
    }
    
    public void addSuccess() {
        successCount++;
    }
    
    public void addSkipped() {
        skippedCount++;
    }
    
    public void addFailed() {
        failedCount++;
    }
}

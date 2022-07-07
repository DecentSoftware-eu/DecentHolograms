package eu.decentsoftware.holograms.plugin.convertors;

public class ConvertorResult {
    
    private int totalCount = 0;
    private int successCount = 0;
    private int skippedCount = 0;
    private int failedCount = 0;

    public ConvertorResult() {
        this(0, 0, 0, 0);
    }
    
    public ConvertorResult(int totalCount, int successCount, int skippedCount, int failedCount) {
        this.totalCount = totalCount;
        this.successCount = successCount;
        this.skippedCount = skippedCount;
        this.failedCount = failedCount;
    }
    
    public static ConvertorResult createFailed() {
        return new ConvertorResult();
    }

    public boolean isSuccessful() {
        return successCount > 0;
    }

    public int getTotalCount() {
        return totalCount;
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
        totalCount++;
        successCount++;
    }
    
    public void addSkipped() {
        totalCount++;
        skippedCount++;
    }
    
    public void addFailed() {
        totalCount++;
        failedCount++;
    }
}

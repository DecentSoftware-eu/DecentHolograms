package eu.decentsoftware.holograms.plugin.convertors;

import lombok.Getter;

@Getter
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

    public int getTotalCount() {
        return successCount + skippedCount + failedCount;
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

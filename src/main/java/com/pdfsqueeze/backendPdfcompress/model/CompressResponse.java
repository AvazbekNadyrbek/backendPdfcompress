package com.pdfsqueeze.backendPdfcompress.model;

public class CompressResponse {

    private long originalSize;
    private long compressedSize;
    private long savedPercent;

    public CompressResponse(long originalSize, long compressedSize, long savedPercent) {
        this.originalSize = originalSize;
        this.compressedSize = compressedSize;
        this.savedPercent = (int) ((1 - (double) compressedSize / originalSize) * 100);
    }

    // Getters
    public long getOriginalSize() { return originalSize; }
    public long getCompressedSize() { return compressedSize; }
    public long getSavedPercent() { return savedPercent; }
}

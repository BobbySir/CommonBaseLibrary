package com.bean;

/**
 * Created by：bobby on 2021-08-21 16:28.
 * Describe：
 */
public class SDCardBean {
    public boolean isExist;
    public long totalBlocks;     //block总数
    public long freeBlocks;
    public long availableBlocks; //可用的block数目
    public long blockByteSize;   //每个block 占字节数
    public long totalBytes;
    public long freeBytes;
    public long availableBytes;

    public String toString() {
        return "isExist=" + this.isExist + "\ntotalBlocks=" + this.totalBlocks + "\nfreeBlocks=" + this.freeBlocks + "\navailableBlocks=" + this.availableBlocks + "\nblockByteSize=" + this.blockByteSize + "\ntotalBytes=" + this.totalBytes + "\nfreeBytes=" + this.freeBytes + "\navailableBytes=" + this.availableBytes;
    }
} 
package com.example.coursework.bucket;

public enum BucketName {
    PROFILE_IMAGE("internships-image");
    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}

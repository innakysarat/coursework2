package com.example.coursework.filestore;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
public class FileStore {
    private final AmazonS3 s3;

    @Autowired
    public FileStore(AmazonS3 s3) {
        this.s3 = s3;
    }

    public void save(String path, String fileName,
                     Optional<Map<String, String>> metaData,
                     InputStream inputStream) {
        ObjectMetadata metadata = new ObjectMetadata();
        metaData.ifPresent(map -> {
            if (!map.isEmpty()) {
                map.forEach(metadata::addUserMetadata); // (key, value) -> metadata.addUserMetadata(key, value);
            }
        });
        try {
            s3.putObject(path, fileName, inputStream, metadata);
        } catch (AmazonServiceException amazonServiceException) {
            throw new IllegalStateException("Failed to store file to s3", amazonServiceException);
        }
    }


    public byte[] download(String path, String key) {
        try {
            S3Object s3Object = s3.getObject(path, key);
            S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
            byte[] content = IOUtils.toByteArray(s3ObjectInputStream);
            s3Object.close();
            return content;
        } catch (AmazonServiceException | IOException ex) {
            throw new IllegalStateException("Failed to download file to s3", ex);
        }
    }

    public void deleteFile(String path, String key) {
        final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(path, key);
        s3.deleteObject(deleteObjectRequest);
    }
}

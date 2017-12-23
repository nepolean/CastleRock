package com.subsede.util.controller;

import org.springframework.web.multipart.MultipartFile;

public class FileController {

  StorageService storageSvc;

  public String handleUploadFile(MultipartFile file) {
    if (file.isEmpty())
      return null;
    return storageSvc.store(file);
  }

}

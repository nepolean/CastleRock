package com.subsede.util.controller;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

  String store(MultipartFile file);

  Resource load(String path);

}

package com.subsede.amc.catalog.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.subsede.amc.catalog.model.BaseMasterEntity;
import com.subsede.user.model.user.User;
import com.subsede.user.services.user.UserService;

@Service
public class GenericFCRUDService {

  @Autowired
  MongoTemplate template;
  @Autowired
  UserService userRepo;

  public <E extends BaseMasterEntity> E create(E entity, String userid) {
    User user = userRepo.findByUsername(userid);
    entity.setCreatedOn(new Date());
    entity.setCreatedBy(user.getEmail());
    this.template.save(entity);
    return entity;
  }

  public <E extends BaseMasterEntity> E update(E entity, String userid) {
    User user = userRepo.findByUsername(userid);
    entity.setModifiedBy(user.getEmail());
    entity.setLastModified(new Date());
    template.save(entity);
    return entity;
  }

  public <E extends BaseMasterEntity> void delete(E entity, String userid) {
    entity.markForDeletion();
    update(entity, userid);
  }

  public <E extends BaseMasterEntity> List<E> findAll(Class<E> class1) {
    return this.template.findAll(class1);
  }

  public <E extends BaseMasterEntity> E findBy(Object id, Class<E> clazz) {
    return this.template.findById(id, clazz);
  }

  public <E> void removeAll(List<E> objects) {
    for (E obj : objects)
      this.template.remove(obj);
  }

  public void removeAll(Class<?> clz) {
    this.template.dropCollection(clz);
  }

}

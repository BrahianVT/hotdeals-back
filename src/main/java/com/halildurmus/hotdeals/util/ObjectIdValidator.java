package com.halildurmus.hotdeals.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.bson.types.ObjectId;

public class ObjectIdValidator implements ConstraintValidator<IsObjectId, String> {

  @Override
  public void initialize(IsObjectId objectId) {}

  @Override
  public boolean isValid(String objectId, ConstraintValidatorContext cxt) {
    return ObjectId.isValid(objectId);
  }
}

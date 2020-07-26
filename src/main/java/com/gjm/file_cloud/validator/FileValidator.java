package com.gjm.file_cloud.validator;

import com.gjm.file_cloud.entity.File;
import com.gjm.file_cloud.exceptions.file_cloud_runtime_exception.FileValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

@Component
public class FileValidator {
    private Validator validator;

    @Autowired
    public FileValidator(@Qualifier("mvcValidator") Validator validator) {
        this.validator = validator;
    }

    public void validateFileEntity(File fileEntity) {
        Errors errors = new BeanPropertyBindingResult(fileEntity, "fileEntity");
        validator.validate(fileEntity, errors);
        if(errors.hasErrors()) {
            StringBuilder exceptionErrorString = new StringBuilder();

            for(FieldError error : errors.getFieldErrors()) {
                exceptionErrorString.append(error.getField())
                        .append(": ").append(error.getDefaultMessage())
                        .append("\n");
            }

            throw new FileValidationException(exceptionErrorString.toString());
        }
    }
}

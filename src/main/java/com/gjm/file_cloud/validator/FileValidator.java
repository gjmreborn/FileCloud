package com.gjm.file_cloud.validator;

import com.gjm.file_cloud.entity.File;
import com.gjm.file_cloud.exceptions.file_cloud_runtime_exception.FileValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import java.util.Locale;

@Component
public class FileValidator {
    private Validator validator;
    private MessageSource messageSource;

    @Autowired
    public FileValidator(@Qualifier("mvcValidator") Validator validator, MessageSource messageSource) {
        this.validator = validator;
        this.messageSource = messageSource;
    }

    public void validateFileEntity(File fileEntity, Locale locale) {
        Errors errors = new BeanPropertyBindingResult(fileEntity, "fileEntity");
        validator.validate(fileEntity, errors);
        if(errors.hasErrors()) {
            StringBuilder exceptionErrorString = new StringBuilder();

            for(ObjectError error : errors.getAllErrors()) {
                String messageProperty = error.getDefaultMessage();

                exceptionErrorString.append(error.getObjectName())
                        .append(": ").append(messageSource.getMessage(messageProperty, new Object[0], locale))
                        .append("\n");
            }

            throw new FileValidationException(exceptionErrorString.toString());
        }
    }
}

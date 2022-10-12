package com.example.SpringBatchTutorial.job.ValidatedParam.Vallidator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

public class FileParamValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        String fileName = parameters.getString("fileName");
        /* file확장자가 csv가 맞는지? 확인 */
        if(!StringUtils.endsWithIgnoreCase(fileName,"csv")){
            throw new JobParametersInvalidException("This is not csv file!!");
        }
    }
}

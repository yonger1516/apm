package com.iyonger.apm.web.validator;


import com.iyonger.apm.web.model.Project;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by fuyong on 7/2/15.
 */
@Service
public class ProjectValidator implements Validator {

	@Override
	public boolean supports(Class<?> aClass) {
		return false;
	}

	@Override
	public void validate(Object o, Errors errors) {
		if (!Project.class.isAssignableFrom(o.getClass())) {
			return;
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required.project.name", "Field host is required.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "required.project.desc",
				"Field username is required.");

	}
}

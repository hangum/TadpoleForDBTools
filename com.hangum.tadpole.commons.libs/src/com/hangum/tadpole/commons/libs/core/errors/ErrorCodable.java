package com.hangum.tadpole.commons.libs.core.errors;

import java.util.logging.Level;

public interface ErrorCodable {
	String getCode();
	Level  getLevel();
	String getMessage(String...args);
}

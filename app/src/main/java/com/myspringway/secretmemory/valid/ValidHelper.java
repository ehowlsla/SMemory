package com.myspringway.secretmemory.valid;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidHelper {
	
	public static boolean checkEmail (String email){
		String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(email);
		boolean isNormal = m.matches();
		return isNormal;
	}

}

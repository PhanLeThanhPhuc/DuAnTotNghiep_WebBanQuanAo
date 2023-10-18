package com.poly.elnr.utils;

import java.security.SecureRandom;
import java.util.Random;

public class RamDomNameUtils {

	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	private static final int NAME_LENGTH = 8; 

	private static final Random random = new SecureRandom();

	public static String generateRandomPassword() {
		StringBuilder name = new StringBuilder(NAME_LENGTH);

		for (int i = 0; i < NAME_LENGTH; i++) {
			int randomIndex = random.nextInt(CHARACTERS.length());
			char randomChar = CHARACTERS.charAt(randomIndex);
			name.append(randomChar);
		}

		return name.toString();
	}
}

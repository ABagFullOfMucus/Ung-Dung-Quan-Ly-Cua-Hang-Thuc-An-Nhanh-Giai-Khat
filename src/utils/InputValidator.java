package utils;

import java.util.Scanner;

public class InputValidator {

	// Đọc số nguyên an toàn từ bàn phím
	public static int getInt(Scanner sc, String prompt) {
		System.out.print(prompt);
		
		while (!sc.hasNextInt()) {
			System.out.println("Vui lòng nhập một số nguyên hợp lệ!");
			sc.next();
			System.out.print(prompt);
		}
		
		int value = sc.nextInt();
		sc.nextLine();
		return value;
	}
	
	// Đọc số thực an toàn từ bàn phím
	public static double getDouble(Scanner sc, String prompt) {
		System.out.print(prompt);
		
		while (!sc.hasNextDouble()) {
			System.out.println("Vui lòng nhập một số hợp lệ!");
			sc.next();
			System.out.print(prompt);
		}
		
		double value = sc.nextDouble();
		sc.nextLine();
		return value;
	}
	
	// Đọc chuỗi không được để trống
	public static String getNonEmptyString(Scanner sc, String prompt) {
		String value;
		
		do {
			System.out.print(prompt);
			value = sc.nextLine().trim();
			
			if (value.isEmpty()) {
				System.out.println("Không được để trống!");
			}
		} while (value.isEmpty());
		
		return value;
	}
	
	// Đọc true/false
	public static boolean getBoolean(Scanner sc, String prompt) {
		System.out.print(prompt);
		String input = sc.nextLine().trim().toLowerCase();
		
		return input.equals("true") || input.equals("yes") || input.equals("y") || input.equals("co") || input.equals("có");
	}
}

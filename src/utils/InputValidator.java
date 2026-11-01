package utils;

import java.util.Scanner;

public class InputValidator {
	private static final Scanner SC = new Scanner(System.in);

	// Đọc số nguyên an toàn từ bàn phím
	public static int getInt(String prompt) {
		System.out.print(prompt);

		while (!SC.hasNextInt()) {
			System.out.println("Vui lòng nhập một số nguyên hợp lệ!");
			SC.next();
			System.out.print(prompt);
		}

		int value = SC.nextInt();
		SC.nextLine();
		return value;
	}

	// Đọc số thực an toàn từ bàn phím
	public static double getDouble(String prompt) {
		System.out.print(prompt);

		while (!SC.hasNextDouble()) {
			System.out.println("Vui lòng nhập một số hợp lệ!");
			SC.next();
			System.out.print(prompt);
		}

		double value = SC.nextDouble();
		SC.nextLine();
		return value;
	}

	// Đọc chuỗi không được để trống
	public static String getNonEmptyString(String prompt) {
		String value;

		do {
			System.out.print(prompt);
			value = SC.nextLine().trim();

			if (value.isEmpty()) {
				System.out.println("Không được để trống!");
			}
		} while (value.isEmpty());

		return value;
	}

	// Đọc true/false
	public static boolean getBoolean(String prompt) {
		System.out.print(prompt);
		String input = SC.nextLine().trim().toLowerCase();

		return input.equals("true") || input.equals("yes") || input.equals("y") || input.equals("co") || input.equals("có");
	}
}

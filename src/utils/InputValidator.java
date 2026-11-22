package utils;

import java.util.Scanner;

public class InputValidator {
	private static final Scanner SC = new Scanner(System.in);

	public static int getInt(String prompt) {
		System.out.print(prompt);

		while (!SC.hasNextInt()) {
			if (!SC.hasNext()) {
				System.out.println("Luồng nhập liệu kết thúc.");
				System.exit(0);
			}
			System.out.println("Vui lòng nhập một số nguyên hợp lệ!");
			SC.next();
			System.out.print(prompt);
		}

		int value = SC.nextInt();
		SC.nextLine();
		return value;
	}

	public static int getPositiveInt(String prompt) {
		int value;
		do {
			value = getInt(prompt);
			if (value <= 0) {
				System.out.println("Giá trị phải lớn hơn 0!");
			}
		} while (value <= 0);
		return value;
	}

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

	public static double getPositiveDouble(String prompt) {
		double value;
		do {
			value = getDouble(prompt);
			if (value <= 0) {
				System.out.println("Giá trị phải lớn hơn 0!");
			}
		} while (value <= 0);
		return value;
	}

	public static String getString(String prompt) {
		System.out.print(prompt);
		return SC.nextLine().trim();
	}

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

	public static boolean getBoolean(String prompt) {
		System.out.print(prompt);
		String input = SC.nextLine().trim().toLowerCase();

		return input.equals("true")
				|| input.equals("yes")
				|| input.equals("y")
				|| input.equals("co")
				|| input.equals("có");
	}
}
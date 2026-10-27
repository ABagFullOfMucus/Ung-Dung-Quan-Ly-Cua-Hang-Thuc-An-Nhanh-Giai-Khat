package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import interfaces.IStorable;

public class FileHandler {

	// Ghi danh sách đối tượng IStorable vào file (mỗi đối tượng một dòng)
	public static boolean saveToFile(String fileName, IStorable[] items, int count) {
		File file = new File(fileName);
		
		File parent = file.getParentFile();
		if (parent != null && !parent.exists()) {
			parent.mkdirs();
		}
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			for (int i = 0; i < count; i++) {
				if (items[i] == null) continue;
				
				writer.write(items[i].toFileString());
				writer.newLine();
			}
			
			return true;
		} catch (IOException e) {
			System.out.println("Lỗi khi ghi file " + fileName + ": " + e.getMessage());
			return false;
		}
	}
	
	// Đếm số dòng hợp lệ (không trống) trong file, trả về 0 nếu file chưa tồn tại
	public static int countLines(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			return 0;
		}
		
		int count = 0;
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (!line.trim().isEmpty()) {
					count += 1;
				}
			}
		} catch (IOException e) {
			System.out.println("Lỗi khi đọc file " + fileName + ": " + e.getMessage());
			return 0;
		}
		
		return count;
	}
	
	// Đọc toàn bộ dòng trong file, trả về mảng rỗng nếu file chưa tồn tại
	public static String[] readLines(String fileName) {
		int count = countLines(fileName);
		String[] lines = new String[count];
		
		if (count == 0) {
			return lines;
		}
		
		File file = new File(fileName);
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			int index = 0;
			while ((line = reader.readLine()) != null) {
				if (!line.trim().isEmpty()) {
					lines[index] = line.trim();
					index += 1;
				}
			}
		} catch (IOException e) {
			System.out.println("Lỗi khi đọc file " + fileName + ": " + e.getMessage());
		}
		
		return lines;
	}
}

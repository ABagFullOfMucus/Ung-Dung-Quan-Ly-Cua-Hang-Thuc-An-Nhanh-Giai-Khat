package interfaces;


public interface IStorable {
	String toFileString();
	void fromFileString(String line);
}

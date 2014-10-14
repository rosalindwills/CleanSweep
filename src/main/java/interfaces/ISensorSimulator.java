package interfaces;

public interface ISensorSimulator {
	ICell readCell(int floorLevel, int x, int y);
	void cleanCell(int floorLevel, int x, int y);
	void importXml(String path) throws Exception;
}

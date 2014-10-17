package interfaces;

public interface IFloor {
	ICell getCell(int x, int y);
	int getLevel();
	void addCell(ICell cell);
	void cleanCell(int x, int y);
}

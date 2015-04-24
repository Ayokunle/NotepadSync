 package shopping.list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import java.io.Serializable;
import java.util.HashSet;

public class Splash extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		SudokuField sf = new SudokuField(3);
	    long startTime = System.currentTimeMillis();
	    sf.generateFullField(1, 1);
	    long endTime = System.currentTimeMillis();
	    long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
	    sf.print();
	    System.out.println("Duration: " + duration);
	    
		Thread t = new Thread(){
			public void run(){
				try {
					sleep(3000);
			
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					Intent startNewActivityOpen = new Intent(Splash.this, Home.class);
					startActivityForResult(startNewActivityOpen, 0);
				}
			}
		};
		t.start();
	}
	
	public static class SudokuCell implements Serializable {

	    private int value;
	    private boolean filled;
	    private HashSet<Integer> tried;

	    public SudokuCell() {
	        filled = false;
	        tried = new HashSet();
	    }

	    public boolean isFilled() {
	        return filled;
	    }

	    public int get() {
	        return value;
	    }

	    public void set(final int number) {
	        filled = true;
	        value = number;
	        tried.add(number);
	    }

	    public void clear() {
	        value = 0;
	        filled = false;
	    }

	    public void reset() {
	        clear();
	        tried.clear();
	    }

	    public void show() {
	        filled = true;
	    }

	    public void hide() {
	        filled = false;
	    }

	    public boolean isTried(final int number) {
	        return tried.contains(number);
	    }

	    public void tryNumber(final int number) {
	        tried.add(number);
	    }

	    public int numberOfTried() {
	        return tried.size();
	    }
	 }

	public static class Index{
	    int i;
	    int j;
	    Index(int r, int c){
	        i =r;
	        j = c;
	    }
	}
	public static class SudokuField implements Serializable {

	    private final int blockSize;
	    private final int fieldSize;
	    private SudokuCell[][] field;

	    public SudokuField(final int blocks) {
	        blockSize = blocks;
	        fieldSize = blockSize * blockSize;
	        field = new SudokuCell[fieldSize][fieldSize];
	        for (int i = 0; i < fieldSize; ++i) {
	            for (int j = 0; j < fieldSize; ++j) {
	                field[i][j] = new SudokuCell();
	            }
	        }
	    }

	    public int blockSize() {
	        return blockSize;
	    }

	    public int fieldSize() {
	        return fieldSize;
	    }

	    public int variantsPerCell() {
	        return fieldSize;
	    }

	    public int numberOfCells() {
	        return fieldSize * fieldSize;
	    }

	    public void clear(final int row, final int column) {
	        field[row - 1][column - 1].clear();
	    }

	    public void clearAllCells() {
	        for (int i = 0; i < fieldSize; ++i) {
	            for (int j = 0; j < fieldSize; ++j) {
	                field[i][j].clear();
	            }
	        }
	    }

	    public void reset(final int row, final int column) {
	        field[row - 1][column - 1].reset();
	    }

	    public void resetAllCells() {
	        for (int i = 0; i < fieldSize; ++i) {
	            for (int j = 0; j < fieldSize; ++j) {
	                field[i][j].reset();
	            }
	        }
	    }

	    public boolean isFilled(final int row, final int column) {
	        return field[row - 1][column - 1].isFilled();
	    }

	    public boolean allCellsFilled() {
	        for (int i = 0; i < fieldSize; ++i) {
	            for (int j = 0; j < fieldSize; ++j) {
	                if (!field[i][j].isFilled()) {
	                    return false;
	                }
	            }
	        }
	        return true;
	    }

	    public int numberOfFilledCells() {
	        int filled = 0;
	        for (int i = 1; i <= fieldSize; ++i) {
	            for (int j = 1; j <= fieldSize; ++j) {
	                if (isFilled(i, j)) {
	                    ++filled;
	                }
	            }
	        }
	        return filled;
	    }

	    public int numberOfHiddenCells() {
	        return numberOfCells() - numberOfFilledCells();
	    }

	    public int get(final int row, final int column) {
	        return field[row - 1][column - 1].get();
	    }

	    public void set(final int number, final int row, final int column) {
	        field[row - 1][column - 1].set(number);
	    }

	    public void hide(final int row, final int column) {
	        field[row - 1][column - 1].hide();
	    }

	    public void show(final int row, final int column) {
	        field[row - 1][column - 1].show();
	    }

	    public void tryNumber(final int number, final int row, final int column) {
	        field[row - 1][column - 1].tryNumber(number);
	    }

	    public boolean numberHasBeenTried(final int number, final int row, final int column) {
	        return field[row - 1][column - 1].isTried(number);
	    }

	    public int numberOfTriedNumbers(final int row, final int column) {
	        return field[row - 1][column - 1].numberOfTried();
	    }

	    public boolean checkNumberBox(final int number, final int row, final int column) {
	        int r = row, c = column;
	        if (r % blockSize == 0) {
	            r -= blockSize - 1;
	        } else {
	            r = (r / blockSize) * blockSize + 1;
	        }
	        if (c % blockSize == 0) {
	            c -= blockSize - 1;
	        } else {
	            c = (c / blockSize) * blockSize + 1;
	        }
	        for (int i = r; i < r + blockSize; ++i) {
	            for (int j = c; j < c + blockSize; ++j) {
	                if (field[i - 1][j - 1].isFilled() && (field[i - 1][j - 1].get() == number)) {
	                    return false;
	                }
	            }
	        }
	        return true;
	    }

	    public boolean checkNumberRow(final int number, final int row) {
	        for (int i = 0; i < fieldSize; ++i) {
	            if (field[row - 1][i].isFilled() && field[row - 1][i].get() == number) {
	                return false;
	            }
	        }
	        return true;
	    }

	    public boolean checkNumberColumn(final int number, final int column) {
	        for (int i = 0; i < fieldSize; ++i) {
	            if (field[i][column - 1].isFilled() && field[i][column - 1].get() == number) {
	                return false;
	            }
	        }
	        return true;
	    }

	    public boolean checkNumberField(final int number, final int row, final int column) {
	        return (checkNumberBox(number, row, column)
	                && checkNumberRow(number, row)
	                && checkNumberColumn(number, column));
	    }

	    public int numberOfPossibleVariants(final int row, final int column) {
	        int result = 0;
	        for (int i = 1; i <= fieldSize; ++i) {
	            if (checkNumberField(i, row, column)) {
	                ++result;
	            }
	        }
	        return result;
	    }

	    public boolean isCorrect() {
	        for (int i = 0; i < fieldSize; ++i) {
	            for (int j = 0; j < fieldSize; ++j) {
	                if (field[i][j].isFilled()) {
	                    int value = field[i][j].get();
	                    field[i][j].hide();
	                    boolean correct = checkNumberField(value, i + 1, j + 1);
	                    field[i][j].show();
	                    if (!correct) {
	                        return false;
	                    }
	                }
	            }
	        }
	        return true;
	    }

	    public Index nextCell(final int row, final int column) {
	        int r = row, c = column;
	        if (c < fieldSize) {
	            ++c;
	        } else {
	            c = 1;
	            ++r;
	        }
	        return new Index(r, c);
	    }

	    public Index cellWithMinVariants() {
	        int r = 1, c = 1, min = 9;
	        for (int i = 1; i <= fieldSize; ++i) {
	            for (int j = 1; j <= fieldSize; ++j) {
	                if (!field[i - 1][j - 1].isFilled()) {
	                    if (numberOfPossibleVariants(i, j) < min) {
	                        min = numberOfPossibleVariants(i, j);
	                        r = i;
	                        c = j;
	                    }
	                }
	            }
	        }
	        return new Index(r, c);
	    }

	    public int getRandomIndex() {
	        return (int) (Math.random() * 10) % fieldSize + 1;
	    }
	    
	    public void print(){
	        System.out.println();
	        System.out.println();
	        for (int i = 0; i < fieldSize; ++i) {
	            for (int j = 0; j < fieldSize; ++j) {
	                System.out.print(field[i][j].value + " ");
	            }
	            System.out.println();
	        }
	    }
	    private void generateFullField(final int row, final int column) {
	    if (!isFilled(fieldSize(), fieldSize())) {
	        while (numberOfTriedNumbers(row, column) < variantsPerCell()) {
	            int candidate = 0;
	            do {
	                candidate = getRandomIndex();
	            } while (numberHasBeenTried(candidate, row, column));
	            if (checkNumberField(candidate, row, column)) {
	                set(candidate, row, column);
	                //System.out.print(candidate +" ");
	                Index nextCell = nextCell(row, column);
	                if (nextCell.i <= fieldSize()
	                        && nextCell.j <= fieldSize()) {
	                    generateFullField(nextCell.i, nextCell.j);
	                }
	            } else {
	                tryNumber(candidate, row, column);
	            }
	        }
	        if (!isFilled(fieldSize(), fieldSize())) {
	            //System.out.println("Resetting...");
	            reset(row, column);
	        }
	    }
	}
	}
/*
	public static void main(String args[]){
	    
	    SudokuField sf = new SudokuField(3);
	    long startTime = System.currentTimeMillis();
	    sf.generateFullField(1, 1);
	    long endTime = System.currentTimeMillis();
	    long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
	    sf.print();
	    System.out.println("Duration: " + duration);
	    
	}*/
}

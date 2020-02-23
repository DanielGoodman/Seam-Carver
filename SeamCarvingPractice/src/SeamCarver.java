
/**
 * 
 * @author Daniel Goodman
 * 2/15/2020
 * 
 * This class is used to determine least important seams in a picture which are then removed. Seams are based on pixel energy and
 *	cumulative cost matrix.
 */


public class SeamCarver {

	private Picture pic;
	private int energyMap[][];
	private int count = 0;
	
	public SeamCarver(Picture picture) {
		pic = picture;
		energyMap = new int[pic.height()][pic.width()];
		
		//assigns pixel energy values to energyMap
		for(int i = 0; i < energyMap.length; i++)
		{
			for(int j = 0; j < energyMap[i].length; j++)
				energyMap[i][j] = energy(j, i);
			}
		}
		
	
	
	public Picture getPic()
	{
		return pic;
	}
	
	public int width() {
		return pic.width();
	}
	
	public int height() {
		return pic.height();
	}
	
	/**
	 * @param x colum x in the picture with left most colum being 0
	 * @param y row y in in the picture with bottom Most colum being 0
	 * @return
	 */
	public int energy(int x, int y)
	{
		
		int deltaX = 0;
		int deltaY = 0;
		
		int redX;
		int greenX;
		int blueX;
		int redY;
		int greenY;
		int blueY;
		
		//calculates energy for non-border pixel
		if (x > 0 && x < width() - 1 && y > 0 && y < height() - 1)
		{
			redX = Math.abs(pic.get(x - 1, y).getRed() - pic.get(x + 1, y).getRed());
			greenX = Math.abs(pic.get(x - 1, y).getGreen() - pic.get(x + 1, y).getGreen()); 
			blueX = Math.abs(pic.get(x - 1, y).getBlue() - pic.get(x + 1, y).getBlue());
			deltaX = (int) (Math.pow(redX, 2) + Math.pow(greenX, 2) + Math.pow(blueX, 2));

			redY = Math.abs(pic.get(x, y - 1).getRed() - pic.get(x, y + 1).getRed());
			greenY = Math.abs(pic.get(x, y - 1).getGreen() - pic.get(x, y + 1).getGreen());
			blueY = Math.abs(pic.get(x, y - 1).getBlue() - pic.get(x, y + 1).getBlue());
			deltaY = (int) (Math.pow(redY, 2) + Math.pow(greenY, 2) + Math.pow(blueY, 2));	
		} else //calculates energy for border pixel
		{
			// x coord is on left border
			if (x == 0)
			{
				
				redX = Math.abs(pic.get(width() - 1, y).getRed() - pic.get(x + 1, y).getRed());
				greenX = Math.abs(pic.get(width() - 1, y).getGreen() - pic.get(x + 1, y).getGreen());
				blueX = Math.abs(pic.get(width() - 1, y).getBlue() - pic.get(x + 1, y).getBlue());
				deltaX = (int) (Math.pow(redX, 2) + Math.pow(greenX, 2) + Math.pow(blueX, 2));
			} else if (x == width() - 1) //x coord is on right border
			{
				redX = Math.abs(pic.get(x - 1, y).getRed() - pic.get(0, y).getRed());
				greenX = Math.abs(pic.get(x - 1, y).getGreen() - pic.get(0, y).getGreen());
				blueX = Math.abs(pic.get(x - 1, y).getBlue() - pic.get(0, y).getBlue());
				deltaX = (int) (Math.pow(redX, 2) + Math.pow(greenX, 2) + Math.pow(blueX, 2));
			}
			
			//y coord is on top border
			if (y == 0)
			{
				redY = Math.abs(pic.get(x, height() - 1).getRed() - pic.get(x, y + 1).getRed());
				greenY = Math.abs(pic.get(x, height() - 1).getGreen() - pic.get(x, y + 1).getGreen());
				blueY = Math.abs(pic.get(x, height() - 1).getBlue() - pic.get(x, y + 1).getBlue());
				deltaY = (int) (Math.pow(redY, 2) + Math.pow(greenY, 2) + Math.pow(blueY, 2));
			} else if (y == height() - 1) // y coord is on bottom border
			{
				redY = Math.abs(pic.get(x, y - 1).getRed() - pic.get(x, 0).getRed());
				greenY = Math.abs(pic.get(x, y - 1).getGreen() - pic.get(x, 0).getGreen());
				blueY = Math.abs(pic.get(x, y - 1).getBlue() - pic.get(x, 0).getBlue());
				deltaY = (int) (Math.pow(redY, 2) + Math.pow(greenY, 2) + Math.pow(blueY, 2));	
			}
		}
		
		return deltaX + deltaY;
		
	}
	
	
	public int[] findVerticalSeam()
	{
		int costMatrix[][] = new int[energyMap.length][energyMap[0].length];
		
		costMatrix = energyMap;
		
			for (int y = 1; y < costMatrix.length; y++)
			{
				
				for(int x = 0; x < costMatrix[y].length; x++)
				{
					int minEnergy = costMatrix[y - 1][x];
					//x is non border pixel
					if (x > 0 && x < costMatrix[0].length - 1)
					{
						minEnergy = costMatrix[y][x - 1];
						
						if (costMatrix[y - 1][x] < minEnergy)
						{
							minEnergy = costMatrix[y][x];
						} 
						if (costMatrix[y - 1][x + 1] < minEnergy)
						{
							minEnergy = costMatrix[y - 1][x + 1];
						}
						
						costMatrix[y][x] += minEnergy;
					} else if (x == 0)
					{
						
						minEnergy = Math.min(costMatrix[y - 1][x], costMatrix[y - 1][x + 1]);	
						
					} else if (x == costMatrix[y].length  - 1)
					{
						minEnergy = Math.min(costMatrix[y - 1][x], costMatrix[y - 1][x - 1]);
					}
				
					costMatrix[y][x] += minEnergy;
					
				}
			
			}
			
			int xCoords[] = new int[costMatrix.length]; //store x value for the pixel to be removed
			
			//find min cost value on bottom row of cost matrix
			int minCost = costMatrix[costMatrix.length - 1][0];
			xCoords[costMatrix.length - 1] = 0;
			for(int i = 1; i < costMatrix[costMatrix.length - 1].length; i++)
			{
				if (costMatrix[costMatrix.length - 1][i] < minCost)
				{
					minCost = costMatrix[costMatrix.length - 1][i];
					xCoords[costMatrix.length - 1] = i;
				}
			}
			
			//backtrace through costMatrix
			for(int i = costMatrix.length - 2; i >= 0; i--)
			{
				
				//if previous xCoord was not on border
				if (xCoords[i + 1] > 0 && xCoords[i + 1] < costMatrix[i].length - 1)
				{
					minCost = costMatrix[i][xCoords[i + 1] - 1];
					xCoords[i] = xCoords[i + 1] - 1;
					
					if (costMatrix[i][xCoords[i + 1]] < minCost)
					{
						xCoords[i] = xCoords[i + 1];
						minCost = costMatrix[i][xCoords[i + 1]];
					}
					
					if (costMatrix[i][xCoords[i + 1] + 1] < minCost)
					{
						xCoords[i] = xCoords[i + 1] + 1;
						minCost = costMatrix[i][xCoords[i + 1]] + 1;
					}
				
				} else if (xCoords[i + 1] == 0) //previous xCoord was on left border
				{
					minCost = costMatrix[i][xCoords[i + 1]];
					xCoords[i] = xCoords[i + 1];
					
					if (costMatrix[i][xCoords[i + 1] + 1] < minCost)
					{
						minCost = costMatrix[i][xCoords[i + 1] + 1];
						xCoords[i] = xCoords[i + 1] + 1;
					}
					
				} else if (xCoords[i + 1] == costMatrix[i].length - 1)
				{
					minCost = costMatrix[i][xCoords[i + 1]];
					xCoords[i] = xCoords[i + 1];
					
					if (costMatrix[i][xCoords[i + 1] - 1] < minCost)
					{
						minCost = costMatrix[i][xCoords[i + 1] - 1];
						xCoords[i] = xCoords[i + 1] - 1;
					}
				}
				 
				
			}
			
			return xCoords;
		
	}
	
	
	public void removeVerticalSeam(int[] seam)
	{
		Picture temp = new Picture(pic.width() - 1, pic.height());
		int count = 0;
		for (int y = 0; y < pic.height(); y++) {
			count = 0;
			for (int x = 0; x < pic.width(); x++) {
				if (seam[y] != x) {
					temp.set(count, y, pic.get(x, y));
					count++;
				}
			}
		}
		pic.copy(temp);
		
		int tempEnergyMap[][] = new int[energyMap.length][energyMap[0].length - 1];
		
		for(int y = 0; y < energyMap.length; y++)
		{
			for (int x = 0; x < tempEnergyMap[0].length; x++)
			{
				if (x < seam[y]) tempEnergyMap[y][x] = energyMap[y][x];
				else tempEnergyMap[y][x] = energyMap[y][x + 1];
			}
		}
		
		
		energyMap = tempEnergyMap;
		
		count++;
		System.out.println(count);
		
		
	}
	
	
	
}

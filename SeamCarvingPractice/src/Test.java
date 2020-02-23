import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;;

public class Test {

	public static void main(String[] args)
	{
		String userFile;
		System.out.println("Enter file pathway for picture (ex. C:\\Users\\John\\Pictures\\surfer.jpg ): ");
		Scanner in = new Scanner(System.in);;
		
		userFile = in.nextLine();
		
		Picture pic = new Picture(new File(userFile));
		
		System.out.println("Enter file pathway for output (ex. C:\\Users\\John\\Pictures\\surfer.jpg): ");
		userFile = in.nextLine();
		
		SeamCarver carv = new SeamCarver(pic);
		
		int reductionSize;
		
		System.out.println("How many pixels do you want to remove (horizontally): ");
		
		reductionSize = in.nextInt();
		
		for (int i = 0; i < reductionSize; i++)
		{
			carv.removeVerticalSeam(carv.findVerticalSeam());
		}
		
		
		System.out.println("carv complete");
		
		pic.save(new File(userFile));
		
	}
	
}

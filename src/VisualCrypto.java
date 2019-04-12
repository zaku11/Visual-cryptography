import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.Color;

import javax.imageio.ImageIO;

public class VisualCrypto 
{
	private static int White = Color.WHITE.getRGB();
	private static int Black = Color.BLACK.getRGB();
	private static int Cast(boolean val)
	{
		if(val==true) return 1;
		return 0;
	}
	private static int getCoordX(int ActualX,int Num)
	{
		return ActualX + ((Num+1)%2);
	}
	private static int getCoordY(int ActualY,int Num)
	{
		return ActualY + Cast(Num > 2);
	}
	private static void ColorAtCoords(int X,int Y,BufferedImage ans1,BufferedImage ans2,boolean isWhite)
	{
		int Fst,Snd,Rd,Fth; 
		
		List <Integer> list = new ArrayList<Integer>();
		for(int i=1;i<=4;i++) list.add(i);
		Collections.shuffle(list);
		
		Fst = list.get(0);
		Snd = list.get(1);
		Rd = list.get(2);
		Fth = list.get(3);
		
		if(isWhite == true)
		{			
			ans1.setRGB(getCoordX(2*X,Fst),getCoordY(2*Y,Fst),Black);
			ans1.setRGB(getCoordX(2*X,Snd),getCoordY(2*Y,Snd),Black);
			ans1.setRGB(getCoordX(2*X,Rd),getCoordY(2*Y,Rd),White);
			ans1.setRGB(getCoordX(2*X,Fth),getCoordY(2*Y,Fth),White);
			
			ans2.setRGB(getCoordX(2*X,Fst),getCoordY(2*Y,Fst),Black);
			ans2.setRGB(getCoordX(2*X,Snd),getCoordY(2*Y,Snd),Black);
			ans2.setRGB(getCoordX(2*X,Rd),getCoordY(2*Y,Rd),White);
			ans2.setRGB(getCoordX(2*X,Fth),getCoordY(2*Y,Fth),White);
		}
		else
		{
			ans1.setRGB(getCoordX(2*X,Fst),getCoordY(2*Y,Fst),Black);
			ans1.setRGB(getCoordX(2*X,Snd),getCoordY(2*Y,Snd),Black);
			ans1.setRGB(getCoordX(2*X,Rd),getCoordY(2*Y,Rd),White);
			ans1.setRGB(getCoordX(2*X,Fth),getCoordY(2*Y,Fth),White);
		
			ans2.setRGB(getCoordX(2*X,Fst),getCoordY(2*Y,Fst),White);
			ans2.setRGB(getCoordX(2*X,Snd),getCoordY(2*Y,Snd),White);
			ans2.setRGB(getCoordX(2*X,Rd),getCoordY(2*Y,Rd),Black);
			ans2.setRGB(getCoordX(2*X,Fth),getCoordY(2*Y,Fth),Black);
		}
	}
	public static boolean checkCorrectness(BufferedImage img1, BufferedImage img2)
	{
		int n = img1.getHeight();
		int m = img1.getWidth();
		if(img1.getHeight() != img2.getHeight() || img2.getWidth() != img2.getWidth())
		{
			System.out.println("Size of images are not the same sizes.Terminating.");
			return false;
		}
		for(int i=0;i<n;i++) 
		{
			for(int j=0;j<m;j++)
			{
				if(img1.getRGB(j,i) != White && img1.getRGB(j,i) != Black && img2.getRGB(j, i) != Black && img2.getRGB(j,i) != White)
				{
					System.out.println("In given images there are another colors than black and white. Terminating");
					return false;
				}
			}
		}
		for(int i=0;i<n/2;i++)
		{
			for(int j=0;j<m/2;j++)
			{
				int sum = Cast(img1.getRGB(2*j,2*i) == img2.getRGB(2*j,2*i));
				sum += Cast(img1.getRGB(2*j+1,2*i) == img2.getRGB(2*j+1,2*i));
				sum += Cast(img1.getRGB(2*j,2*i+1) == img2.getRGB(2*j,2*i+1));
				sum += Cast(img1.getRGB(2*j+1,2*i+1) == img2.getRGB(2*j+1,2*i+1));
				if(sum != 0 && sum != 4)
				{
					System.out.println("Images, that are given, were not created properly. Terminating");
					return false;
				}
			}
		}
		return true;
	}
	public static String addingEnding(String S,int X)
	{
		String[] partsS = S.split("\\."); 
		return partsS[0]+X+"."+partsS[1];
	}
	public static String getType(String S)
	{
		int pointer = S.length()-1;
		while(S.charAt(pointer) != '.') pointer--;
		return S.substring(pointer);
	}
	public static void main(String[] args)
	{
		if(args.length == 0)
		{
			System.out.println("No file was given.Terminating.");
			System.exit(0);
		}
		if(args.length == 1)
		{
			System.out.println("Encrypting an image.");
			try 
			{
				BufferedImage img = ImageIO.read(new File(args[0]));
				int type = img.getType();
				System.out.println("File found.");
				
				int n = img.getHeight();
				int m = img.getWidth();
				BufferedImage ans1 = new BufferedImage(m*2,n*2,type);
				BufferedImage ans2 = new BufferedImage(m*2,n*2,type);
				
				System.out.println("Generating...");
				for(int i=0;i<n;i++)
				{
					for(int j=0;j<m;j++)
					{
						int Color = img.getRGB(j, i);
						if(Color != White && Color != Black) 
						{
							System.out.println("The given image is not black and white only. Terminating");
							System.exit(0);
						}
						if(Color == White) ColorAtCoords(j,i,ans1,ans2,true);	
						else ColorAtCoords(j,i,ans1,ans2,false);
					}
				}
				ImageIO.write(ans1, "png" ,new File(addingEnding(args[0],1)));
				ImageIO.write(ans2, "png" ,new File(addingEnding(args[0],2)));
				System.out.println("Files created succesfully in:\n"+addingEnding(args[0],1)+"\n"+addingEnding(args[0],2));
			} 
			catch (IOException e) 
			{
					System.out.println("File does not exist, or something else is wrong. Terminating");
					e.printStackTrace();
					System.exit(0);
			}
		}
		if(args.length == 2)
		{
			System.out.println("Concatenating two images into one.");
			try 
			{
				BufferedImage img1 = ImageIO.read(new File(args[0]));
				BufferedImage img2 = ImageIO.read(new File(args[1]));
				
				if(!checkCorrectness(img1,img2)) System.exit(0);
				
				int n = img1.getHeight();
				int m = img1.getWidth();
				int type = img1.getType();
				BufferedImage ans = new BufferedImage(m,n,type);
				
				System.out.println("Images are correct. Beggining concatenation...");
				for(int i=0;i<n;i++)
				{
					for(int j=0;j<m;j++)
					{
						int fst = img1.getRGB(j, i);
						int snd = img2.getRGB(j, i);
						if(fst == White && snd == White) ans.setRGB(j,i,White);
						else ans.setRGB(j, i, Black);
					}
				}
				Path path = Paths.get(args[0]);
				Path newPath = Paths.get(path.getRoot().toString(),path.subpath(0,path.getNameCount()-1).toString(),"Answ"+getType(args[0]));
				ImageIO.write(ans,getType(args[0]).substring(1) ,new File(newPath.toString()));
				System.out.println("Concatenation was successful.");
				System.out.println("The result can be found in "+newPath);				
			} 
			catch (IOException e) 
			{
				System.out.println("File does not exist, or something else is wrong. Terminating");
				e.printStackTrace();
				System.exit(0);
			}
		}
		
	}
}

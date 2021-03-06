package kr.ac.ajou.oop.managers;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;

import kr.ac.ajou.oop.user.User;

@SuppressWarnings("serial")
public class FileManager implements Serializable {
	
	private FileManager() {}

	public static void saveUser(User u) throws IOException {
		File dir = new File("data/user/");
		if(!dir.exists()) dir.mkdir();
		FileOutputStream fos = new FileOutputStream("data/user/user.dat");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(u);//
		oos.close();
	}	
	
	
	public static Image loadImage(int level) throws IOException, IIOException
	{
		BufferedImage image;
		String filename = "data/images/image_" + level + ".png";
		
		image = ImageIO.read(new File(filename));	
		
		return image;
	}
	
	
	public static String loadGuidance(int level) throws IOException {

		File file = new File("data/guidances/guidance_" + level + ".txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		StringBuilder sb = new StringBuilder();
		String line;

		while ((line = br.readLine()) != null) {
			sb.append(line);
			sb.append(System.lineSeparator());
		}
		
		br.close();
		return sb.toString();
	}
	
	public static ArrayList<String> answers(int level) throws IOException {
		
		File file = new File("data/answer/answer_" + level + ".txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		ArrayList<String> answerSet = new ArrayList<String>();
		String line;
		
		while((line = br.readLine()) != null) {
			answerSet.add(line);
		}
		
		br.close();
		
		return answerSet;
	}
	
	public static String loadAnswerCode(int level) throws FileNotFoundException, IOException {
		File file = new File("data/code/code_" + level + ".txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		StringBuilder sb = new StringBuilder();
		String line;

		while ((line = br.readLine()) != null) {
			sb.append(line);
			sb.append(System.lineSeparator());
		}
		
		br.close();
		return sb.toString();
	}
	
	public static boolean userDataExists() {
		if(new File("data/user/user.dat").exists()) return true;
		else return false;
	}
	
	public static int getUserScore(){
		File userData = new File("data/user/user.dat");
		FileInputStream fis;
		ObjectInputStream ois;
		int score;
		
		try {
			fis = new FileInputStream(userData);
			ois = new ObjectInputStream(fis);
			
			Object o = ois.readObject();
			ois.close();
			
			if(o instanceof User) score = ((User)o).getScore();
			else score = -1;
		} catch (FileNotFoundException e) {
			score = -1;
			
		} catch (IOException e) {
			score = -1;
			
		} catch (ClassNotFoundException e) {
			score = -1;
			
		}
		
		return score;
	}
	
	public static int getTFAmount(int level) {
		File data = new File("data/elements/elements.txt");
		int rAmount;

		String line;
		StringBuilder sb = new StringBuilder();
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(data));
			
			while((line = br.readLine()) != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
			}
			
			rAmount = Integer.parseInt(sb.toString().split(" ")[level-1]);
		} catch(NumberFormatException e) {
			rAmount = -1;
			
		} catch (FileNotFoundException e) {
			rAmount = -1;
			
		} catch (IOException e) {
			rAmount = -1;
			
		}
		
		return rAmount;
	}

}

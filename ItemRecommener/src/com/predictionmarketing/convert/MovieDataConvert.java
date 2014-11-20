package com.predictionmarketing.convert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MovieDataConvert {

	/**
	 * in UNIX JUst run:
	 * cat u.data | cut -f1,2,3 | tr "\\t" ","
	 * @param args
	 * @throws IOException 
	 */
	
	//https://www.youtube.com/watch?v=yD40rVKUwPI&index=4&list=PLxhVwir8VBvWP2YYZMz9PBaW1KDlF8QnP
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		BufferedReader br = new BufferedReader(new FileReader("data/u.data"));
		BufferedWriter bw = new BufferedWriter(new FileWriter("data/movies.csv"));

		String line;
		while((line = br.readLine()) != null) {
			String[] values = line.split("\\t", -1);
			bw.write(values[0] + "," + values[1] + "," + values[2] + "\n");
		}

		br.close();
		bw.close();

	}

}

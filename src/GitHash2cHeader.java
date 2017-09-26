/**
 * 
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author physicsboy
 *
 */
public class GitHash2cHeader {
	/**
	 * @param args
	 */
	public static void main(String[] args){
		System.out.printf("GitHash convert to C header\n");
		String InputfileName = args[0]+"HEAD";
		String OutputfileName = "gitinfo.h";
		BufferedReader br;
		BufferedWriter bw;
		String filecontent;
		try {
			br = new BufferedReader(new FileReader(new File(InputfileName)));
			bw = new BufferedWriter(new FileWriter(new File(OutputfileName)));
			bw.write("#ifndef __GITINFO_H\n#define __GITINFO_H\n");
			if((filecontent = br.readLine()) != null) {
                System.out.println(filecontent);
            }
			br.close();
			String ref_filepath[] = filecontent.split(": ", 2);
			if(ref_filepath.length != 2) {
				bw.close();
				throw new IOException();
			}
			//get branch name:
			//FIXME: currently need checkout point has a branch name,
			//cannot support checkout without any branch
			String branchname_arr[] = ref_filepath[1].split("/");
			bw.write("#define CURRENT_BRANCHNAME \""+branchname_arr[branchname_arr.length-1]+"\"\n");
			
			//get commit hash file
			InputfileName = args[0]+ref_filepath[1];
			br = new BufferedReader(new FileReader(new File(InputfileName)));
			if((filecontent = br.readLine()) != null) {
                System.out.println(filecontent);
            }
			//get commit hash
			bw.write("#define CURRENT_COMMIT_HASH \""+filecontent+"\"\n");
			bw.write("#endif\n");
			bw.flush();

			br.close();
			bw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
            System.out.println(
                    "Unable to open file '" + 
                    InputfileName + "'");                
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
            System.out.println(
                    "Error reading file '" 
                    + InputfileName + "'");
		}
		System.out.printf("Successfully ended\n");
	}
}
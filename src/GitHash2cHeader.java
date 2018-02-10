/**
 * 
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
/**
 * @author tianhe wang
 *
 */
public class GitHash2cHeader {
	/**
	 * @param args
	 */
	public static void main(String[] args){
		System.out.printf("GitHash convert to C header\n");
		if(args.length < 2) {
			System.out.printf("Please specify input and output file name");
			return;
		}
		String OutputfileName = args[1];
		try {
			String InputfileName = null;
			String BranchName = null;
			String CommitHASH = null;
			try {
				InputfileName = args[0] + "/HEAD";
				BufferedReader br;
				//read git head message
				br = new BufferedReader(new FileReader(new File(InputfileName)));
				String filecontent = br.readLine();
				br.close();

				//get branch name:
				//FIXME: currently need checkout point has a branch name,
				//cannot support checkout without any branch
				String ref_filepath[] = filecontent.split(": ", 2);
				if(ref_filepath.length != 2) {
					throw new IOException();
				}
				String branchname_arr[] = ref_filepath[1].split("/");
				BranchName = branchname_arr[branchname_arr.length-1];
				if(BranchName != null) {
					System.out.println(BranchName);
				}

				//get commit hash
				//get commit hash file path
				InputfileName = args[0] + "/";
				InputfileName += ref_filepath[1];
				br = new BufferedReader(new FileReader(new File(InputfileName)));
				CommitHASH = br.readLine();
				if(CommitHASH != null) {
					System.out.println(CommitHASH);
	      }
				br.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
        System.out.println("Unable to open file '" + InputfileName + "'");                
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
        System.out.println("Error reading file '" + InputfileName + "'");
        throw new IOException();
			}
			BufferedWriter bw;
			File outputfile = new File(OutputfileName);
			outputfile.getParentFile().mkdirs();
			bw = new BufferedWriter(new FileWriter(outputfile));
			//write #ifdef
			bw.write("#ifndef __GITINFO_H\n#define __GITINFO_H\n");
			bw.write("#define CURRENT_BRANCHNAME \"" + BranchName + "\"\n");
			bw.write("#define CURRENT_COMMIT_HASH \"" + CommitHASH + "\"\n");
			
			//get current runtime as UTC.
			Date s = new Date();
			long currentmsec = s.getTime();
			String utcmsec = Long.toUnsignedString(currentmsec);
			System.out.println(utcmsec);
			String utcsec = utcmsec.substring(0, utcmsec.length()-3);
			
			//write current runtime string.
			bw.write("#define BUILD_STEP_UTC_SEC " + utcsec + "UL\n");
			bw.write("#define BUILD_STEP_UTC_MSEC " + utcmsec + "ULL\n");
			
			//write #endif
			bw.write("#endif\n");
			bw.flush();
			bw.close();
			System.out.printf("Successfully ended\n");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
            System.out.println(
                    "Unable to write file '" + 
                    		OutputfileName + "'");                
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
            System.out.println(
                    "Error reading file '" 
                    + OutputfileName + "'");
		}
	}
}

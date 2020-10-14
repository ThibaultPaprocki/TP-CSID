import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class ExoFile {

	public static void main(String[] args)throws IOException,NoSuchAlgorithmException{

		Map<String, ArrayList<File>> files = new HashMap<>();
		File f = new File(args[0]);
		recursiveFileSearch(f, files);

		System.out.println(files);
	}

	public static void recursiveFileSearch(File currentFile,Map<String,ArrayList<File>> files){

		for (File file : currentFile.listFiles()) {
			if(file.isDirectory()) {
				recursiveFileSearch(file, files);
			}
			else if(file.isFile()){
				if(file.canRead()){
					try {
						treatment(file, files);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public static void treatment(File file, Map<String,ArrayList<File>> files)throws NoSuchAlgorithmException,UnsupportedEncodingException,IOException{

		String sha1 = convertTextFileToSha1(getFileContent(file));
		if(!files.containsKey(sha1)){
			files.put(sha1, new ArrayList<File>());
			files.get(sha1).add(file);
		}
		else
			files.get(sha1).add(file);
	}
	
	public static String getFileContent(File f)throws IOException{

		BufferedReader br = new BufferedReader(new FileReader(f));
		boolean eof = false;
		String content=null;
		String line = "";

		while (!eof){
			line = br.readLine();
			if(line!=null)
				content+=line;
			else
				eof=!eof;
		}
		return content;
	}

	public static String convertTextFileToSha1(String input)throws NoSuchAlgorithmException,UnsupportedEncodingException{

		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		byte[] digest = sha.digest(input.getBytes("UTF-8"));
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < digest.length; i++){
			sb.append(Integer.toHexString(digest[i] & 0xFF | 0x100).substring(1, 3));
		}
		return sb.toString();
	}
}

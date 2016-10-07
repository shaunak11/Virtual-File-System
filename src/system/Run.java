package system;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Run {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner sc=new Scanner(System.in);
		FileSystem filesystem=new FileSystem(200);
			String command="";
			String path="";
			while(true)
			{
				System.out.println("$");
				command=sc.nextLine();
				if(command.equals("exit"))
				{
					break;
				}
				String commandsplit[]=command.split(" ");
				if(commandsplit.length<2){
					continue;
				}
				path=commandsplit[1].trim();
				String patharray[]=path.split("/");
				switch(commandsplit[0]){
					case "mkfile":
						int mksize=0;
						int mktype=-1;
						int mkrating=0;
						String mkt="";
						System.out.println("Enter File size:");
					    mksize=sc.nextInt();
					    System.out.println("Enter File type:");
					    mkt=sc.next();
					    if(mkt.equals("movie"))
					    	mktype=0;
					    else if(mkt.equals("audio"))
					    	mktype=1;
					    else if(mkt.equals("text"))
					    	mktype=2;
					    System.out.println("Enter rating for File:");
					    mkrating=sc.nextInt();
					    sc.nextLine();
						System.out.println("Enter supportingString for File:");
					    mkt=sc.nextLine();
						try{
							if(filesystem.Createfile(patharray,mksize,mktype,mkrating,mkt))
								System.out.println(filesystem.FileExists(patharray).getLeafName()+" created successfully");
							else
								System.out.println("File creation failed\n");
						}catch(Exception e){
							System.out.println("Something went wrong");
						}
						break;
					case "mkdir":
						try{
							if(filesystem.Createdir(patharray))
							{
								System.out.println("Directory Created Successfully\n");
							}
							else
							{
								System.out.println("Directory already exists\n");
							}
						}catch(Exception e){
							System.out.println("Something went wrong");
						}
						break;
					case "listdir":
						try{
							String[] filelist=filesystem.lsdir(patharray);
							if(filelist==null)
							{
								System.out.println("Empty File System\n");
								continue;
							}
							if(filelist.length==0)
							{
								System.out.println("Empty Directory\n");
							}
							for(int i=0;i<filelist.length;i++)
							{
								System.out.println(filelist[i]);
							}
						}catch(Exception e){
							System.out.println("Something went wrong");
						}
						break;
					case "rmdir":
						try{
							if(filesystem.rmdir(patharray))
								System.out.println("Directory removed Successfully\n");
							else
								System.out.println("Directory has not been removed\n");
						}catch(Exception e)
						{
							System.out.println("Something went wrong");
						}
						break;
					case "fileexists":
						try{
						   Leaf fpath=filesystem.FileExists(patharray);
						   String[] parray=fpath.getPath();
						   String p="File exists at:";
						   for (int i = 0; i < parray.length-1; i++) {
							p+=parray[i]+"/";
						   }
						   System.out.println(p);
						}catch(Exception e)
						{
							System.out.println("Something went wrong");
						}
						break;
					case "rmfile":
						try{
							if(filesystem.rmfile(patharray))
								System.out.println("File removed Successfully\n");
							else
								System.out.println("File has not been removed\n");
						}catch(Exception e)
						{
							System.out.println("Something went wrong");
						}
						break;
					case "findbytype":
						try{
							int fttype=-2;
							System.out.println("Enter File type:");
						    String ftt=sc.next();
						    if(ftt.equals("movie"))
						    	fttype=0;
						    else if(ftt.equals("audio"))
						    	fttype=1;
						    else if(ftt.equals("text"))
						    	fttype=2;
							ArrayList<String> typearray=new ArrayList<String>();
							filesystem.searchbyType(patharray,typearray,fttype);
							for (Iterator iterator = typearray.iterator(); iterator.hasNext();) {
								String string = (String) iterator.next();
								System.out.println(string);
							}
						}catch(Exception e){
							System.out.println("Something went wrong");
						}
						break;
					case "findbyrating":
						try{
							System.out.println("Enter rating:");
							int frrating=sc.nextInt();
							ArrayList<String> ratingarray=new ArrayList<String>();
							filesystem.searchbyRating(patharray,ratingarray,frrating);
							for (Iterator iterator = ratingarray.iterator(); iterator.hasNext();) {
								String string = (String) iterator.next();
								System.out.println(string);
							}
						}catch(Exception e)
						{
							System.out.println("Something went wrong");
						}
						break;
					case "listfiles":
						try{
							filesystem.listFiles(patharray);
						}catch(Exception e)
						{
							System.out.println("Something went wrong");
						}
						break;
					case "isFile":
						try{
							if(filesystem.isFile(null,patharray))
								System.out.println("Yes");
							else
								System.out.println("No");
						}catch(Exception e)
						{
							System.out.println("Something went wrong");
						}
						break;
					case "isDir":
						try{
							if(filesystem.isDir(null,patharray))
								System.out.println("Yes");
							else
								System.out.println("No");
						}catch(Exception e)
						{
							System.out.println("Something went wrong");
						}
						break;
					case "write":
						try{					    
							if(filesystem.isFile(null, patharray)){
								System.out.println("write your text");
								ArrayList<String> s = new ArrayList<String>();
								int i=0;
								//if(!sc.next().equals("done")){									
									while(sc.hasNext()){
										
										String q=sc.nextLine();
										if(!q.equals("done")){
											s.add(q);
											i++;
										}
										else{
											filesystem.writeText(patharray, s);
											System.out.println("Written to the text file");
											break;
										}
									}
									
							}
							else
								System.out.println("not a file");
							
						}catch(Exception e){
							System.out.println("Something went wrong");
						}
						break;
					case "read":
						try{					    
							if(filesystem.isFile(null, patharray)){
								ArrayList<String> z = new ArrayList<String>();
								z=filesystem.readText(patharray);
								for(int i=0; i<z.size(); i++){
									System.out.println(z.get(i));
								}
							}
							else
								System.out.println("not a file");
						}catch(Exception e){
							System.out.println("Something went wrong");
						}
						break;
					default:
						System.out.println("No such Command\n");
						break;
				}
			}
	}
	
}


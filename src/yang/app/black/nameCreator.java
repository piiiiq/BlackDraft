package yang.app.black;

import java.io.File;
import java.util.List;
import java.util.Random;

import yang.demo.allPurpose.isYourNeedFile;

public class nameCreator {
	File dir;
	black b;
	List<String> italiana,italiana_m,italiana_f,
	english,english_m,english_f,
	chinese_all
	;
	public nameCreator(black b,File dir){
		this.dir = dir;
		this.b = b;
		if(!dir.exists()) dir.mkdir();
		getallfiles();
	}
	public void getallfiles(){
		ioThread io = new ioThread(b);
		isYourNeedFile needfiles = new isYourNeedFile(dir, ".*.black");
		String[] files = needfiles.Filter();
		for(int i=0;i<files.length;i++){
			String s = files[i];
			//意大利人名
			if(s.equals("i.black")){
				File first = new File(dir.getAbsolutePath()+"\\"+s);
				italiana = cheakDocument.getAllLine(io.readBlackFile(first, null).get());
			}else if(s.equals("if.black")){
				File first = new File(dir.getAbsolutePath()+"\\"+s);
				italiana_f = cheakDocument.getAllLine(io.readBlackFile(first, null).get());
			}else if(s.equals("im.black")){
				File first = new File(dir.getAbsolutePath()+"\\"+s);
				italiana_m = cheakDocument.getAllLine(io.readBlackFile(first, null).get());
			}
			//英语人名
			else if(s.equals("e.black")){
				File first = new File(dir.getAbsolutePath()+"\\"+s);
				english = cheakDocument.getAllLine(io.readBlackFile(first, null).get());
			}else if(s.equals("ef.black")){
				File first = new File(dir.getAbsolutePath()+"\\"+s);
				english_f = cheakDocument.getAllLine(io.readBlackFile(first, null).get());
			}else if(s.equals("em.black")){
				File first = new File(dir.getAbsolutePath()+"\\"+s);
				english_m = cheakDocument.getAllLine(io.readBlackFile(first, null).get());
			}
			//中文人名
			else if(s.equals("call.black")){
				File first = new File(dir.getAbsolutePath()+"\\"+s);
				chinese_all = cheakDocument.getAllLine(io.readBlackFile(first, null).get());
			}
		}
	}
	/**
	 * 检查是否存在意大利人名数据
	 * @return
	 */
	public boolean isHasItalinaNameData(){
		if(italiana != null && italiana_f != null && italiana_m != null)
			return true;
		else return false;
	}
	/**
	 * 产生意大利人名
	 * @param gender性别，女性f或者男性m
	 */
	public String[] getItalinaName(char gender){
		Random ran = new Random();
		if(gender == 'f'){
			int first = ran.nextInt(italiana.size());
			int last = ran.nextInt(italiana_f.size());
			return new String[]{italiana.get(first),italiana_f.get(last)};
		}else{
			int first = ran.nextInt(italiana.size());
			int last = ran.nextInt(italiana_m.size());
			return new String[]{italiana.get(first),italiana_m.get(last)};
		}
	}
	public boolean isHasEnglishNameData(){
		if(english != null && english_f != null && english_m != null)
			return true;
		else return false;
	}
	/**
	 * 产生英语人名
	 * @param gender性别，女性f或者男性m
	 */
	public String[] getEnglishName(char gender){
		Random ran = new Random();
		if(gender == 'f'){
			int first = ran.nextInt(english.size());
			int last = ran.nextInt(english_f.size());
			return new String[]{english.get(first),english_f.get(last)};
		}else{
			int first = ran.nextInt(english.size());
			int last = ran.nextInt(english_m.size());
			return new String[]{english.get(first),english_m.get(last)};
		}
	}
	public boolean isHasChineseNameData(){
		if(chinese_all != null)
			return true;
		else return false;
	}
	/**
	 * 产生指定数量的中文名
	 * @param count
	 * @return
	 */
	public String[] getChineseNames(int count){
		Random ran = new Random();
		String[] names = new String[count];
		String name = null;
		for(int i=0;i<count;i++){
			do{
				int all = ran.nextInt(chinese_all.size());
				name = chinese_all.get(all);
			}while(ishas(name,names));
			names[i] = name;
		}
		return names;
	}
	public boolean ishas(String name, String[] names){
		boolean ishas = false;
		for(int a=0;a<names.length;a++){
			if(name.equals(names[a])){
				ishas = true;
				break;
			}
		}
		return ishas;
	}
	
}

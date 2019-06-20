package javaCode;

public class UrlRepository{
	static String imagesUrl = "../images/";
	static String stylesUrl = "../styles/";
	static String home = "index.jsp";
	static public String getImagesUrl(String fileName){
		return imagesUrl + fileName;
	}
	static public String getStylesUrl(String fileName){
		return stylesUrl + fileName;
	}
	static public String getHome(){
		return home;
	}
}
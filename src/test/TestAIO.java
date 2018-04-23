package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.myusbkey.jyutianex;

public class TestAIO {

	public static void main(String[] args){
		
		jyutianex j = new jyutianex();
		System.out.print(j.FindPort(0));
		
	}
}

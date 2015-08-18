package org.cidarlab.web.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.cidarlab.web.AuthenticationException;
import org.cidarlab.web.Authenticator;
import org.json.JSONException;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuthenticationTester {

	private static final String USER_DB_NAME = "CIDAR";
	private static Authenticator auth;
	
	@BeforeClass
	public static void initialize() {
		auth = new Authenticator(USER_DB_NAME);
	}
	
	//@Test
	public void testRegister() {

		String exception = null;
		
		try {
			auth.register("ernst", "s3cr3t");
		} catch(AuthenticationException ae) {
			exception = ae.getMessage();
		}

		assertEquals(exception, null);
	}
	
	//@Test
	public void testDoubleRegister() {
		try {
			auth.register("double", "s3cr3t");
			auth.register("double", "s3cr3t");
		} catch(AuthenticationException ae) {
			assertEquals(ae.getMessage(), "The user exists already!");
		}
	}
	
	//@Test
	public static void testConvertFileToSecureAuthentication() 
			throws Exception {
                initialize();
		Scanner sc = new Scanner(new File("/Users/evanappleton/dfx_git/igem-datasheet/Datasheet_Generator/src/main/webapp/WEB-INF/restricted/password.txt"));
		String s = null;
		while(sc.hasNext()) {
                        s = sc.nextLine();
			String user = s.split(",")[0];
			String passwd = s.split(",")[1];
			
			try {
				auth.register(user, passwd);
			} catch(AuthenticationException ae) {
				assertNotEquals(ae.getMessage(), "The user exists already!");
			}
			
		}
		sc.close();
	}

	//@Test
	public void testValidLogin() {
		try {
			assertTrue(
					auth.login("ernst", "s3cr3t"));
		} catch(AuthenticationException ae) {
			assertNotEquals(ae.getMessage(), "Invalid Login!");
		}		
	}

	//@Test
	public void testInvalidLogin() {
		try {
			auth.login("me not", "regIster3d");
		} catch(AuthenticationException ae) {
			assertEquals("Invalid Login!", ae.getMessage());
		}
	}
        
//        public static void main(String[] args) throws JSONException, IOException, Exception {
//        
//            initialize();
//            testConvertFileToSecureAuthentication();
//    
//    }
}
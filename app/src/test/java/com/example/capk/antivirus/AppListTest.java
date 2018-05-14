package com.example.capk.antivirus;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.capk.antivirus.Service.ServiceCheck;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import static junit.framework.Assert.assertEquals;

/**
 * Created by capk on 5/9/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class AppListTest {
    @Mock
    Context mMockContext;//Creating Mockito Context to instantiate other classes and object
    @Test
    public void isMyServiceRunningTest(){
        //Instantiating intent for the service
        Intent intent = new Intent(mMockContext,ScanService.class);
        //Parsing data to service
        intent.setData(Uri.parse("partial"));
        //Starting service
        mMockContext.startService(intent);
        //This method compare the expected and actual output if true then result is passed
        assertEquals(new ServiceCheck(mMockContext).isMyServiceRunning(ScanService.class),true);
        //Stopping service
        mMockContext.stopService(intent);
    }

    @Test

    public void isSystemPackageTest(){

    }
    @Test
    public void getMD5Test(){
        assertEquals(new MD5().getMD5("Hello world"),"3e25960a79dbc69b674cd4ec67a72c62");
    }
    @Test
    public void SHA1Test(){
        try {
            assertEquals(SHA.SHA1("Hello world"),"e1AsOh9IyGCa4hLN+2Od7jlnP14=");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void SHA256Test(){
        assertEquals(SHA.SHA256("Hello world".getBytes()),"ZOyIygCyaOW6GjVnihtTFtIS9PNmskdyMlNKiuyjfzw=");

    }
//    @Test
//    public void myServiceNotRunningTest(){
//        assertEquals(new Scan().isMyServiceRunning(ScanService.class),false);
//    }


}

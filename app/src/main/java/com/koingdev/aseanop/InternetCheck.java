package com.koingdev.aseanop;

import java.io.IOException;

/**
 * Created by SSK on 01-Jun-17.
 */

public class InternetCheck{
    public static boolean checkInternetConnection(){
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("ping -c 1 google.com");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (InterruptedException e) {  }
        catch (IOException e)          {  }

        return false;
    }
}

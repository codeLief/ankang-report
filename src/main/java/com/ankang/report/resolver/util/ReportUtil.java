/**
 **Copyright (c) 2015, ancher  安康 (676239139@qq.com).
 ** 
 ** This Source Code Form is subject to the terms of the Mozilla Public
 ** License, v. 2.0. If a copy of the MPL was not distributed with this
 ** file, You can obtain one at 
 ** 
 ** 	http://mozilla.org/MPL/2.0/.
 **
 **If it is not possible or desirable to put the notice in a particular
 **file, then You may include the notice in a location (such as a LICENSE
 **file in a relevant directory) where a recipient would be likely to look
 **for such a notice.
 **/
package com.ankang.report.resolver.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


import com.ankang.report.exception.ReportException;

public class ReportUtil {  
    
    /**
     * 
    	 * @Description:  
         * @author: ankang
         * @date: 2015-9-28 下午3:09:55
         * @param clazz
         * @param pkName
         * @return
     */
    public static List<Class<?>> getAllClassByInterface(Class<?> clazz){  
        List<Class<?>> returnClassList = new ArrayList<Class<?>>();  
        if(clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())){  
        	String packageName = clazz.getPackage().getName();
            try {  
                List<Class<?>> allClass = getClasses(packageName);
                for(int i = 0; i < allClass.size(); i++){  
                    if(clazz.isAssignableFrom(allClass.get(i))){  
                        if(!clazz.equals(allClass.get(i))){  
                            returnClassList.add(allClass.get(i));  
                        }  
                    }  
                }  
            } catch (ClassNotFoundException e) {  
                  throw new ReportException("The package name is not available.{}", packageName);
            }catch (IOException e) {  
            	throw new ReportException("File transfer exception.{}", e.getMessage());
          }  
        }  
        return returnClassList;  
    }  
      
    /**
     * 
    	 * @Description: 根据包名获得该包以及子包下的所有类不查找jar包中的  
         * @author: ankang
         * @date: 2015-9-28 下午3:09:38
         * @param packageName
         * @return
         * @throws ClassNotFoundException
         * @throws IOException
     */
    private static List<Class<?>> getClasses(String packageName) throws ClassNotFoundException, IOException{  
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();  
        String path = packageName.replace(".", "/");  
        Enumeration<URL> resources = classLoader.getResources(path);  
        List<File> dirs = new ArrayList<File>();  
        while(resources.hasMoreElements()){  
            URL resource = resources.nextElement();  
            dirs.add(new File(resource.getFile()));  
        }  
        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();  
        for(File directory:dirs){  
            classes.addAll(findClass(directory, packageName));  
        }  
        return classes;  
    }  
      
    private static  List<Class<?>> findClass(File directory, String packageName)   
        throws ClassNotFoundException{  
        List<Class<?>> classes = new ArrayList<Class<?>>();  
        if(!directory.exists()){  
            return classes;  
        }  
        File[] files = directory.listFiles();  
        for(File file:files){  
            if(file.isDirectory()){  
                assert !file.getName().contains(".");  
                classes.addAll(findClass(file, packageName+"."+file.getName()));  
            }else if(file.getName().endsWith(".class")){  
                classes.add(Class.forName(packageName+"."+file.getName().substring(0,file.getName().length()-6)));  
            }  
        }  
        return classes;  
    }  
}  
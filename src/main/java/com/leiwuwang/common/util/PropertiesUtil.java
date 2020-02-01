package com.leiwuwang.common.util;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtil
{
  private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

  public Properties getProperties(String path) {
    Properties properties = null;
    InputStream in = null;
    try {
      in = getClass().getClassLoader().getResourceAsStream(path);
      properties = new Properties();
      properties.load(in);
    } catch (Exception e) {
      properties = null;
      e.printStackTrace();
    } finally {
      try {
        in.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return properties;
  }

  public static Properties loadProperties(String pathName)
  {
    try
    {
      Properties p = new Properties();
      InputStream in = PropertiesUtil.class.getResourceAsStream(pathName);

      p.load(in);
      in.close();
      return p;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String getPropertyValue(String pathName, String key)
  {
    Properties pro = loadProperties(pathName);
    if (pro != null) {
      return pro.getProperty(key).trim();
    }
    return null;
  }

  public static boolean changeValueByPropertyName(String propertiesFileName, String propertyName, String propertyValue)
  {
    boolean writeOK = true;
    Properties p = new Properties();
    try
    {
      FileInputStream in = new FileInputStream(propertiesFileName);
      p.load(in);
      in.close();
      p.setProperty(propertyName, propertyValue);

      FileOutputStream out = new FileOutputStream(propertiesFileName);

      p.store(out, "Just Test");

      out.flush();

      out.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return writeOK;
  }

  public static void main(String[] args)
  {
    PropertiesUtil propertiesUtil = new PropertiesUtil();
    Properties p = propertiesUtil.getProperties("server.conf");
    logger.info("" + p.get("clazz.server.domain"));
  }
}
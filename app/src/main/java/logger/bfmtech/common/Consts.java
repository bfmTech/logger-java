package logger.bfmtech.common;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.Set;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;

public class Consts {
   public final static String Separator = " \t ";

   public static String GetApplicationLogStr(Level level, String appName, String[] messages, int stack) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
      ApplicationLog applicationLog = new ApplicationLog(appName, level, sdf.format(new Date()),
            String.join(Consts.Separator, messages), GetStack(level == Level.Error ? -1 : stack));
      return applicationLog.toString();
   }

   public static String GetStack(int stack) {
      StackTraceElement[] stackTrace = new Throwable().getStackTrace();
      if (stack == -1) {
         return stackTrace.toString();
      } else {
         if (stackTrace == null || stackTrace.length < 1) {
            return "";
         }
         if (stack < stackTrace.length) {
            return stackTrace[stack].toString();
         }
         return stackTrace[stackTrace.length - 1].toString();
      }
   }

   public static String GetData() {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      return sdf.format(new Date());
   }

   public static String GetDataTime() {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
      return sdf.format(new Date());
   }

   /**
    * 创建文件目录的方法
    * 
    * @param path
    * @throws Exception
    */
   public static void createFileDirectory(String path) throws Exception {
      File file = new File(path);
      if (!file.exists()) {
         Set<PosixFilePermission> perms = EnumSet.noneOf(PosixFilePermission.class);
         perms.add(PosixFilePermission.OWNER_READ);
         perms.add(PosixFilePermission.OWNER_WRITE);
         perms.add(PosixFilePermission.OWNER_EXECUTE);
         FileAttribute<?> attr = PosixFilePermissions.asFileAttribute(perms);
         Files.createDirectories(file.toPath(), attr);
      }
   }

   /**
    * @description: 获取本机的ip地址
    * @return 本机ip地址
    */
   public static String getLocalAddress() {
      try {
         // Traversal Network interface to get the first non-loopback and non-private
         // address
         Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
         ArrayList<String> ipv4Result = new ArrayList<String>();
         ArrayList<String> ipv6Result = new ArrayList<String>();
         while (enumeration.hasMoreElements()) {
            final NetworkInterface networkInterface = enumeration.nextElement();
            final Enumeration<InetAddress> en = networkInterface.getInetAddresses();
            while (en.hasMoreElements()) {
               final InetAddress address = en.nextElement();
               if (!address.isLoopbackAddress()) {
                  if (address instanceof Inet6Address) {
                     ipv6Result.add(normalizeHostAddress(address));
                  } else {
                     ipv4Result.add(normalizeHostAddress(address));
                  }
               }
            }
         }

         // prefer ipv4
         if (!ipv4Result.isEmpty()) {
            for (String ip : ipv4Result) {
               if (ip.startsWith("127.0") || ip.startsWith("192.168")) {
                  continue;
               }

               return ip;
            }

            return ipv4Result.get(ipv4Result.size() - 1);
         } else if (!ipv6Result.isEmpty()) {
            return ipv6Result.get(0);
         }
         // If failed to find,fall back to localhost
         final InetAddress localHost = InetAddress.getLocalHost();
         return normalizeHostAddress(localHost);
      } catch (Exception e) {
         return e.toString();
      }
   }

   public static String normalizeHostAddress(final InetAddress localHost) {
      if (localHost instanceof Inet6Address) {
         return "[" + localHost.getHostAddress() + "]";
      } else {
         return localHost.getHostAddress();
      }
   }

}

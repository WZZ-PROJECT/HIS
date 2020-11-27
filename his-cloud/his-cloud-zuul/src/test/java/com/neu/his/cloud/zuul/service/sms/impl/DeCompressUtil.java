//package com.neu.his.cloud.zuul.service.sms.impl;
//
//
//
//
//import com.github.junrar.Archive;
//import com.github.junrar.rarfile.FileHeader;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.http.util.TextUtils;
//import org.apache.tools.ant.Project;
//import org.apache.tools.ant.taskdefs.Expand;
//import org.apache.tools.zip.ZipEntry;
//import org.apache.tools.zip.ZipOutputStream;
//
//import java.io.*;
//import java.util.zip.*;
//
///**
// * 支持ZIP。RAR文件解压缩
// * @author wzongzheng
// * @since 20201112
// */
//@Slf4j
//public class DeCompressUtil {
//    /**
//     * 解压zip格式压缩包
//     * 对应的是ant.jar
//     */
//    private static void unzip(String sourceZip,String destDir) throws Exception{
//        try{
//            Project p = new Project();
//            Expand e = new Expand();
//            e.setProject(p);
//            e.setSrc(new File(sourceZip));
//            e.setOverwrite(false);
//            e.setDest(new File(destDir));
//               /*
//               ant下的zip工具默认压缩编码为UTF-8编码，
//               而winRAR软件压缩是用的windows默认的GBK或者GB2312编码
//               所以解压缩时要制定编码格式
//               */
//            e.setEncoding("gbk");
//            e.execute();
//        }catch(Exception e){
//            throw e;
//        }
//    }
//    /**
//     * 原来解压rar压缩包的代码 不支持5.0及以上
//     * 解压rar格式压缩包。
//     * 对应的是java-unrar-0.3.jar，但是java-unrar-0.3.jar又会用到commons-logging-1.1.1.jar
//     */
//    private static void unrar(String sourceRar,String destDir) throws Exception{
//        Archive a = null;
//        FileOutputStream fos = null;
//        try{
//            a = new Archive(new File(sourceRar));
//            FileHeader fh = a.nextFileHeader();
//            while(fh!=null){
//                if(!fh.isDirectory()){
//                    //1 根据不同的操作系统拿到相应的 destDirName 和 destFileName
//                    String compressFileName = fh.getFileNameW().isEmpty() ? fh
//                            .getFileNameString() : fh.getFileNameW();
//                   // String compressFileName = fh.getFileNameString().trim();
//                    String destFileName = "";
//                    String destDirName = "";
//                    //非windows系统
//                    if(File.separator.equals("/")){
//                        destFileName = destDir + compressFileName.replaceAll("\\\\", "/");
//                        destDirName = destFileName.substring(0, destFileName.lastIndexOf("/"));
//                        //windows系统
//                    }else{
//                        destFileName = destDir + compressFileName.replaceAll("/", "\\\\");
//                        destDirName = destFileName.substring(0, destFileName.lastIndexOf("\\"));
//                    }
//                    //2创建文件夹
//                    File dir = new File(destDirName);
//                    if(!dir.exists()||!dir.isDirectory()){
//                        dir.mkdirs();
//                    }
//                    //3解压缩文件
//                    fos = new FileOutputStream(new File(destFileName));
//                    a.extractFile(fh, fos);
//                    fos.close();
//                    fos = null;
//                }
//                fh = a.nextFileHeader();
//            }
//            a.close();
//            a = null;
//        }catch(Exception e){
//            throw e;
//        }finally{
//            if(fos!=null){
//                try{fos.close();fos=null;}catch(Exception e){e.printStackTrace();}
//            }
//            if(a!=null){
//                try{a.close();a=null;}catch(Exception e){e.printStackTrace();}
//            }
//        }
//    }
//    public static boolean unrarByWinRAR(String rarFile, String target) {
//        // 这个我安转的winrar。exe的路劲
//        String winrar = "E:\\RAR\\WinRAR.exe";
//        boolean bool = false;
//        File f=new File(rarFile);
//        if(!f.exists()){
//            return false;
//        }
//        String cmd = winrar + " X " + rarFile + " "+target;
//        try {
//            Process proc = Runtime.getRuntime().exec(cmd);
//            int i = proc.waitFor();
//            System.out.println(i);
//            log.info("message:",proc);
//            if (proc.waitFor() != 0) {
//                if (proc.exitValue() == 0) {
//                    bool = false;
//                }
//            } else {
//                bool = true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return bool;
//    }
//    /**
//     * 解压缩
//     * @param sourceFile 要解压的目标文件
//     * @param destDir 要解压到的目标目录
//     */
//    public static void deCompress(String sourceFile,String destDir) throws Exception{
//        //保证文件夹路径最后是"/"或者"\"
//        char lastChar = destDir.charAt(destDir.length()-1);
//        if(lastChar!='/'&&lastChar!='\\'){
//            destDir += File.separator;
//        }
//        //根据类型，进行相应的解压缩
//        String type = sourceFile.substring(sourceFile.lastIndexOf(".")+1);
//        if(type.equals("zip")){
//            DeCompressUtil.unzip(sourceFile, destDir);
//        }else if(type.equals("rar")){
//            DeCompressUtil.unrarByWinRAR(sourceFile, destDir);
//        }else{
//            throw new Exception("只支持zip和rar格式的压缩包！");
//        }
//    }
//
//    /**
//     * zip压缩 解压
//     */
//    /**
//     * 解压文件到指定文件夹
//     *
//     * @param zip      源文件
//     * @param destPath 目标文件夹路径
//     * @throws Exception 解压失败
//     */
//    public static void decompress(String zip, String destPath) throws Exception {
//        //参数检查
//        if (TextUtils.isEmpty(zip) || TextUtils.isEmpty(destPath)) {
//            throw new IllegalArgumentException("zip or destPath is illegal");
//        }
//        File zipFile = new File(zip);
//        if (!zipFile.exists()) {
//            throw new FileNotFoundException("zip file is not exists");
//        }
//        File destFolder = new File(destPath);
//        if (!destFolder.exists()) {
//            if (!destFolder.mkdirs()) {
//                throw new FileNotFoundException("destPath mkdirs is failed");
//            }
//        }
//        ZipInputStream zis = null;
//        BufferedOutputStream bos = null;
//        try {
//            zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zip)));
//            ZipEntry ze;
//            while ((ze = (ZipEntry) zis.getNextEntry()) != null) {
//                //得到解压文件在当前存储的绝对路径
//                String filePath = destPath + File.separator + ze.getName();
//                if (ze.isDirectory()) {
//                    new File(filePath).mkdirs();
//                } else {
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    byte[] buffer = new byte[1024];
//                    int count;
//                    while ((count = zis.read(buffer)) != -1) {
//                        baos.write(buffer, 0, count);
//                    }
//                    byte[] bytes = baos.toByteArray();
//                    File entryFile = new File(filePath);
//                    //创建父目录
//                    if (!entryFile.getParentFile().exists()) {
//                        if (!entryFile.getParentFile().mkdirs()) {
//                            throw new FileNotFoundException("zip entry mkdirs is failed");
//                        }
//                    }
//                    //写文件
//                    bos = new BufferedOutputStream(new FileOutputStream(entryFile));
//                    bos.write(bytes);
//                    bos.flush();
//                }
//
//            }
//        } finally {
//            closeQuietly(zis);
//            closeQuietly(bos);
//        }
//    }
//
//    /**
//     * @param srcPath  源文件的绝对路径，可以为文件或文件夹
//     * @param destPath 目标文件的绝对路径  /sdcard/.../file_name.zip
//     * @throws Exception 解压失败
//     */
//    public static void compress(String srcPath, String destPath) throws Exception {
//        //参数检查
//        if (TextUtils.isEmpty(srcPath) || TextUtils.isEmpty(destPath)) {
//            throw new IllegalArgumentException("srcPath or destPath is illegal");
//        }
//        File srcFile = new File(srcPath);
//        if (!srcFile.exists()) {
//            throw new FileNotFoundException("srcPath file is not exists");
//        }
//        File destFile = new File(destPath);
//        if (destFile.exists()) {
//            if (!destFile.delete()) {
//                throw new IllegalArgumentException("destFile is exist and do not delete.");
//            }
//        }
//
//        CheckedOutputStream cos = null;
//        ZipOutputStream zos = null;
//        try {
//            // 对目标文件做CRC32校验，确保压缩后的zip包含CRC32值
//            cos = new CheckedOutputStream(new FileOutputStream(destPath), new CRC32());
//            //装饰一层ZipOutputStream，使用zos写入的数据就会被压缩啦
//            zos = new ZipOutputStream(cos);
//            zos.setLevel(9);//设置压缩级别 0-9,0表示不压缩，1表示压缩速度最快，9表示压缩后文件最小；默认为6，速率和空间上得到平衡。
//            if (srcFile.isFile()) {
//                compressFile("", srcFile, zos);
//            } else if (srcFile.isDirectory()) {
//                compressFolder("", srcFile, zos);
//            }
//        } finally {
//            closeQuietly(zos);
//        }
//    }
//
//    /**
//     * 格式压缩文件和空目录
//     *
//     * @param prefix
//     * @param src
//     * @param zos
//     * @throws IOException
//     */
//    private static void compressFile(String prefix, File src, ZipOutputStream zos) throws IOException {
//        //若是文件,那肯定是对单个文件压缩
//        //ZipOutputStream在写入流之前，需要设置一个zipEntry
//        //注意这里传入参数为文件在zip压缩包中的路径，所以只需要传入文件名即可
//        String relativePath = prefix + src.getName();
//        if (src.isDirectory()) relativePath += "/";//处理空文件夹
//        ZipEntry entry = new ZipEntry(relativePath);
//        //写到这个zipEntry中，可以理解为一个压缩文件
//        zos.putNextEntry(entry);
//        InputStream is = null;
//        try {
//            if (src.isFile()) {
//                is = new FileInputStream(src);
//                byte[] buffer = new byte[1024 << 3];
//                int len = 0;
//                while ((len = is.read(buffer)) != -1) {
//                    zos.write(buffer, 0, len);
//                }
//            }
//            //该文件写入结束
//            zos.closeEntry();
//        } finally {
//            closeQuietly(is);
//        }
//    }
//
//    /**
//     * 关闭流操作
//     * @param closeable
//     */
//    private static void closeQuietly(final Closeable closeable) {
//        try {
//            if (closeable != null) {
//                closeable.close();
//            }
//        } catch (final IOException ioe) {
//            // ignore
//        }
//    }
//
//    /**
//     * Gzip压缩 解压
//     */
//    /**
//     * 压缩
//     * @param prefix
//     * @param srcFolder
//     * @param zos
//     * @throws IOException
//     */
//    static void compressFolder(String prefix, File srcFolder, ZipOutputStream zos) throws IOException {
//        String new_prefix = prefix + srcFolder.getName() + "/";
//        File[] files = srcFolder.listFiles();
//        //支持空文件夹
//        if (files.length == 0) {
//            compressFile(prefix, srcFolder, zos);
//        } else {
//            for (File file : files) {
//                if (file.isFile()) {
//                    compressFile(new_prefix, file, zos);
//                } else if (file.isDirectory()) {
//                    compressFolder(new_prefix, file, zos);
//                }
//            }
//        }
//    }
//
//    /**
//     * 解压文件到指定文件夹
//     *
//     * @param gzip     源文件
//     * @param destPath 目标文件绝对路径
//     * @throws Exception 解压失败
//     */
//    public static void decompressGZip(String gzip, String destPath) throws Exception {
//        //参数检查
//        if (TextUtils.isEmpty(gzip) || TextUtils.isEmpty(destPath)) {
//            throw new IllegalArgumentException("gzip or destPath is illegal");
//        }
//        File gzipFile = new File(gzip);
//        if (!gzipFile.exists()) {
//            throw new FileNotFoundException("gzip file is not exists");
//        }
//        File destFile = new File(destPath);
//        if (destFile.exists()) {
//            if (!destFile.delete()) {
//                throw new FileNotFoundException("destFile delete is failed");
//            }
//        }
//
//        GZIPInputStream zis = null;
//        BufferedOutputStream bos = null;
//        try {
//            zis = new GZIPInputStream(new BufferedInputStream(new FileInputStream(gzipFile)));
//            bos = new BufferedOutputStream(new FileOutputStream(destPath));
//            byte[] buffer = new byte[1024 << 3];
//            int len = 0;
//            while ((len = zis.read(buffer)) != -1) {
//                bos.write(buffer, 0, len);
//            }
//            bos.flush();
//        } finally {
//            closeQuietly(zis);
//            closeQuietly(bos);
//        }
//    }
//
//    /**
//     * @param srcPath
//     * @param destPath
//     * @throws Exception
//     */
//    public static void compressGZip(String srcPath, String destPath) throws Exception {
//        //参数检查
//        if (TextUtils.isEmpty(srcPath) || TextUtils.isEmpty(destPath)) {
//            throw new IllegalArgumentException("srcPath or destPath is illegal");
//        }
//        File srcFile = new File(srcPath);
//        if (!srcFile.exists() || srcFile.isDirectory()) {
//            throw new FileNotFoundException("srcPath file is not exists");
//        }
//        File destFile = new File(destPath);
//        if (destFile.exists()) {
//            if (!destFile.delete()) {
//                throw new IllegalArgumentException("destFile is exist and do not delete.");
//            }
//        }
//        GZIPOutputStream zos = null;
//        InputStream is = null;
//        try {
//            zos = new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(destPath)));
//            is = new FileInputStream(srcFile);
//            byte[] buffer = new byte[1024 << 3];
//            int len = 0;
//            while ((len = is.read(buffer)) != -1) {
//                zos.write(buffer, 0, len);
//            }
//            zos.flush();
//        } finally {
//            closeQuietly(is);
//            closeQuietly(zos);
//        }
//    }
//
//
//    /**
//     * ZIP RAR 压缩
//     */
//    /**
//     * @param path   要压缩的文件路径
//     * @param format 生成的格式（zip、rar）d
//     */
//    public static void generateFile(String path, String format) throws Exception {
//
//        File file = new File(path);
//        // 压缩文件的路径不存在
//        if (!file.exists()) {
//            throw new Exception("路径 " + path + " 不存在文件，无法进行压缩...");
//        }
//        // 用于存放压缩文件的文件夹
//        String generateFile = file.getParent() + File.separator +"CompressFile";
//        File compress = new File(generateFile);
//        // 如果文件夹不存在，进行创建
//        if( !compress.exists() ){
//            compress.mkdirs();
//        }
//
//        // 目的压缩文件
//        String generateFileName = compress.getAbsolutePath() + File.separator + "AAA" + file.getName() + "." + format;
//
//        // 输入流 表示从一个源读取数据
//        // 输出流 表示向一个目标写入数据
//
//        // 输出流
//        FileOutputStream outputStream = new FileOutputStream(generateFileName);
//
//        // 压缩输出流
//        ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(outputStream));
//
//        generateFile(zipOutputStream,file,"");
//
//        System.out.println("源文件位置：" + file.getAbsolutePath() + "，目的压缩文件生成位置：" + generateFileName);
//        // 关闭 输出流
//        zipOutputStream.close();
//    }
//
//    /**
//     * @param out  输出流
//     * @param file 目标文件
//     * @param dir  文件夹
//     * @throws Exception
//     */
//    private static void generateFile(ZipOutputStream out, File file, String dir) throws Exception {
//
//        // 当前的是文件夹，则进行一步处理
//        if (file.isDirectory()) {
//            //得到文件列表信息
//            File[] files = file.listFiles();
//
//            //将文件夹添加到下一级打包目录
//            out.putNextEntry(new ZipEntry(dir + "/"));
//
//            dir = dir.length() == 0 ? "" : dir + "/";
//
//            //循环将文件夹中的文件打包
//            for (int i = 0; i < files.length; i++) {
//                generateFile(out, files[i], dir + files[i].getName());
//            }
//
//        } else { // 当前是文件
//
//            // 输入流
//            FileInputStream inputStream = new FileInputStream(file);
//            // 标记要打包的条目
//            out.putNextEntry(new ZipEntry(dir));
//            // 进行写操作
//            int len = 0;
//            byte[] bytes = new byte[1024];
//            while ((len = inputStream.read(bytes)) > 0) {
//                out.write(bytes, 0, len);
//            }
//            // 关闭输入流
//            inputStream.close();
//        }
//
//    }
//
//
//    public static void main(String[] args) throws Exception {
//
//
//        // path 是你下载下来的压缩包 其中的文件名需要根据你自己的文件名来定，动态传参，因为你不确定你接到的.zip文件叫什么名子
//        // 刚才有问题的文件 不过没关系
//        String path = "E:\\(log)2020444.rar";
//        // path2 是你需要解压到你指定的目录的路径 但是文件目录的名称也需要根据你的需求来动态生成，这里写死了。、
//        // 指定一个目录
//        String path2 = "E:\\BaiduNetdiskDownload";
//
//        // 这里调用
//
//        DeCompressUtil.unrarByWinRAR(path,path2);
//
//    }
//
//// 所需要的依赖包
////
////      <!-- https://mvnrepository.com/artifact/commons-net/commons-net -->
////        <dependency>
////            <groupId>commons-net</groupId>
////            <artifactId>commons-net</artifactId>
////            <version>3.7.2</version>
////        </dependency>
////        <!-- https://mvnrepository.com/artifact/commons-logging/commons-logging -->
////        <dependency>
////            <groupId>commons-logging</groupId>
////            <artifactId>commons-logging</artifactId>
////            <version>1.2</version>
////        </dependency>
////        <!-- https://mvnrepository.com/artifact/ant/ant -->
////        <dependency>
////            <groupId>ant</groupId>
////            <artifactId>ant</artifactId>
////            <version>1.7.0</version>
////        </dependency>
////        <!-- https://mvnrepository.com/artifact/com.github.junrar/junrar -->
////        <dependency>
////            <groupId>com.github.junrar</groupId>
////            <artifactId>junrar</artifactId>
////            <version>7.4.0</version>
////        </dependency>
//
//}

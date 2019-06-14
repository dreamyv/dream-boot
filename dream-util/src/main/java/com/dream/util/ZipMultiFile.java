package com.dream.util;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 压缩文件工具类
 */
public class ZipMultiFile {

    private static Logger logger = LoggerFactory.getLogger(ZipMultiFile.class);

    private static final int  BUFFER_SIZE = 2 * 1024;

    /**
     * 压缩多个文件
     * 把多个文件压缩到同一个压缩包(文件名重复会报错)
     */
    public static void zipFiles(List<File> srcFiles, File zipFile) throws IOException {
        FileOutputStream fileOutputStream = null;
        ZipOutputStream zipOutputStream = null;
        ZipEntry zipEntry = null; // 创建 ZipEntry 对象
        FileInputStream fileInputStream = null;
        try {
            // 判断压缩后的文件存在不，不存在则创建
            if (!zipFile.exists()) {
                zipFile.createNewFile();
            }
            fileOutputStream = new FileOutputStream(zipFile); // 实例化 FileOutputStream 对象
            zipOutputStream = new ZipOutputStream(fileOutputStream); // 实例化 ZipOutputStream 对象
            // 遍历源文件数组
            for (int i = 0; i < srcFiles.size(); i++) {
                // 将源文件数组中的当前文件读入 FileInputStream 流中
                if (!srcFiles.get(i).exists()) {
                    continue;
                }
                fileInputStream = new FileInputStream(srcFiles.get(i));
                // 实例化 ZipEntry 对象，源文件数组中的当前文件
                zipEntry = new ZipEntry(srcFiles.get(i).getName());
                zipOutputStream.putNextEntry(zipEntry);
                // 该变量记录每次真正读的字节个数
                int len;
                // 定义每次读取的字节数组
                byte[] buffer = new byte[1024];
                while ((len = fileInputStream.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, len);
                }
            }
        } catch (Exception e) {
            logger.error("打压缩包失败!",e);
            throw e;
        }finally {
            try {
                if (zipOutputStream != null) {
                    zipOutputStream.closeEntry();
                    zipOutputStream.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            }catch (Exception e){}
        }
    }

    public static void compress(ZipOutputStream out, BufferedOutputStream bos, File sourceFile, String base) throws Exception {
        //如果路径为目录（文件夹）
        if (sourceFile.isDirectory()) {
            //取出文件夹中的文件（或子文件夹）
            File[] flist = sourceFile.listFiles();
            //如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
            if (flist.length == 0){
                out.putNextEntry(new ZipEntry(base + "/"));
            } else{
                //如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
                for (int i = 0; i < flist.length; i++) {
                    compress(out, bos, flist[i], base + "/" + flist[i].getName());
                }
            }
        } else{
            //如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
            out.putNextEntry(new ZipEntry(base));
            FileInputStream fos = new FileInputStream(sourceFile);
            BufferedInputStream bis = new BufferedInputStream(fos);
            int tag;
            //将源文件写入到zip文件中
            while ((tag = bis.read()) != -1) {
                out.write(tag);
            }
            bis.close();
            fos.close();
        }
    }

    /**
     * 压缩成ZIP 方法1
     * @param srcDir  压缩文件夹路径
     * @param zipPath 压缩文件保存后的路徑
     * @param KeepDirStructure  是否保留原来的目录结构,true:保留目录结构;
     *                          false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static void toZip(String srcDir, String zipPath, boolean KeepDirStructure) throws RuntimeException{
        ZipOutputStream zos = null ;
        FileOutputStream zipFileOut = null;
        try {
            zipFileOut = new FileOutputStream(new File(zipPath));
            zos = new ZipOutputStream(zipFileOut);
            File sourceFile = new File(srcDir);
            compressZip(sourceFile,zos,sourceFile.getName(),KeepDirStructure);
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils",e);
        }finally{
            try {
                if(zos != null){
                    zos.close();
                }
                if(zipFileOut != null){
                    zipFileOut.close();
                }
            } catch (IOException e) {}
        }

    }

    /**
     * 递归压缩方法
     * @param sourceFile 源文件
     * @param zos        zip输出流
     * @param name       压缩后的名称
     * @param KeepDirStructure  是否保留原来的目录结构,true:保留目录结构;
     *                          false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     */
    private static void compressZip(File sourceFile, ZipOutputStream zos, String name,
                                    boolean KeepDirStructure) throws Exception{
        byte[] buf = new byte[BUFFER_SIZE];
        if(sourceFile.isFile()){
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1){
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if(listFiles == null || listFiles.length == 0){
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if(KeepDirStructure){
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }

            }else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compressZip(file, zos, name + "/" + file.getName(),KeepDirStructure);
                    } else {
                        compressZip(file, zos, file.getName(),KeepDirStructure);
                    }

                }
            }
        }
    }


    /**
     * 解压tar.gz文件
     * @param tar_gz 目标源对象
     * @param sourceFolder 解压后的目录
     */
    public static void deCompressTar(File tar_gz,String sourceFolder){
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        GZIPInputStream gzis = null;
        TarArchiveInputStream tais = null;
        OutputStream out = null;
        try {
            fis = new FileInputStream(tar_gz);
            bis = new BufferedInputStream(fis);
            gzis = new GZIPInputStream(bis);
            tais = new TarArchiveInputStream(gzis);
            TarArchiveEntry tae = null;
            boolean flag = false;
            while((tae = tais.getNextTarEntry()) != null ){
                File tmpFile = new File(sourceFolder+"/"+tae.getName());
                if(!flag){
                    //使用 mkdirs 可避免因文件路径过多而导致的文件找不到的异常
                    new File(tmpFile.getParent()).mkdirs();
                    flag = true;
                }
                out = new FileOutputStream(tmpFile);
                int length = 0;
                byte[] b = new byte[BUFFER_SIZE];
                while((length = tais.read(b)) != -1){
                    out.write(b, 0, length);
                }
            }
        } catch (Exception e) {
           logger.error("解压tar.gz文件异常!",e);
        }finally{
            try {
                if(tais != null)  tais.close();
                if(gzis != null) gzis.close();
                if(bis != null) bis.close();
                if(fis != null) fis.close();
                if(out != null){
                    out.flush();
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 压缩tar.gz文件
     * @param list 目标源文件
     * @param outPutPath 存储路径
     * @return
     */
    public static File compressTar(List<File> list, String outPutPath,String folder){
        File outPutFile = null;
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        GZIPOutputStream gzp = null;
        //tar包对象,最后删除
        File tar = new File("\\tmp\\tmp.tar");
        try {
            //先打成tar包，再压缩成gz
            fis = new FileInputStream(pack(list,tar,folder));
            bis = new BufferedInputStream(fis,BUFFER_SIZE);
            outPutFile = new File(outPutPath+".tar.gz");
            fos = new FileOutputStream(outPutFile);
            gzp = new GZIPOutputStream(fos);
            int count;
            byte data[] = new byte[BUFFER_SIZE];
            while ((count = bis.read(data, 0, BUFFER_SIZE)) != -1) {
                gzp.write(data, 0, count);
            }
        } catch (Exception e) {
            logger.error("压缩tar文件异常!",e);
        }finally{
            try {
                if(gzp != null){
                    gzp.finish();
                    gzp.flush();
                    gzp.close();
                }
                if(fos != null)  fos.close();
                if(bis != null)  bis.close();
                if(fis != null)  fis.close();
                if(tar.exists()){
                    tar.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return outPutFile;
    }

    /**
     * 将文件集合压缩成tar包后返回
     * @param files   要压缩的文件集合
     * @param target  tar.输出流的目标文件
     * @return File  指定返回的目标文件
     */
    private static File pack(List<File> files, File target,String folder){
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        TarArchiveOutputStream taos = null;
        FileInputStream fis = null;
        try {
            fos  = new FileOutputStream(target);
            bos = new BufferedOutputStream(fos,BUFFER_SIZE);
            taos = new TarArchiveOutputStream(bos);
            //解决文件名过长问题
            taos.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
            for(File file : files){
                TarArchiveEntry tar = new TarArchiveEntry(file);
                //重置名称,默认是带全路径的
                if(StringUtils.isEmpty(folder)){
                    tar.setName(file.getName());
                }else{
                    tar.setName(folder+"/"+file.getName());
                }
                taos.putArchiveEntry(tar);
                fis = new FileInputStream(file);
                IOUtils.copy(fis, taos);
                taos.closeArchiveEntry();
                fis.close();//把每个实现都关闭
            }
        } catch (Exception e) {
            logger.error("将文件集合压缩成tar包后返回异常!",e);
        }finally{
            try {
                if(fis != null) {
                    fis.close();
                }
                if(taos != null){
                    taos.finish();
                    taos.flush();
                    taos.close();
                }
                if(bos != null){
                    bos.flush();
                    bos.close();
                }
                if(fos != null){
                    fos.flush();
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return target;
    }
}

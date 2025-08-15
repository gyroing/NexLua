package com.nexlua;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import dalvik.system.DexFile;

public final class LuaUtil {

    private LuaUtil() {
    }

    private static Context applicationContext;

    public static Context getApplicationContext() {
        if (applicationContext == null) {
            applicationContext = LuaApplication.getInstance();
        }
        return applicationContext;
    }

    public static byte[] readAsset(Context context, String name) throws IOException {
        try (InputStream is = context.getAssets().open(name)) {
            return readAll(is);
        }
    }

    public static byte[] readAll(InputStream input) throws IOException {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream(8192)) {
            byte[] buffer = new byte[8192];
            int n;
            while ((n = input.read(buffer)) != -1) {
                output.write(buffer, 0, n);
            }
            return output.toByteArray();
        }
    }

    public static ByteBuffer readAll(InputStream input, int size) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocateDirect(size);
        byte[] temp = new byte[8192];
        int n;
        while ((n = input.read(temp)) != -1) {
            buffer.put(temp, 0, n);
        }
        buffer.flip();
        return buffer;
    }

    public static byte[] readAll(String path) throws IOException {
        try (FileInputStream fis = new FileInputStream(path)) {
            return readAll(fis);
        }
    }

    public static byte[] readAll(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return readAll(fis);
        }
    }

    public static void assetsToSD(Context context, String InFileName, String OutFileName) throws IOException {
        try (InputStream myInput = context.getAssets().open(InFileName);
             OutputStream myOutput = new FileOutputStream(OutFileName)) {
            copyStream(myInput, myOutput);
            myOutput.flush();
        }
    }

    public static void copyFile(String from, String to) throws IOException {
        try (InputStream in = new FileInputStream(from);
             OutputStream out = new FileOutputStream(to)) {
            copyStream(in, out);
        }
    }

    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[8192];
        int byteread;
        while ((byteread = in.read(buffer)) != -1) {
            out.write(buffer, 0, byteread);
        }
    }

    public static void copyDir(String from, String to) throws IOException {
        copyDir(new File(from), new File(to));
    }

    public static void copyDir(File from, File to) throws IOException {
        if (from.isDirectory()) {
            to.mkdirs(); // Attempt to create destination directory
            File[] files = from.listFiles();
            if (files != null) {
                for (File file : files) {
                    copyDir(file, new File(to, file.getName()));
                }
            }
        } else {
            File destFile = to;
            if (to.isDirectory()) {
                destFile = new File(to, from.getName());
            }
            try (InputStream in = new FileInputStream(from);
                 OutputStream out = new FileOutputStream(destFile)) {
                copyStream(in, out);
            }
        }
    }

    public static boolean rmDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    rmDir(file);
                }
            }
        }
        return dir != null && dir.delete();
    }

    public static byte[] readZip(String zippath, String filepath) throws IOException {
        try (ZipFile zip = new ZipFile(zippath)) {
            ZipEntry entry = zip.getEntry(filepath);
            // Let zip.getInputStream(entry) throw NullPointerException if entry is null,
            // which will be caught and re-thrown as an IOException by the caller if not handled.
            // This adheres to the "let it fail naturally" principle.
            try (InputStream is = zip.getInputStream(entry)) {
                return readAll(is);
            }
        }
    }

    private static String getFileHash(InputStream in, String algorithm) throws IOException, NoSuchAlgorithmException {
        try (InputStream inputStream = in) {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] buffer = new byte[8192];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return String.format("%032x", bigInt);
        }
    }

    public static String getFileMD5(String file) throws IOException, NoSuchAlgorithmException {
        return getFileHash(new FileInputStream(file), "MD5");
    }

    public static String getFileMD5(File file) throws IOException, NoSuchAlgorithmException {
        return getFileHash(new FileInputStream(file), "MD5");
    }

    public static String getFileMD5(InputStream in) throws IOException, NoSuchAlgorithmException {
        return getFileHash(in, "MD5");
    }

    public static String getFileSha1(String file) throws IOException, NoSuchAlgorithmException {
        return getFileHash(new FileInputStream(file), "SHA-1");
    }

    public static String getFileSha1(File file) throws IOException, NoSuchAlgorithmException {
        return getFileHash(new FileInputStream(file), "SHA-1");
    }

    public static String getFileSha1(InputStream in) throws IOException, NoSuchAlgorithmException {
        return getFileHash(in, "SHA-1");
    }

    public static String getMessageDigest(String in, String algorithm) throws NoSuchAlgorithmException {
        if (in == null) return null;
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        digest.update(in.getBytes());
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return String.format("%032x", bigInt);
    }

    public static String getMD5(String in) throws NoSuchAlgorithmException {
        return getMessageDigest(in, "MD5");
    }

    public static String getSha1(String in) throws NoSuchAlgorithmException {
        return getMessageDigest(in, "SHA-1");
    }

    public static String[] getAllName(Context context, String path) throws IOException {
        List<String> ret = new ArrayList<>();
        DexFile dex = null;
        try {
            dex = new DexFile(context.getPackageCodePath());
            Enumeration<String> cls = dex.entries();
            while (cls.hasMoreElements()) {
                ret.add(cls.nextElement());
            }
        } finally {
            if (dex != null) {
                try {
                    dex.close();
                } catch (IOException ignored) {
                }
            }
        }
        try (ZipFile zip = new ZipFile(path)) {
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                if (name.endsWith(".class")) {
                    ret.add(name.replaceAll("/", ".").replace(".class", ""));
                }
            }
        }
        return ret.toArray(new String[0]);
    }

    public static void unZip(String SourceDir, String extDir, String fileExt) throws IOException {
        File extDirFile = new File(extDir);
        extDirFile.mkdirs();

        try (ZipFile zip = new ZipFile(SourceDir)) {
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();
                if (!name.startsWith(fileExt)) continue;

                File outputFile = new File(extDir, name);
                if (entry.isDirectory()) {
                    outputFile.mkdirs();
                } else {
                    File parentDir = outputFile.getParentFile();
                    if (parentDir != null) {
                        parentDir.mkdirs();
                    }
                    try (InputStream in = zip.getInputStream(entry);
                         FileOutputStream out = new FileOutputStream(outputFile)) {
                        copyStream(in, out);
                    }
                }
            }
        }
    }

    public static void zip(String sourceFilePath, String zipFilePath, String zipFileName) throws IOException {
        File zipFile = new File(zipFilePath, zipFileName);
        zipFile.getParentFile().mkdirs();

        try (FileOutputStream dest = new FileOutputStream(zipFile);
             CheckedOutputStream checksum = new CheckedOutputStream(dest, new Adler32());
             ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(checksum))) {

            compress(new File(sourceFilePath), out, "");
        }
    }

    private static void compress(File file, ZipOutputStream out, String baseDir) throws IOException {
        if (file.isDirectory()) {
            File[] fs = file.listFiles();
            if (fs != null) {
                for (File f : fs) {
                    compress(f, out, baseDir + file.getName() + "/");
                }
            }
        } else {
            try (FileInputStream fi = new FileInputStream(file);
                 BufferedInputStream origin = new BufferedInputStream(fi, 8192)) {
                ZipEntry entry = new ZipEntry(baseDir + file.getName());
                out.putNextEntry(entry);
                byte[] data = new byte[8192];
                int count;
                while ((count = origin.read(data)) != -1) {
                    out.write(data, 0, count);
                }
            }
        }
    }

    public static String readZipFile(String zippath, String filepath) throws IOException {
        byte[] data = readZip(zippath, filepath);
        return new String(data);
    }

    public static String readApkFile(String filepath) throws IOException {
        try (ZipFile zip = new ZipFile(getApplicationContext().getPackageCodePath())) {
            // Let getInputStream throw if entry is not found.
            ZipEntry entry = zip.getEntry(filepath);
            try (InputStream is = zip.getInputStream(entry)) {
                return new String(readAll(is));
            }
        }
    }
}
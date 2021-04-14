package src.rsa;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;


public class DesImageSolution {

    private static final String
            CBC = "CBC",
            SUTD = "SUTD.bmp",
            TRIANGLE = "triangle.bmp";

    public static void main(String[] args) {
        try {
            encryptImage(SUTD, DesSolution.ECB);
            encryptImage(TRIANGLE, DesSolution.ECB);
            encryptImage(SUTD, CBC);
            encryptImage(TRIANGLE, CBC);
            encryptBottomUpImage(TRIANGLE, DesSolution.ECB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void encryptBottomUpImage(String file, String type) throws IOException {
        int image_width = 200;
        int image_length = 200;
        // read image file and save pixel value into int[][] imageArray
        BufferedImage img = ImageIO.read(new File(String.format("%s%s", DesSolution.DIRECTORY, file)));
        image_width = img.getWidth();
        image_length = img.getHeight();
        // byte[][] imageArray = new byte[image_width][image_length];
        int[][] imageArray = new int[image_width][image_length];
        for (int idx = image_width - 1; idx > -1; idx--) {
            for (int idy = image_length - 1; idy > -1; idy--) {
                int color = img.getRGB(idx, idy);
                imageArray[idx][idy] = color;
            }
        }

        DesSolution desSoln = new DesSolution();
        // TODO: generate secret key using DES algorithm
        SecretKey key = desSoln.generateSecretKey();

        // TODO: Create cipher object, initialize the ciphers with the given key, choose encryption algorithm/mode/padding,
        Cipher cipher = desSoln.initCipher(type, Cipher.ENCRYPT_MODE, key);

        //you need to try both ECB and CBC mode, use PKCS5Padding padding method

        // define output BufferedImage, set size and format
        BufferedImage outImage = new BufferedImage(image_width, image_length, BufferedImage.TYPE_3BYTE_BGR);

        for (int idx = image_width - 1; idx > -1; idx--) {
            // convert each column int[] into a byte[] (each_width_pixel)
            byte[] each_width_pixel = new byte[4 * image_length];
            for (int idy = image_length - 1; idy > -1; idy--) {
                ByteBuffer dbuf = ByteBuffer.allocate(4);
                dbuf.putInt(imageArray[idx][idy]);
                byte[] bytes = dbuf.array();
                System.arraycopy(bytes, 0, each_width_pixel, idy * 4, 4);
            }
            // TODO: encrypt each column or row bytes
            byte[] encryptedBytesArray = desSoln.generateByte(cipher, each_width_pixel);

            // TODO: convert the encrypted byte[] back into int[] and write to outImage (use setRGB)
            int[] arr = new int[encryptedBytesArray.length];
            for (int i = encryptedBytesArray.length - 1; i > -1; i--) {
                arr[i] = encryptedBytesArray[i];
            }

            for (int i = image_length - 1; i > -1; i--) {
                outImage.setRGB(idx, i, arr[i]);
            }
        }

        //write outImage into file
        ImageIO.write(outImage, "BMP", new File("triangle_new.bmp"));
    }

    private static void encryptImage(String file, String type) throws IOException {
        int image_width = 200;
        int image_length = 200;
        // read image file and save pixel value into int[][] imageArray
        BufferedImage img = ImageIO.read(new File(String.format("%s%s", DesSolution.DIRECTORY, file)));
        image_width = img.getWidth();
        image_length = img.getHeight();
        // byte[][] imageArray = new byte[image_width][image_length];
        int[][] imageArray = new int[image_width][image_length];
        for (int idx = 0; idx < image_width; idx++) {
            for (int idy = 0; idy < image_length; idy++) {
                int color = img.getRGB(idx, idy);
                imageArray[idx][idy] = color;
            }
        }

        DesSolution desSoln = new DesSolution();
        // TODO: generate secret key using DES algorithm
        SecretKey key = desSoln.generateSecretKey();

        // TODO: Create cipher object, initialize the ciphers with the given key, choose encryption algorithm/mode/padding,
        Cipher cipher = desSoln.initCipher(type, Cipher.ENCRYPT_MODE, key);

        //you need to try both ECB and CBC mode, use PKCS5Padding padding method

        // define output BufferedImage, set size and format
        BufferedImage outImage = new BufferedImage(image_width, image_length, BufferedImage.TYPE_3BYTE_BGR);

        for (int idx = 0; idx < image_width; idx++) {
            // convert each column int[] into a byte[] (each_width_pixel)
            byte[] each_width_pixel = new byte[4 * image_length];
            for (int idy = 0; idy < image_length; idy++) {
                ByteBuffer dbuf = ByteBuffer.allocate(4);
                dbuf.putInt(imageArray[idx][idy]);
                byte[] bytes = dbuf.array();
                System.arraycopy(bytes, 0, each_width_pixel, idy * 4, 4);
            }
            // TODO: encrypt each column or row bytes
            byte[] encryptedBytesArray = desSoln.generateByte(cipher, each_width_pixel);

            // TODO: convert the encrypted byte[] back into int[] and write to outImage (use setRGB)
            IntBuffer intBuf =
                    ByteBuffer.wrap(encryptedBytesArray)
                            .order(ByteOrder.BIG_ENDIAN)
                            .asIntBuffer();
            int[] arr = new int[intBuf.remaining()];
            intBuf.get(arr);

            for (int i = 0; i < image_length; i++) {
                outImage.setRGB(idx, i, arr[i]);
            }
        }

        //write outImage into file
        ImageIO.write(outImage, "BMP", new File(String.format("En%s%s", type, file)));
    }
}
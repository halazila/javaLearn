package com.ba;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import java.awt.image.ColorConvertOp;

public class KaptchaImage {

	public static void main(String[] args) throws IOException {
		DefaultKaptcha kaptcha = new DefaultKaptcha();
		kaptcha.setConfig(new Config(new Properties()));
		Config con = new Config(new Properties());
		System.out.println(con.getTextProducerCharString());
		System.out.println(con.getTextProducerFonts(10)[1]);

		for (int i = 0; i < 100; i++) {
			String capText = kaptcha.createText();
			File file = new File(
					args[0] + "\\" + capText + ".jpg");
			BufferedImage bimg = kaptcha.createImage(capText);
			ImageIO.write(bimg, "jpg", file);
		}

		// String[] text = { "abcde", "23456", "78gfy", "nmpwx" };
		// for (int i = 0; i < text.length; i++) {
		// File file = new
		// File("C:\\Users\\xiong.baohua.SFIT\\Desktop\\jTessBoxEditorFX\\out" + i +
		// ".jpg");
		// BufferedImage img = kaptcha.createImage(text[i]);
		// img = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY),
		// null).filter(img, null);
		// ImageIO.write(img, "jpg", file);
		// }

	}
}

package Main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import FeatureExtract.FqImage;
import JniInterface.PicSegment;
import JniInterface.moduleInterface;
import SvmLib.svm;
import SvmLib.svm_model;
import SvmLib.svm_node;

public class Online {

	/**
	 * @param args
	 */
	//�Լ��ҵ���java������ȡ������Ҫ��ֵͼ
	public static double[] getMyFeature(String picPath){
		BufferedImage im;
		try {
			im = ImageIO.read(new File(picPath));
		} catch (IOException e) {
			return null;
		}
		FqImage imh = new FqImage(im);
		double[] feature = new double[7];
		int count = 0;
		for (int i = 1; i <= 7; i++) {
			feature[count++] = imh.huJu[i];
		}
		return feature;
	}
	//�Լ��ķָ�ԭͼ�õ���ֵͼ������ʦ�ֵ�������ȡ
	public static double[] getDllFeature(String imagePath) {
		int pos = imagePath.lastIndexOf('.');
		String imageBinary = imagePath.substring(0,pos)+"_b"+imagePath.substring(pos);
		new PicSegment().startSegment(imagePath, imageBinary);
		moduleInterface jniinterface = new moduleInterface();
		double[] Shaperesult = new double[100];
		int[] Shapedimension = new int[1];
		double[] Textureresult = new double[300];
		int[] Texturedimension = new int[1];
		double[] result;
		// ����������ȡģ��
		jniinterface.IFE_Shape(imagePath, imageBinary, Shaperesult,
				Shapedimension);
		jniinterface.IFE_Texture(imagePath, imageBinary, Textureresult,
				Texturedimension);
		result = new double[Shapedimension[0]+Texturedimension[0]];
		int count = 0;
		for (int i = 0; i < Shapedimension[0]; i++){
			result[count++] = Shaperesult[i];
		}
		for (int i = 0; i < Texturedimension[0]; i++){
			result[count++] = Textureresult[i];
		}
		return result;
	}
	public static void main(String[] args) {
		try {
			//����
			File[] files;
			double[] feature;
			String imagePath;
			svm_model model = svm.svm_load_model(Common.modelPath);
			files = new File(Common.tmpPicPath).listFiles();
			for (int i = 0; i < files.length; i++) {
				imagePath = files[i].getAbsolutePath();
				if (imagePath.charAt(imagePath.lastIndexOf('.')-1) != 'b') {
					feature = Online.getDllFeature(imagePath);
					svm_node[] x = new svm_node[feature.length];
					for (int j = 0; j < feature.length; j++) {
						x[j] = new svm_node();
						x[j].index = j + 1;
						x[j].value = feature[j];
					}
					double c = svm.svm_predict(model, x);
					System.out.println(imagePath+":"+c);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

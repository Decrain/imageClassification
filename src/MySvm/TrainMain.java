package MySvm;

import java.io.IOException;

import Main.Common;
import SvmService.svm_train;


public class TrainMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException{
		String[] arg = { Common.rightFeaturePath+"|"+Common.wrongFeaturePath, // ���SVMѵ��ģ���õ����ݵ�·��
		 Common.modelPath }; // ���SVMͨ��ѵ������ѵ
		 System.out.println("........SVM���п�ʼ.........."); // ����һ��ѵ������
		 svm_train.main(arg); // ����
	}

}

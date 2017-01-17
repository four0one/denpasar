package com.plumeria.denpasar.util;

import java.util.Date;
import java.util.Random;

/**
 * 生成唯一ID
 *
 * @author Administrator
 *
 */
public class GenOrderCode {

	private static Date date = new Date();
	private static StringBuilder buf = new StringBuilder();

	public static synchronized String next() {

		buf.delete(0, buf.length());
		date.setTime(System.currentTimeMillis());
		String str = String.format("%1$tY%1$tm%1$td%1$tk%1$tM%1$tS%2$05d", date, genSixNum());
		return str;
	}

	/**
	 * 20位订单号
	* @Title: nextMill
	* @Description:
	* @param  @return
	* @return String    返回类型
	* @throws
	 */
	public static synchronized String nextMill() {
		String millTime=String.valueOf(System.currentTimeMillis());
//		System.out.println(millTime);
		int length=millTime.length()-10;
		String millTimeTemp=millTime.substring(length, millTime.length());
//		System.out.println(millTimeTemp);
		Random random = new Random();
		int a=random.nextInt(10);
		int b=random.nextInt(10);
		int c=random.nextInt(10);
		String str = String.valueOf(a)+String.valueOf(b)+String.valueOf(c)+millTimeTemp+String.valueOf(genSixNum());
		return str;
	}

	/**
	 *  随机生成6位数
	 *
	 * @return
	 */
	private static int genSixNum() {
		int[] array = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		Random rand = new Random();
		for (int i = 9; i > 1; i--) {
			int index = rand.nextInt(i);
			int tmp = array[index];
			array[index] = array[i - 1];
			array[i - 1] = tmp;
		}
		int result = 0;
		for (int i = 0; i < 6; i++)
			result = result * 10 + array[i];

		return result;
	}

	public static void main(String[] args) {
		System.out.println(GenOrderCode.next());
	}
}

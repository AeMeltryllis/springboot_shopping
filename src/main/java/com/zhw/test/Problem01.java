package com.zhw.test;

public class Problem01 {
	public static void main(String[] args) {
		Object object = new Object();
		new Thread(new Number(object)).start();
		;
		new Thread(new Character(object)).start();
		;
	}
}
 
class Number implements Runnable {
 
	private Object object;
 
	public Number(Object object) {
		this.object = object;
	}
 
	@Override
	public void run() {
		synchronized (object) {
			for (int i = 1; i < 53; i++) {
				if (i > 1 && i % 2 == 1) {
					System.out.print(" ");
				}
				System.out.print(i);
				if (i % 2 == 0) {
					// 先释放锁,唤醒其他线程,再使本线程阻塞
					object.notifyAll();
					try {
						object.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
 
class Character implements Runnable {
 
	private Object object;
 
	public Character(Object object) {
		this.object = object;
	}
 
	@Override
	public void run() {
		synchronized (object) {
			for (char i = 'A'; i <= 'Z'; i++) {
				System.out.print(i);
				// 先释放锁,唤醒其他线程,再使本线程阻塞
				object.notifyAll();
				if (i < 'Z') {  // 线程执行的最后一次不能堵塞，不然会一直堵塞下去。
					try {
						object.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}

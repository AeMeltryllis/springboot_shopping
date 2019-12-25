package com.zhw.test;

import java.util.LinkedList;
import java.util.List;

public class LinkStack {
        public static List link = new LinkedList();

        public  void add(int num){
            link.add(num);
            System.out.println("放入:"+num);
        }

        public  void pop(){
            System.out.println("弹出："+ link.remove(link.size()-1));}

        public  void foreach(){
            System.out.println("正在遍历元素");
        }
        public void removeAll(){
            for (int i = link.size()-1; i >=0 ; i--) {
                System.out.println("弹出："+ link.remove(i));}

            }




    public static void main(String[] args) {
        LinkStack linkStack = new LinkStack();
        linkStack.add(5);
        linkStack.add(4);
        linkStack.add(3);
        linkStack.pop();
        linkStack.foreach();
        linkStack.removeAll();
    }

}

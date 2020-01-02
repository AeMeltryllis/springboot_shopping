package com.zhw.test;

import java.util.ArrayList;
import java.util.List;

/**
 * 给定一副牌，每张牌上都写着一个整数。
 *
 * 此时，你需要选定一个数字 X，使我们可以将整副牌按下述规则分成 1 组或更多组：
 *
 * 每组都有 X 张牌。
 * 组内所有的牌上都写着相同的整数。
 * 仅当你可选的 X >= 2 时返回 true。
 *
 * 示例 1：
 *
 * 输入：[1,2,3,4,4,3,2,1]
 * 输出：true
 * 解释：可行的分组是 [1,1]，[2,2]，[3,3]，[4,4]
 * 示例 2：
 *
 * 输入：[1,1,1,2,2,2,3,3]
 * 输出：false
 * 解释：没有满足要求的分组。
 * 示例 3：
 *
 * 输入：[1]
 * 输出：false
 * 解释：没有满足要求的分组。
 * 示例 4：
 *
 * 输入：[1,1]
 * 输出：true
 * 解释：可行的分组是 [1,1]
 * 示例 5：
 *
 * 输入：[1,1,2,2,2,2]
 * 输出：true
 * 解释：可行的分组是 [1,1]，[2,2]，[2,2]
 *
 * 提示：
 *
 * 1 <= deck.length <= 10000
 * 0 <= deck[i] < 10000

 */
public class hasGroupsSizeX {
    public boolean hasGroupsSizeX(int[] deck) {
        if(deck.length <=2){
            System.out.println("无分组");
            return false;
        }
        return false;
    }

    /**
     * 由于我们要将 N 张卡片分成 K 堆每堆 X 张，一定有 N % X == 0。
     *
     * 那么，假设有 v 张写有数字 i 的卡片，每组数字 i 卡片有 X 张，那么一定有 v % X == 0。这是一个充要条件。
     *
     */
    class Solution {
        public boolean hasGroupsSizeX(int[] deck) {
            //数组长度
            int N = deck.length;
            int[] count = new int[10000];
            for (int c: deck)
                count[c]++;
            //维护一个桶，下标作为数字，桶值为出现次数
            List<Integer> values = new ArrayList();
            for (int i = 0; i < 10000; ++i)
                if (count[i] > 0)
                    values.add(count[i]);
                //使用标签跳过最外层循环，减少计算量
            search: for (int X = 2; X <= N; ++X)
                if (N % X == 0) {
                    for (int v: values)
                        if (v % X != 0)
                            continue search;
                    return true;
                }

            return false;
        }
    }

}

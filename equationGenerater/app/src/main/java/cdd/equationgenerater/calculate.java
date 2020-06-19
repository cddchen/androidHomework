package cdd.equationgenerater;

import android.content.Intent;
import android.util.Log;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

enum operation { add, sub }
public class calculate {
    public static Random random = new Random();
    static int rand(int l, int r) {
        return (int)((random.nextInt() % (r - l + 1) + (r - l + 1)) % (r - l + 1)) + l;
    }
    static int check(List<Integer> nums, List<operation> ops, int num_max) {
        int rhs = nums.get(0);
        for (int i = 0; i < ops.size(); ++i) {
            rhs += ops.get(i) == operation.add ? nums.get(i + 1) : -1 * nums.get(i + 1);
            if (rhs < 0 || rhs > num_max)
                return -1;
        }
        return rhs;
    }
    static elem equation(int op_min, int op_max, int num_max) {

        elem rhs = new elem(); int ans;
        while (true) {
            int op_cnt = rand(op_min, op_max);
            List<Integer> nums = new ArrayList<>();
            List<operation> ops = new ArrayList<>();
            nums.add(rand(1, num_max));
            for (int i = 0; i < op_cnt; ++i) {
                int parity = rand(0, 1);
                if (parity == 0)
                    ops.add(operation.add);
                else
                    ops.add(operation.sub);
                nums.add(rand(1, num_max));
            }
            if ((ans = check(nums, ops, num_max)) != -1) {
                rhs.eq = new String();
                rhs.eq += nums.get(0).toString();
                for (int i = 0; i < op_cnt; ++i) {
                    if (ops.get(i) == operation.add)
                        rhs.eq += "+";
                    else
                        rhs.eq += "-";
                    rhs.eq += nums.get(i + 1).toString();
                }
                break;
            }
        }
        rhs.ans = Integer.toString(ans);
        return rhs;
    }
}

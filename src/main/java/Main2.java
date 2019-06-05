import java.util.*;


//class ListNode {
//    int val;
//    ListNode next = null;
//
//    ListNode(int val) {
//        this.val = val;
//    }
//}

class Gift {
    int val;
    int hot;
    public Gift(int val, int hot) {
        this.val = val;
        this.hot = hot;
    }
}

public class Main2 {

    int maxVal = 0;
    int maxHot = 0;

    public static void main(String[] args) {
        int[] arrA = new int[]{200,600,100,180,300,450};
        int[] arrB = new int[]{6,10,3,4,5,8};
        int total = 1000;
        new Main2().soultion(total, arrA, arrB);
    }

    public void soultion(int total, int[] arrA, int[] arrB) {

        if(total < 0)
            return;

        Queue<Gift> maxHeap = new PriorityQueue<>(new Comparator<Gift>() {
            @Override
            public int compare(Gift o1, Gift o2) {
                if(o2.hot/o2.val > o1.hot/o1.val) {
                    return 1;
                }
                else if (o2.hot/o2.val == o1.hot/o1.val) {
                    if (o2.val < o1.val)
                        return 1;
                    else if (o2.val == o1.val)
                        return 0;
                    else
                        return -1;
                }
                else {
                    return -1;
                }
            }
        });

        ArrayList<Gift> giftArray = new ArrayList<>();
        ArrayList<Integer> list = new ArrayList<>();

        Gift gift = null;

        for(int i = 0; i < arrA.length; i++) {
            gift = new Gift(arrA[i], arrB[i]);
            maxHeap.add(gift);
            giftArray.add(gift);
        }
        Collections.sort(giftArray, new Comparator<Gift>() {
            @Override
            public int compare(Gift o1, Gift o2) {
                if(o2.hot/o2.val > o1.hot/o1.val) {
                    return 1;
                }
                else if (o2.hot/o2.val == o1.hot/o1.val) {
                    if (o2.val < o1.val)
                        return 1;
                    else if (o2.val == o1.val)
                        return 0;
                    else
                        return -1;
                }
                else {
                    return -1;
                }
            }
        });

        while(!maxHeap.isEmpty()) {
            if(maxVal > total)
                break;

            gift = maxHeap.poll();

            maxVal += gift.val;
            maxHot += gift.hot;
            list.add(gift.hot);


        }

        System.out.println(maxVal - gift.val);
        System.out.println(maxHot - gift.hot);
        return;

    }
}



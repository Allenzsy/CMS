package test;

import java.util.*;

public class Soultion {

    private Deque<Integer> byteKid = new ArrayDeque<>();
    private Deque<Integer> danceKid = new ArrayDeque<>();
    private Deque<Integer> table = new ArrayDeque<>();

    public void solve() {

        Scanner sc = new Scanner(System.in);
        int[] a = {10,2,5,6,13,11,11,4,10,8,12,5,4,1,8,1,7,12,4,13,6,9,9,9,5,7};
        int[] b = {6,3,13,8,2,3,7,3,2,2,12,11,10,6,10,1,1,12,3,5,7,11,13,4,8,9};

        for(int i : a) {
            byteKid.add(i);
        }
        for(int i : b) {
            danceKid.add(i);
        }
        trainGame();
    }

    public void trainGame() {

        int[] puke = new int[14]; //0 空余
        List<Integer> byteKidNum = new ArrayList<>();
        List<Integer> danceKidNum = new ArrayList<>();

        while(!byteKid.isEmpty() && !danceKid.isEmpty()) {

            Integer card = byteKid.poll();
            if(card != null) {
                if(puke[card] == 0) {
                    table.push(card);
                    puke[card] = 1; //标记一下
                } else {
                    byteKidNum.add(card);
                    puke[card] = 0;
                    while(table.peek() != card) {
                        puke[table.peek()] = 0;
                        byteKidNum.add(table.pop());
                    }
                    byteKidNum.add(table.pop()); // 把相同的那个也添加了
                }
            }
            card = danceKid.poll();
            if(card != null) {
                if(puke[card] == 0) {
                    table.push(card);
                    puke[card] = 1; //标记一下
                } else {
                    danceKidNum.add(card);
                    puke[card] = 0;
                    while(table.peek() != card) {
                        puke[table.peek()] = 0;
                        danceKidNum.add(table.pop());
                    }
                    danceKidNum.add(table.pop()); // 把相同的那个也添加了
                }
            }
        }

        if(byteKidNum.size() > danceKidNum.size()) {
            System.out.println("Byte");
        }
        else if (byteKidNum.size() < danceKidNum.size()) {
            System.out.println("Dance");
        }
        else {
            System.out.println("Draw");
        }
    }

    public static void main(String[] args) {
//        new Soultion().solve();
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> ns = new ArrayList<>();
//        String
//        while(!(s = sc.nextLine()).equals("")) {
//            ns.add(scanner.nextLine());
//        }
//        for(String s : ns) {
//            System.out.println(s);
//        }

    }



}
